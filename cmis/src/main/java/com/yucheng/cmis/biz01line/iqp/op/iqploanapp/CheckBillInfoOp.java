package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class CheckBillInfoOp extends CMISOperation {

	private final String modelId = "IqpBatchMng";
	private final String incomeModelId = "IqpBillIncome";
	private final String relModelId = "IqpBatchBillRel";
	private final String modelIdIqp = "IqpLoanApp";
	private final String modelIdDisc = "IqpDiscApp";
	private final String modelIdIqpRp = "IqpRpddscnt";
	private final String modelIdIqpBill = "IqpBillDetail";
	private final String modelIdBailInfo = "PubBailInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		BigDecimal total_amt = new BigDecimal(0);
		BigDecimal drft_amt = new BigDecimal(0);
		BigDecimal app_amt = new BigDecimal(0);
		String is_elec_bill = null;
		String prd_id = null;
		String assure_main = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String serno = (String)context.getDataValue("serno");//业务流水号
			//获取业务信息
			KeyedCollection kCollIqp = dao.queryDetail(modelIdIqp, serno, connection);
			if(kCollIqp.containsKey("serno") && kCollIqp.getDataValue("serno") != null && !"".equals(kCollIqp.getDataValue("serno"))){
				app_amt = BigDecimalUtil.replaceNull(kCollIqp.getDataValue("apply_amount"));
				KeyedCollection kCollDiscSub = dao.queryDetail(modelIdDisc, serno, connection);
				is_elec_bill = (String)kCollDiscSub.getDataValue("is_elec_bill");
				prd_id = (String)kCollIqp.getDataValue("prd_id");
				assure_main = (String)kCollIqp.getDataValue("assure_main");
			}else{
				KeyedCollection kCollIqpRp = dao.queryDetail(modelIdIqpRp, serno, connection);
				app_amt = BigDecimalUtil.replaceNull(kCollIqpRp.getDataValue("bill_total_amt"));
			}
			
			//获取批次信息
			String condition = " where serno = '"+serno+"'";
			KeyedCollection kColl = dao.queryFirst(modelId, null, condition, connection);
			String batch_no = (String) kColl.getDataValue("batch_no");
			
			
			//校验批次中票据是否全部录入收益信息
			String relCondition = " where batch_no = '"+batch_no+"'";
			IndexedCollection relIColl = dao.queryList(relModelId, relCondition, connection);
			
			//校验商票贴现业务是否录入保证金信息
			if("300020".equals(prd_id) && !"400".equals(assure_main)){
				IndexedCollection iCollBailInfo = dao.queryList(modelIdBailInfo, condition, connection);
				if(iCollBailInfo.size()==0){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "未录入保证金信息!");
					return null;
				}else if(iCollBailInfo.size()>1){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "保证金信息只能录入一条!");
					return null;
				}
			}
			
			//没有票据
			if(relIColl.size()==0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "未录入票据信息，无法放入流程！");
				return null;
			}
			
			for(int i=0;i<relIColl.size();i++){
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String porder_no = (String) relKColl.getDataValue("porder_no");
				//获取票据明细表，校验票据是否电票和业务申请中是否一致
				if(kCollIqp.containsKey("serno") && kCollIqp.getDataValue("serno") != null && !"".equals(kCollIqp.getDataValue("serno"))){
					KeyedCollection kCollBill = dao.queryDetail(modelIdIqpBill, porder_no, connection);
					String is_ebill = (String)kCollBill.getDataValue("is_ebill");//是否电票
					if(!is_elec_bill.equals(is_ebill)){
						context.addDataField("flag", "failure");
						context.addDataField("msg", "票据号【"+porder_no+"】中【是否电票】与业务申请中不一致，无法放入流程 !");
						return null;
					}
				}
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batch_no);
				incomeMap.put("porder_no",porder_no);
				KeyedCollection inKColl = dao.queryDetail(incomeModelId, incomeMap, connection);
				String porderno = (String) inKColl.getDataValue("porder_no");
				drft_amt = BigDecimalUtil.replaceNull(inKColl.getDataValue("drft_amt"));
				total_amt = total_amt.add(drft_amt);
				if(porderno==null||porderno.equals("")){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "票据号【"+porder_no+"】未录入收益信息，无法放入流程！");
					return null;
				}
			}
			
			
			//获取收益表信息，并校验付息信息是否完成
			String incomeCondition = " where batch_no = '"+batch_no+"'";
			IndexedCollection icomeIColl = dao.queryList(incomeModelId, incomeCondition, connection);
			for(int i=0;i<icomeIColl.size();i++){
				KeyedCollection incomeKColl = (KeyedCollection) icomeIColl.get(i);
				String biz_type = (String) incomeKColl.getDataValue("biz_type");
				if(biz_type.equals("07")){//直贴才需校验付息信息
					String dscnt_int_pay_mode = (String) incomeKColl.getDataValue("dscnt_int_pay_mode");
					String porder_no = (String) incomeKColl.getDataValue("porder_no");
					if(dscnt_int_pay_mode==null||dscnt_int_pay_mode.equals("")){
						context.addDataField("flag", "failure");
						context.addDataField("msg", "票据号【"+porder_no+"】未录入付息信息，无法放入流程！");
						return null;
					}
				}
			}
			//
			if(app_amt.compareTo(total_amt)!=0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "申请金额与票据总金额不相同，无法放入流程！");
				return null;
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}