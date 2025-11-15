package org.clau.pizzeriasecurityserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

   private final CacheManager cacheManager;

   @Override
   public void evict(String cacheName, String key) {
	  Cache cache = getCache(cacheName);
	  if (cache != null) {
		 cache.evict(key);
	  }
   }

   private Cache getCache(String cacheName) {
	  return cacheManager.getCache(cacheName);
   }
}
