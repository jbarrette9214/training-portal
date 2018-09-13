package misc;

import java.sql.*;
import java.util.*;

public class TestStuff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sqlFunctions sqlFunc = new sqlFunctions();
		Connection conn = sqlFunc.getDbConnection("sa", "");
		
		ResultSet rs = sqlFunc.getRequiredClassesByDept(conn, 1);
		
		System.out.println(rs.toString());
		try {
			while(rs.next()) {
				System.out.println(rs.getString("classcode"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//sqlFunc.addRequired(conn, 1, "nurs102");
		//sqlFunc.addClass(conn, "nurs102", "taken", 1, "12/25/2018");
		
		//sqlFunc.addRequired(conn, 1, "nurs103");
		
		
		ResultSet rs2 = sqlFunc.getPersonsClasses(conn, 1);
		ResultSet rs3 = sqlFunc.getRequiredClassesByDept(conn, 1);	//required
		try{
			System.out.println("has");
			while(rs2.next()) {
				System.out.println(rs2.getString("classcode") + "  -  " + rs2.getInt("employeeID"));
				
			}
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		try {
			System.out.println("required");
			while(rs3.next()) {
				System.out.println(rs3.getString("classcode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		//compare the result sets and find what is missing
		System.out.println("\n\nneeded");
		
		List<String> needed = new ArrayList<String>();
		
		try {
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
					//System.out.println(rs3.getString("classcode"));
					needed.add(rs3.getString("classcode"));
				}
				
			}

			
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		for(int i = 0; i < needed.size(); ++i) {
			System.out.println(needed.get(i));
		}
	
		System.out.println("Testing getEmployeeName");
		employeeFunctions empFunc = new employeeFunctions();
		String name = empFunc.getEmployeeName(conn, 1);
		System.out.println(name);
	
		System.out.println("\nTesting getEmployeeID");
		int id = empFunc.getEmployeeID(conn, "Michelle", "Hill");
		System.out.println(id);
		
		System.out.println("\nTesting getEmployeePositionByID");
		String pos = empFunc.getEmployeePositionByID(conn, 1);
		System.out.println(pos);
		
		
		System.out.println("\nTesting getEmployeePositionByName");
		String pos2 = empFunc.getEmployeePositionByName(conn, "Hill", "Michelle");
		System.out.println(pos2);
		
		System.out.println("\nTesting getEmployeeDepartmentByID");
		String depart = empFunc.getEmployeeDepartmentByID(conn, 1);
		System.out.println(depart);
		
		System.out.println("\nTesting getEmployeeDepartmentByName");
		String depart1 = empFunc.getEmployeeDepartmentByName(conn, "Hill", "Michelle");
		System.out.println(depart1);
		
		ClassFunctions classFunc = new ClassFunctions();
		
		System.out.println("\nTesting getDescriptionFromCode");
		System.out.println("nurs101 description");
		String desc = classFunc.getDescriptionFromCode(conn, "nurs101", 1);
		System.out.println(desc);
		
		
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		
	}

}
