package org.clau.pizzeriasecurityserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriasecurityserver.MyTestConfiguration;
import org.clau.pizzeriasecurityserver.TestHelperService;
import org.clau.pizzeriasecurityserver.TestJwtHelperService;
import org.clau.pizzeriasecurityserver.TestUtil;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class UserControllerTests {

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private TestHelperService testHelperService;

   @Autowired
   private TestJwtHelperService jwtHelper;

   @Autowired
   private ObjectMapper objectMapper;

   @Test
   void givenDeleteUserApiCall_whenPasswordMatches_thenDeleteUser() throws Exception {

	  // Arrange

	  String email = "Tester@example.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(RoleEnum.USER.value()));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + ApiRoutes.USER_ID + "?password=password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  assertThat(testHelperService.findUserByEmail(email)).isNull();
   }

   @Test
   void givenDeleteUserApiCall_whenPasswordDoesNotMatch_thenReturnUnauthorized() throws Exception {

	  // Arrange

	  String email = "Tester@example.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(RoleEnum.USER.value()));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + ApiRoutes.USER_ID + "?password=wrong-password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  ResponseDTO responseObj = TestUtil.getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ApiResponseMessages.BAD_CREDENTIALS);
   }

   @Test
   void givenDeleteUserApiCall_whenUserIsDummyUser_thenReturnInternalServerError() throws Exception {

	  // Arrange

	  String email = "donQuijote@example.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(RoleEnum.USER.value()));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + ApiRoutes.USER_ID + "?password=password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = TestUtil.getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ApiResponseMessages.DUMMY_ACCOUNT_ERROR);
   }

   @Test
   void givenCheckPasswordMatch_whenMatch_thenReturnOk() throws Exception {

	  // Arrange

	  String email = "Tester@example.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(RoleEnum.USER.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + ApiRoutes.USER_ID + ApiRoutes.CHECK + "/password?password=password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();


	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
   }

   @Test
   void givenCheckPasswordMatch_whenNotMatch_thenReturnUnauthorized() throws Exception {

	  // Arrange

	  String email = "Tester@example.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(RoleEnum.USER.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + ApiRoutes.USER_ID + ApiRoutes.CHECK + "/password?password=passworsadad", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();


	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  ResponseDTO responseObj = TestUtil.getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ApiResponseMessages.BAD_CREDENTIALS);
   }
}
