package org.clau.pizzeriaassetsresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaassetsresourceserver.dao.OfferRepository;
import org.clau.pizzeriaassetsresourceserver.service.OfferService;
import org.clau.pizzeriastoreassets.model.Offer;
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
