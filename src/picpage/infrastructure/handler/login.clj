(ns picpage.infrastructure.handler.login
  (:require [picpage.infrastructure.openapi.users :as openapi-users]
            [picpage.usecase.login :as login-usecase]
            [picpage.domain.errors :as errors]))

(def login
  {:summary "login"
   :parameters {:body ::openapi-users/user-login-param}
   :responses {201 {:body {:token string? :userid string?}}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [email password]} :body} parameters]
                (login-usecase/login email password db)))})
