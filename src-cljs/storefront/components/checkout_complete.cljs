(ns storefront.components.checkout-complete
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.components.utils :as utils]
            [storefront.events :as events]))

(defn checkout-complete-component [_ _]
  (om/component
   (html
    [:div.checkout-container
     [:div.order-thank-you
      [:figure.shopping-bag]
      [:p.message "Thank you for your order!"]]

     [:div.solid-line-divider]

     [:p.order-thanks-detail
      "We've received your order and will being processing it right away. Once your order ships we will send you another e-mail confirmation."]
     [:a.big-button.left-half.button.primary
      (utils/route-to events/navigate-home)
      "Return Home"]])))
