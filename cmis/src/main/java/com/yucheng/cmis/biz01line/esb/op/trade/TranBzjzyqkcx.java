package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBailComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 保证金占用情况查询交易
 * @author tangzf
 * 根据保证金账号查询占用情况并返回
 * 
 */
public class TranBzjzyqkcx extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String ACCT_NO = "";
		BigDecimal totlAmt = new BigDecimal(0.0);
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			ACCT_NO = (String)reqBody.getDataValue("GUARANTEE_ACCT_NO");//保证金账号
			
			EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
			Context context = factory.getContextNamed(factory.getRootContextName());
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openday);
			context.put("currentUserId","admin");
			context.put("currentUserName","admin");
			context.put("organNo","9350000000");
			IqpBailComponent iqpBailComponent = (IqpBailComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBAILCOMPONENT, context, connection);
			
			
			/** 根据保证金账号查询占用金额 */
			totlAmt = iqpBailComponent.getBailAmtByAcctNo4Ctr(ACCT_NO);
			
			/** 组装返回报文 */
			retKColl.setDataValue("ret_msg", "【保证金占用情况查询】交易处理完成");
			retKColl.put("acct_no", ACCT_NO);
			retKColl.put("total_amt", totlAmt);
			
			EMPLog.log("TranBzjzyqkcx", EMPLog.INFO, 0, "【保证金占用情况查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【保证金占用情况查询】业务处理失败！保证金账号为："+ACCT_NO);
			e.printStackTrace();
			EMPLog.log("TranBzjzyqkcx", EMPLog.ERROR, 0, "【保证金占用情况查询】交易处理失败，保证金账号为："+ACCT_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
