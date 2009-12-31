package weka;

 

import weka.classifiers.Classifier;

import weka.classifiers.Evaluation;

import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;

import weka.core.Instance;

import weka.core.Instances;

import weka.core.SelectedTag;


 

public class SimpleEnsemble {

 

    /**

     * @param args

     */

    public static void main(String[] args) {

       
       	String sTrainFile = "weka/total-train.arff";
       	String sTestFile = "weka/total-test.arff";
    	
       Instances trainIns = null;

       Instances testIns = null;

       Classifier cfs1 = null;

       Classifier cfs2 = null;

       Classifier cfs3 = null;
       
  

       Classifier[] cfsArray = new Classifier[3]; 

       

       try{

           /*
            * 1.读入训练、测试样本
            */
           trainIns = WekaUtil.getInstances(sTrainFile);
           testIns = WekaUtil.getInstances(sTestFile);
 
           //在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常
           trainIns.setClassIndex(trainIns.numAttributes()-1);
           testIns.setClassIndex(testIns.numAttributes()-1);

            
           /*

            * 2.初始化基分类器

            * 具体使用哪一种特定的分类器可以选择，请将特定分类器的class名称放入forName函数

            * 这样就构建了一个简单的分类器

            */

           //贝叶斯算法
           cfs1 =  new SMO(); //buhao
           //决策树算法，是我们常听说的C45的weka版本，不过在我看代码的过程中发现有一些与原始算法有点区别的地方。
           cfs2 =  new J48();
           // bagging
           cfs3 =  new Id3();
            
           
                

           /*

            * 3.构建ensemble分类器

            */

           cfsArray[0] = cfs1;

           cfsArray[1] = cfs2;

           cfsArray[2] = cfs3;

        

           Vote ensemble = new Vote();

           /*

            * 订制ensemble分类器的决策方式主要有：

            * AVERAGE_RULE

            * PRODUCT_RULE

            * MAJORITY_VOTING_RULE

            * MIN_RULE

            * MAX_RULE

            * MEDIAN_RULE

            * 它们具体的工作方式，大家可以参考weka的说明文档。

            * 在这里我们选择的是多数投票的决策规则

            */

           SelectedTag tag1 = new SelectedTag(

                  Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);

 

           ensemble.setCombinationRule(tag1);

           ensemble.setClassifiers(cfsArray);

           //设置随机数种子

           ensemble.setSeed(2);

           //训练ensemble分类器

           ensemble.buildClassifier(trainIns);

           

           /*
            * 4.使用测试样本测试分类器的学习效果
            */

           Instance testInst;

           /*

            * Evaluation: Class for evaluating machine learning models

            * 即它是用于检测分类模型的类

            */

           Evaluation testingEvaluation = new Evaluation(testIns);

           int length = testIns.numInstances(); 

           for (int i =0; i < length; i++) {

              testInst = testIns.instance(i);

              //通过这个方法来用每个测试样本测试分类器的效果

              testingEvaluation.evaluateModelOnceAndRecordPrediction(

                     ensemble, testInst);

           }

           

           /*

            * 5.打印分类结果

            * 在这里我们打印了分类器的正确率

            * 其它的一些信息我们可以通过Evaluation对象的其它方法得到

            */

           System.out.println( "分类器的正确率：" + (1- testingEvaluation.errorRate()));

       }catch(Exception e){

           e.printStackTrace();

       }

    }

 

}