package com.yucheng.cmis.biz01line.cus.cushand.extInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.ecc.emp.log.EMPLog;

/**
 * 执行按客户经理所有客户的客户资料与业务资料移交
 * 业务中客户经理与主管机构是分表存储，导致不能同步更新，需相互依赖处理，故将此步骤放移交后处理类中处理。
 * 此处使用接收机构作为【合同】表的查询条件，原因：业务合同已经修改完成
 * @Version 1.0
 * @author tsy 2014-05-3 
 * Description:
 */
public class CusOperationHandoverExtImp implements HandoverInterface {
	public void afterAction(Map<String,Object> map, Connection conn) {
		EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "开始按客户经理所有客户的客户资料与业务资料移交后的客户经理绩效处理----");
		
		String handoverscope = (String)map.get("handoverScope");  //移交范围
		String receiveid = (String)map.get("receiveid");   //接收人
		String receivesorg = (String)map.get("receivesorg");  //接收机构
		String handoverid = (String)map.get("handoverid");  //移出人
		//String handoversorg = (String)map.get("handoversorg");  //移出机构
		
		/** 如果移交方式为“2-客户与业务移交”时需要过滤出生效的协议  2014-10-13 唐顺岩 **/
		String handoverMode = (String)map.get("handoverMode");  //移交方式  
		String condition = "";
		if("2".equals(handoverMode)){   //2-客户与业务移交
			condition = " AND CONT_STATUS='200' ";
		}
		
		//修改客户经理SQL语句
		StringBuffer extSql =  new StringBuffer("");
		extSql.append("UPDATE CUS_MANAGER SET MANAGER_ID ='"+receiveid+"' WHERE MANAGER_ID ='"+handoverid+"' ");
		extSql.append("AND CONT_NO IN(");
		extSql.append("SELECT CONT_NO FROM CTR_LOAN_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition+" UNION ALL ");   //借款合同
		extSql.append("SELECT CONT_NO FROM CTR_RPDDSCNT_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition+" UNION ALL ");  //装贴现合同
		extSql.append("SELECT CONT_NO FROM CTR_LIMIT_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition+" UNION ALL ");   //额度合同
		extSql.append("SELECT CONT_NO FROM CTR_ASSETSTRSF_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition+" UNION ALL ");  //资产转让合同
		extSql.append("SELECT CONT_NO FROM CTR_ASSET_PRO_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition+" UNION ALL ");  //资产证券化协议
		extSql.append("SELECT CONT_NO FROM CTR_ASSET_TRANS_CONT WHERE MANAGER_BR_ID ='"+receivesorg+"'"+condition);   //资产流转协议
		extSql.append(")");
		/**END**/
		
		EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "按客户经理移交所有客户的客户资料与业务资料后续处理语句："+extSql.toString());
		/* 按客户经理所有客户移交 ，不需要循环遍历所有客户信息     2014-05-23  唐顺岩   */
		if("2".equals(handoverscope)){ 
			 Statement stmt=null;
	        try{
	            stmt=conn.createStatement();
	            stmt.executeUpdate(extSql.toString());
	        }catch (Exception e) {
	        	EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "SQL执行异常:"+e.getMessage());
	        }finally{
	            try{
	                if(stmt!=null){
	                    stmt.close();
	                }
	            }catch (SQLException e) {
	            	EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "SQL执行异常:"+e.getMessage());
	            }
	        }
		}
		
		EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "按客户经理所有客户的客户资料与业务资料移交后的客户经理绩效处理完成----");
	}

	public Map<String,Object> beforAction(Map<String,Object> map, Connection conn) {
		return map;
	}
}
