package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetCusModifyHistoryCfgAddOp  extends CMISOperation {
	
	private final String modelId = "CusModifyHistoryCfg";
	
	private final String model_id_name = "model_id";
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if("".equals(kColl.getDataValue(model_id_name)) || kColl.getDataValue(model_id_name) == ""){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "The value of pk["+model_id_name+"] cannot be null!");
				throw new EMPJDBCException("The value of pk["+model_id_name+"] cannot be null!");
			}

			String tableModel = (String) kColl.getDataValue("model_id");//表模型Id
			IndexedCollection iColFromDic = new IndexedCollection("STD_ZB_TABLEMODEL_COLUMN");
			this.dealModelDic(tableModel, iColFromDic, context);
			this.putDataElement2Context(iColFromDic, context);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			this.putDataElement2Context(kColl, context);
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
	
	
	/**
	 * 把表模型的字段id和 中文名作为字典 放入iCol
	 * @param modelId  模型id
	 * @param iColDic 
	 * @param isRelModel  是否是 从表模型
	 * @param context
	 */
	public void dealModelDic(String modelId,IndexedCollection iColDic,Context context) throws Exception{
		TableModelLoader loader = (TableModelLoader)context.getService("tableModelLoader");
		TableModel tableModel = loader.getTableModel(modelId);
		if(tableModel == null ){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "模型"+modelId+"未配置");
			throw new EMPJDBCException("模型"+modelId+"未配置");
		}
	
		Iterator<?> iterator = tableModel.getModelFields().keySet().iterator();
		//获取单表
		while (iterator.hasNext()) {
			String fieldId = (String) iterator.next();
			TableModelField field = tableModel.getModelField(fieldId);
			KeyedCollection kTmp = new KeyedCollection();
			
			kTmp.addDataField("enname", fieldId);
		
			kTmp.addDataField("cnname",field.getCnname());
			
			iColDic.addDataElement(kTmp);
		}
	}
	
}
