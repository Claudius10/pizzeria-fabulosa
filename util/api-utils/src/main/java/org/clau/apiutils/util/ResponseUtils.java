package org.clau.apiutils.util;


import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;

import java.util.UUID;

public final class ResponseUtils {

	public static ResponseDTO error(String origin, String reason, String path) {
		return ResponseDTO.builder()
				.apiError(APIError.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.createdOn(TimeUtils.getNowAccountingDST())
						.cause(reason)
						.origin(origin)
						.path(path)
						.logged(false)
						.fatal(false)
						.build())
				.build();
	}
}
