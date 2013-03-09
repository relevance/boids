(ns boids.protocols)

(defprotocol Geometry
  "The geometry used in the simulation."
  (move [this position heading speed]
    "Given a position, heading and speed, return an updated position")
  (distance [this position-1 position-2]
    "Returns the distance between two positions")
  (average [this positions]
    "Given a sequence of positions, return a position that is its
    average.")
  (towards [this p1 p2]
    "Given two positions, return a heading from the first to the
    second.")
  (random-position [this]
    "Returns a random position")
  (random-heading [this]
    "Returns a random heading"))

(defprotocol Boid
  "A representation of a boid"
  (id [this]
    "Unique ID for the boid")
  (position [this]
    "Returns the current position")
  (heading [this]
    "Returns the current heading")
  (update [this position heading]
    "Returns a boid with updated position and heading"))

(defprotocol Behavior
  "A representation of a boid's behavior"
  (steer [this geometry boids boid]
    "Given a geometry, a collection of all boids, and a boid, return a
    new heading for the boid."))

(defprotocol View
  "A dynamically updating view of the simulation."
  (initialize [this element boids-atom]
    "Initializes a view, rendering it to the given DOM element. Also
    takes an atom containing a collection of boids; the view should
    re-render whenever the atom changes."))

