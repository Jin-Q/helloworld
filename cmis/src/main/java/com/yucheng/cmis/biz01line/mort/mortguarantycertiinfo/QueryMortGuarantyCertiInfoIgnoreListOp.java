package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantyCertiInfoIgnoreListOp extends CMISOperation {


	private final String modelId = "MortGuarantyCertiInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guaranty_no = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("subMenuId")){
				context.setDataValue("subMenuId", "qz");
			}
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		//	String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
			//						+"";
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			int size = 10;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			//此为Tab页时的权证信息
			if(context.containsKey("guaranty_no")){
				guaranty_no = (String) context.getDataValue("guaranty_no");
				conditionStr ="where guaranty_no = '"+guaranty_no+"'";
				IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
				iColl.setName(iColl.getName()+"List");
				SInfoUtils.addSOrgName(iColl, new String[] { "keep_org_no","hand_org_no"});
				this.putDataElement2Context(iColl, context);
				TableModelUtil.parsePageInfo(context, pageInfo);
				return "1";
			}else{
				context.addDataField("guaranty_no","");
				List<String> list = new ArrayList<String>();
				list.add("warrant_cls");
				list.add("warrant_type");
				list.add("warrant_no");
				list.add("guaranty_no");
//				list.add("guaranty_type");
				list.add("keep_org_no");
				list.add("hand_org_no");
				list.add("warrant_state");
				list.add("is_main_warrant");
				list.add("warrant_name");
				//此为单独作为主页面的权证信息
				if(context.containsKey("act")){
					//拼接临时出库where查询条件
					if("temp".equals(context.getDataValue("act"))){
						if("".equals(conditionStr)){
							conditionStr +="where warrant_state='4'";
						}else{
							conditionStr +="and warrant_state='4'";
						}
						IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
						SInfoUtils.addSOrgName(iColl, new String[] { "keep_org_no","hand_org_no"});
						iColl.setName(iColl.getName()+"List");
						this.putDataElement2Context(iColl, context);
						TableModelUtil.parsePageInfo(context, pageInfo);
						return "2";
					}
					//拼接已出库where查询条件
					if("out".equals(context.getDataValue("act"))){
						if("".equals(conditionStr)){
							conditionStr +="where warrant_state in ('6','7')";
						}else{
							conditionStr +="and warrant_state in ('6','7')";
						}
						IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
						SInfoUtils.addSOrgName(iColl, new String[] { "keep_org_no","hand_org_no"});
						iColl.setName(iColl.getName()+"List");
						this.putDataElement2Context(iColl, context);
						TableModelUtil.parsePageInfo(context, pageInfo);
						return "2";
					}	
					//拼接已入库where查询条件（根据查询时传参）
					if("act".equals(context.getDataValue("act"))){
						if("".equals(conditionStr)){
							conditionStr +="where warrant_state='3' and warrant_type||'@'||warrant_no not in (select warrant_type||'@'||warrant_no from mort_stor_exwa_detail where serno not in (select serno from mort_stor_exwa_info where approve_status='998' ))";
						}else{
							conditionStr +="and warrant_state='3' and warrant_type||'@'||warrant_no not in (select warrant_type||'@'||warrant_no from mort_stor_exwa_detail where serno not in (select serno from mort_stor_exwa_info where approve_status='998' ))";
						}
					}
					IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
					SInfoUtils.addSOrgName(iColl, new String[] { "keep_org_no","hand_org_no"});
					iColl.setName(iColl.getName()+"List");
					this.putDataElement2Context(iColl, context);
					TableModelUtil.parsePageInfo(context, pageInfo);
					return "3";
				}else{
					if("".equals(conditionStr)){
						conditionStr +="where warrant_state='3' and warrant_type||'@'||warrant_no not in (select warrant_type||'@'||warrant_no from mort_stor_exwa_detail where serno not in (select serno from mort_stor_exwa_info where approve_status='998' ))";
					}else{
						conditionStr +="and warrant_state='3' and warrant_type||'@'||warrant_no not in (select warrant_type||'@'||warrant_no from mort_stor_exwa_detail where serno not in (select serno from mort_stor_exwa_info where approve_status='998' ))";
					}
					IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
					SInfoUtils.addSOrgName(iColl, new String[] { "keep_org_no","hand_org_no"});
					iColl.setName(iColl.getName()+"List");
					this.putDataElement2Context(iColl, context);
					TableModelUtil.parsePageInfo(context, pageInfo);
					return "3";
					
				}
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
	}

}
