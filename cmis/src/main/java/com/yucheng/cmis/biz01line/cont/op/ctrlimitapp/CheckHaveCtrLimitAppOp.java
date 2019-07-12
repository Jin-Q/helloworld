package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CheckHaveCtrLimitAppOp extends CMISOperation {
	private final String modelId = "CtrLimitApp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || "".equals(cus_id))
				throw new EMPJDBCException("The values to cus_id cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/** 查询是否存在在途的变更申请信息，存在不允许新增 */
			KeyedCollection queryKColl = (KeyedCollection)dao.queryFirst(modelId, null, " where cus_id = '"+cus_id+"' and app_type='02' and approve_status not in('990','997','998')", connection);
			String cus_id_sel = (String)queryKColl.getDataValue("cus_id");
			if(cus_id_sel != null && !"".equals(cus_id_sel)){
				SInfoUtils.addSOrgName(queryKColl, new String[] { "input_br_id" });
				SInfoUtils.addUSerName(queryKColl, new String[] { "input_id" });
				String inputName = (String)queryKColl.getDataValue("input_id_displayname");
				String inputBrName = (String)queryKColl.getDataValue("input_br_id_displayname");
				String message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的变更申请业务，不能重复发起。";
				context.addDataField("flag", "failed");
				context.addDataField("message", message);
			}else {
				context.addDataField("flag", "success");
				context.addDataField("message", "");
			}
			
			
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			context.addDataField("serno", "");
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
