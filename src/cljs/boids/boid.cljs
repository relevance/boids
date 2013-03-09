(ns boids.boid
  (:require [boids.protocols :as p]))

(let [id (atom 0)]
  (defn unique-id
    "Returns a new unique ID"
    []
    (swap! id inc)
    @id))

(defn separation
  "Returns a heading adjusted for separation"
  [heading position geometry other-boids]
  (let [avg (p/average geometry (map p/position other-boids))
        new-heading (p/towards geometry position avg)]
    new-heading))

(defn cohesion
  "Returns a heading adjusted for cohesion"
  [heading position geometry other-boids]
  heading)

(defn alignment
  "Returns a heading adjusted for alignment"
  [heading position geometry other-boids]
  heading)

(defrecord NormalBoid [id position heading]
  p/Boid
  (id [_] id)
  (position [_] position)
  (heading [_] heading)
  (update [this position heading]
    (assoc this :position position :heading heading))
  p/Behavior
  (steer [_ geometry boids boid]
    (let [position (p/position boid)
          other-boids (filter #(not (= % boid)) boids)]
      (-> (p/heading boid)
          (separation position geometry other-boids)
          (cohesion position geometry other-boids)
          (alignment position geometry other-boids)))))

(defn create
  "Returns a NormalBoid at a random position and heading on the given
  geometry"
  [geometry]
  (->NormalBoid (unique-id)
                (p/random-position geometry)
                (p/random-heading geometry)))

