package org.clau.pizzeriauserresourceserver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriauserassets.constant.RoleEnum;
import org.clau.pizzeriauserassets.dto.RegisterDTO;
import org.clau.pizzeriauserassets.model.Role;
import org.clau.pizzeriauserassets.model.User;
import org.clau.pizzeriauserresourceserver.service.AnonUserService;
import org.clau.pizzeriauserresourceserver.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

	private final RoleService roleService;

	private final AnonUserService anonUserService;

	private final EntityManager entityManager;

	public void createRole() {
		if (roleService.findByName(RoleEnum.USER.value()).isEmpty()) {
			roleService.createRole(Role.builder().withName(RoleEnum.USER.value()).build());
		}
	}

	public Long createUser(String email) {

		createRole();

		RegisterDTO registerDTO = new RegisterDTO(
				"Tester",
				email,
				email,
				123456789,
				"Password1",
				"Password1");

		anonUserService.createUser(registerDTO);

		return findUserByEmail(email);
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
