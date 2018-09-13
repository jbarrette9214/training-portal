package gui;

import java.sql.Connection;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.sql.*;

import misc.sqlFunctions;


public class createNewClassWindow extends JDialog{
	public void createWindow(Connection conn) {
		this.setSize(500, 375);
		this.setTitle("Create New Class");
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		JDialog window = this;
		
		JLabel idLabel = new JLabel("Class Name");
		idLabel.setSize(new Dimension(100, 25));
		idLabel.setLocation(10, 10);
		idLabel.setVisible(true);
		
		JTextField idText = new JTextField();
		idText.setSize(new Dimension(100, 25));
		idText.setLocation(150, 10);
		idText.setVisible(true);
		
		//listener to not allow anything over 10 characters to be entered
		idText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(idText.getText().length() >= 10) {
					e.consume();
				}
			}
		});
		
		JLabel idMessage = new JLabel("* Required");
		idMessage.setForeground(Color.red);
		idMessage.setSize(100, 25);
		idMessage.setLocation(275, 10);
		this.add(idMessage);
		
		
		
		JSeparator sep1 = new JSeparator();
		sep1.setSize(new Dimension(500, 2));
		sep1.setLocation(1, 50);
		sep1.setVisible(true);
		
		
		JLabel label1 = new JLabel("For Which Departments");
		label1.setSize(new Dimension(150, 25));
		label1.setLocation(10, 60);
		label1.setVisible(true);
		
		//get a list and count of all the departments to build the checkboxes
		sqlFunctions sqlFunc = new sqlFunctions();
		int count = sqlFunc.getDepartmentCount(conn);
		ResultSet rs = sqlFunc.getAllDepartments(conn);
		
		checks = new JCheckBox[count];
		
		//create the checkboxes
		try {
			for(int i = 0; i < count; ++i) {
				if(rs.next()) {
					checks[i] = new JCheckBox(rs.getString("departmentName"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int y = 90;
		

		
		for(int i = 0; i < checks.length; i = i + 2) {
			checks[i].setSize(200, 25);
			checks[i].setLocation(25, y);
			checks[i].setVisible(true);
			this.add(checks[i]);
			
			//check if there is another one
			if((i + 1) < checks.length) {
				checks[i+1].setSize(200, 25);
				checks[i+1].setLocation(window.getWidth()/2 + 10, y);
				checks[i + 1].setVisible(true);
				this.add(checks[i + 1]);
			}
			y = y + 30;
			
			//check if there won't be enough room in the window
			if(y + 30 > (window.getHeight() - 160)) {
				window.setSize(window.getWidth(), window.getHeight() + 30);
			}
		
		}
		
		JSeparator sep2 = new JSeparator();
		sep2.setSize(window.getWidth(), 2);
		sep2.setLocation(0, window.getHeight() - 150);
		sep2.setVisible(true);
		this.add(sep2);
		
		JLabel description = new JLabel("Description");
		description.setSize(150, 25);
		description.setLocation(10, window.getHeight() - 145);
		description.setVisible(true);
		this.add(description);
		
		JTextField descText = new JTextField();
		descText.setSize(window.getWidth() - 50, 25);
		descText.setLocation(20, window.getHeight() - 120);
		descText.setVisible(true);
		this.add(descText);
		
		JButton create = new JButton("Create");
		create.setSize(100, 30);
		create.setLocation(window.getWidth()/2 - 150, window.getHeight() - 75);
		create.setBackground(new Color(238, 238, 238));
		create.setVisible(true);

		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//check and make sure all are filled
				if(idText.getText().isEmpty() == false) {
					int count = 0;
					for(int i = 0; i < checks.length; ++i) {
						if(checks[i].isSelected() == true) {
							if(checkDuplicate(idText.getText(), i , conn) == false) {
								String e = sqlFunc.addRequired(conn, i + 1 , idText.getText().toLowerCase(), descText.getText().toLowerCase());
								++count;
							} else {
								idMessage.setVisible(true);
							}
						}
					}
					if(count != 0) {
						//something was created
						window.dispose();
					} 
				} else {
					idMessage.setVisible(true);
				}
				
				
			}
		});
		
		
		
		this.add(create);
		
		JButton cancel = new JButton("Cancel");
		cancel.setSize(100, 30);;
		cancel.setLocation(window.getWidth()/2 + 50, window.getHeight() - 75);
		cancel.setBackground(new Color(238, 238, 238));
		cancel.setVisible(true);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.dispose();
			}
		});
		
		this.add(cancel);
		
		
		this.add(idLabel);
		this.add(idText);
		this.add(sep1);
		this.add(label1);
		this.setVisible(true);
		
	}
	
	
	public boolean checkDuplicate(String code, int dept, Connection conn) {
		String sql = "select count(*) as rowcount from required_class_by_dept where classCode='" +
				code.toLowerCase() + "'"; 
				//" in classCode";// and " + dept + " in departmentID";
		boolean result = true;
		try {
			
			
/*			//next few lines temp, just to show what is in the table
			PreparedStatement stmt1 = conn.prepareStatement("select count(*) as rowcount2 from required_class_by_dept");
			ResultSet rs1 = stmt1.executeQuery();
			while(rs1.next()) {
				System.out.println(rs1.getInt("rowcount2"));
			}
			System.out.println("done");
			
			PreparedStatement stmt2 = conn.prepareStatement("select * from department");
			ResultSet rs2 = stmt2.executeQuery();
			System.out.println(rs2.toString());
			while (rs2.next()) {
				String deptName = rs2.getString("departmentName");
				int deptID = rs2.getInt("ID");
				System.out.println(deptName + "  something  " +  deptID );
			}			
*/			
			
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("rowcount") == 0) {
					result = false;			//no duplicates found
				} else {
					result = true;			//duplicates found
				}
			}
		} catch(SQLException e) {
			System.err.println(e);
		}
		
		return result;
	}
	
	
	
	
	JCheckBox[] checks;
	
}
