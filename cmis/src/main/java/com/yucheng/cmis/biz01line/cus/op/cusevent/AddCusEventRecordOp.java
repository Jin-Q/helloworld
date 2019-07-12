package com.yucheng.cmis.biz01line.cus.op.cusevent;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusEventRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusEvent";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String serno =CMISSequenceService4JXXD.querySequenceFromDB("CUS", "fromDate", connection, context);
			kColl.put("serno", serno);
			kColl.setDataValue("status", "0");
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			/*
			 * 调用业务提醒接口
			 */
		//	Remin4OutInterface remin4OutInterface = (Remin4OutInterface) CMISComponentFactory
         //   .getComponentFactoryInstance().getComponentInterface(PUBConstant.REMINDAGENT, context,connection);
//			String yyrq = (String)context.getDataValue("OPENDAY");
		//	remin4OutInterface.remindUsers("0400", (String)kColl.getDataValue("serno"), yyrq);
			flag = "新增成功";
		}catch (EMPException ee) {
			flag = "新增失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
