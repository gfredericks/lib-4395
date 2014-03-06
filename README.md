# lib-4395

A Clojure library for fluently working with various algebras.

## Usage

E.g., to set up a namespace to fluently do arithmetic on the ring of
integers mod 10:

``` clojure
(ns my.mod-10
  (:refer-clojure :exclude [+ - * /])
  (:require [clojure.core :as core]
            [com.gfredericks.lib-4395 :as alg]))

(alg/defs-keys + - *
  (alg/compile-ring 0 1 ; the two identities
                    ;; plus
                    #(mod (core/+ %1 %2) 10)
                    ;; negate
                    #(mod (core/- %) 10)
                    ;; times
                    #(mod (core/* %1 %2) 10)))

;; repl
my.mod-10> (* 3 7 8)
8
my.mod-10> (+)
0
my.mod-10> (- 9 8 7)
4
```

You can create a map of functions with `compile-monoid`, `compile-group`, `compile-ring`,
and `compile-field`. Then you can (if desired) def those functions succinctly with `defs-keys`.


## License

Copyright © 2014 Gary Fredericks

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
