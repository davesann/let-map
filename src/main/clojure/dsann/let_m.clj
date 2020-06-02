(ns dsann.let-m
  (:require [dsann.doc-strings :as doc]))

;; --------------------------------------------------
;; Examples
(def api-docs
  '{let-m
    {:examples
     [["Takes a list of name-value pairs and returns a map {:name :value ...}"
       (let-m a 1 b 2)
       {:a 1 :b 2}]

      ["Works like let"
       (let-m a 1 b (inc a))
       {:a 1 :b 2}]

      ["Is a bit nicer than ..."
       (let [a 1 b (inc a)]
         {:a a :b b})]

      ["Destructuring works"
       (let [a-map {:a 1 :b 2}]
         (let-m {:keys [a]} a-map
                c           (inc a)))
       {:a 1 :c 2}]

      ["Names that begin with _ are removed from the result"
       (let-m _a 1 b (inc _a))
       {:b 2}]]}

    assoc-m
    {:examples
     [["Like let-m, but assocs into the supplied map. Note that the values in the supplied map are NOT available in calcs"
       (let [a-map {:a 1}]
         (assoc-m a-map _b 2 c (* _b 2)))
       {:a 1 :c 4}]]}

    sym-m
    {:examples
      [["creates a map from a list of symbols"
        (let [a 1 b 2]
          (sym-m a b))
        {:a 1 :b 2}]]}

    assoc-syms
    {:examples
      [["like sym-m but assocs to supplied map"
        (let [m {:a 1 :b 2}
              a 4
              c 3]
          (assoc-syms m a c))
        {:a 4 :b 2 :c 3}]]}})


;;--------------------------------------------------
;; general helpers

;; copied from clojure.core - marked as private there.
(defmacro assert-args
  [& pairs]
  `(do (when-not ~(first pairs)
         (throw (IllegalArgumentException.
                  (str (first ~'&form) " requires " ~(second pairs) " in " ~'*ns* ":" (:line (meta ~'&form))))))
     ~(let [more (nnext pairs)]
        (when more
          (list* `assert-args more)))))


;; --------------------------------------------------
;; helpers for arg name filtering

(defn _key?
  "begin with _"
  [sym]
  (= \_ (nth (name sym) 0)))

(defn clojure-destructure-id? 
  "matches clojure's temporary destructure names" 
  [sym]
  (let [s (name sym)]
    (re-matches #"(vec|map)__\d+" s)))

(def xform (comp
              (remove _key?)          ;; exclude unwanted keys
              (remove clojure-destructure-id?)  ;; exclude destructure names
              (map #(-> [(keyword %) %]))))  ;; map entry is [keyword symbol]


;; --------------------------------------------------
(defmacro symbol-map [args]
  (into {} xform args))

(defmacro let-m
  "Takes a list of name value pairs and returns a map {:name value}"
  [& args]
  (assert-args (even? (count args)) "an even number of forms")
  (let [dargs (destructure (vec args))
        vars  (take-nth 2 dargs)]
    `(let [~@dargs]
       (symbol-map ~vars))))

(defmacro assoc-m
  "Like let-m but takes an input map and assocs the values"
  [m & args]
  `(into ~m (let-m ~@args)))

(defmacro sym-m
  "creates list from map of symbol names"
  [& syms]
  (let [args (mapcat vector syms syms)]
    `(symbol-map ~args)))

(defmacro assoc-syms
  "Like sym-m but assocs symbols into the supplied map"
  [m & syms]
  `(into ~m (sym-m ~@syms)))

(doc/replace-ns-docs api-docs)
