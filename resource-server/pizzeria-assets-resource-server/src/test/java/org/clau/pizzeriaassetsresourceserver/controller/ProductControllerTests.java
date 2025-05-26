package org.clau.pizzeriaassetsresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriaassetsresourceserver.dao.ProductRepository;
import org.clau.pizzeriastoreassets.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestcontainersConfiguration.class)
public class ProductControllerTests {

	private final String path = Route.BASE + Route.V1 + Route.PRODUCT_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@BeforeAll
	public void init() {
		Product product = Product.builder()
				.withType("pizza")
				.withImage("image1")
				.withName(Map.of("en", "Gluten Free"))
				.withDescription(Map.of("en", List.of("Bacon", "Cheese")))
				.withFormats(Map.of("m", Map.of("en", "Medium", "es", "Mediana")))
				.withPrices(Map.of("m", 13.30))
				.withAllergens(Map.of("en", List.of("Lactose")))
				.build();
		productRepository.save(product);
	}

	@Test
	void givenGetProductApiCall_thenReturnResource() throws Exception {

		// Act

		// get api call to find product list
		MockHttpServletResponse response = mockMvc.perform(get(path + "?type=pizza&pageNumber=0&pageSize=5"))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		ProductsDTO actual = objectMapper.readValue(response.getContentAsString(), ProductsDTO.class);

		assertThat(actual.size()).isEqualTo(5);
		assertThat(actual.number()).isEqualTo(0);
		assertThat(actual.totalElements()).isEqualTo(1);
		assertThat(actual.last()).isEqualTo(true);
		assertThat(actual.content().getFirst().getId()).isEqualTo(1L);
		assertThat(actual.content().getFirst().getType()).isEqualTo("pizza");
		assertThat(actual.content().getFirst().getImage()).isEqualTo("image1");
		assertThat(actual.content().getFirst().getName().get("en")).isEqualTo("Gluten Free");
		assertThat(actual.content().getFirst().getDescription().get("en")).isEqualTo(List.of("Bacon", "Cheese"));
		assertThat(actual.content().getFirst().getPrices().get("m")).isEqualTo(13.30);
	}

	public record ProductsDTO(
			List<Product> content,
			int number,
			int size,
			long totalElements,
			boolean last
	) {
	}
}
