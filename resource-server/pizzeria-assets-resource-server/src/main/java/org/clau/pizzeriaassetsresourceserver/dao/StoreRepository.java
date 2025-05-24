package org.clau.pizzeriaassetsresourceserver.dao;

import org.clau.pizzeriastoreassets.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}