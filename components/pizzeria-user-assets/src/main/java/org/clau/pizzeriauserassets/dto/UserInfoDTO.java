package org.clau.pizzeriauserassets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoDTO(

		@NotBlank
		String id,

		@NotBlank
		String sub,

		@NotBlank
		String name,

		@NotBlank
		String email,

		@NotNull
		Boolean emailVerified,

		@NotBlank
		String phoneNumber,

		@NotNull
		Boolean phoneNumberVerified,

		@NotBlank
		String address,

		@NotBlank
		String locale,

		@NotBlank
		String zoneInfo,

		@NotBlank
		String updatedAt
) {
}
