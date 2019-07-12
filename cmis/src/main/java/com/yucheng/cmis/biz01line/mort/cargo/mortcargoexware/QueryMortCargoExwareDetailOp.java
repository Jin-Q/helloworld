package com.yucheng.cmis.biz01line.mort.cargo.mortcargoexware;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryMortCargoExwareDetailOp  extends CMISOperation {
	
	private final String modelId = "MortCargoExware";
	
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

			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String guaranty_no = (String)kColl.getDataValue("guaranty_no");//押品编号
			//货物与监管协议关系记录详情
			KeyedCollection kcRe = mortRe.queryCarOverReRecordDetail(guaranty_no);
			String agr_type = (String)kcRe.getDataValue("agr_type");
			kColl.put("agr_type", agr_type);//协议类型，为前台页面展示用，多一次查询数据库md
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			//IndexedCollection iColl = dao.queryList("MortDelivList" ,"where re_serno='"+serno_value+"'",connection);
			//
			
			String conditionStr = "where guaranty_no ='"+guaranty_no+"' order by cargo_id desc";
			IndexedCollection iColl1 = dao.queryList("MortCargoPledge",null ,conditionStr,connection);
			IndexedCollection iColl = dao.queryList("MortDelivList",null ,"where re_serno='"+serno_value+"'",connection);
			if(iColl.size()>0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kcNew = (KeyedCollection) iColl.get(i);
					String cargo_id_new = (String) kcNew.getDataValue("cargo_id");
					for(int j=0;j<iColl1.size();j++){
						KeyedCollection kcC = (KeyedCollection) iColl1.get(j);
						String  cargo_id = (String) kcC.getDataValue("cargo_id");
						if(cargo_id_new.equals(cargo_id)){
							kcNew.put("cargo_name", kcC.getDataValue("cargo_name"));
						}
					}
				}
			}
			iColl.setName("MortDelivListHisList");		
			
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iColl, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iColl, context);
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
