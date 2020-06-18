(ns dsann.let-m
  (:require
     [dsann.docs.self-update  :refer [auto-append-docs!]]
     [dsann.impl.let-m        :as impl :refer [assert-args _key? clojure-destructure-id?]]
     #?(:clj
         [net.cgrand.macrovich    :as macros]))
  #?(:cljs
     (:require-macros 
       [net.cgrand.macrovich :as    macros]
       [dsann.let-m          :refer [symbol-map let-m assoc-m sym-m assoc-syms]]))) ; cljs must self refer macros)


(macros/deftime
  ;; --------------------------------------------------
  (defmacro symbol-map 
    {:doc 
     "Creates a map from a seq of symbols.
    Symbol names are converted to keywords as the key to the value it holds"
     :dsann/docs '[{:example (let [a 1 b 2] 
                               (symbol-map [a b]))
                    :result  {:a 1 :b 2}}]}
    [syms]
    (into {} 
          (comp
            (remove _key?)                   ;; exclude unwanted keys
            (remove clojure-destructure-id?) ;; exclude destructure names
            (map #(-> [(keyword %) %])))     ;; map entry is [keyword symbol]
          syms))

  (defmacro let-m
    {:doc "Takes a list of name-value pairs and returns a map {:name :value ...}" 
     :dsann/docs '[{:example (let-m a 1 b 2)
                    :result  {:a 1 :b 2}}

                   {:desc    "Works like let - previously defined values can be used"
                    :example (let-m a 1 b (inc a))
                    :result  {:a 1 :b 2}}

                   {:desc    "Destructuring works"
                    :example (let [a-map {:a 1 :b 2}
                                   a-vec [1 2 3]]
                               (let-m {:keys [a]} a-map  
                                      [fst & rst] a-vec
                                      c           (inc a)))
                    :result  {:a 1 :c 2 :fst 1 :rst '(2 3)}}

                   {:desc    "Names that begin with _ are removed from the result"
                    :example (let-m _a 1 b (inc _a))
                    :result  {:b 2}}]}
    [& args]
    (impl/assert-args (even? (count args)) "an even number of forms")
    (let [destruct   (macros/case 
                       :clj  destructure
                          ;; have to use resolve for the cljs branch because, even though not used,
                          ;; the normal clj reader will throw an exception reading cljs.core
                          ;; this does not work with self hosted - e.g. planck
                       :cljs (ns-resolve 'cljs.core 'destructure))]
       (let [dargs (destruct args)
             vars  (take-nth 2 dargs)]
         `(let [~@dargs]
            (symbol-map ~vars)))))


  (defmacro assoc-m
    {:doc "Like let-m, but assocs into the supplied map"
     :dsann/docs '[{:example (let [a-map {:a 1}] 
                               (assoc-m a-map 
                                        _b 2 
                                         c (* _b 2)))
                    :result  {:a 1 :c 4}}
                   "note: values in the supplied map are not available in expressions (unless bound in the scope)"]}
    [m & args]
    `(into ~m (let-m ~@args)))

  (defmacro sym-m
    {:doc "creates a map from a list of symbols"
     :dsann/docs '{:example (let [a 1 b 2] 
                              (sym-m a b))
                   :result  {:a 1 :b 2}}}
    [& syms]
    `(symbol-map ~syms))

  (defmacro assoc-syms
    {:doc "like sym-m but assocs to supplied map" 
     :dsann/docs '{:example (let [m {:a 1 :b 2}
                                  a 4
                                  c 3]
                                 (assoc-syms m a c))
                   :result  {:a 4 :b 2 :c 3}}}
    [m & syms]
    `(into ~m (sym-m ~@syms)))
  
  
  nil)

(auto-append-docs!)
