(ns {{name}}.web-server
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.logging :as log]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [clojure.tools.logging :as log]))

(defn wrap-exceptions [f]
  (fn [req]
    (try 
      (f req)
      (catch Exception e
        (log/error e (str "error in http request" (:uri req) (:params req)))
        {:status 500}))))

(defn wrap-components [f deps]
  (fn [req]
    (f (assoc req :components deps))))

(defn wrap-routes 
  "Inject the component dependencies into web requests"
  [routes deps]
  (-> routes
      (wrap-components deps)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-exceptions)))

(defrecord WebServer [id port routes]
  component/Lifecycle
  (start [sys]
    (let [server (jetty/run-jetty (wrap-routes routes (dissoc sys :port id)) 
                                  {:port port :join? false})]
      (assoc sys id server)))
  (stop [sys]
    (.stop (id sys))
    (dissoc sys id)))

(defn web-server
  [id routes port]
  (map->WebServer {:id id :port port :routes routes}))

