package org.clau.pizzeriadata.dto.admin;

import java.util.List;

public record OrderStatisticsByState(

   List<Integer> countsByState

) {
}
