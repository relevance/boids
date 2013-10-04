(defproject boids "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1909"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [compojure "1.1.5"]]
  :source-paths ["src/clj"]
  :main boids.server
  :plugins [[lein-cljsbuild "0.3.3"]]
  :cljsbuild {:crossovers [boids.euclidean-vector boids.behaviors]
              :builds [{:source-paths ["src/cljs"]
                        :compiler {:optimizations :none
                                   :output-to "resources/public/build/deps.js"
                                   :output-dir "resources/public/build"}}
                       {:source-paths ["src/cljs"]
                        :compiler {:optimizations :advanced
                                   :pretty-print false
                                   :externs ["resources/public/lib/jquery-externs.js"]
                                   :output-to "resources/public/build/advanced.js"}}]})

