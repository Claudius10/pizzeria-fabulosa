package org.clau.pizzeriaadminresourceserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriaadminresourceserver.MyTestConfiguration;
import org.clau.pizzeriaadminresourceserver.TestJwtHelperService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.common.SecurityResponse;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriautils.util.common.test.TestUtils.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class SecurityTests {

   private final String path = Route.API + Route.V1 + Route.ADMIN_BASE;

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

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&pageNumber=0&pageSize=5")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo("Access Denied");
	  assertThat(responseObj.getApiError().getCause()).isEqualTo("AuthorizationDeniedException");
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_ADMIN);
   }

   @Test
   void givenApiCallToResource_whenNoBearerToken_thenReturnUnauthorized() throws Exception {

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&pageNumber=0&pageSize=5")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(""))
			.with(csrf())
		 )

		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(SecurityResponse.MISSING_TOKEN);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo("InsufficientAuthenticationException");
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_ADMIN);
   }

   @Test
   void givenApiCallToResource_whenBadAuthorizationHeader_thenReturnUnauthorized() throws Exception {

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?origin=" + MyApps.RESOURCE_SERVER_ADMIN + "&pageNumber=0&pageSize=5")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", "random-value")))
		 .andReturn().getResponse();

	  // Assert

	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(SecurityResponse.INVALID_TOKEN);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo(SecurityResponse.INVALID_TOKEN);
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_ADMIN);
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
