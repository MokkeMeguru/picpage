(ns picpage.infrastructure.router.pictures
  (:require [picpage.infrastructure.handler.pictures :refer [upload delete lists picture-detail picture-information picture-thumb]]))

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
    {:delete delete}]
   ["/:picture-id"
    ["/thumb"
     {:get picture-thumb}]
    ["/info"
     {:get picture-information}]
    ["/detail"
     {:get picture-detail}]]])
