package com.yucheng.cmis.biz01line.cus.op.cusother.cusfixauthorize;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class Update4StatusOp extends CMISOperation {


	private final String modelId = "CusFixAuthorize";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			String conditionStr = null;
			String status = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				status = (String)context.getDataValue("status");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);//获得当前选中的 对象
			String auth_id = (String)kColl.getDataValue("auth_id");//授权人id
			String opendate = (String) context.getDataValue("OPENDAY");//系统当前日期
			if("1".equals(status)){
				kColl.put("status", "0");
				dao.update(kColl, connection);
				context.addDataField("flag", "success");
			}else{
				conditionStr = " where status ='1' and enddate >='"+ opendate+"' and auth_id ='"+auth_id+"'";//0--无效 1--生效  2--未完成
				IndexedCollection ic = dao.queryList(modelId, conditionStr, connection);//获得所有对象 
				if(ic==null||ic.size()==0){//如果不存在
					IndexedCollection icTemp = dao.queryList(modelId, "where auth_id='"+auth_id+"'", connection);
					for(int i=0; i<icTemp.size(); i++){
						KeyedCollection kc = (KeyedCollection)icTemp.get(i);
						if(serno_value.equals(kc.getDataValue("serno"))){
							kc.setDataValue("status", "1"); //置为生效
						}else{
							kc.setDataValue("status", "0");
						}
						dao.update(kc, connection);  //更新
					}
					context.addDataField("flag", "success");
				}else{
					context.addDataField("flag", "failed");
				}
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
