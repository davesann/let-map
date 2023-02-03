# Table of contents
-  [`dsann.let-map`](#dsann.let-map) 
    -  [`assoc-syms`](#dsann.let-map/assoc-syms) - like sym-map but assocs to supplied map.
    -  [`let-assoc`](#dsann.let-map/let-assoc) - Like let-map, but assocs into the supplied map.
    -  [`let-map`](#dsann.let-map/let-map) - Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...} For examples, see: dsann.let-map-test.
    -  [`sym-map`](#dsann.let-map/sym-map) - Creates a map from a seq of symbols.

-----
# <a name="dsann.let-map">dsann.let-map</a>






## <a name="dsann.let-map/assoc-syms">`assoc-syms`</a> [📃](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L56-L60)
<a name="dsann.let-map/assoc-syms"></a>
``` clojure

(assoc-syms m & syms)
```


Macro.


like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/let-assoc">`let-assoc`</a> [📃](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L49-L53)
<a name="dsann.let-map/let-assoc"></a>
``` clojure

(let-assoc m & args)
```


Macro.


Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/let-map">`let-map`</a> [📃](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L38-L46)
<a name="dsann.let-map/let-map"></a>
``` clojure

(let-map & args)
```


Macro.


Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
     For examples, see: dsann.let-map-test

## <a name="dsann.let-map/sym-map">`sym-map`</a> [📃](https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L26-L36)
<a name="dsann.let-map/sym-map"></a>
``` clojure

(sym-map & syms)
```


Macro.


Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test
