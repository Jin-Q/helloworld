package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class QueryBailAmtByContNoOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			String cont_no = "";
			String serno_disc = "";
			TableModelDAO dao = this.getTableModelDAO(context);
			connection = this.getConnection(context);
			if(context.containsKey("cont_no")){
				cont_no = (String) context.getDataValue("cont_no");
			}
			KeyedCollection kColl = null;
			//构建组件类
			//IqpBailComponent iqpBail = (IqpBailComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(AppConstant.IQPBAILCOMPONENT, context, connection);
			//kColl = iqpBail.getBailAmtByContNo(cont_no);
			kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
			String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");//合同币种
			String prd_id = (String)kColl.getDataValue("prd_id");//产品编号
			String serno = (String)kColl.getDataValue("serno");//业务编号
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));//合同金额
			String security_cur_type = (String)kColl.getDataValue("security_cur_type");//保证金币种
			BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));//保证金比例
			
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//获取实时汇率  start
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//			KeyedCollection kCollRate = service.getHLByCurrType(cont_cur_type, context, connection);
			KeyedCollection kCollRate4Security = service.getHLByCurrType(security_cur_type, context, connection);
//			if("failed".equals(kCollRate.getDataValue("flag"))){
//				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//			}
			if("failed".equals(kCollRate4Security.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
//			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//合同汇率
			BigDecimal adj_security_exchange_rate = BigDecimalUtil.replaceNull(kCollRate4Security.getDataValue("sld"));//保证金汇率
			//获取实时汇率  end
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//合同汇率
			BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金汇率
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			DecimalFormat df = new DecimalFormat("0.00");
			//计算保证金折算人民币金额
			BigDecimal cont_rmb_amount = cont_amt.multiply(exchange_rate);
			
			//计算保证金金额
			BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
			BigDecimal ori_bail_amt = new BigDecimal(0);//保证金金额
			BigDecimal securityAmt = new BigDecimal(0);
			if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
					prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
					prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
				
				//判断是否为信用证业务
				if("700020".equals(prd_id) || "700021".equals(prd_id)){
					KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
					if(kCollCredit.containsKey("serno")){
						floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
					}
				}
				securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc));
				securityAmt = (securityAmt.multiply(exchange_rate)).divide(security_exchange_rate,2,BigDecimal.ROUND_HALF_EVEN);
				java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				String caculateAmt = String.valueOf(securityAmt);
				securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
				String changeAmt = nf.format(securityAmt);
				ori_bail_amt = BigDecimalUtil.replaceNull(changeAmt);
			}else{
				ori_bail_amt = cont_amt.multiply(security_rate).multiply(exchange_rate).divide(security_exchange_rate,2,BigDecimal.ROUND_HALF_EVEN);
			}
			if(context.containsKey("serno")){
				serno_disc = (String) context.getDataValue("serno");
				KeyedCollection kColl4subdisc = dao.queryDetail("IqpBailSubDis", serno_disc, connection);
				ori_bail_amt = BigDecimalUtil.replaceNull(kColl4subdisc.getDataValue("ori_bail_amt"));//原保证金金额
				security_rate = BigDecimalUtil.replaceNull(kColl4subdisc.getDataValue("ori_bail_perc"));//原保证金比例
			}
			
			//计算保证金折算人民币金额
			BigDecimal security_rmb_amt = new BigDecimal(0);//保证金折算人民币金额
			security_rmb_amt = ori_bail_amt.multiply(security_exchange_rate);
			
			context.addDataField("cont_amt",cont_amt);//合同金额
			context.addDataField("cont_cur_type",cont_cur_type);//合同币种
			context.addDataField("exchange_rate",exchange_rate);//合同汇率
			context.addDataField("cont_rmb_amount",df.format(cont_rmb_amount));//合同折算成人民币金额
			
			context.addDataField("security_cur_type",security_cur_type);//保证金币种
			context.addDataField("security_exchange_rate",security_exchange_rate);//保证金汇率
			context.addDataField("ori_bail_perc",security_rate);//保证金比例
			context.addDataField("ori_bail_amt",ori_bail_amt);//保证金金额
			context.addDataField("security_rmb_amt",df.format(security_rmb_amt));//保证金折算人民币金额
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			context.addDataField("adj_security_exchange_rate",adj_security_exchange_rate);//调整保证金汇率
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
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
