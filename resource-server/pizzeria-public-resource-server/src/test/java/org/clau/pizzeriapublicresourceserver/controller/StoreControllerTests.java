package org.clau.pizzeriapublicresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriadata.dao.assets.StoreRepository;
import org.clau.pizzeriadata.model.assets.Store;
import org.clau.pizzeriapublicresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.assets.StoreListDTO;
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
public class StoreControllerTests {

   private final String path = Route.API + Route.V1 + Route.RESOURCE + Route.STORE_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private StoreRepository storeRepository;

   @BeforeAll
   public void init() {
	  Store store = Store.builder()
		 .withName("test1")
		 .withAddress("test1")
		 .withImage("test1")
		 .withPhoneNumber(1)
		 .withSchedule(Map.of("en", "test1"))
		 .build();
	  storeRepository.save(store);
   }

   @Test
   void givenGetStoresApiCall_thenReturnResource() throws Exception {

	  // Act

	  // get api call to find store actual
	  MockHttpServletResponse response = mockMvc.perform(get(path)).andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  StoreListDTO storeList = objectMapper.readValue(response.getContentAsString(), StoreListDTO.class);
	  List<Store> actual = storeList.stores();

	  assertThat(actual).hasSize(1);
	  assertThat(actual.getFirst().getId()).isEqualTo(1);
	  assertThat(actual.getFirst().getImage()).isEqualTo("test1");
	  assertThat(actual.getFirst().getName()).isEqualTo("test1");
	  assertThat(actual.getFirst().getAddress()).isEqualTo("test1");
	  assertThat(actual.getFirst().getPhoneNumber()).isEqualTo(1);
   }
}
