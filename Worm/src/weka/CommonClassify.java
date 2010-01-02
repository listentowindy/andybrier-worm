package weka;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class CommonClassify {

	
	private   Classifier[] classfilers = { new J48(), new Id3(), new NaiveBayes(),
		new LibSVM(), new SMO() };
	
	private Classifier classfiler = classfilers[0];
	


	   //要分类的数据
		private String files[]  = {"file","memory","process","reg","socket","net","dll"}; 
	 
	
		public void work() throws Exception{
			
			for(String file : files){
				String trainFile = "weka/classedApiWeka/"+file+".arff.train";
				String testFile = "weka/classedApiWeka/"+file+".arff.test";
				run(trainFile, testFile,file);
				
			}
			
		}
		
		
	
	public void run(String trainFile,String testFile,String clazz) throws Exception{
		

		Instance testInst = null;
		
		Instances train = WekaUtil.getInstances(trainFile);
		Instances test = WekaUtil.getInstances(testFile);
		
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
	    Evaluation testingEvaluation = new Evaluation(test);

		 int length = test.numInstances(); 
		 
		    //训练分类器
         classfiler.buildClassifier(train);

          for (int i =0; i < length; i++) {

             testInst = test.instance(i);

             //通过这个方法来用每个测试样本测试分类器的效果

             testingEvaluation.evaluateModelOnceAndRecordPrediction(
                    classfiler, testInst);
          }

          System.out.println( clazz+" 分类器的正确率：" + (1- testingEvaluation.errorRate()));
		
	}
	
	
	
	public static void main(String args[]){
		CommonClassify bean = new CommonClassify();
		try {
			bean.work();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
