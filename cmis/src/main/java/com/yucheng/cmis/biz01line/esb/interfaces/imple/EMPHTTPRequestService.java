/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataElementSerializer;
import com.ecc.emp.data.DataUtility;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.flow.EMPFlow;
import com.ecc.emp.format.FormatElement;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.transaction.EMPTransactionManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EMPHTTPRequestService implements HTTPRequestService {
	public static int SESSION_SERVICE = 0;

	public static int END_SESSION_SERVICE = 1;

	public static int NORMAL_SERVICE = 2;
	protected String serviceName;
	protected boolean checkSession = true;

	protected int serviceType = NORMAL_SERVICE;
	protected String sessionContextName;
	protected String EMPFlowId;
	protected String opId;
	private FormatElement requestDataFormat;
	private FormatElement responseDataFormat;
	protected EMPFlowComponentFactory factory = null;

	private boolean enabled = true;

	/** @deprecated */
	public String getEMPFlowId() {
		return this.EMPFlowId;
	}

	/** @deprecated */
	public void setEMPFlowId(String flowId) {
		this.EMPFlowId = flowId;
	}

	public String getBizId() {
		return this.EMPFlowId;
	}

	public void setBizId(String bizId) {
		this.EMPFlowId = bizId;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSessionContextName() {
		return this.sessionContextName;
	}

	public void setSessionContextName(String sessionContextName) {
		this.sessionContextName = sessionContextName;
	}

	public void setId(String id) {
		this.serviceName = id;
	}

	public int getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public void setServiceType(String value) {
		if ("session".equals(value))
			this.serviceType = SESSION_SERVICE;
		else if ("endSession".equals(value))
			this.serviceType = END_SESSION_SERVICE;
	}

	public String handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context sessionContext,
			String sessionId) throws EMPException {
		EMPFlow flow = null;
		Context flowContext = null;

		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"EMPHTTPService [" + this.serviceName + "] receive request :"
						+ request.getAttribute("reqData"));
		try {
			flow = this.factory.getEMPFlow(getEMPFlowId());

			if (flow == null) {
				throw new EMPException("Flow named " + getEMPFlowId()
						+ " not defined!");
			}
			flowContext = (Context) flow.getContext().clone();

			if ((flowContext.getParent() == null) && (sessionContext != null)) {
				flowContext.chainedTo(sessionContext);
			}
			request.setAttribute(EMPConstance.ATTR_CONTEXT, flowContext);

			String opName = getOpId();

			DataElement outputData = null;
			DataElement inputDataDef = flow.getInput(opName);

			updateModel(request, response, flowContext, inputDataDef);

			EMPTransactionManager transactionManager = null;
			try {
				transactionManager = (EMPTransactionManager) flowContext
						.getService(EMPConstance.TRX_SVC_NAME);

				EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
						"execute the Flow: " + getEMPFlowId() + "." + getOpId()
								+ "...");

				String retValue = flow.execute(flowContext, opName);

				outputData = flow.getOutput(flowContext, opName);

				EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
						"execute the Flow: " + getEMPFlowId() + " retValue:"
								+ retValue);

				if (transactionManager != null) {
					transactionManager.commit();
				}
				String retMsg = getResponseMessage(request, response,
						flowContext, outputData);

				EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
						"EMPHTTPService [" + this.serviceName
								+ "] receive return :" + retMsg);

				return retMsg;
			} catch (Exception e) {
				if (transactionManager != null)
					transactionManager.rollback();
				throw e;
			}
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException("Failed to process http service "
					+ super.toString(), e);
		}
	}

	public void updateModel(HttpServletRequest request,
			HttpServletResponse response, Context context,
			DataElement dataElementDef) throws EMPException {
		String reqData = (String) request.getAttribute("reqData");
		if ((reqData == null) || (reqData.length() == 0)) {
			return;
		}
		if (dataElementDef == null) {
			DataElement element = null;
			if (getRequestDataFormat() != null) {
				element = new KeyedCollection();
				element.setAppend(true);
				try {
					getRequestDataFormat().unFormat(reqData, element);
				} catch (Exception e) {
					throw new EMPException(e.toString());
				}
			} else {
				element = DataElementSerializer.serializeFrom(reqData);
			}
			DataUtility.updateDataModel(context, element,
					this.factory.getDataTypeDefine());
		} else {
			DataElement dstElement = (DataElement) dataElementDef.clone();
			if (getRequestDataFormat() != null) {
				try {
					getRequestDataFormat().unFormat(reqData, dstElement);
				} catch (Exception e) {
					throw new EMPException(e.toString());
				}
			} else {
				DataElement element = DataElementSerializer
						.serializeFrom(reqData);
				DataUtility.copyKeyedCollectionData((KeyedCollection) element,
						(KeyedCollection) dstElement, null);
			}
			DataUtility.updateDataModel(context, dstElement,
					this.factory.getDataTypeDefine());
		}
	}

	protected String getResponseMessage(HttpServletRequest request,
			HttpServletResponse response, Context context,
			DataElement dataElement) throws Exception {
		try {
			String resData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<kColl></kcoll>";
			if (dataElement != null) {
				if (getResponseDataFormat() != null)
					resData = (String) getResponseDataFormat().format(
							dataElement);
				else {
					resData = DataElementSerializer.doSerialize(dataElement);
				}

			} else if (getResponseDataFormat() != null)
				resData = (String) getResponseDataFormat().format(context);
			else {
				return resData;
			}

			return resData;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isCheckSession() {
		return this.checkSession;
	}

	public void setCheckSession(boolean checkSession) {
		this.checkSession = checkSession;
	}

	public String getOpId() {
		return this.opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public boolean isSessionService() {
		return (this.serviceType == SESSION_SERVICE);
	}

	public boolean isEndSessionService() {
		return (this.serviceType == END_SESSION_SERVICE);
	}

	public FormatElement getRequestDataFormat() {
		return this.requestDataFormat;
	}

	public void setRequestDataFormat(FormatElement requestDataFormat) {
		this.requestDataFormat = requestDataFormat;
	}

	public FormatElement getResponseDataFormat() {
		return this.responseDataFormat;
	}

	public void setResponseDataFormat(FormatElement responseDataFormat) {
		this.responseDataFormat = responseDataFormat;
	}

	public void setComponentFactory(ComponentFactory factory) {
		this.factory = ((EMPFlowComponentFactory) factory);
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}