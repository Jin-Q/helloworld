package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cont.component.ContComponent;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.op.trade.Trade0200200001001;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.pvp.component.PvpBizFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateCtrLoanContRecordOp extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				kColl.setDataValue("cont_status", 200);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			
			/**获取评估分数，返回显示   start*/
			String prd_id = (String) kColl.getDataValue("prd_id");
			/** 合同评分在合同生成时即生成。原因：出账队列放合同签订之前处理，出账队列通过后在生成待签订合同  2014-10-09 唐顺岩    
			String cont_no = (String) kColl.getDataValue("cont_no");
			KeyedCollection subKColl = (KeyedCollection) kColl.getDataElement("CtrLoanContSub");
			if(subKColl!=null){
				String is_close_loan = (String) subKColl.getDataValue("is_close_loan");
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				if(!"200024".equals(prd_id)&&!"500032".equals(prd_id)&&!"400020".equals(prd_id)&&!"400024".equals(prd_id)&&!"400023".equals(prd_id)&&!"400022".equals(prd_id)
						&&!"700021".equals(prd_id)&&!"700020".equals(prd_id)&&!"400021".equals(prd_id)&&!"100063".equals(prd_id)&&!"300020".equals(prd_id)
						&&!"300021".equals(prd_id)&&!"300023".equals(prd_id)&&!"600020".equals(prd_id)&&!"300024".equals(prd_id)&&!"300022".equals(prd_id)&&"2".equals(is_close_loan)){
					BigDecimal cont_number = iqpLoanAppComponent.getContNumberByRuleSet(cont_no);
					String cont_number_str = (String) kColl.getDataValue("cont_number");
					if(cont_number_str==null||cont_number_str.equals("")){
						kColl.setDataValue("cont_number", cont_number);
					}
				}
			}
			  END **/
			/**获取评估分数，返回显示   end*/
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}else{				
				context.addDataField("flag", "success");
			}
			
			/** added by yangzy 2015/07/09 需求：XD150407026， 更新存量外币业务的汇率及敞口 start **/
			String serno = (String) kColl.getDataValue("serno");
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//业务汇率
			BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金汇率
			BigDecimal risk_open_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("risk_open_rate"));//风险敞口比例
			BigDecimal risk_open_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("risk_open_amt"));//风险敞口金额
			KeyedCollection kColl4App = new KeyedCollection("IqpLoanApp");
			kColl4App.put("serno", serno);
			kColl4App.put("exchange_rate", exchange_rate);
			kColl4App.put("security_exchange_rate", security_exchange_rate);
			kColl4App.put("risk_open_rate", risk_open_rate);
			kColl4App.put("risk_open_amt", risk_open_amt);
			dao.update(kColl4App, connection);
			/** added by yangzy 2015/07/09 需求：XD150407026， 更新存量外币业务的汇率及敞口 end **/
			
			//保证金状态改为执行中
			ContComponent contComponent = (ContComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("ContComponent", context, connection);
			contComponent.updatePubBailInfo(kColl.getDataValue("cont_no").toString(), context, connection, dao);
			
			//如果是信托贷款业务，贷款意向直接生成台账
			PvpBizFlowComponent PvpBizFlowComponent = (PvpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("PvpBizFlowComponent", context, connection);
			String is_trust_loan = (String)kColl.getDataValue("is_trust_loan");//是否信托贷款
			if(("1".equals(is_trust_loan) && !"300020".equals(prd_id) && !"300021".equals(prd_id)) || "400024".equals(prd_id)){
				PvpBizFlowComponent.insetAccLoan4Ctr(kColl, dao);
				kColl.put("cont_balance", 0.00);
				dao.update(kColl, connection);
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//				KeyedCollection retKColl = new KeyedCollection();
//				KeyedCollection kColl4trade = new KeyedCollection();
//				kColl4trade.put("CLIENT_NO", (String)kColl.getDataValue("cus_id"));
//				kColl4trade.put("BUSS_SEQ_NO", (String)kColl.getDataValue("serno"));
//				kColl4trade.put("TASK_ID", "");
//				try{
//					retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, context, connection);	//调用影像锁定接口
//				}catch(Exception e){
//					throw new EMPException("影像锁定接口失败!");
//				}
//				if(!TagUtil.haveSuccess(retKColl, context)){
//					//交易失败信息
//					throw new EMPException("影像锁定接口失败!");
//				}
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			}
			if("1".equals(is_trust_loan) && "300020".equals(prd_id) && "300021".equals(prd_id)){
				PvpBizFlowComponent.insetAccDrft4Ctr(kColl, dao);
				kColl.put("cont_balance", 0.00);
				dao.update(kColl, connection);
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//				KeyedCollection retKColl = new KeyedCollection();
//				KeyedCollection kColl4trade = new KeyedCollection();
//				kColl4trade.put("CLIENT_NO", (String)kColl.getDataValue("cus_id"));
//				kColl4trade.put("BUSS_SEQ_NO", (String)kColl.getDataValue("serno"));
//				kColl4trade.put("TASK_ID", "");
//				try{
//					retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, context, connection);	//调用影像锁定接口
//				}catch(Exception e){
//					throw new EMPException("影像锁定接口失败!");
//				}
//				if(!TagUtil.haveSuccess(retKColl, context)){
//					//交易失败信息
//					throw new EMPException("影像锁定接口失败!");
//				}
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			}
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */
			if("100051".equals(prd_id)){//法人账户透支
			
				/**授权数据采集---------------start----------------------------------*/
				String cont_no = (String) kColl.getDataValue("cont_no");
				KeyedCollection retKColl2 = null;
				Trade0200200001001 trade=new Trade0200200001001();
				trade.doWfAgreeForSelfLoan(cont_no, context, connection);
				retKColl2 = trade.trade0200100000101(cont_no, context, connection);	//调用接口
				
				if(!TagUtil.haveSuccess(retKColl2, context)){
					throw new Exception("【法人账户透支贷款协议授权】,交易失败："+retKColl2.getDataValue("RET_MSG"));
				}else{
					EMPLog.log("Trade0200200001001", EMPLog.INFO, 0, "【法人账户透支贷款协议发放】交易处理完成...", null);
				}

			}
			context.addDataField("msg","");
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更  end */
		}catch (EMPException ee) {
		/*added by wangj 2015/09/24  需求编号:XD141222087,法人账户透支需求变更 begin */
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			context.setDataValue("flag", "error");
			context.addDataField("msg",ee.getMessage());
		} catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			context.setDataValue("flag", "error");
			context.addDataField("msg", e.getMessage());
			/*added by wangj 2015/09/24  需求编号:XD141222087,法人账户透支需求变更 end */
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
