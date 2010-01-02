package weka;

import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.*;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class Classic {

	
	static Instances data = null; // contains the full dataset  
	private String file; // 要测试的样本的位置
	
	
	private Instances trainIns;  //训练
	private Instances testIns;   //测试

	// 分类器集合
	private static Classifier[] classfilers = { new J48(), new Id3(), new NaiveBayes(),
			new LibSVM(), new SMO() };

	public static void main(String[] options) {

	
		Classic bean = new Classic();

		// set file
		bean.setFile("weka/total.arff");
		
		// 读取file到instance
		FileReader frData;
		try {
			frData = new FileReader(bean.getFile());
			data = new Instances(frData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// Class Index是指示用于分类的目标属性:一般是最后一个属性
		data.setClassIndex(data.numAttributes() - 1);

		
		 
		for(Classifier c : classfilers){
		   bean.runCrossValidate(c); //10则交叉验证
		  // bean.runDIY(c);             //自己分割训练和测试
		} 
		 

	}

	 
	
	/**
	 * 10则交叉验证
	 * @param c
	 */
	public void runCrossValidate(Classifier c) {
		try {
		
			//交叉验证
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(c, data, 10, new Random(1));
			
			//输出结果
			System.out.println(" **************"+c.getClass()+"********** ");
			//System.out.println("time cost: " + eval.totalCost());
			System.out.println("Accuracy: " + (1-eval.errorRate()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * 自己分割训练和测试
	 * @param c
	 */
	public void runDIY(Classifier c){
		try {
			    filterData(); //分割训练和测试
			    c.buildClassifier(trainIns);

		        Evaluation eval = new Evaluation( trainIns );
		        eval.evaluateModel(c,testIns);
		    	//输出结果
				System.out.println(" **************"+c.getClass()+"********** ");
				System.out.println("Accuracy: " + (1-eval.errorRate()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * 分割样本为测试和训练
	 * @throws Exception
	 */
	 public void filterData() throws Exception

	    {

	        Resample removePercentage =new Resample();
	        
           //80%作为训练
	        String[] options = Utils.splitOptions("-Z 80 -no-replacement");

	        removePercentage.setOptions(options);

	        removePercentage.setInputFormat( data );

	        trainIns = Filter.useFilter( data, removePercentage);  

	        //WekaUtil.writeToArffFile(this.getFile()+".train", trainIns );

	        
            //20%作为测试
	        options = Utils.splitOptions("-Z 20 -no-replacement");

	        removePercentage.setOptions(options);

	        removePercentage.setInputFormat( data );

	        testIns = Filter.useFilter( data,removePercentage);

           //ekaUtil.writeToArffFile(this.getFile()+".test", testIns );

	        

	        trainIns.setClassIndex( trainIns.numAttributes() - 1 );

	        testIns.setClassIndex( testIns.numAttributes() - 1 );

	    }
	
	
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
