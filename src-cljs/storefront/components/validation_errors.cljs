(ns storefront.components.validation-errors
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.keypaths :as keypaths]
            [clojure.string :as string]))

(defn- field->human-name [key]
  (get {"billing-address" "Billing Address"
        "shipping-address" "Shipping Address"
        "address1" "Street Address"
        "address2" "Street Address (cont'd)"
        "first-name" "First Name"
        "last-name" "Last Name"}
       key
       key))

(defn- filter-errors [errors]
  (or (seq (filter #(not= % "can't be blank") errors))
      errors))

(defn- display-field-errors [[field errors]]
  (let [field-names (map field->human-name (string/split (name field) #"\."))]
    (->> (filter-errors errors)
         (map (fn [err] [:li (str (string/capitalize (string/join " " field-names))
                                  " "
                                  err)])))))

(defn validation-errors-component [data owner]
  (om/component
   (html
    (let [fields (get-in data keypaths/validation-errors-details)]
      (when (seq fields)
        [:div#errorExplanation.errorExplanation
         [:p "There were problems with the following fields:"]
         [:ul
          (map display-field-errors (sort-by first fields))]])))))

(defn redesigned-validation-errors-component [errors owner]
  (om/component
   (html
    (when (seq errors)
      [:div#errorExplanation.errorExplanation
       [:p "There were problems with the following fields:"]
       [:ul
        (map display-field-errors (sort-by first errors))]]))))
