(ns boids.3d.view)

 (defn obj
  "Convert a cljs map to a Clojure map"
  [m]
  (let [out (js-obj)]
    (doseq [[k v] m]
      (aset out (name k) v))
    out))

(defn make-camera
  "Creates and returns a three.js camera"
  [window-width window-height]
  (let [camera
        (THREE/PerspectiveCamera. 75 (/ window-width window-height) 1 10000)]
    (set! (.-z (.-position camera)) 1000)
    camera))

(defn make-3d-object
  "Creates and returns a three.js mesh object"
  []
  (let [geometry (THREE/CylinderGeometry. 0 50 400 8 2 false)
        material (THREE/MeshBasicMaterial. (obj {:color 0 :wireframe true}))]
    (THREE/Mesh. geometry material)))

(defn make-renderer
  "Creates and returns a three.js renderer"
  [width height]
  (let [renderer (THREE/WebGLRenderer. )]
    (.setSize renderer width height)
    renderer))

(defn display
  "Initializes a full display and adds it to the given DOM
  element. Returns a display map."
  [element width height]
  (let [scene (THREE/Scene.)
        objects [(make-3d-object)]
        renderer (make-renderer width height)
        camera (make-camera width height)]
    (doseq [mesh objects]
      (.add scene mesh))
    (.appendChild element (.-domElement renderer))
    {:scene scene
     :objects objects
     :renderer renderer
     :camera camera}))

(def the-display (atom nil))

(defn rotate
  "Rotates a mesh"
  [mesh x y z]
  (let [rotation (.-rotation mesh)]
    (set! (.-x rotation) (+ x (.-x rotation)))
    (set! (.-y rotation) (+ y (.-y rotation)))
    (set! (.-z rotation) (+ z (.-z rotation)))))

(defn animate
  "Animates the display contained in the given display atom"
  [display]
  (let [{:keys [objects camera scene renderer]} @display]
    (doseq [mesh objects]
      (rotate mesh 0.00 0.01 0.01))
    (.render renderer scene camera))
  (js/requestAnimationFrame #(animate display)))

(defn main []
  (reset! the-display (display (.-body js/document)
                               (.-innerWidth js/window)
                               (.-innerHeight js/window)))
  (animate the-display))