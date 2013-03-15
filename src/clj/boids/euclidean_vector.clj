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
  or lesser magnitude than the scalar provided"))
