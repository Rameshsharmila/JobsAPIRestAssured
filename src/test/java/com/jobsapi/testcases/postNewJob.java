package com.jobsapi.testcases;

import java.io.IOException;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jobsapi.base.BaseClass;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.jobsapi.base.excelDataProvider;



public class postNewJob {
	public static RequestSpecification httpRequest;
	public static Response response;

	

	@DataProvider (name="PostDataFromExcel")
	public String[][] postDataFromExcel() throws IOException {
		
		String path = System.getProperty("user.dir") + "/src/utils/data.xlsx";
		
		int rowNum = excelDataProvider.getRowCount(path, "getjobs");
		int colNum = excelDataProvider.getCellCount(path, "getjobs", 1);
		
		String jobdata[][] = new String[rowNum][colNum];
		
		for(int i=1; i<=rowNum; i++) {
			for(int j=0; j<colNum-1; j++) {
				jobdata[i-1][j] = excelDataProvider.getCellData(path, "getjobs", i, j);
			}
		}

		return jobdata;
	}
	
	@Test(dataProvider="PostDataFromExcel")
	public void postNewJobWithAllValidDetails(String Title, String CompanyName, String Location, String Type, String PostedTime, String desc, String Id, String t) {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title",Title);
		httpRequest.queryParam("Job Company Name",CompanyName);
		httpRequest.queryParam("Job Location",Location);
		httpRequest.queryParam("Job Type",Type);
		httpRequest.queryParam("Job Posted time",PostedTime);
		httpRequest.queryParam("Job Description",desc);
		httpRequest.queryParam("Job Id",Id);
		
		response = httpRequest.request(Method.POST);
		

		String responseBody = response.getBody().asString();
		String responseBodyjsonval = response.getBody().asString().replaceAll("NaN","\"1 hr\"").replaceAll("null","\"1 hr\"");
		
//		MatcherAssert.assertThat(responseBodyjsonval,JsonSchemaValidator.matchesJsonSchema("postjobsschema.json"));
		Assert.assertEquals(responseBody!=null,true);
		Assert.assertEquals(response.getStatusCode(), 200);
		

		Assert.assertTrue(responseBody.contains(Title));
		Assert.assertTrue(responseBody.contains(CompanyName));
		Assert.assertTrue(responseBody.contains(Location));
		Assert.assertTrue(responseBody.contains(Type));
		Assert.assertTrue(responseBody.contains(PostedTime));
		Assert.assertTrue(responseBody.contains(desc));
		Assert.assertTrue(responseBody.contains(Id));
	
	}
	
	@Test(dataProvider="PostDataFromExcel", dependsOnMethods={"postNewJobWithAllValidDetails"})
	public void postNewJobWithDuplicateID(String Title, String CompanyName, String Location, String Type, String PostedTime, String desc, String Id,String t) {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title",Title);
		httpRequest.queryParam("Job Company Name",CompanyName);
		httpRequest.queryParam("Job Location",Location);
		httpRequest.queryParam("Job Type",Type);
		httpRequest.queryParam("Job Posted time",PostedTime);
		httpRequest.queryParam("Job Description",desc);
		httpRequest.queryParam("Job Id",Id);
		
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		
		String message = Id +"' already exists";
		System.out.println("**************Status Code*************");
		Assert.assertEquals(response.getStatusCode(), 409);
		
		System.out.println("**************Status Line*************");
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 409 CONFLICT");
		
		System.out.println("**************Message*************");
		Assert.assertTrue(response.body().asString().contains(message));
		
	}
	
	@Test()
	public void postNewJobWithBlankJobId() {
		
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Description","desc");

		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Id\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));		
		
	}
	
	@Test()
	public void postNewJobWithBlankJobDesc() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Description\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}

	@Test()
	public void postNewJobWithBlankJobTitle() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Description","desc");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Title\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}
	
	@Test()
	public void postNewJobWithBlankCompanyName() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Description","desc");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Company Name\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}
	
	@Test()
	public void postNewJobWithBlankLocation() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Description","desc");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Location\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}
	
	@Test()
	public void postNewJobWithBlankJobType() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Posted time","PostedTime");
		httpRequest.queryParam("Job Description","desc");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Type\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}
	
	@Test()
	public void postNewJobWithBlankJobPostedTime() {
		RestAssured.baseURI = "https://jobs123.herokuapp.com/Jobs";
		httpRequest = RestAssured.given();
		httpRequest.queryParam("Job Title","Title");
		httpRequest.queryParam("Job Company Name","CompanyName");
		httpRequest.queryParam("Job Location","Location");
		httpRequest.queryParam("Job Type","Type");
		httpRequest.queryParam("Job Description","desc");
		httpRequest.queryParam("Job Id","Id");
		
		response = httpRequest.request(Method.POST);
		
		
		String responseBody = response.body().asString();
		String statusLine = response.getStatusLine();
		System.out.println(responseBody);
		System.out.println(statusLine);
		
		String message = "\"Job Posted time\": \"Missing required parameter in the JSON body or the post body or the query string\"";
		
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 BAD REQUEST");
		Assert.assertTrue(response.body().asString().contains(message));				
	}
	
	
	

}
