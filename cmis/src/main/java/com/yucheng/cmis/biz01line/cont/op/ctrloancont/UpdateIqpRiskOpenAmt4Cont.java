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
/**
 * 
 * <pre> 
 * Title:合同签订同步业务申请
 * Description: 需求：XD150407026， 贸易融资外币业务汇率，合同签订同步申请业务敞口及汇率
 * </pre>
 * @author yangzhiyong  yangjy1@yuchengtech.com
 * 创建日期：2015-10-28 下午05:08:25
 * @version 1.00.00
 * <pre>
 *    修改后版本:        修改人：         修改日期:              修改内容: 
 * </pre>
 */
public class UpdateIqpRiskOpenAmt4Cont extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
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
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			context.setDataValue("flag", "error");
		} catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			context.setDataValue("flag", "error");
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
