package com.yucheng.cmis.biz01line.mort.cargo.mortcargorepl;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortCargoReplRecordOp extends CMISOperation {
	
	private final String modelId = "MortCargoRepl";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = "";
			KeyedCollection kColl = null;
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT,context,connection);
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				TableModelDAO dao = this.getTableModelDAO(context);

				if(context.containsKey("flg")){//入库记账时，批量更改货物的状态，改为入库状态。（入库记账时，所需要做的操作）
					if(context.containsKey("serno")){
						serno = (String) context.getDataValue("serno");
						//删除提货清单中置换金额为0的数据
						SqlClient.update("deleteMortDelivListBySerno", serno, null, null, connection);
					}else{
						throw new EMPException("传入业务流水号为空！");
					}
					if("2".equals(context.getDataValue("flg").toString().trim())){
						String guaranty_no = (String) context.getDataValue("guaranty_no");

						//将入库待记账状态的货物批量设置为入库状态
						mortCom.updateStatusBatchCargoRepl("04","02", guaranty_no, serno);
						mortCom.insertmortCargoPledge4ZH("02", serno);  //货物置换入库功能，应将待入库货物直接入库  2014-05-30 唐顺岩
						
						//查出出库待记账状态的货物
						String conStr = "where guaranty_no='"+guaranty_no+"' and cargo_status='05'";
						IndexedCollection ic = dao.queryList("MortCargoPledge",conStr,connection);
						//查要被置换的货物
						String conStr1 = "where guaranty_no='"+guaranty_no+"' and op_type='1' and re_serno='"+serno+"'";
						IndexedCollection icReplList = dao.queryList("MortDelivList",conStr1,connection);
						
						if(ic.size()>0){//若存在被置换出来的货物
							KeyedCollection kc = null;//提货清单Kc
							KeyedCollection mortKc = null;//货物清单
							for(int i=0;i<ic.size();i++){
								kc = (KeyedCollection) ic.get(i);
								for(int j=0;j<icReplList.size();j++){
									mortKc = (KeyedCollection)icReplList.get(j);
									if(kc.getDataValue("cargo_id").equals(mortKc.getDataValue("cargo_id"))){
										kc.setDataValue("qnt",mortKc.getDataValue("surplus_qnt"));
										kc.setDataValue("identy_total",mortKc.getDataValue("surplus_value"));
										Double qnt = Double.valueOf(mortKc.getDataValue("surplus_qnt")+"");
										if(qnt.compareTo(0.00)==0){
											kc.put("cargo_status","03");
											kc.put("exware_date",context.getDataValue("OPENDAY"));
										}
										dao.update(kc, connection);//更新货物清单表
										mortKc.setDataValue("cargo_status", "03");
										dao.update(mortKc, connection);//状态更新为 出库 
									}
								}
							}
						}
						//将出库待记账状态的货物批量设置为入库状态
						mortCom.updateStatusBatch("05","02",guaranty_no,context);
						//设置记账状态为已记账
						kColl.setDataValue("status","01");
						//设置记账日期为当前日期
						kColl.setDataValue("tally_date", context.getDataValue("OPENDAY"));
					}
				}else{
					//新增时设置主键
					if(context.containsKey("serno")){
						
					}else{
						serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						kColl.setDataValue("serno",serno);
					}
				}
				
				int count = 0;
				if(context.containsKey("serno")){
				 count = dao.update(kColl, connection);
				}else{	
				 count = dao.insert(kColl, connection);
			 	//存入货物清单历史数据，查看列表数据时显示。
				//4--置出
//				mortCom.insertMortDelivListByStatus(serno,"4","05",(String) kColl.getDataValue("guaranty_no"));
//				mortCom.insertMortDelivListByStatus(serno,"4","03",(String) kColl.getDataValue("guaranty_no"));
//				//5--置入
//				mortCom.insertMortDelivListByStatus(serno,"5","04",(String) kColl.getDataValue("guaranty_no"));
//				mortCom.insertMortDelivListByStatus(serno,"5","01",(String) kColl.getDataValue("guaranty_no"));
//				//给提货清单表中关联业务流水号设值。
//				mortCom.updateMortDelivListOper(serno,(String)kColl.getDataValue("guaranty_no"),"1");
				}
				if(count==1){
					context.addDataField("flag","success");		
				}else {
					context.addDataField("flag","fail");		
				}
				context.put("serno", serno);
			}catch(Exception e){
				EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "货物置换操作失败！");
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"货物置换操作失败！请重新操作！");
			}if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
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
