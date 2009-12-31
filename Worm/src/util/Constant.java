package util;

public class Constant {

	
	public static final String FeatherOrigine = "api"; //原始的API文件目录:用于生成特征
	public static final String FeatherFormat = "format"; //格式化之后的API文件目录
	public static final String FeatherMatrix = "matrix" ;  //存放生成特征矩阵的文件夹
	
	public static final String  APIListFile = "api.txt";   //API列表文件
	public static final String  ClassedAPIFileDir = "classedApi";   //分类之后的api
	
	public static final String  VerifyOrigine = "verify" ; //原始的严重文件
	public static final String  VerifyFormat = "verifyFormat" ;  //格式化之后的严重文件
	public static final String  VerifyMatrix = "VerifyMatrix" ;  //存放验证结果的matrix
	/**
	 * 格式API文件的格式：S100S222 后面的数字是API在api.txt中的位置
	 */
	public static final String flagOfFormat="S" ;    //格式化API文件的时候 开始符号
}
