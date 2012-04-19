
(ns topix.text
  (:require [clojure.string :as string]))

(def ^{:doc "A list of stopwords that are removed from text"} stopwords #{
  "this" "is" "the" "a" "to" "not" "what" "in" "by"
})

(defn- not-stopword
  [word]
  (not
    (some #{word} stopwords)))

(defn- not-url
  [url]
  (not
    (re-matches #"http://t.co/\w+" url)))

;; Public

(defn explode
  "Splits a string into words to score, optionally also in chunked groups"
  ([text] (explode text 1))
  ([text size] 
   (->> (string/split (.toLowerCase text) #"\s+")
        (filter not-url)
        (filter not-stopword))))

