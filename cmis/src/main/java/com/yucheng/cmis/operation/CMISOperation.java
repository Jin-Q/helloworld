package com.yucheng.cmis.operation;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.ecc.emp.accesscontrol.AccessInfo;
import com.ecc.emp.accesscontrol.AccessManager;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.flow.EMPAction;
import com.ecc.emp.flow.Flow;
import com.ecc.emp.flow.Operation;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.ecc.emp.transaction.EMPTransaction;
import com.ecc.emp.transaction.EMPTransactionDef;
import com.ecc.emp.transaction.EMPTransactionManager;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.permission.SRowrightService;
import com.yucheng.cmis.primarykey.CMISPrimaryKeyService;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.sequence.CMISSequenceService;

/**
 * 业务逻辑处理基类。
 * <p>
 * 所有CMIS的业务处理实现类都必须继承自该类
 * 
 * @author zhangjiawei
 * 
 */
abstract public class CMISOperation extends Operation { 
	
	private boolean monitorOpened = false;

	private AccessInfo accessInfo = null;
	
	/**
	 * 隶属的BusinessLogic的ID
	 */
	private String bizId;

	private String defaultDataSourceName = null;

	/**
	 * 业务逻辑执行，不允许重载
	 * 
	 * 在本方法里处理访问控制、事务管理等功能
	 * 
	 * 在本方法中，通过getConnection()获得的连接，如果不做特定逻辑处理，则保持在一个全局事务中。
	 * 如果调用executeAction（）方法来运行一个EMPAction，则，也可以保持在这个全局事务中。
	 * 如果调用executeAction（）方法来运行的EMPAction中声明为独立事务，则，运行这个EMPAction，可以作为独立事务来处理。
	 */
	@Override
	final public String execute(Context context) throws Exception {
		long begin = System.currentTimeMillis();
		AccessManager accessManager = (AccessManager) context
				.getService(EMPConstance.ACCESS_MANAGER);
		Object accessObj = null;
		String retValue = null;
		Throwable te = null;
		try {
			if (accessManager != null) {
				String accessItemId = this.getName();
				if (this.getBizId() != null)
					accessItemId = getBizId() + "." + this.getName();

				accessObj = accessManager.checkAccess(context, null, accessItemId);
				if (accessObj != null)
					accessManager.beginAccess(accessObj);
			}
			if (monitorOpened)
				accessInfo.newAccess();

			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "The flow [" + getName() + "] 's context is :", null);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, context.toString(), null);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "The flow [" + getName() + "] 's data is :", null);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, context.getDataElement().toString(), null);
			
			try {
				HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				String requestUrl = request.getServletPath();
				if(context.containsKey("menuIdTab")){
					request.setAttribute("menuIdTab", context.getDataValue("menuIdTab"));
				}
				try {
					context.addDataField("requestUrl", requestUrl);
				} catch (DuplicatedDataNameException e) {
					context.setDataValue("requestUrl", requestUrl);
				}
			} catch (Exception e1) {}
			
			EMPTransactionDef transactionDef = this.getTransactionDef();
			EMPTransactionManager transactionManager = this.getTransactionManager(context);
			if( transactionDef != null && transactionManager != null )
			{
				transactionManager.getTransaction( transactionDef );
			}
			
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "Begin to execute the flow [" + getName() + "]...", null);
			retValue = doExecute(context);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "Execute the flow [" + getName() + "] end.  retValue=" + retValue, null);
			
			return retValue;
		} catch (Exception e) {
			te = e;
			throw e;
		} finally {
			if (monitorOpened) {
				accessInfo.setRetValue(retValue);
				accessInfo.setThrowable(te);
				accessInfo.endAccess(System.currentTimeMillis() - begin);
			}

			if (accessManager != null && accessObj != null)
				accessManager.endAccess(accessObj, begin);
		}		
	}

	/**
	 * 执行业务逻辑的具体代码
	 * 
	 * @param context
	 * @return
	 */
	abstract public String doExecute(Context context) throws EMPException;
	
//	int mode = 0;//0:独立事务，1：全局事务
//	EMPTransaction transaction = getTransactionManager(context)
//			.getTransaction(new EMPTransactionDef(mode));
//	try {
//		//getConnection(context);
//		// service.dosth();
//		//releaseConnection(context,connection);
//		if (mode == 0)
//			getTransactionManager(context).commit(transaction);
//	} catch (Exception e) {
//		if (mode == 0)
//			getTransactionManager(context).rollback();
//		else
//			getTransactionManager(context).setRollBackOnly();
//	}

	/**
	 * 业务逻辑初始化，在此，可以创建action并进行属性赋值等操作
	 * 
	 */
	public void initialize(){
		
	}

	/**
	 * 用于直接在代码中，执行EMPAction对象，并能够进行事务处理。
	 * 
	 * @param transactionManager
	 * @param action
	 * @param context
	 * @return
	 * @throws Exception
	 */
	final protected String executeAction(Context context, EMPAction action)
			throws Exception {
		EMPTransaction transaction = null;
		String retValue = null;
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "Execute the step ["
				+ action.getFullName() + "]...", null);

		EMPTransactionDef transactionDef = action.getTransactionDef();
		EMPTransactionManager transactionManager = getTransactionManager(context);
		if (transactionDef != null && transactionManager != null) {
			transaction = transactionManager.getTransaction(transactionDef);
		}

		try {
			retValue = action.execute(context);
			setRetValue(context, retValue);
			if (transactionDef != null
					&& transactionDef.getTrsactionReq() == EMPTransactionDef.TRX_REQUIRE_NEW) // 对于要求建立新的交易事务的Action，只在单一请求中完成
			{
				transactionManager.commit(transaction);
			}
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "The step ["
					+ action.getFullName() + "] returns value: " + retValue,
					null);
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,
					"Execute the step [" + action.getFullName()
							+ "] end dure to Exception : " + e, e);
			// if there are new transaction, just rollback it!
			if (transactionDef != null
					&& transactionDef.getTrsactionReq() == EMPTransactionDef.TRX_REQUIRE_NEW) // 对于要求建立新的交易事务的Action，只在单一请求中完成
			{
				transactionManager.rollback();
			} else if (transactionDef != null)
			// there are exception so need to rollBack current transaction
			{
				transactionManager.setRollBackOnly();
			}
			throw e;
		}
		return retValue;
	}

	/**
	 * 将当前交易步骤返回值设置到context名为的retValue数据域中 用于EndAction缺省返回值
	 * 
	 * @param context
	 * @param retValue
	 */
	private void setRetValue(Context context, String retValue) {
		DataField field = null;
		try {
			field = (DataField) context.getDataElement("retValue");
		} catch (EMPException e) {
		}
		try {
			if (field != null)
				context.setDataValue("retValue", retValue);
			else
				context.addDataField("retValue", retValue);
		} catch (EMPException e) {

		}
	}


	
	/**
	 * 获得缺省的数据库服务
	 * @param context
	 * @return
	 * @throws EMPJDBCException
	 */
	protected DataSource getDataSource(Context context) throws EMPJDBCException{
		DataSource dataSource = null;
		if(this.defaultDataSourceName != null)
			dataSource = (DataSource) context.getService(defaultDataSourceName);
		if(dataSource == null)
			dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		return dataSource;
	}
	
	/**
	 * 获得数据库服务
	 * @param context
	 * @param dataSourceName
	 * @return
	 * @throws EMPJDBCException
	 */
	protected DataSource getDataSource(Context context, String dataSourceName) throws EMPJDBCException{
		DataSource dataSource = (DataSource) context.getService(dataSourceName);
		return dataSource;
	}

	/**
	 * 使用默认数据源来获取数据库连接
	 * 
	 * @param context
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	protected Connection getConnection(Context context) throws EMPJDBCException, SessionException {
		DataSource dataSource = null;
		if(this.defaultDataSourceName != null)
			dataSource = (DataSource) context.getService(defaultDataSourceName);
		if(dataSource == null)
			dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		return getConnection(context, dataSource);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param context
	 * @param dataSourceName
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	protected Connection getConnection(Context context, String dataSourceName)
			throws EMPJDBCException, SessionException {
		DataSource dataSource = (DataSource) context.getService(dataSourceName);
		return getConnection(context, dataSource);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	private Connection getConnection(Context context, DataSource dataSource)
			throws EMPJDBCException, SessionException {
		if (dataSource == null)
			throw new SessionException("登陆超时，请重新登陆或联系管理员 !"
					+ this.toString());
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		
		EMPLog.log( EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Apply new connection from data source: "+dataSource+" success!");
		return connection;
	}

	/**
	 * 释放数据库连接
	 * 
	 * @param dataSource
	 * @param connection
	 * @throws EMPJDBCException
	 */
	private void releaseConnection(DataSource dataSource, Connection connection)
			throws EMPJDBCException {
		ConnectionManager.releaseConnection(dataSource, connection);
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Do release the connection from data source: " + dataSource + " success!");
	}

	/**
	 * 释放数据库连接
	 * 
	 * @param context
	 * @param dataSourceName
	 * @param connection
	 * @throws EMPJDBCException
	 */
	protected void releaseConnection(Context context, String dataSourceName,
			Connection connection) throws EMPJDBCException {
		DataSource dataSource = (DataSource) context.getService(dataSourceName);
		releaseConnection(dataSource, connection);
	}

	/**
	 * 使用默认数据源ID释放数据库连接
	 * 
	 * @param context
	 * @param connection
	 * @throws EMPJDBCException
	 */
	protected void releaseConnection(Context context, Connection connection)
			throws EMPJDBCException {
		if(this.defaultDataSourceName != null)
			releaseConnection(context, defaultDataSourceName, connection);
		else
			releaseConnection(context, CMISConstance.ATTR_DATASOURCE, connection);
	}
	
	/**
	 * 获得随机的主键
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected String getPrimaryKey(Context context) throws EMPException{
		CMISPrimaryKeyService service = (CMISPrimaryKeyService)context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
		return service.getPrimaryKey();
	}
	
	/**
	 * 获得流水号
	 * @param aType
	 * @param owner
	 * @param context
	 * @param connection
	 * @return
	 * @throws EMPException
	 */
	protected String getSequence(String aType,String owner,Context context,Connection connection) throws EMPException{
		CMISSequenceService service = (CMISSequenceService)context.getService(CMISConstance.ATTR_SEQUENCESERVICE);
		return service.getSequence(aType, owner, context, connection);
	}

	/**
	 * 获得整个系统的根路径
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected String getRequestContextPath(Context context) throws EMPException{
		HttpServletRequest request = null;
		try {
			request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
		} catch (ObjectNotFoundException e) {
			return null;
		}
		if(request == null)
			return null;
		else
			return request.getContextPath();
	}
	
	/**
	 * 在Servlet接入时，获得request对象进行处理
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected HttpServletRequest getHttpServletRequest(Context context) throws EMPException{
		HttpServletRequest request = null;
		try {
			request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
		} catch (ObjectNotFoundException e) {
			return null;
		}
		if(request == null)
			return null;
		else
			return request;
	}
	
	/**
	 * 取得缺省的数据库表模型的操作类
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected TableModelDAO getTableModelDAO(Context context) throws EMPException{
		return this.getTableModelDAO(CMISConstance.ATTR_TABLEMODELDAO, context);
	}
	
	/**
	 * 根据指定的名称找到相应的数据库表模型的操作类
	 * @param modelId
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected TableModelDAO getTableModelDAO(String modelId, Context context) throws EMPException{
		TableModelDAO dao = (TableModelDAO)context.getService(modelId);
		return dao;
	}

	/**
	 * 取到缺省的记录级权限的服务类
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected RecordRestrict getRecordRestrict(Context context)throws EMPException{
		return this.getRecordRestrict(CMISConstance.ATTR_RECORDRESTRICT, context);
	}

	/**
	 * 根据指定的名称找到相应的记录级权限的服务类
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected RecordRestrict getRecordRestrict(String serviceId, Context context)throws EMPException{
		RecordRestrict recordRestrict = (RecordRestrict)context.getService(serviceId);
		return recordRestrict;
	}
	
	protected EMPTransactionManager getTransactionManager(Context context) {
		return (EMPTransactionManager) context
				.getService(EMPConstance.TRX_SVC_NAME);
	}
	
	/**
	 * 将特定的dataElement设置到Context中，如有重名，则覆盖原有的数据
	 * @param dataElement
	 * @param context
	 * @throws InvalidArgumentException
	 */
	protected void putDataElement2Context(DataElement dataElement, Context context) throws InvalidArgumentException{
		try {
			context.addDataElement(dataElement);
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (DuplicatedDataNameException e) {
			context.removeDataElement(dataElement.getName());
			try {
				context.addDataElement(dataElement);
			} catch (DuplicatedDataNameException e1) {
			}
		}
	}
	
	/**
	 * 缺省使用全局事务，若子类不想进行事务处理，则重写该方法，并返回null
	 * @return
	 */
	public EMPTransactionDef getTransactionDef(){
		EMPTransactionDef transactionDef = new EMPTransactionDef(EMPTransactionDef.TRX_REQUIRED);
		return transactionDef;
	}
	
	@Override
	public void setMonitorOpened(boolean value) {
		this.monitorOpened = value;
		String accessItemId = this.getName();
		if (this.getBizId() != null)
			accessItemId = getBizId() + "." + this.getName();

		if (monitorOpened)
			accessInfo = new AccessInfo(accessItemId);
		else
			accessInfo = null;
	}
	
	@Override
	public AccessInfo getAccessInfo() {
		return this.accessInfo;
	}
	
	@Override
	public Flow getFlow() {
		return null;
	}
	
	@Override
	public void setFlow(Flow flow) {
		
	}

	@Override
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	

	public String judgeRowRight(Context context, Connection connection,TableModelDAO dao, String modelId, String condition, String restrictType) throws EMPException{
		SRowrightService SRowrightService=(SRowrightService)context.getService(CMISConstance.ATTR_ROWRIGHTRESTRICT);
		condition= SRowrightService.getRestrict(context,connection,dao,modelId, condition, restrictType);
		if(condition==null)
			return "";
		return condition;
	}
	
	/**
	 * 获取当前营业日期
	 * @param context
	 * @return
	 * @throws ComponentException
	 */
	public String getOpenDay(Context context) throws ComponentException{
		String openDay = null;		//当前营业日期
		try {
			openDay= (String) context.getDataValue(PUBConstant.OPENDAY);
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取营业时间失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"获取营业失败！请重新操作！");
		
		}
		return openDay;
	}
	/**
	 * 获取当前操作人ID
	 * @param context
	 * @return
	 * @throws ComponentException
	 */
	public String getLoginUserId(Context context) throws ComponentException {
		String loginuserid = null; //当前编辑用户
		try {
			loginuserid = (String)(context.getDataValue(PUBConstant.loginuserid));
		} catch (Exception e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取当前当前操作人失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的当前操作人代码失败！请重新操作！");
		}
		return loginuserid;
	}
	/**
	 * 获取当前操作人机构
	 * @param context
	 * @return
	 * @throws ComponentException
	 */
	public String getLoginOrgId(Context context) throws ComponentException {
		String loginorgid = null; //当前编辑用户
		try {
			loginorgid = (String)(context.getDataValue(PUBConstant.loginorgid));
		} catch (Exception e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取当前操作人机构失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的当前操作人机构代码失败！请重新操作！");
		}
		return loginorgid;
	}
	/**
	 * 获取当前操作人
	 * @param context
	 * @return
	 * @throws ComponentException
	 */
	public String getUsrId(Context context) throws ComponentException {
		String tmp_currentUserId = null; //当前编辑用户
		try {
			tmp_currentUserId = (String)(context.getDataValue("currentUserId"));
		} catch (Exception e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取当前操作人失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您当前操作人的代码失败！请重新操作！");
		}
		return tmp_currentUserId;
	}
	/**
	 * 获取当前登录用户的机构ID
	 * @return
	 * @throws ComponentException
	 */
	public String getUsrBchId(Context context) throws ComponentException {
		String tmp_orgid =  null; //机构代码
		try {
			tmp_orgid = (String)(context.getDataValue("organNo"));
		} catch (Exception e) {
		
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取机构代码失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的机构代码失败！请重新操作！");
		}
		return tmp_orgid;
	}
	/**
	 * 取法人机构代码
	 * @return
	 * @throws ComponentException
	 */
	public String getUsrArtiId(Context context) throws ComponentException {
		String tmp_orgid =  null; //新增方法，取法人机构代码
		try {
			tmp_orgid = (String)(context.getDataValue("ARTI_ORGANNO"));
		} catch (Exception e) {
		
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取客户法人机构代码失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取客户机构代码失败！请重新操作！");
		}
		return tmp_orgid;
	}
}
