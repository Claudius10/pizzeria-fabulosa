package org.clau.pizzeriabusinessresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.util.SecurityCookies;
import org.clau.pizzeriabusinessassets.dto.CartItemDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.dto.OrderSummaryListDTO;
import org.clau.pizzeriabusinessassets.model.CartItem;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.validation.ValidationResponses;
import org.clau.pizzeriabusinessresourceserver.MyTestcontainersConfiguration;
import org.clau.pizzeriabusinessresourceserver.TestHelperService;
import org.clau.pizzeriabusinessresourceserver.TestJwtHelperService;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderProjection;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.clau.apiutils.util.SecurityCookies.ACCESS_TOKEN;
import static org.clau.pizzeriabusinessassets.util.TestUtils.userOrderStub;
import static org.clau.pizzeriabusinessresourceserver.TestUtils.getResponse;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestcontainersConfiguration.class)
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class OrderControllerTests {

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TestHelperService testHelperService;

	@Autowired
	private TestJwtHelperService testJwtHelperService;

	@Test
	void givenPostApiCallToCreateOrder_thenReturnCreatedAndDTO() throws Exception {

		// Arrange

		Long userId = 1L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		boolean emptyCart = false;
		NewUserOrderDTO expected = userOrderStub(emptyCart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(expected))
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 60, true, false)))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		Order actual = objectMapper.readValue(response.getContentAsString(), Order.class);

		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getUserId()).isEqualTo(userId);

		assertThat(actual.getAnonCustomerName()).isNull();
		assertThat(actual.getAnonCustomerEmail()).isNull();
		assertThat(actual.getAnonCustomerContactNumber()).isNull();

		assertThat(actual.getAddress()).isEqualTo(expected.address());

		assertThat(actual.getOrderDetails().getDeliveryTime()).isEqualTo(expected.orderDetails().deliveryTime());
		assertThat(actual.getOrderDetails().getPaymentMethod()).isEqualTo(expected.orderDetails().paymentMethod());
		assertThat(actual.getOrderDetails().getStorePickUp()).isEqualTo(expected.orderDetails().storePickUp());
		assertThat(actual.getOrderDetails().getComment()).isNull();
		assertThat(actual.getOrderDetails().getBillToChange()).isEqualTo(expected.orderDetails().billToChange());
		assertThat(actual.getOrderDetails().getChangeToGive()).isEqualTo(expected.orderDetails().billToChange() - expected.cart().totalCost());

		assertThat(actual.getCart().getTotalCost()).isEqualTo(expected.cart().totalCost());
		assertThat(actual.getCart().getTotalCostOffers()).isEqualTo(expected.cart().totalCostOffers());
		assertThat(actual.getCart().getTotalQuantity()).isEqualTo(expected.cart().totalQuantity());

		assertThat(actual.getCart().getCartItems().size()).isEqualTo(expected.cart().cartItemsDTO().size());

		CartItem actualItem = actual.getCart().getCartItems().getFirst();
		CartItemDTO expectedItem = expected.cart().cartItemsDTO().getFirst();
		assertThat(actualItem.getName()).isEqualTo(expectedItem.name());
		assertThat(actualItem.getDescription()).isEqualTo(expectedItem.description());
		assertThat(actualItem.getFormats()).isEqualTo(expectedItem.formats());
		assertThat(actualItem.getType()).isEqualTo(expectedItem.type());
		assertThat(actualItem.getQuantity()).isEqualTo(expectedItem.quantity());
		assertThat(actualItem.getPrice()).isEqualTo(expectedItem.price());
	}

	@Test
	void givenPostApiCallToCreateOrder_whenCartIsEmpty_thenReturnBadRequestWithMessage() throws Exception {

		// Arrange

		Long userId = 1L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		boolean emptyCart = true;
		NewUserOrderDTO expected = userOrderStub(emptyCart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(expected))
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 60, true, false)))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
		assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
	}

	@Test
	void givenGetApiCallToFindOrder_thenReturnOrder() throws Exception {

		// Arrange

		Long userId = 1L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		boolean emptyCart = false;
		NewUserOrderDTO expected = userOrderStub(emptyCart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post(path + "?userId=" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(expected))
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 60, true, false)))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

		CreatedOrderDTO createdOrderDTO = objectMapper.readValue(response.getContentAsString(), CreatedOrderDTO.class);

		// Act

		// get api call to find user order
		MockHttpServletResponse getResponse = mockMvc.perform(get(path + Route.ORDER_ID, createdOrderDTO.id())
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
		OrderProjection actual = objectMapper.readValue(getResponse.getContentAsString(), OrderProjection.class);

		assertThat(actual.getId()).isNotNull();

		assertThat(actual.getAddress()).isEqualTo(expected.address());

		assertThat(actual.getOrderDetails().getDeliveryTime()).isEqualTo(expected.orderDetails().deliveryTime());
		assertThat(actual.getOrderDetails().getPaymentMethod()).isEqualTo(expected.orderDetails().paymentMethod());
		assertThat(actual.getOrderDetails().getStorePickUp()).isEqualTo(expected.orderDetails().storePickUp());
		assertThat(actual.getOrderDetails().getComment()).isNull();
		assertThat(actual.getOrderDetails().getBillToChange()).isEqualTo(expected.orderDetails().billToChange());
		assertThat(actual.getOrderDetails().getChangeToGive()).isEqualTo(expected.orderDetails().billToChange() - expected.cart().totalCost());

		assertThat(actual.getCart().getTotalCost()).isEqualTo(expected.cart().totalCost());
		assertThat(actual.getCart().getTotalCostOffers()).isEqualTo(expected.cart().totalCostOffers());
		assertThat(actual.getCart().getTotalQuantity()).isEqualTo(expected.cart().totalQuantity());

		assertThat(actual.getCart().getCartItems().size()).isEqualTo(expected.cart().cartItemsDTO().size());

		CartItem actualItem = actual.getCart().getCartItems().getFirst();
		CartItemDTO expectedItem = expected.cart().cartItemsDTO().getFirst();
		assertThat(actualItem.getName()).isEqualTo(expectedItem.name());
		assertThat(actualItem.getDescription()).isEqualTo(expectedItem.description());
		assertThat(actualItem.getFormats()).isEqualTo(expectedItem.formats());
		assertThat(actualItem.getType()).isEqualTo(expectedItem.type());
		assertThat(actualItem.getQuantity()).isEqualTo(expectedItem.quantity());
		assertThat(actualItem.getPrice()).isEqualTo(expectedItem.price());
	}

	@Test
	void givenGetApiCallToFindOrder_whenOrderNotFound_thenReturnNoContent() throws Exception {

		// Arrange

		Long orderId = 45678L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		// Act

		// get api call to find user order
		MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_ID, orderId)
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void givenOrderDelete_whenWithinTimeLimit_thenReturnDeletedOrderId() throws Exception {

		// Arrange

		Long userId = 1L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		// create user order
		int minutesInThePast = 0;
		OrderProjection order = testHelperService.createOrder(userId, minutesInThePast);

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, order.getId())
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
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
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		// create user order
		int minutesInThePast = 21;
		OrderProjection order = testHelperService.createOrder(userId, minutesInThePast);

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, order.getId())
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		ResponseDTO responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseObj.getApiError().getMessage()).isEqualTo(ValidationResponses.ORDER_DELETE_TIME_ERROR);
		assertThat(responseObj.getApiError().getCause()).isEqualTo(ValidationResponses.ORDER_VALIDATION_FAILED);
		assertThat(responseObj.getApiError().getOrigin()).isEqualTo(Constant.APP_NAME);
	}

	@Test
	void givenOrderDelete_whenOrderNotFound_thenReturnNoContent() throws Exception {

		// Arrange

		Long orderId = 5437L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete(path + Route.ORDER_ID, orderId)
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
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
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		// create user order
		int minutesInThePast = 0;
		OrderProjection expcted = testHelperService.createOrder(userId, minutesInThePast);

		int pageSize = 5;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		// can't directly deserialize the Page/PageImpl or Slice/SliceImpl, so got to use the DTO
		OrderSummaryListDTO actual = objectMapper.readValue(response.getContentAsString(), OrderSummaryListDTO.class);
		assertThat(actual.size()).isEqualTo(5);
		assertThat(actual.number()).isEqualTo(0);
		assertThat(actual.totalElements()).isEqualTo(1);
		assertThat(actual.last()).isEqualTo(true);

		assertThat(actual.content().getFirst().paymentMethod()).isEqualTo(expcted.getOrderDetails().getPaymentMethod());
		assertThat(actual.content().getFirst().quantity()).isEqualTo(expcted.getCart().getTotalQuantity());
		assertThat(actual.content().getFirst().cost()).isEqualTo(expcted.getCart().getTotalCost());
		assertThat(actual.content().getFirst().costAfterOffers()).isEqualTo(expcted.getCart().getTotalCostOffers());
	}

	@Test
	void givenGetUserOrderSummary_whenNoOrders_thenReturnEmptyOrderSummaryList() throws Exception {

		// Arrange

		Long userId = 1L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		int pageSize = 1;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//		Response responseObj = getResponse(response, objectMapper);
//		OrderSummaryListDTO orderList = objectMapper.convertValue(responseObj.getPayload(), OrderSummaryListDTO.class);
//		assertThat(orderList).isNotNull();
//		assertThat(orderList.orderList().size()).isZero();
//		assertThat(orderList.totalElements()).isZero();
	}

	@Test
	void givenGetUserOrderSummary_whenUserNotFound_thenReturnNoContent() throws Exception {

		// Arrange

		Long userId = 985643L;

		// create JWT token
		String accessToken = testJwtHelperService.generateAccessToken(List.of("order"));

		int pageSize = 1;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get(path + Route.ORDER_SUMMARY + "?pageNumber={pN}&pageSize={pS}&userId={userId}", pageNumber, pageSize, userId)
						.cookie(SecurityCookies.prepareCookie(ACCESS_TOKEN, accessToken, 30, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
