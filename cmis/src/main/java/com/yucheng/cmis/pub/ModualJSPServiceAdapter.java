package com.yucheng.cmis.pub;


import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * <p>
 * 	页面服务适配器类
 * 	<ul>
 * 		描述：
 * 		<li>该类作为所有模块对外提供的JSP页面服务的统一入口，由该类来决定是转发到档板还是模块实际页面。</li>
 * 		<li>所有模块页面服务的调用的URL写法是：getModualJSPService.do?modualId=m1&jspServiceId=页面服务id&param1=value1&param2=value2
 * 			其中getModualJSPService.do是对应OP是该类，modualId是指被调用的模块ID/li>
 * 		<li>根据cmis.properties控制调用档板页面服务还是模块实际对外提供的页面服务</li>
 * 		<li>跳转到公共JSP(ModualJSPServiceAdapter.jsp)页面，由公共JSP页面来将reqeust范围内的参数传递和跳转到指定（jspServiceId）的页面</ul>
 * 	</ul>
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 * @time 2013年04月24日 星期三 18时10分59秒 
 *
 */
public class ModualJSPServiceAdapter extends CMISOperation {
	
	@Override
	public String doExecute(Context context) throws EMPException {
		String jspServiceId = (String)context.getDataValue(CMISConstant.JSP_SERVICE_ID);//目标.do
		String beCalledModual = (String)context.getDataValue(CMISConstant.JSP_SERVICE_MODUAL_PARAM_NAME);//调用的模块
		
		HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
		
		//1、记录模块之间的依赖关系
		
		//2、在该类中决定调用的是档板还是模块服务
		String beCalledModualValue = CMISPropertyManager.getInstance().getPropertyValue(beCalledModual);//被调用模块是否使用档板
		//使用档板
		if(beCalledModualValue!=null && beCalledModualValue.equals(CMISConstant.MODUAL_CONFIG_TYPE_01)){
			jspServiceId = jspServiceId+CMISConstant.BAFFLE_SUFFIX;
			//context.setDataValue(CMISContents.JSP_SERVICE_ID, jspServiceId);
			//因为Context.setDataValue在JSP页面中通过getParameters取不到值，所以通过request传递
			request.setAttribute(CMISConstant.JSP_SERVICE_ID, jspServiceId);
		}
		else{
			request.setAttribute(CMISConstant.JSP_SERVICE_ID, jspServiceId);
		}
		
		return "0";
	}

}
