package com.yucheng.cmis.biz01line.mort.cargo.mortcargostorage;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class ChangeGaurantyStatus extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoStorage";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cargo_id_str = "";//需要进行状态改变的货物编号群（登记时，入库待记账状态。入库记账时，入库状态）
			KeyedCollection kColl = null;
			String flg="";
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("flg")){
				flg = context.getDataValue("flg").toString().trim();
				cargo_id_str = context.getDataValue("cargo_id_str").toString().trim();
				IndexedCollection ic = new IndexedCollection();//用来存储需要更改状态的货物编号
				if(cargo_id_str.length()==1){
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("cargo_id",cargo_id_str);
					ic.add(kc);
				}else{
					String[] cargoArr = cargo_id_str.split(",");
					for(int i=0;i<cargoArr.length;i++){
						KeyedCollection kc = new KeyedCollection();
						kc.addDataField("cargo_id",cargoArr[i]);
						ic.add(kc);
					}
				}
				if("1".equals(flg)){// 04--入库待记账
					String storage_mode = (String) context.getDataValue("storage_mode");
					mortCom.updateStatusBatchCheck("04", ic, connection);
					/*if("00".equals(storage_mode)){//首次
						mortCom.updateStatusBatchCheck("04", ic, connection);
					}else if("01".equals(storage_mode)){//补货
						mortCom.updateStatusBatchCheckRepl("04", ic, connection);
					}*/
					
				}else if("2".equals(flg)){//01--登记
					String storage_mode = (String) context.getDataValue("storage_mode");
					mortCom.updateStatusBatchCheck("01", ic, connection);
					/*if("00".equals(storage_mode)){//首次
						mortCom.updateStatusBatchCheck("01", ic, connection);
					}else if("01".equals(storage_mode)){//补货
						mortCom.updateStatusBatchCheckRepl("01", ic, connection);
					}
					*/
					
				}else if("3".equals(flg)){//03--入库
					mortCom.updateStatusBatchCheck("02", ic, connection);
				}else if("cancle".equals(flg)){//撤销出库
					String serno = "";
					String guaranty_no = "";
					IndexedCollection iColl = null;
					try {
						iColl = (IndexedCollection)context.getDataElement("MortCargoPledgeList");
						guaranty_no = (String) context.getDataValue("guaranty_no");
					} catch (Exception e) {}
					if(iColl.size() == 0){
						throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
					}else{
						 for(int i=0;i<iColl.size();i++){
							KeyedCollection kc = (KeyedCollection) iColl.get(i);
							kc.put("storage_qnt",kc.getDataValue("qnt"));//库存数量
							kc.put("storage_value",kc.getDataValue("identy_total"));//库存总价值
							kc.put("guaranty_no",guaranty_no);//押品编号
							kc.put("input_id", context.getDataValue(PUBConstant.currentUserId));//登记人
							kc.put("input_br_id", context.getDataValue(PUBConstant.organNo));//登记机构
							kc.put("reg_date", context.getDataValue(PUBConstant.OPENDAY));//登记日期
							kc.put("op_type","3");//操作类型--出库
							kc.put("cargo_status", "05");//状态为登记状态
							kc.put("deliv_qnt", 0);
							kc.put("deliv_value", 0);
							kc.setName("MortDelivList");
							if(kc.getDataValue("serno")!=null&&!"".equals(kc.getDataValue("serno"))){
								//dao.update(kc, connection);
								dao.deleteByPk("MortDelivList", (String)kc.getDataValue("serno"), connection);
							}else{
							//	serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
							//	kc.put("serno",serno);//业务编号
							//	dao.insert(kc,connection);
							}
						 }
					}
				}else if("4".equals(flg)){//05--出库待记账
					//mortCom.updateStatusBatchCheck("05", ic, connection);
					
					/*************************************************/
					String serno = "";//业务流水号（提货清单中）
					String guaranty_no = "";//押品编号（提货清单中）
					IndexedCollection iColl = null;//保存多个提货清单
					try {
						iColl = (IndexedCollection)context.getDataElement("MortCargoPledgeList");
						guaranty_no = (String) context.getDataValue("guaranty_no");
					} catch (Exception e) {}
//					mortCom.updateStatusBatchCheck("05", ic, connection);//改为出库待记账
					if(iColl.size() == 0){
						throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
					}else{
						for(int i=0;i<iColl.size();i++){
							KeyedCollection kc = (KeyedCollection) iColl.get(i);
							if(kc.getDataValue("deliv_qnt")!=null&&!"0".equals(kc.getDataValue("deliv_qnt"))){
								kc.put("storage_qnt",kc.getDataValue("qnt"));//库存数量
								kc.put("storage_value",kc.getDataValue("identy_total"));//库存总价值
								kc.put("guaranty_no",guaranty_no);//押品编号
								kc.put("input_id", context.getDataValue(PUBConstant.currentUserId));//登记人
								kc.put("input_br_id", context.getDataValue(PUBConstant.organNo));//登记机构
								kc.put("reg_date", context.getDataValue(PUBConstant.OPENDAY));//登记日期
								kc.put("op_type","3");//操作类型--出库
								kc.put("cargo_status", "05");//状态为出库待记账状态
								kc.setName("MortDelivList");
								if(kc.getDataValue("serno")!=null&&!"".equals(kc.getDataValue("serno"))){
									dao.update(kc, connection);
								}else{
									serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
									kc.put("serno",serno);//业务编号
									dao.insert(kc,connection);
								}
							}
						}
					}
					/*************************************************/
					
					
				}
				context.addDataField("flag","success");		
			}else if(context.containsKey("flgzh")){
				String flgzh = (String) context.getDataValue("flgzh");
				cargo_id_str = context.getDataValue("cargo_id_str").toString().trim();
				String serno = (String)context.getDataValue("serno");
				IndexedCollection ic = new IndexedCollection();//用来存储存在于库中并需要更改状态的货物编号
				if(cargo_id_str.length()==1){
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("cargo_id",cargo_id_str);
					ic.add(kc);
				}else{
					String[] cargoArr = cargo_id_str.split(",");
					for(int i=0;i<cargoArr.length;i++){
						KeyedCollection kc = new KeyedCollection();
						kc.addDataField("cargo_id",cargoArr[i]);
						ic.add(kc);
					}
				}
				String cargo_id_str_new = context.getDataValue("cargo_id_str_new").toString().trim();
				IndexedCollection icNew = new IndexedCollection();//用来存储新增货物的货物编号
				if(cargo_id_str_new.length()==1){
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("cargo_id",cargo_id_str_new);
					icNew.add(kc);
				}else{
					String[] cargoArr = cargo_id_str_new.split(",");
					for(int i=0;i<cargoArr.length;i++){
						KeyedCollection kc = new KeyedCollection();
						kc.addDataField("cargo_id",cargoArr[i]);
						icNew.add(kc);
					}
				}
//				String serno = "";//业务流水号（提货清单中）
				String guaranty_no = "";//押品编号（提货清单中）
				IndexedCollection iColl = null;//保存多个提货清单
				//构建组件类
//				MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
//				.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
				try {
					iColl = (IndexedCollection)context.getDataElement("MortCargoPledgeList");
					guaranty_no = (String) context.getDataValue("guaranty_no");
				} catch (Exception e) {}
				
				if(flgzh.equals("5")){//货物置换时，确认按钮
					mortCom.updateStatusBatchCheckRepl("04", icNew, connection);//置入货物改为入库待记账
					mortCom.updateStatusBatchCheck("05", ic, connection);//改为出库待记账
					if(iColl.size() == 0){
						throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
					}else{
						 for(int i=0;i<iColl.size();i++){
							KeyedCollection kc = (KeyedCollection) iColl.get(i);
							if(kc.getDataValue("deliv_qnt")!=null&&!"0".equals(kc.getDataValue("deliv_qnt"))){
								kc.put("storage_qnt",kc.getDataValue("qnt"));//库存数量
								kc.put("storage_value",kc.getDataValue("identy_total"));//库存总价值
								kc.put("guaranty_no",guaranty_no);//押品编号
								kc.put("input_id", context.getDataValue(PUBConstant.currentUserId));//登记人
								kc.put("input_br_id", context.getDataValue(PUBConstant.organNo));//登记机构
								kc.put("reg_date", context.getDataValue(PUBConstant.OPENDAY));//登记日期
								kc.put("op_type","1");//操作类型
								kc.put("cargo_status", "05");//状态为出库待记账
								kc.setName("MortDelivList");
								kc.put("re_serno", serno);
								//KeyedCollection res = mortCom.queryMortDelivList(guaranty_no, kc.getDataValue("cargo_id").toString().trim(),"1");
								if(kc.getDataValue("serno")!=null&&!"".equals(kc.getDataValue("serno"))){
									dao.update(kc, connection);
								}else{
									String sernoPk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
									kc.put("serno",sernoPk);//业务编号
									dao.insert(kc,connection);
								}
							}
						}
					}
				}else if(flgzh.equals("6")){//货物置换时，撤销按钮
					mortCom.updateStatusBatchCheckRepl("01", icNew, connection);//改为登记
					mortCom.updateStatusBatchCheck("02", ic, connection);//改为入库状态
					if(iColl.size() == 0){
						throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
					}else{
						 for(int i=0;i<iColl.size();i++){
							KeyedCollection kc = (KeyedCollection) iColl.get(i);
							kc.addDataField("storage_qnt",kc.getDataValue("qnt"));//库存数量
							kc.addDataField("storage_value",kc.getDataValue("identy_total"));//库存总价值
							kc.addDataField("guaranty_no",guaranty_no);//押品编号
							kc.addDataField("input_id", context.getDataValue(PUBConstant.currentUserId));//登记人
							kc.addDataField("input_br_id", context.getDataValue(PUBConstant.organNo));//登记机构
							kc.addDataField("reg_date", context.getDataValue(PUBConstant.OPENDAY));//登记日期
							kc.addDataField("op_type","1");//操作类型
							//kc.addDataField("cargo_status", "00");//状态为登记状态
							kc.setDataValue("cargo_status", "00");//状态为登记状态
							kc.setName("MortDelivList");
							KeyedCollection res = mortCom.queryMortDelivList(guaranty_no, kc.getDataValue("cargo_id").toString().trim(),"1");
							if("true".equals(res.getDataValue("result"))){//true--新增
							//	serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
							//	kc.addDataField("serno",serno);//业务编号
								
							}else{//false--修改
							//	kc.addDataField("serno",res.getDataValue("serno"));
							//	dao.update(kc, connection);
								serno = (String) res.getDataValue("serno");
								dao.deleteByPk("MortDelivList",serno,connection);
							}
						 }
					}
				}
				context.addDataField("flag","success");		
			}
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
