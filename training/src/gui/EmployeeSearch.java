package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import training.Start;
import misc.sqlFunctions;
import misc.ClassFunctions;
import misc.employeeFunctions;

public class EmployeeSearch extends JPanel{

	public EmployeeSearch(Start mainWindow) {
		this.setPreferredSize(new Dimension(mainWindow.getWidth(), mainWindow.getHeight() - 60));
		this.setLayout(null);
		this.setBackground(new Color(235, 220, 239));
		
		Connection tempConn = mainWindow.getOpenedConnection();
		try{
			PreparedStatement stmt = tempConn.prepareStatement("select * from employee", 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = stmt.executeQuery();
			
		} catch(SQLException e) {
			System.err.println(e);
		}
		
	
		JLabel nameLabel = new JLabel("Last Name:");
		nameLabel.setSize(200, 25);
		nameLabel.setLocation(25, 45);
		nameLabel.setFont(h1);
		nameLabel.setVisible(true);
		this.add(nameLabel);
		
		JTextField nameText = new JTextField();
		nameText.setSize(300, 30);
		nameText.setLocation(175, 45);
		nameText.setFont(h1);;
		nameText.setVisible(true);
		this.add(nameText);
		
		
		
		//listener for when the nameText changes to update the list box
		nameText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(nameText.getText(), tempConn);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update(nameText.getText(), tempConn);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(nameText.getText(), tempConn);
			}
			
		});		
		
		
		JLabel message = new JLabel("Select a name from the below list to make Query");
		message.setForeground(Color.red);
		message.setSize(new Dimension(400, 40));
		message.setLocation(550, 15);
		this.add(message);
		
		
		
		listModel = new DefaultListModel();
		
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(5);
		
		JScrollPane scroll = new JScrollPane(list);
		scroll.setSize(new Dimension(400, 100));
		scroll.setVisible(true);
		scroll.setLocation(550, 45);
		
		this.add(scroll);
		
		
		//buttons for query and clear
		JButton query = new JButton("Query");
		query.setSize(new Dimension(100, 30));
		query.setLocation(50, 100);
		query.setVisible(true);
		query.setBackground(new Color(236,236,236));
		query.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(list.getSelectedIndex() != -1) {
					fillLists(tempConn);
					
				} else {
					//show message that no name was selected
					message.setVisible(true);
				}
			}
		});
		
		this.add(query);
		
		JButton clear = new JButton("Clear");
		clear.setSize(new Dimension(100, 30));
		clear.setLocation(170, 100);
		clear.setVisible(true);
		clear.setBackground(new Color(236, 236, 236));
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				nameText.setText("");
				
				listModel.clear();
				takenListModel.clear();
				neededListModel.clear();
					
			}
		});
		
		
		this.add(clear);
		
		JSeparator sep1 = new JSeparator(SwingConstants.HORIZONTAL);
		sep1.setLocation(1,175);
		sep1.setVisible(true);
		sep1.setSize(new Dimension(mainWindow.getWidth() , 2));
		this.add(sep1);
		
		JLabel summary = new JLabel("Summary for ");
		summary.setSize(new Dimension(500, 40));
		summary.setFont(h1);
		summary.setLocation(100, 200);
		summary.setVisible(true);
		this.add(summary);
		
		takenListModel = new DefaultListModel<String>();
		
		JList<String> takenList = new JList<String>(takenListModel);
		takenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		takenList.setVisibleRowCount(20);
		
		JScrollPane takenScroll = new JScrollPane(takenList);
		takenScroll.setSize(400, 300);
		takenScroll.setLocation(125, 250);
		takenScroll.setVisible(true);
		this.add(takenScroll);
		
		
		neededListModel = new DefaultListModel<String>();
		
		JList<String> neededList = new JList<String>(neededListModel);
		neededList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		neededList.setVisibleRowCount(20);
		
		JScrollPane neededScroll = new JScrollPane(neededList);
		neededScroll.setSize(400, 300);
		neededScroll.setLocation(mainWindow.getWidth() - 125 - 400, 250);
		neededScroll.setVisible(true);
		this.add(neededScroll);
		
		String mess2 = "To mark classes as taken select from right side then click 'Mark As Taken' to apply.";
		JLabel message2 = new JLabel(mess2);
		message2.setSize(800, 25);
		message2.setLocation(mainWindow.getWidth() / 2, 200);
		message2.setVisible(true);
		this.add(message2);
		
		String mess3 = "To remove a class select a class from the left and click 'Remove' to apply the change.";
		JLabel message3 = new JLabel(mess3);
		message3.setSize(800, 25);;
		message3.setLocation(mainWindow.getWidth() / 2, 220);
		message3.setVisible(true);
		this.add(message3);
		
		JButton mark = new JButton("Mark as Taken");
		mark.setSize(150, 30);
		mark.setLocation(mainWindow.getWidth() / 2 - 75, 350);
		mark.setVisible(true);
		
//listener here		
		mark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(neededList.getSelectedIndex() != -1) {
					//something is actually selected
					
					//get the id number
					String selected = list.getSelectedValue();
					int index = selected.indexOf('>');
					
					String temp = selected.substring(0, index);
					int id = Integer.parseInt(temp);

					String code = neededList.getSelectedValue();
					index = -1;
					index = code.indexOf(' ');
					code = code.substring(0, index);
					
					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					Calendar cal = Calendar.getInstance();
					String date = dateFormat.format(cal.getTime());
					
					
					sqlFunctions sqlFunc = new sqlFunctions();
					sqlFunc.addClass(tempConn, code, id, date);
					
					//update the lists
					takenListModel.clear();
					neededListModel.clear();
					
					fillLists(tempConn);
					
				}
			}
		});
		
		
		
		this.add(mark);
		
		JButton remove = new JButton("Remove");
		remove.setSize(150, 30);
		remove.setLocation(mainWindow.getWidth() / 2 - 75, 425);
		remove.setVisible(true);
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//get the id number
				String selected = list.getSelectedValue();
				int index = selected.indexOf('>');
				
				String temp = selected.substring(0, index);
				int id = Integer.parseInt(temp);

				//get the class code
				String code = takenList.getSelectedValue();
				index = -1;
				index = code.indexOf(' ');
				code = code.substring(0, index);
				
				sqlFunctions sqlFunc = new sqlFunctions();
				sqlFunc.deleteClass(tempConn, code, id);
				
				//update the lists
				takenListModel.clear();
				neededListModel.clear();
				
				fillLists(tempConn);

			}
			
		});
		
		this.add(remove);
		
		
		
		
		this.setVisible(true);
	}
	
	private void update(String match, Connection conn) {
		int matchLength = match.length();
		listModel.clear();
		try {
			rs.beforeFirst();
			while(rs.next()) {
				String temp = rs.getString("lastName");
				temp = temp.substring(0, matchLength);
				if(temp.toUpperCase().equals(match.toUpperCase())) {
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

	//fills the taken and needed lists when the query button is clicked
	private void fillLists(Connection conn) {
		//clear the lists
		takenListModel.clear();
		neededListModel.clear();
		
		
		//get the first token from the string in the list that is selected
		String selected = list.getSelectedValue();
		int index = selected.indexOf('>');
		
		String temp = selected.substring(0, index);
		int id = Integer.parseInt(temp);
		
		sqlFunctions sqlFunc = new sqlFunctions();
		employeeFunctions empFunc = new employeeFunctions();
		int deptID = empFunc.getEmployeesDepartmentID(conn, id);
		
		ClassFunctions classFunc = new ClassFunctions();
		
		ResultSet rs2 = sqlFunc.getPersonsClasses(conn, id);
		ResultSet rs3 = sqlFunc.getRequiredClassesByDept(conn, id);	//required

		try {
			int count = 0;
			while(rs2.next()) {
				++count;
				String description = classFunc.getDescriptionFromCode(conn, rs2.getString("classcode"), 
						deptID);
				String output = String.format("%-25s%-25s%-100s", rs2.getString("classcode"), 
						rs2.getString("dateTaken"), description);
				
				takenListModel.addElement(output);
			}
			
			
			
			rs2.beforeFirst();
			rs3.beforeFirst();
			while(rs3.next()) {
				boolean exitLoop = false;
				rs2.beforeFirst();
				while(rs2.next() && exitLoop == false) {
					
					if(rs2.getString("classcode").equals(rs3.getString("classcode"))) {
						//has class exit the loop
						exitLoop = true;
					} 
				}
				if(exitLoop == false) {
					//doesn't have the class
					String str = rs3.getString("classcode") + "    " + classFunc.getDescriptionFromCode(conn, 
							rs3.getString("classcode"), deptID);
					
					
					neededListModel.addElement(str);
				}
				
			}

		
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		
		
	}
	
	
	
	private Font h1 = new Font("Serif", Font.BOLD, 25);
	private DefaultListModel listModel, takenListModel, neededListModel;
	private JList<String> list;
	
	private ResultSet rs;
	
	private int empID, departmentID;

}
