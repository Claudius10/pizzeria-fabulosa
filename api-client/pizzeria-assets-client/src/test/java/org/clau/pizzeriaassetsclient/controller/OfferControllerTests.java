package org.clau.pizzeriaassetsclient.controller;

import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.OfferService;
import org.clau.pizzeriastoreassets.model.Offer;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import({OfferControllerTests.MockOfferService.class})
public class OfferControllerTests {

	private final String path = Route.API + Route.V1 + Route.OFFER_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OfferService offerService;

	@Test
	void givenGetOffers_whenOk_thenReturnOk() throws Exception {

		// Arrange

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

		Mono<Object> offers = Mono.just(expected);

		doReturn(offers).when(offerService).findAll();

		// Act

		MvcResult result = mockMvc.perform(get(path)).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		List<Offer> actual = (List<Offer>) responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(actual.size()).isEqualTo(2);

		assertThat(actual.get(0).getId()).isEqualTo(expected.get(0).getId());
		assertThat(actual.get(0).getName()).isEqualTo(expected.get(0).getName());
		assertThat(actual.get(0).getImage()).isEqualTo(expected.get(0).getImage());
		assertThat(actual.get(0).getDescription()).isEqualTo(expected.get(0).getDescription());
		assertThat(actual.get(0).getCaveat()).isEqualTo(expected.get(0).getCaveat());

		assertThat(actual.get(1).getId()).isEqualTo(expected.get(1).getId());
		assertThat(actual.get(1).getName()).isEqualTo(expected.get(1).getName());
		assertThat(actual.get(1).getImage()).isEqualTo(expected.get(1).getImage());
		assertThat(actual.get(1).getDescription()).isEqualTo(expected.get(1).getDescription());
		assertThat(actual.get(1).getCaveat()).isEqualTo(expected.get(1).getCaveat());
	}

	@Test
	void givenGetOffers_whenError_thenReturnInternalServerError() throws Exception {

		// Arrange

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

		Mono<Object> offers = Mono.just(responseDTOStub);

		doReturn(offers).when(offerService).findAll();

		// Act

		MvcResult result = mockMvc.perform(get(path)).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		ResponseDTO responseDTO = (ResponseDTO) responseEntity.getBody();
		APIError actual = responseDTO.getApiError();
		APIError expected = responseDTOStub.getApiError();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(responseDTO.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getCause()).isEqualTo(expected.getCause());
		assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
		assertThat(actual.getOrigin()).isEqualTo(expected.getOrigin());
	}

	@TestConfiguration
	public static class MockOfferService {

		@Bean
		@Primary
		OfferService offerService() {
			return mock(OfferService.class);
		}
	}
}
