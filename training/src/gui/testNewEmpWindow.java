package gui;

import java.sql.*;
import misc.sqlFunctions;

public class testNewEmpWindow {
	public static void main(String[] args) {
		sqlFunctions sqlFunc = new sqlFunctions();
		Connection conn = sqlFunc.getDbConnection("sa", ""); 
		
		createNewEmployeeWindow wind = new createNewEmployeeWindow();
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
