package com.zj.ryxb.common.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {
	public static final String CONTENT_TYPE = "application/json; charset=utf-8";

	public static void writeJsonToStream(Object modelMap, HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
		// response.setCharacterEncoding();
		response.setContentType(CONTENT_TYPE);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), modelMap);
	}

}
