(ns {{name}}.bootstrap
  (:gen-class)
  (:require [{{name}}.routes :as rts]
            [{{name}}.web-server :as w]
            [{{name}}.config :as c]
            [{{name}}.riemann :as r]
            [environ.core :as environ]
            [{{name}}.logging :as log]
            [com.stuartsierra.component :as component]
            [stoic.bootstrap :as bs]
            [stoic.config.curator :as cc]))

(defonce mode (keyword (environ/env :operation-mode)))

(defn system-map []
  (log/info (str "Running as " mode))


  (component/system-map
    :web-server (component/using (w/web-server :web-server rts/all-routes 4040) [:riemann])
    :riemann (r/new-riemann)))

(defn path-for [root k]
  "Root is the config - e.g. clj-fe-int or clj-fe-rachel
  k is the key to the specific node e.g. :es"
  (letfn [(zk-path [app k] (format "%s/%s" app (name k)))
          (zk-path-comp [k] (zk-path "components" k))
          (zk-path-app [k] (zk-path "applications" k))]

    (let [path-head (format "/stoic/%s/typed/" (name root))
          path-tail (condp = k
                      :web-server (zk-path-app :shared)
                      (zk-path-comp k))]

      (str path-head path-tail))))


(defonce config nil)
(defonce sys {})

(defn- get-sys [] sys)

(defn- update-sys [new-sys]
  (alter-var-root #'sys (fn [_] new-sys)))


(defn init []
  (log/info "Creating component system")
  (update-sys (system-map))
  (let [config-supplier (cc/config-supplier path-for)
        {s :system c :config-supplier} (bs/bootstrap get-sys update-sys config-supplier)]
    (alter-var-root #'config (fn [_] c))
    (update-sys s)))

(defn start []
  (log/info "Starting components")
  (alter-var-root #'sys bs/start-safely)
  (log/info "Components started"))

(defn stop []
  (alter-var-root #'sys component/stop-system)
  (alter-var-root #'config component/stop))

(defn restart []
  (stop)
  (init)
  (start))


(defn -main [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop))
  (init)
  (start))
