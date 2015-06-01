(ns storefront.components.checkout-steps
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.keypaths :as keypaths]
            [storefront.events :as events]
            [storefront.components.utils :refer [route-to]]
            [clojure.string :as string]))

(def checkout-steps
  [{:event events/navigate-checkout-address
    :name "address"}
   {:event events/navigate-checkout-delivery
    :name "delivery"}
   {:event events/navigate-checkout-payment
    :name "payment"}
   {:event events/navigate-checkout-confirmation
    :name "confirm"}])

(defn display-progress-step [app-state current-index index {step-name :name}]
  [:li.progress-step
   {:class (->> [step-name
                 (if (< index current-index) "completed")
                 (if (= index (inc current-index)) "next")
                 (if (= index current-index) "current")
                 (if (zero? index) "first")
                 (if (= index (dec (count checkout-steps))) "last")]
                (filter seq)
                (string/join " "))}
   [:span
    (let [text [:div.progress-step-index
                (str (inc index) " ")
                (string/capitalize step-name)]]
      (if (< index current-index)
        [:a (route-to app-state (get-in checkout-steps [index :event])) text]
        text))]])

(defn checkout-step-bar [data]
  (let [[current-index current-step] (->> checkout-steps
                                          (map-indexed vector)
                                          (filter #(= (:event (second %)) (get-in data keypaths/navigation-event)))
                                          first)]
    [:div.row
     [:div.columns.thirteen.omega
      [:ol {:class (str "progress-steps checkout-step-" (:name current-step))}
       (map-indexed (partial display-progress-step data current-index)
                    checkout-steps)]]]))
