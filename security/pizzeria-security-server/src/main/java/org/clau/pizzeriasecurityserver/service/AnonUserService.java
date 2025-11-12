package org.clau.pizzeriasecurityserver.service;

import org.clau.pizzeriasecurityserver.data.dto.RegisterDTO;

public interface AnonUserService {

   void create(RegisterDTO registerDTO);
}
