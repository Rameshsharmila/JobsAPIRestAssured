package com.jobsapi.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jobsapi.base.BaseClass;

import io.restassured.RestAssured;
import io.restassured.http.Method;

import org.hamcrest.MatcherAssert;
import io.restassured.module.jsv.JsonSchemaValidator;

/**
 * @author Sharmila
 *
 */
public class getAllJobDetails extends BaseClass{
	
	
	@Test() 
	public void getJobs() {
		
		RestAssured.baseURI = "https://jobs123.herokuapp.com";
		httpRequest = RestAssured.given();
		response = httpRequest.request(Method.GET,"/Jobs");
		String responseBody = response.body().asString();
		
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertTrue(responseBody !=null);
		
		String responseBodyjsonval = response.getBody().asString().replaceAll("NaN","\"1 hr\"").replaceAll("null","\"1 hr\"");
//		MatcherAssert.assertThat(responseBodyjsonval,JsonSchemaValidator.matchesJsonSchema("getjobsschema.json"));

				
	}
	
	

}
