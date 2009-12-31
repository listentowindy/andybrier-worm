package worm;
 
import java.sql.ResultSet;
 
 
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.*;

import util.Constant;
import util.DBOperate;
import util.StringUtil;
/** 
 * @author 作者：袁启侠 
 * 为每一个类别（病毒，蠕虫，木马）生成weka属性文件：ARFF文件
 * @version 创建时间：2009-10-8 下午02:38:15 
 */
public class Arff {
	private List<File> virus = new LinkedList<File>(); // 要处理的病毒文件
	private List<File> worm = new LinkedList<File>(); // 要处理的蠕虫文件
	private List<File> trojan = new LinkedList<File>(); // 要处理的木马文件
	private List<File> normal = new LinkedList<File>(); // 要处理的正常文件
	private String dir = Constant.VerifyMatrix;     // 验证文件的矩阵文件夹
    private String attributes[];                   //从topig表中读到的所有记录，取name
	private String filename="weka/total.arff";		  //生成total weka属性文件的 文件名称
    
    private int re[];
    DBOperate db;
	public Arff() {
		db=new DBOperate();
		createAttributes(); //根据数据库中的表，得到所有的topig
		File f=new File(dir);
		getNormalFiles(f);
		getFiles(f,worm,"XWorm");
		getFiles(f,trojan,"XTrojan");
		getFiles(f,virus,"XVirus");
	}
	public Arff(File f) {
		db=new DBOperate();
		createAttributes();
		getNormalFiles(f);
		getFiles(f,worm,"XWorm");
		getFiles(f,trojan,"XTrojan");
		getFiles(f,virus,"XVirus");
	}
	
	public Arff(String tableName) {
		db=new DBOperate();
		createAttributes(tableName); //根据数据库中的表，得到所有的topig
		File f=new File(dir);
		getNormalFiles(f);
		getFiles(f,worm,"XWorm");
		getFiles(f,trojan,"XTrojan");
		getFiles(f,virus,"XVirus");
	}
	
	
	
	
	/**
	 * 产生所有文件的weka arff：total.arff
	 */
	public void createTotalArff(){
		String name= getFilename(); //文件total arff文件名称	
		try {
			    File myFilePath = new File("weka",name);
			 
			    FileWriter resultFile = new FileWriter(myFilePath);  
				PrintWriter myFile = new PrintWriter(resultFile); 
				
				writeHeader(myFile);  //write header
				
				myFile.println("@attribute class {virus,worm,trojan,benign}");				
				myFile.println("");
				myFile.println("@data");
			    
				//data for begine
				for (File f : normal) {
					myFile.println(getFileLine(f,"benign"));
				}
				//data for worm
		        for (File f : worm) {
					myFile.println(getFileLine(f, "worm"));
		        }
				 //data for trojan
			     for (File f : trojan) {
						myFile.println(getFileLine(f,"trojan"));
					}
			     //data for virus
				for (File f : virus) {
					 myFile.println(getFileLine(f,"virus"));
				}
				
			  resultFile.close(); 
			  myFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//分别产生3个类别与benign对应的arff文件。可以用来10-cross交叉验证.

	public void createArff(String type){
		String name="weka/"+type+".arff";		
		try {
			  File myFilePath = new File(name);
			  
			    FileWriter resultFile = new FileWriter(myFilePath);  
				PrintWriter myFile = new PrintWriter(resultFile); 
			    
				//write header
				writeHeader(myFile);
				 
				myFile.println("@attribute class {"+type+",benign}");				
				myFile.println("");
				myFile.println("@data");
				for (File f : normal) {
					myFile.println(getFileLine(f,"benign"));
				}
				if(type.equals("worm")){
					for (File f : worm) {
					myFile.println(getFileLine(f,type));
					}
				}else if(type.equals("trojan")){
					for (File f : trojan) {
						myFile.println(getFileLine(f,type));
						}
				}else if(type.equals("virus")){
					for (File f : virus) {
						myFile.println(getFileLine(f,type));
						}
				}else{
					System.out.println("您所输入的类型参数错误");
				}			
			  resultFile.close(); 
			  myFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 产生验证weka文件: 可以分别用来产生训练和验证arff。可以使用weka的库函数自动生成
	 * @param type
	 */
	@Deprecated
	public void createTestArff(String type){
		String test="weka/"+type+"-test.arff";	
		String train="weka/"+type+"-train.arff";		
		
		try {
			  File testFile = new File(test);
			  File trainFile = new File(train);
			  
			  
				PrintWriter pwTest = new PrintWriter(new FileWriter(testFile)); 
				PrintWriter pwTrain = new PrintWriter(new FileWriter(trainFile)); 
				
				//写头
			     writeHeader(pwTest);
			     writeHeader(pwTrain);
				 
				//写class属性
				pwTest.println("@attribute class {"+type+",benign}");				
				pwTest.println("");
				pwTest.println("@data");
				
				pwTrain.println("@attribute class {"+type+",benign}");				
				pwTrain.println("");
				pwTrain.println("@data");
				
				//正常文件
				List<File> testNormal = generateRandomFile(normal, 10);  //从原来的文件中随机取10个作为验证。其余的作为训练
				List<File> trainNormal = removeALl(normal, testNormal);  //去掉刚刚随机取出来的10个
			 
				
				//test
				for (File f : testNormal) {
					pwTest.println(getFileLine(f,"benign"));
				}
				//train
				for (File f : trainNormal) {
					pwTrain.println(getFileLine(f,"benign"));
				}
				
				
				//worm
				if(type.equals("worm")){
					List<File> testWorm = generateRandomFile(worm, 10);
					List<File>  trainWorm = removeALl(worm, testWorm);
 				  for (File f : testWorm) {
				    pwTest.println(getFileLine(f,type));
			     	}
 				  for (File f : trainWorm) {
 					 pwTrain.println(getFileLine(f,type));
 				   	}
				}
				//trojan
				else if(type.equals("trojan")){
					List<File> testTrojan  = generateRandomFile(trojan, 10);
					List<File>  trainTrojan  = removeALl(trojan, testTrojan);
					for (File f : testTrojan) {
						pwTest.println(getFileLine(f,type));
						}
					for (File f : trainTrojan) {
						pwTrain.println(getFileLine(f,type));
						}
				}
				
				else if(type.equals("virus")){
					List<File> testV  = generateRandomFile(virus, 10);
					List<File>  trainV  = removeALl(virus, testV);
					for (File f : testV) {
						pwTest.println(getFileLine(f,type));
						}
					for (File f : trainV) {
						pwTrain.println(getFileLine(f,type));
						}
				}else{
					System.out.println("您所输入的类型参数错误");
				}			
				pwTrain.close(); 
				pwTest.close();
			 // System.out.println(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 对于给定的文件file，产生一个用于.arff文件中的一行/@data文件
	 * @param file
	 * @param type
	 * @return
	 */
	public String getFileLine(File file,String type){
		String result="",tmp="";
		int size=attributes.length;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			
			re=new int[size];
			for(int i=0;i<size;i++){
				re[i]=0;
			}
			String  line;
			while ((line = bf.readLine()) != null) {
			 
				// 准备参数 得到4-gram名称
				List<String> list = StringUtil.cut(line,"  ");
				tmp = list.get(0);
				
				for(int i=0;i<size;i++){
					if(tmp.equals(attributes[i])){
						re[i]=1;
						break;
					}
				}
			}
			 
				bf.close();
				fr.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<size;i++){
			result+=re[i]+" ";
		}
		result+=type;  // 0 1 0 1 0 1 .....0 1 worm
		return result;
	}
	/**
	 * 生成attributes数组，这是本程序工作的基础
	 */
	public void createAttributes(){
		int size;
		ResultSet rs=db.select("select * from topig");
		try{
			rs.last();
			size=rs.getRow();
			rs.absolute(1);
			attributes=new String[size];
			attributes[0]=rs.getString("name");
			int i=1;
			while(rs.next()){
				attributes[i]=rs.getString("name");
				i++;
			}
			System.out.println("生成参数数组成功！数组元素个数："+i);
			//System.out.println(attributes);
		}catch(Exception e){
			System.out.println("生成参数出错"+e);
		}	
	}
	
	public void createAttributes(String tableName){
		int size;
		ResultSet rs=db.select("select * from " + tableName);
		try{
			rs.last();
			size=rs.getRow();
			rs.absolute(1);
			attributes=new String[size];
			attributes[0]=rs.getString("name");
			int i=1;
			while(rs.next()){
				attributes[i]=rs.getString("name");
				i++;
			}
			System.out.println("生成参数数组成功！数组元素个数："+i);
			//System.out.println(attributes);
		}catch(Exception e){
			System.out.println("生成参数出错"+e);
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
	/**
	 * 获得的是正常文件的目录
	 * @param file
	 */
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
	
	/**
	 * 写weka arff文件的头：包括500个属性
	 * @param pw
	 */
	public void writeHeader(PrintWriter pw){
		//write header
		pw.println("@relation apifeatures");
		pw.println("");
		
		//write class
		int size=attributes.length;
		for(int i=0;i<size;i++){
			pw.println("@attribute "+attributes[i]+" "+"{0,1}");
		}
	 
	}
	
	public List<File> generateRandomFile(List<File> files, int size){
	      int length = files.size();
	      List<File> result = new LinkedList<File>();
	      Random rand = new Random();
	      for(int i=0;i<size;i++){
	    	 result.add(files.get(rand.nextInt(length)));
	      }
	      return result;
	}
	public List<File> removeALl(List<File> origin,List<File> tobeRemoved){
	     
		//复制一份新的
		List<File> tmp = new LinkedList<File>();
		for(File o : origin){
        	tmp.add(o);
        }
	  	tmp.removeAll(tobeRemoved);
		 return  tmp;
		
	}
	
	//生成Worm，Virus，Trojan对应的Arff文件
	public void doWork(){
		/*
		createArff("worm");
		createArff("virus");
		createArff("trojan");
		 
		createTestArff("worm");
		createTestArff("virus");
		createTestArff("trojan");
		*/
		createTotalArff();
		
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
 
  //getter and setter
	
	
}
