(ns picpage.domain.utils
  (:require [clojure.string :as string]))

(defn check-trim [s]
  (= (count s) (count (string/trim s))))
