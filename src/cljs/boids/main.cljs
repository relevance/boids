(ns boids.main
  (:require [boids.protocols :as p]
            [boids.view :as v]))

(defn main
  "Starts everything running"
  []
  (v/create (.-body js/document)))