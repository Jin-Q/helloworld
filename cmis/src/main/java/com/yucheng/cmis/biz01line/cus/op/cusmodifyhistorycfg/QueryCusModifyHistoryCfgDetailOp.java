package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusModifyHistoryCfgDetailOp  extends CMISOperation {
	
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
			
			String model_id_value = null;//表模型Id
			try {
				model_id_value = (String)context.getDataValue(model_id_name);
			} catch (Exception e) {}
			if(model_id_value == null || model_id_value.length() == 0){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "The value of pk["+model_id_name+"] cannot be null!");
				throw new EMPJDBCException("The value of pk["+model_id_name+"] cannot be null!");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, model_id_value, connection);
			String tableModel = (String) kColl.getDataValue("model_id");
			String cfgColumn = (String)kColl.getDataValue("cfg_column");
			String cfgColumns[] = cfgColumn.split(",");
			List<String> cfgList = new ArrayList<String>();
			for(int i=0;i<cfgColumns.length;i++){
				cfgList.add(cfgColumns[i]);
			}
			
			IndexedCollection tableIcoll = new IndexedCollection();//所有字段
			IndexedCollection iColFromDic = new IndexedCollection("STD_ZB_TABLEMODEL_COLUMN");//待选字段
			IndexedCollection iColToDic = new IndexedCollection("STD_ZB_CFG_COLUMN");//已选字段
			this.dealModelDic(tableModel, tableIcoll, context);
			
			//将已经配置字段与未配置字段分别放入各自iColl中
			for(int i=0;i<tableIcoll.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)tableIcoll.get(i);
				String enname = (String)kCollTmp.getDataValue("enname");
				if(cfgList.contains(enname)){
					iColToDic.add(kCollTmp);
				}else{
					iColFromDic.add(kCollTmp);
				}
			}
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			this.putDataElement2Context(iColFromDic, context);
			this.putDataElement2Context(iColToDic, context);
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
