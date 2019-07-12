package com.yucheng.cmis.biz01line.cus.trustee.custrusteeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckCusTrusteeInputIdOp extends CMISOperation {

	private final String modelId = "SDutyuser";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = getTableModelDAO(context);
			String inputId = (String) context.getDataValue("input_id");
			IndexedCollection iColl = dao.queryList(modelId, " where dutyno in ('S0200','S0226') and  actorno = '" + inputId + "'", connection);
			if(iColl!=null&&iColl.size()>0){
				context.put("flag", "1");
			}else{
				context.put("flag", "2");
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
