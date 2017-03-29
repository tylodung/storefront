(ns storefront.components.stylist.account.commission
  (:require [storefront.component :as component]
            [storefront.components.ui :as ui]
            [storefront.events :as events]
            [storefront.keypaths :as keypaths]
            [storefront.platform.component-utils :as utils]
            [storefront.request-keys :as request-keys]
            [storefront.accessors.experiments :as experiments]))

(defn green-dot-component
  [{:keys [green-dot focused field-errors]} owner opts]
  (let [{:keys [first-name last-name card-number expiration-date]} green-dot]
    (component/create
     [:div
      (ui/text-field-group {:data-test "green-dot-first-name"
                            :errors    (get field-errors ["green_dot_attributes" "card_first_name"])
                            :id        "green-dot-first-name"
                            :keypath   (conj keypaths/stylist-manage-account :green_dot_payout_attributes :card_first_name)
                            :focused   focused
                            :label     "Card First Name"
                            :name      "green-dot-first-name"
                            :required  true
                            :type      "text"
                            :value     first-name}
                           {:data-test "green-dot-last-name"
                            :errors    (get field-errors ["green_dot_attributes" "card_last_name"])
                            :id        "green-dot-last-name"
                            :keypath   (conj keypaths/stylist-manage-account :green_dot_payout_attributes :card_last_name)
                            :focused   focused
                            :label     "Card Last Name"
                            :name      "green-dot-last-name"
                            :required  true
                            :type      "text"
                            :value     last-name})
      (ui/text-field {:data-test "green-dot-card-number"
                      :errors    (get field-errors ["green_dot_attributes" "card_last_name"])
                      :id        "green-dot-card-number"
                      :keypath   (conj keypaths/stylist-manage-account :green_dot_payout_attributes :card_number)
                      :focused   focused
                      :label     "Card Number"
                      :name      "green-dot-card-number"
                      :required  true
                      :type      "text"
                      :value     card-number})
      (ui/text-field {:data-test "green-dot-expiration-date"
                      :errors    (get field-errors ["green_dot_attributes" "card_last_name"])
                      :id        "green-dot-expiration-date"
                      :keypath   (conj keypaths/stylist-manage-account :green_dot_payout_attributes :expiration_date)
                      :focused   focused
                      :label     "Expiration Date (MM/YY)"
                      :name      "green-dot-expiration-date"
                      :required  true
                      :type      "text"
                      :value     expiration-date})
      [:p.ml1.mb3
       "We accept most bank or debit cards. Your commissions will be sent to this card and ready for use after payout is complete."]])))

(defn component [{:keys [focused
                         saving?
                         payout-method
                         payout-methods
                         venmo-phone
                         paypal-email
                         green-dot
                         address1
                         address2
                         zipcode
                         city
                         state-id
                         states
                         phone
                         field-errors]} owner opts]
  (component/create
   [:form
    {:on-submit
     (utils/send-event-callback events/control-stylist-account-commission-submit)}
    [:div.col.col-12.col-6-on-tb-dt
     [:h1.h3.light.my3.center.col-12 "Update commission info"]
     [:div.col-12.col-10-on-tb-dt.mx-auto
      (ui/select-field {:data-test "payout-method"
                        :errors    (get field-errors ["chosen_payout_method"])
                        :id        "payout-method"
                        :keypath   (conj keypaths/stylist-manage-account :chosen_payout_method)
                        :focused   focused
                        :label     "Payout Method"
                        :options   payout-methods
                        :required  true
                        :value     payout-method})
      (condp = payout-method
        "venmo"         (ui/text-field {:data-test "venmo-phone"
                                        :errors    (get field-errors ["venmo_payout_attributes" "phone"])
                                        :id        "venmo-phone"
                                        :keypath   (conj keypaths/stylist-manage-account :venmo_payout_attributes :phone)
                                        :focused   focused
                                        :label     "Venmo Phone #"
                                        :name      "venmo-phone"
                                        :required  true
                                        :type      "tel"
                                        :value     venmo-phone})
        "paypal"        (ui/text-field {:data-test "paypal-email"
                                        :errors    (get field-errors ["paypal_payout_attributes" "email"])
                                        :id        "paypal-email"
                                        :keypath   (conj keypaths/stylist-manage-account :paypal_payout_attributes :email)
                                        :focused   focused
                                        :label     "PayPal Email"
                                        :name      "paypal-email"
                                        :required  true
                                        :type      "email"
                                        :value     paypal-email})
        "green-dot"   (component/build green-dot-component
                                       {:green-dot green-dot
                                        :focused focused
                                        :field-errors field-errors}
                                       opts)

        "mayvenn_debit" [:p.ml1.mb3 "A prepaid Visa debit card will be mailed to the address entered here"]
        "check"         [:p.ml1.mb3 "Checks will mail to the address entered here"]
        [:p.ml1.mb3 "Checks will mail to the address entered here"])]]


    [:div.col.col-12.col-6-on-tb-dt
     [:div.mx-auto.col-12.col-10-on-tb-dt
      [:div.border-light-gray.border-top.hide-on-tb-dt.mb3]
      (ui/text-field {:data-test  "account-address1"
                      :errors     (get field-errors ["address" "address1"])
                      :id         "account-address1"
                      :keypath    (conj keypaths/stylist-manage-account :address :address1)
                      :focused    focused
                      :label      "Address"
                      :name       "account-address1"
                      :required   true
                      :type       "text"
                      :value      address1})

      [:div.col-12
       (ui/text-field-group
        {:type      "text"
         :label     "Apt/Suite"
         :keypath   (conj keypaths/stylist-manage-account :address :address2)
         :focused   focused
         :value     address2
         :errors    (get field-errors ["address" "address2"])
         :name      "account-address2"
         :data-test "account-address2"
         :id        "account-address2"}

        {:type       "text"
         :label      "Zip Code"
         :keypath    (conj keypaths/stylist-manage-account :address :zipcode)
         :focused    focused
         :value      zipcode
         :errors     (get field-errors ["address" "zipcode"])
         :name       "account-zipcode"
         :id         "account-zipcode"
         :data-test  "account-zipcode"
         :required   true
         :max-length 5
         :min-length 5
         :pattern    "\\d{5}"
         :title      "zip code must be 5 digits"})]

      (ui/text-field {:data-test "account-city"
                      :errors    (get field-errors ["address" "city"])
                      :id        "account-city"
                      :keypath   (conj keypaths/stylist-manage-account :address :city)
                      :focused   focused
                      :label     "City"
                      :name      "account-city"
                      :required  true
                      :type      "text"
                      :value     city})

      (ui/select-field {:data-test   "account-state"
                        :errors      (get field-errors ["address" "state"])
                        :id          :account-state
                        :keypath     (conj keypaths/stylist-manage-account :address :state_id)
                        :focused     focused
                        :label       "State"
                        :options     states
                        :placeholder "State"
                        :required    true
                        :value       state-id})

      (ui/text-field {:data-test "account-phone"
                      :errors    (get field-errors ["address" "phone"])
                      :id        :account-phone
                      :keypath   (conj keypaths/stylist-manage-account :address :phone)
                      :focused   focused
                      :label     "Mobile Phone"
                      :name      "account-phone"
                      :required  true
                      :type      "tel"
                      :value     phone})]]

    [:div.my2.col-12.clearfix
     ui/nbsp
     [:div.border-light-gray.border-top.hide-on-mb.mb3]
     [:div.col-12.col-5-on-tb-dt.mx-auto
      (ui/submit-button "Update" {:spinning? saving?
                                  :data-test "account-form-submit"})]]]))

(defn payout-methods [original-payout-method green-dot?]
  (cond-> [["Venmo" "venmo"]
           ["PayPal" "paypal"]
           ["Check" "check"]]

    green-dot?
    (conj ["Debit/Prepaid" "green-dot"])

    (= original-payout-method "mayvenn_debit")
    (conj ["Mayvenn Debit" "mayvenn_debit"])))

(defn green-dot-query [data]
  {:first-name ""
   :last-name ""
   :card-number ""
   :expiration-date ""})

(defn query [data]
  {:saving?        (utils/requesting? data request-keys/update-stylist-account-commission)
   :payout-method  (get-in data (conj keypaths/stylist-manage-account :chosen_payout_method))
   :payout-methods (payout-methods (get-in data (conj keypaths/stylist-manage-account :original_payout_method))
                                   (experiments/green-dot? data))
   :paypal-email   (get-in data (conj keypaths/stylist-manage-account :paypal_payout_attributes :email))
   :venmo-phone    (get-in data (conj keypaths/stylist-manage-account :venmo_payout_attributes :phone))
   :green-dot      (green-dot-query data)
   :address1       (get-in data (conj keypaths/stylist-manage-account :address :address1))
   :address2       (get-in data (conj keypaths/stylist-manage-account :address :address2))
   :city           (get-in data (conj keypaths/stylist-manage-account :address :city))
   :zipcode        (get-in data (conj keypaths/stylist-manage-account :address :zipcode))
   :state-id       (get-in data (conj keypaths/stylist-manage-account :address :state_id))
   :phone          (get-in data (conj keypaths/stylist-manage-account :address :phone))
   :states         (map (juxt :name :id) (get-in data keypaths/states))
   :field-errors   (get-in data keypaths/field-errors)
   :focused        (get-in data keypaths/ui-focus)})
