package org.clau.pizzeriauserresourceserver.service;

import org.clau.pizzeriauserassets.dto.RegisterDTO;

public interface AnonUserService {

	void createUser(RegisterDTO registerDTO);
}
