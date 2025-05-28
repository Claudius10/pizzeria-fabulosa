package org.clau.pizzeriabusinessassets.util;

import org.clau.pizzeriabusinessassets.dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class TestUtils {

	private TestUtils() {
		// no init
	}

	public static NewAnonOrderDTO anonOrderStub(String customerName,
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

		CartDTO cartStub = new CartDTO(
				1,
				14.75D,
				0.0,
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

		if (emptyCart) {
			cartStub = new CartDTO(
					0,
					0D,
					0D,
					List.of()
			);
		}

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

		CartDTO cart;

		if (emptyCart) {
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

		OrderDetailsDTO orderDetails = new OrderDetailsDTO(
				"ASAP",
				"Card",
				20D,
				null,
				false,
				null
		);

		String address = "Baker Street 221b";
		return new NewUserOrderDTO(address, orderDetails, cart);
	}

	public static CreatedOrderDTO createdOrderDTOStub() {

		CustomerDTO customerDTO = new CustomerDTO(
				"Tester",
				111222333,
				"tester@gmail.com"
		);

		OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(
				"ASAP",
				"Card",
				null,
				null,
				false,
				null
		);

		List<CartItemDTO> cartItemDTOList = List.of(
				new CartItemDTO(
						1L,
						"pizza",
						13.30,
						1,
						Map.of("en", "Gluten Free"),
						Map.of("en", List.of("Tomato Sauce", "100% Mozzarella", "Parmesan Cheese", "Emmental Cheese", "Blue Cheese")),
						Map.of("m", Map.of("en", "Medium"))
				)
		);

		CartDTO cartDTO = new CartDTO(
				1,
				13.30,
				0.0,
				cartItemDTOList
		);

		String formattedCreatedOn = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		String address = "Baker Street 221b";

		return new CreatedOrderDTO(1L, formattedCreatedOn, customerDTO, address, orderDetailsDTO, cartDTO);
	}
}
