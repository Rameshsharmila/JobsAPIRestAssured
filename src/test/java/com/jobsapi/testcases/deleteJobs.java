package com.jobsapi.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jobsapi.base.excelDataProvider;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class deleteJobs {

	public static RequestSpecification httpRequest;
	public static Response response;
	
	@DataProvider (name="DeleteDataFromExcel")
	public String[] postDataFromExcel() throws IOException {
		String path = System.getProperty("user.dir") + "/src/utils/data.xlsx";
		
		int rowNum = excelDataProvider.getRowCount(path, "getjobs");
		int colNum = excelDataProvider.getCellCount(path, "getjobs", 1);
		
		String jobdata[] = new String[rowNum];
		
		
			for(int i=1; i<=rowNum; i++) {				
					jobdata[i-1] = excelDataProvider.getCellData(path, "getjobs", i, 6);
					System.out.println(jobdata[i-1]);
				
		}

		return jobdata;
	}
	
	@Test (dataProvider="DeleteDataFromExcel")
	public void deleteJobs(String Id) {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Id",Id);
		
		response = httpRequest.request(Method.DELETE);
		
		String responseBody = response.body().asString();	
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertFalse(response.body().asString().contains(Id));
	}
	
	@Test
	public void deleteInvalidJobId() {
		int invalidId = 6767;
		String message = invalidId +"' Job  not found.";
		
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Id",invalidId);
		
		response = httpRequest.request(Method.DELETE);
		
		String responseBody = response.body().asString();	
		Assert.assertEquals(response.getStatusCode(), 404);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 404 NOT FOUND");
		Assert.assertTrue(response.body().asString().contains(message));		
	
	}
}
