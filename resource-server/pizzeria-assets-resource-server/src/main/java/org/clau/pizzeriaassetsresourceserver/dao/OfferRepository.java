package org.clau.pizzeriaassetsresourceserver.dao;

import org.clau.pizzeriastoreassets.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}