package com.cp.database;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DBDemoTest {
	@Test
	public void GivenEntriesInDataBaseReturnCount() {
		DBDemo dbdemo = new DBDemo();
		List<EmployeePayrollData> empdata = dbdemo.readDataFromDB();
		Assert.assertEquals(4, empdata.size());
	}
}
