
(ns topix.layout
  (:use noir.core)
  (:require [hiccup.page :as page]
            [hiccup.form :as form]))

(defpartial layout [title & body]
  [:head
    [:title "T0P1X"]
    (page/include-css "/css/bootstrap.min.css")
    (page/include-css "/css/default.css")]
  [:body
    [:div.main
      [:div.span12 body
        [:h1 title]
        [:div.footer "by rod"]]
      (page/include-js "/js/bootstrap.min.js")]])

;; Public

(defn index-page [_]
  (layout "Welcome to Topix!"
    (form/form-to [:get "/topic"])))

