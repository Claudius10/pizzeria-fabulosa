package org.clau.fabulosa.pizzeria.publicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.model.assets.Offer;
import org.clau.fabulosa.pizzeria.publicresourceserver.dao.OfferRepository;
import org.clau.fabulosa.pizzeria.publicresourceserver.service.OfferService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OfferServiceImpl implements OfferService {

   private final OfferRepository offerRepository;

   @Override
   @Cacheable("offers")
   public List<Offer> findAll() {
	  return offerRepository.findAll();
   }
}
