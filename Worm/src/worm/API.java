package worm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;
/**
 * 给定一个目录，遍历目录，获取所有的API集合（Set）
 * @author Administrator
 *
 */
public class API {
	
	  private	Set<String> apis = new HashSet<String>();  //得到的API集合（没有重复）
	  private   List<File>  files;                 //要处理的所有的文件	
	  private   String dir;                //要处理的目录	
		
	  
	  public API(String dir){
		  this.dir = dir;
	  }
	  
	 
	  
	  /**
		 * 得到目录下所有API集合
		 * @param sDir 存放API文件的目录
		 * @return
		 */
		public String[] getAPI(){
		   
			File file = new File(dir);
			files = FileUtil.getFiles(file);   //遍历文件夹，获得所有的要处理的文件
			int i  = 0 ;
			for(File f : files){
				getAPIFromFile(f);
				//显示进度，换行
				i++;
				System.out.print(".");
				if(i%80 == 0){
					System.out.print("\n");
				}
			}
			System.out.print("\n");
			System.out.printf("一共处理了 %d 个文件，获得了 %d 个API",files.size(),apis.size() );
			
			return (String[])this.apis.toArray(new String[apis.size()]);
			
		}
		
		/**
		 * 从文件中得到API的Set
		 explorer*0x668*IsBadReadPtr*lp:0xDEECF8, ucb:0x10*0x0*SUCCESS*0
		 * @param file
		 */
		public void getAPIFromFile(File file){
		     
			String line = "";
			try {
				  BufferedReader bf = new BufferedReader(new FileReader(file));
				
				  while ((line = bf.readLine()) != null) {
					  String[] st = line.split("[*]");
					  if(st.length < 3){
						  System.out.println("发生了错误：" +line);
					  }else{
						  apis.add(st[2]);
					  }
				  }
				  
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	 
	 
		
		
}
