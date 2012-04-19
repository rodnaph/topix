
(ns ^{:doc "Tools for initialising topix"}
  topix.init
  (:require [monger.core :as mongo]
            [noir.server :as server]))

(defn- getenv
  "Return value of environment variable, or default if not set"
  [prop default]
  (or (System/getenv prop) default))

;; Public

(defn mongo 
  "Initialise the MongoDB connection"
  []
  (mongo/connect!)
  (mongo/set-db! (mongo/get-db (getenv "TOPIX_MONGO_DB" "topix"))))

(defn server 
  "Initialise the HTTP server"
  []
  (server/start (Integer/parseInt
                  (getenv "TOPIX_PORT" "8001"))))

