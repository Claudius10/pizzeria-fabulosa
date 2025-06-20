package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriapublicresourceserver.dao.StoreRepository;
import org.clau.pizzeriapublicresourceserver.service.StoreService;
import org.clau.pizzeriapublicassets.model.Store;
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
