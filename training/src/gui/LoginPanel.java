package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

import misc.sqlFunctions;
import training.Start;

public class LoginPanel extends JPanel {
	public LoginPanel(Start mainWindow) {
		this.setPreferredSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight() - 60));
		this.setLayout(null);
		this.setBackground(Color.lightGray);
		
		JLabel userLabel = new JLabel("User Name");
		userLabel.setSize(new Dimension(200, 50));
		userLabel.setFont(new Font("Serif", Font.BOLD, 25));
		userLabel.setLocation(mainWindow.getWidth()/2 - 200, 200);
		userLabel.setVisible(true);
		
		JTextField userText = new JTextField();
		userText.setSize(new Dimension(250, 30));
		userText.setFont(new Font("Serif", Font.PLAIN, 20));
		userText.setLocation(mainWindow.getWidth()/2 - 50 , 210);
		userText.setVisible(true);
		userText.setFocusable(true);
		
		
		JLabel passLabel = new JLabel("Password");
		passLabel.setSize(new Dimension(200, 50));
		passLabel.setFont(new Font("Serif", Font.BOLD, 25));
		passLabel.setLocation(mainWindow.getWidth()/2 - 200, 250);
		passLabel.setVisible(true);
		
		JPasswordField passField = new JPasswordField();
		passField.setSize(new Dimension(250, 30));
		passField.setLocation(mainWindow.getWidth()/2 - 50, 260);
		passField.setVisible(true);
		
		JLabel message = new JLabel("message");
		message.setSize(new Dimension(300, 50));
		message.setFont(new Font("Serif", Font.BOLD, 25));
		message.setForeground(Color.red);
		message.setLocation(mainWindow.getWidth()/2 - 150, 300);
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVisible(false);
		
		
		JButton login = new JButton("Login");
		login.setSize(new Dimension(80, 30));
		login.setLocation(mainWindow.getWidth()/2 - 30, 350);
		login.setBackground(new Color(238, 238, 238));
		login.setVisible(true);
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sqlFunctions  sqlFunc = new sqlFunctions();
				String pass = new String(passField.getPassword());
				Connection conn = sqlFunc.getDbConnection(userText.getText().toUpperCase(), pass);
				boolean admin = true;
				if(conn != null) {
					//check if admin or not
					try {
						PreparedStatement stmt = conn.prepareStatement("SET IGNORECASE TRUE");
						stmt.execute();
					} catch (SQLException e1) {
						//statement did not execute so no admin
						admin = false;
						System.out.println(e1);
					}
					String user = userText.getText();
					userText.setText("");
					passField.setText("");
					mainWindow.setConnection(conn, admin, user);
				}
			}
		});
		
		
		
		this.add(userLabel);
		this.add(userText);
		this.add(passLabel);
		this.add(passField);
		this.add(message);
		this.add(login);
		
	}
	
}
