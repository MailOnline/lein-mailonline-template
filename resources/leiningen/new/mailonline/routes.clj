(ns {{name}}.routes
  (:require [clojure.string :as s]
            [compojure.core :refer [defroutes ANY GET POST]]
            [compojure.route :as route]
            [cheshire.core :as json]
            [manifest.core :as man]))

(defroutes all-routes
  (GET "/meta" {web-app :web-app}
       {:body (json/generate-string {:manifest (man/manifest "postman_pat.bootstrap")})})

  (GET "/healthcheck" {web-app :web-app}
       "FIXME"))

