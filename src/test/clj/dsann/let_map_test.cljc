(ns dsann.let-map-test
  (:require
    [clojure.test  :refer [deftest is testing]]
    [dsann.test    :refer [are]]
    [dsann.let-map :refer [let-map let-assoc sym-map assoc-syms]]))

(deftest test-let-map
  (testing "dsann.let-map"
    (testing "Simple maps. (Can be done with map literal also)"
      (are =
        (let-map a 1)                          {:a 1}
        (let-map a 1 b 2)                      {:a 1 :b 2}))

    (testing "Calculations on values. Cannot be done with map literal"
      (are =
       ;  compare: - no need to create the map at the end.
       ; (let  [a 1 b (inc a)] {:a a :b b})
         (let-map a 1 b (inc a))               {:a 1 :b 2}

         (let-map a (range 3) b (map inc a))   {:a '(0 1 2) :b '(1 2 3)}))


    (testing "Intermediate values: names starting _ are excluded from the result"
      (are =
         (let-map _a 1  b (inc _a))            {:b 2}))

    (testing "Destructuring works if needed"
      (let [m {:a 1} v [1 2 3]]
        (are =
           (let-map {:keys [a]} m)             {:a 1}

           (let-map {:keys [a]} m  b (inc a))  {:a 1 :b 2}

           (let-map [first & rest] v)          {:first 1 :rest '(2 3)})))

    (testing "Nesting is ok. Scope applies: You must extract values from nested maps if you want to use them"
       (are =
          (let-map
            a 1
            b (let-map x 5 y 7)
            c (-> b :x inc))                 {:a 1 :b {:x 5 :y 7} :c 6}))))

(deftest test-let-assoc
  (testing "dsann.let-assoc"
    (let [m  {:x 1}
          md {:a 1}
          vd [1 2 3]]
      (testing "Exactly like let-map but assocs to an existing map."
        (are =
          (let-assoc m a 1)                        {:x 1 :a 1}
          (let-assoc m a 1 b 2)                    {:x 1 :a 1 :b 2}
          (let-assoc m a 1 b (inc a))              {:x 1 :a 1 :b 2}

          (let-assoc m a (range 3) b (map inc a))  {:x 1 :a '(0 1 2) :b '(1 2 3)}
          (let-assoc m _a 1  b (inc _a))           {:x 1 :b 2}

          (let-assoc m {:keys [a]} md b (inc a))   {:x 1 :a 1 :b 2}
          (let-assoc m [first & rest] vd)          {:x 1 :first 1 :rest '(2 3)}

          (let-assoc m
            a 1
            b (let-map x 5 y 7)
            c (-> b :x inc))                     {:x 1 :a 1 :b {:x 5 :y 7} :c 6})))))


(deftest test-sym-map
  (testing "dsann.sym-map"
     (testing "Create map from symbols ..."
       (are =
         (let [a 1 b 2] (sym-map a b))             {:a 1 :b 2}))))

(deftest test-assoc-syms
  (testing "dsann.assoc-syms"
     (testing "Assoc symbols to map ..."
       (are =
         (let [a 1 b 2] (assoc-syms {:x 1} a b)) {:x 1 :a 1 :b 2}))))

