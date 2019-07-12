package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtnamelist_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtNameList_jointListOp extends CMISOperation {

	private final String modelId = "LmtAppNameList";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;

		String serno = "";//申请流水号
		try{
			connection = this.getConnection(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
//			if(context.containsKey("joint_serno")){
//				joint_serno = (String) context.getDataValue("joint_serno");
//			}
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(serno==null||"".equals(serno)){
				throw new Exception("申请编号为空！");
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
//			//入/退圈申请成员
//			if(app_serno!=null&&!"".equals(app_serno)){
//				if(conditionStr.contains("WHERE")){
////					conditionStr += " and serno in (select serno from lmt_app_join_back where biz_area_no in (select biz_area_no from lmt_app_join_back where serno = '"+baSerno+"')) and joint_guar_no is null ";
//					conditionStr += " and app_serno='"+app_serno+"'";
//				}else{
////					conditionStr += " where serno in (select serno from lmt_app_join_back where biz_area_no in (select biz_area_no from lmt_app_join_back where serno = '"+baSerno+"')) and joint_guar_no is null ";
//					conditionStr += " where app_serno='"+app_serno+"'";
//				}
//			}else
			
			if(conditionStr.contains("WHERE")){
				conditionStr += " and serno ='" + serno + "' ";
			}else{
				conditionStr += " where serno ='" + serno + "' ";
			}
			conditionStr += "order by serno desc";
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
			iColl.setName(iColl.getName()+"List");
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

}
