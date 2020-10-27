(ns picpage.usecase.picture-detail
  (:require [picpage.interface.database.pictures-repository :as pictures-repository]
            [clojure.java.io :as io]
            [picpage.domain.pictures :as pictures]
            [picpage.domain.errors :as errors]))

(defn detail [picture-id db]
  (let [picture (pictures-repository/get-picture db picture-id)]
    (cond
      (empty? picture) nil
      (:is_deleted picture) nil
      (not (.isFile (io/file (str pictures/pictures-save-dest (:path picture))))) nil
      :else
      (try
        (io/input-stream
         (io/file (str pictures/pictures-save-dest (:path picture))))
        (catch Exception e nil)))))
