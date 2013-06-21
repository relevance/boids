(ns boids.main
  (:require [boids.euclidean-vector :as v]
            [boids.view :as view]
            [boids.control-panel :as cp]
            [boids.behaviors :as b]))

;; A boid consists of a position and a velocity (both EuclideanVectors)
(defrecord Boid [pos vel])

(def num-boids 10)

(def default-options {:steer-force 0.1
                      :max-speed 6
                      :cohere-distance 300
                      :avoid-distance 50
                      :align-distance 200
                      :cohesion-weight 1
                      :avoidance-weight 1
                      :alignment-weight 1
                      :goal-weight 1
                      :goal [(/ (.-innerWidth js/window) 2)
                             (/ (.-innerHeight js/window) 2)]})

(defn create-boid
  "Returns a new boid with a random position on the screen."
  []
  (map->Boid {:pos [(rand-int (.-innerWidth js/window))
                    (rand-int (.-innerHeight js/window))]
              :vel [0 0]}))

(defn acceleration
  "Returns an acceleration vector for the given boid."
  [options flock boid]
  (->> { b/cohesion :cohesion-weight
         b/avoidance :avoidance-weight
         b/alignment :alignment-weight
         b/goal :goal-weight }
       (map (fn [[behavior option-weight-key]]
              (v/mul (behavior options flock boid)
                     (options option-weight-key))))
       (reduce v/add)))

(defn wrap
  "Ensure that the boids never leave the screen"
  [[x y]]
  (let [w (.-innerWidth js/window)
        h (.-innerHeight js/window)]
    [(mod x w) (mod y h)]))

(defn update-boid
  "Given a collection containing the flock and an individual boid,
  return an updated boid, using the provided options."
  [options flock boid]
  (let [desired-velocity (v/add (:vel boid) (acceleration options flock boid))
        velocity (v/limit desired-velocity (:max-speed options))
        position (v/add (:pos boid) velocity)]
    (assoc boid
      :pos (wrap position)
      :vel velocity)))

(defn update-flock
  "Given a flock (a collection of boids), return an updated flock"
  [options flock]
  (map (partial update-boid options flock) flock))

(defn requestAnimationFrame
  "Cross-browser wrapper for requestAnimationFrame"
  [callback]
  (cond
   (.-requestAnimationFrame js/window)
   (.requestAnimationFrame js/window callback)
   (.-webkitRequestAnimationFrame js/window)
   (.webkitRequestAnimiationFrame js/window callback)
   (.-mozRequestAnimationFrame js/window)
   (.mozRequestAnimationFrame js/window callback)
   (.-msRequestAnimationFrame js/window)
   (.msRequestAnimationFrame js/window callback)))

(defn tick
  "The main 'loop' of the simulation."
  [options-atom flock-atom]
  (swap! flock-atom (partial update-flock @options-atom))
  (requestAnimationFrame #(tick options-atom flock-atom)))

(defn ^:export main
  "Starts everything running"
  []
  (let [options-atom (atom default-options)
        flock-atom (atom (repeatedly num-boids create-boid))]
    (cp/init options-atom flock-atom)
    (view/init flock-atom)
    (tick options-atom flock-atom)))


