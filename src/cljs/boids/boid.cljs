(ns boids.boid
  (:require [boids.protocols :as p]))

(let [id (atom 0)]
  (defn unique-id
    "Returns a new unique ID"
    []
    (swap! id inc)
    @id))

(defrecord NormalBoid [id position heading]
  p/Boid
  (id [_] id)
  (position [_] position)
  (heading [_] heading)
  (update [this position heading]
    (assoc this :position position :heading heading))
  p/Behavior
  (steer [_ geometry boids boid]
    ;; Bad - not agnostic to what a heading actually is
    (+ (p/heading boid) 0.05)))

(defn create
  "Returns a NormalBoid at a random position and heading on the given
  geometry"
  [geometry]
  (->NormalBoid (unique-id)
                (p/random-position geometry)
                (p/random-heading geometry)))

