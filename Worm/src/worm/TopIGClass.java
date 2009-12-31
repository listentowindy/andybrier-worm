package worm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import util.Constant;
import util.DBOperate;



/**
 * 按照某一类API对top IG 就行过滤
 * @author andy
 *
 */
public class TopIGClass {

	
	public static final String FILE = "File IO.apifilter";  //文件IO
	public static final String REG = "Registry.apifilter";  //寄存器
	public static final String DLL = "Dynamic-Link Libraries.apifilter";  //DLL
	public static final String MEMORY = "Memory Management.apifilter";  //内存
	public static final String PROCESS = "Processes and Threads.apifilter";  //进程
	public static final String SOCKET = "Windows Sockets.apifilter";  //文件IO
	public static final String NET = "Network Management.apifilter";  //网络
	
	private  List<Integer>  postion;                   //某一类api在api.txt中的位置
	
	private String tableName;
	
	
	DBOperate dbOp;
	Connection  con;        
	PreparedStatement ps;
	ResultSet rs;
	
	
	/**
	 * 构造函数
	 * @param className API类别
	 * @param tableName 存放提取出来的API表名
	 */
	public TopIGClass(String className,String tableName) {
		 this.tableName = tableName; 
		 postion = new LinkedList<Integer>();
		 dbOp = new DBOperate();
		 con = dbOp.getCon();
		 setPosition(className);
	}



	/**
	 * 过滤topig表
	 */
	public void filterIG(){
		
		//PrintUtil.printList(postion);
		
		String name = "" ;
		double ig;
		
		//批量插入
		String batchInsert  =  "insert into "+ tableName + " (name,ig) values (?,?)";
		
		String sqlAll = "select * from topig";
       
        
        try {
       
         //获取topig表中的所有数据
         rs = dbOp.select(sqlAll);
         
         con.setAutoCommit(false);	
         ps = con.prepareStatement(batchInsert,ResultSet.TYPE_SCROLL_SENSITIVE,
      	    		ResultSet.CONCUR_READ_ONLY);  
        	
			while(rs.next()){
				name = rs.getString("name");
				ig = rs.getDouble("ig");
				
				//如果name中存在此类api
				if(containAPI(name, postion)){
				    ps.setString(1,name);
				    ps.setDouble(2, ig);
				    ps.addBatch();
				    
				}
			}
		
			ps.executeBatch();
			con.commit();
			con.close();
			
			System.out.println("处理完毕");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	/**
	 * 得到某一类所有的API在API List中数字编号
	 * @param className
	 */
	private void setPosition(String className){
		
		File classedFile = new File(Constant.ClassedAPIFileDir,className);//已经分类之后的某类api文件
		File apiListFile = new File(Constant.APIListFile);  //API list文件
		
		String line = "" ;
		
		try {
			BufferedReader br  = new BufferedReader(new FileReader(classedFile));
			while((line = br.readLine())!=null){
				if(! "".equals(line)){
					int number = getLineNumber(line, apiListFile);
					postion.add(number);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 得到某一个字符串在文件中的行数.从0开始
	 * @param line
	 * @param file
	 * @return
	 */
	private int getLineNumber(String line,File file){
		int number = 0;
		String word = "" ;
		try {
			BufferedReader br  = new BufferedReader(new FileReader(file));
			while( (word = br.readLine())!=null ){
				if(word.equals(line)){
					break;
				}
				number++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return number;
	}
	
	//判断name中是否含有某一类api
	private boolean containAPI(String name,List<Integer> api){
	     
	    boolean result = false;
		
		for(int i : api){
			if(name.indexOf(""+i) != -1){
				result = true;
				break;
			}
		}
		return result;
		
		
	}
	
	//test
	public static void main(String args[]){
		TopIGClass bean = new TopIGClass(TopIGClass.FILE,"file_topig");
		bean.filterIG();
	}
	
	
}
