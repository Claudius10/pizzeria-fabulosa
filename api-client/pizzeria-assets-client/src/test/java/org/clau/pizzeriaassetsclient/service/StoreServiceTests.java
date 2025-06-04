package org.clau.pizzeriaassetsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.impl.StoreServiceImpl;
import org.clau.pizzeriastoreassets.dto.StoreListDTO;
import org.clau.pizzeriastoreassets.model.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

public class StoreServiceTests {

	private final String path = Route.API + Route.V1 + Route.STORE_BASE;

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
	void givenFindStores_whenOk_thenReturnOfferListDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		StoreService service = new StoreServiceImpl(webClient);

		List<Store> expected = List.of(
				Store.builder()
						.withId(1L)
						.withName("Name1")
						.withAddress("Address1")
						.withImage("Image1")
						.withPhoneNumber(1)
						.withSchedule(Map.of("en", "test1"))
						.build(),
				Store.builder()
						.withId(2L)
						.withName("Name2")
						.withAddress("Address2")
						.withImage("Image2")
						.withPhoneNumber(2)
						.withSchedule(Map.of("en", "test2"))
						.build()
		);

		String json = objectMapper.writeValueAsString(expected);

		prepareResponse(response -> response
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.setResponseCode(HttpStatus.OK.value())
				.setBody(json)
		);

		// Act

		Mono<Object> result = service.findAll();

		// Assert

		StepVerifier.create(result)
				.consumeNextWith(response -> {

					StoreListDTO actual = (StoreListDTO) response;
					assertThat(actual.stores().size()).isEqualTo(2);

					assertThat(actual.stores().get(0).getId()).isEqualTo(expected.get(0).getId());
					assertThat(actual.stores().get(0).getName()).isEqualTo(expected.get(0).getName());
					assertThat(actual.stores().get(0).getImage()).isEqualTo(expected.get(0).getImage());
					assertThat(actual.stores().get(0).getAddress()).isEqualTo(expected.get(0).getAddress());
					assertThat(actual.stores().get(0).getPhoneNumber()).isEqualTo(expected.get(0).getPhoneNumber());

					assertThat(actual.stores().get(1).getId()).isEqualTo(expected.get(1).getId());
					assertThat(actual.stores().get(1).getName()).isEqualTo(expected.get(1).getName());
					assertThat(actual.stores().get(1).getImage()).isEqualTo(expected.get(1).getImage());
					assertThat(actual.stores().get(1).getAddress()).isEqualTo(expected.get(1).getAddress());
					assertThat(actual.stores().get(1).getPhoneNumber()).isEqualTo(expected.get(1).getPhoneNumber());

				})
				.expectComplete()
				.verify(Duration.ofSeconds(3));


		expectRequestCount(1);
		expectRequest(request -> {
			assertThat(request.getPath()).isEqualTo(path);
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		});
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void givenFindStores_whenError_thenReturnResponseDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		StoreService service = new StoreServiceImpl(webClient);

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

		Mono<Object> result = service.findAll();

		// Assert

		StepVerifier.create(result)
				.consumeNextWith(response -> {

					ResponseDTO responseDTO = (ResponseDTO) response;
					assertThat(responseDTO.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

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
