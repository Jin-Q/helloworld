package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitapp;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpLawLawsuitAppListOp extends CMISOperation {

	private final String modelId = "ArpLawLawsuitApp";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			String conditionMember = queryNameList(queryData, dao, connection, context,"001");
			conditionMember = conditionMember + queryNameList(queryData, dao, connection, context,"002");
			
		
			if(conditionStr.indexOf("WHERE") != -1){				
				conditionStr = conditionStr + conditionMember + " order by serno desc";
			}else{
				conditionStr = "where 1=1 " + conditionMember + " order by serno desc";
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			int size = 10;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));			
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("lawsuit_cap");
			list.add("lawsuit_int");
			list.add("lawsuit_sub");
			list.add("law_disp_mode");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("input_date");
			list.add("approve_status");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
			iColl.setName(iColl.getName()+"List");
			
			addNameList(iColl, dao, connection, context ,"001");
			addNameList(iColl, dao, connection, context ,"002");
			
			this.putDataElement2Context(iColl, context);			
			TableModelUtil.parsePageInfo(context, pageInfo);			
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
	
	/**
	 * 在诉讼列表上展示被告人、债务人
	 * 由于被告人、债务人是多条记录，这里只取pk_serno最小的一条（即最先录入的一条），处理后传到列表
	 */
	public void addNameList(IndexedCollection iColl,TableModelDAO dao ,Connection connection ,Context context ,String type) throws EMPException {
		String serno = "",Member_Name="" ,Member_Condition ="";
		int i = 0,j = 0;
		for (i = 0; i < iColl.size(); i++) {
			Member_Name="";
			KeyedCollection kColl = (KeyedCollection) iColl.get(i);
			serno = kColl.getDataValue("serno").toString();
			
			/*** 取最先录入的诉讼人员，作为首页展示 ***/
			if(type.equals("001")){	//被告人
				Member_Condition  = " where member_type = '001' and  serno = '"+serno+"'  and rownum = 1 order by pk_serno asc";
			}else{	//债务人
				Member_Condition  = " where member_type = '002' and  serno = '"+serno+"'  and rownum = 1 order by pk_serno asc";
			}
			
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("pk_serno");
			IndexedCollection Member_iColl = dao.queryList("ArpLawMemberInfo",list ,Member_Condition,connection);
			
			String[] args = new String[] { "cus_id"};
			String[] modelIds = new String[] { "CusBase"};
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name"};
			String[] resultName = new String[] { "cus_name"};
			SystemTransUtils.dealPointName(Member_iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			for  (j = 0; j < Member_iColl.size(); j++) {
				KeyedCollection Member_kColl = (KeyedCollection) Member_iColl.get(j);
				Member_Name = Member_kColl.getDataValue("cus_name")+"";
			}
			
			if(type.equals("001")){	//被告人
				kColl.addDataField("defendant_name", Member_Name);
			}else{	//债务人
				kColl.addDataField("debtor_name", Member_Name);
			}			
		}
	}
	
	/**
	 * 被告人、债务人查询处理
	 * 大致上是拿cus_id到ArpLawMemberMana中取case_no，再将多个case_no拼成sql的in条件,再返回处理
	 */
	public String queryNameList(KeyedCollection kColl,TableModelDAO dao ,Connection connection ,Context context,String type) throws EMPException {
		String serno = "", condition = "", value = "";
		if(kColl != null){
			if(type.equals("001")){
				value = kColl.getDataValue("defendant").toString();	//被告人名称
			}else{
				value = kColl.getDataValue("debtor").toString();	//债务人名称
			}	

			if(!value.equals("")){
				serno = "'";
				condition = "where cus_id = '"+value+"' and member_type = '"+type+"'";			
				IndexedCollection defendant_iColl = dao.queryList("ArpLawMemberInfo", condition, connection);				
				for( int j = 0; j < defendant_iColl.size(); j++) {
					KeyedCollection Member_kColl = (KeyedCollection) defendant_iColl.get(j);
					serno = serno +Member_kColl.getDataValue("serno").toString()+ "','";
				}			
				serno = "and serno in ("+serno +"')";
			}
		}
		return serno;
	}

}
