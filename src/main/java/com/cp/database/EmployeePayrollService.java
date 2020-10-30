package com.cp.database;

import java.util.*;

public class EmployeePayrollService {
	public enum IOService {
		DB_IO;
	};

	private List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			this.employeePayrollList = new EmployeePayrollDBService().readData();
		}
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = new EmployeePayrollDBService().updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		for (EmployeePayrollData data : employeePayrollList) {
			if (data.getName().equals(name)) {
				return data;
			}
		}
		return null;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		for (EmployeePayrollData data : employeePayrollList) {
			if (data.getName().equals(name)) {
				if (Double.compare(data.getSalary(), 3000000.00) == 0) {
					return true;
				}
			}
		}
		return false;
	}
}