(ns {{name}}.config
  (:require [environ.core :as environ]
            [zookeeper :as zk]
            [com.stuartsierra.component :as component]))

(defn- zk-ips []
  (environ/env :clj-fe-zk-conn-str))

(defn- zk-root []
  (environ/env :clj-fe-zk-root))

(defn- connect []
  (zk/connect (zk-ips)
              :timeout-msec 10000))

(defn- deserialize-data [data]
  (when data (read-string (String. data "UTF-8"))))

(defn get-node [config type node]
  (let [path (format "/stoic/%s/typed/%s/%s" (zk-root) type node)
        client (:zk-client config)
        restart-fn (:restart-fn config)]
    (deserialize-data
      (:data
        (zk/data
          client
          path
          :watcher (fn restart-app [event]
                     (when (= :NodeDataChanged (:event-type event))
                       (restart-fn))))))))

(defn get-{{name}} [config]
  (get-node config "applications" "{{name}}"))

(defn get-riemann [config]
  (get-node config "components" "riemann"))

(defn get-shared [config]
  (get-node config "applications" "shared"))


(defrecord Config [restart-fn]
  component/Lifecycle

  (start [this]
    (let [zk-client (connect)]
      (assoc this :restart-fn restart-fn)
      (assoc this :zk-client zk-client)))

  (stop [this]
    (when-let [zk-client (:zk-client this)]
      (zk/close zk-client))
    (dissoc this :restart-fn)
    (dissoc this :zk-client)))

(defn new-config [restart-fn]
  (Config. restart-fn))
