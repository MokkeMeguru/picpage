(ns picpage.infrastructure.router.core
  (:require
   [reitit.ring :as ring]
   [reitit.core :as reitit]
   [reitit.coercion.spec]

   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.coercion :as coercion]

   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.exception :as exception]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.ring.middleware.dev :as dev]
   [reitit.ring.spec :as spec]

   [spec-tools.spell :as spell]
   [muuntaja.core :as m]

   [clojure.java.io :as io]

   [ring.logger :refer [wrap-with-logger]]
   [integrant.core :as ig]
   [taoensso.timbre :as timbre]

   [reitit.dev.pretty :as pretty]

   [picpage.infrastructure.router.utils :refer [wrap-db my-wrap-cors]]
   [picpage.infrastructure.router.samples :refer [sample-router]]
   [picpage.infrastructure.router.registrations :refer [signup-router login-router logout-router]]
   [picpage.infrastructure.router.pictures :refer [pictures-router]]))

(defn app [env db]
  (ring/ring-handler
   (ring/router
    [["/swagger.json"
      {:get {:no-doc true
             :swagger {:info {:title "picpage-api"}
                       :securityDefinitions
                       {:ApiKeyAuth
                        {:type "apiKey" :name "authorization" :in "header"}}
                       :basePath "/"} ;; prefix for all paths
             :handler (swagger/create-swagger-handler)}}]
     ["/api"
      (sample-router env)
      (signup-router env)
      (login-router env)
      (logout-router env)
      ["/authed"
       ;; (upload-router env)
       (pictures-router env)]]]

    {:exception pretty/exception
     :data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :middleware [;; swagger feature
                         swagger/swagger-feature
                         ;; query-params & form-params
                         parameters/parameters-middleware
                           ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                           ;; encoding response body
                         muuntaja/format-response-middleware
                           ;; exception handling
                         exception/exception-middleware
                           ;; decoding request body
                         muuntaja/format-request-middleware
                           ;; coercing response bodys
                         coercion/coerce-response-middleware
                           ;; coercing request parameters
                         coercion/coerce-request-middleware
                           ;; multipart
                         multipart/multipart-middleware
                         [wrap-db db]]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/api"})
    (ring/create-default-handler))
   {:middleware [my-wrap-cors
                 wrap-with-logger]}))

(defmethod ig/init-key ::router [_ {:keys [env db]}]
  (timbre/info "router got: env" env)
  (timbre/info "router got: db" db)
  (app env db))


;; (def app
;;   (ring/ring-handler
;;    (ring/router
;;     ["/api"
;;      ["/ping" {:name ::ping
;;                :get (fn [_]
;;                       {:status 200
;;                        :body "pong"})}]
;;      ["/plus/:z" {:name ::plus
;;                   :post {:coercion reitit.coercion.spec/coercion
;;                          :parameters {:query {:x int?}
;;                                       :body {:y int?}
;;                                       :path {:z int?}}
;;                          :responses {200 {:body {:total int?}}}
;;                          :handler (fn [{:keys [parameters]}]
;;                                     (let [total (+ (-> parameters :query :x)
;;                                                    (-> parameters :body :y)
;;                                                    (-> parameters :path :z))]
;;                                       {:status 200
;;                                        :body {:total total}}))}}]]
;;     {:data {:middleware [coercion/coerce-exceptions-middleware
;;                          coercion/coerce-request-middleware
;;                          coercion/coerce-response-middleware]}})))

;; (app {:request-method :post
;;       :uri "/api/plus/3"
;;       :query-params {"x" "1"}
;;       :body-params {:y 2}})
