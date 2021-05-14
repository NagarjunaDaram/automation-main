package com.cdi.automation.util;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetJsonNode{
	public static Object GetNode(String filename) throws Exception {
		FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);

	}


}
    