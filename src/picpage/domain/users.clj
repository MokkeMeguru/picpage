(ns picpage.domain.users
  (:require [clojure.spec.alpha :as s]
            [picpage.domain.utils :refer [check-trim]]))

(def username-min-length 4)
(def username-max-length 18)
(def userid-min-length 4)
(def userid-max-length 18)
(def password-min-length 6)
(def password-max-length 18)

(def user-name-regex
  ;; This SQL escape is excessive. because I use parametrized_SQL_statement
  ;; https://rosettacode.org/wiki/Parametrized_SQL_statement
  ;; this escape is because, To found a man who want to do SQL injection.
  ;; We will report the request which was failed this assertion.
  ;;
  ;; condition
  ;; >>> - be not contain & @ ^ ( ) [ ] { } . ? + * | \ ' "
  ;; #"[0-9a-zA-Zぁ-んァ-ヶ一-龠々ー]*$"
  #"^[^&@\^\(\)\[\]\{\}.\?\+\*\|\\\'\"]*$")

(def user-id-regex
  ;; This SQL escape is excessive. because I use parametrized_SQL_statement
  ;; https://rosettacode.org/wiki/Parametrized_SQL_statement
  ;; this escape is because, To found a man who want to do SQL injection.
  ;; We will report the request which was failed this assertion.
  ;;
  ;; condition
  ;; >>> - be not contain & @ ^ ( ) [ ] { } . ? + * | \ ' "
  ;; #"[0-9a-zA-Zぁ-んァ-ヶ一-龠々ー]*$"
  #"[0-9A-Za-z]*$")

(def password-regex
  ;; condition
  ;; >>> - less 1 number
  ;; >>> - less 1 small alphabet
  ;; >>> - less 1 big alphabet
  #"^(?=.*\d+)(?=.*[a-z])(?=.*[A-Z])[A-Za-z\d+]*$")

(def email-regex
  #"^[\w-\.+]*[\w-\.]\@([\w]+\.)+[\w]+[\w]$")

(s/def ::username
  (s/and string?
         #(re-matches user-name-regex %)
         check-trim
         #(<= username-min-length (count %) username-max-length)))

(s/def ::userid
  (s/and string?
         #(re-matches user-id-regex %)
         check-trim
         #(<= userid-min-length (count %) userid-max-length)))

(s/def ::password
  (s/and string?
         #(re-matches password-regex %)
         check-trim
         #(<= password-min-length (count %) password-max-length)))

(s/def ::email
  (s/and string?
         #(re-matches email-regex %)))
