package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class QuerySearchAmt4WfiOp extends CMISOperation {

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			String appType = (String)context.getDataValue("app_type");
			Map<String,String> modelMap=new HashMap<String,String>();
			modelMap.put("IN_申请流水号", serno_value);
			Map<String,String> outMap=new HashMap<String,String>();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  begin**/
			if("03".equals(appType)){
				outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMTAPPDET", modelMap);
				BigDecimal frozeAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_冻结金额").toString()) ;
				context.addDataField("flag", PUBConstant.SUCCESS);
				context.addDataField("Amt", frozeAmt);
			}else if("04".equals(appType)){
				outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETUNFROZENAMTAPPDET", modelMap);
				BigDecimal unfrozenAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_解冻金额").toString()) ;
				context.addDataField("flag", PUBConstant.SUCCESS);
				context.addDataField("Amt", unfrozenAmt);
			}
			/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  end**/
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
