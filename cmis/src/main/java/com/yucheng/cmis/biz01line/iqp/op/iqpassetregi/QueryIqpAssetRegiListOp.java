package com.yucheng.cmis.biz01line.iqp.op.iqpassetregi;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAssetRegiListOp extends CMISOperation {


	private final String modelId = "IqpAssetRegi";
	private final String modelIdAcc = "AccView";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
									
			
			String conditionSelect = "";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionSelect = recordRestrict.judgeQueryRestrict(this.modelId, conditionSelect, context, connection);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			if(conditionStr!=null&&!"".equals(conditionStr)){
				conditionStr = conditionStr + " and bill_no in (select bill_no from acc_view " +conditionSelect + " )";
			}else{
				conditionStr = " where bill_no in (select bill_no from acc_view " +conditionSelect + " )";
			}
			
			conditionStr=conditionStr+"order by serno desc";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String bill_no = (String)kColl.getDataValue("bill_no");
				String condition = "where bill_no='"+bill_no+"'";
				KeyedCollection kCollApp = (KeyedCollection)dao.queryFirst(modelIdAcc, null, condition, connection);
				kColl.put("cus_id", kCollApp.getDataValue("cus_id")+"");
			}
			
			
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    
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
