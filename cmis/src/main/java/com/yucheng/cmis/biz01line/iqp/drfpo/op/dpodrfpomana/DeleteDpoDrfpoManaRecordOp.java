package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteDpoDrfpoManaRecordOp extends CMISOperation {

	private final String modelId = "IqpDrfpoMana";
	

	private final String drfpo_no_name = "drfpo_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String drfpo_no_value = null;
			try {
				drfpo_no_value = (String)context.getDataValue(drfpo_no_name);
			} catch (Exception e) {}
			if(drfpo_no_value == null || drfpo_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+drfpo_no_name+"] cannot be null!");
				
			/** 级联删除票据池与票据关联表中的记录*/
			DpoDrfpoComponent cmisComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			int kcResult = cmisComponent.deleteDrfpoByDrfpoNo(drfpo_no_value);
			if(kcResult<0){
				throw new EMPException("Remove Failed! Records :"+kcResult);
			}
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
