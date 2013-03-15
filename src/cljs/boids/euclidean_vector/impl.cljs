(ns boids.euclidean-vector.impl
  (:require [boids.euclidean-vector :as v]))

(extend-protocol v/EuclideanVector
  cljs.core.PersistentVector
  (add [this vector]
    (mapv + this vector))
  (sub [this vector]
    (mapv - this vector))
  (mul [this scalar]
    (mapv #(* % scalar) this))
  (div [this scalar]
    (mapv #(/ % scalar) this))
  (magnitude [this]
    (Math/sqrt (reduce + (map #(Math/pow % 2) this))))
  (distance [this vector]
    (v/magnitude (v/sub this vector)))
  (scale [this scalar]
    (let [m (v/magnitude this)]
      (if (< 0 m)
        (v/mul (v/div this m) scalar)
        (v/mul this 0))))
  (limit [this scalar]
    (let [m (v/magnitude this)]
      (if (< scalar m)
        (v/scale this scalar)
        this))))