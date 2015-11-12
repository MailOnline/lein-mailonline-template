(ns {{name}}.riemann
  (:require [{{name}}.config :as c]
            [riemann.client :as r]
            [com.stuartsierra.component :as component]))

(defn- local-host-name* []
  (try
    (.getHostName (java.net.InetAddress/getLocalHost))
    (catch Exception e "unknown")))

(def ^:private local-host-name (memoize local-host-name*))

(defn- connect [riemann-info]
  (let [riemann-arg-map {:host (:riemann-host riemann-info)}]
    (r/tcp-client riemann-arg-map)))

(defrecord Riemann []
  component/Lifecycle

  (start [this]
    (let [riemann-client (-> this :settings deref connect)]
      (assoc this :riemann-client riemann-client)))

  (stop [this]
    (when-let [riemann-client (:riemann-client this)]
      (r/close-client riemann-client))
    (dissoc this :riemann-client)))

(defn new-riemann []
  (Riemann.))

(defn send-event [riemann message stack-trace]
  (r/send-event (:riemann-client riemann)
                {:description (str message " - " stack-trace)
                 :tags ["exception"]
                 :state "critical"
                 :service "{{name}}"
                 :metric 0
                 :ttl 300
                 :host (local-host-name)}))
