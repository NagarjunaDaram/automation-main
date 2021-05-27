package com.cdi.automation.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cdi.automation.model.ExcelDataModel;
import com.cdi.automation.model.TestListDataModel;
import com.cdi.automation.model.TokenResponse;
import com.cdi.automation.util.ConnectionUtil;
import com.cdi.automation.util.BuildMasterJson;
import com.cdi.automation.util.FlatMapUtil;
import com.cdi.automation.util.GetJsonNode;
import com.cdi.automation.util.TestReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 







@Service
public class CDIAutomationService {
	
	private static final Logger logger = LoggerFactory.getLogger(CDIAutomationService.class);
	
//	RestTemplate restTemplate;
	@Autowired
	ConnectionUtil connectionUtil;
	
	@Autowired
	BuildMasterJson buildMasterJson;
	

	
	@Value("${masterdatasheet}")
	public String file_path;
	
	@Value("${jsonfile}")
	public String jsonfile_path;
	
	@Value("${EnterpriseWorksheet}")
	public String EnterpriseWorksheet;
	
	@Value("${OrganizationWorksheet}")
	public String OrganizationWorksheet;
	
	@Value("${SubscriptionWorkSheet}")
	public String SubscriptionWorkSheet;
	
	@Value("${TestListSheet}")
	public String testListSheet;
	
	@Value("${MasterDataSheet}")
	public String MasterData;
	
	@Value("${SwaggerSchemaEndpoint}")
	public String SwaggerSchemaUrl;
	
	@Value("${SwaggerUCCEndpoint}")
	public String SwaggerUCCUrl;

	@Value("${sourceSystemConfigUrl}")
	public String sourceSystemUrl;
	
	public  String functionaltest() {
		String response =null;
		String masterJson = null;
		logger.info("====================In Service========================");
		try {
			
			List<ExcelDataModel> data = getEnterpriseData(EnterpriseWorksheet);
			List<ExcelDataModel> subscriptionData = getSubscriptionData(SubscriptionWorkSheet);
			List<ExcelDataModel> organizationData = getOrganizationData(OrganizationWorksheet);
			List<TestListDataModel> testListData = getTestListData(testListSheet);
			List<ExcelDataModel> ParticipantData = new ArrayList<ExcelDataModel>();
			
			List<String> providerDataElement = new ArrayList<String>();
			List<String> providerDatElement = new ArrayList<String>();
			List<String> providerDataIndex = new ArrayList<String>();
			List<String> providerIndex = new ArrayList<String>();
			
			
			List<String> subscriberDataElement = new ArrayList<String>();
			List<String> subscriberDatElement = new ArrayList<String>();
			List<String> subscriberDataIndex = new ArrayList<String>();
			List<String> subscriberIndex = new ArrayList<String>();
			//List<String> subscriberFromData = new ArrayList<String>();
			

			int countOfRowsInSubscription = subscriptionData.size();
			int countofRowsInEnterpriseSheet = data.size();
			int countofRowsInOrganizationSheet = organizationData.size();
			int countofRowsInTestListSheet = testListData.size();
			
			
			String ProvideType = "CONSUMER";
			
			
			for (int i =0; i < countofRowsInTestListSheet; i++) {
				String tokenUrl = null;
				masterJson = null;
				String participantOrgId = null;
				
				String SystemId = testListData.get(i).getTestSystemId();
				System.out.println(SystemId);
				/*Check the Health API */
				String testURL = testListData.get(i).getTestUrl();
				System.out.println(testURL);
				
				String systemName = testListData.get(i).getTestSystemName();
			
				HttpHeaders requestHeaders = new HttpHeaders();
				tokenUrl = testURL+"test-token";
				System.out.println("toekn-url:"+tokenUrl);
				String testtoken = getToken(tokenUrl);
				System.out.println("Testtoken:"+testtoken);
				
				//requestHeaders.set("Authorization", "Bearer ".concat(getToken(tokenUrl)));
				//requestHeaders.add(SystemId, testURL);
				String healthUrl = testURL+"v1/health";
				requestHeaders.set("Authorization", "Bearer "+testtoken);
				String configResponse =	connectionUtil.callGetRestService(healthUrl,  requestHeaders);
				System.out.println("Health API Response:"+healthUrl+"\n"+configResponse);
				if (configResponse.isEmpty()) {
					System.out.println("health Check for Pitstop"+healthUrl+"Failed:No Further tests will be executed");
					
					continue;
				}
				else {
					System.out.println("health Check for Pitstop"+healthUrl+"---->"+" Successful: Continuing the Test.....");
					System.out.println("Run the Config Check ......");
					List<String> dataEnterpriseList = new ArrayList<String>();
					List<String> dataOrganizationList = new ArrayList<String>();
					for (int a =0; a < countofRowsInEnterpriseSheet; a++)
					{
						String participantSystemID = data.get(a).getenterpriseSystemId();
						
						if(participantSystemID.equals(SystemId)) {
							
							System.out.println("Channel_Url:"+data.get(a).getEnterpriseendPointUrl());
							dataEnterpriseList.add(participantSystemID);
							dataEnterpriseList.add(data.get(a).getenterpriseSystemname());
							dataEnterpriseList.add(data.get(a).getEnterpriseendPointUrl());
							dataEnterpriseList.add(data.get(a).getenterpriseS3BucketName());
							dataEnterpriseList.add(data.get(a).getPublicKey());
							participantOrgId = data.get(a).getenterpriseOrgId();
							System.out.println("Organization ID in Data JSOn is"+participantOrgId);
							for (int b=0; b < countofRowsInOrganizationSheet; b++) {
								if (participantOrgId.equals(organizationData.get(b).getOrgnizationOrgId())) {
									dataOrganizationList.add(organizationData.get(b).getOrgnizationOrgId());
									dataOrganizationList.add(organizationData.get(b).getOrganizationName());
									dataOrganizationList.add(organizationData.get(b).getOrganizationType());
									dataOrganizationList.add(organizationData.get(b).getOrganizationIconUrl());
								}
							}
							
						}
						
						
					}
					
					String dataJson = buildMasterJson.ConfigJson(dataEnterpriseList, dataOrganizationList); 
					
					
					String firstreplace = dataJson.substring(0, dataJson.length() - 1); // AB
					String replaced = firstreplace + "}";
					
					String secondreplace = replaced.substring(0, replaced.length() - 1); // AB
					String datajson1 = secondreplace + "}";
					System.out.println("Data JSON is :"+datajson1);
					
					
					for (int j = 0; j < countOfRowsInSubscription; j++ ) {
						String providerSystemId = subscriptionData.get(j).getProsumerSystemId();
						String subscriberSystemId = subscriptionData.get(j).getSubscriberSystemId();
						//System.out.println("Prosumer System ID:"+providerSystemId);
						/*to get the what is being provided*/
						if (providerSystemId.equals(SystemId)) {
							String providerType = subscriptionData.get(j).getProsumerType();
							
							if(providerType.equals(ProvideType)) {
								System.out.println("Prosumer Type:"+providerSystemId+providerType+"Index: "+j+subscriptionData.get(j).getDataElementId());
								providerDatElement.add(subscriptionData.get(j).getDataElementId());
								providerDataIndex.add(subscriptionData.get(j).getDataElementId()+":"+j);
								 
								
								
							}
						}
						/*to get the what is being consumed*/
						if (subscriberSystemId.equals(SystemId)) {
							String subscriberType = subscriptionData.get(j).getProsumerType();
							
							if(subscriberType.equals(ProvideType)) {
								System.out.println("Subscriber Type:"+providerSystemId+subscriberType+"Index: "+j+subscriptionData.get(j).getDataElementId());
								subscriberDatElement.add(subscriptionData.get(j).getDataElementId());
								subscriberDataIndex.add(subscriptionData.get(j).getDataElementId()+":"+j);
								 
								
								
							}
						}
						
					}
					/*Construct the Produces Structure*/
					if(!providerDatElement.isEmpty()) {
						System.out.println("provider List with their respective indexes"+providerIndex);
						providerDataElement = removeDuplicates((ArrayList<String>) providerDatElement);
						//System.out.println("ArrayList with duplicates removed: " + providerDataElement);
						for (int k =0; k < providerDataElement.size(); k ++) {
							List<String> providerToData = new ArrayList<String>();
							String DataElement = providerDataElement.get(k);
							/*Get the Swagger Schema for each Data Element*/
							String SwaggerBody = getSwaggerSchemaBody(DataElement, SwaggerUCCUrl);
							System.out.println("Swagger Schema Body for  " + DataElement+"\n"+SwaggerBody);
							HttpHeaders SwaggerSchemaHeaders = new HttpHeaders();
							SwaggerSchemaHeaders.set("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImNkaS1tb2NrIiwiaWF0IjoxNjE4NDcyODc1fQ.lhIFuqAOIAkuniIiNrDFgvHyPJqqDqBl0NWswUNCZHA");
							String providerSwaggerSchema = connectionUtil.callPostRestService(SwaggerSchemaUrl, SwaggerBody, SwaggerSchemaHeaders);
							providerSwaggerSchema = providerSwaggerSchema.substring(0, providerSwaggerSchema.length() - 1) + "";
							providerSwaggerSchema = providerSwaggerSchema.substring(0, providerSwaggerSchema.length() - 1) + "";
							providerSwaggerSchema = providerSwaggerSchema+",";
							
							if(providerSwaggerSchema.contains("querySchema")) {
								providerSwaggerSchema = providerSwaggerSchema.substring(0, providerSwaggerSchema.length() - 1) + "";
								providerSwaggerSchema = providerSwaggerSchema.substring(0, providerSwaggerSchema.length() - 1) + ",";
								
							}
							else{
								providerSwaggerSchema = providerSwaggerSchema.substring(0, providerSwaggerSchema.length() - 1) + ",";
								
							}
							//Thread.sleep(6000);
							//System.out.println("Swagger Schema :"+DataElement+"\n"+providerSwaggerSchema);
							
							providerIndex = getMatchingElements(DataElement, providerDataIndex);
							String subscriberOrgName = null;
							String subscriberOrgIconUrl = null;
							String subscriberonbehalfOfId = null;
							String subscriberonbehalfOfName = null;
							String subscriberonbehalfOfIconurl = null;
							String ProsumerOrgId = null;
							String SubscriberOrgId = null;
							String subscriberEnterpriseSystemID = null;
							String subscriberEnterpriseSystemName = null;
							System.out.println("provider index"+providerIndex);
							for (int n =0; n < providerIndex.size(); n++) {
								String parts [] = providerIndex.get(n).split(":");
								int index = Integer.parseInt(parts[1]);
								SubscriberOrgId = subscriptionData.get(index).getSubscriberOrgId();
								ProsumerOrgId = subscriptionData.get(index).getProsumerOrgId();
								for (int m =0; m < countofRowsInOrganizationSheet; m ++) {
									if(SubscriberOrgId.equals(organizationData.get(m).getOrgnizationOrgId())) {
										subscriberOrgName = organizationData.get(m).getOrganizationName();
										subscriberOrgIconUrl = organizationData.get(m).getOrganizationIconUrl();
										if (ProsumerOrgId.equals(participantOrgId))
										{
											subscriberonbehalfOfId = "";
										}
									
										else {
											subscriberonbehalfOfId = SubscriberOrgId;
											for (int o=0; o <countofRowsInOrganizationSheet; o++) {
												if(subscriberonbehalfOfId.equals(organizationData.get(o).getOrgnizationOrgId())){
													subscriberonbehalfOfName = organizationData.get(o).getOrganizationName();
													subscriberonbehalfOfIconurl = organizationData.get(o).getOrganizationIconUrl();
												}
											}
											for (int d=0; d<countofRowsInEnterpriseSheet;d++) {
												if(ProsumerOrgId.equals(data.get(d).getenterpriseOrgId())) {
													subscriberEnterpriseSystemID = data.get(d).getenterpriseSystemId();
													subscriberEnterpriseSystemName = data.get(d).getenterpriseSystemname();
												}
											}
											
										}
										
									}
								}
								String dataElementToContent = getProviderToContent(SubscriberOrgId,subscriberOrgName, subscriberOrgIconUrl, subscriberonbehalfOfId, subscriberonbehalfOfName, subscriberonbehalfOfIconurl,subscriberEnterpriseSystemID, subscriberEnterpriseSystemName); 
								System.out.println("Provider To Content is "+dataElementToContent);
								providerToData.add(dataElementToContent);
							}
							
							System.out.println("Provider To Content is"+providerToData);
							  //String res = buildMasterJson.MasterJson(data , i); 
							  //masterJson = res+providerSwaggerSchema; 
							  //System.out.println(masterJson);
							 
							
							
						}
						
					}
					/*Construct the Consumes Structure*/
					if(!subscriberDatElement.isEmpty()) {
						subscriberDataElement = removeDuplicates((ArrayList<String>) subscriberDatElement);
						//System.out.println("ArrayList with duplicates removed: " + providerDataElement);
						
						for (int k =0; k < subscriberDataElement.size(); k ++) {
							List<String> subscriberFromData = new ArrayList<String>();
							String DataElement = subscriberDataElement.get(k);
							String SwaggerBody = getSwaggerSchemaBody(DataElement, SwaggerUCCUrl);
							System.out.println("Swagger Schema Body for  " + DataElement+"\n"+SwaggerBody);
							HttpHeaders SwaggerSchemaHeaders = new HttpHeaders();
							SwaggerSchemaHeaders.set("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImNkaS1tb2NrIiwiaWF0IjoxNjE4NDcyODc1fQ.lhIFuqAOIAkuniIiNrDFgvHyPJqqDqBl0NWswUNCZHA");
							String consumerSwaggerSchema = connectionUtil.callPostRestService(SwaggerSchemaUrl, SwaggerBody, SwaggerSchemaHeaders);
							if(consumerSwaggerSchema.contains("querySchema")) {
								consumerSwaggerSchema = consumerSwaggerSchema.substring(0, consumerSwaggerSchema.length() - 1) + "";
								consumerSwaggerSchema = consumerSwaggerSchema.substring(0, consumerSwaggerSchema.length() - 1) + ",";
								
							}
							else{
								consumerSwaggerSchema = consumerSwaggerSchema.substring(0, consumerSwaggerSchema.length() - 1) + ",";
								
							}
							
							//consumerSwaggerSchema = consumerSwaggerSchema.substring(0, consumerSwaggerSchema.length() - 1) + "";
							//consumerSwaggerSchema = consumerSwaggerSchema+",";
							//Thread.sleep(6000);
							System.out.println("Swagger Schema :"+DataElement+"\n"+consumerSwaggerSchema);
							/*
							 * String res = buildMasterJson.MasterJson(data , i); masterJson =
							 * res+consumerSwaggerSchema; System.out.println(masterJson);
							 */
							
							subscriberIndex = getMatchingElements(DataElement, subscriberDataIndex);
							String ProsumerOrgName = null;
							String ProsumerOrgIconUrl = null;
							String onbehalfOfId = null;
							String onbehalfOfName = null;
							String onbehalfOfIconurl = null;
							String ProsumerOrgId = null;
							String SubscriberOrgId = null;
							String EnterpriseSystemID = null;
							String EnterpriseSystemName = null;
							
							for (int l =0; l < subscriberIndex.size(); l++) {
								
								String parts[] = subscriberIndex.get(l).split(":");
								int index = Integer.parseInt(parts[1]);
							     
								System.out.println("Indexex of each element"+ parts[1]);
								ProsumerOrgId = subscriptionData.get(index).getProsumerOrgId();
								SubscriberOrgId = subscriptionData.get(index).getSubscriberOrgId();
								
								
								for (int m =0; m < countofRowsInOrganizationSheet; m ++) {
									if(ProsumerOrgId.equals(organizationData.get(m).getOrgnizationOrgId())) {
										ProsumerOrgName = organizationData.get(m).getOrganizationName();
										ProsumerOrgIconUrl = organizationData.get(m).getOrganizationIconUrl();
										if (SubscriberOrgId.equals(participantOrgId))
										{
											onbehalfOfId = "";
										}
									
										else {
											onbehalfOfId = SubscriberOrgId;
											for (int n=0; n <countofRowsInOrganizationSheet; n++) {
												if(onbehalfOfId.equals(organizationData.get(n).getOrgnizationOrgId())){
													onbehalfOfName = organizationData.get(n).getOrganizationName();
													onbehalfOfIconurl = organizationData.get(n).getOrganizationIconUrl();
												}
											}
											for (int c=0; c<countofRowsInEnterpriseSheet;c++) {
												if(ProsumerOrgId.equals(data.get(c).getenterpriseOrgId())) {
													EnterpriseSystemID = data.get(c).getenterpriseSystemId();
													EnterpriseSystemName = data.get(c).getenterpriseSystemname();
												}
											}
											
										}
										
									}
								}
										
								String dataElementFromContent = getConsumerFromContent(ProsumerOrgId,ProsumerOrgName, ProsumerOrgIconUrl, onbehalfOfId, onbehalfOfName, onbehalfOfIconurl,EnterpriseSystemID, EnterpriseSystemName); 
								System.out.println("Consumer From Content is "+dataElementFromContent);
								subscriberFromData.add(dataElementFromContent);
										
							}
							
							System.out.println("Consumer From Content is "+subscriberFromData);
							
						}
						
					}

					
					
				}
				
				
			}
			
			
		
			

		   response = "Test Complete!" ;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String currentdatetime = dtf.format(now);
		String ReportFilename = "Automation_Report_"+currentdatetime;
		TestReport.GenerateReport(ReportFilename);
		
		return response;
	}
	
	
	
	public List<String> getMatchingElements(String search, List<String> list) {
	    List<String> matches = new ArrayList<String>();

	    for(String str: list) {
	        if (str.contains(search)) {
	            matches.add(str);
	        }
	    }

	    return matches;
	}
	

	@SuppressWarnings("unchecked")
	public String getSwaggerSchemaBody(String dataElement, String swaggerUrl) {
		String schemaBody = null;
		
	
		JSONObject swagger = new JSONObject();
		swagger.put("swaggerUrl", swaggerUrl);
		swagger.put("swaggerAPIKey", "8abd5281-801e-4344-afb7-49960fb24ab4");
		JSONArray array = new JSONArray();
		array.add(dataElement);
		swagger.put("parameters", array);
		schemaBody = swagger.toJSONString();
		return schemaBody;
	}
	
	@SuppressWarnings("unchecked")
	public String getProviderToContent(String Id, String Name, String iconUrl, String onBehalfOfId, String onBehalfOfName, String onBehalfOfIconUrl, String systemid, String systemName ) {
		
		JSONObject swagger = new JSONObject();
		JSONObject toInnerElement = new JSONObject();
		JSONObject onBehalfofInnerElement = new JSONObject();
		JSONObject toSystem = new JSONObject();
		JSONArray to = new JSONArray();
		JSONArray onBehalfOf = new JSONArray();
		
		toInnerElement.put("id", Id);
		toInnerElement.put("name", Name);
		toInnerElement.put("iconUrl", iconUrl);
		to.add(toInnerElement);
		
		if (!onBehalfOfId.isEmpty()) {
			onBehalfofInnerElement.put("id", onBehalfOfId);
			onBehalfofInnerElement.put("name", onBehalfOfName);
			onBehalfofInnerElement.put("iconUrl", onBehalfOfIconUrl);
			onBehalfOf.add(onBehalfofInnerElement);
			toInnerElement.put("on_behalf_of", onBehalfOf);
		}
		else {
			toInnerElement.put("on_behalf_of", onBehalfOf);
		}
		

		toSystem.put("id", systemid);
		toSystem.put("name", systemName);
		  
		toInnerElement.put("system", toSystem);
		 
		  
		System.out.println(toInnerElement);

		 
		
		
		
		//to.add(system);
		//to.add(onBehalfofInnerElement);
		
		//onBehalfOf.add(onBehalfofInnerElement);
		//system.add(toSystem);
		//to.add(onBehalfOf);
		//to.add(system);
		swagger.put("to", to);
		
		return swagger.toString();
		
		
		
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public String getConsumerFromContent(String Id, String Name, String iconUrl, String onBehalfOfId, String onBehalfOfName, String onBehalfOfIconUrl, String systemid, String systemName ) {
		
		JSONObject swagger = new JSONObject();
		JSONObject fromInnerElement = new JSONObject();
		JSONObject onBehalfofInnerElement = new JSONObject();
		JSONObject toSystem = new JSONObject();
		JSONArray from = new JSONArray();
		JSONArray onBehalfOf = new JSONArray();
		
		fromInnerElement.put("id", Id);
		fromInnerElement.put("name", Name);
		fromInnerElement.put("iconUrl", iconUrl);
		from.add(fromInnerElement);
		
		if (!onBehalfOfId.isEmpty()) {
			onBehalfofInnerElement.put("id", onBehalfOfId);
			onBehalfofInnerElement.put("name", onBehalfOfName);
			onBehalfofInnerElement.put("iconUrl", onBehalfOfIconUrl);
			onBehalfOf.add(onBehalfofInnerElement);
			fromInnerElement.put("on_behalf_of", onBehalfOf);
		}
		else {
			fromInnerElement.put("on_behalf_of", onBehalfOf);
		}
		

		toSystem.put("id", systemid);
		toSystem.put("name", systemName);
		  
		fromInnerElement.put("system", toSystem);
		 
		  
		System.out.println(fromInnerElement);

		 
		
		
		
		//to.add(system);
		//to.add(onBehalfofInnerElement);
		
		//onBehalfOf.add(onBehalfofInnerElement);
		//system.add(toSystem);
		//to.add(onBehalfOf);
		//to.add(system);
		swagger.put("from", from);
		
		return swagger.toString();
		
		
		
		
		
		
	}
		
	
    public ArrayList<String> removeDuplicates(ArrayList<String> list)
    {
  
        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<String>();
  
        // Traverse through the first list
        for (String element : list) {
  
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
  
                newList.add(element);
            }
        }
        return newList;
       }
     
	
	
	public String getToken(String tokenUrl) {
		String token = null;
		
		//String tokenUrl ="http://Test-CDI-tfg-back-env.eba-nbxpadyk.ap-southeast-1.elasticbeanstalk.com/api/test-token" ;
		//String tokenUrl = "https://equatorial.integrate.afa-cdi.com/api/test-token" ;
		HttpHeaders tokenHeaders = new HttpHeaders();
		//tokenHeaders.set("Authorization", "Basic Y2RpQWRtaW46Y2RpQWRtaW4hMTIz");
		//tokenHeaders.add(token, tokenUrl);
		tokenHeaders.add("Authorization", "Basic Y2RpQWRtaW46Y2RpQWRtaW4hMTIz");
		
	   try {
		String jsonResponse =	connectionUtil.callGetRestService(tokenUrl,tokenHeaders);
		     TokenResponse response = new Gson().fromJson(jsonResponse, TokenResponse.class);		     
		     
		     
		     TokenResponse tokenresponse = new Gson().fromJson( response.getData().toString(), TokenResponse.class);
		     
		     token = tokenresponse.getToken();
		     
		     
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		return token;
	}
	
	
	
	
	public List<ExcelDataModel>  getEnterpriseData(String sheetName) throws Exception {
		
		
		//InputStream	inputStream = null;
	    //String sheetName = MasterData;
	  
	    File initialFile = new File(file_path);
	    InputStream inputStream =  new DataInputStream(new FileInputStream(initialFile));
	    
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Number of Rows in the Sheet:"+numberOfRows);
        List<ExcelDataModel> dataList = new ArrayList<ExcelDataModel>();
		while (rows.hasNext()) {
			
		  Row currentRow = rows.next();
		  int row_number =currentRow.getRowNum();
		  if(row_number == 0) {
			  continue;
			  
		  }
		 
     //
		  System.out.println("====================ROW  :"+row_number+"===================");
          ExcelDataModel model = new ExcelDataModel();

		  Iterator<Cell> cellsInRow = currentRow.iterator();

		  while (cellsInRow.hasNext()) {
			  
		     Cell currentCell = cellsInRow.next();    
		    if( currentCell.getColumnIndex() == 0) {
		    	 model.setEnterpriseSystemId(currentCell.getStringCellValue());
		     }
			if( currentCell.getColumnIndex() == 1) {
				model.setEnterpriseSystemName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 2) {
				model.setEnterpriseendPointUrl(currentCell.getStringCellValue());
			}
			
			if( currentCell.getColumnIndex() == 3) {
				 model.setEnterpriseS3BucketName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 4) {
				 model.setPublicKey(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 5) {
				 model.setEnterpriseOrgId(currentCell.getStringCellValue());
			}
		
			if( currentCell.getColumnIndex() == 6) {
				model.setEnterpriseSystemConfig(currentCell.getStringCellValue());
		
		     		  
		  }
		  
		  
		  
		  
		  
		  dataList.add(model);
		  //System.out.println(dataList.get(0).getSubscriberSystemName());
		  
		  		  
		}
		     
		workbook.close();
		
		}
		    
	    return dataList;
		
		
	
	
	}
	
	
	public List<ExcelDataModel>  getSubscriptionData(String sheetName) throws Exception {
		
		
		//InputStream	inputStream = null;
	    //String sheetName = MasterData;
	  
	    File initialFile = new File(file_path);
	    InputStream inputStream =  new DataInputStream(new FileInputStream(initialFile));
	    
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Number of Rows in the Sheet:"+numberOfRows);
        List<ExcelDataModel> subList = new ArrayList<ExcelDataModel>();
		while (rows.hasNext()) {
			
		  Row currentRow = rows.next();
		  int row_number =currentRow.getRowNum();
		  if(row_number == 0) {
			  continue;
			  
		  }
		 
     //
		  System.out.println("====================ROW  :"+row_number+"===================");
          ExcelDataModel submodel = new ExcelDataModel();

		  Iterator<Cell> cellsInRow = currentRow.iterator();

		  while (cellsInRow.hasNext()) {
			  
		     Cell currentCell = cellsInRow.next();    
		     if( currentCell.getColumnIndex() == 0) {
		    	 submodel.setSubscriptionId(currentCell.getStringCellValue());
		     }
			if( currentCell.getColumnIndex() == 1) {
				submodel.setSubscriberOrgId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 2) {
				submodel.setSubscriberSystemId(currentCell.getStringCellValue());
			}
			
			if( currentCell.getColumnIndex() == 3) {
				 submodel.setUseCaseId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 4) {
				 submodel.setDataElementId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 5) {
				 submodel.setProsumerOrgId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 6) {
				submodel.setProsumerSystemId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 7) {
				 submodel.setProsumerType(currentCell.getStringCellValue());
			}
			
		     
		
		     
		     
		  
		  }
		  
		  
		  
		  
		  
		  subList.add(submodel);
		  //System.out.println(dataList.get(0).());
		  
		  		  
		}
		     
		workbook.close();
		
		
		String  jSon1 = new Gson().toJson(subList.get(0));
		String  jSon2 = new Gson().toJson(subList.get(1));
		
		System.out.println(jSon1);
		jsonCompasre(jSon1,jSon2);
		
		JSONObject jsonObject = (JSONObject) GetJsonNode.GetNode(jsonfile_path);
	    System.out.println(jsonObject);
	    //System.out.println(jsonObject.get("age"));
	    
	    return subList;
	
		
		//System.out.println(new Gson().toJson(dataList));
	
	}
	
	
	public List<TestListDataModel>  getTestListData(String sheetName) throws Exception {
		
		
		//InputStream	inputStream = null;
	    //String sheetName = MasterData;
	  
	    File initialFile = new File(file_path);
	    InputStream inputStream =  new DataInputStream(new FileInputStream(initialFile));
	    
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Number of Rows in the Sheet:"+numberOfRows);
        List<TestListDataModel> testList = new ArrayList<TestListDataModel>();
		while (rows.hasNext()) {
			
		  Row currentRow = rows.next();
		  int row_number =currentRow.getRowNum();
		  if(row_number == 0) {
			  continue;
			  
		  }
		 
     //
		  System.out.println("====================ROW  :"+row_number+"===================");
		  TestListDataModel testmodel = new TestListDataModel();

		  Iterator<Cell> cellsInRow = currentRow.iterator();

		  while (cellsInRow.hasNext()) {
			  
		     Cell currentCell = cellsInRow.next();    
		     if( currentCell.getColumnIndex() == 0) {
		    	 testmodel.setTestSystemId(currentCell.getStringCellValue());
		     }
			if( currentCell.getColumnIndex() == 1) {
				testmodel.setTestSystemName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 2) {
				testmodel.setTestUrl(currentCell.getStringCellValue());
			}
			
		     
		  
		  }
		  
		  
		  
		  
		  
		  testList.add(testmodel);
		  //System.out.println(dataList.get(0).());
		  
		  		  
		}
		     
		workbook.close();
		
		

	    
	    return testList;
	
		
		//System.out.println(new Gson().toJson(dataList));
	
	}
	
	
	public List<ExcelDataModel>  getOrganizationData(String sheetName) throws Exception {
		
		
		//InputStream	inputStream = null;
	    //String sheetName = MasterData;
	  
	    File initialFile = new File(file_path);
	    InputStream inputStream =  new DataInputStream(new FileInputStream(initialFile));
	    
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("Number of Rows in the Sheet:"+numberOfRows);
        List<ExcelDataModel> orgList = new ArrayList<ExcelDataModel>();
		while (rows.hasNext()) {
			
		  Row currentRow = rows.next();
		  int row_number =currentRow.getRowNum();
		  if(row_number == 0) {
			  continue;
			  
		  }
		 
     //
		  System.out.println("====================ROW  :"+row_number+"===================");
		  ExcelDataModel orgmodel = new ExcelDataModel();

		  Iterator<Cell> cellsInRow = currentRow.iterator();

		  while (cellsInRow.hasNext()) {
			  
		     Cell currentCell = cellsInRow.next();    
		     if( currentCell.getColumnIndex() == 0) {
		    	 orgmodel.setOrgnizationOrgId(currentCell.getStringCellValue());
		     }
			if( currentCell.getColumnIndex() == 2) {
				orgmodel.setOrganizationName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 4) {
				orgmodel.setOrganizationType(currentCell.getStringCellValue());
			}
			
			if( currentCell.getColumnIndex() == 5) {
				orgmodel.setOrganizationIconUrl(currentCell.getStringCellValue());
			}
			
		     
		  
		  }
		  
		  
		  
		  
		  
		  orgList.add(orgmodel);
		  //System.out.println(dataList.get(0).());
		  
		  		  
		}
		     
		workbook.close();
		
		

	    
	    return orgList;
	
		
		//System.out.println(new Gson().toJson(dataList));
	
	}
	
	

	
	public void jsonCompasre(String jSon1,String jSon2) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
		
		Map<String, Object> leftMap = mapper.readValue(jSon1, type);
		Map<String, Object> rightMap = mapper.readValue(jSon2, type);
		
		Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
		Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);

		MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);

		System.out.println("Entries only on the left\n--------------------------");
		difference.entriesOnlyOnLeft()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries only on the right\n--------------------------");
		difference.entriesOnlyOnRight()
		          .forEach((key, value) -> System.out.println(key + ": " + value));

		System.out.println("\n\nEntries differing\n--------------------------");
		difference.entriesDiffering()
		          .forEach((key, value) -> System.out.println(key + ": " + value));
		
	}
	
	
	
}

	    
	

