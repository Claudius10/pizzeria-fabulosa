package org.clau.fabulosa.data.dto.admin;

import jakarta.validation.constraints.NotNull;
import org.clau.fabulosa.data.model.common.APIError;

import java.util.List;

public record IncidenceListDTO(

   @NotNull
   List<APIError> content

) {
}
