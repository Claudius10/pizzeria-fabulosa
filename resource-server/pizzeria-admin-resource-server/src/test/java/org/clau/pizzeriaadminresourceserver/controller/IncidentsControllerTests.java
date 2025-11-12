package org.clau.pizzeriaadminresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriaadminresourceserver.MyTestConfiguration;
import org.clau.pizzeriaadminresourceserver.TestHelperService;
import org.clau.pizzeriaadminresourceserver.TestJwtHelperService;
import org.clau.pizzeriadata.dto.admin.IncidenceListDTO;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.enums.RoleEnum;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ADMIN_BASE + ApiRoutes.INCIDENTS_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestJwtHelperService testJwtHelperService;

   @Autowired
   private TestHelperService testHelperService;

   @Test
   void givenGetAllErrorsByOrigin_whenStarDate_thenReturnErrorList() throws Exception {

	  // Arrange

	  String startDate = "2025-08-02T09:37:01.924Z";
	  String cleanDateTwo = startDate.substring(0, startDate.indexOf('T')) + "T11:00:00";
	  LocalDateTime nowTwo = LocalDateTime.parse(cleanDateTwo, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	  testHelperService.createApiError("TEST", "TEST ERROR", MyApps.RESOURCE_SERVER_ADMIN, path, true, nowTwo);

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&startDate=" + startDate + "&endDate=")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  IncidenceListDTO actual = objectMapper.readValue(response.getContentAsString(), IncidenceListDTO.class);

	  assertThat(actual.content().size()).isEqualTo(1);
   }

   @Test
   void givenGetAllErrorsByOrigin_whenStarDateAndEndDate_thenReturnErrorList() throws Exception {

	  // Arrange

	  // createApiError error 1
	  String startDate = "2025-08-02T09:37:01.924Z";
	  String cleanDateTwo = startDate.substring(0, startDate.indexOf('T')) + "T11:00:00";
	  LocalDateTime nowTwo = LocalDateTime.parse(cleanDateTwo, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	  testHelperService.createApiError("TEST", "TEST ERROR", MyApps.RESOURCE_SERVER_ADMIN, path, true, nowTwo);

	  // createApiError error 2
	  String endDate = "2025-08-03T09:37:01.924Z";
	  String cleanDate = endDate.substring(0, endDate.indexOf('T')) + "T11:00:00";
	  LocalDateTime now = LocalDateTime.parse(cleanDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	  testHelperService.createApiError("TEST", "TEST ERROR", MyApps.RESOURCE_SERVER_ADMIN, path, true, now);

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&startDate=" + startDate + "&endDate=" + endDate)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  IncidenceListDTO actual = objectMapper.readValue(response.getContentAsString(), IncidenceListDTO.class);

	  assertThat(actual.content().size()).isEqualTo(2);
   }

   @Test
   void givenGetAllErrorsByOrigin_whenEndDate_thenReturnErrorList() throws Exception {

	  // Arrange

	  // createApiError error
	  String endDate = "2025-08-03T09:37:01.924Z";
	  String cleanDate = endDate.substring(0, endDate.indexOf('T')) + "T11:00:00";
	  LocalDateTime now = LocalDateTime.parse(cleanDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	  testHelperService.createApiError("TEST", "TEST ERROR", MyApps.RESOURCE_SERVER_ADMIN, path, true, now);

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&startDate=" + "&endDate=" + endDate)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  IncidenceListDTO actual = objectMapper.readValue(response.getContentAsString(), IncidenceListDTO.class);

	  assertThat(actual.content().size()).isEqualTo(1);
   }
}
