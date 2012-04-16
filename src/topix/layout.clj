
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
    [:div.container
      [:div.span12 
        [:h1 title]
        [:div.content body]
        [:div.footer "by rod"]]
      (page/include-js "/js/bootstrap.min.js")]])

;; Public

(defn index-page []
  (layout "Welcome to Topix!"
    (form/form-to {:class "well"} [:get "show"]
      (form/label "topic" "Choose a topic...")
      (form/text-field {} "topic")
      (form/submit-button {:class "btn"} "Show Topic"))))

(defn show-page [topic]
  (layout topic
    (form/form-to {:class "well"} [:post "submit"]
      (form/label "body" "Enter some text...")
      (form/text-area {} "body")
      [:label.checkbox 
        (form/check-box {} "hit")
        "Matches topic?"]
      (form/submit-button {:class "btn"} "Add Example"))))

