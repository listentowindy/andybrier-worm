package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

/**
 * Behavior knowledge Space 算法的实现。将数据拆分为训练和测试，将训练数据用BKS算法（基于不同的分类方法）
 * 训练出BKS矩阵，然后利用矩阵，对测试数据就行测试，得出BKS的正确率。。
 * @author andy
 * 
 */
public class MyBKSMain {

	// 分类器集合
	private Classifier[] classfilers = { new J48(), new Id3(),
			new NaiveBayes(), new SMO() };
	
	//选用性能最好的SMO算法作为BKS的基本算法
	private Classifier classifier = classfilers[2]; 

	// 类别集合
	private String classes[] = { "virus", "worm", "trojan", "benign" }; // 0-virus
																		// 1-worm
																		// 2-trojan
																		// 3-benign

   ////要就行BKS的数据
	private String files[]  = {"file","memory","process","reg","socket","net","dll"}; 
 
	int k = 3333334;
	private int[][] BKS = new int[classes.length][k]; // BKS矩阵

	double TH = 0.50; // 阈值

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyBKSMain bks = new MyBKSMain();
		try {
			
			//bks.filter();  //将每个分类分割为训练和测试
		   	
		   bks.train();   //训练样本
		   //bks.writeArrayToFile(bks.BKS);//将BKS矩阵print
		 	double accury = bks.test();
			/// bks.printArray(bks.BKS);  //将BKS矩阵输出到文件中
		    //bks.writeArrayToFile(bks.BKS);//将BKS矩阵print
	 	System.out.println("BKS正确率为：" + accury );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 训练阶段，得到BKS矩阵
	 * 
	 * @throws Exception
	 */
	public void train() throws Exception {

		Instances train = null;
		

		Instance testInst = null; // 单个样本
		int lengthOfInstances =  120; // 总的训练样本个数:200*60%=120

		int p = 0;  //控制输出.
		
		// 对每一个训练样本
		for (int i = 0; i < lengthOfInstances; i++) {

			StringBuilder result = new StringBuilder();

			// 对每一个类别，得到一个分类结果
			for (String file : files) {
				
				train = WekaUtil.getInstances(new File("weka/classedApiWeka/"+file+".arff.train"));
				train.setClassIndex(train.numAttributes()-1);
				

				testInst = train.instance(i); // 单个样本
				
				classifier.buildClassifier(train); // 训练样本
				 
				
				result.append((int) classifier.classifyInstance(testInst));
			}

			int labelClass = (int) testInst.classValue(); // 原来的类别标签
			int unit = Integer.parseInt(result.toString()); // 分类器分类之后的结果
			// System.out.println(labelClass+"-"+unit);

			BKS[labelClass][unit]++;

			
			System.out.print(".");
			p++;
			if(p%80==0){
				System.out.println("\n");
			}

		}


	}

	/**
	 * 对测试样本就行试验
	 * 
	 * @return 分类正确率
	 * @throws Exception
	 */
	public double test() throws Exception {

		Instance testInst = null; // 单个样本
		Instances test = null;  
		Instances train = null;  
		
		int lengthOfInstances =  80; // 总的训练样本个数:200*40%=80


		int accuryNum = 0; // 正确分类的样本数
		int totalNum = 80; // 总的样本数


		// 对每一个样本
		for (int i = 0; i < lengthOfInstances; i++) {
			
			StringBuilder result = new StringBuilder();
			
			// 对每一个类别，得到一个分类结果
			for (String file : files) {
				
				test = WekaUtil.getInstances(new File("weka/classedApiWeka/"+file+".arff.test"));
				train = WekaUtil.getInstances(new File("weka/classedApiWeka/"+file+".arff.train"));
				
				test.setClassIndex(test.numAttributes()-1);
				train.setClassIndex(train.numAttributes()-1);

				testInst = test.instance(i); // 单个样本
				
				classifier.buildClassifier(train); // 训练样本
				 
				
				result.append((int) classifier.classifyInstance(testInst));
			}

			int labelClass = (int) testInst.classValue(); // 原来的类别标签
			int unit = Integer.parseInt(result.toString()); // 分类器分类之后的结果
			
			// System.out.println(labelClass+"-"+unit);

			int value = BKS[labelClass][unit];
			int total = BKS[0][unit] + BKS[1][unit] + BKS[2][unit]
					+ BKS[3][unit];

			
			double th = value / total;

			// 分类是有正确的
			if (th >= TH) {
				accuryNum++;
			}

		}
		 
		 
		return (double)accuryNum / (double)totalNum; // 分类正确率

	}

	/**
	 * 从文件读取instances
	 * 
	 * @param filePath
	 * @return
	 */
	public Instances getPreparedInstanceFromFile(String filePath) {

		Instances data = null;
		// 读取file到instance
		FileReader frData;
		try {
			frData = new FileReader(filePath);
			data = new Instances(frData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Class Index是指示用于分类的目标属性:一般是最后一个属性
		data.setClassIndex(data.numAttributes() - 1);
		return data;

	}
	
 
	
	/**
	 * 分割样本为测试和训练
	 * @throws Exception
	 */
	 public void filterData(String fileName, Instances data) throws Exception

	    {

	        Resample removePercentage =new Resample();
	        
           //60%作为训练
	        String[] options = Utils.splitOptions("-Z 60 -no-replacement");

	        removePercentage.setOptions(options);

	        removePercentage.setInputFormat( data );

	       Instances trainIns = Filter.useFilter( data, removePercentage);  

	         WekaUtil.writeToArffFile(fileName+".train", trainIns );

	        
            //40%作为测试
	        options = Utils.splitOptions("-Z 40 -no-replacement");

	        removePercentage.setOptions(options);

	        removePercentage.setInputFormat( data );

	        Instances   testIns = Filter.useFilter( data,removePercentage);

           WekaUtil.writeToArffFile(fileName+".test", testIns );

	    }
	
	  
	 public void filter(){
		 
			// 读取file到instance
			FileReader frData;
			Instances ins;
			try {
				for(String file : files){
				  frData = new FileReader("weka/classedApiWeka/"+file+".arff");
				  ins = new Instances(frData);
				  ins.setClassIndex(ins.numAttributes() - 1);
				  filterData("weka/classedApiWeka/"+file+".arff",ins);
				  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		 
		 
		 
	 }

}
