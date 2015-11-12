(ns {{name}}.logging
  (:import [org.slf4j LoggerFactory]))

(defmacro get-logger []
  `(.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)))

; New functions to invoke logger with object arguments, as opposed to the standard behaviour of
; evaluating all args with prt-string and therefore concatenating everything into a big string
; Invoked as (logging/warn "A Message" more-clojure-args)
(defmacro trace [message & details]
     `(.trace (.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)) (str ~message)  ~@details))
(defmacro debug [message & details]
     `(.debug (.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)) (str ~message)  ~@details))
(defmacro info [message & details]
     `(.info (.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)) (str ~message)  ~@details))
(defmacro warn [message & details]
     `(.warn (.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)) (str ~message)  ~@details))
(defmacro error [message & details]
     `(.error (.getLogger (LoggerFactory/getILoggerFactory) (str ~*ns*)) (str ~message)  ~@details))
