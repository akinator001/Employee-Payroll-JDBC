package com.cp.database;

import java.time.LocalDate;

public class EmployeePayrollData {
	int id;
	String name;
	double salary;
	LocalDate start;
	String gender;
	String department;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		public double getSalary() {
			return salary;
		}

		public void setSalary(double salary) {
			this.salary = salary;
		}

		public EmployeePayrollData(int id, String name, double salary) {
			this.id = id;
			this.name = name;
			this.salary = salary;
		}

		public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
			this(id, name, salary);
			this.start = startDate;
		}
		
		public EmployeePayrollData(int id, String name, double salary, LocalDate startDate,String gender,String department) {
			this(id, name, salary);
			this.start = startDate;
			this.gender=gender;
			this.department=department;
		}
		
		public LocalDate getStart() {
			return start;
		}

		public void setStart(LocalDate start) {
			this.start = start;
		}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", salary=" + salary + " ; ";
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmployeePayrollData that = (EmployeePayrollData) o;
		return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name);
	}

}