
(ns topix.text
  (:require [clojure.string :as string]))

(def data (ref {}))

(defn split-words
  "Splits a string into words to score"
  [text]
  (string/split text #"\s+"))

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

;; Public

(defn analyse [topic text hit]
  "Analyses a peice of text and updates the scores in our data"
  (let [words (split-words text)
        word-scores (map (partial score-word topic hit) words)]
    (doseq [word words]
      (update-word topic word hit))))

(defn relevance [topic text]
  )

(defn topics [text]
  )

