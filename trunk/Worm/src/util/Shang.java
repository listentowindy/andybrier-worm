package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @author 作者：袁启侠
 * @version 创建时间：2009-10-6 下午08:40:00
 */
public class Shang {
	private List<File> virus = new LinkedList<File>(); // 要处理的病毒文件
	private List<File> worm = new LinkedList<File>(); // 要处理的蠕虫文件
	private List<File> trojan = new LinkedList<File>(); // 要处理的木马文件
	private List<File> normal = new LinkedList<File>(); // 要处理的正常文件
	private String dir = "juzhen";// 目标文件夹
	ArrayList<FiveTuple> fi=new ArrayList<FiveTuple>();

	public Shang() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 要求对正常文件目录和病毒，木马，蠕虫目录进行初始化
	 * @param mulu
	 */
	public Shang(File mulu){
		getNormalFiles(mulu);
		getFiles(mulu,worm,"XWorm");
		getFiles(mulu,trojan,"XTrojan");
		getFiles(mulu,virus,"XVirus");
	}
	public void getShang(){
		System.out.println("开始处理病毒文件……");
		getShang(virus);
		System.out.println("开始处理蠕虫文件……");
		getShang(worm);
		System.out.println("开始处理木马文件……");
		getShang(trojan);
	}
	public void getShang(List<File> file){
		int size=0;
		FiveTuple five1,five2;
		int m1,n1,p1,j1,m2,n2,p2,j2;
		double s1,s2;
		for (File f : file) {
			compare(f);
			size=fi.size();
			for(int i=0;i<size-1;i++){
				five1=fi.get(i);
				five2=fi.get(i+1);
				m1=five1.getM();
				n1=five1.getN();
				p1=five1.getP();
				j1=five1.getJ();
				s1=five1.getS();
				m2=five2.getM();
				n2=five2.getN();
				p2=five2.getP();
				j2=five2.getJ();
				s2=five2.getS();
				if(m1==m2&&n1==n2&&p1==p2&&j1==j2){
					if(s1>s2){
						fi.remove(i+1);
					}else{
						fi.remove(i);
					}
					size--;
					i--;
				}				
			}
			String name="shang/"+f.getName().substring(0,f.getName().length()-11)+".sha";
			
			try {
				  File myFilePath = new File(name);
				  if (!myFilePath.exists()) {  
						myFilePath.createNewFile();  
						}
				  FileWriter resultFile = new FileWriter(myFilePath);  
					PrintWriter myFile = new PrintWriter(resultFile); 
					size=fi.size();
					for(int i=0;i<size;i++){
						myFile.println(fi.get(i).getM()+" "+fi.get(i).getN()+" "+fi.get(i).getP()+" "+fi.get(i).getJ()+" "+fi.get(i).getS()+" ");
					}
				  resultFile.close(); 
				 // System.out.println(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 对于文件file,读取其中的每一行，将其中的api序列次数与正常文件中出现的
	 * 对应api序列次数进行信息熵计算，将得到的结果生成FiveTurple对象保存到fi中
	 * @param file
	 */
	public void compare(File file) {
		fi.clear();
		String line = "";
		int m = 0, n = 0, p = 0, j = 0;
		double a = 0, b = 0;
		double s = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			int index = 0;
			String tmp="";
			int i=0;
			while ((line = bf.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				if (st.hasMoreTokens()) {
					m = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					n = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					p = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					j = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					a = Double.parseDouble(st.nextToken());
				} else
					break;
				
				for (File f : normal) {
					if ((index = searchSe(line.substring(0, line.lastIndexOf("  ")), f)) > 0) {
						//获得文件中的指定行
						tmp=readAppointedLineNumber(f,index);
						tmp=tmp.substring(tmp.lastIndexOf(" "));
						b=Double.parseDouble(tmp.trim());
						s=(-log2(b/(a+b)))*(a/(a+b))-log2(b/(a+b))*(b/(a+b));
						FiveTuple five = new FiveTuple(m, n, p, j, s);
						fi.add(five);
					}
				}
				//显示进度，换行
				i++;
				System.out.print(".");
				if(i%80 == 0){
					System.out.print("\n");
				}
			}
			bf.close();
			fr.close();
			// System.out.println(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 在文件file中查询字符串str，如果找到就返回所在的行号，如果没有返回-1
	 * @param str
	 * @param file
	 * @return
	 */
	public int searchSe(String str, File file) {
		//str = str.substring(0, str.lastIndexOf("  "));
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			int index = 0;
			while ((line = bf.readLine()) != null) {
				if (line.indexOf(str) == 0) {
					return index;
				}
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 读取文件中指定行的数据
	 * @param sourceFile
	 * @param lineNumber
	 * @return
	 * @throws IOException
	 */
	private String readAppointedLineNumber(File sourceFile, int lineNumber)
			throws IOException {
		FileReader in = new FileReader(sourceFile);
		LineNumberReader reader = new LineNumberReader(in);
		String s=reader.readLine();		
		for(int i=0;i<lineNumber;i++){
			s=reader.readLine();
		}
		reader.close();
		in.close();
		return s;
	}
	private double log2(double value) {
		return (Math.log(value)/Math.log(2));
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("juzhen");
        Shang s=new Shang(file);
        s.getShang();
	}

}

class FiveTuple {
	int m, n, p, j;
	double s;

	public FiveTuple(int m, int n, int p, int j, double s) {
		this.m = m;
		this.n = n;
		this.p = p;
		this.j = j;
		this.s = s;
	}

	public int getM() {
		return this.m;
	}

	public int getN() {
		return this.n;
	}

	public int getP() {
		return this.p;
	}

	public int getJ() {
		return this.j;
	}

	public double getS() {
		return this.s;
	}
}