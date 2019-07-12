package com.yucheng.cmis.biz01line.mort.cargo.mortcargoexware;

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

public class AddMortCargoExwareLeadRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoExware";
	
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
		String serno = "";
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		try{
			connection = this.getConnection(context);
			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			guaranty_no = (String) context.getDataValue("guaranty_no");
			//货物与监管协议关系记录详情
			kcRe = mortRe.queryCarOverReRecordDetail(guaranty_no);
			if(!context.containsKey("serno")){//从货物管理菜单做出库操作
				KeyedCollection kc = mortRe.queryRegiOutStorRecord(guaranty_no);
				if(null==kc||kc.size()==0){
					
				}else{
					context.addDataField("serno", kc.getDataValue("serno"));	
				}
				
			}else{
				KeyedCollection kcMortCargoStorage = dao.queryDetail(modelId,(String) context.getDataValue("serno"), connection);
				String memo = (String) kcMortCargoStorage.getDataValue("memo");
				kcRe.addDataField("memo", memo);
			}
			cus_id = (String) context.getDataValue("cus_id");
			
			//获取库存总金额
			KeyedCollection kc = mortRe.queryIdentyTotalInfo(guaranty_no,"02");
			//获取出库待记账状态的货物总价值
			KeyedCollection kcA = mortRe.queryIdentyTotalInfo(guaranty_no,"05");
			BigDecimal add_total = (BigDecimal) kcA.getDataValue("identy_total");
			BigDecimal identy_total = (BigDecimal) kc.getDataValue("identy_total");
			kcRe.addDataField("storage_total",identy_total.add(add_total));//库存总价值
			kcRe.addDataField("cus_id",cus_id);
			kcRe.addDataField("oversee_agr_no",kcRe.getDataValue("agr_no"));
			kcRe.remove("agr_no");
			kcRe.setName(modelId);
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kcRe, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			//获取该货物质押下的货物清单
//			String conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status in ('02','05')order by cargo_id desc";
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
			list.add("qnt");
			list.add("identy_unit_price");
			
			
			/*****************************************************************/
			BigDecimal this_exware_total = new BigDecimal(0);//此次出库总价值（出库清单中相关货物的提货价值总和）
			String conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status in('02','05') order by cargo_id desc";
			String conditionStr1 = "where guaranty_no ='"+guaranty_no+"' and cargo_status <> '03' and op_type='3' order by cargo_id desc";//出库操作
			IndexedCollection iColl = dao.queryList("MortCargoPledge",list ,conditionStr,connection);
			IndexedCollection iCollNew = dao.queryList("MortDelivList",null ,conditionStr1,connection);
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
							kcC.put("serno", kcNew.getDataValue("serno"));
							kcC.put("cargo_status", kcNew.getDataValue("cargo_status"));
							BigDecimal deliv_value = new BigDecimal(kcNew.getDataValue("deliv_value").toString());
							this_exware_total = this_exware_total.add(deliv_value);//累加提货价值
						}
					}
				}
				iColl.setName("MortCargoPledgeList");
			}
			
			kcRe.addDataField("this_exware_total",this_exware_total);//此次出库总价值
			BigDecimal after_repl_total = (identy_total.add(add_total).subtract(this_exware_total));//出库后总价值
			kcRe.addDataField("exware_total",after_repl_total);
			/*****************************************************************/
			
			
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
