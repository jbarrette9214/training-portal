package gui;

import java.sql.Connection;
import java.sql.SQLException;

import misc.sqlFunctions;

public class TestNewDepartmentWindow {
	public static void main(String[] args) {
		sqlFunctions sqlFunc = new sqlFunctions();
		Connection conn = sqlFunc.getDbConnection("mhill", "nursing1"); 
		
		CreateNewDepartment wind = new CreateNewDepartment();
		wind.createWindow(conn);
		wind.dispose();
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	
	
}
