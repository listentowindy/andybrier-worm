package worm;
import java.sql.ResultSet;

import util.DBOperate;
/** 
 * @author 作者：袁启侠 
 *  生成表sequence的ig字段.计算熵
 * @version 创建时间：2009-10-9 下午03:28:35 
 */
public class ShangV2 {
   
	
	private int total=314;
	private int tbenign=48,tvirus=115,ttrojan=67,tworm=84;//各种文件数目
	private DBOperate db;
	private ResultSet rs;
	public ShangV2() {
		db=new DBOperate();
	}
    public void createShang(){
    	DBOperate db2=new DBOperate();
    	String sql="select * from sequence",name;
    	rs=db.select(sql);
     
    	int count,benign,worm,virus,trojan;
    	double p1,p2,r1,r2,result;
    	try{
    		while(rs.next()){
    			name=rs.getString("name");
    			benign=rs.getInt("benign");
    			virus=rs.getInt("virus");
    			worm=rs.getInt("worm");
    			trojan=rs.getInt("trojan");
    			count=benign+virus+worm+trojan;
    			 
    		 
    			p1=((double)count)/total;
    			p2=1-p1;        
    			r1=(((double)benign)/count)*log2(((double)benign)/count);
    			r1+=(((double)virus)/count)*log2(((double)virus)/count);
    			r1+=(((double)worm)/count)*log2(((double)worm)/count);
    			r1+=(((double)trojan)/count)*log2(((double)trojan)/count);
    			r2=(((double)(tbenign-benign))/(total-count))*log2(((double)(tbenign-benign))/(total-count));
    		 
    			r2+=(((double)(tvirus-virus))/(total-count))*log2(((double)(tvirus-virus))/(total-count));
    			 
    			r2+=(((double)(tworm-worm))/(total-count))*log2(((double)(tworm-worm))/(total-count));
    		 
    			r2+=(((double)(ttrojan-trojan))/(total-count))*log2(((double)(ttrojan-trojan))/(total-count));
    			result=p1*r1+p2*r2;
    		 
    			sql="update sequence set ig="+result+" where name='"+name+"'";
    			db2.execute(sql);
    		}
    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		db2.closeStatement();
    		db2.closeConnection();
    		db.closeStatement();
    		db.closeConnection();
    	}
    	
    }
    /**
     * log2的定义
     * @param value
     * @return
     */
	private double log2(double value) {
		if(value<0.000000000000001&&value>-0.000000000000001)
			return 0;
		return (Math.log(value)/Math.log(2));
	}
 

}
