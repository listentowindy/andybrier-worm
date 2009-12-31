package util;

import java.util.Date;

/**
 * 2进制Binary
 * 8进制Octal
 * 10进制Decimal
 * 16进制Hex
 * @author Administrator
 *
 */
public class NumberUtil {

	
	/**
	 * 二进制转化为10进制
	 * @param binary 1001101 1*2(0)+0*2(1)
	 */
	public static int  binToDec(String binary){
		int number = 0;
		int length = binary.length();
		int begin = 0 ;
		for(int i = length-1; i>=0; i--){
			
			char c = binary.charAt(i); 
			
			//check 0~1之间
			if((c > '1') || (c < '0')){
				System.err.println("输入数据 "+ binary + " 有错误！");
				System.exit(0);
			}
			if(c=='1'){
				number = number + (int)Math.pow(2,begin);
			}
			begin++;
		}
		return number;
	}
	
	/**
	 * 8进制转化为10进制.以0开始
	 * 例如：1507 , 7 * 8(0) + 0 * 8(1) + 5 * 8(2) + 1 * 8(3) = 839
	 * @param octal 8进制的数字
	 * @return
	 */
	public static int OctalToDec(String octal){
		int number = 0;
		int length = octal.length();
		int begin = 0 ;
		for(int i = length-1; i>=0; i--){
			
			char c = octal.charAt(i); 
			int aNumber = Integer.parseInt(""+c);
			//check 0~7之间
			if((c>'7') || (c < '0')){
				System.err.println("输入数据 "+ octal + " 有错误！");
				System.exit(0);
			}
			if(c!='0'){
				number = number + aNumber * (int)Math.pow(8,begin);
			}
			begin++;
		}
		return number;
	}
	
	/**
	 * 16进制转化为10进制. 用 0~9 +A，B，C，D，E，F 表示.0x开头
	 * 例如： 2AF5 , 5 * 16（0)  + F * 16(1) + A * 16(2) + 2 * 16(3) = 10997
	 * @param hex 16进制的数字 
	 * @return
	 */
	public static int HexToDec(String hex){
		
		if(hex.indexOf("0x")!=0){
			System.err.println("输入数据 "+ hex + " 有错误！ 注意以0x开始开头");
			System.exit(0);
		}
		
		int result = 0;  //结果
		int length = hex.length();
		int begin = 0 ; 
		for(int i = length-1; i>=2; i--){
			
			int aNumber = 0;  
			char c = hex.charAt(i); 
			
			//check 0~F之间
			if((c>'F') || (c < '0')){
				System.err.println("输入数据 "+ hex + " 有错误！");
				System.exit(0);
			}
			
			//数字部分
			if( c >='0' && c <='9'){
				aNumber = Integer.parseInt(""+c);
			}else{  //字母部分
				c = (""+c).toUpperCase().charAt(0); //转化为大写
				switch(c){
				case('A') : aNumber=10; break;
				case('B') : aNumber=11; break;
				case('C') : aNumber=12; break;
				case('D') : aNumber=13; break;
				case('E') : aNumber=14; break;
				case('F') : aNumber=15; break;
				}
			}
		
			if(c!='0'){
				result = result + aNumber * (int)Math.pow(16,begin);
			}
			begin++;
		}
		return result;
	}
	
	/**
	 * 10进制转化为2进制
	 * @param dec
	 * @return
	 */
	public static String  decToBin(int dec){
		
		return null;
	}
	
	
	
	
	
	
	
	 public static void main(String args[]){
	    	System.out.println(binToDec("11111110"));
	    	System.out.println(OctalToDec("155607"));
	    	System.out.println(HexToDec("0x2af5"));
	    }
	    
}
