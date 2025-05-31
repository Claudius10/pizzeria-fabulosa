package org.clau.pizzeriaassetsclient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.OrderService;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessassets.util.TestUtils.anonOrderStub;
import static org.clau.pizzeriabusinessassets.util.TestUtils.createdOrderDTOStub;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import({OrderControllerTests.MockOrderService.class})
public class OrderControllerTests {

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderService orderService;

	@Test
	void givenPostNewAnonOrder_whenOk_thenReturnCreatedAndCreatedOrderDTO() throws Exception {

		// Arrange

		NewAnonOrderDTO newAnonOrderDTO = anonOrderStub(
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
				false);

		CreatedOrderDTO expected = createdOrderDTOStub();

		Mono<Object> createdOrderDTO = Mono.just(expected);

		doReturn(createdOrderDTO).when(orderService).createAnonOrder(any());

		// Act

		MvcResult result = mockMvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAnonOrderDTO))
		).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		CreatedOrderDTO actual = (CreatedOrderDTO) responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		assertThat(actual.id()).isEqualTo(expected.id());
		assertThat(actual.formattedCreatedOn()).isEqualTo(expected.formattedCreatedOn());
		assertThat(actual.address()).isEqualTo(expected.address());

		assertThat(actual.customer()).isEqualTo(expected.customer());

		assertThat(actual.orderDetails()).isEqualTo(expected.orderDetails());

		assertThat(actual.cart().cartItems()).isEqualTo(expected.cart().cartItems());
		assertThat(actual.cart().totalCost()).isEqualTo(expected.cart().totalCost());
		assertThat(actual.cart().totalQuantity()).isEqualTo(expected.cart().totalQuantity());
		assertThat(actual.cart().totalCostOffers()).isEqualTo(expected.cart().totalCostOffers());
	}

	@Test
	void givenPostNewAnonOrder_whenError_thenReturnInternalServerError() throws Exception {

		// Arrange

		NewAnonOrderDTO newAnonOrderDTO = anonOrderStub(
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
				false);


		ResponseDTO responseDTOStub = ResponseDTO.builder()
				.apiError(APIError.
						builder()
						.withId(1L)
						.withCreatedOn(LocalDateTime.now())
						.withCause("TestException")
						.withMessage("TestMessage")
						.withOrigin("Tests")
						.withPath(path)
						.withFatal(false)
						.withLogged(false)
						.build())
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();

		Mono<Object> returnObject = Mono.just(responseDTOStub);

		doReturn(returnObject).when(orderService).createAnonOrder(any());

		// Act

		MvcResult result = mockMvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAnonOrderDTO))
		).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		ResponseDTO responseDTO = (ResponseDTO) responseEntity.getBody();
		APIError actual = responseDTO.getApiError();
		APIError expected = responseDTOStub.getApiError();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(responseDTO.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getCause()).isEqualTo(expected.getCause());
		assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
		assertThat(actual.getOrigin()).isEqualTo(expected.getOrigin());
	}

	@TestConfiguration
	public static class MockOrderService {

		@Bean
		@Primary
		OrderService orderService() {
			return mock(OrderService.class);
		}
	}
}
