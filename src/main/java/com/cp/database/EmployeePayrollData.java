package com.cp.database;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double basic_pay;
	public LocalDate date;

	public EmployeePayrollData(int id, String name, double basic_pay, LocalDate date) {
		this.id = id;
		this.name = name;
		this.basic_pay = basic_pay;
		this.date = date;
	}

	@Override
	public String toString() {
		return " id:" + id + " name:" + name + " basic_pay:" + basic_pay + " startdate:" + date;
	}
}