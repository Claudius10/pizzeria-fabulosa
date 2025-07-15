package org.clau.pizzeriautils.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.clau.pizzeriadata.model.common.APIError;

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