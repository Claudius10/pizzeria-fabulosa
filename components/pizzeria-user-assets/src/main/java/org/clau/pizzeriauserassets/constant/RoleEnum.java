package org.clau.pizzeriauserassets.constant;

import lombok.ToString;

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

