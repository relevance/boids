(ns boids.view)

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

(defn draw-boid
  "Draws a boid aligned with the positive X axis."
  [ctx]
    (.beginPath ctx)
    (.moveTo ctx 5 0) ; tip
    (.lineTo ctx -5 -3) ; corner
    (.lineTo ctx -5 3)  ; corner
    (.lineTo ctx 5 0) ; finish
    (set! (.-lineWidth ctx) 1)
    (set! (.-strokeStyle ctx) "black")
    (.stroke ctx))

(defn render-boid
  "Renders a boid, complete with placement and rotation"
  [ctx boid]
  (let [[x y] (:pos boid)
        [dx dy] (:vel boid)
        angle (Math/atan2 dy dx)]
    (.save ctx)
    (.translate ctx x y)
    (.rotate ctx angle)
    (draw-boid ctx)
    (.restore ctx)))

(defn render
  "Given a collection of boids, render them to the canvas"
  [canvas boids]
  (let [ctx (.getContext canvas "2d")]
    (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
    (doseq [boid boids]
      (render-boid ctx boid))))

(defn init
  "Given an atom containing a seq of boids, initialize the view"
  [boids]
  (let [canvas (.createElement js/document "canvas")]
    (.setAttribute canvas "width" (.-innerWidth js/window))
    (.setAttribute canvas "height" (.-innerHeight js/window))
    (.appendChild (.-body js/document) canvas)
    (add-watch boids :renderer (fn [_ _ _ boids]
                                 (render canvas boids)))
    (init-fps-monitor boids)))
