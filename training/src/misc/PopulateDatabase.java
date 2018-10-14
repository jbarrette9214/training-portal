package misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PopulateDatabase {

	public PopulateDatabase() {
		sqlFunctions sqlFunc = new sqlFunctions();
		
		Connection conn = sqlFunc.getDbConnection("sa", "");
		System.out.println(conn.toString());
		
		try {
			if (!conn.isClosed() && conn != null) {
				//connection is valid
				
				//test for adding to department
 				String result = "";
				result = sqlFunc.addDepartment(conn, "nursing1");
				System.out.println(result);
				result = sqlFunc.addDepartment(conn, "nursing2");
				System.out.println(result);
				result = sqlFunc.addDepartment(conn, "nursing3");
				System.out.println(result);
				
				PreparedStatement stmt = conn.prepareStatement("select * from department");
				ResultSet rs = stmt.executeQuery();
				System.out.println(rs.toString());
				while (rs.next()) {
					String deptName = rs.getString("departmentName");
					int deptID = rs.getInt("ID");
					System.out.println(deptName + "  something  " +  deptID );
				}
				
				
				
				//test for adding to employee
				String result2;
				result2 = sqlFunc.addEmployee(conn, "Michelle", "Hill", 1, "RN");
				System.out.println(result2);
				result2 = sqlFunc.addEmployee(conn, "Alannah", "Chappell", 1, "LPN");
				System.out.println(result2);
				
				PreparedStatement stmt2 = conn.prepareStatement("select * from employee");
				ResultSet rs2 = stmt2.executeQuery();
				System.out.println(rs2);
				while(rs2.next()) {
					String first = rs2.getString("firstName");
					String last = rs2.getString("lastName");
					String dept = rs2.getString("departmentID");
					
					String deptName = "poop";

					//gets the name of the department using the id
					PreparedStatement stmt3 = conn.prepareStatement("select * from department " + 
							"where ID = " + dept);
					ResultSet rs3 = stmt3.executeQuery();
					System.out.println(rs3.toString());
					while(rs3.next()) {
						deptName = rs3.getString("departmentName");
					};
					
					String position = rs2.getString("position");
					
					System.out.println(first + " " + last + "   " + deptName + "   " + position);
				}

				

				
				
				
				//test the required_class_by_dept
				System.out.println("required classes test");
				String result40 = "";
				result40 = sqlFunc.addRequired(conn, 1, "nurs101", "basic nursing");
				System.out.println(result40);
				result40 = sqlFunc.addRequired(conn, 2, "basic1", "needed by everyone");
				System.out.println(result40);
				
				
				PreparedStatement stmt6 = conn.prepareStatement("select * from required_class_by_dept");
				ResultSet rs6 = stmt6.executeQuery();
				while(rs6.next()) {
					System.out.println(rs6.getInt("departmentID") + "  " + rs6.getString("classCode") +
							rs6.getString("description"));
				}
				
				System.out.println("after required");
				
				
				//test for adding class that are taken by an employee
				String result3 = "";
				result3 = sqlFunc.addClass(conn, "nurs101", 1, "12-25-2018");
				System.out.println(result3 + "first");
				
				
				String result4 = "";
				result4 = sqlFunc.addClass(conn, "basic1", 1, "11-12-2017");
				System.out.println(result4 + "second");

				
				
				//get the results from classes table
				System.out.println("classes");
				PreparedStatement stmt4 = conn.prepareStatement("select * from classes");
				ResultSet rs4 = stmt4.executeQuery();
				while(rs4.next()) {
					String code = rs4.getString("classCode");
					String date = rs4.getString("dateTaken");
					
					PreparedStatement stmt5 = conn.prepareStatement("select * from employee where " +
							"employeeID = " + rs4.getInt("employeeID"));
					ResultSet rs5 = stmt5.executeQuery();
					String name = "";
					while(rs5.next()) {
						name = rs5.getString("firstName") + " " + rs5.getString("lastName");
					}
					System.out.println(name + "    " + code + "    " + date);
				}

				
				
				
				
				//String temp = sqlFunc.deleteEmployee(conn, "alannah", "chappel", 1, "none");
				//System.out.println(temp);
				PreparedStatement stmt7 = conn.prepareStatement("select * from employee");
				ResultSet rs7 = stmt7.executeQuery();
				while(rs7.next()) {
					System.out.println(rs7.getString("firstName"));
				}
				
				
				
				
				
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
