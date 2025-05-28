package org.clau.pizzeriaassetsresourceserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.clau.apiutils.dto.ResponseDTO;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class TestUtils {

	public static ResponseDTO getResponse(MockHttpServletResponse response, ObjectMapper mapper) throws JsonProcessingException, UnsupportedEncodingException {
		return mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ResponseDTO.class);
	}
}