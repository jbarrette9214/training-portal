package gui;

import java.sql.Connection;
import java.sql.SQLException;

import misc.sqlFunctions;

public class TestCreateDBUser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sqlFunctions sqlFunc = new sqlFunctions();
		CreateDbUser user = new CreateDbUser();
		Connection conn = sqlFunc.getDbConnection("SA", "");
		user.createWindow(conn);
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
