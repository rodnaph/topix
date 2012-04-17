
(ns topix.core
  (:use noir.core)
  (:require [topix.text :as text]
            [topix.layout :as layout]
            [noir.server :as server]))

(defpage "/" [] 
  (layout/index-page))

(defpage "/show" {:keys [topic]}
  (layout/show-page topic))

(defpage [:post "/submit"] {:keys [topic text hit]}
  (text/analyse topic text hit)
  (layout/submit-page topic text hit))

(defpage "/score" {:keys [topic text]}
  (layout/relevance-page
    topic text (text/relevance topic text)))

(defn -main [& args]
  (server/start 8080))

