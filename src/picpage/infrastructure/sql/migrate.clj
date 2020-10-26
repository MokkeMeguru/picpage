(ns picpage.infrastructure.sql.migrate
  (:require [ragtime.jdbc :as jdbc]
            [clojure.java.io :as io]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [clojure.string :as string]
            [integrant.core :as ig]
            [taoensso.timbre :as timbre]
            [ragtime.repl :as ragr]))

(def migration-option
  {:encoding "UTF-8"
   :append true})

(defn init-file! [fname]
  (let [{:keys [encoding append]} migration-option]
    (when-not (.exists (io/as-file fname))
      (timbre/info "first logging ... ")
      (spit fname "first-log\n" :encoding encoding :append append))))

(defn migrate! [command database-url migration-folder migration-log]
  (let [{:keys [encoding append]} migration-option
        config {:datastore (jdbc/sql-database {:connection-uri database-url})
                :migrations (jdbc/load-resources migration-folder)}]
    (condp = command
      :up (do
            (timbre/info "migration start!")
            (ragr/migrate config)
            (spit migration-log (str "migrated! at " (tc/to-string (time/now)) "\n")
                  :encoding encoding :append append))
      nil)))

(defmethod ig/init-key ::migrate
  [_ {:keys [env]}]
  (let [{:keys [database-url
                running
                migration-folder
                migration-log
                new-migration]} env
        log (do (init-file! migration-log)
                (string/split-lines (slurp migration-log)))]
    (timbre/info "load migration file: " migration-log)
    (timbre/info "new migration exists?" new-migration)
    (if new-migration
      (migrate! :up database-url migration-folder migration-log)
      (timbre/info "keep database as is"))
    env))
