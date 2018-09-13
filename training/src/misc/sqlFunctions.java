package misc;

import java.sql.*;

import javax.swing.JOptionPane;

public class sqlFunctions {
	//get connection to the database and return it
	//
	//parameters:  String for userName, String for password
	//
	//return: Connection object
	public Connection getDbConnection(String userName, String password){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/staffDevelopment;", 
					userName, password);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
					"Connection to database failed.\nCheck User name and password");
			return null;
		} 
		//return connection, check where it was called from if null was returned before proceeding
		return conn;
		
	}
	
	//closes the connection
	//
	//parameters:   Connection to close
	//
	//return:   none
	public void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//adds a department to the department table
	//
	//parameters:  Connection, String for the department to add
	//
	//return:  different statements depending on where it exited
	public String addDepartment(Connection conn, String department) {
		//make sure string is not null
		if (department == "" || department == null) {
			return "All fields must be filled out";
		}
		
		try {
			if (!conn.isClosed() || conn != null) {
				//connection is valid
				PreparedStatement statement = conn.prepareStatement
						("INSERT INTO department(departmentName) VALUES(?)");
				statement.setString(1, department);
				
				int i = statement.executeUpdate();
				return i + " records updated.";
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		
		return "through to end";
	}
	
	
	//adds an employee to the employee table
	//
	//Parameters: Connection, String for firstName, String for lastName, int for departmentID,
	//				String for position
	//
	//return: string depending on where it exits
	public String addEmployee(Connection conn, String firstName, String lastName, int department, String position) {
		//make sure all fields are full
		if(firstName == "" || lastName == "" || department == 0 || position == "") {
			return "fill all fields";
		}

		
		try {
			if(!conn.isClosed() && conn != null) {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO employee(firstName, lastName,"
						+ "departmentID, position) VALUES(?,?,?,?)");
				
				stmt.setString(1, firstName);
				stmt.setString(2, lastName);
				stmt.setInt(3, department);
				stmt.setString(4, position);
				
				int i = stmt.executeUpdate();
				
				return i + " records updated";
				
			}
			
		} catch (SQLException e) {
			System.err.println(e);
		}

		return "through to end";
	}

	//used to delete an employee
	public String deleteEmployee(Connection conn, String firstName, String lastName, int department, String position) {
		try {
			if(!conn.isClosed() && conn != null) {
				String sql = "select employeeID from employee where firstName='" + firstName +
						"' and lastName='" + lastName + "' and departmentID='" + department + 
						"' and position='" + position + "'";
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				int index = -1;
				while(rs.next()) {
					index = rs.getInt("employeeID");
				}
				
				if(index != -1) {
					PreparedStatement stmt2 = conn.prepareStatement("delete from employee where (employeeID = " + index + ")");
					stmt2.executeUpdate();
					return "1 record deleted.";
				}
				
				
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		
		return "through to end";
	}
	
	
	//adds a class to the classes table, this is just a list of the classes that are available
	public String addClass(Connection conn, String classCode, int empID, String date) {
		if(classCode == null || empID == 0 || date == null) {
			System.out.println("all fields must be filled out");
			return "all fields must be filled out";
		}
		
		try {
			if (!conn.isClosed() &&  conn!= null) {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO classes VALUES(?,?,?)");
				stmt.setString(1, classCode);
				stmt.setInt(2, empID);
				stmt.setString(3, date);
				
				int i = stmt.executeUpdate();
				
				return i + " records updated";
				
			}
		} catch(SQLException e) {
			System.err.println(e);
		}
		
		return "through to the end";
	}
	
	//adds a class to the required_class_by_dept table, these are the classes that are needed by a 
	//department, takes a connection, interger for the department, and the classcode as parameters
	public String addRequired(Connection conn, int deptID, String classCode, String desc) {
		if(classCode == "" || desc == "") {
			return "must fill out both fields";
		}
		
		try {
			if(!conn.isClosed() && conn != null) {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO required_class_by_dept " +
						"VALUES(?,?, ?)");
				stmt.setInt(1, deptID);
				stmt.setString(2, classCode);
				stmt.setString(3, desc);
				
				int i = stmt.executeUpdate();
				
				return i + " records updated";
			}
		} catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return "through to the end";
	}
	
	
	//return a ResultSet of all departments
	public ResultSet getAllDepartments(Connection conn){
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement("select departmentName from department");
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	//returns a count of how many departments there are
	public int getDepartmentCount(Connection conn) {
		int count = -1;
		try {
			PreparedStatement stmt = conn.prepareStatement("select count(*) as rowcount from department");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("rowcount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	//gets all the classes that are required by a particular department, then returns a resultset
	public ResultSet getRequiredClassesByDept(Connection conn, int deptID) {
		ResultSet rs1 = null;
		String sql = "select classCode from required_class_by_dept where departmentID = '" +
				deptID + "'";
		
		try {
			//PreparedStatement stmt = conn.prepareStatement("select classCode from required_class_by_dept" 
			//		+ " where departmentID = '" + deptID + "'");
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			rs1 = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		return rs1;
	}
	
	
	//gets all the classes with no duplicates
	public ResultSet getAllClassesNoDuplicate(Connection conn) {
		ResultSet rs = null;
		String sql = "select distinct classCode from required_class_by_dept";
		
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		
		return rs;
	}
	
	//gets all the classes that a person has, takes an int that is the persons id,returns a resultset
	public ResultSet getPersonsClasses(Connection conn, int empID) {
		ResultSet rs1 = null;
		String sql = "select * from classes where employeeID = '" + empID + "'";
		
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			
			//PreparedStatement stmt = conn.prepareStatement("select * from classes " +
			//		"where employeeID = '" + empID + "'");
			rs1 = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		return rs1;
	}
	
	public void deleteClass(Connection conn, String code, int empID) {
		try{
			String sql = "delete from classes where classcode = '" + code + "' and employeeID = " + empID;
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	
}
