package org.clau.pizzeriauserresourceserver.controller;

import org.clau.apiutils.constant.Route;
import org.clau.pizzeriauserresourceserver.MyTestConfiguration;
import org.clau.pizzeriauserresourceserver.TestHelperService;
import org.clau.pizzeriauserresourceserver.TestJwtHelperService;
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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
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

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestHelperService testHelperService;

	@Autowired
	private TestJwtHelperService jwtHelper;

	@Test
	void givenDeleteUserApiCall_thenDeleteUser() throws Exception {

		// Arrange

		String email = "Tester3@gmail.com";
		Long userId = testHelperService.createUser(email);
		assertThat(testHelperService.findUserByEmail(email)).isNotNull();

		// create JWT token
		String accessToken = jwtHelper.generateAccessToken(List.of("user"));

		// Act

		// put api call to delete the user
		MockHttpServletResponse response = mockMvc.perform(delete(path + Route.USER_ID, userId)
						.with(csrf())
						.header("Authorization", format("Bearer %s", accessToken))
				)
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(testHelperService.findUserByEmail(email)).isNull();
	}
}
