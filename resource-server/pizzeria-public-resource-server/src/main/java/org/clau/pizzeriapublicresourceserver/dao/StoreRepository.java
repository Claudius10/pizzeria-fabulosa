package org.clau.pizzeriapublicresourceserver.dao;

import org.clau.pizzeriapublicassets.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}