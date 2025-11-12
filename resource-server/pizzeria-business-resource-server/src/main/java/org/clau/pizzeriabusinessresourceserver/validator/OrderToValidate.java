package org.clau.pizzeriabusinessresourceserver.validator;

import org.clau.pizzeriadata.dto.business.CartDTO;
import org.clau.pizzeriadata.dto.business.OrderDetailsDTO;

public record OrderToValidate(
   CartDTO cart,
   OrderDetailsDTO orderDetails
) {
}
