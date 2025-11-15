package org.clau.fabulosa.pizzeria.publicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.model.assets.Store;
import org.clau.fabulosa.pizzeria.publicresourceserver.dao.StoreRepository;
import org.clau.fabulosa.pizzeria.publicresourceserver.service.StoreService;
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
