package org.clau.pizzeriabusinessresourceserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriabusinessassets.dto.*;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.MyTestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestcontainersConfiguration.class)
public class AnonControllerTests {

	private final String path = Route.BASE + Route.V1 + Route.ORDER_BASE + Route.ANON_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenAnonOrderPostApiCall_whenAllOk_thenReturnOrder() throws Exception {

		// Act

		// post api call to create anon order
		MockHttpServletResponse response = mockMvc.perform(post(path)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"customerEmail@gmail.com",
								"Street",
								5,
								"15",
								"E",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		Order order = objectMapper.readValue(response.getContentAsString(), Order.class);

		assertThat(order.getAnonCustomerName()).isEqualTo("customerName");
		assertThat(order.getAnonCustomerContactNumber()).isEqualTo(111222333);
		assertThat(order.getAnonCustomerEmail()).isEqualTo("customerEmail@gmail.com");

		assertThat(order.getAddress()).isEqualTo("Street 5 15 E");

		assertThat(order.getOrderDetails().getPaymentMethod()).isEqualTo("Cash");
		assertThat(order.getOrderDetails().getDeliveryTime()).isEqualTo("ASAP");
		assertThat(order.getOrderDetails().getStorePickUp()).isFalse();
		assertThat(order.getOrderDetails().getBillToChange()).isNull();
		assertThat(order.getOrderDetails().getChangeToGive()).isNull();
		assertThat(order.getOrderDetails().getComment()).isNull();

		assertThat(order.getCart().getTotalQuantity()).isEqualTo(1);
		assertThat(order.getCart().getTotalCost()).isEqualTo(14.75);
		assertThat(order.getCart().getTotalCostOffers()).isZero();
		assertThat(order.getCart().getCartItems().size()).isEqualTo(1);
		assertThat(order.getCart().getCartItems().getFirst().getPrice()).isEqualTo(14.75);
		assertThat(order.getCart().getCartItems().getFirst().getType()).isEqualTo("pizza");
		assertThat(order.getCart().getCartItems().getFirst().getName().get("en")).isEqualTo("Cuatro Quesos");
	}

	private NewAnonOrderDTO anonOrderStub(String customerName, int customerNumber, String customerEmail, String street,
										  int streetNumber, String floor, String door, Double changeRequested,
										  String deliveryHour, String paymentType, String comment,
										  boolean emptyCart) {

		CartDTO cartStub = new CartDTO(
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
				new OrderDetailsDTO(deliveryHour, paymentType, changeRequested, comment, false, 0D),
				cartStub
		);
	}
}
