package util;


import java.io.*;
import java.sql.*;
import java.util.*;


/**
 * 	建立一个数据库连接池，用于提供和回收数据库连接。<p>
 * 	建立连接池的目的，一是为了代码复用，而是为了避免频繁的创建释放数据库连接。<p>
 * 	public static synchronized DBConnectionPool getInstance()<p>
 * 	返回惟一实例，如果是第一次调用此方法，则创建该实例.<p>
 * 	public synchronized void freeConnection(Connection con)<p>
 * 	将不再使用的连接返回给连接池.<p>	
 * 	public synchronized Connection getConnection()<p>
 * 	从连接池申请一个可用连接。如没有空闲的连接且当前连接数小于最大连接数的限制，则创建新的连接.	
 *	@author shi_xiaoping
 *  Start Time:2008-3-28
 */
public class DBConnectionPool {
	private int minConn=1;	//最小连接数
	private int maxConn=20;	//最大连接数	
	private int maxWait=-1;	//最长等待
	private int stackcapacity=50;
	private String driver=null;
	private String url=null;	
	private String user=null;
	private String password=null;	

	private int connAmount=0;
	private Stack<Connection> connStack=new Stack<Connection>();//使用STACK来保存数据库连接
	private static DBConnectionPool instance=null;	
	/**	 
	 *	返回惟一实例，如果是第一次调用此方法，则创建该实例.
	 */	
	public static synchronized DBConnectionPool getInstance(){	
		if(instance==null){
	         instance=new DBConnectionPool(); 
		}
		return instance;  		
	}	
	private DBConnectionPool(){
		getProperty();//读取数据库属性文件		
		//注册驱动程序			
		try{
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Class.forName(driver).newInstance();			
		}catch(Exception e){
			 System.out.println("无法正常加载驱动！");
	  		 e.printStackTrace();  				
		}		    
		//根据最少连接数生成连接
		for(int i=0;i<minConn;i++){
			connStack.push(newConnection());
		} 		
	}	
	/**
	 *	从连接池申请一个可用连接。没有空闲的连接且当前连接数小于最大连接数，则创建新的连接.
	 */
	public synchronized Connection getConnection(){
		
		Connection con=null;		
		if(!connStack.empty()){
			//获取一个可用的连接
			con=(Connection)connStack.pop();
		}else if(connAmount<maxConn){
			con=newConnection();      
		}
		else{
			try{
				wait(10000);
		    	return getConnection();           
			}catch(Exception e){
				//用户等待
				e.printStackTrace();
			}  
		}
		System.out.println("获得新的链接成功");
		return con;    
	}		
	/**
	 * 将不再使用的连接返回给连接池.
	 */  
	public synchronized void freeConnection(Connection con){
		if (connStack.size() < stackcapacity) {
			connStack.push(con);//将指定连接入栈
			notifyAll();        //唤醒正在等待连接的进程	
		} else {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}
	/**
     * 创建新的连接
     */
	private Connection newConnection(){	
		Connection con=null;
		try{
			con=DriverManager.getConnection(url,user,password);
	    	connAmount++;	     
	 	}catch(SQLException e){	
	 		e.printStackTrace();
	        return null;
		}
	    return con;		
	}
	
	/**
	 * 读取数据库属性配置文件datebase.properties
	 */
	private void getProperty()	{
		
		Properties prop = new Properties();
		try	{
			/*			
			String propertyPath=servletContext.getRealPath("/WEB-INF");
			System.out.println(propertyPath);
			FileInputStream in = new FileInputStream(propertyPath+"\\"+"datebase.properties");
			prop.load(in);	
			*/
			InputStream in = this.getClass().getResourceAsStream(
				"datebase.properties");	
			prop.load(in);
			driver = prop.getProperty("driver");			
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");	
			minConn=Integer.parseInt(prop.getProperty("minConn"));
			maxConn=Integer.parseInt(prop.getProperty("maxConn"));
			maxWait=Integer.parseInt(prop.getProperty("maxWait"));
			stackcapacity=Integer.parseInt(prop.getProperty("stackcapacity"));
		}
		catch(FileNotFoundException e){			
			e.printStackTrace();
			System.out.println("数据库属性配置文件无法找到！");
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("读取数据库属性配置失败！");
		}
		
	}

}

