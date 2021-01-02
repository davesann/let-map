# let-m

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
(require '[dsann.let-m :refer [let-m])

(let-m
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

## Variants

* assoc-m:
  * like let-m but add to a provided map
* sym-m:
  * create a map from a list of symbols. Keys will be the symbol names
* assoc-syms
  * like sym-m but add a provided map

## Examples
See namespace: dsann.let-m-test
