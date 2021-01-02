(ns dsann.call-m
 (:require
   [net.cgrand.macrovich  :as macros]
   [clojure.string :as s])
 ; cljs must self refer macros
 #?(:cljs (:require-macros [dsann.call-m :refer [call-m !]])))


(defn ^:private string-kw [s]
  (let [[n f] (s/split s #"/" 2)]
    (if f
      (keyword n f)
      (keyword n))))

(defn ^:private as-kw [v]
  (condp #(%1 %2) v
    symbol?  (keyword (namespace v) (name v))
    keyword? v
    string?  (string-kw v)))

(macros/deftime
  (defmacro call-m
    "Lookup f-sym in m (as a keyword) and apply the result as a function to args
      throws ex-info exception if key is not found
      For examples, see: dsann.call-m-test"
    [m f & args]
    (let [f-key (as-kw f)]
      `(if-let [f# (~f-key ~m)]
         (f# ~@args)
         (throw (ex-info "call-m : map function not found" {:f-name '~f :f-key ~f-key :map ~m})))))

  (defmacro !
    "alias for call-m"
    [m f-sym & args]
    `(call-m ~m ~f-sym ~@args))

  nil)

(defn call-m*
  "Lookup value of fname in m (as a keyword) and apply the result as a function to args
    Throws an ex-info exception if the key is not found.
    Prefer call-m if you know the function name at compile time (i.e. do not need to pass as a var)
    For examples, see: dsann.call-m-test"

  [m fname & args]
  (let [f-key (as-kw fname)]
    (if-let [f (get m f-key)]
      (apply f args)
      (throw (ex-info "call-m* : map function not found" {:f-name fname :f-key f-key :map m})))))

(def !*
  "alias for call-m*"
   call-m*)

