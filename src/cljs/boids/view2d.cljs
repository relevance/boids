(ns boids.view2d
  (:require [boids.protocols :as p]))

(defn draw-boid
  "Draws a boid, assuming placement and rotation are already in the context"
  [ctx]
    (.beginPath ctx)
    (.moveTo ctx 0 -5) ; top corner
    (.lineTo ctx -2 5) ; bottom left corner
    (.lineTo ctx 2 5)  ; bottom right corner
    (.lineTo ctx 0 -5) ; finish
    (set! (.-lineWidth ctx) 1)
    (set! (.-strokeStyle ctx) "black")
    (.stroke ctx))

(defn render-boid
  "Renders a boid, complete with placement and rotation"
  [ctx boid]
  (let [[x y] (p/position boid)
        angle (p/heading boid)]
    (.save ctx)
    (.translate ctx x y)
    (.rotate ctx angle)
    (draw-boid ctx)
    (.restore ctx)))

(defn render
  "Given a canvas and a set of boids, renders the scene."
  [canvas boids]
  (let [ctx (.getContext canvas "2d")
        c (count boids)]
    (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
    (doseq [boid boids]
      (render-boid ctx boid))))

;; Note: Geometry must be a Geometry2D
(defrecord View2D [geometry]
  p/View
  (initialize [_ element boids]
    (let [canvas (.createElement js/document "canvas")]
      (.setAttribute canvas "width" (:width geometry))
      (.setAttribute canvas "height" (:height geometry))
      (.appendChild element canvas)
      (add-watch boids :view (fn [_ _ _ state]
                               (render canvas state)))
      (render canvas @boids))))