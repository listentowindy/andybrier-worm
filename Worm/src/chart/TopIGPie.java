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
public class TopIGPie {

	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	String sql = "select count(*) from topig where ig < ? and ig > ?";
	/**
	 * @param args
	 * @throws IOException 
	 */ 
	public static void main(String[] args) throws IOException {
	   TopIGPie pie = new TopIGPie();
	   System.out.println("正在生成Top500 API信息增益分布情况图，准备数据需要一定的时间，请稍后");
	   pie.createChart();

	}
	
	
	// 生成饼图数据集对象
	   public  DefaultPieDataset  createDataset(){
		   int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0,i7=0;
		  conn = DBUtil.getConn();
		  try {
			ps = conn.prepareStatement(sql);
			
			ps.setDouble(1, 1);
			ps.setDouble(2, 0.99);
			rs = ps.executeQuery();
			rs.next();
		    i1 = rs.getInt(1);
		    
			ps.setDouble(1, 0.99);
			ps.setDouble(2, 0.98);
			rs = ps.executeQuery();
			rs.next();
		    i2 = rs.getInt(1);
		    
			ps.setDouble(1, 0.98);
			ps.setDouble(2, 0.97);
			rs = ps.executeQuery();
			rs.next();
		    i3 = rs.getInt(1);
			
			ps.setDouble(1, 0.97);
			ps.setDouble(2, 0.96);
			rs = ps.executeQuery();
			rs.next();
		    i4 = rs.getInt(1);
		    
			ps.setDouble(1, 0.96);
			ps.setDouble(2, 0.95);
			rs = ps.executeQuery();
			rs.next();
		    i5 = rs.getInt(1);
		    
			ps.setDouble(1, 0.95);
			ps.setDouble(2, 0.94);
			rs = ps.executeQuery();
			rs.next();
		    i6 = rs.getInt(1);
		    
			ps.setDouble(1, 0.94);
			ps.setDouble(2, 0.0);
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
	     defaultpiedataset.setValue("0.99-1",i1);
	     defaultpiedataset.setValue("0.98-0.99",i2);
	     defaultpiedataset.setValue("0.97-0.98",i3);
	     defaultpiedataset.setValue("0.96-0.97",i4);
	     defaultpiedataset.setValue("0.95-0.96",i5);
	     defaultpiedataset.setValue("0.94-0.95",i6);
	     defaultpiedataset.setValue("0.0-0.94",i7);
	     
	     return defaultpiedataset;
	   }
	   
	   //生成图表主对象JFreeChart
	   public   void createChart() throws IOException{
		   DefaultPieDataset data= createDataset();   
		   JFreeChart chart=ChartFactory.createPieChart(   
	                "Top 500 API的信息增益分布情况", data, true,   
	                false, false           
	        );   

		   chart.setTitle(new TextTitle("Top 500 API的信息增益分布情况",new Font("宋体",Font.ITALIC,17)));//标题字体   
	        //设置图例部分   
	        LegendTitle legend =chart.getLegend(0);   
	        legend.setItemFont(new Font("宋体",Font.BOLD,20));//设置图例的字体   
	        //设置图的部分   
	        PiePlot plot =(PiePlot)chart.getPlot();   
	        plot.setLabelFont(new Font("宋体",Font.BOLD,18));//设置实际统计图的字体   
	        plot.setBackgroundAlpha(0.9f);   
	        plot.setForegroundAlpha(0.50f);   
	        
	         //设置图上显示百分比
	        PiePlot pieplot = (PiePlot)chart.getPlot();
	        pieplot.setLabelFont(new Font("宋体", 0, 12));
	        pieplot.setNoDataMessage("无数据");
	        pieplot.setCircular(true);
	        pieplot.setLabelGap(0.02D);
	        pieplot.setBackgroundPaint(new Color(199,237,204));
	                pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(
	                "{0} {2}",
	                NumberFormat.getNumberInstance(),
	                new DecimalFormat("0.00%")));
	                
	        pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));  
	       
	        
	           
	        FileOutputStream fos=new FileOutputStream("chart/Topapi.jpg");   
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
