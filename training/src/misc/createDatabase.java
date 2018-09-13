package misc;

import java.sql.*;

import javax.swing.JOptionPane;

public class createDatabase {
	//create new database return true if successful and false if not
	public Boolean createNewDatabase() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:~/staffDevelopment", "sa", "");
			String SQL = "";
			System.out.println(conn);
			if (conn != null) {
				System.out.println("connection good");
				
				
				//create the Department table
				Statement statement = conn.createStatement();
				SQL = "CREATE TABLE department(id INT PRIMARY KEY AUTO_INCREMENT, "
						+ "departmentName VARCHAR(50) NOT NULL);";
				
				statement.execute(SQL);
				System.out.println("department created");
				
				SQL = "ALTER TABLE department SET REFERENTIAL_INTEGRITY FALSE";
				statement.execute(SQL);
				
				
				
				
				//create the employee table
				SQL = "CREATE TABLE employee(employeeID INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(50) NOT NULL, " +
						"lastName VARCHAR(50) NOT NULL, departmentID INT, position VARCHAR(50) NOT NULL) " ;
				
				statement.execute(SQL);
				System.out.println("employee created");
				
				SQL = "ALTER TABLE employee ADD FOREIGN KEY (departmentID) REFERENCES department(id);";
				System.out.println("foreign key for departmentID in employee references department(id)");
				
				SQL = "ALTER TABLE employee SET REFERENTIAL_INTEGRITY FALSE";
				statement.execute(SQL);
				
				
				
				
				
				
				//create the requred_class_by_dept, have to wait to add foriegn key referenct for
				//classCode til class table is built
				SQL = "CREATE TABLE required_class_by_dept(departmentID INT, classCode VARCHAR(10) " +
					", description VARCHAR(100))"; 
				statement.execute(SQL);
				System.out.println("required created");
				
				SQL = "ALTER TABLE required_class_by_dept ADD FOREIGN KEY (departmentID) REFERENCES  department(id);";
				statement.execute(SQL);
				System.out.println("foreign key in required for departmentID references department(id)");
				
				
				
				
				//create the classes table
				SQL = "CREATE TABLE classes (classCode VARCHAR(10) PRIMARY KEY, employeeID INT, dateTaken VARCHAR(10));";
				statement.execute(SQL);
				System.out.println("classes created");
				
				SQL = "ALTER TABLE classes ADD FOREIGN KEY (employeeID) REFERENCES employee(employeeID);";
				statement.execute(SQL);
				System.out.println("foreign key in classes for employeeID references employee(employeeID)");
				
				
				//set the foreign key in required_class to reference to classes
				//SQL = "ALTER TABLE required_class_by_dept ADD FOREIGN KEY (classCode) REFERENCES classes(classCode);";
				
				SQL = "ALTER TABLE classes ADD FOREIGN KEY (classCode) REFERENCES required_class_by_dept(classCode);";
				statement.execute(SQL);
				System.out.println("foreign key in required for classCode references classes(classCode");
				
				
				
			} else {
				JOptionPane.showMessageDialog(null, "Failed to create database");
				System.err.println(SQL);
			}
			
			conn.close();
			
			return true;
			
		}catch (SQLException e) {
			System.err.println(e);
		}
		
		
		
		
	
		return false;
	}
	
};
