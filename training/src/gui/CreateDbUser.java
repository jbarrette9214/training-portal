//creates a window to add a user to the database

package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.*;

import javax.swing.JDialog;

public class CreateDbUser extends JDialog{
	public void createWindow(Connection conn) {
		JDialog window = this;
		
		window.setSize(400, 200);
		window.setLocationRelativeTo(null);
		window.setTitle("Create User");
		window.setBackground(Color.blue);
		window.setLayout(null);
		window.setResizable(false);
		window.setModal(true);
		
		JLabel loginLabel = new JLabel("Login");
		loginLabel.setSize(50, 25);
		loginLabel.setLocation(10,10);
		loginLabel.setVisible(true);
		window.add(loginLabel);
		
		JTextField loginText = new JTextField();
		loginText.setSize(150, 25);
		loginText.setLocation(90, 10);
		loginText.setVisible(true);
		window.add(loginText);
		
		JLabel message1 = new JLabel("Required");
		message1.setSize(75, 25);
		message1.setLocation(250, 10);
		message1.setForeground(Color.red);
		message1.setVisible(false);
		window.add(message1);
		
		JLabel passLabel = new JLabel("Password");
		passLabel.setSize(75, 25);
		passLabel.setLocation(10, 40);
		passLabel.setVisible(true);
		window.add(passLabel);
		
		JPasswordField pass1 = new JPasswordField();
		pass1.setSize(150, 25);
		pass1.setLocation(90, 40);
		pass1.setVisible(true);
		window.add(pass1);
		
		JLabel message2 = new JLabel("Must match");
		message2.setSize(125, 50);
		message2.setLocation(250, 40);
		message2.setForeground(Color.red);
		message2.setVisible(false);
		window.add(message2);
		
		JLabel reenterLabel = new JLabel("Re-enter");
		reenterLabel.setSize(75, 25);
		reenterLabel.setLocation(10, 70);
		reenterLabel.setVisible(true);
		window.add(reenterLabel);
		
		JPasswordField pass2 = new JPasswordField();
		pass2.setSize(150, 25);
		pass2.setLocation(90, 70);
		pass2.setVisible(true);
		window.add(pass2);
		
		JCheckBox admin = new JCheckBox();
		admin.setSize(25, 25);
		admin.setLocation(10, 100);
		admin.setSelected(false);
		admin.setVisible(true);
		window.add(admin);
		
		JLabel adminLabel = new JLabel("Grant this user admin rights");
		adminLabel.setSize(200, 25);
		adminLabel.setLocation(40, 100);
		adminLabel.setVisible(true);
		window.add(adminLabel);
		
		
		
		JButton create = new JButton("Create");
		create.setSize(100, 30);
		create.setLocation(75, 135);
		create.setBackground(new Color(238, 238, 238));
		create.setVisible(true);
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//check and make sure all fields are ok
				message1.setVisible(false);
				message2.setVisible(false);
				if(loginText.getText().isEmpty()) {
					message1.setVisible(true);
				} else if (pass1.getPassword().length < 6){ 
					message2.setText("Must be 6 characters");
					message2.setVisible(true);
				} else if (!Arrays.equals(pass1.getPassword(), pass2.getPassword())) {
					message2.setText("Does not match");
					message2.setVisible(true);
				} else {
					try {
						String password = new String(pass1.getPassword());
						String sql = "CREATE USER " + loginText.getText().toUpperCase() + " PASSWORD '" +
								password + "'";
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						
						if(admin.isSelected()) {
							//set as admin
							sql = "ALTER USER '" + loginText.getText().toUpperCase() + "' ADMIN TRUE";
							System.out.println("admin");
						} else {
							//not admin so grant only select priveledges
							sql = "GRANT SELECT ON EMPLOYEE TO " + loginText.getText().toUpperCase();
							stmt = conn.prepareStatement(sql);
							stmt.executeUpdate();
							sql = "GRANT SELECT ON DEPARTMENT TO " + loginText.getText().toUpperCase();
							stmt = conn.prepareStatement(sql);
							stmt.executeUpdate();
							sql = "GRANT SELECT ON REQUIRED_CLASS_BY_DEPT " + loginText.getText().toUpperCase();
							stmt = conn.prepareStatement(sql);
							stmt.executeUpdate();
							sql = "GRANT SELECT ON CLASSES " + loginText.getText().toUpperCase();
							stmt = conn.prepareStatement(sql);
							stmt.executeUpdate();
						}
					} catch(SQLException e) {
						System.out.println(e);
					}
					window.dispose();
				
				}
				
				
			}
		});
		
		window.add(create);
		
		JButton cancel = new JButton("Cancel");
		cancel.setSize(100, 30);
		cancel.setLocation(225, 135);
		cancel.setBackground(new Color(238, 238, 238));
		cancel.setVisible(true);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.dispose();
			}
		});
		
		
		window.add(cancel);
		
		
		
		window.setVisible(true);
	}
	
	
	
}
