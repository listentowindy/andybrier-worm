package worm;

 
import util.Constant;
import util.FileUtil;

/**
 * Category: nomal, Number: 98 Category: trojan, Number: 117 Category: virus,
 * Number: 165 Category: worm, Number: 134 一个237个API,一共有514个文件
 * 
 */

public class WormMain {

	int k = 237; // API总数
	String[] apis; // API数组

	public static void main(String args[]) {

		WormMain wm = new WormMain();

		//特征文件
	    //wm.getAPIList(); //获得API集合，并且写入到文件之中，只需要运行一次
		// wm.formatAPI(Constant.FeatherOrigine,Constant.FeatherFormat); //API进行格式化，只需要执行一次。时间 1分钟
	    // wm.getMatrix(Constant.FeatherFormat,Constant.FeatherMatrix);   //得到频率矩阵,只需要执行一次。时间时间为51分钟
		//wm.setUpSequence();        //生成sequence表
     	//	wm.setIGForSequence();       //生成sequence表的ig字段
		//get the most big 500 ig from sequence to topig
		
		
		
		//验证文件
	   //  wm.formatAPI(Constant.VerifyOrigine, Constant.VerifyFormat);
	   //  wm.VerifyMatrix(Constant.VerifyFormat,Constant.VerifyMatrix);
		 
		 
		// 依据file-api对Verify目录下的原始文件生成格式化
         wm.formatAPI("verify", "verifyFormat-file","classedApi/File");	
		
		// wm.writeVerifyWeka();  //写weka属性文件
	}

	/**
	 * 获得出现的API集合
	 */
	public void getAPIList() {

		// 获得所有的出现的API序列:特征文件目录
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		System.out.println("正在获取API，请稍后");
		
		API apiBean = new API(Constant.FeatherOrigine);
		apis = apiBean.getAPI();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("API获取成功，时间为%s分钟,现在将API写到文件中",minute);
		FileUtil.writeStringArrayToFile(apis,  Constant.APIListFile);

	}

	/**
	 * 对API文件进行格式化 ：特征目录 和验证目录
	 */
	public void formatAPI(String srcOrgine,String toFormat) {
		System.out.println("正在对文件进行格式化，请稍后");
		long StartTime = System.currentTimeMillis(); // 取出目前時間
	
		Format format = new Format(srcOrgine,toFormat);
		format.format();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("文件格式化成功！时间为%d分钟",minute);
	}
	/**
	 * 对API文件进行格式化 ：特征目录 和验证目录
	 */
	public void formatAPI(String srcOrgine,String toFormat,String apiListFile) {
		System.out.println("正在对文件进行格式化，请稍后");
		long StartTime = System.currentTimeMillis(); // 取出目前時間
	
		Format format = new Format(srcOrgine,toFormat,apiListFile);
		format.format();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("文件格式化成功！时间为%d分钟",minute);
	}
	/**
	 * 得到频率矩阵  : 特征目录   和 验证目录
	 */
	public void getMatrix(String srcFormat, String toMatrix) {
		System.out.println("正在生成矩阵，请稍后");
		
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		
		FourMatrix m = new FourMatrix(srcFormat,toMatrix);
		m.doMatrix();

		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("生成矩阵成功！时间为%s分钟",minute);
	}

	/**
	 *从特征文件的matrix矩阵，生成表sequence 得到各种4-gram出现的次数：
	 */
	public void setUpSequence(){
		System.out.println("正在生成表sequence，请稍后");
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		 
		Sequence se = new Sequence();
		se.createSequences();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf(" 生成表sequence成功！时间为%s分钟",minute);
	}
	
	
	/**
	 * 从 sequence表，计算ig，并且更新到表sequence之中
	 */
	public void setIGForSequence(){
		System.out.println("正在生成sequence表中的ig字段，请稍后");
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		 
		ShangV2 s=new ShangV2();
		s.createShang();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("生成sequence表中的ig字段成功！时间为%s分钟",minute);
	}
	
	/**
	 * 生成验证文件的weka属性文件：验证文件
	 */
	public void writeVerifyWeka(){
		System.out.println("正在生成生成验证文件的weka属性文件，请稍后");
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		 
		Arff arff=new Arff();
		arff.doWork();
		
		long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間
		long minute = ProcessTime/60000;
		System.out.printf("生成生成验证文件的weka属性文件成功！时间为%s分钟",minute);
	}
	
	
 
	 
	
}
