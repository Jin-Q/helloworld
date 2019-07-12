package com.yucheng.cmis.biz01line.mort.mortguarantyevalvalue;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortGuarantyEvalValueRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortGuarantyEvalValue";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			KeyedCollection mortGuarantyBaseInfo = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				String guaranty_no = (String) kColl.getDataValue("guaranty_no");
				mortGuarantyBaseInfo =(KeyedCollection) context.getDataElement("MortGuarantyBaseInfo");
				mortGuarantyBaseInfo.addDataField("guaranty_no", guaranty_no);
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			//价值评估信息已存在，做更新操作
			if(!"".equals(kColl.getDataValue("eval_id"))){
				dao.update(kColl, connection);
			}else{
				//价值评估信息不存在，在新增操作
				String eval_id = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
				kColl.setDataValue("eval_id", eval_id);
				dao.insert(kColl, connection);
			}
			//更新押品基本信息里面的相关字段
			mortGuarantyBaseInfo.addDataField("guaranty_info_status","2");//押品状态
			dao.update(mortGuarantyBaseInfo, connection);
			context.addDataField("flag","success");
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
