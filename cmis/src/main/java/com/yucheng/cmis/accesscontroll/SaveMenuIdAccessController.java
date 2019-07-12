package com.yucheng.cmis.accesscontroll;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.accesscontrol.EMPAccessController;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.ObjectNotFoundException;

public class SaveMenuIdAccessController extends EMPAccessController {
	
	public Object checkAccess(Context sessionContext, Object requestObj, String actionId) throws EMPException {
		
		if(sessionContext == null || requestObj == null)
			return null;
		HttpServletRequest request = (HttpServletRequest)requestObj;
		
		String menuId = request.getParameter("menuId");
		if (menuId != null && menuId.length() > 0) {
			try {
				sessionContext.setDataValue("menuId", menuId);
			} catch (ObjectNotFoundException e) {
				sessionContext.addDataField("menuId", menuId);
			}
		}
		return null;
	}
}
