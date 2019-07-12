package com.yucheng.cmis.biz01line.cus.op.cusother.cusfixauthorize;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.TimeUtil;
import com.yucheng.cmis.util.MD5;

public class AddCusFixAuthorizeRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusFixAuthorize";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String auth_id = kColl.getDataValue("auth_id").toString();
			KeyedCollection kc = dao.queryFirst(modelId, null, "where auth_id='"+auth_id+"'", connection);
			String enddate = (String ) kColl.getDataValue("enddate");
			String opendate = (String) context.getDataValue("OPENDAY");
			if(kc==null||kc.getDataValue("auth_id")==null){//如果数据不存在
				String manager_br_id = (String)kColl.getDataValue("manager_br_id");
				String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
				kColl.put("serno", serno);
				String checkCode = (String) kColl.getDataValue("checkcode");
				checkCode = MD5.encode(checkCode).toUpperCase();
				kColl.setDataValue("checkcode", checkCode);
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
			}else{//如果存在数据
				if(TimeUtil.checkDate1BeforeDate2(opendate, enddate)){//如果结束日期大于当前日期
					context.addDataField("flag", "failed");
				}else{
					String manager_br_id = (String)kColl.getDataValue("manager_br_id");
					String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
					kColl.put("serno", serno);
					String checkCode = (String) kColl.getDataValue("checkcode");
					checkCode = MD5.encode(checkCode).toUpperCase();
					kColl.setDataValue("checkcode", checkCode);
					dao.insert(kColl, connection);
					context.addDataField("flag", "success");
				}
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
