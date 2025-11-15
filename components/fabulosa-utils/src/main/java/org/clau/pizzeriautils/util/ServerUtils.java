package org.clau.pizzeriautils.util;

public final class ServerUtils {

   private ServerUtils() {
	  throw new IllegalStateException("Utility class");
   }

   public static String resolvePath(String one, String two) {
	  boolean isOneInvalid = (null == one || one.isBlank());
	  boolean isTwoInvalid = (null == two || two.isBlank());

	  if (isOneInvalid && isTwoInvalid) {
		 return null;
	  }

	  if (isOneInvalid) {
		 return two;
	  }

	  return one;
   }
}