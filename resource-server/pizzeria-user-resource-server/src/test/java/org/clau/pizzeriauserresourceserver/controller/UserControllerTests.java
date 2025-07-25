package org.clau.pizzeriauserresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriauserresourceserver.MyTestConfiguration;
import org.clau.pizzeriauserresourceserver.TestHelperService;
import org.clau.pizzeriauserresourceserver.TestJwtHelperService;
import org.clau.pizzeriauserresourceserver.util.Constant;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriautils.util.common.TestUtils.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class UserControllerTests {

   private final String path = Route.API + Route.V1 + Route.USER_BASE;

   private final String userRole = "USER";

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

	  String email = "Tester3@gmail.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(userRole));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.USER_ID + "?password=password", userId)
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

	  String email = "Tester3@gmail.com";
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(userRole));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.USER_ID + "?password=wrong-password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(Response.BAD_CREDENTIALS);
   }

   @Test
   void givenDeleteUserApiCall_whenUserIsDummyUser_thenReturnInternalServerError() throws Exception {

	  // Arrange

	  String email = Constant.DUMMY_ACCOUNT_EMAIL;
	  Long userId = testHelperService.createUser(email);
	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of(userRole));

	  // Act

	  // put api call to delete the user
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.USER_ID + "?password=password", userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(Response.DUMMY_ACCOUNT_ERROR);
   }
}
