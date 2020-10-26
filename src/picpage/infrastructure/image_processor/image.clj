(ns picpage.infrastructure.image-processor.image
  (:import [java.awt.image AffineTransformOp BufferedImage]
           [java.io ByteArrayOutputStream FileInputStream File]
           java.awt.geom.AffineTransform
           javax.imageio.ImageIO
           java.net.URLEncoder)
  (:require [clojure.java.io :as io]))

(def thumb-size 360)

(def thumb-prefix "thumb_")

(defn scale [image ratio width height]
  (let [scale (AffineTransform/getScaleInstance (double ratio) (double ratio))
        transform-op (AffineTransformOp.
                      scale AffineTransformOp/TYPE_BILINEAR)]
    (.filter transform-op image (BufferedImage. width height (.getType image)))))

(defn scale-image [^BufferedImage image thumb-size]
  (let [width (-> image .getWidth)
        height (-> image .getHeight)
        ratio (/ (double thumb-size) height)]
    (println ratio)
    (scale image ratio thumb-size thumb-size)))

(defn center-crop-image [^BufferedImage image]
  (let [width (-> image .getWidth)
        height (-> image .getHeight)
        min-size (min width height)
        x (/ (- width  min-size) 2)
        y (/ (- height min-size) 2)]
    (-> image
        (.getSubimage x y min-size min-size))))

(defn get-thumb [^java.io.File source ^Integer thumb-size]
  (-> source
      ImageIO/read
      center-crop-image
      (scale-image thumb-size)))

(defn dump-file [^BufferedImage image ^java.io.File target]
  (ImageIO/write image "png" target))

;; (def sample-image (io/file "resources/sample-image.png"))
;; (if (-> sample-image .isFile)
;;   (-> sample-image
;;       (get-thumb thumb-size)
;;       (dump-file (io/file (str "resources/" thumb-prefix (-> sample-image .getName))))))
;; (if (-> sample-image .isFile)
;;   (let [img (ImageIO/read sample-image)
;;         cropped-img (center-crop-image img)
;;         scaled-img (scale-image cropped-img thumb-size)]
;;     (println (.getWidth img) (-> img .getHeight))
;;     (println (.getWidth cropped-img) (-> cropped-img .getHeight))
;;     (println (.getWidth scaled-img) (-> scaled-img .getHeight))
;;     (dump-file scaled-img (io/file (str "resources/" thumb-prefix (-> sample-image .getName))))))
