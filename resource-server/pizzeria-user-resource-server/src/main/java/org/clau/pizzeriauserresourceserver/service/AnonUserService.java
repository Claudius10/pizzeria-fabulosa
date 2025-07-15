package org.clau.pizzeriauserresourceserver.service;

import org.clau.pizzeriautils.dto.user.RegisterDTO;

public interface AnonUserService {

   void create(RegisterDTO registerDTO);
}
