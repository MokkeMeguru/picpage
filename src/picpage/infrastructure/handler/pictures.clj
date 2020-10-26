(ns picpage.infrastructure.handler.pictures
  (:require [reitit.ring.middleware.multipart :as multipart]
            [clojure.walk :as w]
            [picpage.usecase.picture-upload :as picture-upload-usecase]
            [picpage.infrastructure.openapi.pictures :as openapi-pictures]
            [picpage.infrastructure.openapi.users :as openapi-users]))

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

(def detail
  {:summary "get the picture"
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :responses {200 {:body {}}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [picture-id user-id]} :path} parameters]
                (println picture-id user-id)))})

(def delete
  {:summary "delete the picture"
   :swagger {:security [{:ApiKeyAuth []}]}
   :parameters {:path {:user-id ::openapi-users/user_id
                       :picture-id ::openapi-pictures/picture_id}}
   :responses {201 {:body {}}}
   :handler (fn [{:keys [parameters headers db]}]
              (let [{{:keys [picture-id user-id]} :path} parameters]
                (println user-id picture-id)))})

(def lists
  {:summary "list of the pictures"
   :parameters {:path {:user-id ::openapi-users/user_id}}
   :responses {200 {:body [::openapi-pictures/picture]}}
   :handler (fn [{:keys [parameters db]}]
              (let [{{:keys [user-id]} :path} parameters]
                (println user-id)))})
