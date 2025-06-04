package org.clau.pizzeriaassetsclient.controller;

import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.service.StoreService;
import org.clau.pizzeriastoreassets.model.Store;
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
@Import({StoreControllerTests.MockStoreService.class})
public class StoreControllerTests {

	private final String path = Route.API + Route.V1 + Route.STORE_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StoreService storeService;

	@Test
	void givenGetStores_whenOk_thenReturnOk() throws Exception {

		// Arrange

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

		Mono<Object> stores = Mono.just(expected);

		doReturn(stores).when(storeService).findAll();

		// Act

		MvcResult result = mockMvc.perform(get(path)).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		List<Store> actual = (List<Store>) responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(actual.size()).isEqualTo(2);

		assertThat(actual.get(0).getId()).isEqualTo(expected.get(0).getId());
		assertThat(actual.get(0).getName()).isEqualTo(expected.get(0).getName());
		assertThat(actual.get(0).getImage()).isEqualTo(expected.get(0).getImage());
		assertThat(actual.get(0).getAddress()).isEqualTo(expected.get(0).getAddress());
		assertThat(actual.get(0).getPhoneNumber()).isEqualTo(expected.get(0).getPhoneNumber());

		assertThat(actual.get(1).getId()).isEqualTo(expected.get(1).getId());
		assertThat(actual.get(1).getName()).isEqualTo(expected.get(1).getName());
		assertThat(actual.get(1).getImage()).isEqualTo(expected.get(1).getImage());
		assertThat(actual.get(1).getAddress()).isEqualTo(expected.get(1).getAddress());
		assertThat(actual.get(1).getPhoneNumber()).isEqualTo(expected.get(1).getPhoneNumber());
	}

	@Test
	void givenGetStores_whenError_thenReturnInternalServerError() throws Exception {

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

		doReturn(offers).when(storeService).findAll();

		// Act

		MvcResult result = mockMvc.perform(get(path)).andReturn();

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
	public static class MockStoreService {

		@Bean
		@Primary
		StoreService storeService() {
			return mock(StoreService.class);
		}
	}
}
