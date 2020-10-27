(ns picpage.usecase.picture-thumb
  (:require [picpage.domain.pictures :as pictures]
            [clojure.java.io :as io]
            [picpage.interface.database.pictures-repository :as pictures-repository]))

(defn thumb [picture-id db]
  (let [picture (pictures-repository/get-picture db picture-id)]
    (println picture)
    (cond
      (empty? picture) nil
      (:is_deleted picture) nil
      (not (.isFile (io/file (str pictures/thumb-save-dest (:path picture))))) nil
      :else
      (try
        (io/input-stream
         (io/file (str pictures/thumb-save-dest (:path picture))))
        (catch Exception e nil)))))
