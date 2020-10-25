(ns picpage.core
  (:gen-class)
  (:require [environ.core :refer [env]]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [integrant.repl :as igr]
            ;; [pipage.Boundary.migrate :refer [reset-migrate]]
            ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
