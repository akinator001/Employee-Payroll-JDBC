package com.cp.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {
	static String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	static String username = "root";
	static String password = "Ashutosh@23";

	public static void main(String[] args) {

		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");

		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find drivers", e);
		}
		listDrivers();

		try {
			System.out.println("Connecting to database: " + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Database Connection Successful " + connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void listDrivers() {

		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println("  " + driverClass.getClass().getName());
		}
	}
}
