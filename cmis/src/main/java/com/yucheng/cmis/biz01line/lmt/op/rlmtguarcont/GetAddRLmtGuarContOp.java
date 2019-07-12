package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class GetAddRLmtGuarContOp  extends CMISOperation {
	
	private final String modelId = "GrtGuarCont";
	private final String modelIdR = "RLmtGuarCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guar_cont_no="";//担保合同编号
		String cus_id = "";//借款人客户码
		try{
			connection = this.getConnection(context); 
			KeyedCollection kCollR = new KeyedCollection(modelIdR);
			try {
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
			} catch (Exception e) {}
			if(guar_cont_no == null || guar_cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk[guar_cont_no] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, guar_cont_no, connection);
			cus_id = (String)kColl.getDataValue("cus_id");
			if(cus_id==null||"".equals(cus_id)){
				throw new EMPJDBCException("合同编号为["+guar_cont_no+"]的担保合同借款人为空！");
			}
			kCollR.put("cus_id", cus_id);
			kCollR.put("guar_cont_no", guar_cont_no);//担保合同编号
			kCollR.put("guar_amt", kColl.getDataValue("guar_amt"));//本次担保金额
			kCollR.put("guar_cont_type", kColl.getDataValue("guar_cont_type"));//担保合同类型
//			context.put("cus_id", cus_id);
			this.putDataElement2Context(kCollR, context);
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
