package misc;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class employeeFunctions {

	//gets the employees name from the id number
	//
	//parameters:  Connection    int id
	//
	//return: lastName + ", " + firstName as a single string;
	public String getEmployeeName(Connection conn, int id) {
		String name = "";
		
		try {
			String sql = "select * from employee where employeeID = '" + id + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				name = rs.getString("lastName") + ", " + rs.getString("firstName");
			}
		}catch (SQLException e) {
			System.out.println(e);
		}

		return name;
	}
	
	
	
	
	
	//gets the id number of an employee
	//
	//parameters: Connection    String for firstName    String for lastName
	//
	//return: int of the id	
	public int getEmployeeID(Connection conn, String firstName, String lastName) {
		int id = -1;
		
		try {
			String sql = "select * from employee where lastName = '" + lastName + "' and " +
					"firstName = '" + firstName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				id = rs.getInt("employeeID");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
			
		return id;
	}
	
	
	
	
	
	//gets the employees position by id
	//
	//parameters:   Connection    Int
	//
	//return: String that is the position of the employee
	public String getEmployeePositionByID(Connection conn, int id) {
		String position = "";
		
		try {
			String sql = "select position from employee where employeeID = '" + id + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				position = rs.getString("position");
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		return position;
	}
	
	
	
	
	
	
	//returns the employees position when passed the name
	//
	//parameters: Connection   String for first name    String for last name
	//
	//return:  String that is the position of the employee
	public String getEmployeePositionByName(Connection conn, String lastName, String firstName) {
		String position = "";
		
		try {
			String sql = "select position from employee where lastName = '" + lastName +
					"' and firstName = '" + firstName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				position = rs.getString("position");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return position;
	}
	
	
	
	
	
	
	//returns the employees department when passed the employeeID
	//
	//parameters:   Connection     Int for employeeID
	//
	//Return: String for the department
	public String getEmployeeDepartmentByID(Connection conn, int id) {
		String department = "";
		
		try {
			String sql = "select departmentID from employee where employeeID = " + id;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int temp = -1;
			while(rs.next()) {
				temp= rs.getInt("departmentID");
			}
			
			//use the int from departmentID to search the department table
			sql = "select departmentName from department where id = " + temp;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				department = rs.getString("departmentName");
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		return department;
	}
	
	
	
	
	//returns the employees department when passed name
	//
	//parameters: Connection String fistName String lastName
	//
	//return:  String that is the department name
	public String getEmployeeDepartmentByName(Connection conn, String lastName, String firstName) {
		String department = "";
		
		try {
			String sql = "select departmentID from employee where lastName = '" + lastName + 
					"' and firstName = '" + firstName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int temp = -1;
			while(rs.next()) {
				temp= rs.getInt("departmentID");
			}
			
			//use the int from departmentID to search the department table
			sql = "select departmentName from department where id = " + temp;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				department = rs.getString("departmentName");
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		return department;
	}

	public int getEmployeesDepartmentID(Connection conn, int employeeID) {
		int deptID = -1;
		
		try {
			String sql = "select departmentID from employee where employeeID = " + employeeID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				deptID = rs.getInt("departmentID");
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		return deptID;
	}
	
	public List<String> getEmployeesDontHaveClass(Connection conn, String code, int dept) {
		List<String> list = new ArrayList<String>();
		String sql = "select * from employee where departmentID = " + dept;
		
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			
			String sql2 = "select * from classes where classcode = '" + code + "'";
			Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs2 = stmt2.executeQuery(sql2);
			
			while(rs.next()) {
				boolean has = false;
				String text;
				while(rs2.next() && has == false) {
					if(rs2.getInt("employeeID") == rs.getInt("employeeID")) {
						has = true;
					}
				}
				if(has == false) {
					
					text = rs.getString("lastName") + ", " + rs.getString("firstName") + 
							"     " + getEmployeePositionByID(conn, rs.getInt("employeeID")) +
							"     " + getEmployeeDepartmentByID(conn, rs.getInt("employeeID"));
					list.add(text);
				}
			}
			
		
		} catch(SQLException e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	
}
