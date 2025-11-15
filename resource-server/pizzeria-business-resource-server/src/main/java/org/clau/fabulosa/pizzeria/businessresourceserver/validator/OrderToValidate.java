package org.clau.fabulosa.pizzeria.businessresourceserver.validator;

import org.clau.fabulosa.data.dto.business.CartDTO;
import org.clau.fabulosa.data.dto.business.OrderDetailsDTO;

public record OrderToValidate(
   CartDTO cart,
   OrderDetailsDTO orderDetails
) {
}
