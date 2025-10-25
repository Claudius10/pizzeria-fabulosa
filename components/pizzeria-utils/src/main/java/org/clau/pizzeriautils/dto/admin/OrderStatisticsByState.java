package org.clau.pizzeriautils.dto.admin;

import java.util.List;

public record OrderStatisticsByState(
   List<Integer> countsByState
) {
}
