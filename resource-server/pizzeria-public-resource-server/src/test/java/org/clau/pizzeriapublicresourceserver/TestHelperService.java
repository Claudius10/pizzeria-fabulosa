package org.clau.pizzeriapublicresourceserver;

import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class TestHelperService {

   public ResponseDTO getResponse(String responseAsJson, ObjectMapper mapper) throws IOException {
	  return mapper.readValue(responseAsJson, ResponseDTO.class);
   }
}
