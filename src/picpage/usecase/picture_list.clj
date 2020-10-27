(ns picpage.usecase.picture-list
  (:require [picpage.interface.database.users-repository :as users-repository]
            [picpage.interface.database.pictures-repository :as pictures-repository]
            [picpage.domain.errors :as errors]
            [picpage.domain.pictures :as pictures]))

(defn lists [user-id db]
  (let [user (users-repository/get-user db :user_id user-id)]
    (cond
      (empty? user) errors/user-not-found
      :else
      (try
        (let [pictures (pictures-repository/get-pictures db (:id user))]
          {:status 200
           :body (map
                  (fn [picture]
                    (let [picture_id (:id picture)
                          tim (:created_at picture)]
                      (-> picture
                          (dissoc :id)
                          (dissoc :type)
                          (dissoc :user_id)
                          (dissoc :is_deleted)
                          (dissoc :description)
                          (assoc :picture_id picture_id)
                          (update :created_at str)
                          (update :updated_at str))))
                  (filter (fn [picture] (not (:is_deleted picture))) pictures))})
        (catch Exception e
          (errors/unknown-error (.getMessage e)))))))
