package worm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


import util.Constant;
import util.DBUtil;
import util.StringUtil;
/** 
 * 生成每个串在文件中次数
 * @author 作者：袁启侠 
 * @version 创建时间：2009-10-9 下午02:23:35 
 */
public class Sequence {
	private List<File> virus = new LinkedList<File>(); // 要处理的病毒文件
	private List<File> worm = new LinkedList<File>(); // 要处理的蠕虫文件
	private List<File> trojan = new LinkedList<File>(); // 要处理的木马文件
	private List<File> normal = new LinkedList<File>(); // 要处理的正常文件
	
	String sql_select = "select name from sequence where name = ?";
	 
	
	Connection conn = null;
	PreparedStatement ps_select = null;
	Statement stat = null;
	 
	
	
	private String dir = Constant.FeatherMatrix;// 目标文件夹
	 
	private ResultSet rs;
	public Sequence(){
		
		//DB
		conn = DBUtil.getConn();
		try {
			ps_select = conn.prepareStatement(sql_select);
			stat = conn.createStatement();
		 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   
        //File		
		File mulu=new File(dir);
		getNormalFiles(mulu);
		getFiles(mulu,worm,"XWorm");
		getFiles(mulu,trojan,"XTrojan");
		getFiles(mulu,virus,"XVirus");
	}
	public Sequence(File mulu){
	 
		getNormalFiles(mulu);
		getFiles(mulu,worm,"XWorm");
		getFiles(mulu,trojan,"XTrojan");
		getFiles(mulu,virus,"XVirus");
	}
	public void createSequences(){
		System.out.println("开始处理病毒文件……");
		handleFiles(virus,"virus");
		System.out.println("开始处理蠕虫文件……");
		handleFiles(worm,"worm");
		System.out.println("开始处理木马文件……");
		handleFiles(trojan,"trojan");
		System.out.println("开始处理正常文件……");
		handleFiles(normal,"benign");
		System.out.println("done");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	int i = 0;
	public void handleFiles(List<File> file,String type){
		
		for (File f : file) {
			i++;
			System.out.print(".");
			if(i%80==0){
				System.out.print("\n");
			}
			handleFile(f,type);
			 
		}
	}
	public void handleFile(File file,String type){
		String line="",tmp="",sql="";
		try{
			FileReader fr=new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			while ((line = bf.readLine()) != null) {
				
				// 准备参数
				List<String> list = StringUtil.cut(line,"  ");
				tmp = list.get(0);
				 ps_select.setString(1, tmp);
				 rs= ps_select.executeQuery();
				 
				 //表中存在该项 更新
				 if(rs.next()){
				 
					 sql="update sequence set "+type+"="+type+"+1 where name='" +tmp+"'";
					 
				 }else{
					 sql="insert into sequence(name,"+type+") values('"+tmp+"',1)"; 
				 }
				 
				    stat.executeUpdate(sql);
				 
			}
			bf.close();
			fr.close();
			rs.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	/**
	 * 遍历文件夹，得到里面所有的文件（文件夹可能嵌套）
	 * @param 
	 * @return
	 */
	public void getFiles(File file,List<File> lf,String mark){
		
		//如果file是个文件夹
       if(file.isDirectory()){
		
    	 File tempfiles[] = file.listFiles();
	     for(File f : tempfiles){
	    	   if(f.isDirectory()){
	    		   getFiles(f,lf,mark);  //递归
	    	   }else{
	    		   if(f.getName().startsWith(mark))
	    		   lf.add(f);
	    	   }
	       }
		    
       }else{
    	   lf.add(file);  //file是个文件
       }		
	}
	public void getNormalFiles(File file){
		
		//如果file是个文件夹
       if(file.isDirectory()){
		
    	 File tempfiles[] = file.listFiles();
	     for(File f : tempfiles){
	    	   if(f.isDirectory()){
	    		   getNormalFiles(f);  //递归
	    	   }else{
	    		   if(!f.getName().startsWith("XTrojan")&&!f.getName().startsWith("XVirus")&&!f.getName().startsWith("XWorm"))
	    		    normal.add(f);
	    	   }
	       }
		    
       }else{
    	   if(!file.getName().startsWith("XTrojan")&&!file.getName().startsWith("XVirus")&&!file.getName().startsWith("XWorm"))
    		   normal.add(file);  //file是个文件
       }		
	}
 

}
