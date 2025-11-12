package org.clau.pizzeriaadminresourceserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriadata.dto.common.ResponseDTO;

import java.io.IOException;

public final class TestUtil {

   private TestUtil() {
	  throw new IllegalStateException("Utility class");
   }

   public static ResponseDTO getResponse(String responseAsJson, ObjectMapper mapper) throws IOException {
	  return mapper.readValue(responseAsJson, ResponseDTO.class);
   }
}
