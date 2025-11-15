package org.clau.fabulosa.pizzeria.publicresourceserver.dao;

import org.clau.fabulosa.data.model.assets.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}