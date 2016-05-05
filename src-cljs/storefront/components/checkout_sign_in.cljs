(ns storefront.components.checkout-sign-in
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.components.utils :as utils]
            [storefront.components.facebook :as facebook]
            [storefront.components.sign-in :as sign-in]
            [storefront.events :as events]
            [storefront.hooks.experiments :as experiments]
            [storefront.keypaths :as keypaths]))

(defn text-field [label keypath value input-attributes]
  (list
   [:input.col-10.h3.border.border-width-1.border-light-gray.border-teal-gradient.mb1.col-10.rounded-1.glow.floating-label
    (merge (utils/change-text keypath value)
           (when (seq value) {:class "has-value"})
           {:placeholder label}
           input-attributes)]
   [:label.col-10.h6.teal label]))

(def large-button-style {:class "my2 btn btn-large btn-primary btn-teal-gradient col-10"})
(def large-button-text :.h3.p1.extra-light.letter-spacing-1)

(defn redesigned-checkout-sign-in-component [sign-in-form-data owner]
  (om/component
   (html
    [:.flex.flex-column.items-center.black.sans-serif.bg-white
     [:.my2.h2.block. "I'm new here"]
     [:div
      (merge large-button-style
             {:on-click (utils/send-event-callback events/control-checkout-as-guest-submit)})
      [large-button-text "Guest Checkout"]]
     [:.my3.border.col-2.border-light-gray]
     [:.h2.mb1 "Already registered?"]
     [:.h5 "Sign into your account below, and checkout even faster!"]
     (om/build sign-in/redesigned-sign-in-form-component sign-in-form-data)])))

(defn old-checkout-sign-in [data owner]
  [:div.centered-content
   [:div.guest-checkout
    [:h2.explanation-header.center "I'm new here"]
    [:div.button.primary#guest-checkout-button
     {:on-click (utils/send-event-callback events/control-checkout-as-guest-submit)}
     "Guest Checkout"]

    [:div.short-divider]
    [:h2.explanation-header.center "Already registered?"]
    [:p.explanation.center "Sign into your account below, and checkout even faster!"]]
   [:div#existing-customer.new_spree_user
    (facebook/sign-in-button (get-in data keypaths/loaded-facebook))
    [:h2.center.or "OR"]
    [:form.simple_form
     {:on-submit (utils/send-event-callback events/control-sign-in-submit)}
     [:div#password-credentials
      [:div.input.email
       [:label.email "Email"]
       [:input.string.email
        (merge (utils/change-text data owner keypaths/sign-in-email)
               {:autofocus "autofocus"
                :type "email"
                :name "email"})]]
      [:div.input.password
       [:label.password "Password"]
       [:input.string.password
        (merge (utils/change-text data owner keypaths/sign-in-password)
               {:type "password"
                :name "password"})]]]
     [:p
      [:input#remember-me
       (merge (utils/change-checkbox data keypaths/sign-in-remember)
              {:type "checkbox"
               :name "remember-me"})]
      [:label {:for "remember-me"} "Remember me"]]
     [:a.forgot-password (utils/route-to events/navigate-forgot-password) "Forgot Password?"]
     [:p
      [:input.button.primary {:type "submit"
                              :value "Login"}]]]]
   [:p.center "Don't have an account? "
    [:a (utils/route-to events/navigate-sign-up) "Register Here"]]])

(defn checkout-sign-in-component [data owner]
  (om/component
   (html
    (if (experiments/three-steps-redesign? data)
      (om/build redesigned-checkout-sign-in-component (sign-in/query data))
      (om/build old-checkout-sign-in data)))))

(defn requires-sign-in-or-guest [app-state authorized-component]
  (if (or (get-in app-state keypaths/user-id)
          (get-in app-state keypaths/checkout-as-guest))
    authorized-component
    checkout-sign-in-component))
