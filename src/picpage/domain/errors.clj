(ns picpage.domain.errors)

(defn unknown-error [message]
  {:status 500 :body {:code 777 :message (str message)}})

(def duplicate-user {:status 400 :body {:code 1101 :message "duplicate user_id"}})
(def duplicate-email {:status 400 :body {:code 1102 :message "duplicate email address"}})

(def user-not-found {:status 404 :body {:code 1401 :message "user is not found"}})

(def login-failed {:status 500 :body {:code 1501 :message "cannot generate login token"}})
(def invalid-password {:status 400 :body {:code 1502 :message "passward is invalid"}})
(def invalid-authorization {:status 400 :body {:code 1503 :message "login information is invalid"}})
(def invalid-user-operation {:status 400 :body {:code 1504 :message "invalid user's operation"}})

(def picture-not-found {:status 400 :body {:code 1601 :message "picture is not found"}})
