(ns leiningen.new.mailonline
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "mailonline"))

(defn mailonline
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' mailonline project.")
    (->files data
             ["src/{{sanitized}}/bootstrap.clj" (render "bootstrap.clj" data)]
             ["src/{{sanitized}}/logging.clj" (render "logging.clj" data)]
             ["src/{{sanitized}}/riemann.clj" (render "riemann.clj" data)]
             ["src/{{sanitized}}/web_server.clj" (render "web_server.clj" data)]
             ["src/{{sanitized}}/routes.clj" (render "routes.clj" data)]
             ["src/{{sanitized}}/config.clj" (render "config.clj" data)]
             ["dev/user.clj" (render "user.clj" data)]
             ["dev/project_repl/core.clj" (render "core.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["config.json" (render "config.json" data)]
             ["resources/logback.xml" (render "logback.xml" data)]
             ["resources/logback-access.xml" (render "logback-access.xml" data)]
             )))
