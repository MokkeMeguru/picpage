(ns picpage.interface.database.users-repository
  (:require [next.jdbc :as jdbc]
            [honeysql.core :as sql]
            [picpage.interface.database.utils :as utils]
            [picpage.infrastructure.sql.sql]))

(defprotocol Users
  (get-users [db])
  (get-user [db k v])
  (create-user [db user])
  (update-user [db m idm])
  (delete-user [db idm])
  (erase-user [db  user]))

(extend-protocol Users
  picpage.infrastructure.sql.sql.Boundary
  (get-users [{:keys [spec]}]
    (with-open [conn (jdbc/get-connection (:datasource spec))]
      (jdbc/execute! conn ["SELECT * FROM users"])))
  (get-user [{:keys [spec]} k v]
    (let [res (cond->
               (utils/get-by-id spec :users k v)
                #(:created_at %) (update :created_at utils/sql-to-long)
                #(:updated_at %) (update :updated_at utils/sql-to-long))
          res (into {} (remove (fn [[k v]] (nil? v)) res))]
      res))
  (create-user [{:keys [spec]} user]
    (utils/insert! spec :users user))
  (update-user [{:keys [spec]} m idm]
    (utils/update! spec :users m idm))
  (delete-user [{:keys [spec]} idm]
    (let [email (:email (utils/get-by-id spec :users :id {:id idm}))]
      (utils/update! spec :users {:is_deleted true :email (str email "-" (:id idm))} idm)))
  (erase-user [{:keys [spec]} user]
    (utils/delete! spec :users {:id (:id user)})))


;; for development


;; (defonce inst (picpage.infrastructure.sql.sql/->Boundary {:datasource (hikari-cp.core/make-datasource
;;                                                                        {:jdbc-url (environ.core/env :database-url)})}))

;; (get-users inst)
;; (get-user inst :email "meguru.mokke@gmail.com")
;; (erase-user inst {:user_id "meguru"})
;; (erase-user inst (get-user inst :email "meguru.mokke@gmail.com"))
;; (let [user (get-user inst :users/user_id "meguru")]
;;   (when-not (emsigr inst user)))

;; (create-user inst {:user_id "meguru"
;;                    :name "meguru"
;;                    :email "test@gmail.com"
;;                    :password (utils/hash-password "emacs")})
;; (def sample-user (get-user inst :users/email "test@gmail.com"))
;; (utils/check-password sample-user "emacs")
;; (update-user inst  {:users/password (utils/hash-password "doom")} {:id (:id sample-user)})
;; (println sample-user)
;; (def sample-user (get-user inst :users/email "test@gmail.com"))
;; (println sample-user)
;; (utils/check-password sample-user "doom")
