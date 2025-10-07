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

<<<<<<< HEAD
=======
### ns let-map 

>>>>>>> namespaced-keys
```clojure
(require '[dsann.let-map :refer [let-map])

(let-map
  a 1
  b (inc a)
  ...)

=> {:a 1 :b 2}

```

<<<<<<< HEAD
=======
### ns let-map2

let-map2 should be preferred because the syntax is closer to let.
It also adds additional features: namespaced keywords and value filtering (particularly nils)

```clojure
(require '[dsann.let-map2 :refer [let-map])

(let-map
  [a 1
   b (inc a)
   ...])

=> {:a 1 :b 2}

```

refer to dsann.let-map2-test for examples


>>>>>>> namespaced-keys
## Deps

```clojure
<%= git-coord-name %> {:git/tag "<%= latest-git-tag %>" :git/sha "<%= latest-git-sha %>"}
```

if you want to list all versions

```
git show-ref --tags --abbrev
```

## Supported dialects

- clj
- cljs
- bb

## Features

- Names that start \_ are removed from the final map
  - Useful for intermediate values
- Destructuring works
- Refer to tests for examples: [let-map tests](src/test/clj/dsann/let_map_test.cljc)
- since v8:
  - The input map type will be retained when using assoc versions
  - If you want to let-map a specific map type then `(let-assoc (my-map-type) a 1 ...)`

<<<<<<< HEAD
## Variants
=======
## Variants in ns let-map

- let-assoc:
  - like let-map but add to a provided map
- sym-map:
  - create a map from a list of symbols. Keys will be the symbol names
- assoc-syms
  - like sym-map but add to a provided map
  
## Variants in ns let-map2
>>>>>>> namespaced-keys

- let-assoc:
  - like let-map but add to a provided map
- sym-map:
  - create a map from a list of symbols. Keys will be the symbol names
- assoc-syms
  - like sym-map but add to a provided map

<<<<<<< HEAD
## Examples

See namespace: [dsann.let-map-test](src/test/clj/dsann/let_map_test.cljc)
=======
### namespaced keywords

all variants that end in -ns create namespaced keywords

- let-map-ns
- let-assoc-ns
- sym-map-ns
- assoc-syms-ns

### value filtering variants 

all variants that end in a '-' filter based on values. the default is to filter nils.
  
- let-map-
- let-assoc-
- sym-map-
- assoc-syms-

- let-map-ns-
- let-assoc-ns-
- sym-map-ns-
- assoc-syms-ns-

## Examples

See namespaces: 
- [dsann.let-map-test](src/test/clj/dsann/let_map_test.cljc)
- [dsann.let-map2-test](src/test/clj/dsann/let_map2_test.cljc)
>>>>>>> namespaced-keys

# Other

## clj-kondo

if using clj-kondo and you are seeing unresolved-symbol errors

<<<<<<< HEAD
1. Ensure there is a .clj-kondo in your project
=======
1. Ensure there is a .clj-kondo directory in your project
>>>>>>> namespaced-keys
1. Then run the following command to copy configs to exclude these errors

```
clj-kondo --lint "$(clojure -Spath)" --copy-configs --skip-lint
```
