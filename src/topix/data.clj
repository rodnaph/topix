
(ns topix.data
  (:require [monger.collection :as db]
            [bayes.core :as bayes]))

(def ^{:doc "In memory data store, is currently the canonical place for data and uses STM to ensure
             consistency before pushing to durable store" :dynamic true} *data* (ref {}))

;; Topic Analysis

(defn- to-topics
  [acc word]
  (let [topics (get @*data* word {})]
    (merge acc topics)))

(defn- by-value
  [[_ a] [_ b]]
  (> a b))

(defn- to-relevance
  [text [topic _]]
  (vector topic 
          (bayes/probability topic text)))

;; Mongo DB

(defn- to-score-record
  "Converts an individual topic score to a mongo db record"
  [word [topic score]]
  { :topic topic
    :word word 
    :score score })

(defn- to-score-records
  "Converts in memory data to mongo db records"
  [[word topics]]
  (map (partial to-score-record word) topics))

;; Public

(defn analyse 
  "Analyses a peice of text and updates the scores in our data"
  [topic text]
  (dosync
    (ref-set *data*
      (bayes/train topic text @*data*))))

(defn relevance 
  "Analyses the text and returns a relavance score between 0 (bad) and 1 (good)"
  [topic text]
  (bayes/probability topic text @*data*))

(defn relevant-topics
  [text]
  (->> (bayes/features text)
       (reduce to-topics {}) 
       (map (partial to-relevance text))
       (sort by-value)))

(defn all-topics
  "Returns all current topics (fetched from mongo)"
  []
  (db/distinct "features" "topic"))

(defn reset
  "Resets all scores in mongodb"
  []
  (db/remove "categories")
  (db/remove "features"))

(defn reload
  "Reloads data from the durable mongodb store"
  []
  (dosync
    (doseq [{:keys [topic word score]} (db/find-maps "features")]
      (alter *data*
        #(assoc-in % [:features word topic] score)))
    (doseq [{:keys [category score]} (db/find-maps "categories")]
      (alter *data*
        #(assoc-in % [:categories category] score)))))

(defn commit
  "Commit in-memory data to mongodb, replacing it"
  []
  (reset)
  (doseq [records (map to-score-records (:features @*data*))]
    (doseq [record records]
      (db/insert "features" record)))
  (doseq [[category score] (:categories @*data*)]
    (db/insert "categories" {:category category
                             :score score})))

