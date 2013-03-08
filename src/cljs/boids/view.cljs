(ns boids.view
  (:require [boids.protocols :as p]))

(defn create
  "Initializes a view and adds it to the provided HTML element."
  [element]
  (let [canvas (.createElement js/document "canvas")]
    (.setAttribute canvas "width" (.-innerWidth js/window))
    (.setAttribute canvas "height" (.-innerHeight js/window))
    (.appendChild element canvas)))
