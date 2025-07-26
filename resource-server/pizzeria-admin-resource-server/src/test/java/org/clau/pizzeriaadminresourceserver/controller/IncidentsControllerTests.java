package org.clau.pizzeriaadminresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriaadminresourceserver.MyTestConfiguration;
import org.clau.pizzeriaadminresourceserver.TestHelperService;
import org.clau.pizzeriaadminresourceserver.TestJwtHelperService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.user.RoleEnum;
import org.clau.pizzeriautils.dto.admin.IncidenceListDTO;
import org.clau.pizzeriautils.util.common.constant.MyApps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class IncidentsControllerTests {

   private final String path = Route.API + Route.V1 + Route.ADMIN_BASE + Route.INCIDENTS_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestJwtHelperService testJwtHelperService;

   @Autowired
   private TestHelperService testHelperService;

   @Test
   void givenGetAllErrorsByOrigin_thenReturnErrorList() throws Exception {

	  // Arrange

	  testHelperService.create("TEST", "TEST ERROR", MyApps.RESOURCE_SERVER_ADMIN, path, true);

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&pageNumber=0&pageSize=5")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  IncidenceListDTO actual = objectMapper.readValue(response.getContentAsString(), IncidenceListDTO.class);

	  assertThat(actual.size()).isEqualTo(5);
	  assertThat(actual.number()).isEqualTo(0);
	  assertThat(actual.totalElements()).isEqualTo(1);
	  assertThat(actual.last()).isTrue();
   }

}
