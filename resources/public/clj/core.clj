(ns noir-example.core
  (:require [noir.server :as server]))




"give server a name, so you can stop it
(server/stop run-server)"
(defn start-server []
  (def running-server (server/start 8000)))

(defn stop-server []
  (server/stop running-server))

