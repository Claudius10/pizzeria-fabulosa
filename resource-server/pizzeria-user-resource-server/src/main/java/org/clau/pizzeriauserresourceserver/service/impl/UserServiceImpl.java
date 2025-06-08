package org.clau.pizzeriauserresourceserver.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Response;
import org.clau.pizzeriauserresourceserver.dao.UserRepository;
import org.clau.pizzeriauserresourceserver.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public void deleteById(Long userId) {
		userRepository.findById(userId).ifPresentOrElse(
				userRepository::delete,
				() -> {
					throw new EntityNotFoundException(Response.USER_NOT_FOUND);
				}
		);
	}
}
