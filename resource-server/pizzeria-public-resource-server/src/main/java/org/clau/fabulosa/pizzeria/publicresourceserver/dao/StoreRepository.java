package org.clau.fabulosa.pizzeria.publicresourceserver.dao;

import org.clau.fabulosa.data.model.assets.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}