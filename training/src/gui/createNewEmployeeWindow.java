package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.sql.*;

import misc.sqlFunctions;

public class createNewEmployeeWindow extends JDialog{
	public void createWindow(Connection conn) {
		this.setSize(new Dimension(500,300));
		this.setTitle("Create New Employee");
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		JDialog window = this;
		
		JLabel firstLabel = new JLabel("First Name");
		firstLabel.setSize(new Dimension(100, 25));
		firstLabel.setLocation(10, 10);
		firstLabel.setVisible(true);
	
		JLabel lastLabel = new JLabel("Last Name");
		lastLabel.setSize(new Dimension(100, 25));
		lastLabel.setLocation(10, 40);
		lastLabel.setVisible(true);
		
		JLabel deptLabel = new JLabel("Department");
		deptLabel.setSize(new Dimension(100, 25));
		deptLabel.setLocation(10, 100);
		deptLabel.setVisible(true);
		
		JLabel positionLabel = new JLabel("Position");
		positionLabel.setSize(new Dimension(100, 25));
		positionLabel.setLocation(10, 70);
		positionLabel.setVisible(true);
		
		firstField.setSize(200, 25);
		firstField.setLocation(100, 10);
		firstField.setVisible(true);
		
		lastField.setSize(200, 25);
		lastField.setLocation(100, 40);
		lastField.setVisible(true);
		
		JTextField positionField = new JTextField();
		positionField.setSize(200, 25);
		positionField.setLocation(100, 70);
		positionField.setVisible(true);
		
		JLabel warning = new JLabel("All fields must be filled out.");
		warning.setForeground(Color.red);
		warning.setSize(new Dimension(200, 25));
		warning.setLocation(175, 200);
		warning.setVisible(false);

		
		ResultSet rs;
		int count = 0;

		try {
			PreparedStatement sql = conn.prepareStatement("select departmentName from department");
			rs = sql.executeQuery();
			
			//get the size of the result set
			PreparedStatement sql2 = conn.prepareStatement("select count(*) as rowcount from department");
			ResultSet rs2 = sql2.executeQuery();
			while(rs2.next()) {
				count = rs2.getInt("rowcount");
			}
			
			radBut = new JRadioButton[count];
			int index = 0;
			int rowCount = 0;
			int colCount = 0;
			while(rs.next()) {
				radBut[index] = new JRadioButton(rs.getString("departmentName"));
				group.add(radBut[index]);
				
				int xLoc = (colCount * 100) + (10 * (colCount - 1)) + 20;
				int yLoc = (rowCount * 40) + 125;

				radBut[index].setLocation(xLoc, yLoc);
				radBut[index].setSize(new Dimension(100, 25));
				this.add(radBut[index]);
				
				++ colCount;
				if(colCount == 4) {
					colCount = 0;
					++ rowCount;
				}
				
				++index;
			}
			
			
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		JButton create = new JButton("Create");
		create.setSize(new Dimension(80, 30));
		create.setLocation(this.getWidth()/2 - 100, 225);
		create.setBackground(new Color(238, 238, 238));
		create.setVisible(true);
		
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//add the new employee to the database
				int checkedRadio = -1;
				for(int i = 0; i < radBut.length; ++i) {
					if(radBut[i].isSelected()) {
						checkedRadio = i + 1;     //add 1 because array starts at 0 while db starts at 1
						break;
					}
				}
				
				
				if(firstField.getText().isEmpty() || lastField.getText().isEmpty() ||
						positionField.getText().isEmpty()) {
					warning.setVisible(true);
				} else {
					//all fields filled out create employee
					ResultSet rs;
					boolean executeCreate = true;
					try {
						
							
						//check for employee already existing
						PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee");
						rs = stmt.executeQuery();
						while(rs.next()) {
							if(rs.getString("firstName").toLowerCase().equals(firstField.getText().
									toLowerCase())){
								if(rs.getString("lastName").toLowerCase().
										equals(lastField.getText().toLowerCase())){
									if(rs.getString("position").toLowerCase().equals(positionField.getText().toLowerCase())) {
										//duplicate found
										executeCreate = false;
										warning.setText("Employee already exists.");
										warning.setVisible(true);
										break;
									}
								}
							}
						}

					} catch(SQLException e) {
						
					}
					
					if(executeCreate) {
						sqlFunctions sqlFunc = new sqlFunctions();
						sqlFunc.addEmployee(conn, firstField.getText(), lastField.getText(), 
								checkedRadio, positionField.getText());	
						window.setVisible(false);
					}

				}
				
			}
		});
		
		radBut[0].setSelected(true);
		
		
		JButton cancel = new JButton("Cancel");
		cancel.setSize(new Dimension(80, 30));
		cancel.setLocation(this.getWidth()/2 + 20, 225);
		cancel.setBackground(new Color(238, 238, 238));
		cancel.setVisible(true);
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.dispose();
			}
		});
		
		try {
			PreparedStatement temp = conn.prepareStatement("select * from employee");
			temp.execute();
			ResultSet r = temp.executeQuery();
			while(r.next()) {
				System.out.println(r.getString("firstName") + " h  " + r.getString("lastName") + 
						"  h   " + r.getString("position"));
			}

		} catch (SQLException e) {
			System.err.println("did not execute " + e);
		}
		
		
		this.add(firstLabel);
		this.add(lastLabel);
		this.add(positionLabel);
		this.add(deptLabel);
		this.add(firstField);
		this.add(lastField);
		this.add(positionField);
		this.add(warning);
		this.add(create);
		this.add(cancel);
		
		this.setVisible(true);
		
		
	}
	
	private JTextField firstField = new JTextField();
	private JTextField lastField = new JTextField();
	private ButtonGroup group = new ButtonGroup();
	private JRadioButton[] radBut;
	private String depart;
}
