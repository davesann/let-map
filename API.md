# Table of contents
-  [`dsann.let-map`](#dsann.let-map) 
    -  [`assoc-syms`](#dsann.let-map/assoc-syms) - like sym-map but assocs to supplied map.
    -  [`let-assoc`](#dsann.let-map/let-assoc) - Like let-map, but assocs into the supplied map.
    -  [`let-map`](#dsann.let-map/let-map) - Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...} For examples, see: dsann.let-map-test.
    -  [`sym-map`](#dsann.let-map/sym-map) - Creates a map from a seq of symbols.
-  [`dsann.let-map-test`](#dsann.let-map-test) 
    -  [`test-assoc-syms`](#dsann.let-map-test/test-assoc-syms)
    -  [`test-let-assoc`](#dsann.let-map-test/test-let-assoc)
    -  [`test-let-map`](#dsann.let-map-test/test-let-map)
    -  [`test-sym-map`](#dsann.let-map-test/test-sym-map)

-----
# <a name="dsann.let-map">dsann.let-map</a>






## <a name="dsann.let-map/assoc-syms">`assoc-syms`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L56-L60)
<a name="dsann.let-map/assoc-syms"></a>
``` clojure

(assoc-syms m & syms)
```


Macro.


like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/let-assoc">`let-assoc`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L49-L53)
<a name="dsann.let-map/let-assoc"></a>
``` clojure

(let-assoc m & args)
```


Macro.


Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/let-map">`let-map`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L38-L46)
<a name="dsann.let-map/let-map"></a>
``` clojure

(let-map & args)
```


Macro.


Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/sym-map">`sym-map`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L26-L36)
<a name="dsann.let-map/sym-map"></a>
``` clojure

(sym-map & syms)
```


Macro.


Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test

-----
# <a name="dsann.let-map-test">dsann.let-map-test</a>






## <a name="dsann.let-map-test/test-assoc-syms">`test-assoc-syms`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L72-L76)
<a name="dsann.let-map-test/test-assoc-syms"></a>
``` clojure

(test-assoc-syms)
```


## <a name="dsann.let-map-test/test-let-assoc">`test-let-assoc`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L43-L63)
<a name="dsann.let-map-test/test-let-assoc"></a>
``` clojure

(test-let-assoc)
```


## <a name="dsann.let-map-test/test-let-map">`test-let-map`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L7-L41)
<a name="dsann.let-map-test/test-let-map"></a>
``` clojure

(test-let-map)
```


## <a name="dsann.let-map-test/test-sym-map">`test-sym-map`</a> [:page_facing_up:](https://github.com/davesann/let-map/blob/main/src/test/clj/dsann/let_map_test.cljc#L66-L70)
<a name="dsann.let-map-test/test-sym-map"></a>
``` clojure

(test-sym-map)
```

