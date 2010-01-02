package weka;

import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

/**
 * 利用bagging算法优化分类效果
 * @author andy
 *
 */
public class BaggingMain {

	
	private String fileInstance =  "weka/total.arff";
    private Instances data = null; // contains the full dataset  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
          new BaggingMain().run();
	}
	
	
	public void run(){
		
		
		// 读取file到instance
		FileReader frData;
		try {
			frData = new FileReader(fileInstance);
			data = new Instances(frData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// Class Index是指示用于分类的目标属性:一般是最后一个属性
		data.setClassIndex(data.numAttributes() - 1);
		
		
		//默认基分类器是：REPTree
		Classifier bagging = new Bagging();
 
		
		Evaluation eval = null;
		try {
			 eval = new Evaluation(data);
			 eval.crossValidateModel(bagging, data, 10, new Random(1));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//输出结果
		System.out.println(" **************Bagging********** ");
		System.out.println("Accuracy: " + (1-eval.errorRate()));

		
	}
	

}
