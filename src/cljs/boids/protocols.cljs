(ns boids.protocols)

(defprotocol EuclideanVector
  "Represents a mathematical vector"
  (add [this vector] "Add to another vector")
  (subtract [this vector] "Subtract another vector from this one")
  (multiply [this scalar] "Multiply this vector by a scalar")
  (divide [this scalar] "Divide this vector by a scalar")
  (magnitude [this] "Return the length of the vector as a scalar")
  (distance [this other] "Return the scalar distance between two vectors")
  (zero [this] "Returns a zero vector of the same dimensions"))

(extend-protocol EuclideanVector
  PersistentVector
  (add [this other]
    (mapv + this other))
  (subtract [this other]
    (add this (multiply other -1)))
  (multiply [this scalar]
    (mapv (partial * scalar) this))
  (divide [this scalar]
    (mapv #(/ % scalar) this))
  (magnitude [this]
    (Math/sqrt (reduce + (map #(Math/pow % 2) this))))
  (zero [this]
    (vec (repeat (count this) 0))))


(defprotocol Behavior
  "A defined behavior for a boid"
  (delta-v [behavior boid neighbors]
    "Given a boid and a collection of neighbors, returns a vector
     representing the difference in velocity given by this
     behavior."))