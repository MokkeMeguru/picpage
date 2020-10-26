(ns picpage.infrastructure.handler.signup
  (:require [picpage.infrastructure.openapi.users :as openapi-users]
            [picpage.usecase.signup :as signup-usecase]))

(def signup
  {:summary "create a user"
   :parameters {:body ::openapi-users/user-create-param}
   :responses {201 {:body
                    {:user_id string?
                     :name string?
                     :email string?
                     :password string?}}}
   :handler (fn [params]
              (signup-usecase/signup params))})
