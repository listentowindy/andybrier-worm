package weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class WekaUtil {
 
	
	
	/*

     * 从.arff文件中获取样本Instances; 已经设置了classIndex

     * 1.fileName instances的文件名

     */

    public static Instances getInstances(String fileName) {

       File file= new File(fileName);

       return getInstances(file);

    }

 

/*

     * 从.arff文件中获取样本Instances; 已经设置了classIndex

     * 1.file 获得instances的File对象

     */

    public static Instances getInstances(File file) {

       Instances inst = null;

       try{

           ArffLoader loader = new ArffLoader();

           loader.setFile(file);

           inst = loader.getDataSet();
           
           inst.setClassIndex(inst.numAttributes()-1);
         
         

       }

       catch(Exception e){

           System.out.print("error");

       }

       return inst;

    }
 
	
	/*

     * 

     * 1.h 一个已经训练过的分类器

     * 2.ins 测试样本

     */

    public static Evaluation getEvaluation(Classifier h,Instances ins){

       try{
    	

             Instance testInst;
           
      
           /*

            * Evaluation: Class for evaluating machine learning models

            * 即它是用于检测分类模型的类

            */
          
           Evaluation testingEvaluation = new Evaluation(ins);

           
           int length = ins.numInstances(); 
           
           for (int i =0; i < length; i++) {
        	    
              testInst = ins.instance(i);

              //通过这个方法来用每个测试样本测试分类器的效果

              testingEvaluation.evaluateModelOnceAndRecordPrediction(

                   h, testInst);

            }

           return testingEvaluation;

       }

       catch(Exception e){

           System.out.println("haha bug!");

           System.out.println(e);

       }

       return null;      

    }

 
    /**
     * 用模型来训练样本
     * @param cf
     * @param ins
     */
    public static void buildClassifier(Classifier cf, Instances ins){
    	try {
			cf.buildClassifier(ins);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    /**
     * 将一个Instances写到weka arff文件中
     * @param newFilePath
     * @param ins
     */
    public static void writeToArffFile(String newFilePath,Instances ins) 
    {
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(newFilePath));
		    writer.write(ins.toString());
		    writer.flush();
		    writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
    
    
    
    
  /**
   * 随机分割样本为测试和训练
   * @param data 要分割的样本
   * @param trainPercentage 训练样本所占的比例.80表示80%
   * @return 分割的训练样本和测试样本数组。数据第一项表示训练样本（train）
   * @throws Exception
   */
	 public static Instances[] filterData(Instances data, int trainPercentage) 

	    {

		    Instances result[] = new Instances[2];
		    
	        Resample removePercentage =new Resample();
	        
	        removePercentage.setRandomSeed(2);
	        
	        try{
	        //trainPercentage%作为训练
	        String option = "-Z " +trainPercentage +" -no-replacement";
	        String[] options = Utils.splitOptions(option);

	        removePercentage.setOptions(options);

	        removePercentage.setInputFormat( data );

	        result[0] = Filter.useFilter( data, removePercentage);  

	        //WekaUtil.writeToArffFile(this.getFile()+".train", trainIns );

	        
            //20%作为测试
	        String optionForTest = "-Z " +(100-trainPercentage) +" -no-replacement";
	        options = Utils.splitOptions(optionForTest);

	        removePercentage.setOptions(options);
	        removePercentage.setInputFormat( data );

	        result[1] = Filter.useFilter( data,removePercentage);

           //ekaUtil.writeToArffFile(this.getFile()+".test", testIns );
	        
	        }
	        catch(Exception e){
	           e.printStackTrace();
	        	
	        }

	        return result;
 
	    }
    
	
}
