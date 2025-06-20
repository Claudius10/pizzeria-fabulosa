package org.clau.pizzeriaassetsresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaassetsresourceserver.dao.StoreRepository;
import org.clau.pizzeriaassetsresourceserver.service.StoreService;
import org.clau.pizzeriastoreassets.model.Store;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

   private final StoreRepository storeRepository;

   @Override
   @Cacheable("stores")
   public List<Store> findAll() {
	  return storeRepository.findAll();
   }
}
