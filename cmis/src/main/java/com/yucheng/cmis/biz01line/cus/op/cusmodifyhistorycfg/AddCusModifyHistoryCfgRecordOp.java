package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;
import java.util.Set;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCusModifyHistoryCfgRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusModifyHistoryCfg";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String model_id = null;//表模型id
			String cfg_column = null;//需要存储历史的列
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			model_id = (String)kColl.getDataValue("model_id");
			cfg_column = (String)kColl.getDataValue("cfg_column");
			IndexedCollection iColl = new IndexedCollection("ModifyHistoryCfgList");
			
			TableModelLoader loader = (TableModelLoader)context.getService("tableModelLoader");
			TableModel tableModel = loader.getTableModel(model_id);
			if(tableModel == null )
				throw new EMPJDBCException("模型"+modelId+"未配置");
		
			Set set = tableModel.getModelFields().keySet();
			String[] cfg_columns = cfg_column.split(",");
			for(int i=0;i<cfg_columns.length;i++){
				if(set.contains(cfg_columns[i])){
					TableModelField field = tableModel.getModelField(cfg_columns[i]);
					KeyedCollection kTmp = new KeyedCollection();
					kTmp.addDataField("model_id", model_id);
					kTmp.addDataField("column_name", cfg_columns[i]);
					kTmp.addDataField("column_name_displayname", field.getCnname());
					iColl.add(kTmp);
				}
			}
			//生成字典项
			IndexedCollection dicIcoll = new IndexedCollection("STD_ZB_POPORDIC");
			KeyedCollection kTmp = new KeyedCollection();
			kTmp.addDataField("enname", "01");
			kTmp.addDataField("cnname","下拉框");
			dicIcoll.addDataElement(kTmp);
			KeyedCollection kTmp1 = new KeyedCollection();
			kTmp1.addDataField("enname", "02");
			kTmp1.addDataField("cnname","弹出框");
			dicIcoll.addDataElement(kTmp1);
			this.putDataElement2Context(dicIcoll, context);
			this.putDataElement2Context(iColl, context);
			
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
