(ns boids.control-panel)

(defn update-fps
  [div frame-count]
  (set! (.-innerHTML div) @frame-count)
  (reset! frame-count 0))

(defn init-fps-monitor
  "Create and maintain a FPS indicator that counts how often the the
  boids atom updates."
  [boids]
  (let [frame-count (atom 0)
        div (.createElement js/document "div")
        style (.-style div)]
    (set! (.-color style) "white")
    (set! (.-backgroundColor style) "black")
    (set! (.-zIndex style) 1)
    (set! (.-position style) "absolute")
    (set! (.-top style) 0)
    (set! (.-left style) 0)
    (.appendChild (.-body js/document) div)
    (add-watch boids :framecounter #(swap! frame-count inc))
    (.setInterval js/window #(update-fps div frame-count) 1000)))

(defn init
  "Initialize the control panel"
  [options-atom flock-atom]
  (init-fps-monitor flock-atom))