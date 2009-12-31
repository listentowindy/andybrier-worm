package weka;
import java.awt.BorderLayout;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileReader;

import java.util.Random;

 

import weka.classifiers.Classifier;

import weka.classifiers.Evaluation;

import weka.classifiers.bayes.NaiveBayes;

import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;

import weka.core.Instances;

import weka.core.Utils;

import weka.gui.visualize.PlotData2D;

import weka.gui.visualize.ThresholdVisualizePanel;

 

 

 

/*

 * Date: 2009.4.6

 * by: Wang Yi

 * Email: wangyi19840906@yahoo.com.cn

 * QQ: 270135367

 * 

 */

public class SimpleROC {

 

	
	 Instances trainins;  //训练样本
     Instances testins; //测试样本
     
     ThresholdVisualizePanel vmc;
    
     int classIndex = 0;  //要画那一个类别的roc曲线
    
	
    /**

     * @param args

     */
     
     public SimpleROC(){
    	 try {
			trainins = new Instances(
			         new BufferedReader(
			           new FileReader("weka/total-train.arff")));
			testins = new Instances(
			         new BufferedReader(
			           new FileReader("weka/total-test.arff")));
			
			 trainins.setClassIndex( trainins.numAttributes() - 1 );

		     testins.setClassIndex( testins.numAttributes() - 1 );
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//vmc的设置
		  vmc = new ThresholdVisualizePanel();
	      vmc.setROCString("ROC结果显示");
	      vmc.setName("ROC结果显示" );
	      
	      
     	 
     }
     

     
     public void OneMethod( Classifier c,Color color) throws Exception{
    	 
    		 //训练样本
    		 c.buildClassifier(trainins);
    		 
    		 //得到分类结果
    		 Evaluation eval = new Evaluation(testins);
    		 eval.evaluateModel(c, testins);
    		 
    		 //生成roc曲线
    		  ThresholdCurve tc = new ThresholdCurve();
    	    
    	      Instances result= tc.getCurve(eval.predictions(), classIndex);

    	        //贝叶斯       
    	        PlotData2D tempd = new PlotData2D(result);

    	        String className = c.getClass().getName();
    	        
    	        tempd.setPlotName( className.substring(className.lastIndexOf("."))+"  " + Utils.doubleToString(tc.getROCArea(result), 4));

    	        tempd.addInstanceNumberAttribute();
    	        
    	        tempd.setCustomColour(color);

    	        vmc.addPlot(tempd);
    	 
     }
     
     
     public void OneMethod( Classifier c ) throws Exception{
    	 OneMethod(c,Color.red);
     }
     
     
     public void show(){
    
    	 final javax.swing.JFrame jf = 

             new javax.swing.JFrame("Weka Classifier Visualize: ");

           jf.setSize(500,400);

           jf.getContentPane().setLayout(new BorderLayout());

           jf.getContentPane().add(vmc, BorderLayout.CENTER);
            
           
           jf.addWindowListener(new java.awt.event.WindowAdapter() {

             public void windowClosing(java.awt.event.WindowEvent e) {

             jf.dispose();

             }

           });

           jf.setVisible(true);
    	 
     }
     
     
     
    public int getClassIndex() {
		return classIndex;
	}



	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}



	public static void main(String[] args) throws Exception {


        Classifier c1 = new NaiveBayes();  //贝叶斯
        Classifier c2 = new J48();          //J48
        Classifier c3 = new Id3();
        Classifier c4 = new LibSVM();
     
            
         SimpleROC test = new SimpleROC();
         
         test.setClassIndex(classEnum.worm);
         
         test.OneMethod(c1, Color.BLUE);
         test.OneMethod(c2, Color.red);
         test.OneMethod(c3, Color.BLACK);
         test.OneMethod(c4, Color.CYAN);
         
         test.show();
     
       

    }


}


 class classEnum{
	
	public static int virus = 0;
	public static int worm = 1;
	public static int trojan = 2;
	public static int benign = 3;
	
	
}


