(ns picpage.infrastructure.handler.pictures
  (:require [reitit.ring.middleware.multipart :as multipart]
            [clojure.walk :as w]
            [picpage.usecase.picture-upload :as picture-upload-usecase]
            [picpage.usecase.picture-delete :as picture-delete-usecase]
            [picpage.usecase.picture-list :as picture-list-usecase]
            [picpage.usecase.picture-detail :as picture-detail-usecase]
            [picpage.usecase.picture-thumb :as picture-thumb-usecase]
            [picpage.usecase.picture-info :as picture-info-usecase]
            [picpage.infrastructure.openapi.pictures :as openapi-pictures]
            [picpage.infrastructure.openapi.users :as openapi-users]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]))

(def upload
  {:summary "upload the picture"
   :swagger {:security [{:ApiKeyAuth []}]}
   :parameters {:multipart
                ::openapi-pictures/picture-upload-param
                :path ::openapi-users/user_id}
   :responses {201 {:body {:title string? :file-id string?}}}
   :handler (fn [{:keys [parameters headers db]}]
              (let [{{:keys [file title description]} :multipart} parameters
                    user-id (-> parameters :path :user-id)
                    authorization (-> headers w/keywordize-keys :authorization)]
                (picture-upload-usecase/upload authorization file user-id title description db)))})

(def picture-information
  {:summary "get the picture information"
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :responses {200 {:body {}}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [picture-id user-id]} :path} parameters]
                (picture-info-usecase/info picture-id db)))})

(def picture-detail
  {:summary "get the picture itself"
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :swagger {:produces ["image/png"]}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [picture-id]} :path} parameters]
                {:status 200
                 :headers {"Content-Type" "image/png"}
                 :body (picture-detail/detail picture-id db)}))})

(def picture-thumb
  {:summary "get the picture thumb itself"
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :swagger {:produces ["image/png"]}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [picture-id]} :path} parameters]
                {:status 200
                 :headers {"Content-Type" "image/png"}
                 :body (picture-thumb-usecase/thumb picture-id db)}))})

(def delete
  {:summary "delete the picture"
   :swagger {:security [{:ApiKeyAuth []}]}
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :responses {200 {:body {}}}
   :handler (fn [{:keys [parameters headers db]}]
              (let [{{:keys [picture-id user-id]} :path} parameters
                    authorization (-> headers w/keywordize-keys :authorization)]
                (picture-delete-usecase/delete authorization user-id picture-id db)))})

(def lists
  {:summary "list of the pictures"
   :parameters {:path {:user-id ::openapi-users/user_id}}
   :responses {200 {:body ::openapi-pictures/pictures}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [user-id]} :path} parameters]
                (picture-list-usecase/lists user-id db)))})

;; (with-redefs [clojure.core/println (fn [args] (str "binded println:" args))]
;;   (clojure.core/println "Hello"))
