package org.clau.pizzeriautils.dto.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.validation.common.annotation.DoubleLength;
import org.clau.pizzeriautils.validation.common.annotation.IntegerLength;

import java.util.List;
import java.util.Map;

public record CartItemDTO(

   Long id,

   @NotBlank
   String type,

   @NotNull
   @DoubleLength(min = 1, max = 5)
   Double price,

   @NotNull
   @IntegerLength(min = 1, max = 2, message = ValidationResponses.CART_ITEM_MAX_QUANTITY_ERROR)
   Integer quantity,

   @NotNull
   Map<String, String> name,

   @NotNull
   Map<String, List<String>> description,

   @NotNull
   Map<String, Map<String, String>> formats
) {
}
