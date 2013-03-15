(ns boids.euclidean-vector)

(defprotocol EuclideanVector
  "Represents a mathematical vector"
  (add [this vector] "Add to another vector")
  (sub [this vector] "Subtract another vector from this one")
  (mul [this scalar] "Multiply this vector by a scalar")
  (div [this scalar] "Divide this vector by a scalar")
  (magnitude [this] "The magnitude of the vector as a scalar")
  (distance [this vector] "The distance between two vectors")
  (scale [this scalar] "A vector of the same direction, with the
  specified magnitude.")
  (limit [this scalar] "A vector of the same direction with an equal
  or lesser magnitude than the scalar provided")
  (zero [this] "A zero vector of the same dimension"))

(extend-protocol EuclideanVector
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
    (Math/sqrt (reduce cljs.core/+ (map #(Math/pow % 2) this))))
  (distance [this vector]
    (magnitude (sub this vector)))
  (scale [this scalar]
    (let [m (magnitude this)]
      (if (< 0 m)
        (mul (div this m) scalar)
        (mul this 0))))
  (limit [this scalar]
    (let [m (magnitude this)]
      (if (< scalar m)
        (scale this scalar)
        this)))
  (zero [this]
    (vec (repeat (count this) 0))))

