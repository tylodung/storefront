(ns storefront.components.product
  (:require [storefront.components.utils :as utils]
            [storefront.keypaths :as keypaths]
            [storefront.events :as events]
            [storefront.experiments :as experiments]
            [storefront.query :as query]
            [storefront.taxons :refer [taxon-path-for taxon-class-name]]
            [storefront.components.breadcrumbs :refer [breadcrumbs]]
            [storefront.components.counter :refer [counter-component]]
            [storefront.components.reviews :refer [reviews-component]]
            [om.core :as om]
            [clojure.string :as string]
            [sablono.core :refer-macros [html]]))

(defn display-price [app-state product]
  (let [variant-query (get-in app-state keypaths/browse-variant-query)
        variant (or (->> product :variants (query/get variant-query))
                    (-> product :variants first))]
    (str "$" (.toFixed (js/parseFloat (variant :price))))))

(defn display-product-image [image]
  [:img {:src (:product_url image)}])

(defn number->words [n]
  (let [mapping ["Zero" "One" "Two" "Three" "Four" "Five" "Six" "Seven" "Eight" "Nine" "Ten" "Eleven" "Twelve" "Thirteen" "Fourteen" "Fifteen"]]
    (get mapping n (str "(x " n ")"))))

(defn display-bagged-variant [app-state {:keys [id quantity]}]
  (let [variant (query/get {:id id}
                           (->> (get-in app-state keypaths/products)
                                vals
                                (mapcat :variants)))
        product (first (filter #(contains? (set (:variants %)) variant)
                               (-> (get-in app-state keypaths/products)
                                   vals)))]
    [:div.item-added
     [:strong "Added to Bag: "]
     (str (number->words quantity)
          " "
          (-> (:options_text variant)
              (string/replace #"Length: " "")
              (string/replace #"''" " inch"))
          " "
          (:name product))]))

(defn display-variant [app-state variant checked?]
  [:li.keypad-item
   [:input.keypad-input {:type "radio"
                         :id (str "variant_id_" (:id variant))
                         :checked checked?
                         :on-change (utils/send-event-callback app-state
                                                               events/control-browse-variant-select
                                                               {:variant variant})}]
   [:label.keypad-label {:for (str "variant_id_" (:id variant))}
    (if (variant :can_supply?)
      [:div.variant-description
       (string/join "," (map :presentation (variant :option_values)))]
      [:div.variant-description.out-of-stock
       (string/join "," (map :presentation (variant :option_values)))
       [:br]
       "sold out"])]])

(defn product-component [data owner]
  (om/component
   (html
    (let [taxon (query/get (get-in data keypaths/browse-taxon-query)
                           (get-in data keypaths/taxons))
          taxon-path (if taxon (taxon-path-for taxon))
          product (query/get (get-in data keypaths/browse-product-query)
                             (vals (get-in data keypaths/products)))
          images (->> product :master :images)
          collection-name (:collection_name product)
          variants (:variants product)]
      (when product
        [:div
         (when taxon-path
           [:div
            [:div.taxon-products-banner
             {:class (if taxon-path (taxon-class-name taxon) "unknown")}]

            (breadcrumbs
             data
             ["Categories" [events/navigate-category {:taxon-path taxon-path}]]
             [(:name taxon) [events/navigate-category {:taxon-path taxon-path}]])])

         [:div.product-show {:item-type "http://schema.org/Product"}
          [:div#product-images
           [:div#main-image
            (cond
              (> (count images) 1)
              [:div#slides (map display-product-image images)]
              (seq images)
              (display-product-image (first images)))]
           [:div.product-info
            [:div.product-collection
             [:div.product-collection-indicator {:class collection-name}]
             [:span collection-name]]
            [:div.product-title {:item-prop "name"}
             (product :name)]]]
          [:div.cart-form-container
           [:div#cart-form
            [:form
             [:div#inside-product-cart-form {:item-prop "offers"
                                             :item-scope ""
                                             :item-type "http://schema.org/Offer"}
              (if (seq variants)
                [:div#product-variants
                 [:h6.ui-descriptor "Select a hair length in inches:"]
                 [:ul.keypad
                  (->> variants
                       (filter (comp seq :option_values))
                       (map-indexed
                        (fn [index variant]
                          (display-variant data
                                           variant
                                           (if-let [variant-query (get-in data keypaths/browse-variant-query)]
                                             (query/matches? variant-query variant)
                                             (= index 0))))))
                  [:div {:style {:clear "both"}}]]]
                [:input {:type "hidden"
                         :id (get-in product [:master :id])}])
              [:div.price-container
               [:div.quantity
                [:h4.quantity-label "Quantity"]
                (om/build counter-component data {:opts {:path keypaths/browse-variant-quantity}})]
               [:div#product-price.product-price
                [:span.price-label "Price:"]
                [:span.price.selling {:item-prop "price"}
                 (display-price data product)]
                [:span {:item-prop "priceCurrency" :content (:currency product)}]
                (if (get-in product [:master :can_supply?])
                  [:link {:item-prop "availability" :href "http://schema.org/InStock"}]
                  [:span.out-of-stock [:br] (str (:name product) " is out of stock.")])]

               [:div.add-to-cart {:style {:clear "both"}}
                [:input.large.primary#add-to-cart-button
                 {:type "submit"
                  :value "Add to Bag"
                  :on-click (utils/send-event-callback data events/control-browse-add-to-bag)}]]]]]

            (when-let [bagged-variants (seq (get-in data keypaths/browse-recently-added-variants))]
              [:div#after-add {:style {:display "block"}}
               [:div.added-to-bag-container
                (map (partial display-bagged-variant data) bagged-variants)]
               [:div.go-to-checkout
                [:a.cart-button
                 (utils/route-to data events/navigate-cart)
                 "Go to Checkout >>"
                 [:figure.checkout-cart]
                 [:figure.checkout-guarantee]]]])]]

          [:div
           [:div.left-of-reviews-wrapper
            [:div#product-collection-description.product-collection-description
             [:div.product-collection-circles-container
              [:div.product-collection-circles
               [:div.inner-product-collection-circles {:class (str "premier" (when-not (= collection-name "premier") " disabled"))}]
               [:div.inner-product-collection-circles {:class (str "deluxe" (when-not (= collection-name "deluxe") " disabled"))}]
               [:div.inner-product-collection-circles {:class (str "ultra" (when-not (= collection-name "ultra") " disabled"))}]]
              [:div.bar]]
             [:div.product-collection-text
              [:h3.sub-header (str collection-name ": ")]
              (product :collection_description)]]
            (when-let [html-description (:description product)]
              [:div#product-description.product-description
               [:h3.sub-header "Description"]
               [:div.product-description-text {:item-prop "description" :dangerouslySetInnerHTML {:__html html-description}}]])]

           (when (experiments/display-variation data "product-reviews")
             (om/build reviews-component data))]]

         [:div.gold-features
          [:figure.guarantee-feature]
          [:figure.free-shipping-feature]
          [:figure.triple-bundle-feature]]])))))
