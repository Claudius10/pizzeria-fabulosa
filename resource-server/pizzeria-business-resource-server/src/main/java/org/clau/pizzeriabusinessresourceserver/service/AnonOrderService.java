package org.clau.pizzeriabusinessresourceserver.service;

import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;

public interface AnonOrderService {

	Order createAnonOrder(NewAnonOrderDTO newAnonOrder);
}
