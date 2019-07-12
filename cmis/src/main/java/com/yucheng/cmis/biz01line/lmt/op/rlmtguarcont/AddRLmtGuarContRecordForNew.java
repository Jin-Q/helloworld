package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class AddRLmtGuarContRecordForNew extends CMISOperation {
	
	private final String modelId = "RLmtGuarCont";
	
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
			String condition  = "where limit_code='"+kColl.getDataValue("limit_code")+"' and guar_cont_no = '"+kColl.getDataValue("guar_cont_no")+"'"; 
			IndexedCollection iColl = (IndexedCollection)dao.queryList(modelId,condition, connection);
			if(iColl!=null && iColl.size()>0){//该担保关系已经存在，不能再次新增
				context.put("flag", PUBConstant.EXISTS);
				return "0";
			}
			kColl.put("type", "Y");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			service.addRLmtAppGuarCont(kColl, (String)kColl.getDataValue("guar_cont_type"), context, connection);
			context.put("flag", PUBConstant.SUCCESS);
//			dao.insert(kColl, connection);
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
