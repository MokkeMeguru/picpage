(ns picpage.infrastructure.handler.logout
  (:require [picpage.infrastructure.openapi.users :as openapi-users]
            [picpage.usecase.logout :as logout-usecase]))

(def logout
  {:summary "logout from the session"
   :parameters {:body ::openapi-users/user-logout-param}
   :responses {201  {:body {}}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [token]} :body} parameters]
                (logout-usecase/logout token db)))})
