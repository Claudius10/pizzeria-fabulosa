package org.clau.pizzeriadata.dao.assets;

import org.clau.pizzeriadata.model.assets.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}