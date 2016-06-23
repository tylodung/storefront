(ns storefront.components.sign-in
  (:require #?(:clj [storefront.component-shim :as component]
               :cljs [storefront.component :as component])
            [storefront.components.facebook :as facebook]
            [storefront.components.ui :as ui]
            [storefront.platform.component-utils :as utils]
            [storefront.events :as events]
            [storefront.keypaths :as keypaths]))

(defn form-component
  [{:keys [facebook-loaded?
           email
           password
           remember-me?
           get-satisfaction-login?]} _ _]
  (component/create
   [:div.flex.flex-column.items-center.black.sans-serif.col-12.mt1

    (facebook/sign-in-button facebook-loaded?)
    [:div.h4.gray.light.my2 "OR"]

    [:form.col-12.flex.flex-column.items-center
     {:on-submit (utils/send-event-callback events/control-sign-in-submit)}

     (ui/text-field "Email"
                    keypaths/sign-in-email
                    email
                    {:autofocus "autofocus"
                     :type      "email"
                     :name      "email"
                     :data-test "user-email"
                     :required  true})

     (ui/text-field "Password"
                    keypaths/sign-in-password
                    password
                    {:type      "password"
                     :name      "password"
                     :data-test "user-password"
                     :required  true})

     (ui/submit-button "Sign In"
                       {:data-test "user-submit"})

     [:div.mt2.col-12.mb3
      [:label.col.col-6.left-align.gray
       [:input.align-middle
        (merge (utils/toggle-checkbox keypaths/sign-in-remember remember-me?)
               {:type      "checkbox"
                :name      "remember-me"
                :data-test "user-remember"
                })]
       [:div.inline.ml1 "Remember me"]]
      [:a.col.col-6.right-align.gray
       (utils/route-to events/navigate-forgot-password) "Forgot Password?"]]]

    (when-not get-satisfaction-login?
      [:div.clearfix.center.gray.mb2 "Don't have an account? "
       [:a.green (utils/route-to events/navigate-sign-up) "Register Here"]])]))

(defn component [{:keys [get-satisfaction-login?] :as form-data} owner opts]
  (component/create
   (ui/narrow-container
    (when-not get-satisfaction-login?
      [:div.h2.center.my2 "Sign in to your account"])
    (component/build form-component form-data nil))))

(defn query [data]
  {:email                   (get-in data keypaths/sign-in-email)
   :password                (get-in data keypaths/sign-in-password)
   :remember-me?            (get-in data keypaths/sign-in-remember)
   :facebook-loaded?        (get-in data keypaths/loaded-facebook)
   :get-satisfaction-login? (get-in data keypaths/get-satisfaction-login?)})

(defn built-component [data owner opts]
  (component/create (component/build component (query data) nil)))

(defn requires-sign-in [app-state authorized-component]
  (let [sign-in-component built-component]
    (if (get-in app-state keypaths/user-id) authorized-component sign-in-component)))

(defn redirect-getsat-component [data owner opts]
  (component/create
   (ui/narrow-container
    ;; effects injects GetSat JS that will redirect / close this window as needed
    (if (or (nil? (get-in data keypaths/user))
            (get-in data keypaths/user-store-slug))
      [:div.page-heading.center "Signing in to the Mayvenn Stylist Community..."]
      [:div.flash.error
       "The Mayvenn Stylist Community is only for Mayvenn stylists. Become a stylist at welcome.mayvenn.com!"]))))