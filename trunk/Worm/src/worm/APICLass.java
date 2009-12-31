package worm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.Constant;
import util.FileUtil;

/**
 * API 分类 apiclass ： 从API Monitor安装目录拷出来的api分类文件，修改了文件名 classedApi：分类之后，每类api列表
 * 
 * @author Administrator
 * 
 */
public class APICLass {

	List<File> apiclassFiles; // 分类的标准，直接从api monitor的APIMonitor\apifilter目录下拷贝

	String folder = "apiclass"; // 存放标准API Monitor中api filter文件

	public APICLass() {
		// 获取比较的标准
		this.apiclassFiles = FileUtil.getFiles(folder);
	}

	public static void main(String args[]) throws Exception {
		APICLass a = new APICLass();

		// System.out.println( a.classic("NtOpenKey") );
		a.classicAPI();

		System.out.println("api分类完成！");

	}

	/**
	 * 给API分类：api文件：api.txt
	 */
	public void classicAPI() {

		File apiFile = new File(Constant.APIListFile);
		String classedFile = ""; // 存放某类api的文件
		PrintWriter writer = null;
		String className = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(apiFile));
			String api = "";
			while ((api = br.readLine()) != null) {
				className = classic(api);
				// 如果找到了一个类别,则把这个类别写到对应的文件中
				if (!className.equals("")) {
					classedFile = Constant.ClassedAPIFileDir + "/" + className;
					writer = new PrintWriter(new FileWriter(classedFile, true));
					writer.append(api);
					writer.append("\n");
					writer.close();
				} else {
					// 找不到类别
					classedFile = Constant.ClassedAPIFileDir + "/" + "other";
					writer = new PrintWriter(new FileWriter(classedFile, true));
					writer.append(api);
					writer.append("\n");
					writer.close();
				}

			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 找到一个api对应的类别
	 * 
	 * @param apiName
	 * @return
	 */
	public String classic(String apiName) {
		String stringFile;
		String fileName;
		String className = "";
		for (File f : apiclassFiles) {
			fileName = f.getName();
			stringFile = FileUtil.getStringFormFile(f);
			if (stringFile.indexOf(apiName) > 0) {
				className = fileName;
				break;
			}
		}
		return className;
	}

}
