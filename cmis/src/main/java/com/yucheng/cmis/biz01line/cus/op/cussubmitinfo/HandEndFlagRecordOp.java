package com.yucheng.cmis.biz01line.cus.op.cussubmitinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;

public class HandEndFlagRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSubmitInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);

			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(serno==null||"".equals(serno)){
				throw new Exception("[serno]字段值为空！");
			}
			
			//查询记录并置为办结
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			kColl.setDataValue("end_flag", "0");
			kColl.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
			dao.update(kColl, connection);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
