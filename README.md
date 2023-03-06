# let-map

## Status
![example workflow](https://github.com/davesann/let-map/actions/workflows/tests.yml/badge.svg)

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

## Deps

```clojure
io.github.davesann/let-map {:git/tag "v6" :git/sha "48015fb"}
```

if you want to list all versions
```
git show-ref --tags --abbrev
```

## Supported dialects

* clj
* cljs
* bb

## Features

* Names that start _ are removed from the final map
  * Useful for intermediate values
* Destructuring works
* Refer to tests for examples: [let-map tests](src/test/clj/dsann/let_map_test.cljc)

## Variants

* let-assoc:
  * like let-map but add to a provided map
* sym-map:
  * create a map from a list of symbols. Keys will be the symbol names
* assoc-syms
  * like sym-map but add to a provided map

## Examples
See namespace: [dsann.let-map-test](src/test/clj/dsann/let_map_test.cljc)


# Other

## clj-kondo
if using clj-kondo and you are seeing unresolved-symbol errors

. Ensure there is a .clj-kondo in your project
. Then run the following command to copy configs to exclude these errors

```
clj-kondo --lint "$(clojure -Spath)" --copy-configs --skip-lint
```
