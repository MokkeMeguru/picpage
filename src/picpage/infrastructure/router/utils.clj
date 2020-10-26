(ns picpage.infrastructure.router.utils
  (:require [taoensso.timbre :as timbre]))

(defn wrap-db [handler db]
  (fn [request]
    (handler (assoc request :db db))))

(defn my-wrap-cors
  "Wrap the server response in a Control-Allow-Origin Header to
  allow connections from the web app."
  [handler]
  (fn [request]
    (timbre/warn "Access Origin: " (-> request :headers (get "origin")))
    ;;    (print request)
    (let [response (handler request)]
      (-> response
          (assoc-in [:headers "Access-Control-Allow-Credentials"] "true")
          (assoc-in [:headers "Access-Control-Allow-Origin"] "http://localhost:3000")
          (assoc-in [:headers "Access-Control-Allow-Headers"] "authorization,content-type")
          (assoc-in [:headers "Access-Control-Allow-Methods"] "POST,GET,OPTIONS,DELETE,PUT,UPDATE,PATCH")))))
