
(ns topix.core
  (:use noir.core)
  (:require [topix.init :as init]
            [topix.data :as data]
            [topix.layout :as layout]))

;; Routes

(defpage "/" [] 
  (layout/index-page (data/all-topics)))

(defpage "/score" {:keys [topic text]}
  (layout/relevance-page
    topic text 
    (data/relevance topic text)
    (data/relevant-topics text)))

;; Main

(defn -main [& args]
  (init/mongo)
  (data/reload)
  (init/server))

