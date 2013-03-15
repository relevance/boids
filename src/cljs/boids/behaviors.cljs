(ns boids.behaviors
  (:require [boids.euclidean-vector :as v]))

(def steer-force 0.1)
(def max-speed 6)

(def cohere-distance 300)
(def avoid-distance 50)
(def align-distance 200)

(defn seek
  "Return a vector pointing from the source to the target, limited by
  the maximum force"
  [source target]
  (v/limit (v/sub target source) steer-force))

(defn nearby
  "Given a boid and a flock, return a collection of the boids within a
  certain distance of the boid, not including the boid itself."
  [boid flock dist]
  (filter #(and (< (v/distance (:pos %) (:pos boid)) dist)
                (not= % boid)) flock))

(defn cohesion
  "An acceleration for a boid representing flock cohesion."
  [boid flock]
  (let [cohere-with (nearby boid flock cohere-distance)]
    (if (zero? (count cohere-with))
      (v/zero (:pos boid))
      (let [center (v/div (reduce v/add (map :pos cohere-with))
                          (count cohere-with))]
        (seek (:pos boid) center)))))

(defn avoidance
  "An acceleration for a boid representing avoidance of neighbors."
  [boid flock]
  (let [avoid (nearby boid flock avoid-distance)]
    (if (zero? (count avoid))
      (v/zero (:pos boid))
      (let [direction (reduce (fn [steer pos]
                                (-> (v/sub (:pos boid) pos)
                                    (v/scale 1)
                                    (v/add steer)))
                              (v/zero (:pos boid))
                              (map :pos avoid))]
        (v/limit direction steer-force)))))


(defn alignment
  "An acceleration for a boid representing the way a flock aligns
  velocities."
  [boid flock]
  (let [align-with (nearby boid flock align-distance)]
    (if (zero? (count align-with))
      (v/zero (:pos boid))
      (let [s (reduce v/add (map :vel flock))
            avg (v/div s (count flock))
            dir (v/scale avg max-speed)
            steer (v/sub dir (:vel boid))]
        (v/limit steer steer-force)))))

(defn goal
  "An acceleration towards a specific point"
  [boid flock]
  (let [origin [(/ (.-innerWidth js/window) 2)
                (/ (.-innerHeight js/window) 2)]
        d (v/distance (:pos boid) origin)]
    (seek (:pos boid) origin)))