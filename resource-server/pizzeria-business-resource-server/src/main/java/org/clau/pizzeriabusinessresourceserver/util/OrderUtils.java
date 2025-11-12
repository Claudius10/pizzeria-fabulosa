package org.clau.pizzeriabusinessresourceserver.util;

public final class OrderUtils {

   private OrderUtils() {
	  throw new IllegalStateException("Utility class");
   }

   // changeRequested == null || (changeRequested - totalCostOffers || totalCost)
   public static Double calculatePaymentChange(Double changeRequested, Double totalCost, Double totalCostAfterOffers) {
	  if (changeRequested == null) {
		 return null;
	  }

	  if (totalCostAfterOffers > 0) {
		 return (double) Math.round((changeRequested - totalCostAfterOffers) * 100) / 100;
	  }

	  return (double) Math.round((changeRequested - totalCost) * 100) / 100;
   }
}
