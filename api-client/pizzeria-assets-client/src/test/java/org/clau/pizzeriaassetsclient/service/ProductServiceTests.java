package org.clau.pizzeriaassetsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.impl.ProductServiceImpl;
import org.clau.pizzeriastoreassets.dto.ProductListDTO;
import org.clau.pizzeriastoreassets.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class ProductServiceTests {

	private final String path = Route.API + Route.V1 + Route.PRODUCT_BASE;

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
	void givenFindOProductsByType_whenOk_thenReturnProductListDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		ProductService service = new ProductServiceImpl(webClient);

		List<Product> expected = List.of(
				Product.builder()
						.withId(1L)
						.withType("pizza")
						.withImage("image1")
						.withName(Map.of("en", "Gluten Free"))
						.withDescription(Map.of("en", List.of("Bacon", "Cheese")))
						.withFormats(Map.of("m", Map.of("en", "Medium", "es", "Mediana")))
						.withPrices(Map.of("m", 13.30))
						.withAllergens(Map.of("en", List.of("Lactose")))
						.build(),
				Product.builder()
						.withId(2L)
						.withType("pizza")
						.withImage("image2")
						.withName(Map.of("en", "Gluten Free2"))
						.withDescription(Map.of("en", List.of("Bacon", "Cheese")))
						.withFormats(Map.of("m", Map.of("en", "Medium", "es", "Mediana")))
						.withPrices(Map.of("m", 13.30))
						.withAllergens(Map.of("en", List.of("Lactose2")))
						.build()
		);

		int pageSize = 5;
		int pageNumber = 0;
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
		Page<Product> products = new PageImpl<>(expected, pageRequest, 2);

		String json = objectMapper.writeValueAsString(products);

		prepareResponse(response -> response
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.setResponseCode(HttpStatus.OK.value())
				.setBody(json)
		);

		// Act

		Mono<Object> result = service.findAllByType("pizza", pageSize, pageNumber);

		// Assert

		StepVerifier.create(result)
				.consumeNextWith(response -> {

					ProductListDTO actual = (ProductListDTO) response;
					List<Product> actualContent = actual.content();

					assertThat(actual.last()).isTrue(); // pageSize = 5, totalElements 2 -> last = true
					assertThat(actual.number()).isEqualTo(pageNumber);
					assertThat(actual.size()).isEqualTo(pageSize);
					assertThat(actual.totalElements()).isEqualTo(expected.size());
					assertThat(actualContent.size()).isEqualTo(expected.size());

					assertThat(actualContent).isEqualTo(expected);

				})
				.expectComplete()
				.verify(Duration.ofSeconds(3));


		expectRequestCount(1);
		expectRequest(request -> {
			assertThat(request.getPath()).isEqualTo(path + "?type=pizza&pageSize=5&pageNumber=0");
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		});
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void givenFindOProductsByType_whenError_thenReturnResponseDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		ProductService service = new ProductServiceImpl(webClient);

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
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();

		String json = objectMapper.writeValueAsString(responseDTOStub);

		prepareResponse(response -> response
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setBody(json)
		);

		// Act

		Mono<Object> result = service.findAllByType("pizza", 5, 0);

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
			assertThat(request.getPath()).isEqualTo(path + "?type=pizza&pageSize=5&pageNumber=0");
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
