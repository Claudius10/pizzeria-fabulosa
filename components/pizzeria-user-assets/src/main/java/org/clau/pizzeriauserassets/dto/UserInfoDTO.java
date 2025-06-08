package org.clau.pizzeriauserassets.dto;

public record UserInfoDTO(
		String id,
		String sub,
		String name,
		String email,
		Boolean emailVerified,
		String phoneNumber,
		Boolean phoneNumberVerified,
		String address,
		String locale,
		String zoneInfo,
		String updatedAt
) {
}
