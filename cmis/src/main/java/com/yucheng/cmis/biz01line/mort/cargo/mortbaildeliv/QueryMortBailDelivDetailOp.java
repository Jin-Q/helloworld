package com.yucheng.cmis.biz01line.mort.cargo.mortbaildeliv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryMortBailDelivDetailOp  extends CMISOperation {
	
	private final String modelId = "MortBailDeliv";
	
	private final String serno_name = "serno";
	
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no ="";
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			    guaranty_no = (String) context.getDataValue("guaranty_no");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			//获取该质押物清单下的未记账的质押物记录
			String conditionStr = "where guaranty_no ='"+guaranty_no+"' and op_type='2' and re_serno='"+serno_value+"' order by cargo_id desc";
			IndexedCollection iCollNew = dao.queryList("MortDelivList",null ,conditionStr,connection);
			
			if(iCollNew.size()>0){
				for(int i=0;i<iCollNew.size();i++){
					KeyedCollection kCollDel = (KeyedCollection)iCollNew.get(i);
					String cargo_id = (String)kCollDel.getDataValue("cargo_id");
					KeyedCollection kCollRepl = dao.queryDetail("MortCargoPledge", cargo_id, connection);
					kCollDel.put("identy_unit_price", kCollRepl.getDataValue("identy_unit_price"));
					kCollDel.put("identy_total", kCollRepl.getDataValue("identy_total"));
					//kCollDel.put("cargo_status", kCollRepl.getDataValue("cargo_status"));
					kCollDel.put("qnt", kCollRepl.getDataValue("qnt"));
				}
			}
			iCollNew.setName("MortCargoPledgeNewList");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollNew, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iCollNew, context);
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
}
