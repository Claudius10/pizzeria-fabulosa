package org.clau.pizzeriadata.dto.admin;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderStatisticsByState(

   @NotNull
   List<Integer> count

) {
}
