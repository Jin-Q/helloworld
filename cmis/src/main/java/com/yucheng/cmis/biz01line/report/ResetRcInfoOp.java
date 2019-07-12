package com.yucheng.cmis.biz01line.report;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-7-22
*@description TODO 需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更
*@version v1.0
*
 */
public class ResetRcInfoOp extends CMISOperation {

	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String print_type ="";
			String guaranty_no =null;
			String agr_no = null;
			String cont_no = null;
			String cus_id = null;
			String po_no = null;
			String task_id = null;
			try {
				if(context.containsKey("guaranty_no") && !"".equals(context.getDataValue("guaranty_no"))){
					guaranty_no = context.getDataValue("guaranty_no").toString();
				}
				if(context.containsKey("print_type") && !"".equals(context.getDataValue("print_type"))){
					print_type = context.getDataValue("print_type").toString();
				}			
				if(context.containsKey("agr_no") && !"".equals(context.getDataValue("agr_no"))){
					agr_no = context.getDataValue("agr_no").toString();
				}
				if(context.containsKey("cont_no") && !"".equals(context.getDataValue("cont_no"))){
					cont_no = context.getDataValue("cont_no").toString();
				}
				if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
					cus_id = context.getDataValue("cus_id").toString();
				}
				if(context.containsKey("po_no") && !"".equals(context.getDataValue("po_no"))){
					po_no = context.getDataValue("po_no").toString();
				}
				if(context.containsKey("task_id") && !"".equals(context.getDataValue("task_id"))){
					task_id = context.getDataValue("task_id").toString();
				}
			} catch (Exception e) {}
			
			//重置抵（质）押物资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("00")>=0)){
				if(guaranty_no!=null && !"".equals(guaranty_no)){
					try {
						  String sql  = "delete from cr_limits_info cr where cr.guaranty_no ='"+guaranty_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置保证担保法律要件（单一法人/集团成员/联保成员/个人）资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("01")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
						  String sql  = "delete from cr_jural_info cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置产品个性化资料数据（协议管理）
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("02")>=0)){
				if(cont_no!=null && !"".equals(cont_no)){
					try {
						  String sql  = "delete from cr_prd_indiv_info cr where cr.cont_no ='"+cont_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
					
			//重置纯基础资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("03")>=0)){
				if(cus_id!=null && !"".equals(cus_id)){
					try {
					  String sql  = "delete from cr_cus_base_info cr where cr.cus_id ='"+cus_id+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置授信资料（单一法人/集团成员/联保小组成员/个人）数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("04")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
						  String sql  = "delete from cr_credit_info cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
					
					try {
						  String sql  = "delete from cr_crd_prd_info cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置用信资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("05")>=0)){
				if(cont_no!=null && !"".equals(cont_no)){
					try {
						  String sql  = "delete from cr_agr_info cr where cr.cont_no ='"+cont_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置贷后资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("06")>=0)){
				if(task_id!=null && !"".equals(task_id)){
					try {
						  String sql  = "delete from cr_pvp_info cr where cr.task_id ='"+task_id+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置发票资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("07")>=0)){
				if(cont_no!=null && !"".equals(cont_no)){
					try {
						  String sql  = "delete from cr_acc_info cr where cr.cont_no ='"+cont_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置产品个性化资料数据（客户管理入口）
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("08")>=0)){
				if(cus_id!=null && !"".equals(cus_id)){
					try {
					  String sql  = "delete from cr_indiv_info_cus cr where cr.cus_id ='"+cus_id+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置合作方授信资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("09")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
					  String sql  = "delete from cr_credit_info cr where cr.agr_no ='"+agr_no+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
					
					try {
						  String sql  = "delete from cr_crd_prd_coop cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置行业授信资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("10")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
					  String sql  = "delete from cr_credit_info cr where cr.agr_no ='"+agr_no+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
					
					try {
						  String sql  = "delete from cr_crd_prd_coop cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置圈商授信资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("11")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
					  String sql  = "delete from cr_credit_info cr where cr.agr_no ='"+agr_no+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
					
					try {
						  String sql  = "delete from cr_crd_prd_coop cr where cr.agr_no ='"+agr_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置产品个性化资料数据（协议管理）
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("12")>=0)){
				if(agr_no!=null && !"".equals(agr_no)){
					try {
					  String sql  = "delete from cr_indiv_info_lmt cr where cr.agr_no ='"+agr_no+"'";
					  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置保证担保法律要件（一般担保合同）资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("13")>=0)){
				if(cont_no!=null && !"".equals(cont_no)){
					try {
						  String sql  = "delete from cr_jural_info_clc cr where cr.cont_no ='"+cont_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			//重置池管理资料数据
			if(print_type!=null && !"".equals(print_type) && (print_type.indexOf("14")>=0)){
				if(po_no!=null && !"".equals(po_no)){
					try {
						  String sql  = "delete from cr_pool_mana_info cr where cr.po_no ='"+po_no+"'";
						  SqlClient.deleteBySql(sql, connection);
					} catch (Exception e) {}
				}
			}
			
			context.addDataField("flag","success");
		}catch (EMPException ee) {
			context.addDataField("flag","error");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
