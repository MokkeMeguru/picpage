(ns picpage.usecase.logout
  (:require [picpage.interface.database.users-repository :as users-repository]
            [picpage.domain.errors :as errors]
            [picpage.interface.database.user-tokens-repository :as user-tokens-repository]))

(defn logout [token db]
  (try
    (do
      (user-tokens-repository/delete-token db token)
      {:status 201
       :body {}})
    (catch Exception e (errors/unknown-error (.getMessage e)))))
