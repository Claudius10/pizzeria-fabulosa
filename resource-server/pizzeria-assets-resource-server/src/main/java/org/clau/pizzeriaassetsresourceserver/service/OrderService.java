package org.clau.pizzeriaassetsresourceserver.service;

import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;

public interface OrderService {

	Order createAnonOrder(NewAnonOrderDTO newAnonOrder);

}
