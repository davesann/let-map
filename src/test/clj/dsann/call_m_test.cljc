(ns dsann.call-m-test
   (:require
    [clojure.test       :refer [deftest testing]]
    [dsann.test         :refer [are is throws]]
    [dsann.call-m       :refer [call-m call-m* ! !*]])
 #?(:clj (:import [clojure.lang ExceptionInfo])))

(defn f
  ([]  1)
  ([x] (inc x)))

(def m {:f f
        :some.ns/g f})

(def var-string  "f")
(def var-keyword :f)
(def var-sym     'f)

(def g-var-string  "some.ns/g")
(def g-var-keyword :some.ns/g)
(def g-var-sym     'some.ns/g)

(deftest test-call-m
  (testing "dsann.call-m/call-m"
    (testing "- call a function in a map (macro)"
      (testing "- with symbol (uses the name of the symbol)"
        (are =
          (call-m m f)   1
          (call-m m f 1) 2
          (call-m m some.ns/g)   1
          (call-m m some.ns/g 1) 2)
        (testing "- function not found"
          (throws
             ExceptionInfo
             (call-m m this-is-not-in-the-map))))
      (testing "- with value"
        (testing "- no args"
          (are =
            (call-m m "f") 1
            (call-m m :f)  1
            (call-m m "some.ns/g") 1
            (call-m m :some.ns/g)  1))
        (testing "- with args"
          (are =
            (call-m m "f"  1) 2
            (call-m m :f   1) 2
            (call-m m "some.ns/g" 1) 2
            (call-m m :some.ns/g  1) 2))
        (testing "- wrong args - normal errors"
           (throws
             #?(:clj  clojure.lang.ArityException
                :cljs js/Error)
             (call-m m "f" 1 20)))))))

(deftest test-call-m*
  (testing "dsann.call-m"
    (testing "call-m*"
      (testing "no args"
        (are =
          (call-m* m "f")           1
          (call-m* m :f)            1
          (call-m* m 'f)            1
          (call-m* m var-string)    1
          (call-m* m var-keyword)   1
          (call-m* m var-sym)       1

          (call-m* m "some.ns/g")   1
          (call-m* m :some.ns/g)    1
          (call-m* m 'some.ns/g)    1
          (call-m* m g-var-string)  1
          (call-m* m g-var-keyword) 1
          (call-m* m g-var-sym)     1))
      (testing "function not found"
        (throws
           ExceptionInfo
           (call-m m this-is-not-in-the-map)))

      (testing "with args"
        (are =
          (call-m* m "f"          1) 2
          (call-m* m :f           1) 2
          (call-m* m 'f           1) 2
          (call-m* m var-string   1) 2
          (call-m* m var-keyword  1) 2
          (call-m* m var-sym      1) 2

          (call-m* m "some.ns/g"   1) 2
          (call-m* m :some.ns/g    1) 2
          (call-m* m 'some.ns/g    1) 2
          (call-m* m g-var-string  1) 2
          (call-m* m g-var-keyword 1) 2
          (call-m* m g-var-sym     1) 2)))))

(deftest call-m-aliases
  (testing "dsann.call-m"
    (testing "! and !* - just aliases call-m and call-m*"
     (are =
       (!  m f)      1
       (!  m f 1)    2

       (!  m some.ns/g)    1
       (!  m some.ns/g 1)  2

       (!* m :f)     1
       (!* m "f" 1)  2

       (!* m :some.ns/g)     1
       (!* m "some.ns/g" 1)  2))))



