package com.yucheng.cmis.biz01line.grt.domain.grtguarantee;               
                                                                         
import com.yucheng.cmis.pub.CMISDomain;                                  
                                                                         
public class GrtGuartee implements CMISDomain {                         
                                                                         
	private String conditionStr="";
	
	public String getConditionStr() {
		return conditionStr;
	}
	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}       
	public String doGetSql()
	{  
		String Sql = "select t.guar_cont_no,"
		+"t.cus_id,"
		+"t.guar_start_date,"
		+"t.guar_end_date,"
		+"t.input_id,"
		+"t.manager_id,"
		+"t.input_br_id,"
		+"t.manager_br_id,"
	    +"t.reg_date,"
	    +"t1.cus_id as guarty_cus_id,"
	    +"t1.guar_amt,"
	    +"t1.guar_type "
	    +"from Grt_Guar_Cont t, grt_guarantee t1 "
	    +"where t.guar_cont_no in"
	    +"(select t2.guar_cont_no "
	    +"from grt_guaranty_re t2 "
	    +"where t1.guar_id = t2.guaranty_id )"+this.conditionStr;
		return Sql;
	}                                                               
	public/*protected*/ Object clone() throws CloneNotSupportedException { 
                                                                         
		// call父类的clone方法                                               
                                                                         
		Object result = super.clone();                                       
                                                                         
                                                                         
                                                                         
		//TODO: 定制clone数据                                                
                                                                         
		return result;                                                       
                                                                         
		}                                                                    
                                                                         
	                                                                       
}                                                                        