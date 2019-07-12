package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoretask;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class QueryQntValue4TaskIdOp extends CMISOperation {
	

	private final String modelId = "IqpChkStoreGageRecord";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String task_id = (String)context.getDataValue("task_id");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = "where task_id='"+task_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			if(iColl == null || iColl.size() == 0){
				context.addDataField("flag", PUBConstant.SUCCESS);
				context.addDataField("qnt", "0");
				context.addDataField("value", "0.00");
			}else{
				BigDecimal value_temp = new BigDecimal(0);
				double qnt_temp = 0;
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection) iColl.get(i);
					BigDecimal value = BigDecimalUtil.replaceNull(kColl.getDataValue("value"));
					String qnt = (String)kColl.getDataValue("qnt");
					double qnt_value = Double.parseDouble(qnt);
					value_temp = value_temp.add(value);
					qnt_temp += qnt_value;
				}
				context.addDataField("flag", PUBConstant.FAIL);
				context.addDataField("qnt", qnt_temp);
				context.addDataField("value", value_temp);
			}
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
