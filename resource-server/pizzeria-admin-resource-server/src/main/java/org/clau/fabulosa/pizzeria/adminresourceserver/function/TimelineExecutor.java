package org.clau.fabulosa.pizzeria.adminresourceserver.function;

import org.clau.fabulosa.pizzeria.adminresourceserver.data.util.Interval;
import org.clau.fabulosa.pizzeria.adminresourceserver.data.util.Timeline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

// https://en.wikipedia.org/wiki/Higher-order_function

public final class TimelineExecutor {
   private TimelineExecutor() {
   }

   public static <N extends Number> List<N> execute(
	  Timeline timeline,
	  Supplier<LocalDateTime> nowSupplier,
	  Function<Interval, N> counter
   ) {
	  LocalDateTime now = nowSupplier.get();
	  LocalDate today = now.toLocalDate();

	  List<Interval> intervals = timeline.intervals(today, now);

	  List<N> result = new ArrayList<>(intervals.size());

	  for (Interval interval : intervals) {
		 result.add(counter.apply(interval));
	  }

	  return result;
   }
}