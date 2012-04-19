
(ns topix.trainer
  (:use cheshire.core)
  (:require [topix.init :as init]
            [topix.data :as data]
            [monger.core :only [connect! set-db! get-db]]
            [monger.collection :as db]))

(defn- train-with
  "Loading training data from the specified file"
  [file]
  (let [[_ topic] (re-matches #"(\w+).json" file)
        data (parse-string (slurp (str "data/" file)) true)]
    (doseq [tweet data]
      (data/analyse topic (:text tweet) true))))

;; Public

(defn run
  []
  (init/mongo)
  (let [data (java.io.File. "data")]
    (db/remove "scores")
    (doseq [file (.list data)]
      (println "Loading: " file)
      (train-with file))))

