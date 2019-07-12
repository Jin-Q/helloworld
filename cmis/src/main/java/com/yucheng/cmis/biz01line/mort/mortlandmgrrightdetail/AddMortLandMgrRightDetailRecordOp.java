package com.yucheng.cmis.biz01line.mort.mortlandmgrrightdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * @Description:XD150714054 新增土地承包经营权押品类型
 * @author FChengLiang
 * @time:2015-8-19  上午10:08:07
 */
public class AddMortLandMgrRightDetailRecordOp extends CMISOperation {
	
	private final String modelId = "MortLandMgrRightDetail";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String guaranty_no = (String)kColl.getDataValue("guaranty_no");
			IndexedCollection iColl = dao.queryList(modelId, "where guaranty_no='"+guaranty_no+"'", connection);
			if(iColl!=null && iColl.size()>0){
				dao.update(kColl, connection);
				context.addDataField("flag", "success");
			}else{
				dao.insert(kColl, connection);
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
