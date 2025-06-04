package org.clau.pizzeriabusinessresourceserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.SecurityResponse;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessresourceserver.MyTestConfiguration;
import org.clau.pizzeriabusinessresourceserver.TestJwtHelperService;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
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

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessresourceserver.TestUtils.getResponse;
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

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

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

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(responseObj.getApiError().getMessage()).isEqualTo("Access Denied");
		assertThat(responseObj.getApiError().getCause()).isEqualTo("AuthorizationDeniedException");
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
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
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(SecurityResponse.MISSING_TOKEN);
		assertThat(responseObj.getApiError().getCause()).isEqualTo("InsufficientAuthenticationException");
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
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

		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(SecurityResponse.INVALID_TOKEN);
		assertThat(responseObj.getApiError().getCause()).isEqualTo(SecurityResponse.INVALID_TOKEN);
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
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
