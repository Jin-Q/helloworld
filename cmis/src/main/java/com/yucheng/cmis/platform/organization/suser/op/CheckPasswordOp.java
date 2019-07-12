package com.yucheng.cmis.platform.organization.suser.op;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
 
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.MD5;
/**
 * 检查密码
 * @author wqgang
 *
 */
public class CheckPasswordOp extends CMISOperation {
	
	private final String modelId = "IqpLoanAppIqpComLoanWcl";
	public String doExecute(Context context) throws EMPException {
     
      
			String cusid=(String) context.getDataValue("cusid");
			String password=(String) context.getDataValue("password");
			String passwordold=(String) context.getDataValue("passwordold");
			if(MD5.getMD5ofStr(cusid+password).equals(passwordold)) {
				 context.addDataField("result", "ok");
			}else{
				context.addDataField("result", "nook");
				//throw new CMISException(CMISMessage.MESSAGEDEFAULT,"!");
			}
			

		
		return "success";
}
}
