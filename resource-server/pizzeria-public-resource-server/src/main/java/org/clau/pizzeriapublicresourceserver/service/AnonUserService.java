package org.clau.pizzeriapublicresourceserver.service;

import org.clau.pizzeriapublicassets.dto.RegisterDTO;

public interface AnonUserService {

   void createUser(RegisterDTO registerDTO);
}
