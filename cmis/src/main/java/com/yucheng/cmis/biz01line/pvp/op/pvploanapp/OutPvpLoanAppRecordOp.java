package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class OutPvpLoanAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PvpLoanApp";
	private final String contModelId = "CtrLoanCont";
	private final String contSubModelId = "CtrLoanContSub";
	private final String modelPvpLimit = "PvpLimitSet";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = null;
			Double totPvpAmt = 0.00;
			try {
				iColl = (IndexedCollection)context.getDataElement("CtrLoanContList"); 
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
			                                .getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IndexedCollection value_icoll = new IndexedCollection();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection pvpKColl = new KeyedCollection();
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				//获取判断勾选项
				String checkBox =(String)kColl.getDataValue("box");
				if(checkBox.equals("on")){
					String cont_no = (String)kColl.getDataValue("cont_no");
					String pvp_amt = (String)kColl.getDataValue("pvp_amt");
					totPvpAmt += Double.valueOf(pvp_amt);
					
					/**生成可签订合同列表 */
					KeyedCollection value_kcoll = new KeyedCollection();
					value_kcoll.addDataField("serno",cont_no+"_"+i);
					value_kcoll.addDataField("cont_no",cont_no);
					value_kcoll.addDataField("input_date", context.getDataValue("OPENDAY"));
					value_icoll.add(value_kcoll);
					/**END*/
					/** 出账队列放到合同签订之前，合同生成时直接得出合同评分，故放款按钮点击放款后不需要生成出账记录    2014-10-09 唐顺岩  **/
//					//通过合同编号查询合同详细信息
//					KeyedCollection contKColl = dao.queryAllDetail(contModelId, cont_no, connection);
//					//通过合同编号查询合同子表详细信息
//					KeyedCollection contSubKColl = dao.queryAllDetail(contSubModelId, cont_no, connection);
//					
//					pvpKColl.addDataField("cont_no", (String)contKColl.getDataValue("cont_no"));
//					pvpKColl.addDataField("prd_id", (String)contKColl.getDataValue("prd_id"));
//					pvpKColl.addDataField("cus_id", (String)contKColl.getDataValue("cus_id"));
//					pvpKColl.addDataField("cur_type", (String)contKColl.getDataValue("cont_cur_type"));
//					pvpKColl.addDataField("input_id", (String)contKColl.getDataValue("input_id"));
//					pvpKColl.addDataField("manager_br_id", (String)contKColl.getDataValue("manager_br_id"));
//					pvpKColl.addDataField("input_br_id", (String)contKColl.getDataValue("input_br_id"));
//					//pvpKColl.addDataField("input_date", (String)contKColl.getDataValue("input_date"));
//					pvpKColl.addDataField("input_date", context.getDataValue(CMISConstance.OPENDAY));
//					pvpKColl.addDataField("approve_status", "000");//审批状态
//					if(contSubKColl.containsKey("is_delay")&&contSubKColl.getDataValue("is_delay")!=null&&!"".equals(contSubKColl.getDataValue("is_delay"))){
//						pvpKColl.put("is_delay", contSubKColl.getDataValue("is_delay").toString());
//					}
//					pvpKColl.setName("PvpLoanApp");
//					
//					/*if(prd_id.equals("300021")||prd_id.equals("300020")){//贴现业务类型，由于生产的是票据流水台账，不保存借据号
//						pvpKColl.addDataField("bill_no", "");
//					}else{
//						*//** 获取借据编号，只做测试使用 *//*
//						String billNo = cmisComponent.getBillNoByContNo(cont_no);
//						pvpKColl.addDataField("bill_no", billNo);
//					}*/
//					String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
//					String cont_amt=(String) contKColl.getDataValue("cont_amt");
//					//String cont_balance=(String) contKColl.getDataValue("cont_balance");
//					//Double balance = 0.00;
//					//计算合同余额
//					//balance = Double.valueOf(cont_balance)-Double.valueOf(pvp_amt);
//					//往合同中更新合同余额
//					//contKColl.setDataValue("cont_balance", balance);
//					//往出账中插入金额数据
//					pvpKColl.addDataField("cont_amt", cont_amt);
//					pvpKColl.addDataField("pvp_amt", pvp_amt);
//					//pvpKColl.addDataField("cont_balance", balance);
//					pvpKColl.addDataField("serno", serno);
//					dao.insert(pvpKColl, connection);
//					//dao.update(contKColl, connection);
					/** END **/
				}
			}
			
			SqlClient.executeBatch("insertPvpGroupInputInfo", null, value_icoll, null, connection);
			
			//修改放款额度控制表中(当日已放额度)
			String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
			KeyedCollection kc = dao.queryFirst(modelPvpLimit,null, " where open_day = '"+openDay+"'", connection);
			String sernoHelp = (String)kc.getDataValue("serno");
			if(sernoHelp != null && sernoHelp.length() > 0){
				String out_limit_amt = (String)kc.getDataValue("out_limit_amt");
				kc.setDataValue("out_limit_amt", Double.valueOf(out_limit_amt)+totPvpAmt);
				dao.update(kc, connection);
			}
			
			context.addDataField("flag", "success");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
