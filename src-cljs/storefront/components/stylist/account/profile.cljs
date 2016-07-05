(ns storefront.components.stylist.account.profile
  (:require [storefront.component :as component]
            [storefront.components.ui :as ui]
            [storefront.events :as events]
            [storefront.keypaths :as keypaths]
            [storefront.platform.component-utils :as utils]
            [storefront.request-keys :as request-keys]))

(defn component [{:keys [saving?
                         address
                         user
                         birth-date]} owner opts]
  (component/create
   [:form {:on-submit
           (utils/send-event-callback events/control-stylist-account-profile-submit)}
    [:div.flex.flex-column.items-center.col-12
     [:h1.h2.light.col-12.my3.center "Update your info"]
     [:div.flex.col-12
      [:div.col-6 (ui/text-field "First Name"
                              (conj keypaths/stylist-manage-account :address :firstname)
                              (:firstname address)
                              {:autofocus "autofocus"
                               :type      "text"
                               :name      "account-first-name"
                               :data-test "account-first-name"
                               :id        "account-first-name"
                               :class     "rounded-left"
                               :required  true})]

      [:div.col-6 (ui/text-field "Last Name"
                              (conj keypaths/stylist-manage-account :address :lastname)
                              (:lastname address)
                              {:type      "text"
                               :name      "account-last-name"
                               :id        "account-last-name"
                               :data-test "account-last-name"
                               :class     "rounded-right border-width-left-0"
                               :required  true})]]

     (ui/text-field "Mobile Phone"
                    (conj keypaths/stylist-manage-account :address :phone)
                    (:phone address)
                    {:type      "tel"
                     :name      "account-phone"
                     :id        "account-phone"
                     :data-test "account-phone"
                     :required  true})

     (ui/text-field "Email"
                    (conj keypaths/stylist-manage-account :user :email)
                    (:email user)
                    {:type      "email"
                     :name      "account-email"
                     :id        "account-email"
                     :data-test "account-email"
                     :required  true})

     [:div.flex.flex-column.items-center.col-12
      (ui/text-field "Birthday"
                     (conj keypaths/stylist-manage-account :birth-date)
                     birth-date
                     {:type      "date"
                      :id        "account-birth-date"
                      :name      "account-birth-date"
                      :data-test "account-birth-date"
                      :required  true})]


     [:div.my2.col-12
      (ui/submit-button "Update" {:spinning? saving?
                                  :data-test "account-form-submit"})]]]))

(defn query [data]
  {:saving?    (utils/requesting? data request-keys/update-stylist-account-profile)
   :address    (get-in data (conj keypaths/stylist-manage-account :address))
   :birth-date (get-in data (conj keypaths/stylist-manage-account :birth-date))
   :user       (get-in data (conj keypaths/stylist-manage-account :user))} )