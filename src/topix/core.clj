
(ns topix.core
  (:use noir.core)
  (:require [topix.init :as init]
            [topix.data :as data]
            [topix.layout :as layout]))

;; Routes

(defpage "/" [] 
  (layout/index-page (data/all-topics)))

(defpage [:post "/topics"] {:keys [text]}
  (layout/relevance-page
    text 
    (data/relevant-topics text)))

;; Main

(defn -main [& args]
  (init/mongo)
  (data/reload)
  (init/server))

