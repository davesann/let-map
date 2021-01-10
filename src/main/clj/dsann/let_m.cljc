(ns dsann.let-m
  (:require
    [dsann.macros.helpers :refer [assert-args]]
    [net.cgrand.macrovich :as macros])
  ; cljs must self refer macros
  #?(:cljs (:require-macros
             [dsann.let-m :refer [let-m assoc-m sym-m assoc-syms]])))

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
  (defmacro sym-m
    "Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-m-test"
    [& syms]
    (into {}
        (comp
          (remove _key?)                   ;; exclude unwanted keys
          (remove clojure-destructure-id?) ;; exclude destructure names
          (map #(-> [(keyword %) %])))     ;; map entry is [keyword symbol]
        syms))

  (defmacro let-m
    "Takes a list of name-value pairs and returns a map: (let-m a 1 ...) => {:a 1 ...}
     For examples, see: dsann.let-m-test"
    [& args]
    (assert-args (even? (count args)) "an even number of forms")
    (let [destruct   (macros/case
                       :clj  destructure
                          ;; have to use resolve for the cljs branch because, even though not used,
                          ;; the normal clj reader will throw an exception reading "cljs.core"
                          ;; this does not work with self hosted - e.g. lumo as cljs.core/destructure is in a clj file
                       :cljs (ns-resolve 'cljs.core 'destructure))]
       (let [dargs (destruct args)
             vars  (take-nth 2 dargs)]
         `(let [~@dargs]
            (sym-m ~@vars)))))


  (defmacro assoc-m
    "Like let-m, but assocs into the supplied map.
     For examples, see: dsann.let-m-test"
    [m & args]
    `(merge ~m (let-m ~@args)))


  (defmacro assoc-syms
    "like sym-m but assocs to supplied map.
     For examples, see: dsann.let-m-test"
    [m & syms]
    `(merge ~m (sym-m ~@syms)))

  nil)

