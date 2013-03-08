(ns boids.geometry2d
  (:require [boids.protocols :as p]))

(defn w+
  "Wrapping addition"
  [max & addends]
  (let [result (apply + addends)]
    (cond
     (< max result) (- result max)
     (< result 0) (+ result max)
     :else result)))

(defrecord Geometry2D [width height]
  p/Geometry
  (move [_ position heading speed]
    (let [[x y] position
          delta-x (* (Math/sin heading) speed)
          delta-y (* (Math/cos heading) (* -1 speed))]
      [(w+ width x delta-x) (w+ height y delta-y)]))
  (random-position [_]
    [(rand-int width)
     (rand-int height)])
  (random-heading [_]
    (rand (* 2 Math/PI))))