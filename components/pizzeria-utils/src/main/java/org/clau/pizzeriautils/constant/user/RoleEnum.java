package org.clau.pizzeriautils.constant.user;

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

