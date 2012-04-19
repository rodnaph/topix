
(ns topix.data
  (:require [topix.text :as text]
            [monger.collection :as db]))

(def ^{:doc "In memory data store, is currently the canonical place for data and uses STM to ensure
             consistency before pushing to durable store" :dynamic true} *data* (ref {}))

;; Word scoring

(defn word-score
  "Returns the current scoring data for the word in the topic"
  [topic word]
  (get (get (deref *data*) topic {})
       word nil))

(defn inc-if
  "Increment the value if the condition is true"
  [condition value]
  (if condition (inc value) value))

(defn score-data
  "Updates the data to increase the total, and possibly the number
   of hits if this is a hit"
  [hit curr]
  (let [total (get curr :total 0)
        hits (get curr :hits 0)
        score {:total (inc total)
               :hits (inc-if hit hits)}]
    score))

;; Updating memory and datastore

(defn update-data
  "Updates our memory store with the new word score"
  [topic word hit]
  (dosync 
    (alter *data*
      #(update-in % [topic word]
                  (partial score-data hit)))))

(defn- update-mongo
  "Updates the db with the new word score"
  [topic word]
  (let [match {:topic topic :word word}
        doc (merge match (word-score topic word))]
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

;; Public

(defn analyse 
  "Analyses a peice of text and updates the scores in our data"
  [topic text hit]
  (doseq [word (text/explode text)]
    (update-word topic word hit)))

(defn relevance 
  "Analyses the text and returns a relavance score between 0 (bad) and 1 (good)"
  [topic text]
  (let [word-scores (map (partial word-score topic) (text/explode text))
        relevance-scores (map to-relevance word-scores)]
    (/ (reduce + relevance-scores)
       (count relevance-scores))))

(defn reload
  "Reloads data from the durable mongodb store"
  []
  (doseq [{:keys [topic word total hits]} (db/find-maps "scores")]
    (dosync
      (alter *data*
        #(assoc-in % [topic word] 
                   {:total total :hits hits})))))

