(ns picpage.infrastructure.openapi.users
  (:require [clojure.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::user_id string?)
(s/def ::email string?)
(s/def ::password string?)
(s/def ::updated_at int?)
(s/def ::created_at int?)
(s/def ::token string?)
(s/def ::path-params (s/keys :req-un [::id]))

(s/def ::users-update-param (s/keys :req-un [::name ::user_id ::email ::password]))
(s/def ::user-update-response (s/keys :req-un [::name ::created_at ::updated_at] :opt-un [::email]))

(s/def ::user-get-response (s/keys :req-un [::name ::created_at] :opt-un [::email]))
(s/def ::user-create-param (s/keys :req-un [::name ::user_id ::password] :opt-un [::email]))

(s/def ::user-create-response (s/keys :req-un [::name ::userid ::created_at] :opt-un [::email]))
(s/def ::user-login-param (s/keys :req-un [::email ::password]))
(s/def ::user-logout-param (s/keys :req-un [::token]))
