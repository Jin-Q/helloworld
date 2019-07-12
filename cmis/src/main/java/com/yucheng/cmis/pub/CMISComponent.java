
package com.yucheng.cmis.pub;



import java.sql.Connection;
import java.util.HashMap;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.dbmodel.EMPRestrictException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.BusinessRestrictException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * 
 * @Classname com.yucheng.cmis.pub.CMISComponent.java
 * @author wqgang
 * @Since 2008-9-24 上午09:21:18 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class CMISComponent {
	
	private String id;
	private String describe;
	private Context context;
	private Connection connection;
	
	private HashMap<String,String> configTable = new HashMap<String,String>();
	/**
	 * 当期用户机构代码
	 */
	
	private String usrBchId; 
	/**
	 * 当期用户代码
	 */
	private String usrId;

	
	/**
	 * 当期系统时间戳的样式 YYYY-MM-DD@hh:mm:ss.mmm
	 */
	private String curTimestamp;
	
	/**
	 * 当期系统日期 YYYY-MM-DD
	 */
	private String curDate;
	/**
	 * 系统营业日期 YYYY-MM-DD
	 */
	private String openDay;
	
	public String getLoginUserId() throws ComponentException {
		String loginuserid =  null; //机构代码
		try {
			loginuserid = (String)(context.getDataValue(PUBConstant.loginuserid));
		} catch (Exception e) {
		
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取当前操作人失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的当前操作人失败！请重新操作！");
		}
		return loginuserid;
	}
	
	public String getUsrBchId() throws ComponentException {
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
	public String getUsrArtiId() throws ComponentException {
		String tmp_orgid =  null; //新增方法，取法人机构代码
		try {
			tmp_orgid = (String)(context.getDataValue("ARTI_ORGANNO"));
		} catch (Exception e) {
		
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取机构代码失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的机构代码失败！请重新操作！");
		}
		return tmp_orgid;
	}
	public void setUsrBchId(String usrBchId) {
		this.usrBchId = usrBchId;
	}
  
	public String getOpenDay() throws ComponentException{
		try {
			return this.openDay=(String) context.getDataValue(PUBConstant.OPENDAY);
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取营业时间失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"获取营业失败！请重新操作！");
		
		}
	}
	
	public String getUsrId() throws ComponentException {
		String tmp_currentUserId = null; //当前编辑用户
		try {
			tmp_currentUserId = (String)(context.getDataValue("currentUserId"));
		} catch (Exception e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取机构代码失败！");
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的代码失败！请重新操作！");
		}
		return tmp_currentUserId;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getDescribe(){
		return this.describe;
	}
	
	public void setDescribe(String describe){
		this.describe = describe;
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	

	public Context getContext() {
		return context;
	}
	/**
	 * 同以组件间调用生成相应组件对象方法
	 * @param 
	 * @return 
	 * @exception 
	 * @param comId
	 * @return
	 */
	public CMISComponent getComponent(String comId){
		CMISComponent component=null;
		try {
			component = CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(comId,this.context,false,this.connection);
			//component.setContext(this.getContext());
			component.setConnection(this.connection);
		} catch (ComponentException e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "组件内调用组件对象生成发生异常\n"+e.toString());
		}
		
		return component;
		
	}
	
	/**
	 * 利用类名获取组建类
	 * @param className 类名
	 * @return component
	 */
	public CMISComponent getComponentByClassName(String className){
		
		CMISComponent component=null;
		try {
			component = CMISComponentFactory.getComponentFactoryInstance().getComponentByClassName(className,context,this.connection);
			component.setConnection(this.connection);
		} catch (ComponentException e) {

			e.printStackTrace();
			//EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "实例化组建发生异常\n"+e.toString());
		}
		return component;
		
	}
	/**
	 * 获取接口（引用原来的连接）
	 * @param 接口名 类名
	 * @return component
	 * @throws ComponentException 
	 */
	public CMISComponent getInterfaceWithOldCon(String interfaceId) throws ComponentException{
		
		
		CMISComponent interf = CMISComponentFactory.getComponentFactoryInstance().getComponentInterfaceWithOldConnection(interfaceId, context,this.connection);
		interf.setConnection(this.connection);
		
		return interf;
		
	}

	/**
	 * <p>设置业务组件配置参数</p>
	 * <p>其在通过业务组件工厂实例化组件时获得</p>
	 * <p>注：本方法不用于设置单个配置数，且该方法一般情况下，只用于组件工厂</p>
	 * @param configs 配置参数列表
	 */
	public void setParameter(HashMap<String,String> configs){
		
		this.configTable = configs;
	}
	
	/**
	 * <p>由组件配置参数名获取业务组件的配置信息</p>
	 * <p>若组件没有设置configTable，或对应参数名在configTable中不存在，则返回缺省值</p>
	 * @param parameterid 配置参数名
	 * @param defvalue    缺省值
	 * @return 配置参数对应的值（注，统一以String类型返回）
	 */
	public String getParameter(String parameterid, String defvalue) {
		String st_val = null;
		if(this.configTable != null){
			st_val = (String)this.configTable.get(parameterid);
		}else{
			st_val = null;
		}
		if(st_val == null){
			st_val = defvalue; //从配置表中取数为空则使用缺省值
		}
		return st_val;
	}
	
	/**
	 * <p>由组件配置参数名获取业务组件的配置信息</p>
	 * <p>若组件没有设置configTable，或对应参数名在configTable中不存在，则返回空</p>
	 * @param parameterid 配置参数名
	 * @return 配置参数对应的值（注，统一以String类型返回）
	 */
	public String getParameter(String parameterid){
		return this.getParameter(parameterid, null);
	}
	
	protected Connection getConnection() throws ComponentException {
		/**
		DataSource dataSource = null;
		
		if(dataSource == null)
			dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);		
		if (dataSource == null){
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "在Context中数据源为空");
			throw new ComponentException(CMISMessage.DATASOURCENULLERROR,"在Context中数据源为空");
		}
		
		try {
			this.connection = ConnectionManager.getConnection(dataSource);
		} catch (GetConnectionFailedException e) {
		
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取数据源发生异常\n"+e.toString());
			throw new ComponentException(CMISMessage.DATASOURCEERROR,"取数据源发生异常");
		}	
		*/	
		if(connection == null){
			throw new ComponentException(CMISMessage.DATASOURCEERROR,"数据库连接为空");
		}
		return connection;
	}
			
	protected RecordRestrict getRecordRestrict()throws ComponentException{
		
		RecordRestrict recordRestrict = (RecordRestrict)this.context.getService(CMISConstance.ATTR_RECORDRESTRICT);
		return recordRestrict;		
	}
	
	protected RecordRestrict getRecordRestrict(String serviceId)throws ComponentException{		
		RecordRestrict recordRestrict = (RecordRestrict)this.context.getService(serviceId);
		return recordRestrict;		
	}
	
	public void judgeDeleteRestrict(String modelId) 
	       throws BusinessRestrictException ,ComponentException{		
		RecordRestrict recordRestrict = this.getRecordRestrict();
		try {
			recordRestrict.judgeDeleteRestrict(modelId, this.context, this.connection);
		} catch (EMPRestrictException e) {
			
			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行删除操作");
		}
	}
	
	public void judgeUpdateRestrict(String modelId)
	       throws BusinessRestrictException, ComponentException{
		RecordRestrict recordRestrict = this.getRecordRestrict();
		try {
			recordRestrict.judgeUpdateRestrict(modelId, this.context, this.connection);
		} catch (EMPRestrictException e) {

			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行更新操作");
		}
	}
	
	public void judgeViewRestrict(String modelId,String condition)
	       throws BusinessRestrictException, ComponentException{
		RecordRestrict recordRestrict = this.getRecordRestrict();
		try {
			recordRestrict.judgeQueryRestrict(modelId, condition, this.context, this.connection);
		} catch (EMPRestrictException e) {
			
			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行更新操作");
		}
	} 
	
	public void judgeDeleteRestrict(String modelId,String serviceId) 
	       throws BusinessRestrictException, ComponentException{		
		RecordRestrict recordRestrict = this.getRecordRestrict(serviceId);
		try {
			recordRestrict.judgeDeleteRestrict(modelId, this.context, this.connection);
		} catch (EMPRestrictException e) {
			
			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行更新操作");
		}
	}
	
	public void judgeUpdateRestrict(String modelId,String serviceId)
	       throws BusinessRestrictException, ComponentException{
		RecordRestrict recordRestrict = this.getRecordRestrict(serviceId);
		try {
			recordRestrict.judgeUpdateRestrict(modelId, this.context, this.connection);
		} catch (EMPRestrictException e) {
			
			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行更新操作");
		}
	}
	
	public void judgeViewRestrict(String modelId,String condition,String serviceId)
	        throws BusinessRestrictException, ComponentException{
		RecordRestrict recordRestrict = this.getRecordRestrict(serviceId);
		try {
			
			recordRestrict.judgeQueryRestrict(modelId, condition, this.context, this.connection);
		} catch (EMPRestrictException e) {
			
			e.printStackTrace();
			throw new BusinessRestrictException("在业务组件[" + this.id + "]中无权对[" + modelId+ "]执行更新操作");
		}
	} 

	
	public CMISAgent getAgentInstance(String agentId)throws AgentException{
		/**
		 * @todo 异常类还需要定义
		 */
	   CMISAgent agent = CMISAgentFactory.getAgentFactoryInstance().getAgentInstance(agentId);
	   /** 设置基本信息*/
	   agent.setContext(this.context);
	   agent.setConnection(this.connection);
	   
	   return agent;
	}

	public CMISComponent getComponentInterface(String interfaceId)throws ComponentException{
		/**
		 * @todo 异常类还需要定义
		 */
		CMISComponent interf = CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(interfaceId, context,false,this.connection);
	   /** 设置基本信息
		interf.setContext(this.context);
		interf.setConnection(this.connection);
	   */
		interf.setConnection(this.connection);	
	   return interf;
	}
	
	public CMISComponent getOtherComponentInstance(String componentId)throws ComponentException{
		
		CMISComponent cmp = CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(componentId, context,false,this.getConnection());
	   cmp.setConnection(this.connection);
	   return cmp;
	}
	
	public CMISComponent getOtherComponentInstanceWithOldCon(String componentId)throws ComponentException{
		
		CMISComponent cmp = CMISComponentFactory.getComponentFactoryInstance().getComponentInstanceWithOldCon(componentId, context,this.connection);
		//this.releaseConnection(cmp.getConnection());
		cmp.setConnection(this.connection);
	   return cmp;
	}
	
	
	public String getCurDate() {
		return TimeUtil.getInstance().getCurDate();
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getCurTimestamp() {
		return TimeUtil.getInstance().getCurTimeStamp();
	}

	/**
	 * 释放数据库连接便于子类释放连接资源，此函数屏蔽错误抛出， 原因：此函数一般在finally中调用抛出异常一般都不处理
	 * 
	 * @param dataSource
	 *            数据库服务的数据源
	 * @param connection
	 */
	protected void releaseConnection(Connection connection) {
		// 读取默认的数据源
		DataSource dataSource;
		dataSource = (DataSource) this.context
				.getService(CMISConstance.ATTR_DATASOURCE);

		try {

			// 释放连接
			ConnectionManager.releaseConnection(dataSource, connection);
		} catch (EMPJDBCException e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0,
					"Do release the connection from data source: " + dataSource
							+ " failed!");
		}
		// 写日志
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0,
				"Do release the connection from data source: " + dataSource
						+ " success!");

	}
	
	public void setCurTimestamp(String curTimestamp) {
		this.curTimestamp = curTimestamp;
	}
	
	protected Connection getConnectionFromContext() throws ComponentException {
		try {
			DataSource dataSource = null;
			if(context == null){
				throw new EMPJDBCException("系统尚未初始化，没有得到Context");
			}
			if(dataSource == null)
				dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);		
			if (dataSource == null)
				throw new EMPJDBCException("dataSource is null in :"
						+ this.toString());
			
			this.connection = ConnectionManager.getConnection(dataSource);		
			return connection;
		} catch (EMPJDBCException e) {
			
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, e.toString());
			throw new ComponentException(CMISMessage.CONNECTIONERROR,"获取数据库连接失败");
		}
		
	}
	
	public CMISDao getDaoInstance(String agentId) throws ComponentException {

		CMISDao dao = CMISDaoFactory.getDaoFactoryInstance().getDaoInstance(agentId);
		dao.setContext(this.context);
		dao.setConnection(this.connection);

		return dao;
	}
	protected TableModelDAO getTableModelDAO() {

		return (TableModelDAO) this.context.getService(CMISConstance.ATTR_TABLEMODELDAO);
	}
}

