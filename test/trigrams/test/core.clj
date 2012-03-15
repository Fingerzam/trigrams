(ns trigrams.test.core
  (:use trigrams.core)
  (:use midje.sweet))

(facts "of trigrams"
  (trigrams "foo bar baz quux")     => {["foo" "bar"] ["baz"], ["bar" "baz"] ["quux"]}
  (trigrams "foo bar baz
            quux")     => {["foo" "bar"] ["baz"], ["bar" "baz"] ["quux"]}
  (trigrams "foo  bar baz")         => {["foo" "bar"] ["baz"]}
  (trigrams "foo bar foo bar quux") => (contains {["foo" "bar"] ["foo" "quux"], ["bar" "foo"] ["bar"]})
  (trigrams "foo bar")              => {}
  (trigrams "")                     => {})

(facts "of generation"
  (generate-words (trigrams "foo bar baz") [0 0 0])               => "foo bar baz"
  (generate-words (trigrams "foo bar foo bar quux") [0 0 0.99 0]) => "bar foo bar quux"
  (generate-words (trigrams "foo bar foo bar quux") [0.99 0.99])  => "foo bar quux"
  (generate-words (trigrams "") [])                               => "")
