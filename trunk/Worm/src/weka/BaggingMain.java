package weka;

import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * 利用bagging算法优化分类效果
 * @author andy
 *
 */
public class BaggingMain {

	
	private String fileInstance =  "weka/total.arff";
    private Instances data = null; // contains the full dataset  
    
    // 分类器集合
	private static Classifier[] classfilers = { new J48(), new Id3(), new NaiveBayes(),
			new LibSVM(), new SMO() };
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
		
		
	
		Bagging bagging = new Bagging();        //默认基分类器是：REPTree
        bagging.setClassifier(classfilers[0]);  //设置基分类器
		
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
