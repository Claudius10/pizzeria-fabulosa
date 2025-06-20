package org.clau.pizzeriauserassets.constant;

public enum RoleEnum {
   USER("USER"),
   ADMIN("ADMIN");

   private final String name;

   RoleEnum(String name) {
	  this.name = name;
   }

   public String value() {
	  return this.name;
   }
}

