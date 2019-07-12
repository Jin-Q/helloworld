package com.yucheng.cmis.biz01line.mort.cargo.mortcargorepl;

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

public class QueryMortCargoReplDetailOp  extends CMISOperation {
	
	private final String modelId = "MortCargoRepl";

	private final String serno_name = "serno";
	
	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String guaranty_no = (String) kColl.getDataValue("guaranty_no");
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			//获取该货物质押下的货物清单（在库或者出库待记账）
			String conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status in('02','05','03') order by cargo_id desc";
			//获取该质押物清单下的未记账的质押物记录
			String conditionStr1 = "where RE_SERNO='"+serno_value+"' and guaranty_no ='"+guaranty_no+"' and op_type='1' order by cargo_id desc";
			IndexedCollection iCollNew = dao.queryList("MortDelivList",null ,conditionStr1,connection);
			IndexedCollection iColl = dao.queryList("MortCargoPledge",null ,conditionStr,connection);
			IndexedCollection iCollPle = new IndexedCollection("MortCargoPledgeList");
			if(iCollNew.size()==0){
				iColl.setName("MortCargoPledgeList");
			}else{
				for(int i=0;i<iCollNew.size();i++){
					KeyedCollection kcNew = (KeyedCollection) iCollNew.get(i);
					String cargo_id_new = (String) kcNew.getDataValue("cargo_id");
					for(int j=0;j<iColl.size();j++){
						KeyedCollection kcC = (KeyedCollection) iColl.get(j);
						String  cargo_id = (String) kcC.getDataValue("cargo_id");
						if(cargo_id_new.equals(cargo_id)){
							kcC.put("deliv_qnt", kcNew.getDataValue("deliv_qnt"));
							kcC.put("deliv_value", kcNew.getDataValue("deliv_value"));
							kcC.put("surplus_qnt", kcNew.getDataValue("surplus_qnt"));
							kcC.put("surplus_value", kcNew.getDataValue("surplus_value"));
							iCollPle.add(kcC);
						}
					}
				}
//				iColl.setName("MortCargoPledgeList");
			}
			
			//登记状态的货物置换清单中的记录
			conditionStr = "where guaranty_no ='"+guaranty_no+"' and serno='"+serno_value+"' order by cargo_id desc";
			IndexedCollection iCollnew = dao.queryList("MortCargoReplList",null,conditionStr,connection);
			iCollnew.setName("MortCargoPledgeNewList");
			
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollPle, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollnew, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iCollPle, context);
			this.putDataElement2Context(iCollnew, context);
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
