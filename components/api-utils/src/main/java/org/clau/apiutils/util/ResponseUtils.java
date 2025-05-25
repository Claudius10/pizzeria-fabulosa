package org.clau.apiutils.util;


import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;

import java.util.UUID;

public final class ResponseUtils {

	public static ResponseDTO error(String origin, String reason, String path) {
		return ResponseDTO.builder()
				.apiError(APIError.builder()
						.withId(UUID.randomUUID().getMostSignificantBits())
						.withCreatedOn(TimeUtils.getNowAccountingDST())
						.withCause(reason)
						.withOrigin(origin)
						.withPath(path)
						.withLogged(false)
						.withFatal(false)
						.build())
				.build();
	}
}
