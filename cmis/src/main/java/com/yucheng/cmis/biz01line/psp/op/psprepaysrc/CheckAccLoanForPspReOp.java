package com.yucheng.cmis.biz01line.psp.op.psprepaysrc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckAccLoanForPspReOp extends CMISOperation {
	private final String modelId = "PspRepaySrc";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String cus_id = (String) context.getDataValue("cus_id");
			String bill_no = (String) context.getDataValue("bill_no");
			if(cus_id==null){
				throw new EMPException("获取不到客户编号！");
			}
			if(bill_no==null){
				throw new EMPException("获取不到借据编号！");
			}
			String condition = " where cus_id='"+cus_id+"' and bill_no = '"+bill_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			
			if(iColl.size()>0){
				context.addDataField("flag", "failed");
				return "0";
			}
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
