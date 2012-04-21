
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
              [:li.active [:a {:href "/"} "Home"]]
              [:li [:a {:href "/reload" :title "Reload Topix Data"} "Reload Data"]]]]]]]
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
      [:p "To give it a try just paste some text into the field below and click submit."]]
    (form/form-to {} [:post "topics"]
      [:fieldset
        (form/label "text" "Enter some text")
        (form/text-area {} "text")]
      (form/submit-button {:class "btn btn-primary"} "Analyse Text!"))
    [:h2 "Topics..."
      [:ul.topics
        (for [topic topics]
          [:li topic])]]))

(defn relevance-page [text relevant-topics]
  (layout "Matched Topics"
    [:p.text-to-score (str "\"" text "\"")]
    [:ul.topics
      (for [[topic score] relevant-topics]
        (let [percentage (double (* 100 score))]
          [:li
            [:span.name topic]
            [:div.score-wrap
              [:div.score {:style (str "width:" percentage "%")}]
              [:span percentage]
              [:div.clearer]]]))]
      [:div.back
        [:a.btn.btn-primary {:href "/"} "Back"]]))

