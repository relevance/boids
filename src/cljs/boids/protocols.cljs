(ns boids.protocols)

(defprotocol EuclideanVector
  "Represents a mathematical vector"
  (add [this vector] "Add to another vector")
  (multiply [this scalar] "Multiply this vector by a scalar"))

(extend-type PersistentVector
  EuclideanVector
  (add [this other]
    (mapv + this other))
  (multiply [this scalar]
    (mapv (partial * scalar) this)))

