(ns picpage.usecase.login
  (:require [picpage.interface.database.users-repository :as users-repository]
            [picpage.domain.errors :as errors]
            [picpage.interface.database.user-tokens-repository :as user-tokens-repository]
            [picpage.utils :as utils]))

(defn login [email password db]
  (let [user (users-repository/get-user db :email email)]
    (cond
      (empty? user) errors/user-not-found
      (nil? (utils/check-password user password)) errors/invalid-password
      :else
      (let [token (utils/uuid)]
        (try (do
               (user-tokens-repository/add-token db (:id user) token)
               {:status 201
                :body {:token token
                       :userid (:user_id user)}})
             (catch Exception e (errors/unknown-error (.getMessage e))))))))
