package com.yucheng.cmis.biz01line.mort.cargo.mortbaildeliv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddMortBailDelivLeadRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortBailDeliv";
	
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
			KeyedCollection bailKc =null;
			//从保证金提货菜单做修改或者提货记账操作
			if(context.containsKey("serno")){
				serno = context.getDataValue("serno").toString();
				bailKc = dao.queryAllDetail(modelId, serno, connection);
				kcRe.put("serno",bailKc.getDataValue("serno"));
				kcRe.put("repay_receipt_type",bailKc.getDataValue("repay_receipt_type"));
				kcRe.put("receipt_serno",bailKc.getDataValue("receipt_serno"));
				kcRe.put("repay_amt",bailKc.getDataValue("repay_amt"));
				KeyedCollection kcMortCargoStorage = dao.queryDetail(modelId,(String) context.getDataValue("serno"), connection);
				String memo = (String) kcMortCargoStorage.getDataValue("memo");
				kcRe.put("memo", memo);
			}
			cus_id = (String) context.getDataValue("cus_id");
			//获取库存总金额
			KeyedCollection kc = mortRe.queryIdentyTotalInfo(guaranty_no,"02");

			//库存总价值
			kcRe.put("storage_total",kc.getDataValue("identy_total"));
			//此次置换总价值
//			kcRe.put("this_repl_total", kcA.getDataValue("identy_total"));
			//置换后总价值（库存总金额-出库待记账状态的货物总价值+入库待记账状态的货物总价值）
//			kcRe.put("after_repl_total",after_repl_total.toString());
//			kcRe.put("not_to_total",kcA.getDataValue("identy_total").toString());//入库待记账
//			kcRe.put("not_out_total",kcB.getDataValue("identy_total").toString());//出库待记账
			kcRe.put("cus_id",cus_id);
			kcRe.put("oversee_agr_no",kcRe.getDataValue("agr_no"));
			kcRe.remove("agr_no");
//			kcRe.remove("agr_type");
			
			kcRe.setName(modelId);
			
			/**根据押品编号查询抵质押率**/
			KeyedCollection kCollEval = dao.queryFirst("MortGuarantyEvalValue", null, "where guaranty_no = '"+guaranty_no+"'", connection);
			String pldimn_rate = (String)kCollEval.getDataValue("pldimn_rate");
			kcRe.put("pldimn_rate", pldimn_rate);//设置抵质押率
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kcRe, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			IndexedCollection iCollNew = null;
			IndexedCollection iColl = null;
			String conditionStr = null;
			String conditionStr1 = null;
			//保证金提货时     2014-05-29 唐顺岩 
			if(context.containsKey("op") && "bail_lad".equals(context.getDataValue("op"))){
				conditionStr = "where guaranty_no ='"+guaranty_no+"' and op_type='2' and cargo_status <>'03' ";
			}else if(serno==null || "".equals(serno)){	//货物管理菜单进入，流水号为空值
				conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status ='00' and op_type='2' ";
			}else{	//保证金提货菜单进入
				//获取该质押物清单下的未记账的质押物记录
				conditionStr = "where guaranty_no ='"+guaranty_no+"' and re_serno='"+serno+"' and op_type='2' ";
			}
			iCollNew = dao.queryList("MortDelivList", null,conditionStr,connection);
			if(iCollNew.size()>0){	//提货清单中有数据直接取值
				conditionStr1 = "where guaranty_no ='"+guaranty_no+"' and cargo_status='02' order by cargo_id desc";
				iCollNew = dao.queryList("MortCargoPledge",null ,conditionStr1,connection);
				for(int j=0;j<iCollNew.size();j++){
					KeyedCollection kcC = (KeyedCollection) iCollNew.get(j);
					String cargo_id1 = (String)kcC.getDataValue("cargo_id");
					iColl = dao.queryList("MortDelivList", null,conditionStr+" and cargo_id = '"+cargo_id1+"' order by cargo_id desc ",connection);
					if(iColl!=null&&iColl.size()>0){
						KeyedCollection kCollDel = (KeyedCollection)iColl.get(0);
						kcC.put("deliv_qnt",kCollDel.getDataValue("deliv_qnt"));
						kcC.put("deliv_value",kCollDel.getDataValue("deliv_value"));
						kcC.put("surplus_qnt",kCollDel.getDataValue("surplus_qnt"));
						kcC.put("surplus_value",kCollDel.getDataValue("surplus_value"));
						kcC.put("cargo_status",kCollDel.getDataValue("cargo_status"));
						kcC.put("serno",kCollDel.getDataValue("serno"));
					}else{
						kcC.put("deliv_qnt","0");
						kcC.put("deliv_value","0");
						kcC.put("surplus_qnt","0");
						kcC.put("surplus_value","0");
					}
					
				}
//				for(int i=0;i<iCollNew.size();i++){
//					KeyedCollection kCollDel = (KeyedCollection)iCollNew.get(i);
//					String cargo_id = (String)kCollDel.getDataValue("cargo_id");
//					KeyedCollection kCollRepl = dao.queryDetail("MortCargoPledge", cargo_id, connection);
//					kCollDel.put("identy_unit_price", kCollRepl.getDataValue("identy_unit_price"));
//					kCollDel.put("identy_total", kCollRepl.getDataValue("identy_total"));
//					//保证金提货时，状态不取押品状态     2014-05-29 唐顺岩 
//					if(!(context.containsKey("op") && "bail_lad".equals(context.getDataValue("op")))){
//						//kCollDel.put("cargo_status", kCollRepl.getDataValue("cargo_status"));
//					}
//					kCollDel.put("qnt", kCollRepl.getDataValue("qnt"));
//				}
			}else{	//提货清单中没有数据时查询‘入库’状态的数据
				conditionStr = "where guaranty_no ='"+guaranty_no+"' and cargo_status='02' order by cargo_id desc";
				iCollNew = dao.queryList("MortCargoPledge",null ,conditionStr,connection);
				for(int j=0;j<iCollNew.size();j++){
					KeyedCollection kcC = (KeyedCollection) iCollNew.get(j);
					kcC.put("deliv_qnt","0");
					kcC.put("deliv_value","0");
					kcC.put("surplus_qnt","0");
					kcC.put("surplus_value","0");
				}
			}
			
			iCollNew.setName("MortCargoPledgeNewList");
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iCollNew, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			this.putDataElement2Context(iCollNew, context);
			this.putDataElement2Context(kcRe, context);
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
