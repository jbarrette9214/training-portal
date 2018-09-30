package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SetPasswordDialog extends JDialog{

	public SetPasswordDialog(Connection conn) {
		JDialog window = this;
		
		window.setModal(true);
		window.setSize(600, 250);
		window.setTitle("Set User Name and Passord");
		window.setLocationRelativeTo(null);
		window.setLayout(null);
		
		JLabel name = new JLabel("User Name");
		name.setSize(200, 30);
		name.setLocation(10,5);
		name.setFont(font1);
		name.setVisible(true);
		window.add(name);
		
		JTextField nameField = new JTextField(25);
		nameField.setLocation(200, 5);
		nameField.setSize(250, 30);
		nameField.setVisible(true);
		window.add(nameField);
		
		JLabel pass1 = new JLabel("Password");
		pass1.setSize(150, 30);
		pass1.setLocation(10, 45);
		pass1.setFont(font1);
		pass1.setVisible(true);
		window.add(pass1);
		
		JPasswordField password1 = new JPasswordField();
		password1.setSize(250, 30);
		password1.setLocation(200, 45);
		password1.setVisible(true);
		window.add(password1);
		
		JLabel pass2 = new JLabel("Re-Type Password");
		pass2.setSize(200, 30);
		pass2.setLocation(10, 85);
		pass2.setFont(font1);
		pass2.setVisible(true);
		window.add(pass2);
		
		JPasswordField password2 = new JPasswordField();
		password2.setSize(250, 30);;
		password2.setLocation(200, 85);
		password2.setVisible(true);
		window.add(password2);
		
		JLabel warning1 = new JLabel("*");
		warning1.setForeground(Color.RED);
		warning1.setSize(75, 30);
		warning1.setLocation(460, 45);
		warning1.setVisible(false);
		window.add(warning1);
		
		JLabel warning2 = new JLabel("*");
		warning2.setForeground(Color.RED);
		warning2.setSize(25, 30);
		warning2.setLocation(460, 80);
		warning2.setVisible(false);
		window.add(warning2);
		
		JLabel warning = new JLabel("Passwords must match");
		warning.setForeground(Color.RED);
		warning.setSize(600, 30);
		warning.setHorizontalAlignment(SwingConstants.CENTER);
		warning.setLocation(1, 115);
		warning.setVisible(false);
		window.add(warning);
		
		Color buttonColor = new Color(225,230,240);
		
		JButton okButton = new JButton("OK");
		okButton.setSize(100, 30);
		okButton.setLocation(175, 150);
		okButton.setBackground(buttonColor);
		okButton.setVisible(true);
		window.add(okButton);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String pass = new String(password1.getPassword());
				String pass2 = new String(password2.getPassword());
				
				if(pass.equals(pass2) && !nameField.getText().isEmpty()) {
					//passwords match and nameField isn't empty
					String sql = "alter user SA rename to " + nameField.getText().toUpperCase();
					try {
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						
						sql = "alter user " + nameField.getText().toUpperCase() + " set password '" +
								pass + "'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
					} catch (SQLException e) {
						System.out.println(e);
					}

					window.setVisible(false);
				} else {
					warning.setVisible(true);
					warning1.setVisible(true);
					warning2.setVisible(true);
				}
			}
		});
		
		
		JButton quitButton = new JButton("Quit");
		quitButton.setSize(80, 30);
		quitButton.setBackground(buttonColor);
		quitButton.setLocation(325, 150);
		quitButton.setVisible(true);
		window.add(quitButton);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		
		
		window.setVisible(true);
		
	}
	
	private Font font1 = new Font("sans serif", Font.PLAIN, 20);
}
