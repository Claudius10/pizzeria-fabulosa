package org.clau.pizzeriautils.logger;

public final class ExceptionLogger {

   private ExceptionLogger() {
	  throw new IllegalStateException("Utility class");
   }

   public static void log(Exception e, org.slf4j.Logger log) {
	  log.warn("----- -- ExceptionLogger START ---- -- ");
	  log.warn("----- --");

	  String simpleName = e.getClass().getSimpleName();
	  String message = e.getMessage();
	  Throwable cause = e.getCause();
	  StackTraceElement[] stackTrace = e.getStackTrace();

	  log.warn("----- -- Details ---- -- ");
	  log.info("Exception simple name: {}", simpleName);

	  if (message != null) {
		 log.info("Exception message: {}", message);
	  }

	  if (cause != null) {
		 log.info("Exception cause: {}", cause.getMessage());
	  } else {
		 log.info("Exception cause: null");
	  }

	  log.warn("----- --");
	  log.warn("----- -- Stack trace ---- -- ");

	  if (stackTrace.length > 0) {
		 log.info("Exception", e);
	  }

	  log.warn("----- --");
	  log.warn("----- -- ExceptionLogger END ---- -- ");
   }
}
