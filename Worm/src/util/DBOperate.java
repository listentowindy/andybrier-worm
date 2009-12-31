package util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
/** 
 * @author 作者：袁启侠 
 * @version 创建时间：2009-10-8 下午09:50:04 
 */
public class DBOperate {
	private Connection con = null;
//	private PreparedStatement pstm;
	private Statement st;
	private ResultSet rs;
    private String error = "";
    
    
	public DBOperate() {
		/**
		 * 调用静态方法获得数据库连接
		 */
		creatConnection();
	}
	/*
	 * 利用java类DBConnectionPool获得数据库连接
	 */
	public boolean creatConnection() {
		
		try {
			 // request.setCharacterEncoding("GBK");
			 con=DBConnectionPool.getInstance().getConnection();						 
			 con.setAutoCommit(true);			 
		} catch (Exception e) {
			System.out.println("获得数据库链接报错" + e.getMessage());
			error=e.getMessage();
			e.printStackTrace();
			return false;
		}
		return true;
	}
    private void createStatement() {		
			try {
				st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
			} catch (Exception ex) {
				error = ex.getMessage();
				ex.printStackTrace();
			}
		
	}
    /**
     * 用于各种select语句的执行
     * @param sql
     * @return
     */
 	public ResultSet select(String sql) {
     	
     	createStatement();      
         try {
             rs = st.executeQuery(sql);  
         } catch (Exception e) {
            error = sql+" "+e.getMessage();          
            return null;
         }
         return rs;
     }
	/**
     * 用于除select外其他语句的执行
     * @param sql
     * @return
     */
 	public boolean execute(String sql) {
     	createStatement();
         try {
             st.executeUpdate(sql);
             st.close();
         } catch (Exception e) {
            error = sql+" "+e.getMessage();          
            return false;
         }
         return true;
     }
 	/**
     * 获得全局变量error
     * @return
     */
     public String getError() {    
         return error;
     }
     public void closeConnection(Connection conn) {
 		if (conn != null) {
 			try {
 				conn.close();
 				conn = null;
 			} catch (Exception ex) {
 				ex.printStackTrace();
 			}
 		}
 	}
     public void closeConnection() {
  		if (con != null) {
  			try {
  				con.close();
  				con = null;
  			} catch (Exception ex) {
  				ex.printStackTrace();
  			}
  		}
  	} 
    /**
 	 * 关闭Statement
 	 * 
 	 * @param stmt
 	 */
     public void closeStatement(Statement stmt) {
 		if (stmt != null) {
 			try {
 				stmt.close();				
 			} catch (Exception ex) {
 				ex.printStackTrace();
 				error=ex.getMessage();
 			}
 		}
 	}
     public void closeStatement() {
  		if (st != null) {
  			try {
  				st.close();				
  			} catch (Exception ex) {
  				ex.printStackTrace();
  				error=ex.getMessage();
  			}
  		}
  	}
      /**
 	 * 关闭ResultSet对象。
 	 */ 
     public void closeResultSet(ResultSet rs) {
     	
 		if (rs != null) {
 			try {
 				rs.close();				
 			} catch (Exception e) {
 				e.printStackTrace();
 				error=e.getMessage();
 			}
 		}
 	}
     
     /**
      * 暴露connection对象
      * @return
      */
	public Connection getCon() {
		return con;
	}
     
     
     
}
