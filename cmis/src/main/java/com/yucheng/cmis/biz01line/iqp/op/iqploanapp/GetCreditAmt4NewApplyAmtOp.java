package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class GetCreditAmt4NewApplyAmtOp extends CMISOperation {
	private final String modelId = "IqpCredit";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = "";
			String apply_cur_type = "";
			BigDecimal apply_amount = null;
			BigDecimal exchange_rate = null;
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("apply_cur_type")){
				apply_cur_type = (String)context.getDataValue("apply_cur_type");
			}
			if(context.containsKey("apply_amount")){
				apply_amount = BigDecimalUtil.replaceNull(context.getDataValue("apply_amount"));
			}
			if(context.containsKey("exchange_rate")){
				exchange_rate = BigDecimalUtil.replaceNull(context.getDataValue("exchange_rate"));
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/** 通过币种获取汇率表CRD_HLWH中币种汇率 */
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection ic = cmisComponent.getHLByCurrType(apply_cur_type);
			if("success".equals(ic.getDataValue("flag")+"")){
				BigDecimal base_remit = BigDecimalUtil.replaceNull(ic.getDataValue("sld"));
				BigDecimal amount = apply_amount.multiply(base_remit);
				KeyedCollection kCollCredit = dao.queryDetail(modelId, serno, connection);
				if(kCollCredit.containsKey("serno")){
					BigDecimal floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
					amount = apply_amount.add((apply_amount.multiply(exchange_rate)).multiply(floodact_perc));
					context.put("flag", "success");
					context.put("msg", "");
					context.put("rateValue", amount);
				}else{
					context.put("flag", "noCredit");
					context.put("msg", "未录入信用证溢装比例！");
					context.put("rateValue", apply_amount);
				}
			}else {
				context.put("flag", "failed");
				context.put("msg", "汇率表中未取到匹配汇率，请确认汇率表汇率配置");
				context.put("rateValue", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}
}
