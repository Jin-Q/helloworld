package com.yucheng.cmis.biz01line.cus.op.cusblklist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusBlkStatusOp extends CMISOperation {
	
	private final String modelId = "CusBlkListTemp";
	private final String modelId1 = "CusBlkList";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno = "";
			String status = "";//状态
			String cus_id = "";
			KeyedCollection kColl = null;
			try {
				serno = (String)context.getDataValue("serno");
				status = (String)context.getDataValue("status");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno))
				throw new EMPJDBCException("共享客户业务编号[serno]不能为空!");
			
			if(status == null || "".equals(status))
				throw new EMPJDBCException("共享客户状态 [status]不能为空!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl = dao.queryDetail(modelId, serno, connection);
			//证件类型和证件号码
			String cert_code = (String) kColl.getDataValue("cert_code");
			String cert_type = (String) kColl.getDataValue("cert_type");
			//根据证件类型和证件号码去客户基表中查询记录是否存在。
			IndexedCollection ic = dao.queryList("CusBase","where cert_code='"+cert_code+"' and cert_type='"+cert_type+"'",connection);
			if(ic.size()==0){//不存在于本系统中时，提示其是否去开户。
				context.addDataField("flag","exist");
				return "0";
			}else{
				KeyedCollection kc = (KeyedCollection) ic.get(0);
				cus_id = (String) kc.getDataValue("cus_id");
			}
			kColl.setDataValue("black_date", context.getDataValue("OPENDAY"));//设置列入日期
			kColl.setDataValue("status", status);//设置状态
			kColl.setDataValue("cus_id", cus_id);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			//生效时将数据插入到结果表中.
			kColl.setName(modelId1);
			dao.insert(kColl, connection);
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
