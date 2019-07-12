package com.yucheng.cmis.platform.riskmanage.op;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.op.risklist.UrlLinkInterface;
/**
 * 风险拦截连接封装处理类
 * @author Pansq
 */
public class DoUrlDualOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		String className = (String)context.getDataValue("className");
		String serno = (String)context.getDataValue("serno");
		if(className == null || className == "null" || className.trim().length() == 0 ){
			throw new EMPException("配置类名不能为空！");
		}
		String nextUrl = "";
		try {
			UrlLinkInterface urlLinkInterface = (UrlLinkInterface)Class.forName(className).newInstance();
			nextUrl = urlLinkInterface.getUrl(context, className, serno);
			
			if(nextUrl != null && nextUrl.trim().length() > 0){
				context.addDataField("flag", "success");
				context.addDataField("nextUrl", nextUrl);
			}else {
				context.addDataField("flag", "failed");
				context.addDataField("nextUrl", "");
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
