package gui;

import java.sql.Connection;
import java.sql.SQLException;

import misc.sqlFunctions;

public class TestNewClassWindow {

	public static void main(String[] args) {
		sqlFunctions sqlFunc = new sqlFunctions();
		Connection conn = sqlFunc.getDbConnection("sa", ""); 
		
		createNewClassWindow wind = new createNewClassWindow();
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
