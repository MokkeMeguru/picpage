(ns picpage.infrastructure.env
  (:require [environ.core :refer [env]]
            [taoensso.timbre :as timbre]
            [integrant.core :as ig]))

(defmethod ig/init-key ::env [_ _]
  (timbre/info "loading environment via environ")
  (let [database-url (env :database-url)
        running (if-let [running (env :env)] running "prod")]
    (timbre/info "running in " (name running))
    (timbre/info "database-url" database-url)
    {:databse-url database-url
     :running running}))