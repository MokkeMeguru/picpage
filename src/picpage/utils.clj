(ns picpage.utils
  (:require [buddy.hashers :as hashers]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn hash-password [password]
  (hashers/derive password))

(defn check-password
  "check password
  args:
  -  user: a map contains hashed-password (from db)
  -  password: raw password
  returns:
  user or nil
  "
  [user password]
  (if  (hashers/check password (:password user))
    user nil))
