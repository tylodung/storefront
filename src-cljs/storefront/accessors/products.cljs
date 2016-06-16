(ns storefront.accessors.products
  (:require [storefront.keypaths :as keypaths]
            [storefront.accessors.taxons :as taxons]))

(defn not-black-color [attr]
  (when (not= "black" (:color attr))
    (:color attr)))

(def ^:private frontal-summary [:style :material :origin :length (constantly "frontal")])
(def ^:private closure-summary [:style :material :origin :length (constantly "closure")])
(def ^:private bundle-summary [not-black-color :origin :length :style])

(defn closure? [variant]
  (= "closures" (get-in variant [:variant-attrs :category])))

(defn frontal? [variant]
  (= "frontals" (get-in variant [:variant-attrs :category])))

(defn bundle? [variant]
  (boolean (get-in variant [:variant-attrs :category])))

(defn summary [{:keys [variant-attrs product-name] :as variant}]
  (let [summary-fns (cond (closure? variant) closure-summary
                          (frontal? variant) frontal-summary
                          (bundle? variant)  bundle-summary
                          :else [(constantly product-name)])
        strs (filter identity ((apply juxt summary-fns) variant-attrs))]
    (clojure.string/join " " strs)))

(def ^:private frontal-product-title [:origin :style :material (constantly "frontal")])
(def ^:private closure-product-title [:origin :style :material (constantly "closure")])
(def ^:private bundle-product-title [not-black-color :origin :style])

(defn product-title [{:keys [variant-attrs product-name] :as variant}]
  (let [title-fns (cond (closure? variant) closure-product-title
                        (frontal? variant) frontal-product-title
                        (bundle? variant)  bundle-product-title
                        :else [(constantly product-name)])
        strs (filter identity ((apply juxt title-fns) variant-attrs))]
    (clojure.string/join " " strs)))

(defn thumbnail-url [products product-id]
  (get-in products [product-id :images 0 :small_url]))
