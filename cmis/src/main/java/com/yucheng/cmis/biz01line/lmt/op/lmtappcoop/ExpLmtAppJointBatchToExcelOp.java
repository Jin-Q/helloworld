package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class ExpLmtAppJointBatchToExcelOp  extends CMISOperation{
	private final String modelId = "LmtAppJointCoop";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			if(conditionStr.indexOf("where")!=-1){
				conditionStr =conditionStr +" and coop_type = '010' "; 
			}else{
				conditionStr =conditionStr +" where coop_type = '010' "; 
			}
			conditionStr +=" order by serno desc";

			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,connection);
			iColl.setName("LmtAppJointList");
			String[] args=new String[] { "cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			String[] resultName = new String[] { "cus_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});  
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
