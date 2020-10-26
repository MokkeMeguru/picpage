(ns picpage.interface.database.pictures-repository
  (:require [next.jdbc :as jdbc]
            [honeysql.core :as sql]
            [picpage.interface.database.utils :as utils]
            [picpage.infrastructure.sql.sql]))

(defprotocol Pictures
  (get-pictures [db user-id])
  (get-picture [db picture-id])
  (create-picture [db picture])
  (erase-picture [db picture-id])
  (delete-picture [db picture-id]))

(extend-protocol Pictures
  picpage.infrastructure.sql.sql.Boundary
  (get-pictures [{:keys [spec]} user-id]
    (utils/find-by-m spec :pictures {:user_id user-id}))
  (get-picture [{:keys [spec]} picture-id]
    (utils/get-by-id spec :pictures :id picture-id))
  (create-picture [{:keys [spec]} picture]
    (utils/insert! spec :pictures picture))
  (delete-picture [{:keys [spec]} picture-id]
    (utils/update! spec :pictures {:is_deleted true} {:id picture-id}))
  (erase-picture [{:keys [spec]} picture-id]
    (utils/delete! :pictures {:id picture-id})))
