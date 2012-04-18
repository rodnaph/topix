
(ns topix.test.text
  (:use topix.text
        midje.sweet))

(facts "about splitting text into word groups"
;  (split-words "foo bar baz" 2) =>
;       (contains ["foo" "baz" "bar" "foo bar" "bar baz"] :in-any-order)
  (split-words "foo bar baz bazzle") => (contains ["foo" "bar" "baz" "bazzle"]))

(facts "about scoring data"
  (score-data false {:total 1 :hits 1}) => {:total 2 :hits 1}
  (score-data true {:total 1 :hits 1}) => {:total 2 :hits 2}
  (score-data true nil) => {:total 1 :hits 1}
  (score-data false nil) => {:total 1 :hits 0})

(facts "about updating words"
  (binding [*data* (ref {})]
    (update-word "foo" "bar" true) => {"foo" {"bar" {:total 1 :hits 1}}}
    (update-word "foo" "bar" false) => {"foo" {"bar" {:total 2 :hits 1}}}
  ))

(facts "about calculating relevance info"
  (to-relevance {:total 40 :hits 20}) => 1/2
  (to-relevance nil) => 0)

