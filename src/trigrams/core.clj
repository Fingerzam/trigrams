(ns trigrams.core
  (:require [clojure.string :as str]))

(defn triples [items]
  (map vector items (rest items) (rest (rest items))))

(defn trigrams [string]
  (let [words (str/split string #"\s+")
        word-triples (triples words)
        key-value-pairs (map (fn [[x y z]] [[x y] [z]]) word-triples)
        merge-follow-words (fn [trigrams [pair third]]
                             (assoc trigrams pair
                                    (concat (get trigrams pair) third)))]
    (reduce merge-follow-words (sorted-map) key-value-pairs)))

(defn random-element [elems random]
  (get (vec elems) (int (Math/floor (* random (count elems))))))

(defn random-follow [trigrams [fst snd :as pair] rands]
  (loop [previous-pair pair
         [rand & rands] rands
         result []]
    (let [follow-ups (trigrams previous-pair)]
      (if (empty? follow-ups)
        result
        (let [random-follow-up (random-element follow-ups rand)]
          (recur [(second previous-pair) random-follow-up]
                 rands
                 (conj result random-follow-up)))))))

(defn random-key [trigrams random]
  (random-element (keys trigrams) random))

(defn generate-words [trigrams [rand1 & rands]]
  (if (empty? trigrams)
    ""
    (let [pair (random-key trigrams rand1)]
      (str/join " " (concat pair (random-follow trigrams pair rands))))))

(defn randoms []
  (repeatedly rand))

