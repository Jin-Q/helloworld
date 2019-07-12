package com.yucheng.cmis.pub.base;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.EMPSessionManager;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * <p>用户签退</p>
 * @author yucheng
 *
 */
public class CMISUserLogoutOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		EMPSessionManager smgr = null;
		String sessionId = null;
		
		try{
		   smgr = (EMPSessionManager)context.getDataValue("sessionManager");
		   sessionId = (String)context.getDataValue(EMPConstance.EMP_SESSION_ID_LABEL);
		   if(smgr != null && sessionId != null){
			   smgr.removeSession(sessionId);
		   }
		}catch(ObjectNotFoundException ex){
			EMPLog.log(EMPConstance.EMP_CORE, 0, EMPLog.INFO, "用户会话可能已经失效，签退时不再清除sessionManager");
		}catch(Exception ex){
			ex.printStackTrace();
			EMPLog.log(EMPConstance.EMP_CORE, 0, EMPLog.ERROR,"签退时异常："+ex.getMessage());
		}	
		HttpServletRequest request = null;
		try{
		  request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
		  if(request != null){
		     request.getSession().removeAttribute(EMPConstance.SESSION_ID);
		  }
		}catch(ObjectNotFoundException ex){
			EMPLog.log(EMPConstance.EMP_CORE, 0, EMPLog.INFO, "用户会话可能已经失效，签退时不再清除sessionManager");
		}catch(Exception ex){
			ex.printStackTrace();
			EMPLog.log(EMPConstance.EMP_CORE, 0, EMPLog.ERROR,"签退时异常："+ex.getMessage());
		}
 
		return null;
	}

}
