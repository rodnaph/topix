
(ns topix.core
  (:use noir.core)
  (:require [topix.text :as text]
            [topix.layout :as layout]
            [noir.server :as server]
            [monger.core :as db]))

(defn- getenv
  "Return value of environment variable, or default if not set"
  [prop default]
  (or (System/getenv prop) default))

(defn- mongo-init []
  (db/connect!)
  (db/set-db! (db/get-db (getenv "TOPIX_MONGO_DB" "topix")))
  (text/reload-data))

(defn- server-init []
  (server/start (Integer/parseInt
                  (getenv "TOPIX_PORT" 8080))))

;; Routes

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

;; Main

(defn -main [& args]
  (mongo-init)
  (server-init))

