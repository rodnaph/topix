
(ns topix.text
  (:require [clojure.string :as string]))

(def data (ref {}))

(defn split-words
  "Splits a string into words to score"
  [text]
  (string/split text #"\s+"))

;; Word scoring

(defn word-score
  "Returns the current scoring data for the word in the topic"
  [topic word]
  (get (get @data topic {})
       word nil))

(defn score-data
  "Updates the data to increase the total, and possibly the number
   of hits if this is a hit"
  [hit curr]
  (let [total (get curr :total 0)
        hits (get curr :hits 0)]
    {:total (inc total)
     :hits (if hit (inc hits) hits)}))

(defn update-word
  "Updates this words score for the topic in the data"
  [topic word hit]
  (dosync 
    (alter data 
      (fn [curr-data]
        (update-in curr-data 
                   [topic word]
                   (partial score-data hit))))))

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
  (doseq [word (split-words text)]
    (update-word topic word hit)))

(defn relevance 
  "Analyses the text and returns a relavance score between 0 (bad) and 1 (good)"
  [topic text]
  (let [word-scores (map (partial word-score topic) (split-words text))
        relevance-scores (map to-relevance word-scores)]
    (/ (reduce + relevance-scores)
       (count relevance-scores))))

(defn topics [text]
  )

