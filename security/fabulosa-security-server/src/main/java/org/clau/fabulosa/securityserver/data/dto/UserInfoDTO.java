package org.clau.fabulosa.securityserver.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserInfoDTO(

   @NotBlank
   String id,

   @NotBlank
   String sub,

   @NotBlank
   String name,

   @NotBlank
   String email,

   @NotEmpty
   List<String> roles,

   @NotNull
   Boolean email_verified,

   @NotBlank
   String phone_number,

   @NotNull
   Boolean phone_number_verified,

   @NotBlank
   String locale,

   @NotBlank
   String zoneinfo,

   @NotBlank
   String updated_at

) {
}
