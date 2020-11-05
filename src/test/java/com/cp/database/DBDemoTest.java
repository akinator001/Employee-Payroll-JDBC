package com.cp.database;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cp.database.EmployeePayrollService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DBDemoTest {
//	@Test
//	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		Assert.assertEquals(4, employeePayrollData.size());
//	}
//
//	@Test
//	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
//		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 3000000.00);
//		Assert.assertTrue(result);
//	}
//	
//	@Test
//	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		employeePayrollService.updateEmployeeSalaryUsingPrepareStatement("Terisa", 2000000.00);
//		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 2000000.00);
//		Assert.assertTrue(result);
//	}
//	
//	@Test
//	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		LocalDate startDate = LocalDate.of(2018, 01, 01);
//		LocalDate endDate = LocalDate.now();
//		List<EmployeePayrollData> employeePayrollData = employeePayrollService
//				.readEmployeePayrollForDateRange(IOService.DB_IO, startDate, endDate);
//		Assert.assertEquals(4, employeePayrollData.size());
//	}
//	
//	@Test
//	public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
//		boolean boolResult = averageSalaryByGender.get("M").equals(2000000.00) && averageSalaryByGender.get("F").equals(3000000.00); 
//		Assert.assertTrue(boolResult);
//				
//	}
	@Before
	public void Setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	private EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		System.out.println("EMPLOYEE PAYROLL ENTRIES IN JSONServer:\n" + response.asString());
		EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmps;
	}
	
	@Test
	public void givenEmployeeInJSONServer_whenRetrieved_ShouldMatchTheCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		long entries = employeePayrollService.countEntries();
		Assert.assertEquals(5, entries);
	}
	
//	@Test
//	public void givenNewEmployee_WhenAdded_ShouldMatch201ResponseAndCount() {
//		EmployeePayrollService employeePayrollService;
//		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
//		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
//		EmployeePayrollData employeePayrollData = new EmployeePayrollData(0, "Mark Zukerberg", 300000,
//				LocalDate.now());
//		Response response = addEmployeeToJsonServer(employeePayrollData);
//		int statusCode = response.getStatusCode();
//		Assert.assertEquals(201, statusCode);
//		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
//		employeePayrollService.addEmployeeToPayroll(employeePayrollData, IOService.REST_IO);
//		long entries = employeePayrollService.countEntries();
//		Assert.assertEquals(5, entries);
//	}

	private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}
	
//	@Test
//	public void givenListOfNewEmployee_whenAdded_ShouldMatch201ResponseAndCount() {
//		EmployeePayrollService employeePayrollService;
//		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
//		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
//		EmployeePayrollData[] arrayOfNewEmps = { new EmployeePayrollData(0, "Sunder", 600000.0, LocalDate.now()),
//				new EmployeePayrollData(0, "Mukesh", 100000.0, LocalDate.now()),
//				new EmployeePayrollData(0, "Anil", 200000.0, LocalDate.now()) };
//		for (EmployeePayrollData employeePayrollData : Arrays.asList(arrayOfNewEmps)) {
//			Response response = addEmployeeToJsonServer(employeePayrollData);
//			int statusCode = response.getStatusCode();
//			Assert.assertEquals(201, statusCode);
//			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
//			employeePayrollService.addEmployeeToPayroll(employeePayrollData, IOService.REST_IO);
//		}
//		long entries = employeePayrollService.countEntries();
//		Assert.assertEquals(8, entries);
//	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch200ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] ArrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(ArrayOfEmps));
		employeePayrollService.updateEmployeeSalary("Anil", 3000000.0, IOService.REST_IO);
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		Response response = request.put("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}
}
