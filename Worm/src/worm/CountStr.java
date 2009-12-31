package worm;

/** 
 * @author 作者：袁启侠 
 * @version 创建时间：2009-10-5 下午08:37:11 
 *   
 * 取文件中与给定的字符串相同的个数 
 */

import java.io.File;
import util.FileUtil;
import util.StringUtil;
/**
 * 利用String的split方法，找到String中包含字串的个数
 * @author Administrator
 *
 */
public class CountStr {
	private String file = ""; // 文件
	private String str = ""; // 文件转化为String

	public CountStr(String f) {
		file = f;
		str = FileUtil.getStringFormFile(new File(file));
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int countPresent(String con) {
		//int count = numberOfStr(str, con);
		int count = StringUtil.countToken(str, con);
		return count;
	}
	
	/*
	public int numberOfStr(String str, String con) {
		str = " " + str;
		if (str.endsWith(con)) {
			return str.split(con).length;
		} else {
			return str.split(con).length - 1;
		}
	}
*/ 
	
	
	public String getStr() {
		return str;
	}

}
