package gui;

import java.sql.Connection;

public class testSetPasswordDialog {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection conn = null;
		SetPasswordDialog setDialog = new SetPasswordDialog(conn, null);
		setDialog.dispose();
	}

}
