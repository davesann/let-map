(ns dsann.let-map
  #?(:cljs (:refer-clojure :exclude [destructure]))
  (:require
    [net.cgrand.macrovich      :as macros]
    [dsann.macros.helpers      :refer [assert-args]]
    #?(:cljs [dsann.let-map.destructure :refer [destructure]])) 
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
  (defmacro sym-map
    "Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test"
    [& syms]
    (into {}
        (comp
          (remove _key?)                   ;; exclude unwanted keys
          (remove clojure-destructure-id?) ;; exclude destructure names
          (map #(-> [(keyword %) %])))     ;; map entry is [keyword symbol]
        syms))

  (defmacro let-map
    "Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
     For examples, see: dsann.let-map-test"
    [& args]
    (assert-args (even? (count args)) "an even number of forms")
    (let [dargs (destructure args)
          vars  (take-nth 2 dargs)]
      `(let [~@dargs]
         (sym-map ~@vars))))


  (defmacro let-assoc
    "Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test"
    [m & args]
    `(merge ~m (let-map ~@args)))


  (defmacro assoc-syms
    "like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test"
    [m & syms]
    `(merge ~m (sym-map ~@syms)))

  nil)

