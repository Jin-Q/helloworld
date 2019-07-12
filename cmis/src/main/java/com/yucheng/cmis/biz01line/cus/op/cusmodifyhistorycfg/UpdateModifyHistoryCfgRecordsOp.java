package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateModifyHistoryCfgRecordsOp extends CMISOperation {
	
//	private final String modelId = "ModifyHistoryCfg";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = null;
			String model_id = "";
			
			try {
				iColl = (IndexedCollection)context.getDataElement("ModifyHistoryCfgList");
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0)
				throw new EMPJDBCException("The values to update cannot be empty!");
			
			KeyedCollection kColl = (KeyedCollection)iColl.get(0);
			model_id = (String)kColl.getDataValue("model_id");
			
			IndexedCollection iColl4Mod = new IndexedCollection("ModifyHistoryCfgList");
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				String opttype = (String)kCollTmp.getDataValue("opttype");
				opttype = opttype.trim();
				if(opttype!=null&&!"".equals(opttype)){
					//若有配置字典项则放入新iColl中
					iColl4Mod.add(iColl.get(i));
				}
			}
			
			ModifyHistoryComponent mhComponent = (ModifyHistoryComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("ModifyHistory", context,connection);
			if(iColl4Mod.size()>0){
				//若存在要配置的信息则先删除原有记录再插入新记录
				mhComponent.deleteModifyCfgByModelId(model_id);
				mhComponent.insertModifyCfgByModelId(iColl4Mod);
			}else{
				//若一条需要配置的数据都不存在则删除原有配置
				mhComponent.deleteModifyCfgByModelId(model_id);
			}
			
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
