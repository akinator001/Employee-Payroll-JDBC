package com.cp.database;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {
	public enum IOService {
		DB_IO,REST_IO;
	};

	private static List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		EmployeePayrollService.employeePayrollList =  new ArrayList<>(employeePayrollList);
	}

	public static long countEntries() {
		return employeePayrollList.size();
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			EmployeePayrollService.employeePayrollList = employeePayrollDBService.readData();
		}
		return EmployeePayrollService.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
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

	public void printEmployeeData(EmployeePayrollService.IOService ioService) {

		System.out.println(employeePayrollList);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name, double salary) {
		for (EmployeePayrollData data : employeePayrollList) {
			if (data.getName().equals(name)) {
				if (Double.compare(data.getSalary(), salary) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeDataUsingPreparedStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService dbIo, LocalDate startDate,
			LocalDate endDate) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getEmployeeForGivenDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getAverageSalaryByGender();
		}
		return null;
	}

	public void addEmployeeToPayroll(int id, String name, double salary, LocalDate startDate, String gender,
			String department) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployee(id, name, salary, startDate,
				gender, department);
		employeePayrollList.add(employeePayrollData);
		System.out.println(employeePayrollList);
	}

	public void addEmployeeToPayrollERDiagram(int id, String name, double salary, LocalDate startDate, String gender,
			String department, String phone, String address) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployeeToPayrollERDiagram(id, name,
				salary, startDate, gender, department, phone, address);
		employeePayrollList.add(employeePayrollData);
	}

	public void removeEmployee(String name) {
		employeePayrollDBService.removeEmployee(name);
	}

	public void addEmployeeToPayrollWithoutThreads(List<EmployeePayrollData> employeePayrollList) {
		employeePayrollList.forEach(employeePayrollData -> {
			System.out.println("Employee being added : " + employeePayrollData.getName());
			this.addEmployeeToPayroll(employeePayrollData.id, employeePayrollData.getName(),
					employeePayrollData.getSalary(), employeePayrollData.start, employeePayrollData.gender,
					employeePayrollData.department);
			System.out.println("Employee added : " + employeePayrollData.getName());
		});
	}

	public void addEmployeesToPayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				this.addEmployeeToPayroll(employeePayrollData.id, employeePayrollData.name, employeePayrollData.salary,
						employeePayrollData.start, employeePayrollData.gender, employeePayrollData.department);
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
				System.out.println("Employee Added: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		System.out.println(employeePayrollDataList);

	}

	public void addEmployeesToERDBWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				this.addEmployeeToPayrollERDiagram(employeePayrollData.id, employeePayrollData.name,
						employeePayrollData.salary, employeePayrollData.start, employeePayrollData.gender,
						employeePayrollData.department, employeePayrollData.phone, employeePayrollData.address);
				System.out.println("Employee Added: " + Thread.currentThread().getName());
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void UpdateEmployeeDataInERDBWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Updated: " + Thread.currentThread().getName());
				this.updateEmployeeSalary(employeePayrollData.getName(), employeePayrollData.getSalary());
				System.out.println("Employee Updated: " + Thread.currentThread().getName());
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	
	public void addEmployeeToPayroll(EmployeePayrollData employeePayrollData, IOService ioService) {
			employeePayrollList.add(employeePayrollData);
	}
}