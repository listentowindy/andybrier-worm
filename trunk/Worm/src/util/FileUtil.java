package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;



/**
 * 文件操作的类
 * @author Administrator
 *
 */
public class FileUtil {

	
	
	/**
	 * 遍历文件夹，得到里面所有的文件（文件夹可能嵌套）
	 * @param file：要遍历的文件夹
	 * @return 文件夹中所有的文件
	 */
	public static List<File> getFiles(File file){
		
	   List<File> result = new LinkedList<File>();
	   
		//如果file是个文件夹
       if(file.isDirectory()){
		
    	 File tempfiles[] = file.listFiles();
	     for(File f : tempfiles){
	    	   if(f.isDirectory()){
	    		   getFiles(f);  //递归
	    	   }else
	    		 result.add(f);
	       }
		    
       }else{
    	   result.add(file);  //file是个文件
       }
       
       return result;
	}
	public static List<File> getFiles(String fileDir){
		File file = new File(fileDir);
		return getFiles(file);
		
	}
	
	/**
	 * 将一个String数组写到一个文件之中，每一项换一行
	 * @param from 要写到文件中的数据
	 * @param url  目标文件
	 */
	public static void writeStringArrayToFile(String[] from, String url){
		
		File f = new File(url);
	    PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(f);
			
			for(String line : from){
				pw.print(line);
				pw.print("\n");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			pw.close();
		}
		
		
		
		
	}
	
	/**
	 * 文件后面添加一行
	 * @param line
	 * @param file
	 */
	public static void appendToFile(String line, String file){
		FileWriter fw;
		try {
			fw = new FileWriter(file,true);
			PrintWriter pw  = new PrintWriter(fw);
			pw.append("\n");
			pw.append(line);
			
			fw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	
	
	
	
	
	
	/**
	 * 从文件中得到STring数组
	 * @return
	 */
	public static  String[] getArrayListFromFile(String file){
		 
		List<String> tmp = new LinkedList<String>();
		
		File f = new File(file);
		String line = "" ;
	    int number = 0 ;
	 
		try {
			BufferedReader 	bf = new BufferedReader(new FileReader(f));
			  while ((line = bf.readLine()) != null) {
			      tmp.add(line);
				  number++;
			  }
			  
			  bf.close();
		}
		 catch (Exception e) {
			 
				e.printStackTrace();
			} 
	 				
		return (String[])tmp.toArray(new String[tmp.size()]);
	}
	
	/**
	 * 读文件，得到一个String.一行一行的读
	 * @param file
	 * @return
	 */
	public static String getStringFormFile(File file){
		StringBuilder sb = new StringBuilder();
		String line = "";
		try {
			BufferedReader 	bf = new BufferedReader(new FileReader(file));
			  while ((line = bf.readLine()) != null) {
				  sb.append(line);
			  }
		   bf.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
		
	}
	
	
	/**
	 * 将一个List写到文件中，注意要重写List中Object的ToString方法
	 * 写完一个对象后换行
	 * @param filePath
	 * @param fromList
	 */
	
	public static void WriteListToFile(String filePath, List fromList){
		
		try {
			File newFile = new File(filePath);
			PrintWriter pw = new PrintWriter(newFile);
			
		   for(Object o : fromList){
			   pw.write(o.toString());
			   pw.write("\n");
		   }
		  
		   pw.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 生成目录：
	 * @param dir 要生成目录的路径
	 */
	public static void makeDir(String dir){
		File f = new File(dir);
		if(f.exists()){
			return;
		}else{
			f.mkdir();
		}
		
	}
 
		
		
	}
	
	
	
	 
