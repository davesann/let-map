(ns dsann.let-map2
  #?(:clj (:refer-clojure :exclude [destructure]))
  (:require
   [net.cgrand.macrovich      :as macros]
   [dsann.macros.helpers      :refer [assert-args]]
   [dsann.let-map.destructure :refer [destructure]])
  ;; cljs must self refer macros
  #?(:cljs (:require-macros
            [dsann.let-map2 :refer [let-map     let-assoc     sym-map     assoc-syms
                                    let-map-    let-assoc-    sym-map-    assoc-syms
                                    let-map-ns  let-assoc-ns  sym-map-ns  assoc-syms-ns
                                    let-map-ns- let-assoc-ns- sym-map-ns- assoc-syms-ns-]])))

;; TODO - option to not remove _ fields for debugging?

;;;; helpers for arg name filtering

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

  ;; basic versions
  (defn resolve-ns-name [ns]
    (cond
      (= nil    ns) (name (ns-name *ns*))
      (string?  ns) ns
      (keyword? ns) (or (namespace ns) (name ns))
      ;; ns-aliases is not implemented in clojurescript so disabling this for consistancy
      ;; (symbol?  ns) (if-let [aliased-ns (get (ns-aliases *ns*) ns)];]
      ;;                 (name (ns-name aliased-ns))
      ;;                 (name ns))))
      (symbol? ns) (name ns)))
  (defn syms->map-entries
    ([syms]
     (into []
           (comp
            (remove _key?)                   ; exclude unwanted keys
            (remove clojure-destructure-id?) ; exclude destructure names
            (map #(-> [(keyword %) %])))     ; map entry is [keyword symbol]
           syms))
    ([target-ns-name syms]
     (into []
           (comp
            (remove _key?)                                         ; exclude unwanted keys
            (remove clojure-destructure-id?)                       ; exclude destructure names
            (map #(-> [(keyword target-ns-name (name %)) %])))     ; map entry is [keyword symbol]
           syms)))

  (defmacro assoc-syms
    "like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map2-test"
    ([m syms]
     (let [entries (syms->map-entries syms)]
       `(into ~m ~entries))))

  (defmacro sym-map
    "Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map2-test"
    [syms]
    `(assoc-syms {} ~syms))

  (defmacro let-assoc
    "Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test"
    [m args]
    (assert-args (even? (count args)) "an even number of forms")
    (let [dargs (destructure args)
          vars  (take-nth 2 dargs)]
      `(let [~@dargs]
         (assoc-syms ~m ~vars))))

  (defmacro let-map
    "Takes a list of name-value pairs and returns a map: (let-map [a 1 ...]) => {:a 1 ...}
      For examples, see: dsann.let-map-test"
    [args]
    `(let-assoc {} ~args))

;; filtering assocs
  (defmacro assoc-syms-
    ([m syms] `(assoc-syms- nil? ~m ~syms))
    ([remove? m syms]
     (let [entries (syms->map-entries syms)]
       `(into ~m
              (remove (fn [[_# v#]] (~remove? v#)) ~entries)))))

  (defmacro sym-map-
    "Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map2-test"
    ([syms] `(sym-map- nil? ~syms))
    ([remove? syms]
     `(assoc-syms- ~remove? {} ~syms)))

  (defmacro let-assoc-
    "Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test"
    ([m args] `(let-assoc- nil? ~m ~args))
    ([remove? m args]
     (assert-args (even? (count args)) "an even number of forms")
     (let [dargs (destructure args)
           vars  (take-nth 2 dargs)]
       `(let [~@dargs]
          (assoc-syms- ~remove? ~m ~vars)))))

  (defmacro let-map-
    "Takes a list of name-value pairs and returns a map: (let-map [a 1 ...]) => {:a 1 ...}
      For examples, see: dsann.let-map-test"
    ([args] `(let-map- nil? ~args))
    ([remove? args]
     `(let-assoc- ~remove? {} ~args)))

;;;; ns versions

  (defmacro assoc-syms-ns
    "like assoc-syms but uses a provide namespace
     For examples, see: dsann.let-map2-test"
    ([m syms] `(assoc-syms-ns nil ~m ~syms))
    ([target-ns m syms]
     (let [target-ns-name (resolve-ns-name target-ns)
           entries (syms->map-entries target-ns-name syms)]
       `(into ~m ~entries))))

  (defmacro sym-map-ns
    "Creates a map from a seq of symbols.
     Symbol names are converted to namespaced keywords (target-ns) as the key to the value it holds
     if target-ns is nil or not provided then defaults to *current namespace* i.e. ::some-key
     For examples, see: dsann.let-map2-test"
    ([syms]
     `(sym-map-ns nil ~syms))
    ([ns-target syms]
     `(assoc-syms-ns ~ns-target {} ~syms)))

  (defmacro let-assoc-ns
    "Like let-map-ns, but assocs into the supplied map.
     For examples, see: dsann.let-map2-test"
    ([m args] `(let-assoc-ns nil ~m ~args))
    ([target-ns m args]
     (assert-args (even? (count args)) "an even number of forms")
     (let [dargs (destructure args)
           vars  (take-nth 2 dargs)]
       `(let [~@dargs]
          (assoc-syms-ns ~target-ns ~m ~vars)))))

  (defmacro let-map-ns
    "Like let-map but takes a target ns for namespaced map
      Symbol names are converted to namespaced keywords (target-ns) as the key to the value it holds
      If target-ns is nil or not provided then defaults to *current namespace* i.e. ::some-key
      For examples, see: dsann.let-map2-test"
    ([args] `(let-map-ns nil ~args))
    ([target-ns args]
     `(let-assoc-ns ~target-ns {} ~args)))

;;;; filtering ns versions
;; take a predicate and do not include k if (pred? v) is true
;;
  (defmacro assoc-syms-ns-
    "like assoc-syms but uses a provide namespace
     For examples, see: dsann.let-map2-test

     trailing '-' indicates Values are filtered i.e. not included if (remove? v) is true. defaults to remove nils"
    ([m syms] `(assoc-syms-ns nil ~m ~syms))
    ([remove? target-ns m syms]
     (let [target-ns-name (resolve-ns-name target-ns)
           entries (syms->map-entries target-ns-name syms)]
       `(into ~m
              (remove (fn [[_# v#]] (~remove? v#)) ~entries)))))

  (defmacro sym-map-ns-
    "Creates a map from a seq of symbols.
     Symbol names are converted to namespaced keywords (target-ns) as the key to the value it holds
     if target-ns is nil or not provided then defaults to *current namespace* i.e. ::some-key
     For examples, see: dsann.let-map2-test

     trailing '-' indicates Values are filtered i.e. not included if (remove? v) is true. defaults to remove nils"

    ([syms]
     `(sym-map-ns- nil?        nil ~syms))
    ([ns-target syms]
     `(sym-map-ns- nil? ~ns-target ~syms))
    ([remove? ns-target syms]
     `(assoc-syms-ns- ~remove? ~ns-target {} ~syms)))

  (defmacro let-assoc-ns-
    "Like let-map-ns, but assocs into the supplied map.
     For examples, see: dsann.let-map2-test

     trailing '-' indicates Values are filtered i.e. not included if (remove? v) is true. defaults to remove nils"

    ([m args]
     `(let-assoc-ns- nil? nil ~m ~args))
    ([target-ns m args]
     `(let-assoc-ns- nil? ~target-ns ~m ~args))
    ([remove? target-ns m args]
     (assert-args (even? (count args)) "an even number of forms")
     (let [dargs (destructure args)
           vars  (take-nth 2 dargs)]
       `(let [~@dargs]
          (assoc-syms-ns- ~remove? ~target-ns ~m ~vars)))))

  (defmacro let-map-ns-
    "Like let-map but takes a target ns for namespaced map
      Symbol names are converted to namespaced keywords (target-ns) as the key to the value it holds
      If target-ns is nil or not provided then defaults to *current namespace* i.e. ::some-key
      For examples, see: dsann.let-map2-test

     trailing '-' indicates Values are filtered i.e. not included if (remove? v) is true. defaults to remove nils"

    ([args]
     `(let-map-ns-   nil?        nil ~args))
    ([target-ns args]
     `(let-map-ns-   nil? ~target-ns ~args))
    ([remove? target-ns args]
     `(let-assoc-ns- ~remove? ~target-ns {} ~args)))

  nil)
