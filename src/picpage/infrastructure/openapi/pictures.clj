(ns picpage.infrastructure.openapi.pictures
  (:require [clojure.spec.alpha :as s]
            [picpage.infrastructure.openapi.users :as users]
            [reitit.ring.middleware.multipart :as multipart]
            [clj-time.core :as time]))

(s/def ::file multipart/temp-file-part)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::picture_id pos-int?)
(s/def ::created_at string?)
(s/def ::updated_at string?)
(s/def ::type string?)
(s/def ::path string?)

(s/def ::picture-upload-param (s/keys :req-un [::file ::title ::description ::users/user_id]))
(s/def ::picture-delete-param (s/keys :req-un [::picture_id ::users/user_id]))
(s/def ::picture (s/keys :req-un [::picture_id
                                  ::title
                                  ::description
                                  ::path
                                  ::created_at
                                  ::updated_at]))
(s/def ::picture-abst (s/keys :req-un [::picture_id
                                       ::title
                                       ::path
                                       ::created_at
                                       ::updated_at]))
(s/def ::pictures (s/coll-of ::picture-abst :into []))
