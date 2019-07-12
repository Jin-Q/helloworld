package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtGrpApplyListOp extends CMISOperation {

	private final String modelId = "LmtAppGrp";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			if(null != conditionStr && !"".equals(conditionStr)){
				conditionStr += " AND APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}else{
				conditionStr += " WHERE APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}
			
			if(context.containsKey("type") && "app".equals(context.getDataValue("type"))){   //申请
				/**modified by lisj 2014-12-10 需求编号：【XD141107075】 支持输入客户名称可模糊查询 begin**/
				String GrpName="";
				String GrpMemberName="";
				String grpIdList="";
				IndexedCollection iCollSelect = new IndexedCollection();
				IndexedCollection iCollSelect4GM = new IndexedCollection();
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
				if(queryData !=null && !"".equals(queryData)){
					GrpName = (String) queryData.getDataValue("grp_name");
					if(GrpName !=null && !"".equals(GrpName)){
						String conSelect ="select c.grp_no from cus_grp_info c where c.grp_name like '%"+GrpName+"%'";
						iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect !=null && iCollSelect.size() > 0){
							for(int i=0;i<iCollSelect.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
								grpIdList += kColl.getDataValue("grp_no")+",";
							}
							conditionStr += "and instr('"+ grpIdList+"', grp_no)>0";
						}else{
							conditionStr +="and grp_no=''";
						}
					}
					//增加通过集团成员查询集团客户的功能 2015-3-24 by lisj
					GrpMemberName = (String) queryData.getDataValue("grp_member_name");
					if(GrpMemberName !=null && !"".equals(GrpMemberName)){
						String conSelect ="select p1.grp_no from lmt_app_grp p1, lmt_apply p2, cus_base p3 "
								         +"where p1.serno = p2.grp_serno and p3.cus_id = p2.cus_id "
								         +"and p3.cus_name like '%"+GrpMemberName+"%'";
						iCollSelect4GM = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect4GM !=null && iCollSelect4GM.size() > 0){
							for(int i=0;i<iCollSelect4GM.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect4GM.get(i);
								grpIdList += kColl.getDataValue("grp_no")+",";
							}
								conditionStr += "and instr('"+ grpIdList+"', grp_no)>0";						
						}else{
							conditionStr +="and grp_no=''";
						}
					}
				}
				/**modified by lisj 2014-12-10 需求编号：【XD141107075】 支持输入客户名称可模糊查询 end**/
				returnStr ="app";
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
			}else if(context.containsKey("type") && "cusHis".equals(context.getDataValue("type"))){   
				if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
					conditionStr +=" and approve_status!='000'  and serno in( select la.grp_serno from lmt_apply la  where la.cus_id='"+context.getDataValue("cus_id").toString()+"') order by app_date desc ";
				}else{
					throw new EMPException("查询集团（关联）授信审批历史出错，传入客户码[cus_id]不能为空。");
				}
				returnStr ="cusHis";
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
			}else{   //历史
				/**modified by lisj 2014-12-10 需求编号：【XD141107075】 支持输入客户名称可模糊查询 begin**/
				String GrpName="";
				String GrpMemberName="";
				String grpIdList="";
				IndexedCollection iCollSelect = new IndexedCollection();
				IndexedCollection iCollSelect4GM = new IndexedCollection();
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
				if(queryData !=null && !"".equals(queryData)){
					GrpName = (String) queryData.getDataValue("grp_name");
					if(GrpName !=null && !"".equals(GrpName)){
						String conSelect ="select c.grp_no from cus_grp_info c where c.grp_name like '%"+GrpName+"%'";
						iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect !=null && iCollSelect.size() > 0){
							for(int i=0;i<iCollSelect.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
								grpIdList += kColl.getDataValue("grp_no")+",";
							}
							conditionStr += "and instr('"+ grpIdList+"', grp_no)>0";
						}else{
							conditionStr +="and grp_no=''";
						}
					}
					//增加通过集团成员查询集团客户的功能 2015-3-24 by lisj
					GrpMemberName = (String) queryData.getDataValue("grp_member_name");
					if(GrpMemberName !=null && !"".equals(GrpMemberName)){
						String conSelect ="select p1.grp_no from lmt_app_grp p1, lmt_apply p2, cus_base p3 "
								         +"where p1.serno = p2.grp_serno and p3.cus_id = p2.cus_id "
								         +"and p3.cus_name like '%"+GrpMemberName+"%'";
						iCollSelect4GM = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect4GM !=null && iCollSelect4GM.size() > 0){
							for(int i=0;i<iCollSelect4GM.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect4GM.get(i);
								grpIdList += kColl.getDataValue("grp_no")+",";
							}
								conditionStr += "and instr('"+ grpIdList+"', grp_no)>0";						
						}else{
							conditionStr +="and grp_no=''";
						}
					}
				}
				/**modified by lisj 2014-12-10 需求编号：【XD141107075】 支持输入客户名称可模糊查询 end**/
				returnStr ="his";
			}
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
			if(!(context.containsKey("type") && "cusHis".equals(context.getDataValue("type"))))
				conditionStr += " order by serno desc";
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("grp_no");
			list.add("serno");
			list.add("app_type");
			list.add("crd_totl_amt");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("approve_status");
			list.add("cur_type");
			list.add("app_date");
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "grp_no" };
			String[] modelIds=new String[]{"CusGrpInfo"};
			String[] modelForeign=new String[]{"grp_no"};
			String[] fieldName=new String[]{"grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
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
		return returnStr;
	}

}
