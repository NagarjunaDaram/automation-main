package com.cdi.automation.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cdi.automation.model.ExcelDataModel;
import com.cdi.automation.model.TokenResponse;
import com.cdi.automation.util.ConnectionUtil;
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

@Service
public class CDIAutomationService {
	
	private static final Logger logger = LoggerFactory.getLogger(CDIAutomationService.class);
	
//	RestTemplate restTemplate;
	@Autowired
	ConnectionUtil connectionUtil;
	
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
	
	@Value("${MasterDataSheet}")
	public String MasterData;

	
	public  String functionaltest() {
		String response =null;
		logger.info("====================In Service========================");
		try {
			readExcel();
			
			
			String url ="http://Test-CDI-psa-back-env.eba-6irznnps.ap-southeast-1.elasticbeanstalk.com/api/v1/config" ;
			//String url ="https://equatorial.integrate.afa-cdi.com/api/v1/config" ;
			HttpHeaders requestHeaders = new HttpHeaders();
			//requestHeaders.set("Authorization", "Bearer ".concat(getToken()));
			requestHeaders.add("Authorization", "Bearer ".concat(getToken()));
		
		   String apiResponse =	connectionUtil.callGetRestService(url,  requestHeaders);
		
		
			
			System.out.println(apiResponse);
			
			
			
			response = apiResponse ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	public String getToken() {
		String token = "";
		
		String tokenUrl ="http://Test-CDI-tfg-back-env.eba-nbxpadyk.ap-southeast-1.elasticbeanstalk.com/api/test-token" ;
		//String tokenUrl = "https://equatorial.integrate.afa-cdi.com/api/test-token" ;
		HttpHeaders tokenHeaders = new HttpHeaders();
		tokenHeaders.set("Authorization", "Y2RpQWRtaW46Y2RpQWRtaW4hMTIz");
		tokenHeaders.add(token, tokenUrl);
		
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
	
	
	public void  readExcel() throws Exception {
		
		
		//InputStream	inputStream = null;
	    String sheetName = MasterData;
	  
	     File initialFile = new File(file_path);
	       InputStream inputStream =  new DataInputStream(new FileInputStream(initialFile));
	    
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rows = sheet.iterator();
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
		    	 model.setSuBScriberOrgName(currentCell.getStringCellValue());
		     }
			if( currentCell.getColumnIndex() == 1) {
				model.setSubscriberSystemId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 2) {
				model.setSubscriberSystemName(currentCell.getStringCellValue());
			}
			
			if( currentCell.getColumnIndex() == 3) {
				 model.setUseCaseId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 4) {
				 model.setDataElementId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 5) {
				 model.setProsumerOrgId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 6) {
				model.setProsumerOrgName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 7) {
				 model.setProsumerSystemId(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 8) {
				model.setProsumerSystemName(currentCell.getStringCellValue());
			}
			if( currentCell.getColumnIndex() == 9) {
				 
			}
			if( currentCell.getColumnIndex() == 10) {
				 
			}
		     
		
		     
		     
		  
		  }
		  
		  
		  
		  
		  
		  dataList.add(model);
		  System.out.println(dataList.get(0).getSubscriberSystemName());
		  
		  		  
		}
		     
		workbook.close();
		
		
		String  jSon1 = new Gson().toJson(dataList.get(0));
		String  jSon2 = new Gson().toJson(dataList.get(1));
		
		System.out.println(jSon1);
		jsonCompasre(jSon1,jSon2);
		
		JSONObject jsonObject = (JSONObject) GetJsonNode.GetNode(jsonfile_path);
	    System.out.println(jsonObject);
	    //System.out.println(jsonObject.get("age"));
	    
	    String res = TestReport.GenerateReport("MyFile.xlsx");
	
		
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
