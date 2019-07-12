package com.yucheng.cmis.ad.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.flow.Operation;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.transaction.EMPTransactionManager;
import com.ecc.emp.web.servlet.mvc.EMPFlowExecuteResult;
import com.ecc.emp.web.servlet.mvc.EMPRequestController;

public class ADRequestController extends EMPRequestController {

	public ADRequestController() {
		super();
		this.modelUpdater = new ADModelUpdater();
	}
	 public boolean checkSession = true;
	@Override
	public void setCheckSession(boolean checkSession) {
		super.setCheckSession(checkSession);
	}
	
   public boolean updateCheck=false;
  

		/**
	 * @return the updateCheck
	 */
	public boolean isUpdateCheck() {
		return updateCheck;
	}
	
	/**
	 * @param updateCheck the updateCheck to set
	 */
	public void setUpdateCheck(boolean updateCheck) {
		this.updateCheck = updateCheck;
	}

	/**
	 * 执行EMP的逻辑定义
	 * 
	 * @param sessionContext
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected EMPFlowExecuteResult executeEMPLogic(Context sessionContext,
			HttpServletRequest request, HttpServletResponse response) {
		EMPFlowExecuteResult result = new EMPFlowExecuteResult();
		try{
			sessionContext.addDataField("sessionManager", super.sessionManager);
		}catch(Exception e){
			try{
				sessionContext.setDataValue("sessionManager", super.sessionManager);
			}catch(Exception e1){
				e.printStackTrace();
			}
		}
		EMPTransactionManager transactionManager = null;

		String opName = null;
		Context context = null;

		try {
			if (this.flowDef == null
					|| !(this.flowDef instanceof ADFlowInvoker))
				throw new EMPException("FlowInvoker is not defined for controller " + getName());

			Operation operation = ((ADFlowInvoker) this.flowDef).getOperation();
			if (operation == null) {
				throw new EMPException( "Operation is not defined for controller " + getName());
			}

			opName = operation.getName();
			if (opName == null)
				opName = operation.getClass().getName();

			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.DEBUG, 0, "begin to execute operation [" + opName + "] ...");

			context = new Context();
			KeyedCollection dataKColl = new KeyedCollection();
			context.setDataElement(dataKColl);

			if (sessionContext != null
					&& context.getParentContextName() == null)
				context.chainedTo(sessionContext);
			else {
				EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0, "The sessionContext is null , maybe a unvalid session request ...");
			}

			result.context = context;

			transactionManager = (EMPTransactionManager) context
					.getService(EMPConstance.TRX_SVC_NAME);

			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.DEBUG, 0,
					"Update the request data ...");

			String menuId = request.getParameter("menuId");
			if (menuId != null && menuId.length() > 0) {
				try {
					context.setDataValue("menuId", menuId);
				} catch (ObjectNotFoundException e) {
					context.addDataField("menuId", menuId);
				}
			}

			String requestUrl = request.getServletPath();
			try {
				context.addDataField("requestUrl", requestUrl);
			} catch (DuplicatedDataNameException e) {
				context.setDataValue("requestUrl", requestUrl);
			}

			doUpdateModel(request, context, null, null);
			this.doPreprocessModelData(request, context, null, null);

			context.addDataField(EMPConstance.SERVLET_REQUEST, request);

			try{
				context.addDataField("updateCheck", this.isUpdateCheck());
			}catch(Exception e){
				context.setDataValue("updateCheck", this.isUpdateCheck());
			}
			String retValue = operation.execute(context);

			// commit the transaction if exist
			if (transactionManager != null)
				transactionManager.commit();

			result.result = retValue;
			
			 

		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0,
					"Failed to execute operation " + opName + " ! ", e);
			result.e = e;
			if (transactionManager != null)
				transactionManager.rollback();

		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println("---");
	}
	
}
