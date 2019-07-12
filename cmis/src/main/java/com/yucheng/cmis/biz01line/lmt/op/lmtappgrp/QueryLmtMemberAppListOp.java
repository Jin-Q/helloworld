package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtMemberAppListOp extends CMISOperation {

	private final String modelIdApp = "LmtApply";
//	private final String modelIdAgr = "LmtAgrInfo";
	private final String modelIdAppHis = "LmtAppGrpMemHis";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			IndexedCollection iCollAgr = null;
			String group_serno = (String)context.getDataValue("serno");//集团申请编号
//			String grp_agr_no = (String)context.getDataValue("grp_agr_no");//原集团协议编号
			if(group_serno==null||"".equals(group_serno)){
				throw new Exception("集团申请编号[serno]不能为空");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
//			if(null != grp_agr_no && !"".equals(grp_agr_no)){
//				//根据原协议编号
//				String conditionStrAgr =  "where grp_agr_no ='"+ grp_agr_no+"'";
//				iCollAgr = dao.queryList(modelIdAgr, null ,conditionStrAgr,connection);;
//				iCollAgr.setName(iCollAgr.getName()+"List");
//				String[] args=new String[] { "cus_id" };
//				String[] modelIds=new String[]{"CusBase"};
//				String[] modelForeign=new String[]{"cus_id"};
//				String[] fieldName=new String[]{"cus_name"};
//				//详细信息翻译时调用			
//				SystemTransUtils.dealName(iCollAgr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
//				this.putDataElement2Context(iCollAgr, context);
//			}
			
//			if(context.containsKey("app_type") && "02".equals(context.getDataValue("app_type"))){
				//根据客户查询出客户所有授信台账
				String conditionStrAgr =  "WHERE SERNO ='"+ group_serno+"'";
				iCollAgr = dao.queryList(modelIdAppHis,null ,conditionStrAgr,connection);
				iCollAgr.setName("LmtAgrInfoList");
				String[] args=new String[] { "cus_id" };
				String[] modelIds=new String[]{"CusBase"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"cus_name"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(iCollAgr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				this.putDataElement2Context(iCollAgr, context);
//			}
			
			//申请表
			String conditionStrApp = " where grp_serno ='" + group_serno+"'";
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("app_type");
			list.add("cus_id");
			list.add("crd_totl_amt");
			list.add("biz_type");
			list.add("cur_type");
			list.add("lmt_type");
			IndexedCollection iColl = dao.queryList(modelIdApp,list ,conditionStrApp,connection);
			
			iColl.setName(iColl.getName()+"List");
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(iColl, context);
		}catch (EMPException ee) {
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
