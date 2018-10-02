package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import misc.employeeFunctions;
import misc.sqlFunctions;
import training.Start;


public class EditEmployeePanel extends JPanel implements FocusListener{

	public EditEmployeePanel(Start mainWindow) {
		JPanel thisPanel = this;
		Connection conn = mainWindow.getOpenedConnection();
		conn2 = conn;
		
		//load all employees to have quick access to update list
		try{
			PreparedStatement stmt = conn.prepareStatement("select * from employee", 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = stmt.executeQuery();
			
		} catch(SQLException e) {
			System.err.println(e);
		}

		
		SpringLayout layout = new SpringLayout();
		thisPanel.setSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight() - 60));
		thisPanel.setLayout(layout);
		thisPanel.setBackground(new Color(183, 247,236));
		
		JLabel lastLabel = new JLabel("Last Name");
		lastLabel.setSize(100, 30);
		lastLabel.setFont(h1);
		layout.putConstraint(SpringLayout.WEST, lastLabel, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, lastLabel , 20, SpringLayout.NORTH, thisPanel);
		thisPanel.add(lastLabel);
		
		lastNameField = new JTextField(30);
		lastNameField.setFont(new Font("sans serif", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.WEST, lastNameField, 25, SpringLayout.EAST, lastLabel);
		layout.putConstraint(SpringLayout.NORTH, lastNameField, 20, SpringLayout.NORTH, thisPanel);
		thisPanel.add(lastNameField);
		
		//listener for when lastNameField changes
		lastNameField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(lastNameField.getText(), conn);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update(lastNameField.getText(), conn);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(lastNameField.getText(), conn);
			}
			
		});
		
		JLabel warning1 = new JLabel("You must select a name from below");
		warning1.setForeground(Color.RED);
		layout.putConstraint(SpringLayout.WEST, warning1, 10, SpringLayout.EAST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, warning1, 10, SpringLayout.SOUTH, lastLabel);
		warning1.setVisible(false);
		thisPanel.add(warning1);
		
		listModel = new DefaultListModel<String>();
		
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(5);
		
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(400, 200));
		layout.putConstraint(SpringLayout.WEST, scroll, 50, SpringLayout.EAST, lastNameField);
		layout.putConstraint(SpringLayout.NORTH, scroll, 20, SpringLayout.NORTH, thisPanel);
		thisPanel.add(scroll);
		
		JButton editBtn = new JButton("Edit");
		editBtn.setPreferredSize(new Dimension(100, 30));
		editBtn.setBackground(new Color(236, 236, 236));
		layout.putConstraint(SpringLayout.WEST, editBtn, 150, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, editBtn, 20, SpringLayout.SOUTH, warning1);
		thisPanel.add(editBtn);
		
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(list.getSelectedIndex() != -1) {
					setAllFields(conn);
				} else {
					//show message that no name was selected
					warning1.setVisible(true);
				}

			}
		});
		
		JButton clearBtn = new JButton("Clear");
		clearBtn.setPreferredSize(new Dimension(100, 30));
		clearBtn.setBackground(new Color(236, 236, 236));
		layout.putConstraint(SpringLayout.WEST, clearBtn, 50, SpringLayout.EAST, editBtn);
		layout.putConstraint(SpringLayout.NORTH, clearBtn, 20, SpringLayout.SOUTH, warning1);
		thisPanel.add(clearBtn);
		
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				lastNameField.setText("");
				listModel.clear();
				firstText.setText("");
				lastText.setText("");
				positionText.setText("");
			}
		});
		
		JSeparator sep1 = new JSeparator();
		sep1.setPreferredSize(new Dimension(mainWindow.getWidth(), 1));
		layout.putConstraint(SpringLayout.WEST, sep1, 1, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, sep1, 10, SpringLayout.SOUTH, scroll);
		thisPanel.add(sep1);
		
		JLabel firstLbl = new JLabel("First Name");
		firstLbl.setFont(h1);
		firstLbl.setPreferredSize(new Dimension(150,30));
		layout.putConstraint(SpringLayout.WEST, firstLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, firstLbl, 25, SpringLayout.SOUTH, sep1);
		thisPanel.add(firstLbl);
		
		firstText = new JTextField();
		firstText.setPreferredSize(new Dimension(300, 30));
		firstText.setBackground(Color.white);
		firstText.setFont(new Font("sans serif", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.WEST, firstText, 25, SpringLayout.EAST, firstLbl);
		layout.putConstraint(SpringLayout.NORTH, firstText, 25, SpringLayout.SOUTH, sep1);
		thisPanel.add(firstText);
		
		JLabel lastLbl = new JLabel("Last Name");
		lastLbl.setFont(h1);
		lastLbl.setPreferredSize(new Dimension(150, 30));
		layout.putConstraint(SpringLayout.WEST, lastLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, lastLbl, 25, SpringLayout.SOUTH, firstText);
		thisPanel.add(lastLbl);
		
		lastText = new JTextField();
		lastText.setPreferredSize(new Dimension(300,30));
		lastText.setBackground(Color.white);
		lastText.setFont(new Font("sans serif", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.WEST, lastText, 25, SpringLayout.EAST, lastLbl);
		layout.putConstraint(SpringLayout.NORTH, lastText, 25, SpringLayout.SOUTH, firstText);
		thisPanel.add(lastText);
		
		JLabel positionLbl = new JLabel("Position");
		positionLbl.setFont(h1);
		positionLbl.setPreferredSize(new Dimension(150, 30));
		layout.putConstraint(SpringLayout.WEST, positionLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, positionLbl, 25, SpringLayout.SOUTH, lastText);
		thisPanel.add(positionLbl);
		
		positionText = new JTextField();
		positionText.setPreferredSize(new Dimension(300, 30));
		positionText.setBackground(Color.white);
		positionText.setFont(new Font("sans serif", Font.PLAIN, 20));
		layout.putConstraint(SpringLayout.WEST, positionText, 25, SpringLayout.EAST, positionLbl);
		layout.putConstraint(SpringLayout.NORTH, positionText, 25, SpringLayout.SOUTH, lastText);
		thisPanel.add(positionText);
		
		JLabel departmentLbl = new JLabel("Department");
		departmentLbl.setFont(h1);
		departmentLbl.setPreferredSize(new Dimension(150, 30));
		layout.putConstraint(SpringLayout.WEST, departmentLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, departmentLbl, 25, SpringLayout.SOUTH, positionLbl);
		thisPanel.add(departmentLbl);
		
		//get a list of departments
		sqlFunctions sqlFunc = new sqlFunctions();
		ResultSet rs2 = sqlFunc.getAllDepartments(conn);
		int count = sqlFunc.getDepartmentCount(conn);
		deptRadBtns = new JRadioButton[count];
		
		try {
			int index = 0;
			while(rs2.next()) {
				deptRadBtns[index] = new JRadioButton(rs2.getString("departmentName"));
				++index;
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		int xCoord = 0, yCoord = 0, rows = 0, col = 1;
				
		for(int i = 0; i < count; ++i) {
			if(i % 6 == 0) {
				++rows;
				col = 0;
			}
			
			xCoord = (col  * 200) + 50;
			yCoord = (rows * 30) + 90;

			deptRadBtns[i].setPreferredSize(new Dimension(150, 25));
			
			//set the x location
			if(col == 0) {
				layout.putConstraint(SpringLayout.WEST, deptRadBtns[i], 25, SpringLayout.WEST, thisPanel);
			} else {
				layout.putConstraint(SpringLayout.WEST, deptRadBtns[i], 25, SpringLayout.EAST, deptRadBtns[i-1]);
			}
			
			//set the y location
			layout.putConstraint(SpringLayout.NORTH, deptRadBtns[i], 25 * rows, SpringLayout.SOUTH, departmentLbl);
			
			deptRadBtns[i].setBackground(this.getBackground());
			deptRadBtns[i].setFont(new Font("sans serif", Font.PLAIN, 18));
			deptBtnGrp.add(deptRadBtns[i]);
			thisPanel.add(deptRadBtns[i]);
			
			++col;
		}
		
		
		
		
		JSeparator sep2 = new JSeparator();
		sep2.setPreferredSize(new Dimension(thisPanel.getWidth(), 1));
		layout.putConstraint(SpringLayout.WEST, sep2, 1, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, sep2, 125, SpringLayout.SOUTH, departmentLbl);
		thisPanel.add(sep2);

		JButton saveBtn = new JButton("Save");
		saveBtn.setPreferredSize(new Dimension(100, 30));
		saveBtn.setBackground(new Color(236, 236, 236));
		layout.putConstraint(SpringLayout.WEST, saveBtn, mainWindow.getWidth()/2 - 150, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, saveBtn, 10, SpringLayout.SOUTH, sep2);
		thisPanel.add(saveBtn);
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				char ch = '>';
				String temp = list.getSelectedValue();
				int index = temp.indexOf(ch);
				temp = temp.substring(0, index);
				int tempId = Integer.parseInt(temp);
				
				if(!firstText.getText().isEmpty() && !lastText.getText().isEmpty() && !positionText.getText().isEmpty()) {
					try {
						String sql = "update employee set firstName='" +firstText.getText() + "' where employeeID=" + tempId;
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						sql = "update employee set lastName='" + lastText.getText() + "' where employeeID=" + tempId;
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						sql = "update employee set position='" + positionText.getText() + "' where employeeID=" + tempId;
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						
						for(int i = 0; i < deptRadBtns.length; ++i) {
							if(deptRadBtns[i].isSelected()) {
								int dept = i + 1;
								sql = "update employee set departmentID=" + dept + " where employeeID=" + tempId;
								stmt = conn.prepareStatement(sql);
								stmt.executeUpdate();
								i = 100;
							}
						}
						
						
					} catch(SQLException e) {
						System.out.println(e);
					}
					
					firstText.setText("");
					lastText.setText("");
					positionText.setText("");
				}
			}
		});
		
		
		
		
		
		JButton resetBtn = new JButton("Reset");
		resetBtn.setPreferredSize(new Dimension(100, 30));
		resetBtn.setBackground(new Color(236, 236, 236));
		layout.putConstraint(SpringLayout.WEST, resetBtn, 100, SpringLayout.EAST, saveBtn);
		layout.putConstraint(SpringLayout.NORTH, resetBtn, 10, SpringLayout.SOUTH, sep2);
		thisPanel.add(resetBtn);
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setAllFields(conn);
			}
		});
		
		
	}
	
	private void update(String lastName, Connection conn) {
		int matchLength = lastName.length();
		listModel.clear();
		try {
			rs.beforeFirst();
			while(rs.next()) {
				String temp = rs.getString("lastName");
				temp = temp.substring(0, matchLength);
				if(temp.toUpperCase().equals(lastName.toUpperCase())) {
					String sql = "select departmentName from department where id = " +
							rs.getInt("departmentID");
					Statement stmt = conn.createStatement();
					ResultSet rs2 = stmt.executeQuery(sql);
					String name= "";
					while(rs2.next()) {
						name = rs2.getString("departmentName");
					}
					
					String matched = rs.getInt("employeeID") + ">   " + 
							rs.getString("lastName") + ", " + rs.getString("firstName") + 
							"    " + name;
					listModel.addElement(matched);
				}
			}

		} catch(SQLException e2) {
			System.err.println(e2);
		} catch(IndexOutOfBoundsException e3) {
			//just ignore this,   is a problem when backspace is pressed but seems to work fine
		}
	
	}
	
	
	private void setAllFields(Connection conn) {
		String temp = list.getSelectedValue();
		int index = temp.indexOf('>');
		
		temp = temp.substring(0, index);
		int id = Integer.parseInt(temp);
		
		sqlFunctions sqlFunc = new sqlFunctions();
		employeeFunctions empFunc = new employeeFunctions();
		int deptId = empFunc.getEmployeesDepartmentID(conn, id);
		
		//figure out which radio button for department is correct and select it
		ResultSet rs = sqlFunc.getAllDepartments(conn);
		boolean select = false;
		try {
			while(rs.next() && !select) {
				for(int i = 0; i < deptRadBtns.length; ++i) {
					if(deptRadBtns[i].getText().toUpperCase().equals(rs.getString("departmentName").toUpperCase())) {
						deptRadBtns[i].setSelected(true);
						select = true;
						break;
					}  
				}
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
	
		
		temp = empFunc.getEmployeeName(conn, id);
		
		String first, last;
		index = temp.indexOf(' ');
		first = temp.substring(0, index);
		last = temp.substring(index + 1);
		
		firstText.setText(first);
		lastText.setText(last);
		positionText.setText(empFunc.getEmployeePositionByID(conn, id));
		
		
	}
	
	//update the resultset everytme the panel reloads
	@Override
	public void focusGained(FocusEvent arg0) {
		try {
			String sql = "select * from employee";
			PreparedStatement stmt = conn2.prepareStatement(sql);
			stmt.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	//clear everything
	@Override
	public void focusLost(FocusEvent arg0) {

	}

	
	
	private Connection conn2;
	private JTextField lastNameField, firstText, lastText, positionText;
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private Font h1 = new Font("sans serif", Font.BOLD, 20);
	private ResultSet rs;
	private JRadioButton deptRadBtns[];
	private ButtonGroup deptBtnGrp = new ButtonGroup();
}
