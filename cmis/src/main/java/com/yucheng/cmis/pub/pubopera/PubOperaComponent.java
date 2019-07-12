package com.yucheng.cmis.pub.pubopera;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class PubOperaComponent extends CMISComponent {
	/**
	 * 通过domain和keyword返回domain的值
	 * @param domain
	 * @param keyword
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeyword(CMISDomain domain,String keyword) throws ComponentException{
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.getCMISDomainByKeyword(domain,keyword);
	}
	
	/**
	 * 通过domain和keywords返回domain的值
	 * @param domain
	 * @param keywords
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeywords(CMISDomain domain,Map<String,String> keywords) throws ComponentException{
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.getCMISDomainByKeywords(domain,keywords);
	}

	/**
	 * 根据domain和条件，返回domain的list
	 * @param domainClass
	 * @param condition
	 * @return
	 * @throws ComponentException
	 */
	public List<CMISDomain> getCMISDomainByCondition(
			Class<? extends CMISDomain> domainClass, String condition) throws ComponentException{
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.getCMISDomainByCondition(domainClass,condition);
	}

	/**
	 * 更新domain
	 * @param domain
	 * @return
	 * @throws ComponentException
	 */
	public String updateCMISDomain(CMISDomain domain) throws ComponentException {
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.updateCMISDomain(domain);
	}
	
	/**
	 * 写入domain
	 * @param domain
	 * @return
	 * @throws ComponentException
	 */
	public String insertCMISDomain(CMISDomain domain) throws ComponentException {
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.insertCMISDomain(domain);
	}

	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param condition
	 * @return
	 * @throws EMPException 
	 */
	public int deleteDateByTableAndCondition(String tableName, String condition) throws EMPException {
		// TODO Auto-generated method stub
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.deleteDateByTableAndCondition(tableName,condition);
	}

	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param conditionMap 
	 * @return
	 * @throws EMPException
	 */
	public int deleteDateByTableAndConditionMap(String tableName,Map<String,String> conditionMap) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.deleteDateByTableAndConditionMap(tableName,conditionMap);
	}
	
	/**
	 * 根据表名和需要更新的值和条件更新表的数据
	 * @param tableName				表名
	 * @param valueMap				需要更新的字段和值
	 * @param conditionMap    		条件字段和值
	 * @return
	 * @throws EMPException
	 */
	public int updateDateByTableAndValueAndCondition(String tableName,HashMap<String,String> valueMap,
				HashMap<String,String> conditionMap) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.updateDateByTableAndValueAndCondition(tableName,valueMap,conditionMap);
	}
	
	/**
	 * 根据表名和条件查询指定的字段值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段		
	 * @param conditionMap    		条件字段和值
	 * @return
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByTableAndCondition(String tableName,String retValues,
			HashMap<String,String> conditionMap) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.selectValuesByTableAndCondition(tableName,retValues,conditionMap);
	}
	
	/**
	 * 根据表名和主键条件查询指定的字段值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段		
	 * @param conditionMap    		主键和值
	 * @return
	 * @throws EMPException
	 */
	public KeyedCollection selectValuesByTableAndKeyWords(String tableName,String retValues,
			HashMap<String,String> conditionMap) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.selectValuesByTableAndKeyWords(tableName, retValues, conditionMap);
	}

	/**
	 * 根据表名和条件查询指定的字段值
	 * 
	 * @param tableName
	 *            表名
	 * @param retValues
	 *            需要返回的字段
	 * @param condition
	 *            查询条件
	 * @return IndexedCollection 返回值的iColl
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByCondition(String tableName,
			String colName, String condition, Connection connection) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.selectValuesByCondition(tableName, colName, condition,connection);
	}
	
	/**
	 * 根据表名和条件查询指定的字段值，只能返回一个值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段	    	
	 * @param condition    			查询条件
	 * @return  IndexedCollection   返回值的iColl
	 * @throws EMPException
	 */
	public String selectSingleValueByCondition(String tableName,String retValues,String condition) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.selectSingleValueByCondition(tableName,retValues,condition);
	}
	
	/**
	 * 通过查询条件分页查询
	 * @param connection
	 * @param conditionStr
	 * @param pageInfo
	 * @return IndexedCollection
	 * @throws SQLException,InvalidArgumentException,DuplicatedDataNameException
	 **/
	public IndexedCollection queryByPageInfo(Connection connection,
			String conditionStr, PageInfo pageInfo) throws EMPException{
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.queryByPageInfo(connection, conditionStr, pageInfo);
	}
	
	/**
	 * 向表中插入一条记录
	 * @param tableName	表名
	 * @param valueMap	需要插入的字段MAP
	 * @return boolean
	 * @throws EMPException
	 */
	public boolean insertValuesByTableAndFieldMap(String tableName,
			HashMap<String, String> valueMap) throws EMPException {
		PubOperaAgent pubOperaAgent = (PubOperaAgent)this.getAgentInstance(PUBConstant.PUBOPERA);
		return pubOperaAgent.insertValuesByTableAndFieldMap(tableName, valueMap);
	}
	
	/**
	 * 根据传入的脚本，获得结果集的总行数
	 * @param sqlStr
	 * @return
	 */
	public int countRecordsSize(String sqlStr)throws DaoException{
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		StringBuilder stringBuilder = new StringBuilder();
		int count = 0;
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			stringBuilder.append("select count(*) count from (");
			stringBuilder.append(sqlStr);
			stringBuilder.append(" ) ");
			rs = stmt.executeQuery(stringBuilder.toString());
			while (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw new DaoException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
//				logger.error(e.getMessage());
			}
		}
		return count;
	}
}
