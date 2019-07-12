package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddArpLawLawsuitDetailRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "ArpLawLawsuitDetail";
	
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
			
			String[] args=new String[] { "serno" };
			String[] modelIds=new String[]{"ArpLawLawsuitApp"};
			String[] modelForeign=new String[]{"serno"};
			String[] fieldName=new String[]{"manager_br_id"};
			String[] resultName=new String[]{"manager_br_id"};
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName ,resultName);		
			String manager_br_id =  kColl.getDataValue("manager_br_id").toString();				
			String pk_serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			kColl.setDataValue("pk_serno", pk_serno);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			/*** 重新合计诉讼费用 ***/
			UpdateArpLawLawsuitDetailRecordOp cmisOp =new UpdateArpLawLawsuitDetailRecordOp();
			cmisOp.setLawsuitFee(kColl, connection, context);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
