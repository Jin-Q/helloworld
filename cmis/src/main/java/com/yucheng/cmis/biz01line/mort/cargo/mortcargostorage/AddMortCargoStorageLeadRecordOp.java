package com.yucheng.cmis.biz01line.mort.cargo.mortcargostorage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class AddMortCargoStorageLeadRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoStorage";
	//private final String modelIdRe = "IqpCargoOverseeRe";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		//押品编号
		String guaranty_no = "";
		//出质人客户码
		String cus_id = "";
		//存储货物与监管协议关系信息的kc
		KeyedCollection kcRe = null;
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		try{
			connection = this.getConnection(context);
			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			guaranty_no = (String) context.getDataValue("guaranty_no");
			cus_id = (String) context.getDataValue("cus_id");
			//货物与监管协议关系记录详情
			kcRe = mortRe.queryCarOverReRecordDetail(guaranty_no);
			if(!context.containsKey("serno")){//从货物管理菜单做入库操作
				KeyedCollection kc = mortRe.queryRegiRecord(guaranty_no);
				if(kc!=null&&kc.size()>0){
					context.put("serno", kc.getDataValue("serno"));
				}
			}else{
				KeyedCollection kcMortCargoStorage = dao.queryDetail(modelId,(String) context.getDataValue("serno"), connection);
				String memo = (String) kcMortCargoStorage.getDataValue("memo");
				String storage_mode = (String) kcMortCargoStorage.getDataValue("storage_mode");
				kcRe.put("memo", memo);
				kcRe.put("storage_mode", storage_mode);
				kcRe.put("need_reple_total", kcMortCargoStorage.getDataValue("need_reple_total"));
				kcRe.put("act_reple_total", kcMortCargoStorage.getDataValue("act_reple_total"));
			}
			//获取库存总金额
			KeyedCollection kc = mortRe.queryIdentyTotalInfo(guaranty_no,"02");
			//获取入库待记账状态的货物总价值
			KeyedCollection kcA = mortRe.queryIdentyTotalInfo(guaranty_no,"04");
			BigDecimal storage_total = (BigDecimal) kc.getDataValue("identy_total");
			BigDecimal add_total = (BigDecimal) kcA.getDataValue("identy_total");
			BigDecimal after_total = add_total.add(storage_total) ;
			/*
			//获取入库待记账状态的货物总价值
			KeyedCollection kcC = mortRe.queryIdentyTotalInfoRepl(guaranty_no,"04","2");//从货物置换清单中获取
			BigDecimal add_total_two = (BigDecimal) kcC.getDataValue("identy_total");
			if(context.containsKey("guaranty_info_status")){
				String guaranty_info_status = (String) context.getDataValue("guaranty_info_status");
			    if("3".equals(guaranty_info_status)){//押品生效--补货入库
			    	after_total= after_total.add(add_total_two) ;
				}
			}
			*/
			kcRe.put("storage_total", kc.getDataValue("identy_total"));//库存总价值
			kcRe.put("after_storage_total",after_total.toString());//入库后总价值
			kcRe.put("cus_id",cus_id);
			kcRe.put("oversee_agr_no",kcRe.getDataValue("agr_no"));
			kcRe.put("input_id",context.getDataValue("currentUserId"));
			kcRe.put("input_br_id",context.getDataValue("organNo"));
			kcRe.remove("agr_no");
//			kcRe.remove("agr_type");//协议类型不删掉前台用
			kcRe.setName(modelId);
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kcRe, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			int size = 2;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			List<String> list = new ArrayList<String>();
			list.add("cargo_id");
			list.add("guaranty_catalog");
			list.add("cargo_name");
			list.add("identy_total");
			list.add("storage_date");
			list.add("exware_date");
			list.add("cargo_status");
			list.add("reg_date");
			/*
			KeyedCollection guarKc = dao.queryDetail("MortGuarantyBaseInfo", guaranty_no, connection);
			String guaranty_info_status = (String) guarKc.getDataValue("guaranty_info_status");
			if("2".equals(guaranty_info_status)){//登记完成--首次入库
				
			}else if("3".equals(guaranty_info_status)){//押品生效--补货入库
				String conditionStr = "where guaranty_no = '"+guaranty_no+"' and oper ='2' and cargo_status in('01','04') order by cargo_id desc";
				iColl = dao.queryList("MortCargoReplList",list ,conditionStr,pageInfo,connection);
			}
			*/
			/**根据押品编号去押品出库表里查询，若查到数据则不是首次出库 add by tangzf 2014.04.09  START**/
			String condTmp = " where guaranty_no='"+guaranty_no+"'";
			IndexedCollection iCollTmp = dao.queryList("MortCargoStorage", condTmp, connection);
			if(iCollTmp!=null && iCollTmp.size()>0){
				kcRe.put("storage_mode", "01");//补货入库
			}else {
				kcRe.put("storage_mode", "00");//首次入库
			}
			/**----------------------END-----------------------------------------------------------**/
			String conditionStr = "where guaranty_no = '"+guaranty_no+"' and cargo_status in('01','04') order by cargo_id desc";
			IndexedCollection iColl = dao.queryList("MortCargoPledge",list ,conditionStr,pageInfo,connection);
			iColl.setName("MortCargoPledgeList");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iColl, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(kcRe, context);
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
