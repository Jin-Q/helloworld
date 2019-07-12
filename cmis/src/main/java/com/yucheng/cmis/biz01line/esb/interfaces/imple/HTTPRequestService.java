/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HTTPRequestService {
	public abstract String getServiceName();

	public abstract String handleRequest(
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, Context paramContext,
			String paramString) throws EMPException;

	public abstract void setComponentFactory(
			ComponentFactory paramComponentFactory);

	public abstract boolean isSessionService();

	public abstract boolean isEndSessionService();

	public abstract String getSessionContextName();

	public abstract boolean isCheckSession();

	public abstract boolean isEnabled();

	public abstract void setEnabled(boolean paramBoolean);
}