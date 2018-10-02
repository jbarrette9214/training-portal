package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import misc.employeeFunctions;
import misc.sqlFunctions;
import training.Start;

public class DepartmentSearch extends JPanel implements FocusListener{

	public DepartmentSearch(Start mainWindow) {
		this.setPreferredSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight() - 60));
		this.setLayout(null);
		this.setBackground(new Color(143, 174, 224));
		
		tempConn = mainWindow.getOpenedConnection();
		
		JLabel action = new JLabel("Action To Perform");
		action.setFont(h1);
		action.setSize(250, 30);
		action.setLocation(10, 10);
		action.setVisible(true);
		
		this.add(action);
		
		JRadioButton employees = new JRadioButton("Department Employees");
		employees.setLocation(260, 15);
		employees.setSize(250, 30);
		employees.setVisible(true);
		employees.setSelected(true);
		employees.setBackground(this.getBackground());
		this.add(employees);
		
		btnGroup.add(employees);
		
		JRadioButton needed = new JRadioButton("Employees That Need a Class");
		needed.setLocation(520, 15);
		needed.setSize(250, 30);;
		needed.setVisible(true);
		needed.setBackground(this.getBackground());
		this.add(needed);
		
		btnGroup.add(needed);
		
		JSeparator sep1 = new JSeparator();
		sep1.setSize(mainWindow.getWidth(), 1);
		sep1.setLocation(1,60);
		sep1.setVisible(true);
		this.add(sep1);
		
		JLabel deptLabel = new JLabel("Departments");
		deptLabel.setSize(200, 20);
		deptLabel.setLocation(10, 70);
		deptLabel.setFont(h2);
		deptLabel.setVisible(true);
		this.add(deptLabel);
		
		
		
		
		JLabel classLbl = new JLabel("Classes");
		classLbl.setSize(100, 20);
		classLbl.setLocation(850, 70);
		classLbl.setFont(h2);
		classLbl.setVisible(true);
		this.add(classLbl);
		
		classBox = new JComboBox();
		classBox.setSize(150, 20);
		classBox.setLocation(900, 100);
		classBox.setVisible(true);
		classBox.setEnabled(false);
		this.add(classBox);
		
		xCoord = 0;
		yCoord = 0;
		focusGained(null);
		
		JSeparator sep2 = new JSeparator();
		sep2.setSize(mainWindow.getWidth(), 1);
		sep2.setLocation(1, yCoord + 40);
		sep2.setVisible(true);
		this.add(sep2);
		
		JLabel results = new JLabel("Results");
		results.setSize(150, 40);
		results.setLocation(50, yCoord + 40);
		results.setFont(h1);
		results.setVisible(true);
		this.add(results);
		
		textBox = new JTextArea();
		textBox.setBackground(Color.white);
		textBox.setBorder(BorderFactory.createLineBorder(Color.black));
		textBox.setSize(mainWindow.getWidth() - 200, 400);
		textBox.setLocation(100, yCoord + 80);
		textBox.setVisible(true);
		this.add(textBox);
		
		JButton query = new JButton("Query");
		query.setSize(100, 30);
		query.setLocation(1100, 95);
		query.setVisible(true);
		query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				if(employees.isSelected()) {
					textBox.append(employees.getText() + "\t");
				} else {
					if(classBox.getSelectedItem().toString().equals("...")) {
						return;
					}

					textBox.append(needed.getText() + "\t");
				}
				
				for(JRadioButton btn : deptBtns) {
					if(btn.isSelected()) {
						textBox.append("Department: " + btn.getText() + "\t");
					}
				}
				
				if(needed.isSelected()) {
					textBox.append("Class: " + classBox.getSelectedItem().toString());
				}
				
				textBox.append("\n");
				
				updateTextArea(tempConn);
			}
		});
		
		this.add(query);
		
		employees.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				classBox.setEnabled(false);
			}
		});

		needed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				classBox.setEnabled(true);
			}
		});

		
		JButton printBtn = new JButton();
		printBtn.setToolTipText("Print");
		printBtn.setSize(45,45);
		printBtn.setBackground(Color.white);
		printBtn.setBorder(null);
		printBtn.setLocation(mainWindow.getWidth() - 75, 550);
		printBtn.setVisible(true);
		
		JButton clearBtn = new JButton();
		clearBtn.setToolTipText("Clear");
		clearBtn.setSize(45,45);
		clearBtn.setBackground(Color.white);
		clearBtn.setBorder(null);
		clearBtn.setLocation(mainWindow.getWidth() - 75, 450);
		clearBtn.setVisible(true);

		Path iconPath = Paths.get("").resolve("training").resolve("src").resolve("training")
				.resolve("images");
		try {
			Image icon = ImageIO.read(new File(iconPath.toString() + "\\printer.png"));
			printBtn.setIcon(new ImageIcon(icon));
			icon = ImageIO.read(new File(iconPath.toString() + "\\x.png"));
			clearBtn.setIcon(new ImageIcon(icon));
		}catch(IOException e) {
			System.out.println(e);
		}
		
		printBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					//if not empty print it
					if(!textBox.getText().isEmpty()) {
						textBox.print();
					}
				}catch(PrinterException e) {
					System.out.println(e);
				}
			}
		});
		
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				textBox.setText(null);
			}
		});
		
		
		this.add(printBtn);
		this.add(clearBtn);
		
		classBox.addItem(deptChanged(tempConn, 0));
	}
	
	private String[] deptChanged(Connection conn, int deptChosen) {
		String[] temp = null;
		sqlFunctions sqlFunc = new sqlFunctions();
		
		if(deptChosen == 0) {
			ResultSet rs1 = sqlFunc.getAllClassesNoDuplicate(conn);
			try {
				int count = 0;
				while(rs1.next()) {
					++count;
				}
				temp = new String[count];
				
				rs1.beforeFirst();
				int index = 0;
				while(rs1.next()) {
					temp[index] = rs1.getString("classCode");
					++index;
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
			return temp;
		}
		
		
		ResultSet rs = sqlFunc.getRequiredClassesByDept(conn, deptChosen);
		
		try {
			int count = 0;
			while(rs.next()) {
				++count;
			}
			temp = new String[count];

			rs.beforeFirst();
			int index = 0;
			while(rs.next()) {
				temp[index] = rs.getString("classCode");
				++index;
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		for(int i = 0; i < temp.length; ++i) {
			System.out.println(temp[i]);
		}
		return temp;
	}	//end deptChanged
	
	private void updateTextArea(Connection conn) {
		
		employeeFunctions empFunc = new employeeFunctions();
		if(deptBtns[0].isSelected()) {
			//have to go through all the departments
			for(int i = 1; i < deptBtns.length; ++i) {
				List<String> list = empFunc.getEmployeesDontHaveClass(conn, 
						classBox.getSelectedItem().toString(), i);
				for(String temp : list) {
					textBox.append("\t" + temp + "\n");
				}
			}
		} else {
			for(int i = 1; i < deptBtns.length; ++i) {
				if(deptBtns[i].isSelected()) {
					List<String> list = empFunc.getEmployeesDontHaveClass(conn, 
							classBox.getSelectedItem().toString(), i);
					for(String temp : list) {
						textBox.append("\t" + temp + "\n");
					}
					break;
				}
			}
		}
		textBox.append("\n\n");
	
	}	//end updateText
	
	
	@Override
	public void focusGained(FocusEvent e) {
		sqlFunctions sqlFunc = new sqlFunctions();
		ResultSet rs = sqlFunc.getAllDepartments(tempConn);
		int count = sqlFunc.getDepartmentCount(tempConn);
		deptBtns = new JRadioButton[count + 1];
	
		deptBtns[0] = new JRadioButton("All");
		deptBtns[0].setSize(150, 25);
		deptBtns[0].setLocation(50, 90);
		deptBtns[0].setBackground(this.getBackground());
		deptBtns[0].setSelected(true);
		deptBtns[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String[] temp = deptChanged(tempConn, 0);
				
				classBox.addItem("...");
				for(String tmp: temp) {
					classBox.addItem(tmp);
				}
			}
		});
		
		
		
		deptBtns[0].setVisible(true);
		deptBtnGrp.add(deptBtns[0]);
		this.add(deptBtns[0]);
		
		try {
			int index = 1;
			while(rs.next()) {
				deptBtns[index] = new JRadioButton(rs.getString("departmentName"));
				++index;
			}
		} catch(SQLException f) {
			System.err.println(f);
		}
		
		int rows = 0, col = 1;
		

		
		for(int i = 1; i <= count; ++i) {
			if(i % 4 == 0) {
				++rows;
				col = 0;
			}
			
			xCoord = (col  * 200) + 50;
			yCoord = (rows * 30) + 90;

			deptBtns[i].setSize(150, 25);
			deptBtns[i].setLocation(xCoord, yCoord);
			deptBtns[i].setBackground(this.getBackground());
			
			deptBtns[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					classBox.removeAllItems();
					int temp = 1;
					
						for(int i = 1; i < deptBtns.length; ++i) {
							if(deptBtns[i].isSelected()) {
								temp = i;
								break;
							}
						}
						String[] tempClasses = deptChanged(tempConn, temp);
						classBox.addItem("...");
						for(String temp1 : tempClasses) {
							classBox.addItem(temp1);
						}
					
				}
			});
			
			deptBtnGrp.add(deptBtns[i]);
			this.add(deptBtns[i]);
			
			++col;
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		textBox.setText("");
	}

	private Connection tempConn;
	
	private int xCoord, yCoord;
	
	private Font h1 = new Font("serif", Font.BOLD, 22);
	private Font h2 = new Font("serif", Font.PLAIN, 18);
	private ButtonGroup btnGroup = new ButtonGroup();
	
	private JRadioButton[] deptBtns;
	private ButtonGroup deptBtnGrp = new ButtonGroup();
	
	private String[] classes;
	
	private JComboBox classBox;
	
	private JTextArea textBox;

}
