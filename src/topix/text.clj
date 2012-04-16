
(ns topix.text
  (:require [clojure.string :as string]))

(defn split-words
  [text]
  (string/split text #"\s+"))

;; Public

(defn analyse [topic text hit]
  (let [words (split-words text)]
    ))

(defn relevance [topic text]
  )

(defn topics [text]
  )

