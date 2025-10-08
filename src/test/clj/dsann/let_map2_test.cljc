(ns dsann.let-map2-test
  (:require
   [clojure.test  :refer [deftest is testing]]
   [flatland.ordered.map :refer [ordered-map]]
   [dsann.test    :refer [are]]
   [dsann.let-map2 :refer [let-map     let-assoc     sym-map     assoc-syms
                           let-map-    let-assoc-    sym-map-    assoc-syms-
                           let-map-ns  let-assoc-ns  sym-map-ns  assoc-syms-ns
                           let-map-ns- let-assoc-ns- sym-map-ns- assoc-syms-ns-]]

   [my.ns.alias   :as-alias an-alias]))

(deftest test-assoc-syms
  (testing "dsann.assoc-syms"
    (testing "Assoc symbols to map ..."
      (are =
           (let [a 1 b 2]
             (assoc-syms {:x 1} [a b]))
        {:x 1 :a 1 :b 2}))
    (testing "Retains map type"
      (are =
           (type (ordered-map))
        (type (let [a 1]
                (assoc-syms (ordered-map) [a])))))))

(deftest test-assoc-syms-
  (testing "dsann.assoc-syms-"
    (testing "Assoc symbols to map ..."
      (are =
           (let [a 1 b 2 c nil]
             (assoc-syms- {:x 1} [a b c]))
        {:x 1 :a 1 :b 2}))
    (testing "Retains map type"
      (are =
           (type (ordered-map))
        (type (let [a 1]
                (assoc-syms- nil? (ordered-map) [a])))))))

(deftest test-assoc-syms-ns
  (testing "dsann.assoc-syms-ns"
    (testing "Assoc namespaced symbols to map ..."
      (testing "general prefixes"
        (are =
             (let [a 1 b 2]
               (assoc-syms-ns "some.namespace.name" {:x 1} [a b]))
          {:x 1 :some.namespace.name/a 1 :some.namespace.name/b 2}

          (let [a 1 b 2]
            (assoc-syms-ns :some.namespace.name {:x 1} [a b]))
          {:x 1 :some.namespace.name/a 1 :some.namespace.name/b 2}

          (let [a 1 b 2]
            (assoc-syms-ns some.namespace.name {:x 1} [a b]))
          {:x 1 :some.namespace.name/a 1 :some.namespace.name/b 2}))

      (testing "namespaced keywords - use the namespace, ignore the key"
        (are =
             (let [a 1 b 2]
               (assoc-syms-ns :some.namespace/name {:x 1} [a b]))
          {:x 1 :some.namespace/a 1 :some.namespace/b 2}

          (let [a 1 b 2]
            (assoc-syms-ns ::an-alias/x {:x 1} [a b]))
          {:x 1 :my.ns.alias/a 1 :my.ns.alias/b 2}))

      #_(testing "Resolves Aliases for symbols - clojure only"
          (are =
               (let [a 1 b 2]
                 (assoc-syms-ns an-alias {:x 1} [a b]))
            {:x 1 :my.ns.alias/a 1 :my.ns.alias/b 2}

            (let [a 1 b 2]
              (assoc-syms-ns an-alias {:x 1} [a b]))
            {:x 1 ::an-alias/a 1 ::an-alias/b 2}))

      (testing "Retains map type"
        (are =
             (type (ordered-map))
          (type (let [a 1]
                  (assoc-syms-ns :fred (ordered-map) [a]))))))))

(deftest test-sym-map
  (testing "dsann.sym-map"
    (testing "Create map from symbols ..."
      (are =
           (let [a 1 b 2] (sym-map [a b]))           {:a 1 :b 2}))))

(deftest test-let-assoc
  (testing "dsann.let-assoc"
    (let [m  {:x 1}
          md {:a 1}
          vd [1 2 3]]
      (testing "Exactly like let-map but assocs to an existing map."
        (are =
             (let-assoc m [a 1])                        {:x 1 :a 1}
             (let-assoc m [a 1 b 2])                    {:x 1 :a 1 :b 2}
             (let-assoc m [a 1 b (inc a)])              {:x 1 :a 1 :b 2}

             (let-assoc m [a (range 3) b (map inc a)])  {:x 1 :a '(0 1 2) :b '(1 2 3)}
             (let-assoc m [_a 1  b (inc _a)])           {:x 1 :b 2}

             (let-assoc m [{:keys [a]} md b (inc a)])   {:x 1 :a 1 :b 2}
             (let-assoc m [[first & rest] vd])          {:x 1 :first 1 :rest '(2 3)}

             (let-assoc m
                        [a 1
                         b (let-map [x 5 y 7])
                         c (-> b :x inc)])                         {:x 1 :a 1 :b {:x 5 :y 7} :c 6}))
      (testing "Retains map type"
        (are =
             (type (let-assoc (ordered-map :a 1) [b 2]))
          (type (ordered-map)))))))

(deftest test-let-map
  (testing "dsann.let-map"
    (testing "Simple maps. (Can be done with map literal also)"
      (are =
           (let-map [a 1])                          {:a 1}
           (let-map [a 1 b 2])                      {:a 1 :b 2}))

    (testing "Calculations on values. Cannot be done with map literal"
      (are =
       ;  compare: - no need to create the map at the end.
       ; (let  [a 1 b (inc a)] {:a a :b b})
           (let-map [a 1 b (inc a)])               {:a 1 :b 2}

           (let-map [a (range 3) b (map inc a)])   {:a '(0 1 2) :b '(1 2 3)}))

    (testing "Intermediate values: names starting _ are excluded from the result"
      (are =
           (let-map [_a 1  b (inc _a)])            {:b 2}))

    (testing "Destructuring works"
      (let [m {:a 1} v [1 2 3]]
        (are =
             (let-map [{:keys [a]} m])             {:a 1}

             (let-map [{:keys [a]} m  b (inc a)])  {:a 1 :b 2}

             (let-map [[first & rest] v])          {:first 1 :rest '(2 3)})))

    (testing "Nesting is ok. Scope applies: You must extract values from nested maps if you want to use them"
      (are =
           (let-map
            [a 1
             b (let-map [x 5 y 7])
             c (-> b :x inc)])

        {:a 1 :b {:x 5 :y 7} :c 6}))))

(deftest test-let-map-ns
  (testing "dsann.let-map/let-map-ns"
    (testing "Simple maps. (Can be done with map literal also)"
      (testing "default namespace"
        (are =
             (let-map-ns [a 1])      {::a 1}
             (let-map-ns [a 1 b 2])  {::a 1 ::b 2}))

      (testing "provided namespace"
        (are =
             (let-map-ns a.namespace   [a 1])      {:a.namespace/a 1}
             (let-map-ns a.namespace   [a 1 b 2])  {:a.namespace/a 1 :a.namespace/b 2}
             (let-map-ns :a.namespace  [a 1])      {:a.namespace/a 1}
             (let-map-ns :a.namespace  [a 1 b 2])  {:a.namespace/a 1 :a.namespace/b 2}
             (let-map-ns "a.namespace" [a 1])      {:a.namespace/a 1}
             (let-map-ns "a.namespace" [a 1 b 2])  {:a.namespace/a 1 :a.namespace/b 2}))

      #_(testing "aliased namespace"
          (are =
               (let-map-ns an-alias  [a 1])       {::an-alias/a 1}
               (let-map-ns an-alias  [a 1 b 2])   {::an-alias/a 1 ::an-alias/b 2}
               (let-map-ns an-alias  [a 1])       {:my.ns.alias/a 1}
               (let-map-ns an-alias  [a 1 b 2])   {:my.ns.alias/a 1 :my.ns.alias/b 2})

          (testing "is only for symbols"
            (are =
                 (let-map-ns :an-alias  [a 1])       {:an-alias/a 1}
                 (let-map-ns :an-alias   [a 1 b 2])  {:an-alias/a 1 :an-alias/b 2}
                 (let-map-ns "an-alias" [a 1])       {:an-alias/a 1}
                 (let-map-ns "an-alias" [a 1 b 2])   {:an-alias/a 1 :an-alias/b 2}))))

    (testing "Calculations on values. Cannot be done with map literal"
      (are =
       ;  compare: - no need to create the map at the end.
       ; (let  [a 1 b (inc a)] {:a a :b b})
           (let-map-ns [a 1 b (inc a)])               {::a 1 ::b 2}

           (let-map-ns [a (range 3) b (map inc a)])   {::a '(0 1 2) ::b '(1 2 3)}))

    (testing "Intermediate values: names starting _ are excluded from the result"
      (are =
           (let-map-ns [_a 1  b (inc _a)])            {::b 2}))

    (testing "Destructuring works"
      (let [m {:a 1} v [1 2 3]]
        (are =
             (let-map-ns [{:keys [a]} m])             {::a 1}

             (let-map-ns [{:keys [a]} m  b (inc a)])  {::a 1 ::b 2}

             (let-map-ns [[first & rest] v])          {::first 1 ::rest '(2 3)})))

    (testing "Nesting is ok. Scope applies: You must extract values from nested maps if you want to use them"
      (are =
           (let-map-ns
            [a 1
             b (let-map [x 5 y 7])
             c (-> b :x inc)])                 {::a 1 ::b {:x 5 :y 7} ::c 6}))))

;; TODO - improve coverage of these tests
(deftest test-let-map-ns-
  (testing "dsann.let-map/let-map-ns- filtering"
    (testing "Simple maps. (Can be done with map literal also)"
      (testing "default namespace - nil values filtered by default"
        (are =
             (let-map-ns- [a 1 c nil])      {::a 1}
             (let-map-ns- [a 1 b 2 c nil])  {::a 1 ::b 2}))

      (testing "provided namespace - nil values filtered"
        (are =
             (let-map-ns- a.namespace   [a 1     c nil])  {:a.namespace/a 1}
             (let-map-ns- a.namespace   [a 1 b 2 c nil])  {:a.namespace/a 1 :a.namespace/b 2}
             (let-map-ns- :a.namespace  [a 1     c nil])  {:a.namespace/a 1}
             (let-map-ns- :a.namespace  [a 1 b 2 c nil])  {:a.namespace/a 1 :a.namespace/b 2}
             (let-map-ns- "a.namespace" [a 1     c nil])  {:a.namespace/a 1}
             (let-map-ns- "a.namespace" [a 1 b 2 c nil])  {:a.namespace/a 1 :a.namespace/b 2}))

      #_(testing "aliased namespace - nil values filtered"
          (are =
               (let-map-ns- an-alias  [a 1     c nil])   {::an-alias/a 1}
               (let-map-ns- an-alias  [a 1 b 2 c nil])   {::an-alias/a 1 ::an-alias/b 2}
               (let-map-ns- an-alias  [a 1     c nil])   {:my.ns.alias/a 1}
               (let-map-ns- an-alias  [a 1 b 2 c nil])   {:my.ns.alias/a 1 :my.ns.alias/b 2})

          (testing "is only resolved for symbols"
            (are =
                 (let-map-ns- :an-alias  [a 1      c nil])  {:an-alias/a 1}
                 (let-map-ns- :an-alias  [a 1 b 2  c nil])  {:an-alias/a 1 :an-alias/b 2}
                 (let-map-ns- "an-alias" [a 1      c nil])  {:an-alias/a 1}
                 (let-map-ns- "an-alias" [a 1 b 2  c nil])  {:an-alias/a 1 :an-alias/b 2}))))

    (testing "Calculations on values. Cannot be done with map literal"
      (are =
       ;  compare: - no need to create the map at the end.
       ; (let  [a 1 b (inc a)] {:a a :b b})
           (let-map-ns- [a 1         b (inc a)     c (when (zero? a) :a-is-zero)])           {::a 1 ::b 2}

           (let-map-ns- [a (range 3) b (map inc a) c (when (zero? (first b)) :blah)])   {::a '(0 1 2) ::b '(1 2 3)}))

    (testing "Intermediate values: names starting _ are excluded from the result"
      (are =
           (let-map-ns- [_a 1  b (inc _a)])            {::b 2}))

    (testing "Destructuring works"
      (let [m {:a 1} v [1 2 3]]
        (are =
             (let-map-ns- [{:keys [a]} m])             {::a 1}

             (let-map-ns- [{:keys [a]} m  b (inc a)])  {::a 1 ::b 2}

             (let-map-ns- [[first & rest] v])          {::first 1 ::rest '(2 3)})))

    (testing "Nesting is ok. Scope applies: You must extract values from nested maps if you want to use them"
      (are =
           (let-map-ns-
            [a 1
             b (let-map [x 5 y 7])
             c (-> b :x inc)])                 {::a 1 ::b {:x 5 :y 7} ::c 6}))))
