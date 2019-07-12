package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.yucheng.cmis.biz01line.esb.interfaces.imple.HttpRequestHandler;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.op.DealEsbTrandOp;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.ecc.emp.accesscontrol.AccessManager;
import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.component.factory.ServletContextFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.Session;
import com.ecc.emp.session.SessionManager;

import java.util.HashMap;
import java.util.Map; 

import javax.servlet.ServletConfig; 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class EMPHttpAccessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EMPFlowComponentFactory componentFactory;
	private String factoryName;
	private String empIniFileName;
	private String rootPath;
	private HttpRequestHandler httpRequestHandler;
	private HTTPRequestService reqService ;
	private SessionManager sessionManager;
	private String sessionIdField;
	private AccessManager accessManager;
	private Map services;

	public void service(HttpServletRequest request, HttpServletResponse response)
  {
    String sessionId = "-1";
    String reqURI = request.getRequestURI();
   
    Session session = null;
    Context sessionContext = null;
    Object accessObj = null;
    long beginTimeStamp = System.currentTimeMillis(); 
    try
    {
      EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0, 
        "Accept new request from: " + request.getRemoteAddr());
      if (this.httpRequestHandler == null) {
        throw new EMPException(
          "HttpRequest handler not set for EMPHttpAccessServlet!");
      }

      if (!(checkInitialize())) {
        throw new EMPException("Container not initialized!");
      } 
     
      this.httpRequestHandler.parseRequest(request);
      String reqData = (String) request.getAttribute("reqData");//获取到请求报文
      JSONObject jsonObj = JSONObject.fromObject(reqData);
      
      if (this.sessionManager != null)
      {
        session = this.sessionManager.getSession(request, response, false);
        if (session != null)
          sessionContext = (Context)session
            .getAttribute(EMPConstance.ATTR_CONTEXT);
        request.setAttribute(EMPConstance.ATTR_SESSION, session);
      }
 
		
      DealEsbTrandOp deal = new DealEsbTrandOp();
      String retMsg = deal.exec(jsonObj); 

      this.httpRequestHandler.handleResponse(request, response, retMsg, 
        reqURI, sessionId); 
    }
    catch (Exception e)
    {
      EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.ERROR, 0, 
        "service failed!", e);

      if ((reqService != null) && (reqService.isSessionService()) && 
        (this.sessionManager != null)) {
        this.sessionManager.removeSession(session);
        sessionContext.terminate();
      }

      if (this.sessionManager != null)
        reqURI = this.sessionManager.encodeURL(request, response, reqURI, 
          "POST");
      if (this.httpRequestHandler != null)
        this.httpRequestHandler.handleException(request, response, e, 
          reqURI, sessionId);
    }
    finally
    {
      Context context;
      if ((this.accessManager != null) && (accessObj != null))
        this.accessManager.endAccess(accessObj, beginTimeStamp);
       context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
      if (context != null)
        context.terminate();
    }
  }

	public void destroy() {
		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"Destory the servlet: " + getServletName());

		if (this.sessionManager != null) {
			this.sessionManager.terminate();
		}
		if (this.empIniFileName != null)
			this.componentFactory.close();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"Start up  the servlet: " + getServletName());
		doInit();
		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"Start up  the servlet: " + getServletName() + " OK!");
	}

	private void doInit()
    throws ServletException
  {
    this.componentFactory = null;

    this.services = new HashMap();

    this.rootPath = getServletContext()
      .getInitParameter(EMPConstance.SETTINGS_ROOT);

    this.sessionIdField = getInitParameter("sessionIdField");

    if ((this.rootPath != null) && (this.rootPath.startsWith("./"))) {
      this.rootPath = getServletContext().getRealPath("/");
    }
    if (this.rootPath == null) {
      this.rootPath = getServletContext().getRealPath("/");
    }

    this.rootPath = this.rootPath.replace('\\', '/');
    if (!(this.rootPath.endsWith("/"))) {
      this.rootPath += "/";
    }
    this.factoryName = getInitParameter("factoryName");
    this.empIniFileName = getInitParameter("iniFile");

    String rootContextName = getInitParameter("rootContextName");
    try
    {
      if (this.empIniFileName != null) {
        this.componentFactory = new EMPFlowComponentFactory();
        EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0, 
          "Initialize EMP Flow Context from " + this.empIniFileName, 
          null);
        this.componentFactory.setName("EMPFlow");
        this.componentFactory.setRootContextName(rootContextName);
        this.componentFactory.initializeComponentFactory(this.factoryName, 
          this.rootPath + this.empIniFileName);
      }

      EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0, 
        "Share EMP flow Context from: " + this.factoryName);
      this.componentFactory = ((EMPFlowComponentFactory)
        ComponentFactory.getComponentFactory(this.factoryName));
    }
    catch (Exception e)
    {
      EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0, 
        "Initialize EMP Request Servlet from " + this.empIniFileName + 
        " failed!", e);
      throw new ServletException("Failed to initialize the EMP context ", 
        e);
    }

    label391: initHttpReqContext();
    EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0, 
      "Initialize EMP Request Servlet from " + this.empIniFileName + 
      " OK!", null);
  }

	private void initHttpReqContext() {
		String servletContextFile = getInitParameter("servletContextFile");

		EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0,
				"Initialize EMP Servlet Context from " + servletContextFile,
				null);
		ServletContextFactory ctxFactory = new ServletContextFactory();
		ctxFactory.initializeComponentFactory("contextFactory", this.rootPath
				+ servletContextFile);
		try {
			ctxFactory.parseTheContext(this);
			ctxFactory.exportMBean(this, getServletName());

			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.INFO, 0,
					"Initialize EMP Servlet Context from " + servletContextFile
							+ " OK!", null);
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0,
					"Initialize EMP Servlet Context from " + servletContextFile
							+ " Failed", e);
		}
	}

	public void addHTTPRequestService(HTTPRequestService service) {
		this.services.put(service.getServiceName(), service);
		service.setComponentFactory(this.componentFactory);
	}

	private boolean checkInitialize() {
		if ((this.componentFactory == null)
				|| (this.componentFactory.isClosed())) {
			synchronized (this) {
				if ((this.componentFactory != null)
						&& (!(this.componentFactory.isClosed()))) {
					return true;
				}
				this.componentFactory = ((EMPFlowComponentFactory) ComponentFactory
						.getComponentFactory(this.factoryName));

				return (this.componentFactory != null);
			}

		}

		return true;
	}

	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public HttpRequestHandler  getHttpRequestHandler() {
		return this.httpRequestHandler;
	}

	public void setHttpRequestHandler(HttpRequestHandler  httpRequestHandler) {
		this.httpRequestHandler = httpRequestHandler;
	}

	public HTTPRequestService  getHTTPRequestService() {
		return this.reqService;
	}

	public void setHTTPRequestService(HTTPRequestService  hTTPRequestService) {
		this.reqService = hTTPRequestService;
	}
	
	public AccessManager getAccessManager() {
		return this.accessManager;
	}

	public void setAccessManager(AccessManager accessManager) {
		this.accessManager = accessManager;
	}

	public void enableService(String serviceId) {
		HTTPRequestService svc = (HTTPRequestService) this.services
				.get(serviceId);
		if (svc != null)
			svc.setEnabled(true);
	}

	public void disableService(String serviceId) {
		HTTPRequestService svc = (HTTPRequestService) this.services
				.get(serviceId);
		if (svc != null)
			svc.setEnabled(false);
	}

	public boolean isServiceEnabled(String serviceId) {
		HTTPRequestService svc = (HTTPRequestService) this.services
				.get(serviceId);
		if (svc != null)
			return svc.isEnabled();
		return false;
	}

	public String[] getServiceList() {
		Object[] ll = this.services.keySet().toArray();
		if ((ll == null) || (ll.length == 0))
			return null;
		String[] tmp = new String[ll.length];

		for (int i = 0; i < ll.length; ++i) {
			tmp[i] = ll[i].toString();
		}
		return tmp;
	}

	public void reloadServiceContext() {
		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"Reload ServiceContext for " + getServletName());

		this.services = new HashMap();

		Map sessionMaps = null;
		if (getSessionManager() != null) {
			sessionMaps = getSessionManager().getSessions();
		}

		initHttpReqContext();

		if ((getSessionManager() != null) && (sessionMaps != null)) {
			getSessionManager().setSessions(sessionMaps);
		}

		EMPLog.log(EMPConstance.EMP_HTTPACCESS, EMPLog.INFO, 0,
				"Reload ServiceContext for " + getServletName() + " OK!");
	}
}