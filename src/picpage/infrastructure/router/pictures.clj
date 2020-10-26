(ns picpage.infrastructure.router.pictures
  (:require [picpage.infrastructure.handler.pictures :refer [upload delete lists]]))

(defn upload-router [env]
  ["/upload"
   {:swagger {:tags ["pictures"]}
    :post upload}])

(defn pictures-router [env]
  ["/users/:user-id/pictures"
   {:swagger {:tags ["pictures"]}}
   ["/"
    {:post upload
     :get lists}]
   ["/:picture-id"
    {:delete delete}]])
