(ns boids.behaviors
  (:require [boids.protocols :as p]))

(def cohesion
  (reify p/Behavior
    (delta-v [_ boid neighbors]
      (let [center (p/divide (reduce p/add (map :position neighbors))
                             (count neighbors))]
        (p/divide (p/subtract center (:position boid))
                  100)))))

(def avoid-distance 50)

(def avoidance
  (reify p/Behavior
    (delta-v [_ boid neighbors]
      (let [near-neighbors (filter #(< (p/magnitude (p/subtract (:position %)
                                                                (:position boid)))
                                       avoid-distance)
                                   neighbors)]
        (if (< 0 (count near-neighbors))
          (let [center (p/divide (reduce p/add (map :position near-neighbors))
                                 (count near-neighbors))]
            (p/divide (p/subtract center (:position boid)) -75))
          (p/zero (:velocity boid)))))))

#_(def avoidance
  (reify p/Behavior
    (delta-v [_ boid neighbors]
      (reduce (fn [v b]
                (let [offset (p/subtract (:position boid) (:position b))
                      distance (p/magnitude offset)]
                  (if (< distance avoid-distance)
                    (p/subtract v offset)
                    v)))
              (p/zero (:velocity boid))
              neighbors))))

(def alignment
  (reify p/Behavior
    (delta-v [_ boid neighbors]
      (let [pv (p/divide (reduce p/add (map :velocity neighbors))
                         (count neighbors))]
        (p/divide (p/subtract pv (:velocity boid))
                  4)))))