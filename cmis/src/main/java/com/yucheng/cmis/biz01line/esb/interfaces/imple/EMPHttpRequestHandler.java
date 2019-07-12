/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.format.FormatElement;
import com.ecc.emp.log.EMPLog;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EMPHttpRequestHandler implements HttpRequestHandler {
	private String errorCodeField = "errorCode";

	private FormatElement requestHeadFormat = null;

	private FormatElement responseHeadFormat = null;

	private boolean appendReqHead = true;

	private boolean appendRepHead = true;
	private String serviceIdField;
	private String sessionIdField;
	private Map serviceIdMap = null;

	public void parseRequest(HttpServletRequest request) throws Exception {
		InputStream in = request.getInputStream();
		int len = request.getContentLength();
		if (len <= 0) {
			len = 2048;
		}
		byte[] tmp = new byte[2048];
		byte[] buffer = new byte[len];
		int totalLen = 0;
		while (true) {
			int readLen = in.read(tmp, 0, 2048);
			if (readLen <= 0) {
				break;
			}
			if (readLen + totalLen > len) {
				len = len + readLen + 2048;
				byte[] aa = new byte[len];
				System.arraycopy(buffer, 0, aa, 0, totalLen);
				buffer = aa;
			}
			System.arraycopy(tmp, 0, buffer, totalLen, readLen);
			totalLen += readLen;
		}

		String encoding = request.getCharacterEncoding();
		String reqData;
		if (encoding == null)
			reqData = new String(buffer, 0, totalLen);
		else {
			reqData = new String(buffer, 0, totalLen, encoding);
		}
		request.setAttribute("reqData", reqData);
	}

	public String getSessionId(HttpServletRequest request) {
		return ((String) request.getAttribute("SID"));
	}

	public String getRequestServiceName(HttpServletRequest request) {
		return ((String) request.getAttribute("serviceId"));
	}

	public void handleResponse(HttpServletRequest request,
			HttpServletResponse response, String retMsg, String reqURI,
			String sessionId) {
		try {
			String encoding = request.getCharacterEncoding();
			if (encoding != null) {
				response.setCharacterEncoding(encoding);
				response.setContentType("application/json; charset=" + encoding);
			} else {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/json; charset=" + "utf-8");
			}
			PrintWriter writer = response.getWriter();

			Context context = (Context) request
					.getAttribute(EMPConstance.ATTR_CONTEXT); 
			writer.write(retMsg);
		} catch (Exception ee) {
			EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.ERROR, 0,
					"Failed to handle response!", ee);
		}
	}

	public void handleException(HttpServletRequest request,
			HttpServletResponse response, Exception e, String reqURI,
			String sessionId) {
		try {
			String encoding = request.getCharacterEncoding();
			if (encoding != null) {
				response.setCharacterEncoding(encoding);
				response.setContentType("text/xml; charset=" + encoding);
			}
			PrintWriter writer = response.getWriter();

			Context context = (Context) request
					.getAttribute(EMPConstance.ATTR_CONTEXT);
			try {
				context.setDataValue(this.errorCodeField, "exception");
			} catch (Exception localException1) {
			}
			if ((this.appendRepHead) && (this.responseHeadFormat != null)) {
				String retHead = (String) this.responseHeadFormat
						.format(context);
				writer.write(retHead);
			}

			writer.write(e.getMessage());
		} catch (Exception ee) {
			EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.ERROR, 0,
					"Failed to handle exception!", ee);
		}
	}

	public String getErrorCodeField() {
		return this.errorCodeField;
	}

	public void setErrorCodeField(String errorCodeField) {
		this.errorCodeField = errorCodeField;
	}

	public FormatElement getRequestHeadFormat() {
		return this.responseHeadFormat;
	}

	public void setRequestHeadFormat(FormatElement headFormat) {
		this.requestHeadFormat = headFormat;
	}

	public FormatElement getResponseHeadFormat() {
		return this.responseHeadFormat;
	}

	public void setResponseHeadFormat(FormatElement responseHeadFormat) {
		this.responseHeadFormat = responseHeadFormat;
	}

	public String getServiceIdField() {
		return this.serviceIdField;
	}

	public void setServiceIdField(String serviceIdField) {
		this.serviceIdField = serviceIdField;
	}

	public String getSessionIdField() {
		return this.sessionIdField;
	}

	public void setSessionIdField(String sessionIdField) {
		this.sessionIdField = sessionIdField;
	}

	public boolean isAppendReqHead() {
		return this.appendReqHead;
	}

	public void setAppendReqHead(boolean appendHead) {
		this.appendReqHead = appendHead;
	}

	public boolean isAppendRepHead() {
		return this.appendRepHead;
	}

	public void setAppendRepHead(boolean appendHead) {
		this.appendRepHead = appendHead;
	}

	public Map getServiceIdMap() {
		return this.serviceIdMap;
	}

	public void setServiceIdMap(Map serviceIdMap) {
		this.serviceIdMap = serviceIdMap;
	}
}