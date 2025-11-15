package org.clau.fabulosa.securityserver.service;

public interface CacheService {

   void evict(String cacheName, String key);
}
