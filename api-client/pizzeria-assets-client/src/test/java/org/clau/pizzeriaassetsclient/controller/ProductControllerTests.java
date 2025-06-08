package org.clau.pizzeriaassetsclient.controller;

import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.pizzeriaassetsclient.MyTestcontainersConfig;
import org.clau.pizzeriaassetsclient.service.ProductService;
import org.clau.pizzeriastoreassets.dto.ProductListDTO;
import org.clau.pizzeriastoreassets.model.Product;
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
@Import({ProductControllerTests.MockProductService.class, MyTestcontainersConfig.class})
public class ProductControllerTests {

	private final String path = Route.API + Route.V1 + Route.PRODUCT_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductService productService;

	@Test
	void givenGetProductsByType_whenOk_thenReturnOk() throws Exception {

		// Arrange

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
						.withType("beverage")
						.withImage("image2")
						.withName(Map.of("en", "Beer"))
						.withDescription(Map.of("en", List.of("Alcohol")))
						.withFormats(Map.of("m", Map.of("en", "330ML", "es", "330ML")))
						.withPrices(Map.of("m", 2D))
						.withAllergens(Map.of("en", List.of("Alcohol")))
						.build()
		);

		int pageSize = 5;
		int pageNumber = 0;
		boolean last = pageSize > expected.size();
		ProductListDTO productListDTO = new ProductListDTO(expected, pageNumber, pageSize, expected.size(), last);

		Mono<Object> productsByType = Mono.just(productListDTO);

		doReturn(productsByType).when(productService).findAllByType("pizza", pageSize, pageNumber);

		// Act

		MvcResult result = mockMvc.perform(get(path + "?type=pizza&pageNumber=0&pageSize=5")).andReturn();

		// Assert

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) result.getAsyncResult();
		Assertions.assertNotNull(responseEntity.getBody());
		ProductListDTO actual = (ProductListDTO) responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(actual.size()).isEqualTo(pageSize);
		assertThat(actual.number()).isEqualTo(pageNumber);
		assertThat(actual.totalElements()).isEqualTo(expected.size());
		assertThat(actual.last()).isEqualTo(last);
		assertThat(actual.content()).isEqualTo(expected);
	}

	@Test
	void givenGetProductsByType_whenError_thenReturnInternalServerError() throws Exception {

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

		doReturn(offers).when(productService).findAllByType("pizza", 5, 0);

		// Act

		MvcResult result = mockMvc.perform(get(path + "?type=pizza&pageNumber=0&pageSize=5")).andReturn();

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
	public static class MockProductService {

		@Bean
		@Primary
		ProductService productService() {
			return mock(ProductService.class);
		}
	}
}
