(ns boids.protocols)

(defprotocol Math
  "Model for operations involving position and heading."
  (update-position [this position heading speed]
    "Giving a position, a heading and a speed, return a new
    position.")
  (combine-headings [this heading-1 heading-2]
    "Combine two headings into a single heading."))

(defprotocol Boid
  "A representation of a boid"
  (position [this]
    "Returns a position vector (in Cartesian coordinates)")
  (heading [this]
    "Returns a heading vector (in radians)"))

(defprotocol Behavior
  "A model of a boid's behavior"
  (delta-heading [this boid flock]
    "Given a boid and a flock (a sequence of other boids), return a
    delta heading of the boid."))

(defprotocol View
  "A dynamically updating view of the simulation."
  (add [view boid-atom]
    "Adds a boid atom to the view."))


