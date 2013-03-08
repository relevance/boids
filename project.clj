(defproject boids "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [compojure "1.1.5"]]
  :source-paths ["src/clj"]
  :main boids.server
  :plugins [[lein-cljsbuild "0.3.0"]]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:optimizations :none
                                   :output-to "resources/public/build/deps.js"
                                   :output-dir "resources/public/build"}}]})
