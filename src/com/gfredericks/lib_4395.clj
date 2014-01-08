(ns com.gfredericks.lib-4395)

(defmacro defs-keys
  "E.g., (defs-keys foo bar
           m)
         gives
         (def foo (:foo m))
           and
         (def bar (:bar m))
  more or less."
  [& args]
  (let [mm (gensym)]
    `(let [~mm ~(last args)]
       ~@(for [sym (butlast args)
               :let [kw (keyword (str sym))]]
           `(def ~sym (~kw ~mm))))))

(defn compile-monoid
  "Returns a variadic function."
  [zero plus]
  (fn
    ([] zero)
    ([x] x)
    ([x y] (plus x y))
    ([x y & zs] (reduce plus (plus x y) zs))))

(defn compile-minus
  "Returns a function whose behavior mimics clojure.core/-"
  [plus negate]
  (fn
    ([x] (negate x))
    ([x y & zs] (plus x (negate (reduce plus y zs))))))

;; TODO: optional validators?
(defn compile-group
  "Returns a map with :+ and :-."
  [zero plus negate]
  {:+ (compile-monoid zero plus)
   :- (compile-minus plus negate)})

(defn compile-ring
  "Returns a map with :+, :-, :*"
  [zero one plus negate times]
  {:+ (compile-monoid zero plus)
   :* (compile-monoid one times)
   :- (compile-minus plus negate)})

(defn compile-field
  "Returns a map with :+, :-, :*, :/"
  [zero one plus negate times invert]
  (assoc (compile-ring zero one plus negate times)
    :/ (compile-minus times invert)))
