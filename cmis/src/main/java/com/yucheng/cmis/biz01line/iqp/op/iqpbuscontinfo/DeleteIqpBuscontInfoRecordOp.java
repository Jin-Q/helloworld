package com.yucheng.cmis.biz01line.iqp.op.iqpbuscontinfo;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpBuscontInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpBuscontInfo";
	

	private final String tcont_no_name = "tcont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String tcont_no_value = null;
			String po_no = null;
			try {
				tcont_no_value = (String)context.getDataValue(tcont_no_name);
				po_no = (String)context.getDataValue("po_no");
			} catch (Exception e) {}
			
			if(tcont_no_value == null || tcont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+tcont_no_name+"] cannot be null!");
			
			//中文转码
			tcont_no_value = URLDecoder.decode(tcont_no_value,"UTF-8");
			po_no = URLDecoder.decode(po_no,"UTF-8");
			Map<String,String> pkMap = new HashedMap();
			pkMap.put("po_no",po_no);
			pkMap.put("tcont_no",tcont_no_value);
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			
			if(count!=1){
				context.addDataField("flag", "fail");
				throw new EMPException("Remove Failed! Records :"+count);
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
