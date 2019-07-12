package com.yucheng.cmis.ad.mvc;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.component.xml.GeneralComponentParser;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.flow.Operation;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.Session;
import com.ecc.emp.transaction.EMPTransactionManager;
import com.ecc.emp.web.servlet.ModelAndView;
import com.ecc.emp.web.servlet.mvc.EMPFlowExecuteResult;
import com.ecc.emp.web.servlet.mvc.EMPSessionRequestController;
import com.yucheng.cmis.base.CMISConstance;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ADSessionRequestController extends EMPSessionRequestController
{
  private String userIdField;

  public ADSessionRequestController()
  {
    this.modelUpdater = new ADModelUpdater();
  }

  protected EMPFlowExecuteResult executeEMPLogic(Context sessionContext, HttpServletRequest request, HttpServletResponse response)
  {
    EMPFlowExecuteResult result = new EMPFlowExecuteResult();

    EMPTransactionManager transactionManager = null;
	try{
		sessionContext.addDataField("sessionManager", super.sessionManager);
	}catch(Exception e){
		try{
			sessionContext.setDataValue("sessionManager", super.sessionManager);
		}catch(Exception e1){
			e.printStackTrace();
		}
	}
    String opName = null;
    Context context = null;
    try
    {
      if ((this.flowDef == null) || (!(this.flowDef instanceof ADFlowInvoker)))
        throw new EMPException("Operation not defined for controller " + getName());

      Operation operation = ((ADFlowInvoker)this.flowDef).getOperation();

      if (operation != null)
      {
        opName = operation.getName();
        if (opName == null)
          opName = operation.getClass().getName();

        EMPLog.log(EMPConstance.EMP_MVC, EMPLog.DEBUG, 0, "begin to execute operation " + opName + " ...");

        context = new Context();
        KeyedCollection dataKColl = new KeyedCollection();
        context.setDataElement(dataKColl);

        if ((sessionContext != null) && (context.getParentContextName() == null)) {
          context.chainedTo(sessionContext);
        } else {
          EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0, "The sessionContext is null , maybe a unvalid session request ...");
          String rootContextName = this.empFactory.getRootContextName();
          Context parent = this.empFactory.getContextNamed(rootContextName);
          context.chainedTo(parent);
        }

        result.context = context;

        transactionManager = (EMPTransactionManager)context.getService(EMPConstance.TRX_SVC_NAME);

        EMPLog.log(EMPConstance.EMP_MVC, EMPLog.DEBUG, 0, "Update the request data ...");

        String menuId = request.getParameter("menuId");
        try {
          context.addDataField("menuId", menuId);
        } catch (DuplicatedDataNameException e) {
          context.setDataValue("menuId", menuId);
        }

        String requestUrl = request.getServletPath();
        try {
          context.addDataField("requestUrl", requestUrl);
        } catch (DuplicatedDataNameException e) {
          context.setDataValue("requestUrl", requestUrl);
        }

        doUpdateModel(request, context, null, null);
        doPreprocessModelData(request, context, null, null);

        context.addDataField(EMPConstance.SERVLET_REQUEST, request);

        String retValue = operation.execute(context);

        if (transactionManager != null)
          transactionManager.commit();

        result.result = retValue;
      }
    }
    catch (Exception e)
    {
      EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0, "Failed to execute operation " + opName + " ! ", e);
      result.e = e;
      if (transactionManager != null)
        transactionManager.rollback();
    }

    return result;
  }

  public void endRequest(ModelAndView mv, HttpServletRequest request, long timeUsed)
  {
    super.endRequest(mv, request, timeUsed);

    String userid = null;
    try
    {
      Session session = getSession(request, null);
      if (session == null)
        return;

      Context sessionContext = (Context)session.getAttribute(EMPConstance.ATTR_CONTEXT);

      String path = CMISConstance.PERMISSIONFILE_PATH + "/server/";

      userid = (String)sessionContext.getDataValue(this.userIdField);
      path = path + userid + ".xml";

      ComponentFactory permissionFactory = new ComponentFactory();
      GeneralComponentParser parser = new GeneralComponentParser();
      permissionFactory.setComponentParser(parser);
      permissionFactory.initializeComponentFactory(userid, path);
      ComponentFactory.removeComponentFactory(userid);
      Object obj = permissionFactory.getComponent(userid);
      sessionContext.addDataField("permissions", obj);
    } catch (Exception e) {
      EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "Fail to parser the user named[" + userid + "]'s permissionAccess info!", e);
    }
  }

  protected Session initSession(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Session session = super.initSession(request, response);
    request.setAttribute(EMPConstance.EMP_SESSION, session);
    return session; }

  public String getUserIdField() {
    return this.userIdField;
  }

  public void setUserIdField(String userIdField) {
    this.userIdField = userIdField;
  }
}