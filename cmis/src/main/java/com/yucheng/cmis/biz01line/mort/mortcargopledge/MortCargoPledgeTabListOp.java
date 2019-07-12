package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class MortCargoPledgeTabListOp extends CMISOperation {


	private final String modelId = "MortCargoPledge";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String oversee_agr_no="";
		IndexedCollection iColl = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				oversee_agr_no = (String)context.getDataValue("oversee_agr_no");
			} catch (Exception e) {
				throw new Exception("监管质押协议编号异常，请联系后台人员！");
			}
			String conditionStr = "";
			conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if("".equals(conditionStr) || conditionStr == null){
				conditionStr = "where guaranty_no in(select guaranty_no from Iqp_Cargo_Oversee_Re where agr_no='"+oversee_agr_no+"') order by cargo_id desc";
			}else{
				conditionStr += " and guaranty_no in(select guaranty_no from Iqp_Cargo_Oversee_Re where agr_no='"+oversee_agr_no+"') order by cargo_id desc";
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("cargo_id");
			list.add("guaranty_catalog");
			list.add("cargo_name");
			list.add("identy_total");
			list.add("storage_date");
			list.add("exware_date");
			list.add("cargo_status");
			list.add("reg_date");
			
			iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName("MortCargoPledgeList");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iColl, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			
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
