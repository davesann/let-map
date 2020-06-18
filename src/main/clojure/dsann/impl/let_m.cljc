(ns dsann.impl.let-m
    (:require
       [dsann.docs.self-update :refer [auto-append-docs!]]
       #?(:clj
            [net.cgrand.macrovich :as macros]))
  #?(:cljs
       (:require-macros 
         [net.cgrand.macrovich :as macros]
         [dsann.impl.let-m     :refer [assert-args]]))) ; cljs must self refer macros

;;--------------------------------------------------
;; general helpers

;; copied from clojure.core - marked as private there.
;; modified to be host agnostic with ex-info

;; --------------------------------------------------
;; helpers for arg name filtering

(defn _key?
  {:dsann/docs '{:desc "true if begins with _" 
                 :example [(_key? '_sym)  (_key? 'sym )] :result [true false]}}
  [sym]
  (= \_ (nth (name sym) 0)))

(defn clojure-destructure-id? 
  "true if sym matches clojure's internal temporary destructure names" 
  [sym]
  (let [s (name sym)]
    (re-matches #"(vec|map|seq|first|p)__\d+" s)))

(macros/deftime
  (defmacro assert-args
    {:dsann/docs '["
                 Used in macros to assert args meets required form and report errors.
                 copied from clojure.core (private). Modified to be host agnostic with ex-info
                 
                 takes a seq of pred? msg.... 
                   if any (pred? args) fails, the message and file/line number info will be displayed."

  
                   {:desc "Example usage:"
                    :unevaluated-example  (assert-args 
                                            (even?    (count args)) "an even number of forms"
                                            (keyword? (first args)) "the first arg must be a keyword")}]} 
    [& pairs]
    `(do (when-not ~(first pairs)
           (let [ns#          ~'*ns*
                 line#        (:line (meta ~'&form))
                 name#        (first ~'&form)
                 assert-msg#  ~(second pairs)
                 msg#         (str name# " requires " assert-msg# " in " ns# ":" line#)]
             (throw (ex-info msg# {:ns         ns#
                                   :name       name#
                                   :assert-msg assert-msg#
                                   :line       line#}))))
       ~(let [more (nnext pairs)]
          (when more
            (list* `assert-args more)))))
  nil)  


(auto-append-docs!)

