
(ns topix.test.text
  (:use topix.text
        midje.sweet))

(facts "about splitting text into word groups"
  (explode "foo bar baz" 2) =>
       (contains ["foo" "baz" "bar" "foo bar" "bar baz"] :in-any-order)
  (explode "foo bar baz bazzle") => (contains ["foo" "bar" "baz" "bazzle"]))

(facts "about removal of stopwords"
  (count (explode "this is the best")) => 1
  (explode "this is the best") => (contains ["best"]))

(facts "about converting the case of words to store"
  (explode "baZZle") => ["bazzle"])

(facts "about removing invalid data from text when splitting"
  (explode "http://snipurl/7pin60ub bazzle") => ["bazzle"]
  (explode "http://t.co/7pin60ub bazzle") => ["bazzle"])

