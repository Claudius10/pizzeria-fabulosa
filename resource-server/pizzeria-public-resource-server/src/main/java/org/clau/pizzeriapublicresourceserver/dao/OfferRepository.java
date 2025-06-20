package org.clau.pizzeriapublicresourceserver.dao;

import org.clau.pizzeriapublicassets.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}