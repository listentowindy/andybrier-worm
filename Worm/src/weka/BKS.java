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

/**
 * Behavior knowledge Space 算法的实现。将数据拆分为训练和测试，将训练数据用BKS算法（基于不同的分类算法）
 * 训练出BKS矩阵，然后利用矩阵，对测试数据就行测试，得出BKS的正确率。
 * 
 * @author andy
 * 
 */
public class BKS {

	// 分类器集合
	private Classifier[] classfilers = { new J48(), new Id3(),
			new NaiveBayes(), new SMO() };

	// 类别集合
	private String classes[] = { "virus", "worm", "trojan", "benign" }; // 0-virus
																		// 1-worm
																		// 2-trojan
																		// 3-benign

	private String fileTrain = "weka/total.arff.train"; // 要训练的样本集合
	private String fileTest = "weka/total.arff.test"; // 要训练的样本集合
	
	
	private Instances trainInstances;  //训练样本
	private Instances testInstances;   //测试样本
	 
	

	int n = (int) Math.pow(classes.length, classfilers.length); // BKS unit的个数
																// 1024

	int k = 3334;
	private int[][] BKS = new int[classes.length][k]; // BKS矩阵

	double TH = 0.70; // 阈值



	/**
	 * 训练阶段，得到BKS矩阵
	 * @param trainInstances 训练样本：要已经设置了classIndex的!
	 * @throws Exception
	 */
	public void train(Instances trainInstances) throws Exception {

		Instances train = trainInstances;

		Instance testInst; // 单个样本
		int lengthOfInstances = train.numInstances(); // 总的训练样本个数

		
		// 对每一个样本
		for (int i = 0; i < lengthOfInstances; i++) {

			testInst = train.instance(i); // 单个样本
			StringBuilder result = new StringBuilder();

			// 对每一个分类器，得到一个分类结果
			for (Classifier c : classfilers) {
				c.buildClassifier(train); // 训练样本
				result.append((int) c.classifyInstance(testInst));
			}

			int labelClass = (int) testInst.classValue(); // 原来的类别标签
			int unit = Integer.parseInt(result.toString()); // 分类器分类之后的结果
			// System.out.println(labelClass+"-"+unit);

			BKS[labelClass][unit]++;

			 

		}


	}
	
	/**
	 * 训练阶段，得到BKS矩阵
	 * 
	 * @throws Exception
	 */
	public void train(String trainFile) throws Exception {

		Instances train = getPreparedInstanceFromFile(trainFile);
 
        train(train);
	}
	

	/**
	 * 对测试样本就行试验
	 * 
	 * @return 分类正确率
	 * @param trainInstances  训练样本：要已经设置了classIndex的!
	 * @param testInstances   测试样本：要已经设置了classIndex的!
	 * @throws Exception
	 */
	public double test(Instances trainInstances,Instances testInstances) throws Exception {

		Instances test = testInstances;
		Instances train = trainInstances;

		int accuryNum = 0; // 正确分类的样本数
		int totalNum = test.numInstances(); // 总的样本数

		Instance Inst; // 单个样本
		int lengthOfInstances = test.numInstances(); // 总的训练样本个数

		// 对每一个样本
		for (int i = 0; i < lengthOfInstances; i++) {

			Inst = test.instance(i); // 单个样本
			StringBuilder result = new StringBuilder();

			// 对每一个分类器，得到一个分类结果
			for (Classifier c : classfilers) {
				c.buildClassifier(train); // 训练样本
				result.append((int) c.classifyInstance(Inst));
			}

			int labelClass = (int) Inst.classValue(); // 原来的类别标签
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
	 * 对测试样本就行试验
	 * 
	 * @return 分类正确率
	 * @throws Exception
	 */
	public double test(String trainFile,String  testFile) throws Exception {

	 Instances train = getPreparedInstanceFromFile(trainFile);
	 Instances test = getPreparedInstanceFromFile(testFile);
	 
	 return test(train, test);

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
	 * 输出二维矩阵
	 * @param t
	 */
	public void printArray(int[][] t){

		for (int i = 0; i < t[0].length; i++) {
		   System.out.print(" ");	
		   System.out.print(i);	
		}
		System.out.print("\n");
		
		for (int i = 0; i < t.length; i++) {
			
			System.out.print(i);
			System.out.print(" ");
			for (int j = 0; j < t[0].length; j++) {
				System.out.print((t[i][j]));
				System.out.print(" ");
			}
			System.out.print("\n");

		}
		
	}

	/**
	 * 矩阵输出到文件
	 * @param t
	 */
	public void writeArrayToFile(int[][] t) {
		File array = new File("weka/array.txt");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(array);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t[0].length; j++) {
				pw.write(""+t[i][j]);
				pw.write(" ");
			}
			pw.write("\n");

		}

		pw.flush();
		pw.close();

	}
	
	/**
	 * 从文件中读出矩阵
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public int[][] loadArrayFromFile(String file) throws Exception{
		BufferedReader fr = null;
		try {
			fr = new BufferedReader(new FileReader("weka/array.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
   
	    int bks[][] = new int[4][3334];
		for (int i = 0; i < bks.length; i++) {
			String line = fr.readLine();
			for (int j = 0; j < bks[0].length; j++) {
			  bks[i][j]=Integer.parseInt(line.split(" ")[j]);
			}
		}
       
		fr.close();
		 return bks;
		
	}
	
	

}
