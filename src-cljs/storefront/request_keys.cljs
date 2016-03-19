(ns storefront.request-keys)

(def get-taxons [:get-taxons])
(def get-store [:get-store])
(def get-promotions [:get-promotions])
(def get-products [:get-products])
(def get-product [:get-product])
(def get-states [:get-states])

(def sign-in [:sign-in])
(def sign-up [:sign-up])
(def facebook-sign-in [:facebook-sign-in])
(def reset-facebook [:reset-facebook])
(def forgot-password [:forgot-password])
(def reset-password [:reset-password])
(def get-account [:get-account])
(def update-account [:update-account])
(def update-account-address [:update-account-address])

(def get-stylist-account [:get-stylist-account])
(def update-stylist-account [:update-stylist-account])
(def update-stylist-account-profile-picture [:update-stylist-account-profile-picture])
(def get-stylist-stats [:get-stylist-stats])
(def get-stylist-commissions [:get-stylist-commissions])
(def get-stylist-bonus-credits [:get-stylist-bonus-credits])
(def get-stylist-referral-program [:get-stylist-referral-program])

(def get-sms-number [:get-sms-number])

(def get-shipping-methods [:shipping-methods])

(def update-cart [:update-cart])
(def checkout-cart [:checkout-cart])
(def update-line-item [:update-line-item])
(def increment-line-item [:update-line-item])
(def decrement-line-item [:update-line-item])
(def set-line-item [:update-line-item])
(def delete-line-item [:delete-line-item])
(def update-order [:update-order])
(def update-addresses [:update-addresses])
(def update-shipping-method [:update-shipping-method])
(def update-cart-payments [:update-cart-payments])
(def add-line-item [:add-line-item])
(def add-promotion-code [:add-promo-code])
(def remove-promotion-code [:remove-promo-code])
(def get-order [:get-order])
(def add-to-bag [:add-to-bag])
(def add-user-in-order [:add-user-in-order])
(def place-order [:place-order])
