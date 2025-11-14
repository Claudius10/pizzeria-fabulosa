package org.clau.pizzeriadata.dto.admin;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderStatistics(

   @NotNull
   List<OrderStatisticsByState> statisticsByState

) {
}
