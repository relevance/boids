(ns boids.main
  (:require [boids.euclidean-vector :as v]
            [boids.view :as view]
            [boids.behaviors :as behaviors]))

(def num-boids 15)

(defn create-boid
  "Returns a new boid with a random position on the screen."
  []
  {:pos [(rand-int (.-innerWidth js/window))
              (rand-int (.-innerHeight js/window))]
   :vel [0 0]})

;; Behaviors and weights
(def behaviors { behaviors/cohesion 1
                 behaviors/avoidance 1.5
                 behaviors/alignment 1
                 behaviors/goal 0.8})

(defn update-boid
  "Given a collection containing the flock, and an individual boid,
  return an updated boid."
  [boid flock]
  (let [accelerations (map (fn [[behavior weight]]
                             (v/mul (behavior boid flock) weight))
                           behaviors)
        velocity (v/limit (reduce v/add (:vel boid) accelerations) behaviors/max-speed)]
    {:pos (v/add (:pos boid) velocity)
     :vel velocity}))

(defn update-flock
  "Given a flock (a collection of boids), return an updated flock"
  [flock]
  (map #(update-boid % flock) flock))

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
  [flock-atom]
  (swap! flock-atom update-flock)
  (requestAnimationFrame #(tick flock-atom)))

(defn ^:export main
  "Starts everything running"
  []
  (let [flock-atom (atom (repeatedly num-boids #(create-boid)))]
    (view/init flock-atom)
    (tick flock-atom)))

