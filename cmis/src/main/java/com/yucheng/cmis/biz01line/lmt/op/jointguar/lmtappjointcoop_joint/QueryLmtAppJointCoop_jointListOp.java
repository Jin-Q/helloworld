package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;

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

public class QueryLmtAppJointCoop_jointListOp extends CMISOperation {

	private final String modelId = "LmtAppJointCoop";
	
	public String doExecute(Context context) throws EMPException {
		/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
		String returnStr="0";
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			if(conditionStr.toUpperCase().contains("WHERE")){
				conditionStr += " AND coop_type = '010' ";
			}else{
				conditionStr += " WHERE coop_type = '010' ";
			}
			String cusName="";
			String cusMemberName="";
			String cusIdList="";
			IndexedCollection iCollSelect = new IndexedCollection();
			IndexedCollection iCollSelect4GM = new IndexedCollection();
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
			if(queryData !=null && !"".equals(queryData)){
				//modify by jiangcuihua 2019-03-16 queryData中可能不包含cus_name
				if(queryData.containsKey("cus_name")){
					cusName = (String) queryData.getDataValue("cus_name");
				}
				if(cusName !=null && !"".equals(cusName)){
					String conSelect ="SELECT t.cus_id FROM Lmt_App_Joint_Coop t,cus_base cb WHERE t.cus_id=cb.cus_id and cb.cus_name like '%"+cusName+"%'";
					iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
					if(iCollSelect !=null && iCollSelect.size() > 0){
						for(int i=0;i<iCollSelect.size();i++){
							KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
							cusIdList += kColl.getDataValue("cus_id")+",";
						}
						conditionStr += " and instr('"+ cusIdList+"', cus_id)>0 ";
					}else{
						conditionStr +=" and cus_id='' ";
					}
				}
				//modify by jiangcuihua 2019-03-16 queryData中可能不包含cus_member_name
				if(queryData.containsKey("cus_member_name")){
					cusMemberName = (String) queryData.getDataValue("cus_member_name");
				}
				if(cusMemberName !=null && !"".equals(cusMemberName)){
					String conSelect ="SELECT la.cus_id FROM Lmt_App_Joint_Coop la, Lmt_App_Name_List t, cus_base cus "
							   +" WHERE la.serno = t.serno and t.cus_id = cus.cus_id and t.sub_type = '03' "
							   +" and cus.cus_name like '%"+cusMemberName+"%'";
					iCollSelect4GM = TableModelUtil.buildPageData(null, dataSource, conSelect);
					if(iCollSelect4GM !=null && iCollSelect4GM.size() > 0){
						for(int i=0;i<iCollSelect4GM.size();i++){
							KeyedCollection kColl = (KeyedCollection) iCollSelect4GM.get(i);
							cusIdList += kColl.getDataValue("cus_id")+",";
						}
							conditionStr += " and instr('"+ cusIdList+"', cus_id)>0 ";						
					}else{
						conditionStr +=" and cus_id='' ";
					}
				}
			}
			
			if(context.containsKey("type") && "cusHis".equals(context.getDataValue("type"))){   
				if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
					conditionStr +=" and approve_status!='000' and serno in ( SELECT t.serno FROM Lmt_App_Name_List t WHERE t.sub_type='03' and t.cus_id='"+context.getDataValue("cus_id").toString()+"')  order by app_date desc";
				}else{
					throw new EMPException("查询集团（关联）授信审批历史出错，传入客户码[cus_id]不能为空。");
				}
				returnStr ="cusHis";
			}else{
				conditionStr += " ORDER BY SERNO DESC";
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addUSerName(iColl, new String[]{"input_id","manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id","manager_br_id"});
			
			iColl.setName(iColl.getName()+"_jointList");
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
		/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
	}

}
