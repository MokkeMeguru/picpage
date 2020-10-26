(ns picpage.interface.database.user-tokens-repository
  (:require [next.jdbc :as jdbc]
            [honeysql.core :as sql]
            [picpage.interface.database.utils :as utils]
            [picpage.infrastructure.sql.sql]))

(defprotocol User-Tokens
  (add-token [db user-id token])
  (get-token [db user-id token])
  (delete-token [db token])
  (delete-all-token [db user-id]))

(extend-protocol User-Tokens
  picpage.infrastructure.sql.sql.Boundary
  (add-token [{:keys [spec]} user-id token]
    (utils/insert! spec :user_tokens {:user_id user-id :token token}))
  (get-token [{:keys [spec]} user-id token]
    (utils/find-by-m spec :user_tokens {:user_id user-id :token token}))
  (delete-token [{:keys [spec]} token]
    (utils/delete! spec :user_tokens {:token token}))
  (delete-all-token [{:keys [spec]} user-id]
    (utils/delete! spec :user_tokens {:user_id user-id})))

(defonce inst (picpage.infrastructure.sql.sql/->Boundary {:datasource (hikari-cp.core/make-datasource
                                                                       {:jdbc-url (environ.core/env :database-url)})}))

(delete-token inst "a6a7972f-9537-43a5-ad1c-4b4929b3a777")
