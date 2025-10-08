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

(defn resolve-ns-name
  "resolves the input to a namespace name for use in namespaced keywords"
  [ns]
  (cond
    (= nil    ns) (name (ns-name *ns*))
    (string?  ns) ns
    (keyword? ns) (or (namespace ns) (name ns))
    (symbol? ns) (name ns)))
;; note. previously this fn (above) also resolved symbols that were aliases to the aliased namespace.
;; however, the fn ns-aliases is not implemented in clojurescript.
;; So consistently resolving symbol if it is an alias is not possible consistently.
;; Instead, any symbol will be treated literally.
;; To use aliases, a namespaced keyword can be used. This will not lead to unexpected behaviours.
;; (symbol?  ns) (if-let [aliased-ns (get (ns-aliases *ns*) ns)];]
;;                 (name (ns-name aliased-ns))
;;                 (name ns))))

(defn syms->map-entries
  "Convert symbols intoa vector of map entries [k v]"
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
          (map #(-> [(keyword target-ns-name (name %)) %])))     ; map entry is [ns/keyword symbol]
         syms)))

(macros/deftime

  (defmacro assoc-syms
    "like sym-map but assocs to supplied map.

     (let [a 1 b 2] (assoc-syms some-map a b)) => {... :a 1 :b 2}

     For examples, see: dsann.let-map2-test"
    ([m syms]
     (let [entries (syms->map-entries syms)]
       `(into ~m ~entries))))

  (defmacro sym-map
    "Creates a map from a seq of symbols.
       Symbol names are converted to keywords as the key to the value it holds.

     (let [a 1 b 2] (sym-map a b)) => {:a 1 :b 2}

     For examples, see: dsann.let-map2-test"
    [syms]
    `(assoc-syms {} ~syms))

  (defmacro let-assoc
    "Like let-map, but assocs into the supplied map.

       (let-assoc some-map [a 1 b (inc a)]) => {... :a 1 :b 2}

       For examples, see: dsann.let-map2-test"
    [m args]
    (assert-args (even? (count args)) "an even number of forms")
    (let [dargs (destructure args)
          vars  (take-nth 2 dargs)]
      `(let [~@dargs]
         (assoc-syms ~m ~vars))))

  (defmacro let-map
    "Takes a list of name-value pairs and returns a map

       (let-map [a 1 ...]) => {:a 1 ...}

       For examples, see: dsann.let-map2-test"
    [args]
    `(let-assoc {} ~args))

;; filtering assocs
  (defmacro assoc-syms-
    "As for assoc-syms.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test"
    ([m syms] `(assoc-syms- nil? ~m ~syms))
    ([remove? m syms]
     (let [entries (syms->map-entries syms)]
       `(into ~m
              (remove (fn [[_# v#]] (~remove? v#)) ~entries)))))

  (defmacro sym-map-
    "As for sym-map.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test"
    ([syms] `(sym-map- nil? ~syms))
    ([remove? syms]
     `(assoc-syms- ~remove? {} ~syms)))

  (defmacro let-assoc-
    "As for let-assoc.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test"
    ([m args] `(let-assoc- nil? ~m ~args))
    ([remove? m args]
     (assert-args (even? (count args)) "an even number of forms")
     (let [dargs (destructure args)
           vars  (take-nth 2 dargs)]
       `(let [~@dargs]
          (assoc-syms- ~remove? ~m ~vars)))))

  (defmacro let-map-
    "As for let-map.
      Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

      For examples, see: dsann.let-map2-test"
    ([args] `(let-map- nil? ~args))
    ([remove? args]
     `(let-assoc- ~remove? {} ~args)))

;;;; ns versions

  (defmacro assoc-syms-ns
    "As for assoc-syms but uses a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test"
    ([m syms] `(assoc-syms-ns nil ~m ~syms))
    ([target-ns m syms]
     (let [target-ns-name (resolve-ns-name target-ns)
           entries (syms->map-entries target-ns-name syms)]
       `(into ~m ~entries))))

  (defmacro sym-map-ns
    "As for sym-map but uses a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test"
    ([syms]
     `(sym-map-ns nil ~syms))
    ([ns-target syms]
     `(assoc-syms-ns ~ns-target {} ~syms)))

  (defmacro let-assoc-ns
    "As for let-assoc but uses a provided namespace

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test"
    ([m args] `(let-assoc-ns nil ~m ~args))
    ([target-ns m args]
     (assert-args (even? (count args)) "an even number of forms")
     (let [dargs (destructure args)
           vars  (take-nth 2 dargs)]
       `(let [~@dargs]
          (assoc-syms-ns ~target-ns ~m ~vars)))))

  (defmacro let-map-ns
    "As for let-map but uses a provided namespace

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test"
    ([args] `(let-map-ns nil ~args))
    ([target-ns args]
     `(let-assoc-ns ~target-ns {} ~args)))

;;;; filtering ns versions
;; take a predicate and do not include k if (pred? v) is true
;;
  (defmacro assoc-syms-ns-
    "As for assoc-syms-ns.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test"
    ([m syms] `(assoc-syms-ns nil ~m ~syms))
    ([remove? target-ns m syms]
     (let [target-ns-name (resolve-ns-name target-ns)
           entries (syms->map-entries target-ns-name syms)]
       `(into ~m
              (remove (fn [[_# v#]] (~remove? v#)) ~entries)))))

  (defmacro sym-map-ns-
    "As for sym-map-ns.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test"

    ([syms]
     `(sym-map-ns- nil?        nil ~syms))
    ([ns-target syms]
     `(sym-map-ns- nil? ~ns-target ~syms))
    ([remove? ns-target syms]
     `(assoc-syms-ns- ~remove? ~ns-target {} ~syms)))

  (defmacro let-assoc-ns-
    "As for let-assoc-ns.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test"

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
    "As for let-map-ns.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test"

    ([args]
     `(let-map-ns-   nil?        nil ~args))
    ([target-ns args]
     `(let-map-ns-   nil? ~target-ns ~args))
    ([remove? target-ns args]
     `(let-assoc-ns- ~remove? ~target-ns {} ~args)))

  nil)
