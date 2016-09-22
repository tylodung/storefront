(ns storefront.components.reset-password
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.components.facebook :as facebook]
            [storefront.components.ui :as ui]
            [storefront.platform.component-utils :as utils]
            [storefront.events :as events]
            [storefront.keypaths :as keypaths]))

(defn component [{:keys [reset-password reset-password-confirmation loaded-facebook? field-errors]} owner]
  (om/component
   (html
    (ui/narrow-container
     [:.h3.center.my2 "Update Your Password"]
     [:form.col-12
      {:on-submit (utils/send-event-callback events/control-reset-password-submit)}
      (ui/text-field {:errors     (get field-errors ["password"])
                      :keypath    keypaths/reset-password-password
                      :label      "Password"
                      :min-length 6
                      :required   true
                      :type       "password"
                      :value      reset-password})
      (ui/text-field {:errors   (get field-errors ["password_confirmation"])
                      :keypath  keypaths/reset-password-password-confirmation
                      :label    "Password Confirmation"
                      :required true
                      :type     "password"
                      :value    reset-password-confirmation})

      (ui/submit-button "Update")]
     [:.h5.center.gray.light.my2 "OR"]
     (facebook/reset-button loaded-facebook?)))))

(defn query [data]
  {:reset-password              (get-in data keypaths/reset-password-password)
   :reset-password-confirmation (get-in data keypaths/reset-password-password-confirmation)
   :loaded-facebook?            (get-in data keypaths/loaded-facebook)
   :field-errors                (get-in data keypaths/field-errors)})

(defn built-component [data opts]
  (om/build component (query data) opts))
