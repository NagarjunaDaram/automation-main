package com.cdi.automation.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;



public class CdiUtilService {
	
	RestTemplate Rest;
    
	public <T> Object callGenaricPostRestService(String url,String requestInJson,HttpHeaders headers,Class<T> responseType) throws HttpClientErrorException,Exception{
		
		
		return null;
		
	}
    
    

}
