(ns boids.view)

(defn render-boid
  "Renders a boid, complete with placement and rotation"
  [ctx boid]
  (let [[x y] (:pos boid)
        [dx dy] (:vel boid)
        angle (Math/atan2 dy dx)]
    (.save ctx)
    (.translate ctx x y)
    (.rotate ctx angle)
    (.beginPath ctx)
    (.moveTo ctx 5 0) ; tip
    (.lineTo ctx -5 -3) ; corner
    (.lineTo ctx -5 3)  ; corner
    (.lineTo ctx 5 0) ; finish
    (set! (.-lineWidth ctx) 1)
    (set! (.-strokeStyle ctx) "black")
    (.stroke ctx)
    (.restore ctx)))

(defn render
  "Given a collection of boids, render them to the canvas"
  [canvas flock]
  (let [ctx (.getContext canvas "2d")]
    (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
    (doseq [boid flock]
      (render-boid ctx boid))))

(defn init-raw
  "Given an atom containing a seq of boids, initialize the view"
  [flock-atom]
  (let [canvas (.createElement js/document "canvas")]
    (.setAttribute canvas "width" (.-innerWidth js/window))
    (.setAttribute canvas "height" (.-innerHeight js/window))
    (.appendChild (.-body js/document) canvas)
    (add-watch flock-atom :renderer (fn [_ _ _ flock]
                                      (render canvas flock)))))

(defn init
  "Given an atom containing a seq of boids, initialize the view"
  [flock-atom]
  (let [canvas (js/jQuery "<canvas>")]
    (-> canvas
        (.attr "width" (.-innerWidth js/window))
        (.attr "height" (.-innerHeight js/window))
        (.appendTo (js/jQuery "body")))
    (add-watch flock-atom :renderer (fn [_ _ _ flock]
                                      (render (.get canvas 0) flock)))))
