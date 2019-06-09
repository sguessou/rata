(ns rata.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]))

(defn greet [req]
  {:status 200
   :body "Welcome to Rata!"
   :headers {}})

(defroutes routes
  (GET "/" [] greet)

  (ANY "/request" [] handle-dump)
  
  (not-found "Page not found."))

(def app
  (wrap-params
   routes))

(defn -main [port]
   (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
