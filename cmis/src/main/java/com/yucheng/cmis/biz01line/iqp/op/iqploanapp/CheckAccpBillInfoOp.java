package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckAccpBillInfoOp extends CMISOperation {

	private final String modelId = "IqpAccAccp";
	private final String detModelId = "IqpAccpDetail";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String serno = (String)context.getDataValue("serno");//业务流水号
			
			//获取银承从表信息
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			
			
			//获取银承票据明细表信息
			String condition = " where serno = '"+serno+"'";
			IndexedCollection detIColl = dao.queryList(detModelId, condition, connection);
			
			//校验从表信息
			if(kColl.getDataValue("serno")==null){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "未录入银行承兑汇票信息，无法放入流程！");
				return null;
			}
			
			//校验明细信息
			if(detIColl.size()==0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "未录入银行承兑汇票明细信息，无法放入流程！");
				return null;
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}