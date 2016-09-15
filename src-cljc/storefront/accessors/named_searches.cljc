(ns storefront.accessors.named-searches
  (:require [storefront.utils.query :as query]
            [storefront.accessors.products :as products]
            [storefront.accessors.experiments :as experiments]
            [storefront.request-keys :as request-keys]
            [storefront.keypaths :as keypaths]))

(defn current-named-search [app-state]
  (query/get (get-in app-state keypaths/browse-named-search-query)
             (get-in app-state keypaths/named-searches)))

(defn current-named-searches [app-state]
  (let [disallowed-slugs (if (experiments/kinky-straight? app-state) #{} #{"kinky-straight"})
        named-searches   (query/all (dissoc (get-in app-state keypaths/browse-named-search-query) :slug)
                                    (get-in app-state keypaths/named-searches))]
    (remove (comp disallowed-slugs :slug) named-searches)))

(defn products-loaded? [app-state named-search]
  (every? (products/loaded-ids app-state) (:product-ids named-search)))

(defn first-with-product-id [named-searches product-id]
  (->> named-searches
       (filter #(-> % :product-ids set (contains? product-id)))
       first))

(def new-named-search? #{"kinky-straight"})

(defn is-closure? [named-search] (some-> named-search :search :category set (contains? "closures")))
(defn is-frontal? [named-search] (some-> named-search :search :category set (contains? "frontals")))
(defn is-extension? [named-search] (some-> named-search :search :category set (contains? "hair")))
(defn is-closure-or-frontal? [named-search] (or (is-closure? named-search) (is-frontal? named-search)))
(defn is-hair? [named-search] (or (is-extension? named-search) (is-closure-or-frontal? named-search)))

(def is-stylist-product? :stylist_only?)
(def eligible-for-reviews? (complement is-stylist-product?))
(def eligible-for-triple-bundle-discount? is-hair?)