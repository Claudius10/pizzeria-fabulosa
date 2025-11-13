package org.clau.pizzeriapublicresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriapublicresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriautils.constant.ApiRoutes;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestcontainersConfiguration.class)
public class UtilControllerTests {

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.RESOURCE + ApiRoutes.UTIL + ApiRoutes.LOCAL_DATE_TIME_NOW;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Test
   void givenGetNow_thenReturnResource() throws Exception {
	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path)).andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  LocalDateTime date = objectMapper.readValue(response.getContentAsString(), LocalDateTime.class);
	  assertThat(date).isNotNull();
   }
}
