(ns boids.main
  (:require [boids.euclidean-vector :as v]
            [boids.view :as view]
            [boids.behaviors :as behaviors]))

;; A boid consists of a position and a velocity (both EuclideanVectors)
(defrecord Boid [pos vel])

(def num-boids 10)

(def default-options {:steer-force 0.1
                      :max-speed 6
                      :cohere-distance 300
                      :avoid-distance 50
                      :align-distance 200
                      :goal [(/ (.-innerWidth js/window) 2)
                             (/ (.-innerHeight js/window) 2)]})

(defn create-boid
  "Returns a new boid with a random position on the screen."
  []
  (map->Boid {:pos [(rand-int (.-innerWidth js/window))
                    (rand-int (.-innerHeight js/window))]
              :vel [0 0]}))

;; Behaviors and weights
(def behaviors { behaviors/cohesion  1
                 behaviors/avoidance 1
                 behaviors/alignment 1
                 behaviors/goal      1 })

(defn update-boid
  "Given a collection containing the flock and an individual boid,
  return an updated boid, using the provided options atom."
  [options-atom boid flock]
  (let [accelerations (map (fn [[behavior weight]]
                             (v/mul (behavior @options-atom boid flock) weight))
                           behaviors)
        velocity (v/limit (reduce v/add (:vel boid) accelerations) behaviors/max-speed)]
    {:pos (v/add (:pos boid) velocity)
     :vel velocity}))

(defn update-flock
  "Given a flock (a collection of boids), return an updated flock"
  [options-atom flock]
  (map #(update-boid options-atom % flock) flock))

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
  "The main 'loop' of the simulation."
  [options-atom flock-atom]
  (swap! flock-atom (partial update-flock options-atom))
  (requestAnimationFrame #(tick options-atom flock-atom)))

(defn main
  "Starts everything running"
  []
  (let [options-atom (atom default-options)
        flock-atom (atom (repeatedly num-boids #(create-boid)))]
    (view/init flock-atom)
    (tick options-atom flock-atom)))

