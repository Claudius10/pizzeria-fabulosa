package org.clau.pizzeriapublicresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriadata.dto.assets.OfferListDTO;
import org.clau.pizzeriadata.model.assets.Offer;
import org.clau.pizzeriapublicresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriapublicresourceserver.dao.OfferRepository;
import org.clau.pizzeriautils.constant.ApiRoutes;
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
public class OfferControllerTests {

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.RESOURCE + ApiRoutes.OFFER;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private OfferRepository offerRepository;

   @BeforeAll
   public void init() {
	  Offer offer = Offer.builder()
		 .withImage("image1")
		 .withCaveat(Map.of("en", "test1"))
		 .withDescription(Map.of("en", "test1"))
		 .withName(Map.of("en", "test1"))
		 .build();
	  offerRepository.save(offer);
   }

   @Test
   void givenGetOffersApiCall_thenReturnResource() throws Exception {

	  // Act

	  // get api call to find offer actual
	  MockHttpServletResponse response = mockMvc.perform(get(path)).andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  OfferListDTO offerList = objectMapper.readValue(response.getContentAsString(), OfferListDTO.class);
	  List<Offer> actual = offerList.offers();

	  assertThat(actual).hasSize(1);
	  assertThat(actual.getFirst().getId()).isEqualTo(1);
	  assertThat(actual.getFirst().getImage()).isEqualTo("image1");
	  assertThat(actual.getFirst().getName().get("en")).isEqualTo("test1");
	  assertThat(actual.getFirst().getDescription().get("en")).isEqualTo("test1");
	  assertThat(actual.getFirst().getCaveat().get("en")).isEqualTo("test1");
   }
}
