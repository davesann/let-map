(ns dsann.let-m-test
  (:require
    [clojure.test :refer [deftest is testing are]]
    [dsann.let-m  :refer [let-m assoc-m sym-m assoc-syms]]))

(deftest test-let-m
  (testing "let-m"
    (testing "simple creation"
      (are [x y] (= x y)
           {:a 1}        (let-m a 1)

           {:a 1 :b 2}   (let-m a 1 b 2)

           {:a 1 :b 2}   (let-m a 1 b (inc a))

           {:a '(0 1 2)
            :b '(1 2 3)}
           (let-m a (range 3)
                  b (map inc a))))

    (testing "names starting _ are not included in results"
      (are [x y] (= x y)
           {:b 2} (let-m _a 1 b (inc _a))))

    (testing "destructuring"
      (are [x y] (= x y)
           {:a 1 :b 2}
           (let [m {:a 1}]
             (let-m {:keys [a]} m
                    b (inc a)))

           {:first 1 :rest '(2 3)}
           (let [v [1 2 3]]
             (let-m [first & rest] v))))))

 
