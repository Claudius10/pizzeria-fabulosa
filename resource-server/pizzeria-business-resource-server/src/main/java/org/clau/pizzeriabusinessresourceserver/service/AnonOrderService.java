package org.clau.pizzeriabusinessresourceserver.service;

import org.clau.pizzeriadata.dto.business.NewAnonOrderDTO;
import org.clau.pizzeriadata.model.business.Order;

public interface AnonOrderService {

   Order createAnonOrder(NewAnonOrderDTO newAnonOrder);
}
