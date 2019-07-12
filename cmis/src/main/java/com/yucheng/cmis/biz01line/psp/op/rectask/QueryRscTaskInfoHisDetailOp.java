package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryRscTaskInfoHisDetailOp extends CMISOperation {
	private final String modelId = "RscTaskInfoHis";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = new KeyedCollection();
			String serno  = null;
			try {
				serno  = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno  == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");
			 			//查询数据
			KeyedCollection  result = dao.queryDetail(modelId, serno, connection);
			 
			KeyedCollection param = dao.queryFirst("", null, "where serno = '"+serno+"'", connection);
			if(param != null){
				result.put("remark",param.getDataValue("remark"));
			}  
			
			
			
			String[] args=new String[] {"cus_id","prd_id"};
			String[] modelIds=new String[]{"CusBase" ,"PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prd_id" };
			String[] fieldName=new String[]{"cus_id_cname" ,"prd_id_cname"};
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(kColl, new String[]{"upd_id","input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"upd_br_id","input_br_id"});
			//将查询结果放入Context中以便前端获取
			this.putDataElement2Context(kColl, context); 
		} catch (EMPException ee) {
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
