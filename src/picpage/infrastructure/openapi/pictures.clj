(ns picpage.infrastructure.openapi.pictures
  (:require [clojure.spec.alpha :as s]
            [picpage.infrastructure.openapi.users :as users]
            [reitit.ring.middleware.multipart :as multipart]))

(s/def ::file multipart/temp-file-part)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::picture_id string?)
(s/def ::created_at pos-int?)
(s/def ::type string?)

(s/def ::picture-upload-param (s/keys :req-un [::file ::title ::description ::users/user_id]))
(s/def ::picture-delete-param (s/keys :req-un [::picture_id ::users/user_id]))
(s/def ::picture (s/keys :req-un [::picture_id ::title ::description ::created_at]))
