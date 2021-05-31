package com.cdi.automation.util;
import com.github.javafaker;

public class fakeData {
	public void givenValidService_whenRegexifyCalled_checkPattern() throws Exception {

	    FakeValuesService fakeValuesService = new FakeValuesService(
	      new Locale("en-GB"), new RandomService());

	    String alphaNumericString = fakeValuesService.regexify("[a-z1-9]{10}");
	    Matcher alphaNumericMatcher = Pattern.compile("[a-z1-9]{10}").matcher(alphaNumericString);
	 
	    assertTrue(alphaNumericMatcher.find());
	}

}
