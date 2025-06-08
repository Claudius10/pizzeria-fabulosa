package org.clau.pizzeriabusinessresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.CartItemDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.MyTestConfiguration;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessassets.util.TestUtils.anonOrderStub;
import static org.clau.pizzeriabusinessresourceserver.TestUtils.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
public class AnonOrderControllerTests {

	private final String path = Route.API + Route.V1 + Route.ANON_BASE + Route.ORDER_BASE;

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
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(expected)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		Order actual = objectMapper.readValue(response.getContentAsString(), Order.class);

		assertThat(actual.getAnonCustomerName()).isEqualTo(expected.customer().anonCustomerName());
		assertThat(actual.getAnonCustomerContactNumber()).isEqualTo(expected.customer().anonCustomerContactNumber());
		assertThat(actual.getAnonCustomerEmail()).isEqualTo(expected.customer().anonCustomerEmail());

		assertThat(actual.getAddress()).isEqualTo(expected.address());

		assertThat(actual.getOrderDetails().getPaymentMethod()).isEqualTo(expected.orderDetails().paymentMethod());
		assertThat(actual.getOrderDetails().getDeliveryTime()).isEqualTo(expected.orderDetails().deliveryTime());
		assertThat(actual.getOrderDetails().getStorePickUp()).isFalse();
		assertThat(actual.getOrderDetails().getBillToChange()).isEqualTo(expected.orderDetails().billToChange());
		assertThat(actual.getOrderDetails().getChangeToGive()).isEqualTo(expected.orderDetails().billToChange() - expected.cart().totalCost());
		assertThat(actual.getOrderDetails().getComment()).isNull();

		assertThat(actual.getCart().getTotalQuantity()).isEqualTo(expected.cart().totalQuantity());
		assertThat(actual.getCart().getTotalCost()).isEqualTo(expected.cart().totalCost());
		assertThat(actual.getCart().getTotalCostOffers()).isZero();

		assertThat(actual.getCart().getCartItems().size()).isEqualTo(expected.cart().cartItems().size());

		CartItem actualItem = actual.getCart().getCartItems().getFirst();
		CartItemDTO expectedItem = expected.cart().cartItems().getFirst();

		assertThat(actualItem.getName()).isEqualTo(expectedItem.name());
		assertThat(actualItem.getDescription()).isEqualTo(expectedItem.description());
		assertThat(actualItem.getFormats()).isEqualTo(expectedItem.formats());
		assertThat(actualItem.getType()).isEqualTo(expectedItem.type());
		assertThat(actualItem.getQuantity()).isEqualTo(expectedItem.quantity());
		assertThat(actualItem.getPrice()).isEqualTo(expectedItem.price());
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidCustomerName_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
		mockMvc.perform(post(path)
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"·$dfsaf3",
								111222333,
								"customerEmail@gmail.com",
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
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("customer.anonCustomerName");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NAME_INVALID);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidCustomerNumber_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
		mockMvc.perform(post(path)
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								1,
								"customerEmail@gmail.com",
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
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("customer.anonCustomerContactNumber");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NUMBER_INVALID);
						}
				);
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
	void givenAnonOrderPostApiCall_whenInvalidAddressStreet_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
		mockMvc.perform(post(path)
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"%$·%·",
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
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_INVALID);
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
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_BILL);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidComment_thenThrowException() throws Exception {
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
								"Cash",
								"%$·%·$",
								false))))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.comment");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_COMMENT);
						}
				);
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
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
		assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
	}
}
