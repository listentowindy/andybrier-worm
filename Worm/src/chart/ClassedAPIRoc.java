package chart;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

 

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
 
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
 

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;


/**
 * 判断每一个分类算法效果的roc曲线
 * @author andy
 *
 */
public class ClassedAPIRoc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Instances data;
	int classIndex = classEnum.virus;

	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public static void main(String[] args) throws Exception {

		Classifier c1 = new NaiveBayes(); // 贝叶斯
		Classifier c2 = new J48(); // J48
		Classifier c3 = new Id3();
		Classifier c4 = new SMO();

		ClassedAPIRoc myRoc = new ClassedAPIRoc();
		 
		XYSeriesCollection xyDataset = new XYSeriesCollection(); 
		xyDataset.addSeries(myRoc.getSeries(c1, "NaiveBayes"));
		xyDataset.addSeries(myRoc.getSeries(c2, "J48"));
		xyDataset.addSeries(myRoc.getSeries(c3, "Id3"));
		xyDataset.addSeries(myRoc.getSeries(c4, "SMO"));
		
		//作图
	    JFreeChart freeChart = createChart(xyDataset);      
        //步骤3：将JFreeChart对象输出到文件，Servlet输出流等      
        saveAsFile(freeChart, "chart/roc.png", 500, 500);     
        
        System.out.println("输出成功！");
        
	}

	// 根据XYDataset创建JFreeChart对象      
    public static JFreeChart createChart(XYDataset dataset) {      
        // 创建JFreeChart对象：ChartFactory.createXYLineChart      
        JFreeChart jfreechart = ChartFactory.createXYLineChart("ROC", // 标题      
                "FP Rate", // categoryAxisLabel （category轴，横轴，X轴标签）      
                "TP Rate", // valueAxisLabel（value轴，纵轴，Y轴的标签）      
                dataset, // dataset      
                PlotOrientation.VERTICAL, true, // legend      
                false, // tooltips      
                false); // URLs      
      
        // 背景色 
        jfreechart.setBackgroundPaint(Color.WHITE); //这里设置成白色没有用, 图片仍然显示成红色
        // 使用CategoryPlot设置各种参数。以下设置可以省略。      
        XYPlot plot = (XYPlot) jfreechart.getPlot();      
         plot.setBackgroundPaint(Color.white);
     
        return jfreechart;      
    }      

	   // 保存为文件      
    public static void saveAsFile(JFreeChart chart, String outputPath,      
            int weight, int height) {      
        FileOutputStream out = null;      
        try {      
            File outFile = new File(outputPath);      
            if (!outFile.getParentFile().exists()) {      
                outFile.getParentFile().mkdirs();      
            }      
            out = new FileOutputStream(outputPath);      
            // 保存为PNG      
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);      
            // 保存为JPEG      
            // ChartUtilities.writeChartAsJPEG(out, chart, 500, 400);      
            out.flush();      
        } catch (FileNotFoundException e) {      
            e.printStackTrace();      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            if (out != null) {      
                try {      
                    out.close();      
                } catch (IOException e) {      
                    // do nothing      
                }      
            }      
        }      
    }      
	public ClassedAPIRoc() {

		// 读入数据集
		try {
			data = new Instances(new BufferedReader(new FileReader(
					"weka/total.arff")));
			data.setClassIndex(data.numAttributes() - 1);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	 
	  
	
	//一个分类器对应一条曲线
	public XYSeries  getSeries(Classifier c,String className){
	
		XYSeries dataSeries  = new XYSeries(className);      

		Evaluation eval;
		try {
			eval = new Evaluation(data);
			eval.crossValidateModel(c, data, 10, new Random(1));
			ThresholdCurve tc = new ThresholdCurve();
			Instances result = tc.getCurve(eval.predictions(), classIndex);

			int tpIndex = result.attribute(ThresholdCurve.TP_RATE_NAME).index();
			int fpIndex = result.attribute(ThresholdCurve.FP_RATE_NAME).index();

			double[] tpRate = result.attributeToDoubleArray(tpIndex);
			double[] fpRate = result.attributeToDoubleArray(fpIndex);
			
			for(int i = 0 ; i < tpRate.length; i++){
				dataSeries.add(fpRate[i],tpRate[i]);
			}
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSeries;
		
	}
	
 
	public Instances getData() {
		return data;
	}
	 
}
 