(ns boids.main
  (:require [boids.protocols :as p]
            [boids.geometry2d :as g]
            [boids.boid :as b]
            [boids.view2d :as v]))

(def num-boids 10)

(def speed 5)

(defn requestAnimationFrame
  "Invokes the appropriate requestAnimationFrame function for the
  user's browser"
  [callback]
  (cond
   (.-requestAnimationFrame js/window)
   (.requestAnimationFrame js/window callback)
   (.-webkitRequestAnimationFrame js/window)
   (.webkitRequestAnimationFrame js/window callback)
   (.-mozRequestAnimationFrame js/window)
   (.mozRequestAnimationFrame js/window callback)
   :default (js/alert "Browser does not support animation")))

(defn simulate
  "Given a geometry, a collection of all boids and a particular boid,
  returns an updated boid"
  [geometry all-boids boid]
  (let [heading (p/heading boid)
        new-position (p/move geometry (p/position boid) heading speed)
        new-heading (p/steer boid geometry all-boids boid)]
    (p/update boid new-position new-heading)))

(defn simulate-all
  "Given a geometry and a collection of boids, return a collection of
  updated boids"
  [geometry boids]
  (map (partial simulate geometry boids) boids))

(defn ms
  "Returns the current clock time in ms"
  []
  (.getMilliseconds (js/Date.)))

(let [frames (atom 0)]
  (defn count-frame [] (swap! frames inc))
  (defn report-fps
    "Updates the frame rate display every second"
    []
    (let [div (.getElementById js/document "framerate")]
      (set! (.-innerHTML div) @frames))
    (reset! frames 0)
    (.setTimeout js/window report-fps 1000)))

(defn tick
  "Executes one frame of simulation. Returns a smoothed average of the
  frame rate."
  [geometry boids-atom]
  (swap! boids-atom (partial simulate-all geometry))
  (count-frame)
  (requestAnimationFrame #(tick geometry boids-atom)))

(defn main
  "Starts everything running"
  []
  (let [geometry (g/->Geometry2D (.-innerWidth js/window)
                                 (.-innerHeight js/window))
        view (v/->View2D geometry)
        boids-atom (atom (repeatedly num-boids #(b/create geometry)))]
    (p/initialize view (.-body js/document) boids-atom)
    (report-fps)
    (tick geometry boids-atom)))