(ns storefront.core
  (:require [storefront.config :as config]
            [storefront.state :as state]
            [storefront.tee :as tee]
            [storefront.keypaths :as keypaths]
            [storefront.events :as events]
            [storefront.components.top-level :refer [top-level-component]]
            [storefront.history :as history]
            [storefront.hooks.exception-handler :as exception-handler]
            [storefront.platform.messages :as messages]
            [storefront.effects :refer [perform-effects]]
            [storefront.transitions :refer [transition-state]]
            [storefront.trackings :refer [perform-track]]
            [cljs.reader :refer [read-string]]
            [om.core :as om]
            [clojure.data :refer [diff]]))

(when config/enable-console-print?
  (enable-console-print!))

(defn- transition [app-state [event args]]
  (reduce (fn [app-state dispatch]
            (try
              (or (transition-state dispatch event args app-state)
                  app-state)
              (catch js/TypeError te
                (exception-handler/report te {:context {:dispatch dispatch
                                                        :event event
                                                        :args args}}))))
          app-state
          (reductions conj [] event)))

(defn- effects [app-state-before-transition app-state-after-transition [event args]]
  (doseq [event-fragment (rest (reductions conj [] event))]
    (perform-effects event-fragment event args app-state-before-transition app-state-after-transition)))

(defn- track [app-state [event args]]
  (doseq [event-fragment (rest (reductions conj [] event))]
    (perform-track event-fragment event args app-state)))

(defn- log-deltas [old-app-state new-app-state [event args]]
  (let [[deleted added unchanged] (diff old-app-state new-app-state)]
    (js/console.groupCollapsed (clj->js event) (clj->js args))
    (js/console.log "Delta" (clj->js {:deleted deleted
                                      :added added}))
    (js/console.trace "Stacktrace")
    (js/console.groupEnd))
  new-app-state)

(defn- transition-log [app-state message]
  (log-deltas app-state (transition app-state message) message))

(defn handle-message
  ([app-state event] (handle-message app-state event nil))
  ([app-state event args]
   (let [message [event args]]
     ;; rename transition to transition-log to log messages
     (try
       (let [app-state-before @app-state]
         (om/transact! (om/root-cursor app-state) #(transition % message))
         (effects app-state-before @app-state message))
       (track @app-state message)
       (catch :default e
         (exception-handler/report e))))))

(defn listener-handle-message
  ([app-state event] (listener-handle-message app-state event nil))
  ([app-state event args]
   (try
     (om/transact! (om/root-cursor app-state) #(transition % [event args]))
     (catch :default e
       (exception-handler/report e)))))

(defn install-tap [app-state]
  (if-let [room-id (last (re-find #"listen=(.*)&?" js/window.location.search))]
    (do
      (js/console.log "Listening mode active: " room-id)
      (swap! app-state assoc :effectful? false)
      (tee/create-listener room-id (fn [msg]
                                     (js/console.log "handle" (pr-str msg))
                                     (apply handle-message app-state msg))))
    (tee/create-producer)))

(defn reload-app [app-state]
  (install-tap app-state)
  (set! messages/handle-message (partial handle-message app-state)) ;; in case it has changed
  (routes/start-history)
  (messages/handle-message app-state events/app-start)
  (history/set-current-page true))

(defn dom-ready [f]
  (if (not= (.-readyState js/document)
            "loading")
    (f)
    (.addEventListener js/document "DOMContentLoaded" f)))

(defn main- [app-state]
  (set! messages/handle-message (partial handle-message app-state))
  (history/start-history)
  (om/root
   top-level-component
   app-state
   {:target (.getElementById js/document "content")})
  (reload-app app-state))

(defn deep-merge
  [& maps]
  (if (every? map? maps)
    (apply merge-with deep-merge maps)
    (last maps)))

(defn consume-preloaded-data []
  (let [pre-data (read-string js/data)]
    (js-delete js/window "data")
    (set! (.-data js/window) js/undefined)
    pre-data))

(defonce main (memoize main-))
(defonce app-state (atom (deep-merge (state/initial-state)
                                     (consume-preloaded-data))))

(defn ^:export debug-app-state []
  (clj->js @app-state))

(defn on-jsload []
  (handle-message app-state events/app-stop)
  (reload-app app-state))

(defn ^:export fail []
  (try
    (throw (js/Error. "Pokemon"))
    (catch js/Error e
      (exception-handler/report e))))

(defn ^:export external-message [event args]
  (let [event (js->clj event)
        event (mapv keyword (if (coll? event) event [event]))]
    (handle-message app-state
                    event
                    (js->clj args :keywordize-keys true))))

(dom-ready #(main app-state))
