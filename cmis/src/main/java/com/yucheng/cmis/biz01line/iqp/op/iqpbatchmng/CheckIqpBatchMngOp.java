package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpBatchMngOp extends CMISOperation {
	private final String batModel = "IqpBatchMng"; //批次表模型
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String batchno = (String)context.getDataValue("batch_no");//批次号
			
			//判断是否被业务引用
			KeyedCollection kColl = dao.queryDetail(batModel, batchno, connection);
			String serno = (String) kColl.getDataValue("serno");
			if(serno!=null&&!serno.equals("")){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "已被业务申请引用，申请流水号【"+serno+"】，不能进行修改！");
				return null;
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}
}
