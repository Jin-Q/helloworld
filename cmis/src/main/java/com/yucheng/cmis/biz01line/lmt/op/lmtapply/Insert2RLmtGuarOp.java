package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class Insert2RLmtGuarOp extends CMISOperation {
	
    private final String modelId = "RLmtGuarInfo";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection =this.getConnection(context);
			String limit_code = (String)context.getDataValue("limit_code");
			String serno = context.getDataValue("serno").toString();
			String guaranty_no = context.getDataValue("guaranty_no").toString();
			KeyedCollection kColl = new KeyedCollection();
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = "where serno='"+serno+"' and guaranty_no='"+guaranty_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			if(iColl.size()>0){
				context.addDataField("flag", "fail");
				KeyedCollection kColl_msg = (KeyedCollection)iColl.get(0);
				context.addDataField("msg", "担保品已与授信["+kColl_msg.getDataValue("serno")+"]建立关联关系，请重新选择！");
			}else{								
				kColl.addDataField("limit_code", limit_code);
				kColl.addDataField("serno", serno);
				kColl.addDataField("guaranty_no", guaranty_no);
				kColl.addDataField("app_date", context.getDataValue("OPENDAY").toString());
				kColl.setName("RLmtGuarInfo");
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("msg", "担保品与授信关联关系建立成功！");
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
