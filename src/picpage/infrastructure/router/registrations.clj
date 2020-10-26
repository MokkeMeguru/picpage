(ns picpage.infrastructure.router.registrations
  (:require [picpage.infrastructure.handler.signup :refer [signup]]
            [picpage.infrastructure.handler.login :refer [login]]
            [picpage.infrastructure.handler.logout :refer [logout]]))

(defn signup-router [env]
  ["/signup"
   {:post signup}])

(defn login-router [env]
  ["/login"
   {:post login}])

(defn logout-router [env]
  ["/logout"
   {:post logout}])
