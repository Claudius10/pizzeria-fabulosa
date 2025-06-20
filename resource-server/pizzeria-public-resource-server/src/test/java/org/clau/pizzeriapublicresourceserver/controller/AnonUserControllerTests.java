package org.clau.pizzeriapublicresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriapublicresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriapublicresourceserver.TestHelperService;
import org.clau.pizzeriapublicassets.dto.RegisterDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(MyTestcontainersConfiguration.class)
public class AnonUserControllerTests {

   private final String path = Route.API + Route.V1 + Route.ANON_BASE + Route.USER_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestHelperService testHelperService;

   @BeforeAll
   void setup() {
	  testHelperService.createRole();
   }

   @Test
   @Order(1)
   void givenRegisterApiCall_thenRegisterUser() throws Exception {

	  // Arrange

	  String email = "clau2@gmail.com";
	  RegisterDTO registerDTO = new RegisterDTO("Clau",
		 email,
		 email,
		 123456789,
		 "Password1",
		 "Password1");
	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))
		 .andExpect(status().isCreated());

	  // Assert

	  assertThat(testHelperService.findUserByEmail(email)).isNotNull();
   }

   @Test
   void givenRegisterApiCall_whenAccountWithEmailAlreadyExists_thenDontAllowRegister() throws Exception {

	  // Arrange

	  String email = "clau2@gmail.com";
	  RegisterDTO registerDTO = new RegisterDTO("Clau",
		 email,
		 email,
		 123456789,
		 "Password1",
		 "Password1");

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ResponseDTO.class);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(Response.USER_EMAIL_ALREADY_EXISTS);
   }

   @Test
   void givenRegisterPostApiCall_whenInvalidUserName_thenThrowException() throws Exception {

	  // Arrange

	  RegisterDTO registerDTO = new RegisterDTO(
		 "UserToRegi·%$ster",
		 "emailRegister@gmail.com",
		 "emailRegister@gmail.com",
		 123456789,
		 "Password1",
		 "Password1");

	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))

		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("name");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NAME_INVALID);
			}
		 );
   }

   @Test
   void givenRegisterPostApiCall_whenNonMatchingEmail_thenThrowException() throws Exception {

	  // Arrange

	  RegisterDTO registerDTO = new RegisterDTO(
		 "UserToRegister",
		 "emailRegiste2@gmail.com",
		 "emailRegister@gmail.com",
		 123456789,
		 "Password1",
		 "Password1");

	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))

		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_NO_MATCH);
			}
		 );
   }

   @Test
   void givenRegisterPostApiCall_whenInvalidEmail_thenThrowException() throws Exception {

	  // Arrange

	  RegisterDTO registerDTO = new RegisterDTO(
		 "UserToRegister",
		 "emailRegister$·%·$$gmail.com",
		 "emailRegister$·%·$$gmail.com",
		 123456789,
		 "Password1",
		 "Password1");


	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))

		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_INVALID);
			}
		 );
   }

   @Test
   void givenRegisterPostApiCall_whenNonMatchingPassword_thenThrowException() throws Exception {

	  // Arrange

	  RegisterDTO registerDTO = new RegisterDTO(
		 "UserToRegister",
		 "emailRegister@gmail.com",
		 "emailRegister@gmail.com",
		 123456789,
		 "Password1",
		 "Password12");

	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))

		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_NO_MATCH);
			}
		 );
   }

   @Test
   void givenRegisterPostApiCall_whenInvalidPassword_thenThrowException() throws Exception {

	  // Arrange

	  RegisterDTO registerDTO = new RegisterDTO(
		 "UserToRegister",
		 "emailRegister@gmail.com",
		 "emailRegister@gmail.com",
		 123456789,
		 "Password",
		 "Password");

	  // Act

	  mockMvc.perform(post(path + Route.ANON_REGISTER)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerDTO)))

		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_INVALID);
			}
		 );
   }
}
