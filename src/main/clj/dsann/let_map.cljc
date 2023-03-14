(ns dsann.let-map
  #?(:clj (:refer-clojure :exclude [destructure]))
  (:require
     [net.cgrand.macrovich      :as macros]
     [dsann.macros.helpers      :refer [assert-args]]
     [dsann.let-map.destructure :refer [destructure]])
  ; cljs must self refer macros
  #?(:cljs (:require-macros
             [dsann.let-map :refer [let-map let-assoc sym-map assoc-syms]])))

;; --------------------------------------------------
;; helpers for arg name filtering

(defn ^:private _key?
  "filter symbols that begin with _"
  [sym]
  (= \_ (nth (name sym) 0)))

(defn ^:private clojure-destructure-id?
  "true if sym matches clojure's internal temporary destructure names"
  [sym]
  (let [s (name sym)]
    (re-find #"^(vec|map|seq|first|p)__" s)))

(macros/deftime

  (defmacro assoc-syms
    "like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test"
    [m & syms]
    (let [ksym (vec (eduction
                     (comp
                       (remove _key?)                   ;; exclude unwanted keys
                       (remove clojure-destructure-id?) ;; exclude destructure names
                       (map #(-> [(keyword %) %])))     ;; map entry is [keyword symbol]
                     syms))]
      `(into ~m ~ksym)))

  (defmacro sym-map
    "Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test"
    [& syms]
    `(assoc-syms {} ~@syms))

  (defmacro let-assoc
    "Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test"
    [m & args]
    (assert-args (even? (count args)) "an even number of forms")
    (let [dargs (destructure args)
          vars  (take-nth 2 dargs)]
      `(let [~@dargs]
         (assoc-syms ~m ~@vars))))

  (defmacro let-map
    "Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
      For examples, see: dsann.let-map-test"
    [& args]
    `(let-assoc {} ~@args))

  nil)

(comment
    (-> '(assoc-syms {} a)
        macroexpand-1)

    (let [a 1]
      (sym-map a))

    (let [a 1]
      (into {} (list [:a a])))
  nil)
