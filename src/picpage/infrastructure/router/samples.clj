(ns picpage.infrastructure.router.samples
  (:require [reitit.ring.middleware.multipart :as multipart]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]))

(defn sample-router [env]
  ["/samples"
   ["/files"
    {:swagger {:tags ["samples"]}}

    ["/upload"
     {:post {:summary "upload a file"
             :parameters {:multipart
                          {:file multipart/temp-file-part
                           :title string?}}
             :responses {200 {:body {:title string? :file-id string?}}}
             :handler (fn [{{{:keys [file title]} :multipart} :parameters}]
                        (let [fin  (:tempfile file)
                              fout-name (.toString (java.util.UUID/randomUUID))
                              fout (io/file
                                    (str "image-db/" fout-name))]
                          (timbre/info "save title: " title "into: " fout-name)
                          (io/copy fin fout)
                          {:status 200
                           :body {:title title
                                  :file-id fout-name}}))}}]

    ["/download"
     {:get {:summary "downloads a file"
            :swagger {:produces ["image/png"]}
            :handler (fn [_]
                       {:status 200
                        :headers {"Content-Type" "image/png"}
                        :body (io/input-stream (io/resource "icon.png"))})}}]]

   ["/math"
    {:swagger {:tags ["samples"]}}

    ["/plus"
     {:get {:summary "plus with spec query parameters"
            :parameters {:query {:x int?, :y int?}}
            :responses {200 {:body {:total int?}}}
            :handler (fn [{{{:keys [x y]} :query} :parameters}]
                       {:status 200
                        :body {:total (+ x y)}})}
      :post {:summary "plus with spec body parameters"
             :parameters {:body {:x int?, :y int?}}
             :responses {200 {:body {:total int?}}}
             :handler (fn [{{{:keys [x y]} :body} :parameters}]
                        {:status 200
                         :body {:total (+ x y)}})}}]]])

;; (io/resource (.toString (java.util.UUID/randomUUID)))
