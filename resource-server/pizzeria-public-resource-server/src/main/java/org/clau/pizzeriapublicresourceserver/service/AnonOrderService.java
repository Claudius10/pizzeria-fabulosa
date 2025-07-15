package org.clau.pizzeriapublicresourceserver.service;

import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriautils.dto.business.NewAnonOrderDTO;

public interface AnonOrderService {

   Order createAnonOrder(NewAnonOrderDTO newAnonOrder);
}
