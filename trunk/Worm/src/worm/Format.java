package worm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.Constant;
import util.FileUtil;

/**
 * 对API文件进行格式化 每个API生成一个一行的s127s23 .buf文件，表示API的调用序列
 * 
 * @author Administrator
 * 
 */
public class Format {
	String dir; // 要格式化的api文件所在的目录
	String dstDir; // 格式化后存放的文件夹目录
	
	List<File> files; // 要格式化的api文件集合

	String apiListFile = Constant.APIListFile; // api list文件
	String flag = Constant.flagOfFormat; //这里是“S”
	
	String[] apiList = new String[237];

 
	/**
	 * 构造函数
	 * @param srcDir 要格式化的原始api文件所在目录
	 * @param toDir  格式化之后的文件存放目录
	 */
	public Format(String srcDir,String toDir) {
		this.dir = srcDir;
		this.dstDir = toDir;
		files = FileUtil.getFiles(new File(dir));
		apiList = FileUtil.getArrayListFromFile(this.apiListFile);
	}
	/**
	 * 构造函数
	 * @param srcDir 要格式化的源文件夹
	 * @param toDir  存放格式化之后的文件夹
	 * @param apiListFile 依照那个api list文件格式化
	 */
	public Format(String srcDir,String toDir,String apiListFile) {
		this.dir = srcDir;
		this.dstDir = toDir;
		this.apiListFile = apiListFile;
		
		//所有的源文件
		files = FileUtil.getFiles(new File(dir));  
		//从api list文件中获得所有的api
		apiList = FileUtil.getArrayListFromFile(this.apiListFile);
	}
	
	

	/**
	 * 格式化文件，生成.f文件
	 */
	public void format() {
		String line = "";
		int j = 0;
		for (File f : files) {

			// 显示进度，换行
			j++;
			System.out.print(".");
			if (j % 80 == 0) {
				System.out.print("\n");
			}

			FileUtil.makeDir(dstDir);  //生成存放生成的格式化文件夹目录
			// 生成一个同名的,但是后缀为.buf的文件
			String fileName = f.getName().substring(0,
					f.getName().lastIndexOf("."));
			File newFile = new File(dstDir + "//" + fileName + ".buf");

			BufferedReader bf = null;
			PrintWriter pw = null;
			String apiName; // 当前要处理的API名字
			int i = 0; // api在list中的位置
			try {
				bf = new BufferedReader(new FileReader(f));
				pw = new PrintWriter(newFile);

				while ((line = bf.readLine()) != null && (!line.equals(""))) {
					String[] st = line.split("[*]");
					if (st.length < 3) {
						System.out.println("发生了错误：" + line);
					} else {
						apiName = st[2];
						i = findPosition(apiName);
						if(i!=-1){  //在api list中可以找到
						 pw.write(flag + i);
						}else{  //在api list中找不到
						  pw.write("#");
						}
					}
				}

				bf.close();
				pw.close();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	}

	/**
	 * 从API List文件（api.txt）中找到该api的位置（从0开始）
	 * @param apiName
	 * @return
	 * @throws IOException
	 */
	public int findPosition(String apiName) throws IOException {

		for (int i = 0; i < apiList.length; i++) {
			if (apiName.equals(apiList[i])) {
				return i;
			}
		}
		return -1;
	}

}
