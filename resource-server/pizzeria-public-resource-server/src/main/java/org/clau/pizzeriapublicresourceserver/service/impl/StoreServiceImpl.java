package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.assets.StoreRepository;
import org.clau.pizzeriadata.model.assets.Store;
import org.clau.pizzeriapublicresourceserver.service.StoreService;
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
