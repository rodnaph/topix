
(ns topix.test.data
  (:use topix.data
        midje.sweet))

(facts "about scoring data"
  (score-data false {:total 1 :hits 1}) => {:total 2 :hits 1}
  (score-data true {:total 1 :hits 1}) => {:total 2 :hits 2}
  (score-data true nil) => {:total 1 :hits 1}
  (score-data false nil) => {:total 1 :hits 0})

(facts "about updating word data"
  (binding [*data* (ref {})]
    (update-data "foo" "bar" true) => {"foo" {"bar" {:total 1 :hits 1}}}
    (update-data "foo" "bar" false) => {"foo" {"bar" {:total 2 :hits 1}}}
  ))

(facts "about calculating relevance info"
  (to-relevance {:total 40 :hits 20}) => 1/2
  (to-relevance nil) => 0)

