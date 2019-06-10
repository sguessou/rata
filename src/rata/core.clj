(ns rata.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes ANY GET]]
            [compojure.route :refer [not-found]]
            [clj-http.client :as http]
            [cheshire.core :refer [generate-string parse-string]]))


(defn greet [req]
  {:status 200
   :body "Welcome to Rata!"
   :headers {}})

(defroutes routes
  (GET "/" [] greet)

  (ANY "/request" [] handle-dump))
  
(not-found "Page not found.")

(def app
  (wrap-params
   routes))

(defn -main [port]
   (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))

(def resp (http/get "https://rata.digitraffic.fi/api/v1/live-trains?arrived_trains=0&arriving_trains=100&departed_trains=0&departing_trains=100&station=MYR"))


(def ratadata (parse-string (:body resp) true))

(def data (into [] (reduce (fn [a b] (concat a (:timeTableRows b))) [] ratadata)))

(def filtered (filter (fn [m] (= "MYR" (:stationShortCode m))) data))

