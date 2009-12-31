package weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaUtil {
 
	
	
	/*

     * 从.arff文件中获取样本Instances;

     * 1.fileName instances的文件名

     */

    public static Instances getInstances(String fileName) {

       File file= new File(fileName);

       return getInstances(file);

    }

 

/*

     * 从.arff文件中获取样本Instances;

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

     * 获得一个Evaluation对象

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
    
	
}
