
(ns topix.test.text
  (:use topix.text
        midje.sweet))

(facts "about splitting text into word groups"
  (split-words "foo bar baz bazzle") => ["foo" "bar" "baz" "bazzle"])

