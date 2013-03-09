(ns boids.geometry2d
  (:require [boids.protocols :as p]))

(defn w+
  "Wrapping addition"
  [max a b]
  (let [result (+ a b)]
    (cond
     (< max result) (- result max)
     (< result 0) (+ result max)
     :else result)))

(defn distance-1d
  "Determine the distance between two values in a single dimension,
  with wrapping."
  [max a b]
  (let [unwrapped (Math/abs (- a b))
        wrapped (- max unwrapped)]
    (Math/min wrapped unwrapped)))

(defn offset
  "Determine the smallest offset between two values."
  [max a b]
  (let [d (distance-1d max a b)]
    (if (< a b) (* -1 d) d)))

(defrecord Geometry2D [width height]
  p/Geometry

  (move [_ position heading speed]
    (let [[x y] position
          delta-x (* (Math/sin heading) speed)
          delta-y (* (Math/cos heading) (* -1 speed))]
      [(w+ width x delta-x) (w+ height y delta-y)]))

  (distance [_ p1 p2]
    (let [[x1 y1] p1
          [x2 y2] p2
          dist-x (distance-1d width x1 x2)
          dist-y (distance-1d height y1 y2)]
      (Math/sqrt (+ (Math/pow dist-x 2)
                    (Math/pow dist-y 2)))))

  (towards [_ [x1 y1] [x2 y2]]
    (let [ox (offset width x1 x2)
          oy (offset height y1 y2)]
      (Math/atan2 oy ox)))

  (average [_ positions]
    (let [num (count positions)
          sum-x (reduce + (map first positions))
          sum-y (reduce + (map second positions))]
      [(/ sum-x num) (/ sum-y num)]))

  (random-position [_]
    [(rand-int width)
     (rand-int height)])

  (random-heading [_]
    (rand (* 2 Math/PI)))
)