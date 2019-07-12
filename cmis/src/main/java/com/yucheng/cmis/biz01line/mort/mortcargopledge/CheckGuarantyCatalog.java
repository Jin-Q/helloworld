package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckGuarantyCatalog extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoPledge";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			try {
					String guaranty_no =(String) context.getDataValue("guaranty_no");
					String guaranty_catalog =(String) context.getDataValue("guaranty_catalog");
					String condStr = "where cargo_id in (select cargo_id from Mort_Cargo_Pledge where guaranty_no ='"+guaranty_no+"')";
					//遍历该押品编号下存在的所有货物信息
					IndexedCollection ic = dao.queryList(modelId, condStr, connection);
					//不存在货物记录时，可以新增任何押品所属目录下的货物
					if(null==ic){
						context.addDataField("check", "true");
					}else{
						int count = 0;//用来记录是否已存在将要新增的所属押品目录下的记录
						for(int i=0;i<ic.size();i++){
							KeyedCollection kc = (KeyedCollection) ic.get(i);
							if(guaranty_catalog.trim().equals(kc.getDataValue("guaranty_catalog"))){
								context.addDataField("check", "false");
								break;
							}else{
								count++;
							}
						}
						if(count==ic.size()){//遍历结束后不存在
							context.addDataField("check", "true");
						}
					}
					
			} catch (Exception e) {}
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
