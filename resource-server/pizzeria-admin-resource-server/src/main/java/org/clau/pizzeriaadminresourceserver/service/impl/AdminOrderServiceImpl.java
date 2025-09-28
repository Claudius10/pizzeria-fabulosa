package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.service.AdminOrderService;
import org.clau.pizzeriadata.dao.admin.AdminOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderServiceImpl implements AdminOrderService {

   private final AdminOrderRepository adminOrderRepository;

   @Override
   public List<Integer> findCountForTimeline(String value) {
	  if (value == null) return Collections.emptyList();

	  LocalDate today = LocalDate.now();

	  switch (value) {
		 case "hourly": {
			List<Integer> result = new ArrayList<>(12);
			LocalDateTime start = today.atTime(12, 0);
			for (int i = 0; i < 12; i++) {
			   LocalDateTime intervalStart = start.plusHours(i);
			   LocalDateTime intervalEnd = intervalStart.plusHours(1).minusNanos(1);
			   int count = adminOrderRepository.countAllByCreatedOnBetween(intervalStart, intervalEnd);
			   result.add(count);
			}
			return result;
		 }
		 case "daily": {
			List<Integer> result = new ArrayList<>(7);
			for (int i = 6; i >= 0; i--) {
			   LocalDate day = today.minusDays(i);
			   LocalDateTime dayStart = day.atStartOfDay();
			   LocalDateTime dayEnd = day.atTime(23, 59, 59, 999_999_999);
			   int count = adminOrderRepository.countAllByCreatedOnBetween(dayStart, dayEnd);
			   result.add(count);
			}
			return result;
		 }
		 case "monthly": {
			List<Integer> result = new ArrayList<>(12);
			YearMonth currentMonth = YearMonth.from(today);
			for (int i = 11; i >= 0; i--) {
			   YearMonth ym = currentMonth.minusMonths(i);
			   LocalDateTime monthStart = ym.atDay(1).atStartOfDay();
			   LocalDateTime monthEnd = ym.atEndOfMonth().atTime(23, 59, 59, 999_999_999);
			   int count = adminOrderRepository.countAllByCreatedOnBetween(monthStart, monthEnd);
			   result.add(count);
			}
			return result;
		 }
		 case "yearly": {
			int startYear = 2023;
			int currentYear = today.getYear();
			int years = currentYear - startYear;
			List<Integer> result = new ArrayList<>(years + 1);

			for (int y = startYear; y <= currentYear; y++) {
			   Year year = Year.of(y);
			   LocalDateTime yearStart = year.atDay(1).atStartOfDay();
			   LocalDateTime yearEnd = year.atMonth(12).atEndOfMonth().atTime(23, 59, 59, 999_999_999);
			   int count = adminOrderRepository.countAllByCreatedOnBetween(yearStart, yearEnd);
			   result.add(count);
			}

			return result;
		 }
		 default:
			return Collections.emptyList();
	  }
   }
}
