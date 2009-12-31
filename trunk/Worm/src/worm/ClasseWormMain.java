package worm;

public class ClasseWormMain {

	//各个分类标准存储topig的表名
	private static String tableNames[] = { "dll_topig", "net_topig", "file_topig",
		"process_topig", "memory_topig", "reg_topig", "socket_topig" };
	
	
	
	/**
	 * 	初始化各个分类标准的topig表
	 */
	public void initTable(){
		for (String tableName : tableNames) {
			TopIGClass bean = new TopIGClass(TopIGClass.SOCKET, tableName);
			bean.filterIG();
		}
	}
	
	
	/**
	 *  产生各个分类标准的arff文件
	 */
	public void generateArff(){
     
		for (String tableName : tableNames) {
		    Arff arff = new Arff(tableName);
		    //设置文件名
		    arff.setFilename(tableName.split("_")[0]+".arff");
		    arff.doWork();
		}
	}
	
	
	
	public static void main(String args[]) {
        
		 new ClasseWormMain().generateArff();
		 System.out.println("Success!");

	}

}
