package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteMortGuarantyCertiInfoRecordOp extends CMISOperation {

	private final String modelId = "MortGuarantyCertiInfo";
	

	private final String warrant_no_name = "warrant_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String warrant_no_value = null;
			String warrant_type = null;
			try {
				warrant_no_value = (String)context.getDataValue(warrant_no_name);
				warrant_type = (String)context.getDataValue("warrant_type");
			} catch (Exception e) {}
			if(warrant_no_value == null || warrant_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+warrant_no_name+"] cannot be null!");
			
			//权证编号中文传输会乱码，所以使用编码传输，使用前先解码
			warrant_no_value = URLDecoder.decode(warrant_no_value,"UTF-8");
			
			Map<String,String> map = new HashedMap();
			map.put("warrant_no", warrant_no_value);
			map.put("warrant_type", warrant_type);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.deleteAllByPks(modelId, map, connection);
			
			//if(count!=1){
			//	throw new EMPException("Remove Failed! Records :"+count);
			//}
			context.addDataField("flag", "success");
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
