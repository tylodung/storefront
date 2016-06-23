(ns storefront.components.svg
  (:require #?(:clj [storefront.component-shim :as component]
                :cljs [storefront.component :as component])))

;; OPTIMIZATION TOOLS:
;; hiccup -> xml:           Let the browser do it... then delete the data-reactid's
;; svg -> optimized svg:    https://github.com/svg/svgo
;; xml -> hiccup:           http://htmltohiccup.herokuapp.com/
;;                              WARNING: this tool works, but converts 'viewBox' into 'viewbox', which is invalid SVG. Your SVG will render, but will be the wrong size.

(def micro-dollar-sign
  (component/html
   [:svg {:width "14" :height "13" :viewBox "0 0 14 13"}
    [:g.stroke-light-gray {:fill "none"}
     [:path {:d "M13 6.5c0 3.3-2.7 6-6 6s-6-2.7-6-6 2.7-6 6-6 6 2.7 6 6z"}]
     [:path {:d "M5.7 7.8c0 .72.58 1.3 1.3 1.3.72 0 1.3-.58 1.3-1.3 0-.72-.58-1.3-1.3-1.3-.72 0-1.3-.58-1.3-1.3 0-.72.58-1.3 1.3-1.3.72 0 1.3.58 1.3 1.3M7 3.1v6.8"}]]]))

(def large-dollar
  (component/html
   [:svg {:width "72" :height "72" :viewBox "0 0 72 72"}
    [:g {:stroke "#FFF" :fill "none"}
     [:circle {:r "35" :cy "36" :cx "36"}]
     [:path {:d "M28.391 43.609A7.608 7.608 0 0 0 36 51.217 7.609 7.609 0 1 0 36 36a7.609 7.609 0 1 1 7.609-7.609M36 16.217v39.566"}]]]))

(def large-percent
  (component/html
   [:svg {:width "72" :height "72" :viewBox "0 0 72 72"}
    [:g {:stroke "#FFF" :fill "none"}
     [:path {:d "M23.826 48.174l24.348-24.348M31.435 26.87a4.566 4.566 0 1 1-9.132-.002 4.566 4.566 0 0 1 9.132.002zM49.696 45.13a4.566 4.566 0 1 1-9.132 0 4.566 4.566 0 0 1 9.132 0z"}]
     [:path {:d "M71 36c0 19.33-15.67 35-35 35C16.672 71 1 55.33 1 36S16.672 1 36 1c19.33 0 35 15.67 35 35z"}]]]))

(def large-payout
  (component/html
   [:svg {:width "62" :height "60" :viewBox "0 0 62 60"}
    [:g {:stroke "#FFF" :fill "none"}
     [:path {:d "M1 55.045h10.609V35.272H1v19.773zM11.609 52.41C39.457 61.635 30.174 61.635 62 45.817c-2.82-2.8-5.046-3.463-7.957-2.636l-11.76 3.878"}]
     [:path {:d "M11.609 37.91h7.956c6.24 0 10.609 3.954 11.935 5.272h7.957c4.226 0 4.226 5.273 0 5.273H24.87M36.804 8.91c0 4.368 3.562 7.908 7.957 7.908s7.956-3.54 7.956-7.909C52.717 4.541 49.156 1 44.761 1c-4.395 0-7.957 3.54-7.957 7.91zM24.87 27.364c0 4.368 3.561 7.909 7.956 7.909 4.395 0 7.957-3.54 7.957-7.91 0-4.368-3.562-7.908-7.957-7.908s-7.956 3.54-7.956 7.909zM32.371 24.727V30M44.571 6.273v5.272"}]]]))

(def large-mail
  (component/html
   [:svg {:width "44" :height "44" :viewBox "0 0 44 44"}
    [:path {:d "M15.148 20.519h15.437a.482.482 0 0 0 0-.963H15.148a.482.482 0 0 0 0 .963z"}]
    [:path {:d "M22 43.036c-6.36 0-12.066-2.841-15.927-7.317V25.626l15.61 13.658a.481.481 0 0 0 .634 0l15.609-13.658v10.095c-3.86 4.474-9.566 7.315-15.926 7.315zM.964 22C.964 10.4 10.4.964 22 .964 33.6.964 43.036 10.4 43.036 22c0 4.69-1.543 9.024-4.146 12.526v-9.961c0-.03-.008-.056-.012-.083a.475.475 0 0 0-.153-.418l-5.233-4.579a.472.472 0 0 0-.19-.098v-8.543c0-.58-.475-1.054-1.055-1.054H11.753c-.58 0-1.054.473-1.054 1.054v8.553a.474.474 0 0 0-.155.089L5.31 24.065a.476.476 0 0 0-.156.302l-.01.031a.466.466 0 0 0-.034.167v9.96A20.925 20.925 0 0 1 .964 22zM33.3 25.047h3.824l-3.824 3.346v-3.346zm0-4.449l3.983 3.486H33.3v-3.486zm-21.639 8.54V10.843c0-.048.042-.09.09-.09h20.494c.049 0 .091.042.091.09v18.06c0 .097.035.182.085.258L22 38.28l-10.355-9.06c.006-.028.017-.054.017-.084zm-.963-4.091v3.347l-3.825-3.347H10.7zm0-.963H6.752l3.947-3.454v3.454zm28.188 11.998A21.9 21.9 0 0 0 44 22C44 9.869 34.13 0 22 0S0 9.869 0 22a21.9 21.9 0 0 0 5.11 14.078v.08c0 .266.215.481.482.481h.004C9.627 41.151 15.486 44 22 44c6.564 0 12.462-2.894 16.496-7.467a.474.474 0 0 0 .39-.451z"}]
    [:path {:d "M31.066 27.86a.481.481 0 0 0-.481-.482H15.148a.481.481 0 1 0 0 .963h15.437a.482.482 0 0 0 .481-.482M15.148 24.43h15.437a.481.481 0 1 0 0-.963H15.148a.482.482 0 0 0 0 .963M15.63 16.279h9.205v-1.626H15.63v1.626zm-.072.963h9.348c.492 0 .891-.4.891-.892v-1.77c0-.49-.4-.891-.891-.891h-9.348c-.491 0-.891.4-.891.891v1.77c0 .492.4.892.891.892z"}]]))

(defn adjustable-check [svg-options]
  [:svg (merge {:viewBox "0 0 14 14"} svg-options)
   [:g {:fill "none"
        :stroke-width ".5"
        :stroke-linecap "round"}
    [:path {:d "M9.61 5.17L5.7 8.83 4.39 7.52"}]
    [:circle {:r "6" :cx "7" :cy "7"}]]])

(defn bag [opts quantity]
  [:svg (merge {:width "25" :height "28" :viewBox "0 0 24 28"} opts)
   [:path {:class (if (pos? quantity) "fill-navy" "fill-black")
           :d "M17.31 9.772c.26 0 .498.216.498.498a.504.504 0 0 1-.498.498.49.49 0 0 1-.498-.498.49.49 0 0 1 .498-.498m-10.357 0a.49.49 0 0 1 .499.498.49.49 0 0 1-.499.498.504.504 0 0 1-.498-.498c0-.282.238-.498.498-.498M5.025 6.717h1.647V9.23c-.455.13-.78.542-.78 1.04 0 .585.476 1.083 1.061 1.083.607 0 1.084-.498 1.084-1.083 0-.498-.325-.91-.78-1.04V6.717h9.75V9.23c-.455.13-.78.542-.78 1.04 0 .585.476 1.083 1.083 1.083.585 0 1.062-.498 1.062-1.083 0-.498-.325-.91-.78-1.04V6.717h1.646c1.409 0 2.687 1.17 2.839 2.556l1.581 13.65c.173 1.408-.78 2.492-2.188 2.492H2.793c-1.408 0-2.361-1.084-2.21-2.492l1.582-13.65c.173-1.386 1.452-2.556 2.86-2.556M12.132.585a4.873 4.873 0 0 1 4.875 4.897v.671h-9.75v-.671A4.873 4.873 0 0 1 12.132.585m0-.585c-3.012 0-5.46 2.448-5.46 5.482v.671H5.025c-1.712 0-3.228 1.344-3.423 3.055L.02 22.858C-.175 24.57 1.082 26 2.793 26H21.47c1.711 0 2.968-1.43 2.773-3.142l-1.581-13.65c-.195-1.711-1.712-3.055-3.424-3.055h-1.646v-.671c0-3.034-2.449-5.482-5.46-5.482"}]])

(def counter-inc
  [:svg.stroke-pure-white {:width "1.2em" :height "1.2em" :viewBox "0 0 49 49" :title "Increment cart item count"}
   [:circle {:r "24" :cy "24" :cx "24"}]
   [:g {:stroke-width "4"} [:path {:d "M24 8v32M8 24h32"}]]])

(def counter-dec
  [:svg.stroke-pure-white {:width "1.2em" :height "1.2em" :viewBox "0 0 49 49" :title "Decrement cart item count"}
   [:circle {:r "24" :cy "24" :cx "24"}]
   [:path {:stroke-width "4" :d "M9 24h30"}]])