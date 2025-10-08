# Table of contents
-  [`dsann.let-map`](#dsann.let-map) 
    -  [`assoc-syms`](#dsann.let-map/assoc-syms) - like sym-map but assocs to supplied map.
    -  [`let-assoc`](#dsann.let-map/let-assoc) - Like let-map, but assocs into the supplied map.
    -  [`let-map`](#dsann.let-map/let-map) - Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...} For examples, see: dsann.let-map-test.
    -  [`sym-map`](#dsann.let-map/sym-map) - Creates a map from a seq of symbols.
-  [`dsann.let-map2`](#dsann.let-map2) 
    -  [`assoc-syms`](#dsann.let-map2/assoc-syms) - As for sym-map but assocs to a supplied map.
    -  [`assoc-syms-`](#dsann.let-map2/assoc-syms-) - As for assoc-syms, adding value filtering for the final map.
    -  [`assoc-syms-ns`](#dsann.let-map2/assoc-syms-ns) - As for assoc-syms, creates keys in a provided namespace.
    -  [`assoc-syms-ns-`](#dsann.let-map2/assoc-syms-ns-) - As for assoc-syms-ns, adding value filtering for the final map.
    -  [`let-assoc`](#dsann.let-map2/let-assoc) - As for let-map but assocs into a supplied map.
    -  [`let-assoc-`](#dsann.let-map2/let-assoc-) - As for let-assoc, adding value filtering for the final map.
    -  [`let-assoc-ns`](#dsann.let-map2/let-assoc-ns) - As for let-assoc but creates keys in a provided namespace.
    -  [`let-assoc-ns-`](#dsann.let-map2/let-assoc-ns-) - As for let-assoc-ns, adding value filtering for the final map.
    -  [`let-map`](#dsann.let-map2/let-map) - Takes a list of name-value pairs and returns a map (works like 'let').
    -  [`let-map-`](#dsann.let-map2/let-map-) - As for let-map, adding value filtering for the final map.
    -  [`let-map-ns`](#dsann.let-map2/let-map-ns) - As for let-map but creates keys in a provided namespace.
    -  [`let-map-ns-`](#dsann.let-map2/let-map-ns-) - As for let-map-ns, adding value filtering for the final map.
    -  [`resolve-ns-name`](#dsann.let-map2/resolve-ns-name) - (internal) Resolves the input to a namespace name for use in namespaced keywords.
    -  [`sym-map`](#dsann.let-map2/sym-map) - Creates a map from a seq of symbols.
    -  [`sym-map-`](#dsann.let-map2/sym-map-) - As for sym-map, adding value filtering for the final map.
    -  [`sym-map-ns`](#dsann.let-map2/sym-map-ns) - As for sym-map but creates keys in a provided namespace.
    -  [`sym-map-ns-`](#dsann.let-map2/sym-map-ns-) - As for sym-map-ns, adding value filtering for the final map.
    -  [`syms->map-entries`](#dsann.let-map2/syms->map-entries) - (internal) Convert symbols into a vector of map entries [k v].

-----
# <a name="dsann.let-map">dsann.let-map</a>






## <a name="dsann.let-map/assoc-syms">`assoc-syms`</a>
``` clojure

(assoc-syms m & syms)
```
Macro.

like sym-map but assocs to supplied map.
     For examples, see: dsann.let-map-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L27-L37">Source</a></sub></p>

## <a name="dsann.let-map/let-assoc">`let-assoc`</a>
``` clojure

(let-assoc m & args)
```
Macro.

Like let-map, but assocs into the supplied map.
     For examples, see: dsann.let-map-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L46-L54">Source</a></sub></p>

## <a name="dsann.let-map/let-map">`let-map`</a>
``` clojure

(let-map & args)
```
Macro.

Takes a list of name-value pairs and returns a map: (let-map a 1 ...) => {:a 1 ...}
      For examples, see: dsann.let-map-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L56-L60">Source</a></sub></p>

## <a name="dsann.let-map/sym-map">`sym-map`</a>
``` clojure

(sym-map & syms)
```
Macro.

Creates a map from a seq of symbols.
     Symbol names are converted to keywords as the key to the value it holds
     For examples, see: dsann.let-map-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map.cljc#L39-L44">Source</a></sub></p>

-----
# <a name="dsann.let-map2">dsann.let-map2</a>






## <a name="dsann.let-map2/assoc-syms">`assoc-syms`</a>
``` clojure

(assoc-syms m syms)
```
Macro.

As for sym-map but assocs to a supplied map.

     (let [a 1 b 2] (assoc-syms some-map a b)) => {... :a 1 :b 2}

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L65-L73">Source</a></sub></p>

## <a name="dsann.let-map2/assoc-syms-">`assoc-syms-`</a>
``` clojure

(assoc-syms- m syms)
(assoc-syms- remove? m syms)
```
Macro.

As for assoc-syms, adding value filtering for the final map.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L108-L117">Source</a></sub></p>

## <a name="dsann.let-map2/assoc-syms-ns">`assoc-syms-ns`</a>
``` clojure

(assoc-syms-ns m syms)
(assoc-syms-ns target-ns m syms)
```
Macro.

As for assoc-syms, creates keys in a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L152-L165">Source</a></sub></p>

## <a name="dsann.let-map2/assoc-syms-ns-">`assoc-syms-ns-`</a>
``` clojure

(assoc-syms-ns- m syms)
(assoc-syms-ns- remove? target-ns m syms)
```
Macro.

As for assoc-syms-ns, adding value filtering for the final map.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L214-L223">Source</a></sub></p>

## <a name="dsann.let-map2/let-assoc">`let-assoc`</a>
``` clojure

(let-assoc m args)
```
Macro.

As for let-map but assocs into a supplied map.

       (let-assoc some-map [a 1 b (inc a)]) => {... :a 1 :b 2}

       For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L85-L96">Source</a></sub></p>

## <a name="dsann.let-map2/let-assoc-">`let-assoc-`</a>
``` clojure

(let-assoc- m args)
(let-assoc- remove? m args)
```
Macro.

As for let-assoc, adding value filtering for the final map.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L128-L139">Source</a></sub></p>

## <a name="dsann.let-map2/let-assoc-ns">`let-assoc-ns`</a>
``` clojure

(let-assoc-ns m args)
(let-assoc-ns target-ns m args)
```
Macro.

As for let-assoc but creates keys in a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L181-L196">Source</a></sub></p>

## <a name="dsann.let-map2/let-assoc-ns-">`let-assoc-ns-`</a>
``` clojure

(let-assoc-ns- m args)
(let-assoc-ns- target-ns m args)
(let-assoc-ns- remove? target-ns m args)
```
Macro.

As for let-assoc-ns, adding value filtering for the final map.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L237-L251">Source</a></sub></p>

## <a name="dsann.let-map2/let-map">`let-map`</a>
``` clojure

(let-map args)
```
Macro.

Takes a list of name-value pairs and returns a map (works like 'let').

       (let-map [a 1 ...]) => {:a 1 ...}

       For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L98-L105">Source</a></sub></p>

## <a name="dsann.let-map2/let-map-">`let-map-`</a>
``` clojure

(let-map- args)
(let-map- remove? args)
```
Macro.

As for let-map, adding value filtering for the final map.
      Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

      For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L141-L148">Source</a></sub></p>

## <a name="dsann.let-map2/let-map-ns">`let-map-ns`</a>
``` clojure

(let-map-ns args)
(let-map-ns target-ns args)
```
Macro.

As for let-map but creates keys in a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L198-L209">Source</a></sub></p>

## <a name="dsann.let-map2/let-map-ns-">`let-map-ns-`</a>
``` clojure

(let-map-ns- args)
(let-map-ns- target-ns args)
(let-map-ns- remove? target-ns args)
```
Macro.

As for let-map-ns, adding value filtering for the final map.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L253-L263">Source</a></sub></p>

## <a name="dsann.let-map2/resolve-ns-name">`resolve-ns-name`</a>
``` clojure

(resolve-ns-name ns)
```
Function.

(internal) Resolves the input to a namespace name for use in namespaced keywords
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L29-L36">Source</a></sub></p>

## <a name="dsann.let-map2/sym-map">`sym-map`</a>
``` clojure

(sym-map syms)
```
Macro.

Creates a map from a seq of symbols.
       Symbol names are converted to keywords as the key to the value it holds.

     (let [a 1 b 2] (sym-map a b)) => {:a 1 :b 2}

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L75-L83">Source</a></sub></p>

## <a name="dsann.let-map2/sym-map-">`sym-map-`</a>
``` clojure

(sym-map- syms)
(sym-map- remove? syms)
```
Macro.

As for sym-map, adding value filtering for the final map.
       Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.

       For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L119-L126">Source</a></sub></p>

## <a name="dsann.let-map2/sym-map-ns">`sym-map-ns`</a>
``` clojure

(sym-map-ns syms)
(sym-map-ns ns-target syms)
```
Macro.

As for sym-map but creates keys in a provided namespace.

     The provided namespace can be: string; keyword; namespaced keyword; or symbol.
       strings, keywords and symbols takes the literal value for the namespace.
       namespaced keywords take the namespace of the keyword. includes ::x and ::alias/x resolution.
       defaults to current namespace

     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L167-L179">Source</a></sub></p>

## <a name="dsann.let-map2/sym-map-ns-">`sym-map-ns-`</a>
``` clojure

(sym-map-ns- syms)
(sym-map-ns- ns-target syms)
(sym-map-ns- remove? ns-target syms)
```
Macro.

As for sym-map-ns, adding value filtering for the final map.
     Trailing '-' indicates values are filtered out. i.e. not included if (remove? v) is true. defaults to remove nils.
     For examples, see: dsann.let-map2-test
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L225-L235">Source</a></sub></p>

## <a name="dsann.let-map2/syms->map-entries">`syms->map-entries`</a>
``` clojure

(syms->map-entries syms)
(syms->map-entries target-ns-name syms)
```
Function.

(internal) Convert symbols into a vector of map entries [k v]
<p><sub><a href="https://github.com/davesann/let-map/blob/main/src/main/clj/dsann/let_map2.cljc#L46-L61">Source</a></sub></p>
