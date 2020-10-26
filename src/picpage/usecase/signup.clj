(ns picpage.usecase.signup
  (:require [taoensso.timbre :as timbre]
            [picpage.domain.errors :as errors]
            [picpage.interface.database.utils :as utils]
            [picpage.interface.database.users-repository :as users-repository]
            [picpage.interface.database.user-tokens-repository :as user-tokens-repository]))

(defn signup [{:keys [parameters db]}]
  (let [{{:keys [name user_id password email]} :body} parameters]
    (cond
      (not (empty? (users-repository/get-user db :user_id user_id))) errors/duplicate-user
      (not (empty? (users-repository/get-user db :email email))) errors/duplicate-email
      :else (let
             [user {:user_id user_id
                    :name name
                    :email email
                    :password (utils/hash-password password)}]
              (timbre/info "[user created]: " name user_id password email db)
              (try
                (do
                  (users-repository/create-user db user)
                  {:status 201
                   :body user})
                (catch Exception e (errors/unknown-error (.getMessage e))))))))

;; {
;;   "name": "meguru",
;;   "user_id": "meguru",
;;   "password": "password",
;;   "email": "meguru.mokke@gmail.com"
;; }
