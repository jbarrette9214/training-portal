package gui;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import misc.sqlFunctions;

public class CreateNewDepartment extends JDialog {
	public void createWindow(Connection conn) {
		JDialog window = this;
		
		this.setTitle("Create New Department");
		this.setSize(600,  400);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		
		JLabel nameLabel = new JLabel("Name of new Department");
		nameLabel.setSize(200, 25);
		nameLabel.setLocation(10, 10);
		nameLabel.setVisible(true);
		this.add(nameLabel);
		
		JTextField nameText = new JTextField();
		nameText.setSize(300, 25);
		nameText.setLocation(10, 40);
		nameText.setVisible(true);
		this.add(nameText);
		
		//message to use if the add a name that is in use
		JLabel message1 = new JLabel("Name already in use, choose another name.");
		message1.setForeground(Color.red);
		message1.setSize(400, 25);
		message1.setLocation(320, 40);
		message1.setVisible(false);
		this.add(message1);
		
		JSeparator sep1 = new JSeparator();
		sep1.setSize(this.getWidth(), 2);
		sep1.setLocation(0, 70);
		sep1.setVisible(true);
		this.add(sep1);
		
		JLabel inUseLabel = new JLabel("Names already in use", SwingConstants.CENTER);
		inUseLabel.setSize(this.getWidth(), 25);
		inUseLabel.setLocation(0, 75);
		inUseLabel.setVisible(true);
		this.add(inUseLabel);
		
		sqlFunctions sql = new sqlFunctions();
		int count = sql.getDepartmentCount(conn);
		
		
		if(count != -1) {

			departNames = new String[count];
			
			departLabels = new JLabel[count]; 
			
			//there are departments so make the list of used names
			int index = 0;
			
			ResultSet rs = sql.getAllDepartments(conn);
			try {
				while(rs.next()) {
					departNames[index] = rs.getString("departmentName");
					++index;
				}
			} catch (SQLException e) {};
			
			int y = 105;
			for(int i = 0; i < departNames.length; i = i + 2) {
				departLabels[i] = new JLabel(departNames[i]);
				departLabels[i].setSize(100, 25);
				departLabels[i].setLocation(200, y);
				departLabels[i].setVisible(true);
				this.add(departLabels[i]);
				
				//check to make sure there is a next one
				if(i + 1 != count) {
					departLabels[i+1] = new JLabel(departNames[i + 1]);
					departLabels[i+1].setSize(100, 25);
					departLabels[i+1].setLocation(350, y);
					departLabels[i+1].setVisible(true);
					this.add(departLabels[i+1]);
				}
				
				y += 30;
			}
			
		}
		
		JButton create = new JButton("Create");
		create.setSize(100, 30);
		create.setBackground(new Color(238, 238, 238));
		create.setLocation(175, 310);
		create.setVisible(true);
		
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//check for duplicate and create if none
				if(checkDuplicate(nameText.getText()) == false) {
					sql.addDepartment(conn, nameText.getText());
					window.dispose();
				} else {
					message1.setVisible(true);
				}
				
			}
		});
		this.add(create);
		
		
		JButton cancel = new JButton("Cancel");
		cancel.setSize(100, 30);;
		cancel.setBackground(new Color(238, 238, 238));
		cancel.setLocation(325, 310);
		cancel.setVisible(true);
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.dispose();
			}
		});
		
		this.add(cancel);
		
		
		
		this.setVisible(true);
	}
	
	//use to check if the name person entered is already used
	private boolean checkDuplicate(String name) {
		for(int i = 0; i < departNames.length; ++i) {
			if(departNames[i].toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		
		//there weren't any matches
		return false;
	}
	
	private String[] departNames;
	private JLabel[] departLabels;
	
	
}
