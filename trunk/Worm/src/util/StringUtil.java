package util;

import java.util.LinkedList;
import java.util.List;

public class StringUtil {

	
	/**
	 * 分割字符串，去掉空串 
	 * 使用方法： StringUtil.cut("1222aaaxxxcvaaa","aaa")，返回ArrayList，值为（1222，xxxcv）
	 * @param str 要分割的字符串
	 * @param token 分割的标准
	 * @return
	 */
	public static List<String> cut(String str,String token)   
    {   
        if(str==null||token==null){   
            throw new java.lang.IllegalArgumentException("所有参数不能为null !   str="+str+"  :  token="+token);   
        }   
        int len=str.length();int tLen=token.length();   
        if(tLen==0){   
            throw new java.lang.IllegalArgumentException("token长度不能为0 !");   
        }   
        if(len<tLen||(len==tLen&&!str.equals(token)))   
        {   
            List<String> list=new LinkedList<String>();   
            list.add(str);   
            return list;   
        }   
        //2个串相同，则为空
        if(str.equals(token)){   
            return new LinkedList<String>();   
        }   
        List<String> cuts=new LinkedList<String>();   
        int bg=0;int end=0;   
        for(int i=0;i<=len-tLen;i++)   
        {   
            if(str.charAt(i)==token.charAt(0))   
            {   
                boolean isToken=true;   
                for(int k=1;k<token.length();k++)   
                {   
                    if(str.charAt(i+k)!=token.charAt(k)){   
                        isToken=false;
                        break;   
                    }   
                }   
                if(isToken)   
                {   
                    end=i;   
                    String tem=str.substring(bg,end);   
                    if(tem.length()>0){   
                        cuts.add(tem);   
                    }   
                    bg=end+tLen;end=bg;   
                    i+=tLen-1;   
                }   
            }   
        }   
        if(bg<len){   
            cuts.add(str.substring(bg,len));   
        }   
        return cuts;   
    }  
	
	
	/**
	 * 算出一个字串在String中出现的次数
	 * @param str
	 * @param token
	 * @return
	 */
	public static int countToken(String str,String token){
	     int count = 0 ;
	     int index = -1 ;
	     while((index = str.indexOf(token))!=-1){//找到了一个
	    	 count ++;  
	    	 //从index+token长度开始，剩余的子串
	    	 str = str.substring(index+token.length());
	     }
	     return count;
	     
	}
	
	
	
	public static void main(String args[]){
		String str = "aaaefaacefvbddaaa";
		String token = "aa";
		PrintUtil.printList(cut(str,token));
		System.out.println(countToken(str, token));
	}

}
