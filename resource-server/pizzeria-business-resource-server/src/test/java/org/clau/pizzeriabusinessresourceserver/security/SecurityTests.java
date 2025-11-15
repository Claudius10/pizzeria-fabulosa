package org.clau.pizzeriabusinessresourceserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriabusinessresourceserver.MyTestConfiguration;
import org.clau.pizzeriabusinessresourceserver.TestJwtHelperService;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.ApiSecurityResponses;
import org.clau.pizzeriautils.constant.MyApps;
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
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessresourceserver.TestUtil.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class SecurityTests {

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ORDER_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestJwtHelperService jwtHelper;

   @Test
   void givenApiCallToResource_whenValidAccessTokenAndInvalidScope_thenReturnUnauthorized() throws Exception {

	  // Arrange

	  // create JWT token
	  String accessToken = jwtHelper.generateAccessToken(List.of("evil"));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(""))
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo("Access Denied");
	  assertThat(responseObj.getApiError().getCause()).isEqualTo("AuthorizationDeniedException");
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_BUSINESS);
   }

   @Test
   void givenApiCallToResource_whenNoBearerToken_thenReturnUnauthorized() throws Exception {

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(""))
			.with(csrf())
		 )

		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ApiSecurityResponses.MISSING_TOKEN);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo("InsufficientAuthenticationException");
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_BUSINESS);
   }

   @Test
   void givenApiCallToResource_whenBadAuthorizationHeader_thenReturnUnauthorized() throws Exception {

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(""))
			.with(csrf())
			.header("Authorization", format("Bearer %s", "random-value")))
		 .andReturn().getResponse();

	  // Assert

	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ApiSecurityResponses.INVALID_TOKEN);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo(ApiSecurityResponses.INVALID_TOKEN);
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_BUSINESS);
   }

   @Test
   void givenEvilRequest_thenReturnBadRequest() throws Exception {

	  // Act

	  // get api call to check security
	  MockHttpServletResponse response = mockMvc.perform(get("/;")).andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(400);
   }
}
