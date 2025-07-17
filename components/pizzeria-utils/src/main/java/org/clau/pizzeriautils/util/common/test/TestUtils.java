package org.clau.pizzeriautils.util.common.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.pizzeriautils.dto.common.ResponseDTO;

import java.io.UnsupportedEncodingException;

public class TestUtils {

   public static ResponseDTO getResponse(String responseAsJson, ObjectMapper mapper) throws JsonProcessingException, UnsupportedEncodingException {
	  return mapper.readValue(responseAsJson, ResponseDTO.class);
   }
}