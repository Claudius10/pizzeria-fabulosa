package org.clau.apiutils.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.clau.apiutils.model.APIError;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseDTO {

	@NotNull
	private final APIError apiError;

	private final int status;
}