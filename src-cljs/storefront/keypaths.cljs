(ns storefront.keypaths)

(def handle-message [:handle-message])

(def history [:history])
(def cookie [:cookie])
(def routes [:routes])

(def optimizely [:optimizely])
(def optimizely-variations [:optimizely :variations])

(def session-id [:session-id])

(def user [:user])
(def user-email (conj user :email))
(def user-token (conj user :user-token))
(def user-store-slug (conj user :store-slug))
(def user-id (conj user :id))
(def user-total-available-store-credit (conj user :total-available-store-credit))

(def order [:order])
(def order-token (conj order :token))
(def order-number (conj order :number))
(def order-covered-by-store-credit (conj order :covered_by_store_credit))
(def order-total-applicable-store-credit (conj order :total_applicable_store_credit))
(def order-shipments (conj order :shipments)) ;;TODO delete 'cause it doesn't exist
(def order-shipping-method (conj order :shipping-method))

(def last-order [:last-order])

(def promotions [:promotions])

(def past-orders [:past-orders])
(def my-order-ids [:my-order-ids])

(def store [:store])
(def store-slug (conj store :store_slug))
(def store-stylist-id (conj store :stylist_id))


(def taxons [:taxons])
(def products [:products])
(def states [:states])
(def payment-methods [:payment-methods])
(def shipping-methods [:shipping-methods])
(def sms-number [:sms-number])
(def api-cache [:api-cache])

(def ui [:ui])
(def api-requests (conj ui :api-requests))
(def return-navigation-event (conj ui :return-navigation-event))
(def navigation-message (conj ui :navigation-message))
(def navigation-event (conj navigation-message 0))
(def navigation-args (conj navigation-message 1))
(def browse-taxon-query (conj ui :browse-taxon-query))
(def browse-product-query (conj ui :browse-product-query))
(def browse-variant-query (conj ui :browse-variant-query))
(def browse-variant-quantity (conj ui :browse-variant-quantity))
(def browse-recently-added-variants (conj ui :browse-recently-added-variants))
(def past-order-id (conj ui :past-order-id))
(def menu-expanded (conj ui :menu-expanded))
(def account-menu-expanded (conj ui :account-menu-expanded))
(def shop-menu-expanded (conj ui :shop-menu-expanded))

(def sign-in (conj ui :sign-in))
(def sign-in-email (conj sign-in :email))
(def sign-in-password (conj sign-in :password))
(def sign-in-remember (conj sign-in :remember-me))

(def sign-up (conj ui :sign-up))
(def sign-up-email (conj sign-up :email))
(def sign-up-password (conj sign-up :password))
(def sign-up-password-confirmation (conj sign-up :password-confirmation))

(def forgot-password (conj ui :forgot-password))
(def forgot-password-email (conj forgot-password :email))

(def reset-password (conj ui :reset-password))
(def reset-password-password (conj reset-password :password))
(def reset-password-password-confirmation (conj reset-password :password-confirmation))
(def reset-password-token (conj reset-password :token))

(def manage-account (conj ui :manage-account))
(def manage-account-email (conj manage-account :email))
(def manage-account-password (conj manage-account :password))
(def manage-account-password-confirmation (conj manage-account :password-confirmation))

(def cart (conj ui :cart))
(def cart-quantities (conj cart :quantities))
(def cart-coupon-code (conj cart :coupon-code))

(def checkout (conj ui :checkout))
(def checkout-save-my-addresses (conj checkout :save-my-addresses))
(def checkout-ship-to-billing-address (conj checkout :ship-to-billing-address))
(def checkout-billing-address (conj checkout :billing-address))
(def checkout-billing-address-first-name (conj checkout-billing-address :first-name))
(def checkout-billing-address-last-name (conj checkout-billing-address :last-name))
(def checkout-billing-address-address1 (conj checkout-billing-address :address1))
(def checkout-billing-address-address2 (conj checkout-billing-address :address2))
(def checkout-billing-address-city (conj checkout-billing-address :city))
(def checkout-billing-address-state (conj checkout-billing-address :state))
(def checkout-billing-address-zip (conj checkout-billing-address :zipcode))
(def checkout-billing-address-phone (conj checkout-billing-address :phone))
(def checkout-shipping-address (conj checkout :shipping-address))
(def checkout-shipping-address-first-name (conj checkout-shipping-address :first-name))
(def checkout-shipping-address-last-name (conj checkout-shipping-address :last-name))
(def checkout-shipping-address-address1 (conj checkout-shipping-address :address1))
(def checkout-shipping-address-address2 (conj checkout-shipping-address :address2))
(def checkout-shipping-address-city (conj checkout-shipping-address :city))
(def checkout-shipping-address-state (conj checkout-shipping-address :state))
(def checkout-shipping-address-zip (conj checkout-shipping-address :zipcode))
(def checkout-shipping-address-phone (conj checkout-shipping-address :phone))
(def checkout-credit-card-name (conj checkout :credit-card-name))
(def checkout-credit-card-number (conj checkout :credit-card-number))
(def checkout-credit-card-expiration (conj checkout :credit-card-expiration))
(def checkout-credit-card-ccv (conj checkout :credit-card-ccv))
(def checkout-selected-shipping-method (conj checkout :shipping-method)) ;;TODO reorg to orders
(def checkout-selected-shipping-method-id (conj checkout-selected-shipping-method :id))
(def checkout-use-store-credits (conj checkout :use-store-credits))

(def flash (conj ui :flash))
(def flash-success (conj flash :success))
(def flash-success-message (conj flash-success :message))
(def flash-success-nav (conj flash-success :navigation))
(def flash-failure (conj flash :failure))
(def flash-failure-message (conj flash-failure :message))
(def flash-failure-nav (conj flash-failure :navigation))

(def billing-address [:billing-address])

(def shipping-address [:shipping-address])

(def stylist [:stylist])

(def stylist-sales-rep-email (conj stylist :sales-rep-email))

(def stylist-manage-account (conj stylist :manage-account))

(def stylist-commissions (conj stylist :commissions))
(def stylist-commissions-rate (conj stylist-commissions :rate))
(def stylist-commissions-next-amount (conj stylist-commissions :next-amount))
(def stylist-commissions-paid-total (conj stylist-commissions :paid-total))
(def stylist-commissions-new-orders (conj stylist-commissions :new-orders))
(def stylist-commissions-payouts (conj stylist-commissions :payouts))

(def stylist-bonus-credit (conj stylist :bonus-credits))
(def stylist-bonus-credit-bonus-amount (conj stylist-bonus-credit :bonus-amount))
(def stylist-bonus-credit-earning-amount (conj stylist-bonus-credit :earning-amount))
(def stylist-bonus-credit-commissioned-revenue (conj stylist-bonus-credit :commissioned-revenue))
(def stylist-bonus-credit-total-credit (conj stylist-bonus-credit :total-credit))
(def stylist-bonus-credit-available-credit (conj stylist-bonus-credit :available-credit))
(def stylist-bonus-credit-bonuses (conj stylist-bonus-credit :bonuses))

(def stylist-referral-program (conj stylist :referral-program))
(def stylist-referral-program-bonus-amount (conj stylist-referral-program :referral-program-bonus-amount))
(def stylist-referral-program-earning-amount (conj stylist-referral-program :referral-program-earning-amount))
(def stylist-referral-program-total-amount (conj stylist-referral-program :referral-program-total-amount))
(def stylist-referral-program-referrals (conj stylist-referral-program :referral-program-referrals))

(def validation-errors (conj ui :validation-errors))
(def validation-errors-message (conj validation-errors :error-message))
(def validation-errors-details (conj validation-errors :details))

(def reviews-loaded (conj ui :reviews-loaded))
