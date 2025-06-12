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
		Boolean email_verified,

		@NotBlank
		String phone_number,

		@NotNull
		Boolean phone_number_verified,

		@NotBlank
		String address,

		@NotBlank
		String locale,

		@NotBlank
		String zoneinfo,

		@NotBlank
		String updated_at
) {
}
