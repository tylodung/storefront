(ns storefront.components.help
  (:require [storefront.keypaths :as keypaths]
            #?(:clj [storefront.component-shim :as component]
               :cljs [storefront.component :as component])
            [clojure.string :as string]
            [storefront.components.ui :as ui]))

(defn display-sms [number]
  (->> number
       (re-find #"(\d{3})(\d{3})(\d{4})")
       rest
       (string/join "-")
       (str "1-")))

(defn cell [link-attrs & body]
  [:a.dark-gray.btn.btn-outline.border-silver.pxp6.py2.sm-m2.m1
   (merge {:style {:height "134px" :width "142px"}}
          link-attrs)
   (into
    [:div.flex.flex-column.justify-between.items-center]
    body)])

(defn cell-icon [icon-class width height]
  [:div.bg-center.bg-contain.bg-no-repeat.mb1
   {:style {:height height :width width}
    :class icon-class}])
(def cell-text :div.f1.light.dark-gray)
(def cell-description :div.f4.navy.medium)

(defn component [{:keys [sms-number]} owner opts]
  (component/create
   [:div {:data-test "help-page"}
    [:div.py4.bg-white.center
     [:div.h1.navy.mb2 "Get in touch"]
     [:div.h3.dark-gray.light
      [:div "Have a problem?"]
      [:div "Need advice on a style or product?"]
      [:div "Here are a few ways to get a hold of us."]]]

    [:div.m2.center
     [:div
      [:div "Monday to Friday from 9am to 5pm PST"]
      [:div "Orders placed before 10am PST ship that day"]]
     [:div.my2.flex.flex-wrap.justify-center.border-bottom.border-top.border-light-silver.py2.line-height-4
      [:div.flex
       (cell
        {:href "http://help.mayvenn.com"
         :target "_blank"}
        (cell-icon "img-faqs-icon" "44px" "52px")
        [cell-text "FAQs"]
        [cell-description "Visit our help center"])
       (cell
        {:href "tel://+18885627952"}
        (cell-icon "img-phone-icon" "45px" "52px")
        [cell-text "Call"]
        [cell-description "1-888-562-7952"])]
      [:div.flex
       (cell
        (when sms-number {:href (str "sms://+1" sms-number)})
        (cell-icon "img-text-icon" "54px" "52px")
        [cell-text "Text"]
        [cell-description
         (if sms-number
           (display-sms sms-number)
           "Loading...")])
       (cell
        {:href "mailto:help@mayvenn.com"}
        (cell-icon "img-email-icon" "49px" "52px")
        [cell-text "Email"]
        [cell-description
         [:span {:style {:word-break "break-all"}} "help@mayvenn.com"]])]]]

    [:div.mx-auto.col-8.mb4.mt3
     [:div.h1.navy.mb2.center "Company Information"]
     [:div.lg-up-hide.h4.dark-gray.light
      [:div.mb2  [:span.bold "CEO: "] [:span {:item-prop "founder"} "Diishan Imira"]]
      [:div.mb2  [:span.bold  "Headquarters: "] [:span {:item-prop "address"} "Oakland, CA"]]
      [:div.mb2  [:span.bold  "Phone: "] [:span {:item-prop "telephone" :content "+18885627952"} "+1 (888) 652-7952"]]
      [:div.mb2  [:span.bold "Website: "] [:span {:item-prop "url"} "https://mayvenn.com"]]]
     [:div.to-lg-hide.h4.dark-gray.light.center
      [:div.mb2  [:span.bold "CEO: "] [:span {:item-prop "founder"} "Diishan Imira"]]
      [:div.mb2  [:span.bold  "Headquarters: "] [:span {:item-prop "address"} "Oakland, CA"]]
      [:div.mb2  [:span.bold  "Phone: "] [:span {:item-prop "telephone" :content "+18885627952"} "+1 (888) 652-7952"]]
      [:div.mb2  [:span.bold "Website: "] [:span {:item-prop "url"} "https://mayvenn.com"]]]]]))

(defn query [data]
  {:sms-number (get-in data keypaths/sms-number)})

(defn built-component [data opts]
  (component/build component (query data) opts))
