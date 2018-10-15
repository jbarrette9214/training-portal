package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import misc.ClassFunctions;
import misc.sqlFunctions;
import training.Start;

public class EditDepartmentPanel extends JPanel implements FocusListener {

	public EditDepartmentPanel(Start mainWindow) {
		thisPanel = this;
	
		conn = mainWindow.getOpenedConnection();
		
		SpringLayout layout = new SpringLayout();
		thisPanel.setLayout(layout);
		
		thisPanel.setBackground(new Color(244, 244, 127));
		
		JLabel chooseLbl = new JLabel("Choose a department to edit");
		chooseLbl.setPreferredSize(new Dimension(400, 30));
		chooseLbl.setFont(h1);
		layout.putConstraint(SpringLayout.WEST, chooseLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, chooseLbl, 25, SpringLayout.NORTH, thisPanel);
		thisPanel.add(chooseLbl);
		
		//setRadioButtons();

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
			SpringLayout tempLayout = (SpringLayout) thisPanel.getLayout();
			
			//set the x location
			if(col == 0) {
				tempLayout.putConstraint(SpringLayout.WEST, deptRadBtns[i], 25, SpringLayout.WEST, thisPanel);
			} else {
				tempLayout.putConstraint(SpringLayout.WEST, deptRadBtns[i], 25, SpringLayout.EAST, deptRadBtns[i-1]);
			}
			
			//set the y location
			tempLayout.putConstraint(SpringLayout.NORTH, deptRadBtns[i], 25 * rows + 50, SpringLayout.NORTH, thisPanel);
			
			deptRadBtns[i].setBackground(this.getBackground());
			deptRadBtns[i].setFont(new Font("sans serif", Font.PLAIN, 18));

				deptRadBtns[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						fillLists();
					}
				});
			
			
			++col;
			if(i == 0) {
				deptRadBtns[i].setSelected(true);
			}
			deptBtnGrp.add(deptRadBtns[i]);
			thisPanel.add(deptRadBtns[i]);
		
		
		}
		
		
		
		JSeparator sep1 = new JSeparator();
		sep1.setPreferredSize(new Dimension(mainWindow.getWidth(), 1));
		int maxRadio = deptRadBtns.length - 1;
		layout.putConstraint(SpringLayout.WEST, sep1, 1, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, sep1, 25, SpringLayout.SOUTH, deptRadBtns[maxRadio]);
		thisPanel.add(sep1);
		
		JLabel addRemoveLbl = new JLabel("Add/Remove classes from department required list");
		addRemoveLbl.setPreferredSize(new Dimension(500, 30));
		addRemoveLbl.setFont(h1);
		layout.putConstraint(SpringLayout.WEST, addRemoveLbl, 10, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, addRemoveLbl, 10, SpringLayout.SOUTH, sep1);
		thisPanel.add(addRemoveLbl);
		
		JButton newClassBtn = new JButton("New Class");
		newClassBtn.setPreferredSize(new Dimension(100, 30));
		newClassBtn.setBackground(new Color(238, 238, 238));
		layout.putConstraint(SpringLayout.EAST, newClassBtn, -10,  SpringLayout.EAST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, newClassBtn, 10, SpringLayout.SOUTH, sep1);
		newClassBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				createNewEmployeeWindow createEmp = new createNewEmployeeWindow();
			}
		});
		
		
		thisPanel.add(newClassBtn);
		
		
		
		JLabel currentLbl = new JLabel("Current required classes");
		currentLbl.setPreferredSize(new Dimension(250, 18));
		currentLbl.setFont(new Font("sans serif", Font.PLAIN, 16));
		layout.putConstraint(SpringLayout.WEST, currentLbl, 100, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, currentLbl, 25, SpringLayout.SOUTH, addRemoveLbl);
		thisPanel.add(currentLbl);
		
		JLabel otherLbl = new JLabel("Other Classes");
		otherLbl.setPreferredSize(new Dimension(150, 18));
		otherLbl.setFont(new Font("sans serif", Font.PLAIN, 16));
		int y = mainWindow.getWidth()/2 + 100;
		layout.putConstraint(SpringLayout.WEST, otherLbl, y, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, otherLbl, 25, SpringLayout.SOUTH, addRemoveLbl);
		thisPanel.add(otherLbl);
		
		
		//set up the current list box
		currentModel = new DefaultListModel<String>();
		currentList = new JList<String>(currentModel);
		currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		currentList.setVisibleRowCount(50);
		
		JScrollPane currentScroll = new JScrollPane(currentList);
		currentScroll.setPreferredSize(new Dimension (400, 150));
		layout.putConstraint(SpringLayout.WEST, currentScroll, 75, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, currentScroll, 10, SpringLayout.SOUTH, currentLbl);
		thisPanel.add(currentScroll);
		
		//setup the other list box
		otherModel = new DefaultListModel<String>();
		otherList = new JList<String>(otherModel);
		otherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		otherList.setVisibleRowCount(50);
		
		JScrollPane otherScroll = new JScrollPane(otherList);
		otherScroll.setPreferredSize(new Dimension(400,150));
		layout.putConstraint(SpringLayout.WEST, otherScroll, 250, SpringLayout.EAST, currentScroll);
		layout.putConstraint(SpringLayout.NORTH, otherScroll, 10, SpringLayout.SOUTH, otherLbl);
		thisPanel.add(otherScroll);
		
		JButton addClass = new JButton("<< Add <<");
		addClass.setPreferredSize(new Dimension(100, 30));
		addClass.setBackground(new Color(238, 238, 238));
		layout.putConstraint(SpringLayout.WEST, addClass, 75, SpringLayout.EAST, currentScroll);
		layout.putConstraint(SpringLayout.NORTH, addClass, 30, SpringLayout.NORTH, currentScroll);
		thisPanel.add(addClass);
		
		JLabel message = new JLabel("");
		message.setPreferredSize(new Dimension(300, 30));
		message.setForeground(Color.RED);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		layout.putConstraint(SpringLayout.WEST, message, mainWindow.getWidth()/2 - 150, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, message, 10, SpringLayout.SOUTH, currentScroll);
		thisPanel.add(message);
		message.setVisible(false);
		
		addClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//get which department
				int deptID = -1;
				for(int i = 0; i < deptRadBtns.length; ++i) {
					if(deptRadBtns[i].isSelected()) {
						deptID = i + 1;
						break;
					}
				}
				//make sure that something is selected
				String classCode = "";
				String desc = "";
				if(!otherList.isSelectionEmpty()) {			//something is selected
					String temp = otherList.getSelectedValue();
					int index = temp.indexOf('>');
					classCode = temp.substring(0, index);
					ResultSet rs;
					try {
						String sql = "select description from required_class_by_dept where classcode = '" +
								classCode + "'";
						Statement stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						while(rs.next()) {
							desc = rs.getString("description");
							break;
						}
					} catch (SQLException e) {
						System.err.println(e);
					}
				
				} else {
					//show message
					message.setText("Must select a class from Other Classes");
					message.setVisible(true);
				}
				if(deptID != -1 && classCode != "" && desc != "") {
					sqlFunctions sqlfunc = new sqlFunctions();
					sqlfunc.addRequired(conn, deptID, classCode, desc);
					fillLists();		//reset all the lists to show updates
				}
			}
		});	
		
		
		
		JButton removeClass = new JButton(">> Remove >>");
		removeClass.setPreferredSize(new Dimension(100, 30));
		removeClass.setBackground(new Color(238, 238, 238));
		layout.putConstraint(SpringLayout.WEST, removeClass, 75, SpringLayout.EAST, currentScroll);
		layout.putConstraint(SpringLayout.NORTH, removeClass, 30, SpringLayout.SOUTH, addClass);
		thisPanel.add(removeClass);
		
		removeClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//get which department
				int deptID = -1;
				for(int i = 0; i < deptRadBtns.length; ++i) {
					if(deptRadBtns[i].isSelected()) {
						deptID = i + 1;
						break;
					}
				}

				String classCode = "";
				
				if(!currentList.isSelectionEmpty()) {
					String temp = currentList.getSelectedValue();
					int index = temp.indexOf('>');
					classCode = temp.substring(0, index);
					sqlFunctions sqlFunc = new sqlFunctions();
					sqlFunc.deleteClassFromRequired(conn, classCode, deptID);
					
				} else {
					message.setText("Must select a class from Current Required Classes list");
					message.setVisible(true);
				}
				
				fillLists();		//reset all lists
			}
		});
		
		
		fillLists();
		
		JSeparator sep2 = new JSeparator();
		sep2.setPreferredSize(new Dimension(mainWindow.getWidth(), 1));
		layout.putConstraint(SpringLayout.WEST, sep2, 1, SpringLayout.WEST, thisPanel);
		layout.putConstraint(SpringLayout.NORTH, sep2, 10, SpringLayout.SOUTH, otherScroll);
		thisPanel.add(sep2);
		
		
	}
	
	
	private void fillLists() {
		currentModel.clear();
		otherModel.clear();
		
		sqlFunctions sqlFunc = new sqlFunctions();
		ResultSet rs1 = sqlFunc.getAllClassesNoDuplicate(conn);
		
		//find out which department is selected
		int deptID = 0;
		for(int i = 0; i < deptRadBtns.length; ++i) {
			if(deptRadBtns[i].isSelected()) {
				deptID = i + 1;
				break;
			}
		}
		
		ResultSet rs2 = null;
		if(deptID != 0) {
			rs2 = sqlFunc.getRequiredClassesByDept(conn, deptID);

			try {
				while(rs2.next()) {
					String temp1, temp2;
					temp1 = rs2.getString("classCode");
					temp2 = rs2.getString("description");
					temp1 = temp1 + "> " + temp2;
					currentModel.addElement(temp1);
				}
			} catch(SQLException e) {
				System.out.println(e);
			}
		}
		
		//fill otherModel 
		if(rs2 != null) {
			try {
				rs1.beforeFirst();

				while(rs1.next()) {
					boolean exitLoop = false;
					rs2.beforeFirst();
					while(rs2.next() && exitLoop == false) {
						
						if(rs1.getString("classCode").equals(rs2.getString("classCode"))) {
							//has class exit the loop
							exitLoop = true;
						} 
					}
					if(exitLoop == false) {
						//doesn't have the class
						ClassFunctions classFunc = new ClassFunctions();
						String str = rs1.getString("classCode") + "> " + classFunc.getDescriptionFromCode(conn, 
								rs1.getString("classCode"));
						otherModel.addElement(str);
					}
					
				}
			
			} catch (SQLException e) {
				System.out.println(e);
			}
			
		
		}
	}
	
	
	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private JPanel thisPanel;
	
	private JLabel chooseLbl;
	private JList<String> currentList, otherList;
	private DefaultListModel currentModel, otherModel;
	private Font h1 = new Font("sans serif", Font.BOLD, 20);
	private Connection conn;
	
	private JRadioButton deptRadBtns[];
	private ButtonGroup deptBtnGrp = new ButtonGroup();

}
