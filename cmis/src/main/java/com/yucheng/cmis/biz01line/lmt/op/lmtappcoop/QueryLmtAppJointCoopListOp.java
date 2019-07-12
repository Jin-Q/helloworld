package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

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
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppJointCoopListOp extends CMISOperation {

	private final String modelId = "LmtAppJointCoop";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);

			
			if(!"".equals(conditionStr)){   //过滤联保小组数据
				conditionStr += " AND COOP_TYPE<>'010' ";
			}else{
				conditionStr = " WHERE COOP_TYPE<>'010' ";
			}
			
			if(context.containsKey("type") && "app".equals(context.getDataValue("type"))){   //申请
				returnStr ="app";
			}else{   //历史
				returnStr ="his";
			}
			
			conditionStr += "order by serno desc";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("coop_type");
			list.add("cur_type");
			list.add("lmt_totl_amt");
			list.add("single_max_amt");
			list.add("input_date");
			list.add("input_id");
			list.add("app_type");
			list.add("input_br_id");
			list.add("approve_status");
			list.add("app_date");
			list.add("froze_amt");
			list.add("unfroze_amt");
			list.add("manager_id");
			list.add("manager_br_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id" });
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
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
