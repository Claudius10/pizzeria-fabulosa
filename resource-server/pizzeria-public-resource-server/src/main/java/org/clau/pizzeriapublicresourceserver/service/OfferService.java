package org.clau.pizzeriapublicresourceserver.service;

import org.clau.pizzeriadata.model.assets.Offer;

import java.util.List;

public interface OfferService {

   List<Offer> findAll();
}
