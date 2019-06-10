(ns app.core
  (:require [reagent.core :as r]))

(defn app []
  [:div.container "Hello World!!!!"])

(defn ^:export main []
  (r/render
   [app]
   (.getElementById js/document "app")))

