(ns picpage.usecase.picture-upload
  (:require [picpage.interface.database.users-repository :as users-repository]
            [picpage.domain.errors :as errors]
            [picpage.interface.database.user-tokens-repository :as user-tokens-repository]
            [picpage.interface.database.pictures-repository :as pictures-repository]
            [picpage.utils :as utils]
            [picpage.domain.pictures :as pictures]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [picpage.infrastructure.image-processor.image :as image]))

(defn upload [authorization file user-id title description db]
  (let [user (users-repository/get-user db :user_id user-id)]
    (cond
      (empty? user) errors/user-not-found
      (empty? (user-tokens-repository/get-token db (:id user) authorization)) errors/invalid-authorization
      :else
      (let [fin (:tempfile file)
            fout-name (.toString (java.util.UUID/randomUUID))
            fout (io/file
                  (str pictures/pictures-save-dest fout-name))
            thumb-fout (io/file
                        (str pictures/thumb-save-dest fout-name))
            picture {:user_id (:id user)
                     :type (:content-type file)
                     :title title
                     :description description
                     :path fout-name}]
        (timbre/info "[picture uploaded]: " user-id title "into" fout-name)
        (try
          (do (io/copy fin fout)
              (-> fin
                  (image/get-thumb image/thumb-size)
                  (image/dump-file thumb-fout))
              (pictures-repository/create-picture db picture)
              {:status 201
               :body {:title title
                      :file-id fout-name}})
          (catch Exception e
            (do
              (when (.isFile fout) (.delete fout))
              (when (.isFile thumb-fout) (.delete fout))
              (errors/unknown-error (.getMessage e)))))))))
