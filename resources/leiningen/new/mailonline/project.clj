(defproject clj-{{name}} "dev" ;; project version magic happens in clj-manifest-versions
  :description "TODO"
  :url "https://github.com/MailOnline/clj-{{name}}"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.1"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [ring/ring-json "0.3.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [clj_manifest "0.2.0"]
                 [jarohen/nomad "0.6.4"]
                 [riemann-clojure-client "0.2.11"]
                 [zookeeper-clj "0.9.3" :exclusions [org.apache.zookeeper/zookeeper
                                                     log4j]]
                 [org.apache.zookeeper/zookeeper "3.4.6" :exclusions [commons-codec
                                                                      com.sun.jmx/jmxri
                                                                      com.sun.jdmk/jmxtools
                                                                      com.sun.jdmk/jmxtools
                                                                      javax.jms/jms
                                                                      org.slf4j/slf4j-log4j12
                                                                      log4j]]
                 [environ "0.4.0"]
                 [compojure "1.1.8"]
                 [ch.qos.logback/logback-classic "1.1.2" :exclusions [org.slf4j/slf4j-api]]
                 [ch.qos.logback/logback-access "1.1.2"]
                 [ch.qos.logback/logback-core "1.1.2"]
                 [org.slf4j/slf4j-api "1.7.5"]
                 [mol-LogbackAmqpJSONAppender/LogbackAmqpJSONAppender "0.1.9"]
                 [stoic "0.1.2"]                                 ; https://github.com/rachbowyer/stoic
                 ]
  :main ^:skip-aot {{name}}.bootstrap
  :resource-paths ["resources"]
  :target-path "target/%s"
  :repl-options {:init-ns user}
  :repositories {"snapshots" {:url "http://nexusint.andintweb.dmgt.net:8081/nexus/content/repositories/snapshots"
                              :username "admin" :password "admin123"}
                 "releases" {:url "http://nexusint.andintweb.dmgt.net:8081/nexus/content/repositories/releases"
                             :username "admin" :password "admin123" }
                 "thirdparty" {:url "http://nexusint.andintweb.dmgt.net:8081/nexus/content/repositories/thirdparty"}}
  :jvm-opts ["-Dlog.dir=logs"]
  :profiles {:repl {:plugins [[lein-environ "0.4.0"]]}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]
                   :plugins [[lein-kibit "0.0.8"]]}
             :uberjar {:aot :all :plugins [[manifest-versions "0.1.0"]]}})

