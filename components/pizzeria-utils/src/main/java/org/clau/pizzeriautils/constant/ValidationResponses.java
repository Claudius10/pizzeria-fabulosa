package org.clau.pizzeriautils.constant;

public final class ValidationResponses {

   private ValidationResponses() {
	  throw new IllegalStateException("Utility class");
   }

   public static final String NAME_INVALID = "InvalidName";

   public static final String EMAIL_INVALID = "InvalidEmail";

   public static final String EMAIL_NO_MATCH = "NoMatchEmail";

   public static final String PASSWORD_INVALID = "InvalidPassword";

   public static final String PASSWORD_NO_MATCH = "NoMatchPassword";

   public static final String NUMBER_INVALID = "InvalidPhoneNumber";

   public static final String ADDRESS_INVALID = "InvalidAddress";

   public static final String ORDER_DETAILS_DELIVERY_HOUR = "InvalidOrderDetailsDeliveryHour";

   public static final String ORDER_DETAILS_PAYMENT = "InvalidOrderDetailsPayment";

   public static final String ORDER_DETAILS_BILL = "InvalidOrderDetailsBillToChange";

   public static final String ORDER_DETAILS_COMMENT = "InvalidOrderDetailsComment";

   public static final String CART_IS_EMPTY = "InvalidCartIsEmpty";

   public static final String ORDER_CANCEL_TIME_ERROR = "InvalidOrderCancelTime";

   public static final String ORDER_VALIDATION_FAILED = "OrderValidationFailed";

   public static final String ORDER_STATE_UNKNOWN = "UnsupportedOrderState";
}