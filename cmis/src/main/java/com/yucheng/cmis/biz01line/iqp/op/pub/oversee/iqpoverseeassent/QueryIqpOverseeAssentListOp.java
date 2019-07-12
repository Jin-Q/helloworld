package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseeassent;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpOverseeAssentListOp extends CMISOperation {


	private final String modelId = "IqpOverseeAssent";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			String serno = context.getDataValue("serno").toString();
			if(context.containsKey("type")&&"main".equals(context.getDataValue("type")))
			{
				conditionStr = conditionStr+"where assent_type='"+"01'"+"and serno='"+serno+"'"+"order by serno desc";
				returnStr = "app";
			}else
			{
				conditionStr = conditionStr+"where assent_type='"+"02'"+"and serno='"+serno+"'"+"order by serno desc";
				returnStr = "ret";
			}
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
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
		return returnStr;
	}

}
