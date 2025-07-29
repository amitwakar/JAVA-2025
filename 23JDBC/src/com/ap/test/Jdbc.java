package com.ap.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/organization", "root" , "#1Sanikamit");
		System.out.println("Connection to Database successfully");
		
		
		String insertQ = "insert into students(roll_no,name) values(?, ?)";
		PreparedStatement ps = connection.prepareStatement(insertQ);
		ps.setString(1, "45");
		ps.setString(2, "Amit");
		ps.execute();

		
		ps.setString(1, "45");
		ps.setString(2, "Ajay");
		ps.execute();

		
		
		ps.execute();
		System.out.println("Row Inserted Successfully");
		
		
		String selectQ = " select * from students";
		
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(selectQ);
		
		while (rs.next()) {
			System.out.println(rs.getInt("roll_no")+ "- "+ rs.getString("name"));

		}
		
		
		String updateQ = "update students set name = ? where roll_no = ?";
		PreparedStatement ps2 = connection.prepareStatement(updateQ);
		ps2.setString(1, "Arti");
		ps2.setInt(2, 45);
		int updatedrows = ps2.executeUpdate();
		System.out.println(updatedrows);
		
		
		String deleteQ = "delete from students where roll_no = ?";
		PreparedStatement ps3 = connection.prepareStatement(deleteQ);
		ps3.setInt(1, 45); 
		int deletedRows = ps3.executeUpdate();
		System.out.println(deletedRows + " row(s) deleted.");

		
		
				
		
		
		connection.close();

		
		
		
		
		
		
		
		
		
		
		
		
	}
	

}
