package org.clau.pizzeriaadminresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriaadminresourceserver.MyTestConfiguration;
import org.clau.pizzeriaadminresourceserver.TestHelperService;
import org.clau.pizzeriaadminresourceserver.TestJwtHelperService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.user.RoleEnum;
import org.clau.pizzeriautils.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriautils.dto.business.NewUserOrderDTO;
import org.clau.pizzeriautils.util.business.TestUtils;
import org.clau.pizzeriautils.util.common.TimeUtils;
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

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class OrderControllerTests {

   private final String path = Route.API + Route.V1 + Route.ADMIN_BASE + Route.ORDER_BASE + Route.COUNT;

   private final NewUserOrderDTO newUserOrderDTO = TestUtils.userOrderStub(false);

   private final LocalDateTime today = TimeUtils.getNowAccountingDST();

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestJwtHelperService testJwtHelperService;

   @Autowired
   private TestHelperService testHelperService;

   @Test
   void givenHourlyRequest_thenReturnHourOrderCount() throws Exception {

	  // Arrange
	  createHourlyOrders(today, newUserOrderDTO);
	  String timeline = "hourly";
	  String state = "COMPLETED";

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(17);

	  assertThat(statisticsByState.countsByState().get(0)).isEqualTo(1); // 08:00
	  assertThat(statisticsByState.countsByState().get(1)).isEqualTo(2); // 09:00
	  assertThat(statisticsByState.countsByState().get(2)).isEqualTo(3); // 10:00
	  assertThat(statisticsByState.countsByState().get(3)).isEqualTo(4); // 11:00
	  assertThat(statisticsByState.countsByState().get(4)).isEqualTo(5); // 12:00
	  assertThat(statisticsByState.countsByState().get(5)).isEqualTo(6); // 13:00
	  assertThat(statisticsByState.countsByState().get(6)).isEqualTo(7); // 14:00
	  assertThat(statisticsByState.countsByState().get(7)).isEqualTo(8); // 15:00
	  assertThat(statisticsByState.countsByState().get(8)).isEqualTo(9); // 16:00
	  assertThat(statisticsByState.countsByState().get(9)).isEqualTo(10); // 17:00
	  assertThat(statisticsByState.countsByState().get(10)).isEqualTo(11); // 18:00
	  assertThat(statisticsByState.countsByState().get(11)).isEqualTo(12); // 19:00
	  assertThat(statisticsByState.countsByState().get(12)).isEqualTo(13); // 20:00
	  assertThat(statisticsByState.countsByState().get(13)).isEqualTo(14); // 21:00
	  assertThat(statisticsByState.countsByState().get(14)).isEqualTo(15); // 22:00
	  assertThat(statisticsByState.countsByState().get(15)).isEqualTo(16); // 23:00
	  assertThat(statisticsByState.countsByState().get(16)).isEqualTo(17); // 00:00
   }

   @Test
   void givenDailyRequest_thenReturnHourOrderCount() throws Exception {

	  // Arrange

	  createDailyOrders(today, newUserOrderDTO);
	  String timeline = "daily";
	  String state = "COMPLETED";

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(7);

	  assertThat(statisticsByState.countsByState().get(0)).isEqualTo(1); // monday
	  assertThat(statisticsByState.countsByState().get(1)).isEqualTo(2); // tuesday
	  assertThat(statisticsByState.countsByState().get(2)).isEqualTo(3); // wednesday
	  assertThat(statisticsByState.countsByState().get(3)).isEqualTo(4); // thursday
	  assertThat(statisticsByState.countsByState().get(4)).isEqualTo(5); // friday
	  assertThat(statisticsByState.countsByState().get(5)).isEqualTo(6); // saturday
	  assertThat(statisticsByState.countsByState().get(6)).isEqualTo(7); // sunday
   }

   @Test
   void givenMonthlyRequest_thenReturnHourOrderCount() throws Exception {

	  // Arrange

	  createMonthlyOrders(today, newUserOrderDTO);
	  String timeline = "monthly";
	  String state = "COMPLETED";

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(12);

	  assertThat(statisticsByState.countsByState().getFirst()).isEqualTo(1); // january
	  assertThat(statisticsByState.countsByState().get(1)).isEqualTo(2); // february
	  assertThat(statisticsByState.countsByState().get(2)).isEqualTo(3); // march
	  assertThat(statisticsByState.countsByState().get(3)).isEqualTo(4); // april
	  assertThat(statisticsByState.countsByState().get(4)).isEqualTo(5); // may
	  assertThat(statisticsByState.countsByState().get(5)).isEqualTo(6); // june
	  assertThat(statisticsByState.countsByState().get(6)).isEqualTo(7); // july
	  assertThat(statisticsByState.countsByState().get(7)).isEqualTo(8); // august
	  assertThat(statisticsByState.countsByState().get(8)).isEqualTo(9); // september
	  assertThat(statisticsByState.countsByState().get(9)).isEqualTo(10); // october
	  assertThat(statisticsByState.countsByState().get(10)).isEqualTo(11); // november
	  assertThat(statisticsByState.countsByState().get(11)).isEqualTo(12); // december
   }

   @Test
   void givenYearlyRequest_whenCurrentYearIs2025_thenReturnHourOrderCount() throws Exception {

	  // Arrange

	  createYearlyOrders(newUserOrderDTO);
	  String timeline = "yearly";
	  String state = "COMPLETED";

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act

	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert

	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(3);
	  assertThat(statisticsByState.countsByState().get(0)).isEqualTo(1); // 2023
	  assertThat(statisticsByState.countsByState().get(1)).isEqualTo(2); // 2024
	  assertThat(statisticsByState.countsByState().get(2)).isEqualTo(3); // 2025
   }

   private void createHourlyOrders(LocalDateTime today, NewUserOrderDTO newUserOrderDTO) {
	  int[] counts = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
	  LocalDateTime startOfWindow = today.toLocalDate().atTime(8, 0);
	  for (int i = 0; i < counts.length; i++) {
		 int count = counts[i];
		 LocalDateTime intervalStart = startOfWindow.plusHours(i);
		 for (int j = 0; j < count; j++) {
			int minute = getRandomMinute();
			LocalDateTime createdOn = intervalStart.withMinute(minute);
			this.testHelperService.createOrder(1L, newUserOrderDTO, createdOn);
		 }
	  }
   }

   private void createDailyOrders(LocalDateTime today, NewUserOrderDTO newUserOrderDTO) {
	  int[] counts = new int[]{1, 2, 3, 4, 5, 6, 7};
	  for (int i = 0; i < counts.length; i++) {
		 int daysAgo = 6 - i; // oldest first
		 int count = counts[i];
		 LocalDateTime base = today.minusDays(daysAgo).withHour(12).withMinute(0).withSecond(0).withNano(0);
		 for (int j = 0; j < count; j++) {
			int hour = 9 + (j % 12);
			int minute = getRandomMinute();
			LocalDateTime createdOn = base.withHour(hour).withMinute(minute);
			this.testHelperService.createOrder(1L, newUserOrderDTO, createdOn);
		 }
	  }
   }

   private void createMonthlyOrders(LocalDateTime today, NewUserOrderDTO newUserOrderDTO) {
	  int[] counts = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	  int currentYear = today.getYear();
	  for (int m = 1; m <= 12; m++) {
		 YearMonth ym = YearMonth.of(currentYear, m); // Jan..Dec of current year
		 int count = counts[m - 1];
		 for (int j = 0; j < count; j++) {
			int day = Math.min(1 + (j % ym.lengthOfMonth()), ym.lengthOfMonth());
			int hour = 10 + (j % 12); // spread during the day
			int minute = getRandomMinute();
			LocalDateTime createdOn = LocalDateTime.of(ym.getYear(), ym.getMonth(), day, hour, minute, 0);
			this.testHelperService.createOrder(1L, newUserOrderDTO, createdOn);
		 }
	  }
   }

   private void createYearlyOrders(NewUserOrderDTO newUserOrderDTO) {
	  int[] counts = new int[]{1, 2, 3}; // for 2023, 2024, 2025
	  int startYear = 2023;
	  for (int i = 0; i < counts.length; i++) {
		 int year = startYear + i;
		 int count = counts[i];
		 for (int j = 0; j < count; j++) {
			int month = 1 + (j % 12); // spread across all months
			int day = Math.min(15 + (j % 10), YearMonth.of(year, month).lengthOfMonth());
			int hour = 12 + (j % 8); // 12..19
			int minute = getRandomMinute();
			LocalDateTime createdOn = LocalDateTime.of(year, month, day, hour, minute, 0);
			this.testHelperService.createOrder(1L, newUserOrderDTO, createdOn);
		 }
	  }
   }

   @Test
   void givenInvalidState_whenRequest_thenReturnBadRequestWithMessage() throws Exception {

	  // Arrange
	  String timeline = "daily";
	  String state = "NOT_A_STATE";
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	  String body = response.getContentAsString();
	  assertThat(body).contains("Supported states");
   }

   @Test
   void givenUnknownTimeline_whenRequest_thenReturnOkWithEmptyCounts() throws Exception {

	  // Arrange
	  String timeline = "foobar";
	  String state = "COMPLETED";
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState()).isEmpty();
   }

   @Test
   void givenHourlyRequest_whenEventsOnBoundaries_thenBucketsAreInclusiveExclusiveCorrectly() throws Exception {

	  // Arrange
	  // Create 3 orders: 08:00:00 and 08:59:59.999999999 should fall into first bucket, 09:00:00 in the second
	  LocalDateTime startOfWindow = today.toLocalDate().atTime(8, 0, 0, 0);
	  this.testHelperService.createOrder(1L, newUserOrderDTO, startOfWindow);
	  this.testHelperService.createOrder(1L, newUserOrderDTO, startOfWindow.withMinute(59).withSecond(59).withNano(999_999_999));
	  this.testHelperService.createOrder(1L, newUserOrderDTO, startOfWindow.plusHours(1)); // 09:00:00

	  String timeline = "hourly";
	  String state = "COMPLETED";
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(17);
	  assertThat(statisticsByState.countsByState().get(0)).isEqualTo(2);
	  assertThat(statisticsByState.countsByState().get(1)).isEqualTo(1);
	  for (int i = 2; i < 17; i++) {
		 assertThat(statisticsByState.countsByState().get(i)).isEqualTo(0);
	  }
   }

   @Test
   void givenYearlyRequest_whenNoData_thenReturnZeroCountsForAllYears() throws Exception {

	  // Arrange: no orders created
	  String timeline = "yearly";
	  String state = "COMPLETED";
	  int startYear = 2023;
	  int currentYear = today.getYear();
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + "?timeline=" + timeline + "&state=" + state)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatisticsByState statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatisticsByState.class);
	  int expectedSize = currentYear - startYear + 1;
	  assertThat(statisticsByState.countsByState().size()).isEqualTo(expectedSize);
	  for (Integer count : statisticsByState.countsByState()) {
		 assertThat(count).isEqualTo(0);
	  }
   }

   private int getRandomMinute() {
	  Random random = new Random();
	  return random.nextInt(59);
   }
}
