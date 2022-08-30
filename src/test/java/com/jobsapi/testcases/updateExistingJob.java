package com.jobsapi.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jobsapi.base.BaseClass;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.jobsapi.base.excelDataProvider;



public class updateExistingJob {
	public static RequestSpecification httpRequest;
	public static Response response;

	

	@DataProvider (name="PutDataFromExcel")
	public String[][] postDataFromExcel() throws IOException {
		
		String path = System.getProperty("user.dir") + "/src/utils/data.xlsx";
		
		int rowNum = excelDataProvider.getRowCount(path, "getjobs");
		int colNum = excelDataProvider.getCellCount(path, "getjobs", 1);
		
		String jobdata[][] = new String[rowNum][colNum];
		
		for(int i=1; i<=rowNum; i++) {
			for(int j=0; j<colNum; j++) {		
				jobdata[i-1][j] = excelDataProvider.getCellData(path, "getjobs", i, j);
				System.out.println(jobdata[i-1][j]);
				
			}
			
		}
		
		return jobdata;
	}
	
	
	@Test(dataProvider="PutDataFromExcel", enabled=true)
	public void putNewJobWithAllValidDetails(String Title, String CompanyName, String Location, String Type, String PostedTime, String desc, String Id,String updatedTitle) {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title",updatedTitle);
		httpRequest.queryParam("Job Id",Id);
		
		response = httpRequest.request(Method.PUT);
		
		String responseBody = response.body().asString();	
		Assert.assertEquals(response.getStatusCode(), 200);
	
	
		Assert.assertTrue(responseBody.contains(updatedTitle));
		Assert.assertTrue(responseBody.contains(Id));
		
	}
	
	@Test
	public void putJobInvalidJobId() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","updatedTitle");
		httpRequest.queryParam("Job Id","invalidid");
		
		response = httpRequest.request(Method.PUT);
		
		String responseBody = response.body().asString();	
		Assert.assertEquals(response.getStatusCode(), 404);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 404 NOT FOUND");
		
	}
	

	@Test
	public void putJobBlankJobId() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","updatedTitle");
		
		response = httpRequest.request(Method.PUT);
		
		String responseBody = response.body().asString();	
		String message = "\"Job Id\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));		
		
		
	}

}
