package org.clau.pizzeriaassetsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.impl.OfferServiceImpl;
import org.clau.pizzeriastoreassets.dto.OfferListDTO;
import org.clau.pizzeriastoreassets.model.Offer;
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

public class OfferServiceTests {

	private final String path = Route.API + Route.V1 + Route.OFFER_BASE;

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
	void givenFindOffers_whenOk_thenReturnOfferListDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		OfferService service = new OfferServiceImpl(webClient);

		List<Offer> expected = List.of(
				Offer.builder()
						.withId(1L)
						.withImage("image1")
						.withCaveat(Map.of("en", "test1"))
						.withDescription(Map.of("en", "test1"))
						.withName(Map.of("en", "test1"))
						.build(),
				Offer.builder()
						.withId(2L)
						.withImage("image2")
						.withCaveat(Map.of("en", "test2"))
						.withDescription(Map.of("en", "test2"))
						.withName(Map.of("en", "test2"))
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

					OfferListDTO actual = (OfferListDTO) response;
					assertThat(actual.offers().size()).isEqualTo(2);

					assertThat(actual.offers().get(0).getId()).isEqualTo(expected.get(0).getId());
					assertThat(actual.offers().get(0).getName()).isEqualTo(expected.get(0).getName());
					assertThat(actual.offers().get(0).getImage()).isEqualTo(expected.get(0).getImage());
					assertThat(actual.offers().get(0).getDescription()).isEqualTo(expected.get(0).getDescription());
					assertThat(actual.offers().get(0).getCaveat()).isEqualTo(expected.get(0).getCaveat());

					assertThat(actual.offers().get(1).getId()).isEqualTo(expected.get(1).getId());
					assertThat(actual.offers().get(1).getName()).isEqualTo(expected.get(1).getName());
					assertThat(actual.offers().get(1).getImage()).isEqualTo(expected.get(1).getImage());
					assertThat(actual.offers().get(1).getDescription()).isEqualTo(expected.get(1).getDescription());
					assertThat(actual.offers().get(1).getCaveat()).isEqualTo(expected.get(1).getCaveat());

				})
				.expectComplete()
				.verify(Duration.ofSeconds(3));


		expectRequestCount(1);
		expectRequest(request -> {
			assertThat(request.getPath()).isEqualTo(path);
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON.toString());
		});
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void givenFindOffers_whenError_thenReturnResponseDTO(ClientHttpConnector connector) throws Exception {

		// Arrange

		startServer(connector);

		OfferService service = new OfferServiceImpl(webClient);

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

		Mono<Object> result = service.findAll();

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
			assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON.toString());
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
