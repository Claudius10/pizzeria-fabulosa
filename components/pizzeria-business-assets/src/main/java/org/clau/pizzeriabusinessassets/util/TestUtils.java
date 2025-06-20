package org.clau.pizzeriabusinessassets.util;

import org.clau.pizzeriabusinessassets.dto.*;
import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.model.OrderDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class TestUtils {

   private TestUtils() {
	  // no init
   }

   public static NewAnonOrderDTO anonOrderStub(
	  String customerName,
	  int customerNumber,
	  String customerEmail,
	  String street,
	  int streetNumber,
	  String floor,
	  String door,
	  Double changeRequested,
	  String deliveryHour,
	  String paymentType,
	  String comment,
	  boolean emptyCart) {

	  CartDTO cartStub = cartDTOStub(emptyCart);

	  String address = street + " " + streetNumber + " " + floor + " " + door;

	  return new NewAnonOrderDTO(
		 new CustomerDTO(
			customerName,
			customerNumber,
			customerEmail),
		 address,
		 new OrderDetailsDTO(deliveryHour, paymentType, changeRequested, comment, false, null),
		 cartStub
	  );
   }

   public static NewUserOrderDTO userOrderStub(boolean emptyCart) {

	  CartDTO cart = cartDTOStub(emptyCart);

	  OrderDetailsDTO orderDetails = orderDetailsDTOStub();

	  String address = "Baker Street 221b";
	  return new NewUserOrderDTO(address, orderDetails, cart);
   }

   public static CreatedOrderDTO createdOrderDTOStub() {

	  CustomerDTO customerDTO = customerDTOStub();

	  OrderDetailsDTO orderDetailsDTO = orderDetailsDTOStub();

	  CartDTO cartDTO = cartDTOStub(false);

	  String formattedCreatedOn = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	  String address = "Baker Street 221b";

	  return new CreatedOrderDTO(1L, formattedCreatedOn, customerDTO, address, orderDetailsDTO, cartDTO);
   }

   public static OrderDetailsDTO orderDetailsDTOStub() {
	  return new OrderDetailsDTO(
		 "ASAP",
		 "Card",
		 20D,
		 null,
		 false,
		 null
	  );
   }

   public static CartDTO cartDTOStub(boolean empty) {
	  CartDTO cart;

	  if (empty) {
		 cart = new CartDTO(
			0,
			0.0,
			0.0,
			List.of()
		 );
	  } else {
		 cart = new CartDTO(
			1,
			14.75D,
			0D,
			List.of(new CartItemDTO(
			   null,
			   "pizza",
			   14.75D,
			   1,
			   Map.of("es", "Cuatro Quesos", "en", "Cuatro Quesos"),
			   Map.of(
				  "es", List.of("Salsa de Tomate", "Mozzarella 100%", "Parmesano", "Emmental", "Queso Azul"),
				  "en", List.of("Tomato Sauce", "100% Mozzarella", "Parmesan Cheese", "Emmental Cheese", "Blue Cheese")
			   ),
			   Map.of("m", Map.of("es", "Mediana", "en", "Medium"), "l", Map.of(), "s", Map.of())
			))
		 );
	  }

	  return cart;
   }

   public static CustomerDTO customerDTOStub() {
	  return new CustomerDTO(
		 "Tester",
		 111222333,
		 "tester@gmail.com"
	  );
   }

   public static Order userOrderStub() {
	  return Order.builder()
		 .withId(1L)
		 .withUserId(1L)
		 .withCreatedOn(LocalDateTime.now())
		 .withFormattedCreatedOn(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
		 .withAddress("221b Baker Street")
		 .withOrderDetails(orderDetailsStub())
		 .withCart(cartStub())
		 .build();
   }

   public static OrderDetails orderDetailsStub() {
	  return OrderDetails.builder()
		 .withId(1L)
		 .withPaymentMethod("Cash")
		 .withDeliveryTime("20:15")
		 .withStorePickUp(false)
		 .build();
   }

   public static Cart cartStub() {
	  return Cart.builder()
		 .withId(1L)
		 .withTotalCost(20.0)
		 .withTotalCostOffers(10.0)
		 .withTotalQuantity(2)
		 .withCartItems(List.of(
			   CartItem.builder()
				  .withId(1L)
				  .withType("pizza")
				  .withName(Map.of("es", "Cuatro Quesos", "en", "Cuatro Quesos"))
				  .withDescription(Map.of(
					 "es", List.of("Salsa de Tomate", "Mozzarella 100%", "Parmesano", "Emmental", "Queso Azul"),
					 "en", List.of("Tomato Sauce", "100% Mozzarella", "Parmesan Cheese", "Emmental Cheese", "Blue Cheese")
				  ))
				  .withFormats(Map.of("m", Map.of("es", "Mediana", "en", "Medium"), "l", Map.of(), "s", Map.of()))
				  .withPrice(10.0)
				  .withQuantity(2)
				  .build()
			)
		 )
		 .build();
   }
}
