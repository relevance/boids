(ns boids.brepl
  (:require [clojure.browser.repl :as repl]))

(defn connect-repl
  "Connects the browser REPL"
  []
  (repl/connect "http://localhost:9000/repl"))
