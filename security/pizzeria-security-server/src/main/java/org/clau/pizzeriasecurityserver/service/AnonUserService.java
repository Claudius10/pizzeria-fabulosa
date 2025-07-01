package org.clau.pizzeriasecurityserver.service;

import org.clau.pizzeriauserassets.dto.RegisterDTO;

public interface AnonUserService {

   void create(RegisterDTO registerDTO);
}
