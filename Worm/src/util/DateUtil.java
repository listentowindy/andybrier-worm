package util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	
	
	 /**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    
    /** 
     * 获取现在时间 
     * 
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss 
     */ 
    public static Date getNowDate() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     String dateString = formatter.format(currentTime); 
     ParsePosition pos = new ParsePosition(8); 
     Date currentTime_2 = formatter.parse(dateString, pos); 
     return currentTime_2; 
    } 
     
    /** 
     * 获取现在时间 
     * 
     * @return返回短时间格式 yyyy-MM-dd 
     */ 
    public static Date getNowDateShort() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
     String dateString = formatter.format(currentTime); 
     ParsePosition pos = new ParsePosition(8); 
     Date currentTime_2 = formatter.parse(dateString, pos); 
     return currentTime_2; 
    } 
     
    /** 
     * 获取现在时间 
     * 
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss 
     */ 
    public static String getStringDate() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     String dateString = formatter.format(currentTime); 
     return dateString; 
    } 
     
    /** 
     * 获取现在时间 
     * 
     * @return 返回短时间字符串格式yyyy-MM-dd 
     */ 
    public static String getStringDateShort() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
     String dateString = formatter.format(currentTime); 
     return dateString; 
    } 
     
    /** 
     * 获取时间 小时:分;秒 HH:mm:ss 
     * 
     * @return 
     */ 
    public static String getTimeShort() { 
     SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); 
     Date currentTime = new Date(); 
     String dateString = formatter.format(currentTime); 
     return dateString; 
    } 
     
    /** 
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss 
     * 
     * @param strDate 
     * @return 
     */ 
    public static Date strToDateLong(String strDate) { 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     ParsePosition pos = new ParsePosition(0); 
     Date strtodate = formatter.parse(strDate, pos); 
     return strtodate; 
    } 
     
    /** 
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss 
     * 
     * @param dateDate 
     * @return 
     */ 
    public static String dateToStrLong(java.util.Date dateDate) { 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     String dateString = formatter.format(dateDate); 
     return dateString; 
    } 
     
    /** 
     * 将短时间格式时间转换为字符串 yyyy-MM-dd 
     * 
     * @param dateDate 
     * @param k 
     * @return 
     */ 
    public static String dateToStr(java.util.Date dateDate) { 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
     String dateString = formatter.format(dateDate); 
     return dateString; 
    } 
     
    /** 
     * 将短时间格式字符串转换为时间 yyyy-MM-dd 
     * 
     * @param strDate 
     * @return 
     */ 
    public static Date strToDate(String strDate) { 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
     ParsePosition pos = new ParsePosition(0); 
     Date strtodate = formatter.parse(strDate, pos); 
     return strtodate; 
    } 
     
    /** 
     * 得到现在时间 
     * 
     * @return 
     */ 
    public static Date getNow() { 
     Date currentTime = new Date(); 
     return currentTime; 
    } 
     
    /** 
     * 提取一个月中的最后一天 
     * 
     * @param day 
     * @return 
     */ 
    public static Date getLastDate(long day) { 
     Date date = new Date(); 
     long date_3_hm = date.getTime() - 3600000 * 34 * day; 
     Date date_3_hm_date = new Date(date_3_hm); 
     return date_3_hm_date; 
    } 
     
    /** 
     * 得到现在时间 
     * 
     * @return 字符串 yyyyMMdd HHmmss 
     */ 
    public static String getStringToday() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss"); 
     String dateString = formatter.format(currentTime); 
     return dateString; 
    } 
     
    /** 
     * 得到现在小时 
     */ 
    public static String getHour() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     String dateString = formatter.format(currentTime); 
     String hour; 
     hour = dateString.substring(11, 13); 
     return hour; 
    } 
     
    /** 
     * 得到现在分钟 
     * 
     * @return 
     */ 
    public static String getTime() { 
     Date currentTime = new Date(); 
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
     String dateString = formatter.format(currentTime); 
     String min; 
     min = dateString.substring(14, 16); 
     return min; 
    } 
    
    public static void main(String args[]){
    	System.out.print(getWeekOfDate(new Date()));
    }
    
    
}
