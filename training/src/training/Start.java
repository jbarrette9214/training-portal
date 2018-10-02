//database is saved under users/userName/staffDevelopment.mv


package training;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;


import javax.swing.*;

import gui.*;
import misc.*;


public class Start extends JFrame{

	public static void main(String[] args) {
		Path path = Paths.get(System.getProperty("user.home")).resolve("staffDevelopment.mv.db");
		File file = new File(path.toString());
		if(!file.exists()) {
			createDatabase create = new createDatabase();
			
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Database not found.  Would you like to create?"," ", dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION) {
				create.createNewDatabase();
			} else {
				System.exit(0);
			}
			
			//below temporary just for testing purposes
			System.out.println("in start.java main function, remove");
			PopulateDatabase populate = new PopulateDatabase();

			//need to open the database
			sqlFunctions sqlFunc = new sqlFunctions();
			Connection conn2 = sqlFunc.getDbConnection("sa", "");
			
			//add something to change the user name and password
			SetPasswordDialog setupUser = new SetPasswordDialog(conn2, null);
		}

		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initialize();
			}
		});
		
	}
	
	public static void initialize() {
		//setup main window
		
		//Start mainWindow = new Start();
		Toolkit kit = mainWindow.getToolkit();
		Dimension wndSize = kit.getScreenSize();
		mainWindow.setTitle("Staff Development Portal");
		mainWindow.setSize(new Dimension(1250, 750));
		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setVisible(true);
		
		//create and add the menu bar
		menuBar = new MyMenu(mainWindow);	
		mainWindow.setJMenuBar(menuBar);
		
		//create and add the navigation panel
		nav = new navPanel(mainWindow);
		nav.setVisible(true);
		
		login = new LoginPanel(mainWindow);
		
		
		//the main panel where things go
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight() - 65));
		mainPanel.setLayout(null);
		mainPanel.setBackground(new Color(235, 220, 239));
		mainPanel.setVisible(true);
		mainPanel.setLayout(card);
		
//		empSearch = new EmployeeSearch(mainWindow);

		//add panels to the mainWindow
		mainWindow.add(nav, BorderLayout.NORTH);
		mainWindow.add(mainPanel, BorderLayout.CENTER);


		//add all the cards to the mainpanel
//		mainPanel.add("employeeSearch", empSearch);
		mainPanel.add("login", login);
		
		//uncomment these to have a login
		if(conn == null) {
			mainPanel.add("login", login);
			card.show(mainPanel, "login");
		}

		
		//only for testing remove after that
//		card.show(mainPanel, "employeeSearch");
		
		//listener for when window closes so that the db can be closed
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(conn != null) {
					try {
						conn.close();
					} catch (SQLException m) {
						//do nothing here
					}
					System.exit(0);
				}
			}
		});
		
	}
	
	public Connection getOpenedConnection() {
		return conn;
	}
	
	public void setConnection(Connection connect, boolean admin, String userName) {
		conn = connect;
		
		menuBar.setConnection(conn);
		
		
		username = userName;
		card.show(mainPanel, "employee");

		
		//if admin is true have to set the setEnabled to true
		if(admin == true) {
			menuBar.setAdmin();
			mainWindow.setTitle("Staff Development Portal:   connected as admin");
		} else {
			mainWindow.setTitle("Staff Development Portal:   read only access");
		}
		
		empSearch = new EmployeeSearch(mainWindow);
		mainPanel.add("empSearch", empSearch);
		
		deptSearch = new DepartmentSearch(mainWindow);
		mainPanel.add("deptSearch", deptSearch);
		
		editEmpPanel = new EditEmployeePanel(mainWindow);
		mainPanel.add("editEmployee", editEmpPanel);
		
		card.show(mainPanel, "empSearch");

	}

	public void setPaneltoLogin() {
		card.show(mainPanel, "login");
	}
	
	public void changePanel(String panel) {
		card.show(mainPanel, panel);
		if(panel.equals("empSearch")) {
			empSearch.focusGained(null);
		} else if (panel.equals("deptSearch")) {
			deptSearch.focusGained(null);
		} else if (panel.equals("editEmployee")) {
			editEmpPanel.focusGained(null);
		}
	}
	
	public String getUserName() {
		return username;
	}
	
	
	private static Connection conn = null;
	private static Start mainWindow = new Start();
	private static navPanel nav;
	private static MyMenu menuBar;
	private static JPanel mainPanel;
	private static LoginPanel login;
	private static EmployeeSearch empSearch;
	private static DepartmentSearch deptSearch;
	private static EditEmployeePanel editEmpPanel;
	private static CardLayout card = new CardLayout();
	
	private static String username, password;
}
