
(defproject topix "0.0.3"
  :description "Simple baysian analysis"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [noir "1.3.0-beta2"]
                 [com.novemberain/monger "1.0.0-beta4"]
                 [cheshire "4.0.0"]
                 [getweets "1.0.0"]]
  :dev-dependencies [[midje "1.3.2-alpha1"]
                     [lein-midje "1.0.9"]
                     [lein-noir "1.2.1"]
                     [com.stuartsierra/lazytest "1.2.3"]]
  :repositories {"stuart" "http://stuartsierra.com/maven2"}
  :main topix.core)

