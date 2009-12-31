package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

 
	static String driver = "com.mysql.jdbc.Driver";
	static	String url = "jdbc:mysql://localhost:3306/test";
	static	String userName = "root";
	static	String password = "131421";
	
	
	public static Connection getConn(){
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		 
		
	}
	
}
