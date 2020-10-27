(ns picpage.usecase.picture-delete
  (:require [picpage.interface.database.users-repository :as users-repository]
            [picpage.domain.errors :as errors]
            [picpage.interface.database.user-tokens-repository :as user-tokens-repository]
            [picpage.interface.database.pictures-repository :as pictures-repository]
            [clojure.java.io :as io]
            [picpage.domain.pictures :as pictures]))

(defn delete [authorization user-id picture-id db]
  (let [user (users-repository/get-user db :user_id user-id)
        picture (pictures-repository/get-picture db picture-id)]
    (cond
      (empty? user) errors/user-not-found
      (empty? (user-tokens-repository/get-token db (:id user) authorization)) errors/invalid-authorization
      (empty? picture) errors/picture-not-found
      (not= (:user_id picture) (:id user)) errors/invalid-user-operation
      :else
      (try
        (let [source-file (io/file (str pictures/pictures-save-dest (:path picture)))
              thumb-file (io/file (str pictures/pictures-save-dest (:path picture)))]
          (pictures-repository/delete-picture db picture-id)
          (when (.isFile source-file) (.delete source-file))
          (when (.isFile thumb-file) (.delter thumb-file))
          {:status 200
           :body {}})
        (catch Exception e
          (errors/unknown-error (.getMessage e)))))))
