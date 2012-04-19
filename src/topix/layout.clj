
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
    [:div.navbar.navbar-fixed-top
      [:div.navbar-inner
        [:div.container
          [:div.nav-collapse
            [:ul.nav
              [:li.active [:a {:href "/"} "HOME"]]]]]]]
    [:div.container.main-container
      [:div.span12 
        [:h1 title]
        [:div.content body]
        [:div.footer "by rod"]]
      (page/include-js "/js/bootstrap.min.js")]])

(defpartial submit-form [topic]
  (form/form-to {:class "well"} [:post "submit"]
    (form/hidden-field "topic" topic)
    (form/label "text" "Enter some text...")
    (form/text-area {} "text")
    [:label.checkbox 
      (form/check-box {} "hit")
      "Matches topic?"]
    (form/submit-button {:class "btn"} "Add Example")))

(defpartial score-form [topic]
  (form/form-to {:class "well"} [:get "score"]
    (form/hidden-field "topic" topic)
    (form/label "text" "Enter text to score...")
    (form/text-area {} "text")
    (form/submit-button {:class "btn"} "Score Text")))

;; Public

(defn index-page [topics]
  (layout "Welcome to Topix!"
    (form/form-to {:class "well"} [:get "score"]
      [:fieldset
        (form/label "text" "And enter some text")
        (form/text-area {} "text")]
      [:fieldset
        (form/label "topic" "Choose a topic to match for relevance")
        [:select {:name "topic"}
          (form/select-options topics)]]
      (form/submit-button {:class "btn btn-primary"} "Analyse Text!"))))

(defn show-page [topic]
  (layout topic
    (submit-form topic)
    (score-form topic)))


(defn submit-page [topic text hit]
  "DONE")

(defn relevance-page [topic text score]
  (let [percentage (double (* 100 score))]
    (layout (str "Relevance to " topic)
      [:p.text-to-score text]
      [:div.score-wrap
        [:div.score {:style (str "width:" percentage "%")}]
        [:span percentage]
        [:div.clearer]])))

