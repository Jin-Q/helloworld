package com.yucheng.cmis.biz01line.cus.cusindiv.ops.indivbusiness;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCusIndivBusinessRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusIndivBusiness";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String flag = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//校验 营业执照号码usIndivBusiness.biz_lice_id  是否重复
			String bizliceid = (String)kColl.getDataValue("biz_lice_id");
			String cusid = (String)kColl.getDataValue("cus_id");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			List<String> l = new ArrayList<String>();
			l.add(new String("biz_lice_id"));
			String conditions = " WHERE biz_lice_id='" + bizliceid + "' and cus_id = '" + cusid + "'";
			KeyedCollection kCollq = dao.queryFirst(modelId, l, conditions, connection);
			if(kCollq.getDataValue("biz_lice_id")!=null){
				flag = "已存在";
				context.addDataField("flag", flag);
				context.addDataField("cusid", cusid);
				return "exist";
			}else{
				flag = "未存在";
				context.addDataField("flag", flag);
				context.addDataField("cusid", cusid);
			}
			//未重复 插入数据
			dao.insert(kColl, connection);
			
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
