
(ns topix.data
  (:require [topix.text :as text]
            [monger.collection :as db]))

(def ^{:doc "In memory data store, is currently the canonical place for data and uses STM to ensure
             consistency before pushing to durable store" :dynamic true} *data* (ref {}))

;; Word scoring

(defn word-score
  "Returns the current scoring of the word for the specified topic"
  [topic word]
  (get (get (deref *data*) word {})
       topic 0))

(defn score-data
  "Updates the data to increase the total, and possibly the number
   of hits if this is a hit"
  [hit curr]
  (if curr (inc curr) 1))

;; Updating memory and datastore

(defn update-data
  "Updates our memory store with the new word score"
  [topic word hit]
  (dosync 
    (alter *data*
      #(update-in % [word topic]
                  (partial score-data hit)))))

(defn- update-mongo
  "Updates the db with the new word score"
  [topic word]
  (let [match {:topic topic :word word}
        doc (merge match {:score (word-score topic word)})]
    (db/update "scores" match doc :upsert true)))

(defn update-word
  "Updates this words score for the topic in the data"
  [topic word hit]
  (update-data topic word hit)
  (update-mongo topic word))

;; Word relevance analysis

(defn to-relevance
  [score]
  (if (nil? score)
      0
      (/ (:hits score)
         (:total score))))

(defn map-total
  "Calculates totals of integer map values"
  [acc [_ x]] 
  (+ acc x))

(defn relevance-score
  [topic word]
  (let [scores (get @*data* word {})
        total (reduce map-total 0 scores)
        topic-score (get scores topic 1)]
    (if (= 0 total) 0
        (/ topic-score total))))

;; Public

(defn analyse 
  "Analyses a peice of text and updates the scores in our data"
  [topic text hit]
  (doseq [word (text/explode text)]
    (update-word topic word hit)))

(defn relevance 
  "Analyses the text and returns a relavance score between 0 (bad) and 1 (good)"
  [topic text]
  (let [words (text/explode text)
        word-scores (map (partial relevance-score topic) words)]
    (prn "Scores: " (interleave words word-scores))
    (/ (reduce + word-scores)
       (count word-scores))))

(defn reload
  "Reloads data from the durable mongodb store"
  []
  (doseq [{:keys [topic word score]} (db/find-maps "scores")]
    (dosync
      (alter *data*
        #(assoc-in % [word topic] score)))))

