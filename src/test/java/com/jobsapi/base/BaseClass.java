package com.jobsapi.base;

import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	
	public static RequestSpecification httpRequest;
	public static Response response;
	
	@BeforeClass
	public void setUp() {
		
		
	}

}
