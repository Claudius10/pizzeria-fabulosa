package org.clau.pizzeriautils.dto.admin;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriadata.model.common.APIError;

import java.util.List;

public record IncidenceListDTO(
   @NotNull
   List<APIError> content
) {
}
