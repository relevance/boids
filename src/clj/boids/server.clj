(ns boids.server
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [compojure.route :as route]))

(defn -main [& args]
  (jetty/run-jetty (route/resources "/")
                   {:port 3000}))

