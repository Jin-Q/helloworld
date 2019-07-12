package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpBatchMngOp extends CMISOperation {
	private final String modelId = "IqpBatchMng";
	/**
	 * 处理业务逻辑：
	 * 1.通过当前票据列表中的票据信息测算票据总金额，以及票据数量
	 * 2.将票据计算的数量以及总金额
	 * 3.计算出票据中的所有票据的利息总金额
	 * 4.将上述得到的信息插入批次表中，更新批次信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String batchno = null;
			String num = "";
			String total = "";
			String type = "";
			String serno = "";
			if(context.containsKey("type")){
				type = (String)context.getDataValue("type");
			}
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			try {
				batchno = (String)context.getDataValue("batchno");
				num = (String)context.getDataValue("num");
				total = (String)context.getDataValue("total");
			} catch (Exception e) {}
			/** 封装需要更新批次表中的记录 */
			KeyedCollection baKColl = new KeyedCollection("IqpBatchMng");
			baKColl.addDataField("bill_qnt", "0");//票据数量
			baKColl.addDataField("bill_total_amt", "0.00");//票据总金额
			baKColl.addDataField("rpay_amt", "0");//实付金额
			if(type.equals("TX")){
				baKColl.addDataField("serno", serno);
				baKColl.addDataField("rpddscnt_int", "0.00");
			}else {
				baKColl.addDataField("batch_no", batchno);
				baKColl.addDataField("int_amt", "0.00");//票据利息
			}
			
			
			if(batchno == null || batchno.length() == 0){
				context.addDataField("flag", "success");
			}else {
				/** 通过批次号获得票据中的相关信息 */
				TableModelDAO dao = this.getTableModelDAO(context);
				/** 通过批次号，查询关联表中存在的关联记录 */
				IndexedCollection reIColl = dao.queryList("IqpBatchBillRel", " where batch_no='"+batchno+"'", connection);
				if(reIColl != null && reIColl.size() > 0){
					/** 封装需要查询的票据信息SQL */
					String porderSQLHelp = " where porder_no in (";
					for(int i=0;i<reIColl.size();i++){
						KeyedCollection kc = (KeyedCollection)reIColl.get(i);
						String porderno = (String)kc.getDataValue("porder_no");
						porderSQLHelp = porderSQLHelp+"'"+porderno+"',";
					}
					porderSQLHelp = porderSQLHelp.substring(0, porderSQLHelp.length()-1)+") ";

					/** 计算票据总金额 */
					double billAmt = 0;
					IndexedCollection biIColl = dao.queryList("IqpBillDetail", porderSQLHelp, connection);
					if(biIColl != null && biIColl.size() > 0){
						int billNum = biIColl.size();
						for(int i=0;i<biIColl.size();i++){
							KeyedCollection kc = (KeyedCollection)biIColl.get(i);
							String amt = (String)kc.getDataValue("drft_amt");
							billAmt += Double.parseDouble(amt);
						}
						baKColl.setDataValue("bill_qnt", billNum);//票据数量
						baKColl.setDataValue("bill_total_amt", billAmt);//票据总金额
					}
					
					/** 计算票据利息 */
					double intAmt = 0;
					String incomestr = porderSQLHelp + " and batch_no = '"+batchno+"'";
					IndexedCollection inIColl = dao.queryList("IqpBillIncome", incomestr, connection);
					if(inIColl != null && inIColl.size() > 0){
						for(int i=0;i<inIColl.size();i++){
							KeyedCollection kc = (KeyedCollection)inIColl.get(i);
							String amt = (String)kc.getDataValue("int");
							intAmt += Double.parseDouble(amt);
						}
						if(type.equals("TX")){
							baKColl.setDataValue("rpddscnt_int", intAmt);
						}else {
							baKColl.setDataValue("int_amt", intAmt);//票据利息
						}
					}
					baKColl.setDataValue("rpay_amt", intAmt+billAmt);
				}else {
					/** 批次关联表中不存在关联记录，则默认赋值为0 */
				}
				if(type.equals("TX")){
					baKColl.setName("IqpRpddscnt");
				}else {
					baKColl.setName(modelId);
				}
				int count = dao.update(baKColl, connection);
				context.addDataField("flag", "success");
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
