package org.clau.pizzeriaassetsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.impl.OrderServiceImpl;
import org.clau.pizzeriabusinessassets.dto.CartItemDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.model.OrderDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessassets.util.TestUtils.anonOrderStub;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class OrderServiceTests {

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	static Stream<Arguments> arguments() {
		return Stream.of(argumentSet("Reactor Netty", new ReactorClientHttpConnector()));
	}

	private MockWebServer server;

	private WebClient webClient;

	@AfterEach
	void shutdown() throws IOException {
		if (server != null) {
			this.server.shutdown();
		}
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void givenCreateAnonOrder_whenOk_thenReturnCreatedOrderDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		OrderService service = new OrderServiceImpl(webClient);

		// body of POST
		NewAnonOrderDTO body = anonOrderStub(
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

		String formattedCreatedOn = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

		// expected return
		Order expected = Order.builder()
				.withId(1L)
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(formattedCreatedOn)
				.withAddress(body.address())
				.withAnonCustomerName(body.customer().anonCustomerName())
				.withAnonCustomerEmail(body.customer().anonCustomerEmail())
				.withAnonCustomerContactNumber(body.customer().anonCustomerContactNumber())
				.withOrderDetails(OrderDetails.builder()
						.withId(1L)
						.withPaymentMethod(body.orderDetails().paymentMethod())
						.withDeliveryTime(body.orderDetails().deliveryTime())
						.withStorePickUp(body.orderDetails().storePickUp())
						.withBillToChange(body.orderDetails().billToChange())
						.withComment(body.orderDetails().comment())
						.build())
				.withCart(Cart.builder()
						.withId(1L)
						.withTotalCost(body.cart().totalCost())
						.withTotalCostOffers(body.cart().totalCostOffers())
						.withTotalQuantity(body.cart().totalQuantity())
						.withCartItems(body.cart().cartItems().stream().map(item -> CartItem.builder()
								.withId(1L)
								.withDescription(item.description())
								.withPrice(item.price())
								.withQuantity(item.quantity())
								.withFormats(item.formats())
								.withType(item.type())
								.withName(item.name())
								.withQuantity(item.quantity())
								.build()).toList())
						.build())
				.build();

		String json = objectMapper.writeValueAsString(expected);

		// return expected in response
		prepareResponse(response -> response
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.setResponseCode(HttpStatus.CREATED.value())
				.setBody(json)
		);

		// Act

		Mono<Object> result = service.createAnonOrder(body);

		// Assert

		StepVerifier.create(result)
				.consumeNextWith(response -> {

					CreatedOrderDTO actual = (CreatedOrderDTO) response;

					assertThat(actual.id()).isEqualTo(expected.getId());
					assertThat(actual.formattedCreatedOn()).isEqualTo(formattedCreatedOn);
					assertThat(actual.address()).isEqualTo(expected.getAddress());

					assertThat(actual.customer().anonCustomerName()).isEqualTo(expected.getAnonCustomerName());
					assertThat(actual.customer().anonCustomerEmail()).isEqualTo(expected.getAnonCustomerEmail());
					assertThat(actual.customer().anonCustomerContactNumber()).isEqualTo(expected.getAnonCustomerContactNumber());

					assertThat(actual.orderDetails().paymentMethod()).isEqualTo(expected.getOrderDetails().getPaymentMethod());
					assertThat(actual.orderDetails().deliveryTime()).isEqualTo(expected.getOrderDetails().getDeliveryTime());
					assertThat(actual.orderDetails().storePickUp()).isEqualTo(expected.getOrderDetails().getStorePickUp());
					assertThat(actual.orderDetails().billToChange()).isEqualTo(expected.getOrderDetails().getBillToChange());
					assertThat(actual.orderDetails().comment()).isEqualTo(expected.getOrderDetails().getComment());

					assertThat(actual.cart().totalCost()).isEqualTo(expected.getCart().getTotalCost());
					assertThat(actual.cart().totalCostOffers()).isEqualTo(expected.getCart().getTotalCostOffers());
					assertThat(actual.cart().totalQuantity()).isEqualTo(expected.getCart().getTotalQuantity());

					List<CartItemDTO> actualItems = actual.cart().cartItems();
					List<CartItem> expectedItems = expected.getCart().getCartItems();
					assertThat(actualItems).hasSameSizeAs(expectedItems);

					for (int i = 0; i < actualItems.size(); i++) {
						assertThat(actualItems.get(i).id()).isEqualTo(expectedItems.get(i).getId());
						assertThat(actualItems.get(i).description()).isEqualTo(expectedItems.get(i).getDescription());
						assertThat(actualItems.get(i).price()).isEqualTo(expectedItems.get(i).getPrice());
						assertThat(actualItems.get(i).quantity()).isEqualTo(expectedItems.get(i).getQuantity());
						assertThat(actualItems.get(i).formats()).isEqualTo(expectedItems.get(i).getFormats());
						assertThat(actualItems.get(i).type()).isEqualTo(expectedItems.get(i).getType());
						assertThat(actualItems.get(i).name()).isEqualTo(expectedItems.get(i).getName());
					}

				})
				.expectComplete()
				.verify(Duration.ofSeconds(3));

		expectRequestCount(1);
		expectRequest(request -> {
			assertThat(request.getPath()).isEqualTo(path);
			assertThat(request.getMethod()).isEqualTo(HttpMethod.POST.name());
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
			assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		});
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void givenCreateAnonOrder_whenError_thenReturnResponseDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		OrderService service = new OrderServiceImpl(webClient);

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

		String json = objectMapper.writeValueAsString(responseDTOStub);

		prepareResponse(response -> response
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setBody(json)
		);

		// Act

		Mono<Object> result = service.createAnonOrder(anonOrderStub(
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
				false));

		// Assert

		StepVerifier.create(result)
				.consumeNextWith(response -> {

					ResponseDTO responseDTO = (ResponseDTO) response;
					assertThat(responseDTO.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

					APIError actual = responseDTO.getApiError();
					APIError expected = responseDTO.getApiError();

					assertThat(actual.getId()).isEqualTo(expected.getId());
					assertThat(actual.getCause()).isEqualTo(expected.getCause());
					assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
					assertThat(actual.getOrigin()).isEqualTo(expected.getOrigin());

				})
				.expectComplete()
				.verify(Duration.ofSeconds(3));


		expectRequestCount(1);
		expectRequest(request -> {
			assertThat(request.getPath()).isEqualTo(path);
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		});
	}

	private void expectRequest(Consumer<RecordedRequest> consumer) {
		try {
			consumer.accept(this.server.takeRequest());
		} catch (InterruptedException ex) {
			throw new IllegalStateException(ex);
		}
	}


	private void expectRequestCount(int count) {
		assertThat(this.server.getRequestCount()).isEqualTo(count);
	}

	private void prepareResponse(Consumer<MockResponse> consumer) {
		MockResponse response = new MockResponse();
		consumer.accept(response);
		this.server.enqueue(response);
	}

	private void startServer(ClientHttpConnector connector) {
		this.server = new MockWebServer();
		this.webClient = WebClient
				.builder()
				.clientConnector(connector)
				.baseUrl(this.server.url("/").toString())
				.build();
	}
}
