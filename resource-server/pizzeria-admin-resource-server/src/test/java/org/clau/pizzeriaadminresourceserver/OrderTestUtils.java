package org.clau.pizzeriaadminresourceserver;

import org.clau.pizzeriadata.dto.business.CartDTO;
import org.clau.pizzeriadata.dto.business.CartItemDTO;
import org.clau.pizzeriadata.dto.business.NewUserOrderDTO;
import org.clau.pizzeriadata.dto.business.OrderDetailsDTO;

import java.util.List;
import java.util.Map;

public final class OrderTestUtils {

   private OrderTestUtils() {
	  throw new IllegalStateException("Utility class");
   }


   public static NewUserOrderDTO userOrderStub(boolean emptyCart) {

	  CartDTO cart = cartDTOStub(emptyCart);

	  OrderDetailsDTO orderDetails = orderDetailsDTOStub();

	  String address = "Baker Street 221b";
	  return new NewUserOrderDTO(address, orderDetails, cart);
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
}
