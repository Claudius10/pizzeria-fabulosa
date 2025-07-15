package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.assets.OfferRepository;
import org.clau.pizzeriadata.model.assets.Offer;
import org.clau.pizzeriapublicresourceserver.service.OfferService;
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
