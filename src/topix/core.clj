
(ns topix.core
  (:use noir.core)
  (:require [topix.layout :as layout]
            [noir.server :as server]))

(defpage "/" [] 
  (layout/index-page))

(defpage "/show" {:keys [topic]}
  (layout/show-page topic))

(server/start 8080)

