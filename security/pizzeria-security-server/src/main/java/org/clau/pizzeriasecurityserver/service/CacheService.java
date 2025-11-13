package org.clau.pizzeriasecurityserver.service;

public interface CacheService {

   void evict(String cacheName, String key);
}
