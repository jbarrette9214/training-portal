package gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import misc.sqlFunctions;
import training.Start;

public class MyMenu extends JMenuBar{

	public MyMenu(Start mainWindow) {
		
		//Connection conn = mainWindow.getOpenedConnection();
		
		
//file menu item
		fileMenu = new JMenu("File  ");
		fileMenu.setMnemonic(KeyEvent.VK_F);	//alt + f
		
		JMenuItem logoutFile = new JMenuItem("Log Out");
		fileMenu.add(logoutFile);
		logoutFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				mainWindow.setPaneltoLogin();
			}
		});
			
		fileMenu.addSeparator();
		JMenuItem exitFile = new JMenuItem("Exit");
		fileMenu.add(exitFile);
		exitFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					conn.close();
				} catch (SQLException e) {
					//do nothing
				}
				System.exit(0);
			};
		});
		
		
		
//create menu item		
		createMenu = new JMenu("Create");
		fileMenu.setMnemonic(KeyEvent.VK_C);
		JMenuItem empCreate = new JMenuItem("Create New Employee");
		createMenu.add(empCreate);
		empCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createNewEmployeeWindow emp = new createNewEmployeeWindow();
				emp.createWindow(conn);
			}
		});

		JMenuItem classCreate = new JMenuItem("Create New Class");
		createMenu.add(classCreate);
		classCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createNewClassWindow newClass = new createNewClassWindow();
				newClass.createWindow(conn);
			}
		});

		JMenuItem departmentCreate = new JMenuItem("Create New Department");
		createMenu.add(departmentCreate);
		departmentCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				CreateNewDepartment newDept = new CreateNewDepartment();
				newDept.createWindow(conn);			}
		});

		
		
		
		
//edit menu
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		JMenuItem employeeEdit = new JMenuItem("Employee");
		editMenu.add(employeeEdit);
		employeeEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
//code for listener
				
			}
		});
		
		JMenuItem deptEdit = new JMenuItem("Department Requirements");
		editMenu.add(deptEdit);
		deptEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
//code for listener
				
			}
		});
		
		
		setupMenu = new JMenu("Setup");
		setupMenu.setMnemonic(KeyEvent.VK_S);
		JMenuItem changePassSetup = new JMenuItem("Change Password");
		setupMenu.add(changePassSetup);
		changePassSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SetPasswordDialog changePass = new SetPasswordDialog(conn, mainWindow.getUserName());
			}
		});
		
		
		//turn things off for not admin priveledges
		createMenu.setEnabled(false);
		editMenu.setEnabled(false);
		
		
		
		this.add(fileMenu);
		this.add(createMenu);
		this.add(editMenu);
		this.add(setupMenu);
	}
	
	public void setAdmin() {
		createMenu.setEnabled(true);
		editMenu.setEnabled(true);
		
		int temp = setupMenu.getItemCount();
		
		JMenuItem tempItem;
		
		for(int i = 0; i < temp; ++i) {
			tempItem = setupMenu.getItem(i);
			if(tempItem.getName() != null && tempItem.getName().equals("initialSetup")) {
				tempItem.setEnabled(true);
			}
		}
		
		temp = fileMenu.getItemCount();
		
	}
	
	public void setConnection(Connection connection) {
		conn = connection;
	}
	
	JMenu fileMenu;
	JMenu createMenu;
	JMenu editMenu;
	JMenu setupMenu;
	
	Connection conn;
}
