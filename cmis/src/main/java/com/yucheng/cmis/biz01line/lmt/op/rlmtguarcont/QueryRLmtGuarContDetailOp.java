package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryRLmtGuarContDetailOp  extends CMISOperation {
	
	private final String modelId = "RLmtGuarCont";
	private final String modelIdApp = "RLmtAppGuarCont";
	
//	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String lmtCode = "";//额度编号
		String contNo = "";//担保合同编号
		String serno = "";//业务编号
		String returnFlag = "";
		try{
			connection = this.getConnection(context);
		
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
		
			try {
				lmtCode = (String)context.getDataValue("lmtCode");
			} catch (Exception e) {}
			if(lmtCode == null || lmtCode.length() == 0)
				throw new EMPJDBCException("The value of pk["+lmtCode+"] cannot be null!");
			
			try {
				contNo = (String)context.getDataValue("contNo");
			} catch (Exception e) {}
			if(contNo == null || contNo.length() == 0)
				throw new EMPJDBCException("The value of pk["+contNo+"] cannot be null!");
			
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			
			KeyedCollection kColl = null;
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("limit_code", lmtCode);
			pkMap.put("guar_cont_no", contNo);
			if(serno==null||"".equals(serno)){
				kColl = dao.queryDetail(modelId, pkMap, connection);
				returnFlag = modelId;
			}else{
				pkMap.put("serno", serno);
				kColl = dao.queryDetail(modelIdApp, pkMap, connection);
				returnFlag = modelIdApp;
			}
			
			this.putDataElement2Context(kColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnFlag;
	}
}
