package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname com.yucheng.cmis.pub.CMISAgent.java
 * @author wqgang
 * @Since 2009-9-24 上午08:53:01
 * @Copyright yuchengtech
 * @version 1.0
 */

public class CMISAgent {
	/**
	 * 代理ID
	 */
	private String id;
	/**
	 * 代理描述
	 */
	private String describe;
	/**
	 * EMP Context 由OP生成
	 */
	private Context context;
	/**
	 * 连接实例，从context中获取，并保持当前唯一
	 */
	private Connection connection;

	/**
	 * 获取代理ID
	 * 
	 * @return 代理ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置代理ID
	 * 
	 * @param id
	 *            代理ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取代理描述
	 * 
	 * @return 代理描述
	 */
	public String getDescribe() {
		return this.describe;
	}

	/**
	 * 设置代理描述
	 * 
	 * @param describe
	 *            代理描述
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/**
	 * 获取连接
	 * @return 连接
	 */
	protected Context getContext() {
		return this.context;
	}

	/**
	 * 设置context
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 设置连接
	 * @param connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 从context中获取连接
	 * @return 连接
	 * @throws AgentException agent异常
	 */
	protected Connection getConnectionFromContext() throws AgentException {
		try {
			DataSource dataSource = null;
			if (context == null) {
				throw new EMPJDBCException("系统尚未初始化，没有得到Context");
			}
			if (dataSource == null)
				dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			if (dataSource == null)
				throw new EMPJDBCException("dataSource is null in :"+ this.toString());

			this.connection = ConnectionManager.getConnection(dataSource);
			return connection;
		} catch (EMPJDBCException e) {

			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, e
					.toString());
			throw new AgentException(CMISMessage.CONNECTIONERROR, "获取数据库连接失败");
		}

	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * 获取表模型DAO对象
	 * @return 表模型DAO对象
	 */
	protected TableModelDAO getTableModelDAO() {

		return (TableModelDAO) this.context.getService(CMISConstance.ATTR_TABLEMODELDAO);
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
		dataSource = (DataSource) this.context.getService(CMISConstance.ATTR_DATASOURCE);

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

	/**
	 * 通用的新增操作，提供给子类的新增信息的方法
	 * 
	 * @param domain
	 *            具体的VO对象
	 * @return 1 成功 其他失败
	 * @throws EMPException
	 */
	public int insertCMISDomain(CMISDomain domain, String modeId)
			throws AgentException {
		int i_flag = 0; // 1 成功 其他失败
		Connection conn = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			/*
			 * 把对象转换成KeyedCollection结构数据插入数据库表
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			KeyedCollection kCol = componetHelper.domain2kcol(domain, modeId);
			int count = tDao.insert(kCol, conn);// 1成功 其他失败

			i_flag = count;

		} catch (Exception e) {
			System.out.println("add sys log error"+e.toString());
			e.printStackTrace();
			
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "新增失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.ADDERROR, "新增失败");
		}
		return i_flag;

	}
	
	/**
	 * 通用的修改操作，提供给子类的修改信息的方法
	 * 
	 * @param domain
	 *            具体的VO对象
	 * @return 1 成功 其他失败
	 * @throws AgentException
	 */
	public int modifyCMISDomain(CMISDomain domain, String modeId) throws AgentException {
		int i_flag = 0; // 1 成功 其他失败
		Connection conn = null;
		try {
			conn = this.getConnection();
			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();
			/*
			 * 把对象转换成KeyedCollection结构数据更新数据库表中数据
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			KeyedCollection kCol = componetHelper.domain2kcol(domain, modeId);
			int count = tDao.update(kCol, conn);// 1成功 其他失败

			i_flag = count;

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "修改失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.UPDATEERROR, "修改失败");
		}

		return i_flag;
	}

	/**
	 * 通用的删除操作，提供给子类的删除信息的方法。
	 * <p>
	 * 注意对接的表中的每个主要都要设置
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param pk_values
	 *            多个主键 map 结构（"主键名","主键值"） (不是表的主键，所设的值无效)
	 * @return 1 成功 其他失败
	 * @throws AgentException
	 */
	public int removeCMISDomainByKeywords(String modelId, Map<String,String> pkValues)
			throws AgentException {
		int i_flag = 0; // 1 成功 其他失败
		Connection conn = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 删除信息
			int count = tDao.deleteByPks(modelId, pkValues, conn); // 1成功 其他失败

			i_flag = count;

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "删除失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.DELETEERROR, "删除失败");
		}

		return i_flag;
	}

	/**
	 *通用的删除操作，提供给子类的删除信息的方法。
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param keyword
	 *            唯一主键
	 * @return 1 成功 其他失败
	 * @throws AgentException
	 */
	public int removeCMISDomainByKeyword(String modelId, String keyword)
			throws AgentException {
		int iFlag = 0; // 1 成功 其他失败
		Connection conn = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 删除信息
			int count = tDao.deleteByPk(modelId, keyword, conn); // 1成功 其他失败

			iFlag = count;

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "删除失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.DELETEERROR, "删除失败");
		}

		return iFlag;
	}

	/*
	 * 列表查询public IndexedCollection queryList (String modelId, Connecion
	 * connection)throws EMPJDBCException
	 * 功能描述：根据表模型的名称，查询列表数据，返回所有列的值。无论是单表模型，主从表还是主子表，此方法只对主表进行列表查询操作。  参数说明：
	 * modelId: 要执行删除操作的表模型的id。 connection： 数据库连接。 
	 * 返回值：查询结果封装的IndexedCollection。 异常：EMPJDBCException。
	 */

	/**
	 * 通用的但表查询操作，提供给子类的查询信息的方法
	 * <p>
	 * 注意对接的表中的每个主要都要设置
	 * 
	 * @param modelId
	 *            表模型ID
	 * @return 相应的查询对象list
	 * @throws AgentException
	 */
	public List<CMISDomain> findCMISDomainList4one(CMISDomain domain,
			String modelId) throws AgentException {
		Connection conn = null;
		List<CMISDomain> rtList = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 查询数据
			IndexedCollection iCol = tDao.queryList(modelId, conn);

			if (iCol.isEmpty()) {
				return null;
			}

			/*
			 * 把查询结果中的kCol数据结构转换成对象
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			rtList = componetHelper.icol2domainList4One(domain, iCol);

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
		}
		return rtList;
	}

	/*
	 * public IndexedCollection queryList (String modelId, List
	 * columnNames，Connecion connection) throws EMPJDBCException
	 * 
	 * 功能描述：根据表模型的名称，查询列表数据，返回指定列的值。  单表模型下：查询由columnNames决定的列的值。 
	 * 主从表模型下：查询由columnNames决定的列的值，如果要查从表字段的值，
	 * columnNames中的字段名称前应加上从表表模型名称，如：从表名称.从表字段。  主子表模型下：只查询主表指定列的数据。  参数说明： 
	 * modelId: 要执行删除操作的表模型的id。  columnNames: 指定要查询的列在表模型中的字段id。 
	 * connection：数据库连接。  返回值 ：查询结果封装的IndexedCollection。  异常：EMPJDBCException。
	 */
	/**
	 * 通用的但表查询操作，提供给子类的查询信息的方法
	 * <p>
	 * 注意对接的表中的每个主要都要设置
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param columnNames
	 *            : 指定要查询的列在表模型中的字段id。 
	 * @return 相应的查询对象list
	 * @throws AgentException
	 */
	public List<CMISDomain> findCMISDomainList4one(CMISDomain domain,
			String modelId, List<String> columnNames) throws AgentException {
		Connection conn = null;
		List<CMISDomain> rtList = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 查询数据
			IndexedCollection iCol = tDao.queryList(modelId, columnNames, conn);

			if (iCol.isEmpty()) {
				return null;
			}

			/*
			 * 把查询结果中的kCol数据结构转换成对象
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			rtList = componetHelper.icol2domainList4One(domain, iCol);

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
		}

		return rtList;
	}

	/*
	 * public IndexedCollection queryList (String modelId, String condition,
	 * Connecion connection) throws EMPJDBCException
	 * 
	 * 
	 * 功能描述：根据表模型的名称以及传入条件，查询主表列表数据，返回所有列的值。无论是单表模型，主从表还是主子表，此方法只对主表进行条件列表查询操作。
	 *  参数说明：  modelId: 要执行删除操作的表模型的id。  condition:
	 * 列表查询的条件，需要符合sql语句的格式，如：“where id=’123’”,”order by id
	 * desc”，并且只能以主表字段作为查询条件。  connection： 数据库连接。  返回值
	 * ：查询结果封装的IndexedCollection。  异常：EMPJDBCException。
	 */

	/*
	 * public IndexedCollection queryList (String modelId, List
	 * columnNames，String condition,Connecion connection) throws
	 * EMPJDBCException
	 * 
	 *  功能描述：根据表模型的名称以及传入条件，查询列表数据，返回指定列的值。  单表模型下：条件查询由columnNames决定的列的值。 
	 * 主从表模型下：条件查询由columnNames决定的列的值，如果要查从表字段的值，
	 * columnNames中的字段名称前应加上从表表模型名称，如：从表名称.从表字段。  主子表模型下：条件查询主表指定列的数据。  参数说明：
	 *  modelId: 要执行删除操作的表模型的id。  columnNames: 指定要查询的列在表模型中的字段id。  condition:
	 * 列表查询的条件，需要符合sql语句的格式，如：“where id=’123’”,”order by id
	 * desc”，并且只能以主表字段作为查询条件。  connection： 数据库连接。  返回值
	 * ：查询结果封装的IndexedCollection。  异常：EMPJDBCException。
	 * 
	 * public IndexedCollection queryList(String modelId, List columnNames,
	 * String condition, PageInfo pageInfo,Connection connection) throws
	 * EMPJDBCException
	 * 
	 *  功能描述：根据表模型的名称以及传入条件，查询列表数据，返回指定列的值，具有翻页功能。 
	 * 单表模型下：条件查询由columnNames决定的列的值。  主从表模型下：条件查询由columnNames决定的列的值，如果要查从表字段的值，
	 * columnNames中的字段名称前应加上从表表模型名称，如：从表名称.从表字段。  主子表模型下：条件查询主表指定列的数据。  参数说明：
	 *  modelId: 要执行删除操作的表模型的id。  columnNames: 指定要查询的列在表模型中的字段id。  condition:
	 * 列表查询的条件，需要符合sql语句的格式，如：“where id=’123’”,”order by id
	 * desc”，并且只能以主表字段作为查询条件。  PageInfo:
	 * 翻页信息的封装类，通过set方法指定页码PageIdx，一页最大显示数PageSize。  connection： 数据库连接。  返回值
	 * ：查询结果封装的IndexedCollection。  异常：EMPJDBCException。
	 * 
	 *  详情查询 public KeyedCollection queryDetail(String modelId, String
	 * pk_value, Connection con) throws EMPJDBCException
	 * 
	 *  功能描述：详情查询指定表模型主表中的单条数据。无论是单表模型，主从表还是主子表，此方法只对主表进行详情查询操作。  参数说明： 
	 * modelId: 要执行删除操作的表模型的id。  pk_value: 主表主键字段的值。  connection： 数据库连接。  返回值
	 * ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 * 
	 * public KeyedCollection queryDetail(String modelId, Map pks, Connection
	 * con) throws EMPJDBCException
	 * 
	 *  功能描述：根据联合主键，详情查询指定表模型主表中的单条数据。无论是单表模型，主从表还是主子表，此方法只对主表进行详情查询操作。  参数说明：
	 *  modelId: 要执行删除操作的表模型的id。  pks: 主表联合主键的值。  connection： 数据库连接。  返回值
	 * ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 * 
	 * public KeyedCollection queryAllDetail(String modelId, String pk_value,
	 * Connection con) throws EMPJDBCException
	 * 
	 *  功能描述：根据主键值详情查询指定表模型中的数据。  单表模型下：查询由主键值确定的单条数据。 
	 * 主从表模型下：查询主表的单条数据，并且查询所有从表中的单条关联数据。  主子表模型下：查询主表的单条数据，并且查询所有子表中的多条关联数据。 
	 * 参数说明：  modelId: 要执行删除操作的表模型的id。  pk_value: 主表主键字段的值。  connection：
	 * 数据库连接。  返回值 ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 * 
	 * public KeyedCollection queryAllDetail(String modelId, Map pks, Connection
	 * con) throws EMPJDBCException
	 * 
	 *  功能描述：根据联合主键值详情查询指定表模型中的数据。  单表模型下：查询由主键值确定的单条数据。 
	 * 主从表模型下：查询主表的单条数据，并且查询所有从表中的单条关联数据。  主子表模型下：查询主表的单条数据，并且查询所有子表中的多条关联数据。 
	 * 参数说明：  modelId: 要执行删除操作的表模型的id。  pks: 主表联合主键的值。  connection： 数据库连接。 
	 * 返回值 ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 * 
	 * public KeyedCollection queryAllDetail(String modelId, String
	 * pk_value,List tableNames, Connection con) throws EMPJDBCException
	 * 
	 *  功能描述：根据主键查询主表的明细及部分从、子表的数据  单表模型下：此时tableNames为空，顾不推荐使用此方法。 
	 * 主从表模型下：查询主表明细，并根据tableNames指定的从表表模型名称查询部分从表中的关联数据。 
	 * 主子表模型下：查询主表明细，并且根据tableNames指定的子表表模型名称查询部分子表中的多条关联数据。  参数说明：  modelId:
	 * 要执行删除操作的表模型的id。  pk_value: 主表主键的值。  tableNames: 需要查询的从表和子表表模型名称的集合 
	 * connection： 数据库连接。  返回值 ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 * 
	 * public KeyedCollection queryAllDetail(String modelId, Map pks,List
	 * tableNames, Connection con) throws EMPJDBCException
	 * 
	 *  功能描述：根据联合主键查询主表的明细及部分从、子表的数据  单表模型下：此时tableNames为空，顾不推荐使用此方法。 
	 * 主从表模型下：查询主表明细，并根据tableNames指定的从表表模型名称查询部分从表中的关联数据。 
	 * 主子表模型下：查询主表明细，并且根据tableNames指定的子表表模型名称查询部分子表中的多条关联数据。  参数说明：  modelId:
	 * 要执行删除操作的表模型的id。  pks: 主表中联合主键的值。  tableNames: 需要查询的从表和子表表模型名称的集合 
	 * connection： 数据库连接。  返回值 ：查询结果封装的KeyedCollection。  异常：EMPJDBCException。
	 */

	/**
	 * 通用的查询操作，提供给子类的查询信息的方法
	 * <p>
	 * 注意对接的表中的每个主要都要设置
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param pk_values
	 *            多个主键 map 结构（"主键名","主键值"）(不是表的主键，所设的值无效)
	 * @return 相应的查询对象
	 * @throws AgentException
	 */
	public CMISDomain findCMISDomainByKeywords(CMISDomain domain,
			String modelId, Map<String,String> pk_values) throws AgentException {
		Connection conn = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 查询数据
			KeyedCollection kCol = tDao.queryAllDetail(modelId, pk_values, conn);

			/*
			 * 把查询结果中的kCol数据结构转换成对象
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			domain = componetHelper.kcolTOdomain(domain, kCol);

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
		}

		return domain;
	}

	/**
	 * 通用的查询操作，提供给子类的新增信息的方法
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param keyword
	 *            唯一主键
	 * @return 暂定返回null 表示成功 其他表示异常
	 * @throws AgentException
	 */
	public CMISDomain findCMISDomainByKeyword(CMISDomain domain,
			String modelId, String keyword) throws AgentException {
		Connection conn = null;

		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 查询数据
			KeyedCollection kCol = tDao.queryAllDetail(modelId, keyword, conn);

			/*
			 * 把查询结果中的kCol数据结构转换成对象
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			domain = componetHelper.kcolTOdomain(domain, kCol);

		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
		}

		return domain;
	}
	
	/**
         * 根据Domain类型和查询条件，查询获得CMISDomain的List
         * @param domainClass
         * @param condition 查询条件，如 where column_1 = '11' and column_2 in ('1','2')
         * @return List<CMISDomain>
         * @throws ComponentException
         */
        public List<CMISDomain> findListByCondition(Class<? extends CMISDomain> domainClass, String condition) 
            throws ComponentException{
            try {
                TableModelDAO tDao = this.getTableModelDAO();
                
                IndexedCollection iCol = tDao.queryList(domainClass.getSimpleName(),
                        condition, this.getConnection());
            
                if (iCol.isEmpty()) {
                    return null;
                }            
                
                ComponentHelper componetHelper = new ComponentHelper();
                return componetHelper.icol2domainlist(domainClass.getName(), iCol);
            } catch (Exception e) {
                throw new ComponentException(e);
            }
        }

        
        /**
         * 根据modelId和查询条件，查询获得CMISDomain的List
         * @param modelId :表模型ID domain 表模型对应domain的类
         * @param condition 查询条件，如 where column_1 = '11' and column_2 in ('1','2')
         * @return List<CMISDomain>
         * @throws ComponentException
         */
        public List<CMISDomain> findListByIdCondi(String modelId, CMISDomain domain, String condition) 
            throws ComponentException{
            try {
                TableModelDAO tDao = this.getTableModelDAO();
                
                IndexedCollection iCol = tDao.queryList(modelId,
                        condition, this.getConnection());
            
                if (iCol.isEmpty()) {
                    return null;
                }            
                
                ComponentHelper componetHelper = new ComponentHelper();
                return componetHelper.icol2domainlist(domain, iCol);
            } catch (Exception e) {
                throw new ComponentException(e);
            }
        }
        
	/**
	 * 获取自定义的DAO的实例
	 * 
	 * @param agentId
	 *            代理ID
	 * @return CMISDao实例
	 * @throws ComponentException
	 *             组件异常
	 */
	public CMISDao getDaoInstance(String agentId) throws ComponentException {

		CMISDao dao = CMISDaoFactory.getDaoFactoryInstance().getDaoInstance(agentId);
		dao.setContext(this.context);
		dao.setConnection(this.connection);

		return dao;
	}
	
	public String getOpenDay() throws AgentException{
		try {
			return (String) context.getDataValue(PUBConstant.OPENDAY);
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取营业时间失败！");
			throw new AgentException(CMISMessage.MESSAGEDEFAULT,"获取营业失败！请重新操作！");
		
		}
	}	
	public int updateByKcoll(KeyedCollection kColl) throws ComponentException{
		TableModelDAO tableModelDAO=this.getTableModelDAO();
		int count=0;
		try {
			count=tableModelDAO.update(kColl, this.connection);
		} catch (EMPJDBCException e) {
			// TODO Auto-generated catch block
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, e.getMessage());
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据传入的sql返回 一条或者零条数据
	 * 
	 * 例如 传入 select id,name from user where name='jack'
	 * 返回   一个有结果map 里面封装了 jack 的 id  和 name 
	 * @param sql  查询sql
	 * @return 所有返回的查询字段 都是小写 
	 * @throws AgentException
	 */
	protected HashMap<String,String> queryDataOfMapByCondition(String sql) throws AgentException {
		try{
			CMISDao cmisDao = new CMISDao();
			cmisDao.setConnection(this.getConnection());
			return cmisDao.queryDataOfMapByCondition(sql);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 根据传入的sql 返回一个记录列表 里面可能有 一条或者多条记录 或者无记录存在
	 * 
	 * 例如 传入 select id,name from user where name='jack'
	 * 返回   一个list 里面包含 所有叫jack的人的 id,nam map信息
	 * @param sql 查询sql
	 * @return 所有返回的查询字段 都是小写 
	 * @throws AgentException
	 */
	protected List<HashMap<String,String>>queryDataOfMapListByCondition(String sql) throws AgentException{
		try{
			CMISDao cmisDao = this.getDaoInstance("");
			cmisDao.setConnection(this.getConnection());
			return cmisDao.queryDataOfMapListByCondition(sql);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 根据传入的表名 和该表的字段列表 返回查询字段的结果数据
	 * @param tableName 表名 如s_dic
	 * @param queryCol	需要查询的列名称  如 cnname,enname 两个字段之间用','分隔
	 * @param condition 条件 如 cnname='jack'
	 * @return 所有返回的查询字段 都是小写 
	 * @throws AgentException
	 */
	protected HashMap<String,String> queryDataOfMapByCondition(String tableName,String queryCol,String condition) throws AgentException{
		try{
			CMISDao cmisDao = new CMISDao();
			cmisDao.setConnection(this.getConnection());
			return cmisDao.queryDataOfMapByCondition(tableName, queryCol, condition);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	} 
	

	/**
	 * 对 更新、删除、新增 操作进行托管 执行传入语句
	 * @param sql 查询sql
	 * @return 1---成功 2---失败
	 * @throws AgentException
	 */
	protected String executeSql(String sql)throws AgentException{
		try{
			CMISDao cmisDao = new CMISDao();
			cmisDao.setConnection(this.getConnection());
			return cmisDao.executeSql(sql);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	

}
