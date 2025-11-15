package org.clau.fabulosa.pizzeria.businessresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.fabulosa.pizzeria.businessresourceserver.MyTestConfiguration;
import org.clau.fabulosa.data.dto.business.CartItemDTO;
import org.clau.fabulosa.data.dto.business.CreatedOrderDTO;
import org.clau.fabulosa.data.dto.business.NewAnonOrderDTO;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.clau.fabulosa.utils.constant.MyApps;
import org.clau.fabulosa.utils.constant.ValidationResponses;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.fabulosa.pizzeria.businessresourceserver.OrderTestUtils.anonOrderStub;
import static org.clau.fabulosa.pizzeria.businessresourceserver.TestUtil.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class AnonOrderControllerTests {

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ANON + ApiRoutes.ORDER_BASE;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Test
   void givenAnonOrderPostApiCall_whenAllOk_thenReturnOrder() throws Exception {

	  // Arrange

	  NewAnonOrderDTO expected = anonOrderStub(
		 "customerName",
		 111222333,
		 "customerEmail@gmail.com",
		 "Street",
		 5,
		 "15",
		 "E",
		 20D,
		 "ASAP",
		 "Cash",
		 null,
		 false);

	  // Act

	  // post api call to create anon actual
	  MockHttpServletResponse response = mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(expected)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	  CreatedOrderDTO actual = objectMapper.readValue(response.getContentAsString(), CreatedOrderDTO.class);

	  assertThat(actual.id()).isNotZero();

	  assertThat(actual.formattedCreatedOn()).isNotNull();

	  assertThat(actual.customer()).isEqualTo(expected.customer());

	  assertThat(actual.address()).isEqualTo(expected.address());

	  assertThat(actual.orderDetails().paymentMethod()).isEqualTo(expected.orderDetails().paymentMethod());
	  assertThat(actual.orderDetails().deliveryTime()).isEqualTo(expected.orderDetails().deliveryTime());
	  assertThat(actual.orderDetails().comment()).isEqualTo(expected.orderDetails().comment());
	  assertThat(actual.orderDetails().billToChange()).isEqualTo(expected.orderDetails().billToChange());
	  assertThat(actual.orderDetails().storePickUp()).isEqualTo(expected.orderDetails().storePickUp());
	  assertThat(actual.orderDetails().changeToGive()).isNotZero();

	  assertThat(actual.cart().totalCost()).isEqualTo(expected.cart().totalCost());
	  assertThat(actual.cart().totalQuantity()).isEqualTo(expected.cart().totalQuantity());
	  assertThat(actual.cart().totalCostOffers()).isEqualTo(expected.cart().totalCostOffers());

	  List<CartItemDTO> actualItems = actual.cart()
		 .cartItems()
		 .stream()
		 .map(item -> new CartItemDTO(
			null,
			item.type(),
			item.price(),
			item.quantity(),
			item.name(),
			item.description(),
			item.formats()
		 ))
		 .toList();

	  assertThat(actualItems).isEqualTo(expected.cart().cartItems());
   }

   @Test
   void givenAnonOrderPostApiCall_whenInvalidCustomerEmail_thenThrowException() throws Exception {
	  // Act

	  // post api call to create anon order
	  mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(anonOrderStub(
			   "customerName",
			   111222333,
			   "dsajn$·~#",
			   "Street",
			   5,
			   "Floor",
			   "Door",
			   null,
			   "ASAP",
			   "Cash",
			   null,
			   false))))
		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("customer.anonCustomerEmail");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_INVALID);
			}
		 );
   }

   @Test
   void givenAnonOrderPostApiCall_whenInvalidDeliveryHour_thenThrowException() throws Exception {
	  // Act

	  // post api call to create anon order
	  mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(anonOrderStub(
			   "customerName",
			   111222333,
			   "anonCustomerEmail@gmail.com",
			   "Street",
			   14,
			   "Floor",
			   "Door",
			   null,
			   null,
			   "Cash",
			   null,
			   false))))
		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.deliveryTime");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR);
			}
		 );
   }

   @Test
   void givenAnonOrderPostApiCall_whenInvalidPaymentType_thenThrowException() throws Exception {
	  // Act

	  // post api call to create anon order
	  mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(anonOrderStub(
			   "customerName",
			   111222333,
			   "anonCustomerEmail@gmail.com",
			   "Street",
			   14,
			   "Floor",
			   "Door",
			   null,
			   "ASAP",
			   null,
			   null,
			   false))))
		 // Assert

		 .andExpect(result -> {
			   assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			   MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
			   assert exception != null;
			   List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.paymentMethod");
			   assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_PAYMENT);
			}
		 );
   }

   @Test
   void givenAnonOrderPostApiCall_whenInvalidChangeRequest_thenThrowException() throws Exception {
	  // Act

	  // post api call to create anon order
	  MockHttpServletResponse response = mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(anonOrderStub(
			   "customerName",
			   111222333,
			   "anonCustomerEmail@gmail.com",
			   "Street",
			   14,
			   "Floor",
			   "Door",
			   10D,
			   "ASAP",
			   "Cash",
			   null,
			   false))))
		 .andReturn()
		 .getResponse();


	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_BILL);
   }

   @Test
   void givenAnonOrderPostApiCall_whenCartIsEmpty_thenThrowException() throws Exception {
	  // Act

	  // post api call to create anon order
	  MockHttpServletResponse response = mockMvc.perform(post(path)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(anonOrderStub(
			   "customerName",
			   111222333,
			   "anonCustomerEmail@gmail.com",
			   "Street",
			   14,
			   "Floor",
			   "Door",
			   null,
			   "ASAP",
			   "Cash",
			   null,
			   true))))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = getResponse(response.getContentAsString(StandardCharsets.UTF_8), objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(MyApps.RESOURCE_SERVER_PUBLIC);
   }
}
