(ns project-repl.core
    "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
     [clojure.java.io :as io]
     [clojure.java.javadoc :refer (javadoc)]
     [clojure.pprint :refer (pprint)]
     [clojure.reflect :refer (reflect)]
     [clojure.repl :refer (apropos dir doc find-doc pst source)]
     [clojure.set :as set]
     [clojure.string :as str]
     [clojure.test :as test]
     [clojure.tools.namespace.repl :refer :all]
     [clojure.tools.namespace.move :refer :all]
     [{{name}}.bootstrap :as bootstrap]))

(def system
  nil)

(defn create-and-start
  "Creates and starts system instance, updates var system"
  []
    (bootstrap/init)
  (bootstrap/start)
  (println "Started")
  )

(defn stop
  "Stops the system if it is running, updates var system"
  []
  (bootstrap/stop)
  (println "Stopped")
  )

(defn reset-dev
  "Stops the system, reloads modified source files and restarts the system."
  []
  (when (not-empty bootstrap/sys) 
    (stop))
  (refresh :after 'project-repl.core/create-and-start))
