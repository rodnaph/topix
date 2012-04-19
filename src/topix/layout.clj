
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

;; Public

(defn index-page [topics]
  (layout "Welcome to Topix!"
    [:div.intro
      [:p "Topix is a very simple Clojure web application for doing Bayesian
           analysis on text, and indicating how relevant it is to certain topics
           that it has been trained with."]
      [:p "As well as trying to match text to a given topic, it will also return
           results for other similar topics"]]
    (form/form-to {} [:get "score"]
      [:fieldset
        (form/label "text" "And enter some text")
        (form/text-area {} "text")]
      [:fieldset
        (form/label "topic" "Choose a topic to match for relevance")
        [:select {:name "topic"}
          (form/select-options topics)]]
      (form/submit-button {:class "btn btn-primary"} "Analyse Text!"))))

(defn relevance-page [topic text score relevant-topics]
  (let [percentage (double (* 100 score))]
    (layout (str "Relevance to " topic)
      [:p.text-to-score (str "\"" text "\"")]
      [:div.score-wrap
        [:div.score {:style (str "width:" percentage "%")}]
        [:span percentage]
        [:div.clearer]]
      [:ul.topics
        (for [t (filter #(not (= % topic)) relevant-topics)]
          [:li
            (form/form-to {} [:get "score"]
              (form/hidden-field "topic" t)
              (form/hidden-field "text" text)
              (form/submit-button {:class "btn btn-info"} t))])]
      [:div.back
        [:a.btn.btn-primary {:href "/"} "Back"]])))

