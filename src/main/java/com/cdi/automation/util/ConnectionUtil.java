package com.cdi.automation.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;





@Component
public class ConnectionUtil {
	
	public String callPostRestService(String url,String requestInJson,HttpHeaders headers) throws HttpClientErrorException,Exception{
		String jSonresponse = null;
		if( url  != null && !url.isEmpty() ){
			RestTemplate restTemplate =new RestTemplate();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestInJson,headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,String.class);
			if (response.getStatusCodeValue() == 200) {
				jSonresponse =response.getBody();
			} else {
				throw new HttpClientErrorException(response.getStatusCode(),"URL :: "+url  + "requestEntity  :: "+requestEntity );
			}
		}
		
		return jSonresponse;
		
	}
	
	
	public String callGetRestService(String url,HttpHeaders headers) throws HttpClientErrorException,Exception{
		String jSonresponse = null;
		if( url  != null && !url.isEmpty() ){
			RestTemplate restTemplate =new RestTemplate();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			System.out.println(" API Name :: "+url);
			System.out.println(" Headers ::"+ requestEntity.toString());
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
			System.out.println(response.toString());
			if (response.getStatusCodeValue() == 200) {
				jSonresponse =response.getBody();
			} else {
				throw new HttpClientErrorException(response.getStatusCode(),"URL :: "+url  + "requestEntity  :: "+requestEntity  );
			}
		}
		
		return jSonresponse;
		
	}

}
