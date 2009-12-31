package chart;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import util.DBUtil;

/**
 * 生成所有IG（表ig）的分布图
 * @author Administrator
 *
 */
public class IGPie {

	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	String sql = "select count(*) from allig where ig < ? and ig > ?";
	/**
	 * @param args
	 * @throws IOException 
	 */ 
	public static void main(String[] args) throws IOException {
	   IGPie pie = new IGPie();
	   System.out.println("正在生成API信息增益分布情况图，准备数据需要一定的时间，请稍后");
	   pie.createChart();

	}
	
	
	// 生成饼图数据集对象
	   public  DefaultPieDataset  createDataset(){
		   int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0,i7=0;
		  conn = DBUtil.getConn();
		  try {
			ps = conn.prepareStatement(sql);
			
			ps.setDouble(1, 1);
			ps.setDouble(2, 0.9);
			rs = ps.executeQuery();
			rs.next();
		    i1 = rs.getInt(1);
		    
			ps.setDouble(1, 0.9);
			ps.setDouble(2, 0.8);
			rs = ps.executeQuery();
			rs.next();
		    i2 = rs.getInt(1);
		    
			ps.setDouble(1, 0.8);
			ps.setDouble(2, 0.7);
			rs = ps.executeQuery();
			rs.next();
		    i3 = rs.getInt(1);
			
			ps.setDouble(1, 0.7);
			ps.setDouble(2, 0.6);
			rs = ps.executeQuery();
			rs.next();
		    i4 = rs.getInt(1);
		    
			ps.setDouble(1, 0.6);
			ps.setDouble(2, 0.5);
			rs = ps.executeQuery();
			rs.next();
		    i5 = rs.getInt(1);
		    
			ps.setDouble(1, 0.5);
			ps.setDouble(2, 0.4);
			rs = ps.executeQuery();
			rs.next();
		    i6 = rs.getInt(1);
		    
			ps.setDouble(1, 0.4);
			ps.setDouble(2, 0);
			rs = ps.executeQuery();
			rs.next();
		    i7 = rs.getInt(1);
		    
		    rs.close();
		    conn.close();
		    
		    System.out.println("准备数据完成");
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		   
	     DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
	     defaultpiedataset.setValue("0.9-1",i1);
	     defaultpiedataset.setValue("0.8-0.9",i2);
	     defaultpiedataset.setValue("0.7-0.8",i3);
	     defaultpiedataset.setValue("0.6-0.7",i4);
	     defaultpiedataset.setValue("0.5-0.6",i5);
	     defaultpiedataset.setValue("0.4-0.5",i6);
	     defaultpiedataset.setValue("0.0-0.4",i7);
	     
	     return defaultpiedataset;
	   }
	   
	   //生成图表主对象JFreeChart
	   public   void createChart() throws IOException{
		   DefaultPieDataset data= createDataset();   
		   JFreeChart chart=ChartFactory.createPieChart(   
	                "所有生成API的信息增益分布情况", data, true,   
	                false, false           
	        );   

		   chart.setTitle(new TextTitle("API的信息增益分布情况",new Font("宋体",Font.ITALIC,17)));//标题字体   
	       
		   //设置饼图上显示数量：，{0}表示section名，{1}表示section的值，{2}表示百分比
		   PiePlot pieplot = (PiePlot) chart.getPlot(); //通过JFreeChart 对象获得 
           pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: ({1})")); //饼图上显示数量
	       pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: ({2})"));//下面文字处显示百分比
	           
	        FileOutputStream fos=new FileOutputStream("chart/api.jpg");   
	        ChartUtilities.writeChartAsJPEG(   
	                fos,   
	                1,   
	                chart,   
	                800,   
	                600,   
	                null  
	        );   
	        fos.close();   
	        
	        System.out.println("生成了图片");

		   
	   }

}
