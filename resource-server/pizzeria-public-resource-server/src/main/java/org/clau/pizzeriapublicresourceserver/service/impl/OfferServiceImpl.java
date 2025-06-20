package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriapublicresourceserver.dao.OfferRepository;
import org.clau.pizzeriapublicresourceserver.service.OfferService;
import org.clau.pizzeriapublicassets.model.Offer;
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
