package com.cdi.automation.util;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cdi.automation.model.ExcelDataModel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Field;



@Component
public class BuildMasterJson {
	
public String dataJson(List<String> enterpriseData, List<String> organizationData, String sourceSystemUrl) {
		
		String MasterJson = "";
		//String sourceSystemUrl = "http://test-cdi-mock-server.eba-fafhacuy.ap-southeast-1.elasticbeanstalk.com/api/v1";
		
		MasterJson = "{"+"\"data\""+":"
		+"{"+"\"identity\""+":"+
				" {"+"\"id\""+":"+"\""+enterpriseData.get(0)+"\""+","
		+"\"name\""+":"+"\""+enterpriseData.get(1)+"\""+","
				+"\"channel_url\""+":"+"\""+enterpriseData.get(2)+"\""+","
		+"\"bucket_name\""+":"+"\""+enterpriseData.get(3)+"\""+","
				+"\"public_key\""+":"+"\""+enterpriseData.get(4)+"\""+","+"\"organization\""+":"+" {"+
				"\"id\""+":"+"\""+organizationData.get(0)+"\""+","+
		"\"name\""+":"+"\""+organizationData.get(1)+"\""+","+
		"\"org_type\""+":"+"\""+organizationData.get(2)+"\""+","+
		"\"icon_url\""+":"+"\""+organizationData.get(3)+"\""+"},"+
		"\"source_system_config\""+":"+"{"+"url:"+"\""+sourceSystemUrl+"\""+"}"+"},"+"\"produces\""+" :"; 
		
		return MasterJson;
		
}



		
	@SuppressWarnings("unchecked")
	public String ConfigJson(List<String> enterpriseData, List<String> organizationData) {
		JSONObject configJson = new JSONObject();
		String sourceSystemUrl = "http://test-cdi-mock-server.eba-fafhacuy.ap-southeast-1.elasticbeanstalk.com/api/v1";
		
		JSONObject systemConfig = new JSONObject();
		JSONObject identityInnerElement = new JSONObject();
		JSONArray produces = new JSONArray();
		JSONArray consumes = new JSONArray();
		
		identityInnerElement.put("id", enterpriseData.get(0));
		identityInnerElement.put("name", enterpriseData.get(1));
		identityInnerElement.put("channel_url", enterpriseData.get(2));
		identityInnerElement.put("bucket_name", enterpriseData.get(3));
		identityInnerElement.put("public_key", enterpriseData.get(4));
		JSONObject organization = new JSONObject();
	
			
		organization.put("id", organizationData.get(0));
		organization.put("name", organizationData.get(1));
		organization.put("org_type", organizationData.get(2));
		organization.put("icon_url", organizationData.get(3));
		
		systemConfig.put("url", sourceSystemUrl);
		
		identityInnerElement.put("organization", organization);
		identityInnerElement.put("source_system_config", systemConfig);
		
		JSONObject data = new JSONObject();
		
	
		data.put("identity",identityInnerElement);
		data.put("produces",produces);
		data.put("consumes",consumes);
		
		
		configJson.put("data", data);
		
		
		
		String jsonStr = new com.google.gson.Gson().toJson(configJson);
		
		
		//return configJson.toString();
		return jsonStr;
	}
	
	public String ParseJson(String jsonstr) {
		
			JSONParser parser = new JSONParser();
			String nameval = null;
			try {
				JSONObject json = (JSONObject) parser.parse(jsonstr);
				nameval = (String) json.get("name");
				
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			System.out.println("Name Value from Swagger Schema is"+nameval);
			return nameval;
		
	}


}
