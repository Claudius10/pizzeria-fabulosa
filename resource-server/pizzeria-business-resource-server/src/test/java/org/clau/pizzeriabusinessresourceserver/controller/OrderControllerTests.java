package org.clau.pizzeriabusinessresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.*;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.MyTestConfiguration;
import org.clau.pizzeriabusinessresourceserver.TestHelperService;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.pizzeriabusinessassets.util.TestUtils.userOrderStub;
import static org.clau.pizzeriabusinessresourceserver.TestUtils.getResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class OrderControllerTests {

   private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

   private final String userRole = "USER";

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestHelperService testHelperService;

   @Autowired
   private TestJwtHelperService testJwtHelperService;

   @Test
   void givenPostApiCallToCreateOrder_thenReturnCreatedOrder() throws Exception {

	  // Arrange

	  long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  boolean emptyCart = false;
	  NewUserOrderDTO expected = userOrderStub(emptyCart);

	  // Act

	  // post api call to create user order
	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(expected))
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken))
		 )
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	  CreatedOrderDTO actual = objectMapper.readValue(response.getContentAsString(), CreatedOrderDTO.class);

	  assertThat(actual.id()).isNotZero();

	  assertThat(actual.formattedCreatedOn()).isNotNull();

	  assertThat(actual.customer()).isNull();

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

	  assertThat(actual.cart().cartItems().size()).isEqualTo(expected.cart().cartItems().size());

	  CartItemDTO actualItem = new CartItemDTO(
		 null,
		 actual.cart().cartItems().getFirst().type(),
		 actual.cart().cartItems().getFirst().price(),
		 actual.cart().cartItems().getFirst().quantity(),
		 actual.cart().cartItems().getFirst().name(),
		 actual.cart().cartItems().getFirst().description(),
		 actual.cart().cartItems().getFirst().formats()
	  );

	  CartItemDTO expectedItem = expected.cart().cartItems().getFirst();

	  assertThat(actualItem).isEqualTo(expectedItem);
   }

   @Test
   void givenPostApiCallToCreateOrder_whenCartIsEmpty_thenReturnBadRequestWithMessage() throws Exception {

	  // Arrange

	  long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  boolean emptyCart = true;
	  NewUserOrderDTO expected = userOrderStub(emptyCart);

	  // Act

	  // post api call to create user order
	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(expected))
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = getResponse(response, objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
   }

   @Test
   void givenGetApiCallToFindOrder_thenReturnOrder() throws Exception {

	  // Arrange

	  long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  boolean emptyCart = false;
	  NewUserOrderDTO expected = userOrderStub(emptyCart);

	  // post api call to create user order
	  MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(expected))
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

	  Long orderId = objectMapper.readValue(response.getContentAsString(), Order.class).getId();

	  // Act

	  // get api call to find user order
	  MockHttpServletResponse getResponse = mockMvc.perform(get(path + Route.ORDER_ID, orderId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

	  OrderDTO actual = objectMapper.readValue(getResponse.getContentAsString(), OrderDTO.class);

	  assertThat(actual.id()).isNotZero();
	  assertThat(actual.formattedCreatedOn()).isNotNull();
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

	  CartItemDTO actualItem = new CartItemDTO(
		 null,
		 actual.cart().cartItems().getFirst().type(),
		 actual.cart().cartItems().getFirst().price(),
		 actual.cart().cartItems().getFirst().quantity(),
		 actual.cart().cartItems().getFirst().name(),
		 actual.cart().cartItems().getFirst().description(),
		 actual.cart().cartItems().getFirst().formats()
	  );

	  CartItemDTO expectedItem = expected.cart().cartItems().getFirst();

	  assertThat(actualItem).isEqualTo(expectedItem);
   }

   @Test
   void givenGetApiCallToFindOrder_whenOrderNotFound_thenReturnNoContent() throws Exception {

	  // Arrange

	  Long orderId = 45678L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  // Act

	  // get api call to find user order
	  MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_ID, orderId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
   }

   @Test
   void givenOrderDelete_whenWithinTimeLimit_thenReturnDeletedOrderId() throws Exception {

	  // Arrange

	  Long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  // create user order
	  int minutesInThePast = 0;
	  Order order = testHelperService.createOrder(userId, minutesInThePast);

	  // Act

	  // delete api call to delete order
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, order.getId())
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  Long id = objectMapper.readValue(response.getContentAsString(), Long.class);
	  assertThat(id).isEqualTo(order.getId());
   }

   @Test
   void givenOrderDelete_whenTimeLimitPassed_thenReturnBadRequestWithMessage() throws Exception {

	  // Arrange

	  Long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  // create user order
	  int minutesInThePast = 21;
	  Order order = testHelperService.createOrder(userId, minutesInThePast);

	  // Act

	  // delete api call to delete order
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, order.getId())
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  ResponseDTO responseObj = getResponse(response, objectMapper);
	  assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.ORDER_DELETE_TIME_ERROR);
	  assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
	  assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
   }

   @Test
   void givenOrderDelete_whenOrderNotFound_thenReturnNoContent() throws Exception {

	  // Arrange

	  Long orderId = 5437L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  // Act

	  // delete api call to delete order
	  MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, orderId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
   }

   @Test
   void givenGetUserOrderSummary_thenReturnUserOrderSummary() throws Exception {

	  // Arrange

	  Long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  // create user order
	  int minutesInThePast = 0;
	  Order expectedOrder = testHelperService.createOrder(userId, minutesInThePast);

	  int pageSize = 5;
	  int pageNumber = 0;

	  // Act

	  // get api call to get OrderSummary
	  MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	  OrderSummaryListDTO actual = objectMapper.readValue(response.getContentAsString(), OrderSummaryListDTO.class);

	  assertThat(actual.size()).isEqualTo(pageSize);
	  assertThat(actual.number()).isEqualTo(pageNumber);
	  assertThat(actual.totalElements()).isEqualTo(1);
	  assertThat(actual.last()).isTrue();

	  OrderSummaryDTO actualOrder = actual.content().getFirst();

	  assertThat(actualOrder.id()).isEqualTo(expectedOrder.getId());
	  assertThat(actualOrder.formattedCreatedOn()).isEqualTo(expectedOrder.getFormattedCreatedOn());
	  assertThat(actualOrder.paymentMethod()).isEqualTo(expectedOrder.getOrderDetails().getPaymentMethod());
	  assertThat(actualOrder.quantity()).isEqualTo(expectedOrder.getCart().getTotalQuantity());
	  assertThat(actualOrder.cost()).isEqualTo(expectedOrder.getCart().getTotalCost());
	  assertThat(actualOrder.costAfterOffers()).isEqualTo(expectedOrder.getCart().getTotalCostOffers());
   }

   @Test
   void givenGetUserOrderSummary_whenNoOrders_thenReturnEmptySummary() throws Exception {

	  // Arrange

	  Long userId = 1L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  int pageSize = 1;
	  int pageNumber = 0;

	  // Act

	  // get api call to get OrderSummary
	  MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderSummaryListDTO actual = objectMapper.readValue(response.getContentAsString(), OrderSummaryListDTO.class);

	  assertThat(actual.size()).isEqualTo(pageSize);
	  assertThat(actual.number()).isEqualTo(pageNumber);
	  assertThat(actual.totalElements()).isZero();
	  assertThat(actual.last()).isTrue();
	  assertThat(actual.content()).isEmpty();
   }

   @Test
   void givenGetUserOrderSummary_whenUserNotFound_thenReturnEmptySummary() throws Exception {

	  // Arrange

	  Long userId = 985643L;

	  // create JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(userRole));

	  int pageSize = 3;
	  int pageNumber = 0;

	  // Act

	  // get api call to get OrderSummary
	  MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn()
		 .getResponse();

	  // Assert

	  OrderSummaryListDTO actual = objectMapper.readValue(response.getContentAsString(), OrderSummaryListDTO.class);

	  assertThat(actual.size()).isEqualTo(pageSize);
	  assertThat(actual.number()).isEqualTo(pageNumber);
	  assertThat(actual.totalElements()).isZero();
	  assertThat(actual.last()).isTrue();
	  assertThat(actual.content()).isEmpty();
   }
}
