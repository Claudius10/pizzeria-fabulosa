package org.clau.pizzeriaadminresourceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriaadminresourceserver.MyTestConfiguration;
import org.clau.pizzeriaadminresourceserver.OrderTestUtils;
import org.clau.pizzeriaadminresourceserver.TestHelperService;
import org.clau.pizzeriaadminresourceserver.TestJwtHelperService;
import org.clau.pizzeriadata.dto.admin.OrderStatistics;
import org.clau.pizzeriadata.dto.business.NewUserOrderDTO;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.enums.RoleEnum;
import org.clau.pizzeriautils.util.TimeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

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
@Sql(scripts = "file:src/test/resources/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, config = @SqlConfig(transactionMode = ISOLATED))
public class OrderStatisticsControllerTests {

   static Stream<Arguments> orderStateTimelinesWithByClause() {
	  return Stream.of(
		 Arguments.of("hourly", ApiRoutes.ORDER_BASE),
		 Arguments.of("daily", ApiRoutes.ORDER_BASE),
		 Arguments.of("monthly", ApiRoutes.ORDER_BASE),
		 Arguments.of("yearly", ApiRoutes.ORDER_BASE)
	  );
   }

   static Stream<Arguments> userStateTimelinesWithByClause() {
	  return Stream.of(
		 Arguments.of("hourly", ApiRoutes.USER_BASE),
		 Arguments.of("daily", ApiRoutes.USER_BASE),
		 Arguments.of("monthly", ApiRoutes.USER_BASE),
		 Arguments.of("yearly", ApiRoutes.USER_BASE)
	  );
   }

   private final String path = ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ADMIN + ApiRoutes.ORDER_BASE + ApiRoutes.STATISTICS;

   private final NewUserOrderDTO newUserOrderDTO = OrderTestUtils.userOrderStub(false);

   private final LocalDateTime today = TimeUtils.getNowAccountingDST();

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private TestJwtHelperService testJwtHelperService;

   @Autowired
   private TestHelperService testHelperService;

   @BeforeAll
   void setup() {
	  createHourlyOrders(today, newUserOrderDTO);
	  createDailyOrders(today, newUserOrderDTO);
	  createMonthlyOrders(today, newUserOrderDTO);
	  createYearlyOrders(newUserOrderDTO);
   }

   @ParameterizedTest(name = "{index} => timeline={0}")
   @MethodSource("orderStateTimelinesWithByClause")
   void givenRequest_whenOrderState_thenReturnOrderCount(String timeline, String byClause) throws Exception {

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + ApiRoutes.STATE + byClause + "?timeline=" + timeline)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatistics statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatistics.class);

	  List<Integer> countCompleted = statisticsByState.statisticsByState().get(0).count();
	  List<Integer> countCancelled = statisticsByState.statisticsByState().get(1).count();

	  List<Integer> completedList = countCompleted.stream().filter(c -> c > 0).toList();
	  assertThat(completedList).size().isPositive();

	  List<Integer> cancelledList = countCancelled.stream().filter(c -> c > 0).toList();
	  assertThat(cancelledList).size().isPositive();
   }

   @ParameterizedTest(name = "{index} => timeline={0}")
   @MethodSource("userStateTimelinesWithByClause")
   void givenRequest_whenUserState_thenReturnOrderCount(String timeline, String byClause) throws Exception {

	  // createApiError JWT token
	  String accessToken = testJwtHelperService.generateAccessToken(List.of(RoleEnum.ADMIN.value()));

	  // Act
	  MockHttpServletResponse response = mockMvc.perform(get(path + ApiRoutes.STATE + byClause + "?timeline=" + timeline)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.header("Authorization", format("Bearer %s", accessToken)))
		 .andReturn().getResponse();

	  // Assert
	  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	  OrderStatistics statisticsByState = objectMapper.readValue(response.getContentAsString(), OrderStatistics.class);

	  List<Integer> countRegisteredUsers = statisticsByState.statisticsByState().get(0).count();
	  List<Integer> countAnonymousUsers = statisticsByState.statisticsByState().get(1).count();

	  List<Integer> usersList = countRegisteredUsers.stream().filter(c -> c > 0).toList();
	  assertThat(usersList).size().isPositive();

	  List<Integer> anonymousList = countAnonymousUsers.stream().filter(c -> c > 0).toList();
	  assertThat(anonymousList).size().isPositive();
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
	  LocalDateTime mondayOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
	  for (int i = 0; i <= 6; i++) {
		 LocalDateTime base = mondayOfWeek.plusDays(i).withHour(12).withMinute(0).withSecond(0).withNano(0);
		 this.testHelperService.createOrder(1L, newUserOrderDTO, base);
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

   private int getRandomMinute() {
	  Random random = new Random();
	  return random.nextInt(59);
   }
}
