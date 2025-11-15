package org.clau.pizzeriaadminresourceserver.data.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

// https://www.baeldung.com/java-strategy-pattern
// https://refactoring.guru/design-patterns/strategy

public enum Timeline {
   HOURLY {
	  @Override
	  public List<Interval> intervals(LocalDate today, LocalDateTime now) {
		 List<Interval> result = new ArrayList<>(17);
		 LocalDateTime startOfWindow = now.toLocalDate().atTime(8, 0);
		 for (int h = 0; h < 17; h++) {
			LocalDateTime intervalStart = startOfWindow.plusHours(h);
			LocalDateTime intervalEnd = intervalStart.plusHours(1).minusNanos(1);
			result.add(new Interval(intervalStart, intervalEnd));
		 }
		 return result;
	  }
   },
   DAILY {
	  @Override
	  public List<Interval> intervals(LocalDate today, LocalDateTime now) {
		 List<Interval> result = new ArrayList<>(7);
		 LocalDate monday = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
		 for (int i = 0; i <= 6; i++) {
			LocalDate d = monday.plusDays(i);
			result.add(new Interval(
			   d.atStartOfDay(),
			   d.atTime(23, 59, 59, 999_999_999)
			));
		 }
		 return result;
	  }
   },
   MONTHLY {
	  @Override
	  public List<Interval> intervals(LocalDate today, LocalDateTime now) {
		 List<Interval> result = new ArrayList<>(12);
		 int currentYear = today.getYear();
		 for (int m = 1; m <= 12; m++) {
			YearMonth ym = YearMonth.of(currentYear, m);
			result.add(new Interval(
			   ym.atDay(1).atStartOfDay(),
			   ym.atEndOfMonth().atTime(23, 59, 59, 999_999_999)
			));
		 }
		 return result;
	  }
   },
   YEARLY {
	  @Override
	  public List<Interval> intervals(LocalDate today, LocalDateTime now) {
		 int startYear = 2023;
		 int currentYear = today.getYear();
		 List<Interval> result = new ArrayList<>();
		 for (int y = startYear; y <= currentYear; y++) {
			Year year = Year.of(y);
			result.add(new Interval(
			   year.atDay(1).atStartOfDay(),
			   year.atMonth(12).atEndOfMonth().atTime(23, 59, 59, 999_999_999)
			));
		 }
		 return result;
	  }
   };

   public abstract List<Interval> intervals(LocalDate today, LocalDateTime now);

   public static Timeline fromString(String timeline) {
	  if (timeline == null) {
		 throw new IllegalArgumentException("timeline cannot be null");
	  }
	  return switch (timeline.toLowerCase()) {
		 case "hourly" -> HOURLY;
		 case "daily" -> DAILY;
		 case "monthly" -> MONTHLY;
		 case "yearly" -> YEARLY;
		 default -> throw new IllegalArgumentException("Invalid timeline: " + timeline);
	  };
   }
}