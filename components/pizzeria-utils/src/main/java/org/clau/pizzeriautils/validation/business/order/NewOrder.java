package org.clau.pizzeriautils.validation.business.order;

import org.clau.pizzeriautils.dto.business.CartDTO;
import org.clau.pizzeriautils.dto.business.OrderDetailsDTO;

public record NewOrder(
   CartDTO cart,
   OrderDetailsDTO orderDetails
) {
}
