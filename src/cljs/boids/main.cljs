(ns boids.main
  (:require [boids.protocols :as p]
            [boids.view :as view]
            [boids.behaviors :as behaviors]))

(def num-boids 20)

(defn create-boid
  "Returns a new boid with a random position on the screen"
  []
  {:position [(rand-int (.-innerWidth js/window))
              (rand-int (.-innerHeight js/window))]
   :velocity [0 0]})

(def behaviors [behaviors/cohesion
                behaviors/avoidance
                behaviors/alignment])

(def neighbor-distance 200)

(defn neighbor
  "Return true if the boids are neighbors. No boid is a neighbor to
  itself."
  [b1 b2]
  (and (not= b1 b2)
       (< (p/magnitude (p/subtract (:position b1)
                                   (:position b2)))
          neighbor-distance)))

(defn update-boid
  "Given a collection of all the boids and a single boid, return an
  updated boid."
  [boids boid]
  (let [neighbors (filter #(not= boid %) boids)
        deltas (map #(p/delta-v % boid neighbors) behaviors)
        new-velocity (reduce p/add (:velocity boid) deltas)]
    {:position (p/add (:position boid) (:velocity boid))
     :velocity new-velocity}))

(defn update-boids
  "Given a collection of boids, return an updated collection of boids."
  [boids]
  (map #(update-boid boids %) boids))

(defn requestAnimationFrame
  "Cross-browser wrapper for requestAnimationFrame"
  [callback]
  (cond
   (.-requestAnimationFrame js/window)
   (.requestAnimationFrame js/window callback)
   (.-webkitRequestAnimationFrame js/window)
   (.webkitRequestAnimiationFrame js/window callback)
   (.-mozRequestAnimationFrame js/window)
   (.mozRequestAnimationFrame js/window callback)))

(defn tick
  "Execute one frame of the simulation"
  [boids]
  (swap! boids update-boids)
  (requestAnimationFrame #(tick boids)))

(defn ^:export main
  "Starts everything running"
  []
  (let [boids (atom (repeatedly num-boids #(create-boid)))]
    (view/init boids)
    (tick boids)))

