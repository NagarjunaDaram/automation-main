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

@Component
public class BuildMasterJson {
	
public String MasterJson(List<ExcelDataModel> data, List<ExcelDataModel> organizationData,int index) {
		
		String MasterJson = "";
		
		MasterJson = "{"+"\"data\""+":"
		+"{"+"\"identity\""+":"+
				" {"+"\"id\""+":"+"\""+data.get(index).getenterpriseSystemId()+"\""+","
		+"\"name\""+":"+"\""+data.get(index).getenterpriseSystemname()+"\""+","
				+"\"channel_url\""+":"+"\""+data.get(index).getenterpriseEndPointUrl()+"\""+","
		+"\"bucket_name\""+":"+"\""+data.get(index).getenterpriseS3BucketName()+"\""+","
				+"\"public_key\""+":"+"\""+data.get(index).getPublicKey()+"\""+","
		+"\"organization\""+":"+" {"+
				"\"id\""+":"+"\""+data.get(index).getenterpriseOrgId()+"\""+","+
		"\"name\""+":"+"\""+organizationData.get(index).getOrganizationName()+"\""+","+
		"\"org_type\""+":"+"\""+organizationData.get(index).getOrganizationType()+"\""+","+
		"\"icon_url\""+":"+"\""+organizationData.get(index).getOrganizationIconUrl()+"\""+"},"+
		"\"source_system_config\""+":"+data.get(index).getenterpriseSystemConfig()+"},"+"\"produces\""+" :"; 
		
		return MasterJson;
		
}
		
	@SuppressWarnings("unchecked")
	public String ConfigJson(List<String> enterpriseData) {
		JSONObject configJson = new JSONObject();
		
		JSONObject data = new JSONObject();
		JSONObject systemConfig = new JSONObject();
		JSONObject identityInnerElement = new JSONObject();
		JSONObject organization = new JSONObject();
		JSONArray to = new JSONArray();
		JSONArray onBehalfOf = new JSONArray();
		
		
		identityInnerElement.put("id", enterpriseData.get(0));
		identityInnerElement.put("name", enterpriseData.get(1));
		identityInnerElement.put("channel_url", enterpriseData.get(2));
		identityInnerElement.put("bucket_name", enterpriseData.get(3));
		to.add(data);
		
		
		/*
		 * onBehalfofInnerElement.put("id", onBehalfOfId);
		 * onBehalfofInnerElement.put("name", onBehalfOfName);
		 * onBehalfofInnerElement.put("iconUrl", onBehalfOfIconUrl);
		 * onBehalfOf.add(onBehalfofInnerElement); toInnerElement.put("on_behalf_of",
		 * onBehalfOf);
		 * 
		 * toSystem.put("id", systemid); toSystem.put("name", systemName);
		 * 
		 * toInnerElement.put("system", toSystem);
		 * 
		 * 
		 * System.out.println(toInnerElement);
		 */
		
		configJson.put("data", data);
		
		
		return configJson.toString();
	}


}
