
(ns topix.core
  (:use noir.core)
  (:require [topix.layout :as layout]
            [noir.server :as server]))

(defpage "/" [] layout/index-page)

(server/start 8080)

