(ns boids.control-panel)

(defn map->obj
  "Given a Clojure map, return a JavaScript object."
  [m]
  (reduce (fn [o [k v]]
            (aset o (name k) v)
            o)
          (js-obj) m))

(defn update-fps
  [frame-count]
  (-> (js/jQuery "#fps")
      (.html @frame-count))
  (reset! frame-count 0))

(defn init-fps-monitor
  "Create and maintain a FPS indicator that counts how often the the
  flock atom updates."
  [flock-atom]
  (let [frame-count (atom 0)]
    (-> (js/jQuery "<div>FPS: <span id='fps'></span>")
        (.css "font-weight" "bold")
        (.css "margin-bottom" 10)
        (.appendTo "#control-panel"))
    (add-watch flock-atom :framecounter #(swap! frame-count inc))
    (.setInterval js/window #(update-fps frame-count) 1000)))


(defn update-label
  "Updates a label element with the current value of the key in the
   options atom"
  [label options-atom key]
  (.html label (str (name key) ": " (@options-atom key))))

(defn init-control
  "Create control slider"
  [options-atom key slider-opts]
  (let [div (js/jQuery "<div/>")
        label (js/jQuery "<span/>")
        update (fn [new-value]
                 (swap! options-atom assoc key new-value)
                 (update-label label options-atom key))]
    (update-label label options-atom key)
    (.append div label)
    (.append div (-> (js/jQuery "<div/>")
                   (.css "font-size" "0.5em")
                   (.css "margin" 10)
                   (.slider (map->obj slider-opts))
                   (.slider "option" "value" (@options-atom key))
                   (.on "slidechange" (fn [_ ui] (update (.-value ui))))))
    (.appendTo div "#control-panel")))

(defn init-controls
  "Create control sliders"
  [options-atom]
  (init-control options-atom :max-speed {:min 0 :max 20})
  (init-control options-atom :steer-force {:min 0 :max 1 :step 0.02})
  (init-control options-atom :cohere-distance {:min 0 :max 1000})
  (init-control options-atom :avoid-distance {:min 0 :max 500})
  (init-control options-atom :align-distance {:min 0 :max 1000}))

(defn init-mouse
  "Initialize keeping track of the mouse, updating the goal to its
  current position."
  [options-atom]
  (.on (js/jQuery "body") "mousemove"
       (fn [evt]
         (swap! options-atom assoc :goal [(.-pageX evt)
                                          (.-pageY evt)]))))

(defn create-cp
  "Creates and returns a jQuery element for the control panel"
  []
  (-> (js/jQuery "<div id='control-panel'/>")
      (.css "z-index" 1)
      (.css "position" "absolute")
      (.css "top" 0)
      (.css "left" 0)
      (.css "width" 200)
      (.appendTo "body")))

(defn init
  "Initialize the control panel"
  [options-atom flock-atom]
  (create-cp)
  (init-fps-monitor flock-atom)
  (init-controls options-atom)
  (init-mouse options-atom))