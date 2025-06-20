package org.clau.pizzeriaassetsresourceserver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaassetsresourceserver.service.RoleService;
import org.clau.pizzeriauserassets.constant.RoleEnum;
import org.clau.pizzeriauserassets.model.Role;
import org.clau.pizzeriauserassets.model.User;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

   private final RoleService roleService;

   private final EntityManager entityManager;

   public void createRole() {
	  if (roleService.findByName(RoleEnum.USER.value()).isEmpty()) {
		 roleService.createRole(Role.builder().withName(RoleEnum.USER.value()).build());
	  }
   }

   public Long findUserByEmail(String email) {
	  try {
		 User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
		 return user.getId();
	  } catch (NoResultException ex) {
		 return null;
	  }
   }
}
