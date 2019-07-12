package com.yucheng.cmis.biz01line.iqp.op.iqpbksyndic.iqpbksyndicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpBksyndicIqpBksyndicInfoOp extends CMISOperation {
	
	private final String modelId = "IqpBksyndicInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    String serno = "";
		    String prtcpt_amt_rate = "";
		    Float rate = null;
			try {
				serno = (String)context.getDataValue("serno");
				prtcpt_amt_rate = (String)context.getDataValue("prtcpt_amt_rate");
				rate = Float.valueOf(prtcpt_amt_rate);
				rate = rate/100;
			} catch (Exception e) {}
			if(serno == null || serno.length()==0)
				throw new EMPJDBCException("The values to serno["+modelId+"] cannot be empty!");
			String condition = "where serno='"+serno+"'";
			
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String amt_rate= (String)kColl.getDataValue("prtcpt_amt_rate");
				Float amt_rate_float = Float.valueOf(amt_rate);
				rate += amt_rate_float; 
			}
			if(rate>1){
				context.addDataField("flag", "error");
			}else{
				context.addDataField("flag", "success");
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
