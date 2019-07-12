package com.yucheng.cmis.pub.pubopera;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class PubOperaAgent extends CMISAgent {
	/**
	 * 通过domain和keyword返回domain的值
	 * @param domain
	 * @param keyword
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeyword(CMISDomain domain,String keyword) throws AgentException {
		// TODO Auto-generated method stub
		String modelId = domain.getClass().getSimpleName();
		return this.findCMISDomainByKeyword(domain, modelId, keyword);
	}
	
	/**
	 * 通过domain和keywords返回domain的值
	 * @param domain
	 * @param keywords
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeywords(CMISDomain domain,Map<String,String> keywords) throws AgentException {
		// TODO Auto-generated method stub
		String modelId = domain.getClass().getSimpleName();
		return this.findCMISDomainByKeywords(domain, modelId, keywords);
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
		List<CMISDomain> domainList = new ArrayList<CMISDomain>();
		Connection con = this.getConnection();
		IndexedCollection icol;
		TableModelDAO dao = this.getTableModelDAO();
		
		try{
			
			icol = dao.queryList(domainClass.getSimpleName(), condition,con);
			ComponentHelper cHelper = new ComponentHelper();
			domainList = cHelper.icol2domainlist(domainClass.getName(), icol);			
		}
		catch(EMPJDBCException e){
			throw new ComponentException(e);
		}
		catch(CMISException e2){
			throw new ComponentException(e2);
		}
		return domainList;
	}

	/**
	 * 更新domain
	 * @param domain
	 * @return
	 * @throws AgentException
	 */
	public String updateCMISDomain(CMISDomain domain) throws AgentException{
		// TODO Auto-generated method stub
		String strMessage = CMISMessage.MODIFYDEFEAT; // 错误信息
		String modelId = domain.getClass().getSimpleName();
		// 更新信息
		int intMessage = this.modifyCMISDomain(domain, 
				modelId);// 1成功
		if (1 == intMessage) {
			strMessage = CMISMessage.SUCCESS;
		}
		return strMessage;
	}
	
	/**
	 * 写入domain
	 * @param domain
	 * @return
	 * @throws AgentException
	 */
	public String insertCMISDomain(CMISDomain domain) throws AgentException{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		//动态插入主从表信息..等待底层方法支持
		String modelId = domain.getClass().getSimpleName();
		int intMessage = this.insertCMISDomain(domain, modelId);

		if (1 == intMessage) {
			strMessage = CMISMessage.SUCCESS;
		}
		return strMessage;
	}

	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param condition
	 * @param conn
	 * @throws EMPException 
	 */
	public int deleteDateByTableAndCondition(String tableName, String condition) throws EMPException {
		// TODO Auto-generated method stub
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.deleteDateByTableAndCondition(tableName,condition,this.getConnection());
	}
	
	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param conditionMap 
	 * @return
	 * @throws EMPException
	 */
	public int deleteDateByTableAndConditionMap(String tableName,Map<String,String> conditionMap) throws EMPException{
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.deleteDateByTableAndConditionMap(tableName,conditionMap,this.getConnection());
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
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.updateDateByTableAndValueAndCondition(tableName,valueMap,conditionMap,this.getConnection());
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
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.selectValuesByTableAndCondition(tableName,retValues,conditionMap,this.getConnection());
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
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.selectValuesByTableAndKeyWords(tableName, retValues, conditionMap, this.getConnection());
	}
	
	/**
	 * 根据表名和条件查询指定的字段值
	 * 
	 * @param tableName				表名
	 * @param retValues				需要返回的字段
	 * @param condition				查询条件
	 * @return IndexedCollection 	返回值的iColl
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByCondition(String tableName,
			String colName, String condition, Connection connection) throws EMPException {
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.selectValuesByCondition( tableName, colName,  condition, connection);
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
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.selectSingleValueByCondition(tableName,retValues,condition,this.getConnection());
	}

	public IndexedCollection queryByPageInfo(Connection connection,
			String conditionStr, PageInfo pageInfo) throws EMPException{
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.queryByPageInfo( connection, conditionStr,  pageInfo);
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
		PubOperaDao pubOperaDao = new PubOperaDao();
		return pubOperaDao.insertValuesByTableAndFieldMap( tableName, valueMap, this.getConnection());
	}
}
