(ns storefront.platform.images
  (:require [sablono.core :refer [html]]
            [react-transition-group]))

(defn transition-group [options & children]
  (apply js/React.createElement js/ReactTransitionGroup.CSSTransitionGroup (clj->js options) (html children)))

(defn platform-hq-image [attrs]
  (transition-group {:transitionName {:appear "transparent"
                                      :appearActive "opaque"}
                     :transitionAppear true
                     :transitionAppearTimeout 500
                     :transitionEnter false
                     :transitionLeave false}
                    [:img.col-12.absolute.overlay.transition-2.transition-ease attrs]))
