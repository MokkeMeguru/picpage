(ns picpage.usecase.picture-info
  (:require [picpage.interface.database.pictures-repository :as pictures-repository]
            [picpage.domain.errors :as errors]
            [clojure.java.io :as io]))

(defn info [picture-id db]
  (let [picture (pictures-repository/get-picture db picture-id)]
    (cond
      (empty? picture) errors/picture-not-found
      (:is_deleted picture) errors/picture-not-found
      :else
      {:status 200
       :body (-> picture
                 (dissoc :id)
                 (dissoc :type)
                 (dissoc :user_id)
                 (dissoc :is_deleted)
                 (assoc :picture_id picture-id)
                 (update :created_at str)
                 (update :updated_at str))})))
