(ns boids.behaviors
  (:require [boids.euclidean-vector :as v]))

(defn seek
  "Return a vector pointing from the source to the target, limited by
  the maximum force"
  [options source target]
  (v/limit (v/sub target source) (:steer-force options)))

(defn nearby
  "Given a boid and a flock, return a collection of the boids within a
  certain distance of the boid, not including the boid itself."
  [boid flock dist]
  (filter #(and (< (v/distance (:pos %) (:pos boid)) dist)
                (not= % boid)) flock))

(defn cohesion
  "Returns an acceleration EuclideanVector for a boid representing
  flock cohesion, using a given options map."
  [options boid flock]
  (let [cohere-with (nearby boid flock (:cohere-distance options))]
    (if (zero? (count cohere-with))
      (v/zero (:pos boid))
      (let [center (v/div (reduce v/add (map :pos cohere-with))
                          (count cohere-with))]
        (seek options (:pos boid) center)))))

(defn avoidance
  "Returns an acceleration EuclideanVector for a boid representing
  avoidance of neighbors, using the given options map."
  [options boid flock]
  (let [avoid (nearby boid flock (:avoid-distance options))]
    (if (zero? (count avoid))
      (v/zero (:pos boid))
      (let [direction (reduce (fn [steer pos]
                                (-> (v/sub (:pos boid) pos)
                                    (v/scale 1)
                                    (v/add steer)))
                              (v/zero (:pos boid))
                              (map :pos avoid))]
        (v/limit direction (:steer-force options))))))

(defn alignment
  "Returns an acceleration EuclideanVector for a boid representing the
  way a flock aligns velocities, using the given options map"
  [options boid flock]
  (let [align-with (nearby boid flock (:align-distance options))]
    (if (zero? (count align-with))
      (v/zero (:pos boid))
      (let [s (reduce v/add (map :vel flock))
            avg (v/div s (count flock))
            dir (v/scale avg (:max-speed options))
            steer (v/sub dir (:vel boid))]
        (v/limit steer (:steer-force options))))))

(defn goal
  "Returns an acceleration EuclideanVector towards a specific point,
  using the given options map."
  [options boid flock]
  (seek options (:pos boid) (:goal options)))
