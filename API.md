# Table of contents
-  [`dsann.let-map`](#dsannlet-map) 
    -  [`assoc-syms`](#assoc-syms) - like sym-map but assocs to supplied map.
    -  [`let-assoc`](#let-assoc) - Like let-map, but assocs into the supplied map.
    -  [`let-map`](#let-map) - Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 .
    -  [`sym-map`](#sym-map) - Creates a map from a seq of symbols.
-  [`dsann.let-map-test`](#dsannlet-map-test) 
    -  [`test-assoc-syms`](#test-assoc-syms)
    -  [`test-let-assoc`](#test-let-assoc)
    -  [`test-let-map`](#test-let-map)
    -  [`test-sym-map`](#test-sym-map)
# dsann.let-map 





## `assoc-syms`
``` clojure

(assoc-syms m & syms)
```


Macro.


like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test
<br><sub>[source](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L56-L60)</sub>
## `let-assoc`
``` clojure

(let-assoc m & args)
```


Macro.


Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test
<br><sub>[source](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L49-L53)</sub>
## `let-map`
``` clojure

(let-map & args)
```


Macro.


Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
     For examples, see: dsann.let-map-test
<br><sub>[source](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L38-L46)</sub>
## `sym-map`
``` clojure

(sym-map & syms)
```


Macro.


Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test
<br><sub>[source](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L26-L36)</sub>
# dsann.let-map-test 





## `test-assoc-syms`
``` clojure

(test-assoc-syms)
```

<sub>[source](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L70-L74)</sub>
## `test-let-assoc`
``` clojure

(test-let-assoc)
```

<sub>[source](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L41-L61)</sub>
## `test-let-map`
``` clojure

(test-let-map)
```

<sub>[source](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L7-L39)</sub>
## `test-sym-map`
``` clojure

(test-sym-map)
```

<sub>[source](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L64-L68)</sub>
