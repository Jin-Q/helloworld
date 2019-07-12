package com.yucheng.cmis.biz01line.iqp.op.iqpfreedompayinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddIqpFreedomPayInfoRecordOp extends CMISOperation {
	
	private final String modelId = "IqpFreedomPayInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = new IndexedCollection();
			if(context.containsKey("IqpFreedomPayInfoList")){
				iColl = (IndexedCollection)context.getDataElement("IqpFreedomPayInfoList");
			}
			int dateno_num = 0;
			if (iColl != null) {
				dateno_num =iColl.size(); 
				dateno_num++;
			}
			
			String dateno_str = "";	//将期号定为001格式
			if(dateno_num < 10){
				dateno_str = "00"+dateno_num;
			}else if(dateno_num >= 10 && dateno_num  < 100){
				dateno_str = "0"+dateno_num;
			}else{
				dateno_str = ""+dateno_num;
			}

			KeyedCollection kColl = new KeyedCollection(modelId);
			kColl.addDataField("serno", context.getDataValue("serno"));
			kColl.addDataField("dateno", dateno_str);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag", PUBConstant.SUCCESS);
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
