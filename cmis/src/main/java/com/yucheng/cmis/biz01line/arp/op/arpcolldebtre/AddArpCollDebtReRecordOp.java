package com.yucheng.cmis.biz01line.arp.op.arpcolldebtre;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddArpCollDebtReRecordOp extends CMISOperation {
	
	private final String modelId = "ArpCollDebtRe";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;

		try{
			connection = this.getConnection(context);
			
			IndexedCollection iColl = null;//更新多个抵债物关联记录
			IndexedCollection iCollNew = null;//更新多个抵债物关联记录

			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				if(context.containsKey("MortGuarantyBaseInfoList")){
					iCollNew = (IndexedCollection)context.getDataElement("MortGuarantyBaseInfoList");
				}
				if(context.containsKey("ArpCollDebtReList")){
					iColl = (IndexedCollection)context.getDataElement("ArpCollDebtReList");
				}
			} catch (Exception e) {}
			if(iColl == null&iCollNew == null){
				context.addDataField("flag","fail");
			}else{
				if(iColl != null){
					 for(int i=0;i<iColl.size();i++){
							KeyedCollection kc = (KeyedCollection) iColl.get(i);
							kc.setName(modelId);
							dao.update(kc, connection);
						 }
				}
				
				if(iCollNew != null){
					 for(int i=0;i<iCollNew.size();i++){
							KeyedCollection kc = (KeyedCollection) iCollNew.get(i);
							kc.setName(modelId);
							dao.update(kc, connection);
						 }
				}
				 context.addDataField("flag","success");
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
