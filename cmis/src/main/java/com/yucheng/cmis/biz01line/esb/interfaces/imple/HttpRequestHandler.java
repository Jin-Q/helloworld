/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HttpRequestHandler {
	public abstract void parseRequest(HttpServletRequest paramHttpServletRequest)
			throws Exception;

	public abstract String getSessionId(
			HttpServletRequest paramHttpServletRequest);

	public abstract String getRequestServiceName(
			HttpServletRequest paramHttpServletRequest);

	public abstract void handleException(
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse,
			Exception paramException, String paramString1, String paramString2);

	public abstract void handleResponse(
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, String paramString1,
			String paramString2, String paramString3);
}