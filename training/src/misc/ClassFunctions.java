package misc;

import java.sql.*;

public class ClassFunctions {
	
	//returns the description of a class
	//
	//parameters: Connection, the code of the class, the int of the department
	//
	//returns: String
	public String getDescriptionFromCode(Connection conn, String code, int department) {
		String classDesc = "";
		try {
			String sql = "select description from required_class_by_dept where departmentID = " +
					department + " and classCode = '" + code + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				classDesc = rs.getString("description");
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		return classDesc;
	}
	
	public String getDescriptionFromCode(Connection conn, String code) {
		String classDesc = "";
		try {
			String sql = "select description from required_class_by_dept where classCode = '" + code + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				classDesc = rs.getString("description");
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		return classDesc;
	}
	
	
}
