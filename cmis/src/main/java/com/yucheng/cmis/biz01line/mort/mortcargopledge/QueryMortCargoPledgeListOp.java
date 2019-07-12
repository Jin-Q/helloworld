package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortCargoPledgeListOp extends CMISOperation {


	private final String modelId = "MortCargoPledge";
	private final String modelIdA = "MortCargoReplList";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guaranty_no="";
		IndexedCollection iColl = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false,true,false);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List list = new ArrayList();
			list.add("cargo_id");
			list.add("guaranty_catalog");
			list.add("cargo_name");
			list.add("identy_total");
			list.add("storage_date");
			list.add("exware_date");
			list.add("cargo_status");
			list.add("reg_date");
			if(context.containsKey("guaranty_no")){//根据押品编号获得其相应的货物信息
				guaranty_no = (String) context.getDataValue("guaranty_no");
				if("".equals(conditionStr)){
					conditionStr+="where guaranty_no='"+guaranty_no+"'order by cargo_id desc";
				}else{
					conditionStr+="and guaranty_no='"+guaranty_no+"'order by cargo_id desc";
				}
				
				context.setDataValue("guaranty_no", guaranty_no);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}
			if(context.containsKey("status")){//分页查询时，根据押品编号和货物状态过滤记录
				String status = (String) context.getDataValue("status");
				String guarantyNo = (String) context.getDataValue("guarantyNo");
				if("rk".equals(status)){//登记或者入库待记账状态的货物
					conditionStr = "where guaranty_no ='"+guarantyNo+"' and cargo_status in('01','04') order by cargo_id desc";
					size = 2;
					iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				}
				if("ck".equals(status)){//入库或者出库待记账状态的货物
					conditionStr = "where guaranty_no ='"+guarantyNo+"' and cargo_status in('02','05') order by cargo_id desc";
					size = 2;
					iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				}
				if("zh".equals(status)){//登记状态或者入库待记账的货物
//					conditionStr = "where guaranty_no ='"+guarantyNo+"' and cargo_status in('02','05') order by cargo_id desc";
					size = 2;
					PageInfo pageInfo1 = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
//					iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
					conditionStr = "where guaranty_no ='"+guarantyNo+"' and cargo_status in('01','04') order by cargo_id desc";
					IndexedCollection iCollNew = dao.queryList(modelIdA,list ,conditionStr,pageInfo1,connection);
					iCollNew.setName("MortCargoPledgeNewList");
					SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollNew, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
					this.putDataElement2Context(iCollNew, context);
				}
				
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
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
