package com.yucheng.cmis.biz01line.mort.cargo.mortcargoexware;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortCargoExwareRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoExware";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			String serno = "";
			String cargo_id_str = "";//需要进行状态改变的货物编号群（登记时，出库待记账状态。出库记账时，出库状态）
			String flg ="";//标志位，2--出库记账
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				if(context.containsKey("serno")){
					serno = (String) context.getDataValue("serno");
					//设置更新操作时主键
					kColl.setDataValue("serno",serno);
					Map<String, String> map = new HashMap<String, String>();
					map.put("serno", serno);
					//mortCom.deleteByField("MortCargoReplList",map);
				}else{
					serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					//设置主键
					kColl.setDataValue("serno",serno);
				}
				
			} catch (Exception e) {}

			//给提货清单表中关联业务流水号设值。
			mortCom.updateMortDelivListOper(serno,(String)kColl.getDataValue("guaranty_no"),"3");
			
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//存入货物清单历史数据，查看列表数据时显示。
			//mortCom.insertMortDelivListByStatus(serno,"3","02",(String) context.getDataValue("guaranty_no"));
			//mortCom.insertMortDelivListByStatus(serno,"3","05",(String) context.getDataValue("guaranty_no"));
			if(context.containsKey("flg")){//出库记账时，批量更改货物的状态，改为出库状态。（出库记账时，所需要做的操作）
				flg = context.getDataValue("flg").toString().trim();
				if("2".equals(flg)){
					String guaranty_no = (String) context.getDataValue("guaranty_no");
					
					/******************************************************************/
					//判断剩余价值是否大于担保金额
					IndexedCollection icValue = dao.queryList("MortGuarantyEvalValue", "where guaranty_no = '"+guaranty_no+"'", connection);
					if(icValue.size()>0){
						KeyedCollection kCollValue = (KeyedCollection) icValue.get(0);
						String guar_amt = (String)kCollValue.getDataValue("guar_amt");//担保金额
						//押品价值为空时，直接赋值为0  2014-05-30 唐顺岩
						if(null == guar_amt || "null".equalsIgnoreCase(guar_amt)){
							guar_amt = "0";
						}
						String exware_total = (String)kColl.getDataValue("exware_total");//出库后总价值
						if(Double.parseDouble(exware_total)<Double.parseDouble(guar_amt)){//出库后价值小于担保金额，则不允许出库
							context.addDataField("flag","little");
							return "0";
						}
					}
					
					//查出出库待记账状态的货物
					String conStr = "where guaranty_no='"+guaranty_no+"' ";
					IndexedCollection ic = dao.queryList("MortCargoPledge",conStr,connection);
					//查要被置换的货物
					String conStr1 = "where guaranty_no='"+guaranty_no+"' and op_type='3' and re_serno='"+serno+"' ";
					IndexedCollection icReplList = dao.queryList("MortDelivList",conStr1,connection);
					
					if(ic.size()>0){//若存在被置换出来的货物
						KeyedCollection kc = null;//提货清单Kc
						KeyedCollection mortKc = null;//货物清单
						for(int i=0;i<ic.size();i++){
							kc = (KeyedCollection) ic.get(i);
							for(int j=0;j<icReplList.size();j++){
								mortKc = (KeyedCollection)icReplList.get(j);
								if(kc.getDataValue("cargo_id").equals(mortKc.getDataValue("cargo_id"))){
									kc.put("qnt",mortKc.getDataValue("surplus_qnt"));
									kc.put("identy_total",mortKc.getDataValue("surplus_value"));
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
					//mortCom.updateStatusBatch("05","02",guaranty_no,context);
					//设置记账状态为已记账
					kColl.setDataValue("status","01");
					//设置记账日期为当前日期
					kColl.setDataValue("tally_date", context.getDataValue("OPENDAY"));
					/*******************************************************************/
					
				}
			}
			
			int count = 0;
			try{
				if(context.containsKey("serno")){
				 count = dao.update(kColl, connection);
				}else{	
				 count = dao.insert(kColl, connection);
				}
			}catch(Exception e){
				EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "出库操作失败！");
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"出库操作失败！请重新操作！");
			}
			if(count==1){
				context.addDataField("flag","success");		
			}else {
				context.addDataField("flag","fail");		
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
