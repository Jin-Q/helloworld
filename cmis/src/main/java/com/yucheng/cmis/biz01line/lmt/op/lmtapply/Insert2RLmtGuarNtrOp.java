package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class Insert2RLmtGuarNtrOp extends CMISOperation {
	
    private final String modelId = "RLmtGuarntrInfo";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection =this.getConnection(context);
			String limit_code = (String)context.getDataValue("limit_code");
			String serno = context.getDataValue("serno").toString();
			String guar_id = context.getDataValue("guar_id").toString();
			KeyedCollection kColl = new KeyedCollection();
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = "where limit_code='"+limit_code+"' and guar_id='"+guar_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			if(iColl.size()>0){
				context.addDataField("flag", "fail");
				KeyedCollection kColl_msg = (KeyedCollection)iColl.get(0);
				context.addDataField("msg", "保证人已与授信["+kColl_msg.getDataValue("serno")+"]建立关联关系，请重新选择！");
			}else{
				kColl.addDataField("limit_code", limit_code);
				kColl.addDataField("serno", serno);
				kColl.addDataField("guar_id", guar_id);
				kColl.addDataField("app_date", context.getDataValue("OPENDAY"));
				kColl.setName("RLmtGuarntrInfo");
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("msg", "保证人与授信关联关系建立成功！");
			}			
			
		}catch (EMPException ee) {
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
