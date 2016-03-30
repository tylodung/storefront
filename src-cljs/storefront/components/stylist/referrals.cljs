(ns storefront.components.stylist.referrals
  (:require [om.core :as om]
            [sablono.core :refer-macros [html]]
            [storefront.components.formatters :as f]
            [storefront.components.svg :as svg]
            [storefront.components.utils :as utils]
            [storefront.components.stylist.pagination :as pagination]
            [storefront.utils.query :as query]
            [storefront.request-keys :as request-keys]
            [storefront.events :as events]
            [storefront.keypaths :as keypaths]))

(defn circular-progress [{:keys [radius stroke-width fraction-filled]}]
  (let [inner-radius    (- radius stroke-width)
        diameter        (* 2 radius)
        circumference   (* 2 js/Math.PI inner-radius)
        arc-length      (* circumference (- 1 fraction-filled))
        svg-circle-size {:r inner-radius :cy radius :cx radius :stroke-width stroke-width :fill "none"}]
    [:svg {:width diameter :height diameter}
     [:g {:transform (str "rotate(-90 " radius " " radius ")")}
      [:circle.stroke-silver svg-circle-size]
      [:circle.stroke-teal (merge svg-circle-size {:style {:stroke-dasharray circumference
                                                           :stroke-dashoffset arc-length}})]]]))

(defn profile-picture-circle [profile-picture-url]
  (let [width "4em"]
    [:.circle.bg-silver.overflow-hidden {:style {:width width :height width}}
     [:img {:src profile-picture-url :style {:width width}}]]))

(def state-radius 36)
(def state-diameter (* 2 state-radius))
(def no-sales-icon
  (let [width (str (- state-diameter 2) "px")]
    (html
     ;; Absolute centering: https://www.smashingmagazine.com/2013/08/absolute-horizontal-vertical-centering-css/
     [:.relative
      [:.h6.gray.muted.center.absolute.overlay.m-auto {:style {:height "1em"}} "No Sales"]
      [:.border-dashed.border-gray.circle {:style {:width width :height width}}]])))
(def paid-icon
  (let [width (str state-diameter "px")]
    (html
     (svg/adjustable-check {:class "stroke-teal" :width width :height width}))))

(defmulti state-icon (fn [state earning-amount commissioned-revenue] state))
(defmethod state-icon :referred [_ _ _] no-sales-icon)
(defmethod state-icon :paid [_ _ _] paid-icon)
(defmethod state-icon :in-progress [_ earning-amount commissioned-revenue]
  ;; Absolute centering: https://www.smashingmagazine.com/2013/08/absolute-horizontal-vertical-centering-css/
  [:.relative
   [:.center.absolute.overlay.m-auto {:style {:height "50%"}}
    ;; Explicit font size because font-scaling breaks the circular progress
    [:.h2.teal {:style {:font-size "18px"}} (f/as-money-without-cents (js/Math.floor commissioned-revenue))]
    [:.h6.gray.line-height-3 {:style {:font-size "9px"}} "of " (f/as-money-without-cents earning-amount)]]
   (circular-progress {:radius         state-radius
                       :stroke-width   5
                       :fraction-filled (/ commissioned-revenue earning-amount)})])

(defn show-referral [earning-amount {:keys [referred-stylist paid-at commissioned-revenue bonus-due]}]
  (html
   (let [{:keys [name join-date profile-picture-url]} referred-stylist
         state (cond
                 paid-at                      :paid
                 (zero? commissioned-revenue) :referred
                 :else                        :in-progress)]
     [:.flex.items-center.justify-between.border-bottom.border-left.border-right.border-silver.p2
      {:key (str name join-date)}
      [:.mr1 (profile-picture-circle profile-picture-url)]
      [:.flex-auto
       [:.h2 name]
       [:.h6.gray.line-height-4
        [:div "Joined " (f/long-date join-date)]
        (when (= state :paid)
          [:div "Credit Earned: " [:span.black (f/as-money-without-cents bonus-due) " on " (f/short-date paid-at)]])]]
      [:.ml1.sm-mr3 (state-icon state earning-amount commissioned-revenue)]])))

(defn show-lifetime-total [lifetime-total]
  (let [message (goog.string/format "You have earned %s in referrals since you joined Mayvenn."
                                    (f/as-money-without-cents lifetime-total))]
    [:.h6.muted
     [:.p3.to-sm-hide
      [:.mb1.center svg/micro-dollar-sign]
      [:div message]]
     [:.my3.flex.justify-center.items-center.sm-up-hide
      [:.mr1 svg/micro-dollar-sign]
      [:.center message]]]))

(defn show-refer-ad [sales-rep-email bonus-amount earning-amount]
  (let [mailto (str "mailto:" sales-rep-email "?Subject=Referral&body=name:%0D%0Aemail:%0D%0Aphone:")
        message (goog.string/format "Earn %s in credit when each stylist sells their first %s"
                                    (f/as-money-without-cents bonus-amount)
                                    (f/as-money-without-cents earning-amount))]
    [:div
     [:.py2.px3.to-sm-hide
      [:.center svg/large-mail]
      [:p.py1.h5.muted.line-height-2 message]
      [:.h3.col-8.mx-auto.mb3 [:a.col-12.btn.btn-primary.btn-teal-gradient {:href mailto :target "_top"} "Refer"]]]

     [:.p2.clearfix.sm-up-hide.border-bottom.border-silver
      [:.left.mx1 svg/large-mail]
      [:.right.ml2.m1.h3.col-4 [:a.col-12.btn.btn-primary.btn-big.btn-teal-gradient {:href mailto :target "_top"} "Refer"]]
      [:p.overflow-hidden.py1.h5.muted.line-height-2 message]]]))

(def empty-referrals
  (html
   [:.center.p3.to-sm-hide
    [:.m2.img-no-chat-icon.bg-no-repeat.bg-contain.bg-center {:style {:height "4em"}}]
    [:p.h2.gray.muted "Looks like you haven't" [:br] "referred anyone yet."]]))

(defn stylist-referrals-component [{:keys [sales-rep-email
                                           earning-amount
                                           bonus-amount
                                           lifetime-total
                                           referrals
                                           page
                                           pages
                                           fetching?]} _]
  (om/component
   (html
    (if (and (empty? (seq referrals)) fetching?)
      (utils/spinner {:height "100px"})
      [:.mx-auto.container {:data-test "referrals-panel"}
       [:.clearfix.mb3
        [:.sm-col-right.sm-col-4
         (when bonus-amount
           (show-refer-ad sales-rep-email bonus-amount earning-amount))]

        [:.sm-col.sm-col-8
         (when (seq referrals)
           [:div
            (for [referral referrals]
              (show-referral earning-amount referral))
            (pagination/fetch-more events/control-stylist-referrals-fetch fetching? page pages)])
         (when (zero? pages) empty-referrals)]
        [:.sm-col-right.sm-col-4.clearfix
         (when (and (seq referrals) (pos? lifetime-total))
           (show-lifetime-total lifetime-total))]]]))))

(defn stylist-referrals-query [data]
  {:sales-rep-email (get-in data keypaths/stylist-sales-rep-email)
   :earning-amount  (get-in data keypaths/stylist-referral-program-earning-amount)
   :bonus-amount    (get-in data keypaths/stylist-referral-program-bonus-amount)
   :lifetime-total  (get-in data keypaths/stylist-referral-program-lifetime-total)
   :referrals       (get-in data keypaths/stylist-referral-program-referrals)
   :page            (get-in data keypaths/stylist-referral-program-page)
   :pages           (get-in data keypaths/stylist-referral-program-pages)
   :fetching?       (query/get {:request-key request-keys/get-stylist-referral-program} (get-in data keypaths/api-requests))})
