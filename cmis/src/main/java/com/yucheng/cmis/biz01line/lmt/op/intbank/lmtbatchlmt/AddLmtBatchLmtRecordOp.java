package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtBatchLmtRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtBatchLmt";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//系统自动生成业务编号
			String serno_value = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			String batch_cus_no = (String)kColl.getDataValue("batch_cus_no");
			TableModelDAO dao = this.getTableModelDAO(context);

			//更改批量包的状态为已授信
			LmtIntbankComponent lmtIntbankComponent = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
			lmtIntbankComponent.updateStatus(batch_cus_no);
			kColl.setDataValue("serno", serno_value);
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			context.addDataField("serno", serno_value);//用于页面跳转	
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
