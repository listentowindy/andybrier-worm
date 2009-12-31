package worm;

import java.io.File;
import java.io.PrintWriter;
 
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.FileUtil;

/** 
 * 4—gram算法
 * @author 作者：袁启侠 
 * @version 创建时间：2009-10-6 下午01:42:27 
 */
public class FourMatrix {
	
	private int m,n,p,j=237;
	private	Set<String> apis = new HashSet<String>();//存放从文件中得到的4个串的api序列
	List<FourTuple> fo=new LinkedList<FourTuple>();
	 

	private String dir ;  //要处理的文件目录
	private String toDir;     //存放处理结果的目录
	
	private   List<File>  files; //要处理的所有的文件
 
  
	/**
	 * 构造函数
	 * @param srcFromFormateDir 格式化之后的format文件目录
	 * @param toMatrixDir       存放matrix文件目录
	 */
	public FourMatrix(String srcFromFormateDir,String toMatrixDir){
		this.toDir = toMatrixDir ;
		this.dir =  srcFromFormateDir; 
		
	}
	 
	
	
	
	/**
	 * 得到所有要处理的文件的矩阵，并且存入对应的文件中
	 */
	public void doMatrix(){
	
		File file = new File(dir);        
		files = FileUtil.getFiles(file);   //遍历文件夹，获得所有的要处理的文件
		
		int i  = 0 ;
		for(File f : files){
			createMatrix(f.getPath());  //产生矩阵
			//写到文件中
			try {
				   String fileName = f.getName().substring(0,f.getName().lastIndexOf("."));
				   File myFilePath = new File(toDir+"/"+fileName+".m");
				   PrintWriter pw = new PrintWriter(myFilePath);
					
					String tmp="";
					int l=fo.size();
				 
					FourTuple ft;
					for(int step=0;step<l;step++){
						ft=fo.get(step);
						tmp=ft.getM()+"."+ft.getN()+"."+ft.getP()+"."+ft.getJ()+"  "+ft.getF();
						pw.println(tmp);
					}
				  pw.close(); 
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
			//显示进度，换行
			i++;
			System.out.print(".");
			if(i%80 == 0){
				System.out.print("\n");
			}
		 
		}
		System.out.print("\n");
		System.out.printf("一共处理了 %d 个文件",files.size());
		System.out.print("\n");
	}
	
	
	/**
	 * 对于给定的文件（要求是.f格式文件）生成对应的四元组出现频率矩阵
	 * @param file
	 */
	private void createMatrix(String file){
		fo.clear();
		String line="";
		//long c=0;
		CountStr cs=new CountStr(file);
		line=cs.getStr(); //得到本文件，String类型
	
		
		setApis(line); //得到格式化之后的buf文件（只有一行）的所有4-gram序列
		String apilist[]=apis.toArray(new String[apis.size()]);
		int l=apilist.length; //一共有多少个4-gram序列
	 
		String str;
		int index;
		for(int step=0;step<l;step++){
			
			str=apilist[step];
			
			//计算坐标
			index=str.indexOf("S",1);
			m=Integer.parseInt(str.substring(1,index));
			str=str.substring(index);
			index=str.indexOf("S",1);
			n=Integer.parseInt(str.substring(1,index));
			str=str.substring(index);
			index=str.indexOf("S",1);
			p=Integer.parseInt(str.substring(1,index));
			str=str.substring(index);
			j=Integer.parseInt(str.substring(1));
			
			//计算频率 并且添加结果
			int count = cs.countPresent(apilist[step]); //出现的次数
			double f = ((double)(count))/((double)(l));
			FourTuple fourTuple = new FourTuple(m,n,p,j,f);
			fo.add(fourTuple);
		}
		
	}
	/**
	 * 将传进来的字符串中进行处理，找出其中的四位序列，然后存入apis中. 这里的一个字符串对应一个文件
	 * @param str
	 */
	public void setApis(String str){
		
		apis.clear(); //清除上一个
		
		String tmp="";
		int strl=str.length();
		if(strl<8){
			System.out.println("所提供的数据有误！");
		}
		int start,end;
		out:while(true){
			start=0;
			end=1;
			for(int j=0;j<4;j++){
				end=str.indexOf("S",end);
				if(end<0){
					if(j<3)
						break out;
					else
						end=str.length();
					}
				tmp+=str.substring(start, end);
				start=end;
				end++;
			}
			end=str.indexOf("S",end);
			str=str.substring(str.indexOf("S",1));
			apis.add(tmp);
			//System.out.println(tmp);
			tmp="";
		}		
	}
	public void printFourTurple(){
		int l=fo.size();
		FourTuple f;
		for(int step=0;step<l;step++){
			f=fo.get(step);
			System.out.print(f.getM()+".");
			System.out.print(f.getN()+".");
			System.out.print(f.getP()+".");
			System.out.println(f.getJ());
		}
		
	}
	
	
	
 


}	
	 

 
/**
 * 对应一个矩阵
 * @author Administrator
 *
 */
class FourTuple{
		int m,n,p,j;//坐标
		double f; //频率 frequency
		public FourTuple(){
			
		}
		public FourTuple(int m,int n,int p,int j,double f){
			this.m=m;
			this.n=n;
			this.p=p;
			this.j=j;
			this.f=f;
		}
		public int getM(){
			return this.m;
		}
		public int getN(){
			return this.n;
		}
		public int getP(){
			return this.p;
		}
		public int getJ(){
			return this.j;
		}
		public double getF(){
			return this.f;
		}
	}