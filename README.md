# let-map

## Why?
Abbreviation of constructs where you let vars and then want to create a map.

```clojure
(let [a 1
      b (inc a)]
  {:a a
   :b b}

```

## Usage

```clojure
(require '[dsann.let-map :refer [let-map])

(let-map
  a 1
  b (inc a)
  ...)

=> {:a 1 :b 2}

```


## Features

* clj and cljs
* Names that start _ are removed from the final map
  ** Useful for intermediate values
* Destructuring works

* Refer to tests for examples

## Variants

* let-assoc:
  * like let-map but add to a provided map
* sym-map:
  * create a map from a list of symbols. Keys will be the symbol names
* assoc-syms
  * like sym-map but add to a provided map

## Examples
See namespace: dsann.let-map-test
