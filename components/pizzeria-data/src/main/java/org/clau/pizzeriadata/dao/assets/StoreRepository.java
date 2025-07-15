package org.clau.pizzeriadata.dao.assets;

import org.clau.pizzeriadata.model.assets.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}