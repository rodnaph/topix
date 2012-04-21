
(ns topix.core
  (:use noir.core)
  (:require [topix.init :as init]
            [topix.data :as data]
            [topix.layout :as layout]
            [noir.response :as res]))

;; Routes

(defpage "/" [] 
  (layout/index-page (data/all-topics)))

(defpage [:post "/topics"] {:keys [text]}
  (layout/relevance-page
    text 
    (data/relevant-topics text)))

(defpage "/reload" []
  (data/reload)
  (res/redirect "/"))

;; Main

(defn -main [& args]
  (init/mongo)
  (data/reload)
  (init/server))

