package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class SaveBreedInvestigationOp extends CMISOperation {

	/**
	 * 保存养殖情况调查表
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection tableInfo = null;
			IndexedCollection mainInfoList = null;
			IndexedCollection costList = null;
			IndexedCollection predictionList = null;
			
			try {
				tableInfo = (KeyedCollection)context.getDataElement("TableInfo");
				mainInfoList = (IndexedCollection)context.getDataElement("MainInfoList");
				costList = (IndexedCollection)context.getDataElement("CostList");
				predictionList = (IndexedCollection)context.getDataElement("PredictionList");
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection tempKcoll = null;
			
			if(tableInfo != null){
				tableInfo.setName("IqpMeFncBreed");
				dao.update(tableInfo, connection);
			}
			if(mainInfoList != null){
				for(int i=0;i<mainInfoList.size();++i){
					tempKcoll = (KeyedCollection)mainInfoList.get(i);
					tempKcoll.setName("IqpMeFncBreed");
					dao.update(tempKcoll, connection);
				}
			}
			if(costList != null){
				for(int i=0;i<costList.size();++i){
					tempKcoll = (KeyedCollection)costList.get(i);
					tempKcoll.setName("IqpMeFncPlant");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(predictionList != null){
				for(int i=0;i<predictionList.size();++i){
					tempKcoll = (KeyedCollection)predictionList.get(i);
					tempKcoll.setName("IqpMeFncBreed");
					dao.update(tempKcoll, connection);
				}
			}
			context.put("flag", "success");
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存养殖调查表失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存养殖调查表失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}


}
