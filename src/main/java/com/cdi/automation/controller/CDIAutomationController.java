package com.cdi.automation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cdi.automation.service.CDIAutomationService;



@RestController
@RequestMapping("/cdiautomation")
public class CDIAutomationController {
	private static final Logger logger = LoggerFactory.getLogger(CDIAutomationController.class);
	@Autowired
	public CDIAutomationService cdiAutomationService;
	
	@RequestMapping(value = "/functionaltest", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE, headers = {"Accept=application/json"})
	public @ResponseBody String functionaltest(HttpServletRequest httpRequest) {
		String response =null;
		try {
			logger.info("preparing public keys at :: "+new Date());
			 response = cdiAutomationService.functionaltest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}	
	
	
}




