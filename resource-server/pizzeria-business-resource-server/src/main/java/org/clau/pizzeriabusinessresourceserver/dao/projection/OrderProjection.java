package org.clau.pizzeriabusinessresourceserver.dao.projection;


import org.clau.pizzeriabusinessassets.model.Cart;
import org.clau.pizzeriabusinessassets.model.OrderDetails;

import java.time.LocalDateTime;

public interface OrderProjection {

	Long getId();

	LocalDateTime getCreatedOn();

	String getFormattedCreatedOn();

	String getAddress();

	OrderDetails getOrderDetails();

	Cart getCart();
}
