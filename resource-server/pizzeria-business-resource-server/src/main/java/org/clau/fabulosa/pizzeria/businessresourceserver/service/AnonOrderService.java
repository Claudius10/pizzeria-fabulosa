package org.clau.fabulosa.pizzeria.businessresourceserver.service;

import org.clau.fabulosa.data.dto.business.NewAnonOrderDTO;
import org.clau.fabulosa.data.model.business.Order;

public interface AnonOrderService {

   Order createAnonOrder(NewAnonOrderDTO newAnonOrder);
}
