(ns storefront.system
  (:require [taoensso.timbre :as timbre]
            [com.stuartsierra.component :as component]
            [clj-honeybadger.core :as honeybadger]
            [storefront.config :as config]
            [ring.component.jetty :refer [jetty-server]]
            [storefront.handler :refer [create-handler]]))

(defrecord AppHandler [logger exception-handler storeback environment prerender-token honeybadger-cljs-token]
  component/Lifecycle
  (start [c]
    (let [params (merge {:storeback-config storeback
                         :environment environment
                         :prerender-token prerender-token
                         :honeybadger-cljs-token honeybadger-cljs-token}
                        (select-keys c [:logger :exception-handler]))]
      (assoc c :handler (create-handler params))))
  (stop [c] c))

(defn logger [logger-config]
  (fn [level str]
    (timbre/log logger-config level str)))

(defn exception-handler [honeybadger-clj-token environment]
  (fn [e]
    (honeybadger/send-exception! e {:api-key honeybadger-clj-token
                                    :env environment})))

(defn system-map [config]
  (component/system-map
   :logger (logger (config :logging))
   :app-handler (map->AppHandler (select-keys config [:storeback :environment :prerender-token :honeybadger-cljs-token]))
   :embedded-server (jetty-server (config :server-opts))
   :exception-handler (exception-handler (config :honeybadger-clj-token) (config :environment))))

(defn dependency-map []
  {:app-handler [:logger :exception-handler]
   :embedded-server {:app :app-handler}})

(defn create-system
  ([] (create-system {}))
  ([config-overrides]
   (let [config (config/system-config config-overrides)]
     (component/system-using
      (system-map config)
      (dependency-map)))))
