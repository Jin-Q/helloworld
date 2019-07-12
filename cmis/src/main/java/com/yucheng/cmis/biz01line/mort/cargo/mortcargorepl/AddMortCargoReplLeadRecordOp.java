package com.yucheng.cmis.biz01line.mort.cargo.mortcargorepl;

import java.math.BigDecimal;
import java.sql.Connection;

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

public class AddMortCargoReplLeadRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoRepl";
	
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
			if(!context.containsKey("serno")){
				throw new EMPException("申请流水号为空！");
			}else{
				serno = (String)context.getDataValue("serno");
			}
			
			KeyedCollection kcRepl = dao.queryDetail(modelId, serno, connection);
			
			guaranty_no = (String)kcRepl.getDataValue("guaranty_no");
			cus_id = (String) kcRepl.getDataValue("cus_id");
			
			//货物与监管协议关系记录详情
			kcRe = mortRe.queryCarOverReRecordDetail(guaranty_no);
			//获取库存总金额
			KeyedCollection kc = mortRe.queryIdentyTotalInfo(guaranty_no,"02");
			//获取入库待记账状态的货物总价值
//			KeyedCollection kcA = mortRe.queryIdentyTotalInfo(guaranty_no,"04");
			KeyedCollection kcA = mortRe.queryCargoReplListTotalInfo(serno,"04");
		    //获取出库待记账状态的货物总价值
			KeyedCollection kcB = mortRe.queryIdentyTotalInfo(guaranty_no,"05");
			BigDecimal storage_total = (BigDecimal) kc.getDataValue("identy_total");
			BigDecimal out_total = (BigDecimal) kcB.getDataValue("identy_total");//出库待记账
			BigDecimal add_total = (BigDecimal) kcA.getDataValue("identy_total");//入库待记账
			BigDecimal this_repl_total = new BigDecimal(0);//此次置换总价值（提货清单中相关货物的提货价值总和）
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			
			int size = 2;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			//获取该货物质押下的货物清单（在库或者出库待记账）
			String conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status in('02','05') order by cargo_id desc";
			//通过流水号获取该质押物清单下记录
			String conditionStr1 = "where guaranty_no ='"+guaranty_no+"' and re_serno='"+serno+"' and op_type='1' order by cargo_id desc";
			IndexedCollection iColl = dao.queryList("MortCargoPledge",null ,conditionStr,connection);
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
							BigDecimal deliv_value = new BigDecimal(kcNew.getDataValue("deliv_value").toString());
							BigDecimal temp = this_repl_total;
							this_repl_total = temp.add(deliv_value);//累加提货价值
						}
					}
				}
				iColl.setName("MortCargoPledgeList");
			}
			//此次置换总价值
			kcRepl.put("this_repl_total",this_repl_total);
			//出库待记账价值
			kcRepl.put("not_out_total",this_repl_total);
			BigDecimal sub_total = this_repl_total;//出库待记账
			BigDecimal after_repl_total = (add_total.add(storage_total).subtract(sub_total).add(out_total));
			//库存总价值
			kcRepl.put("storage_total",storage_total.add(out_total));
			//置换后总价值（库存总金额-此次置换总价值+入库待记账状态的货物总价值）
			kcRepl.put("after_repl_total",after_repl_total.toString());
			kcRepl.put("not_to_total",kcA.getDataValue("identy_total").toString());//入库待记账
			kcRepl.put("cus_id",cus_id);
			kcRepl.put("oversee_agr_no",kcRe.getDataValue("agr_no"));
			
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kcRepl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			//登记状态的货物置换清单中的记录
			String conditionRL = "where guaranty_no ='"+guaranty_no+"' and serno='"+serno+"' order by cargo_id desc";
			IndexedCollection iCollnew = dao.queryList("MortCargoReplList",null,conditionRL,connection);
			iCollnew.setName("MortCargoPledgeNewList");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iColl, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollnew, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(iCollnew, context);
			
			this.putDataElement2Context(kcRepl, context);
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
