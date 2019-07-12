package com.yucheng.cmis.pub.pubopera;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.CMISDomain;

public interface PubOperaInterface {
	/**
	 * 通过domain和keyword返回domain的值
	 * @param domain
	 * @param keyword
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeyword(CMISDomain domain,String keyword) throws EMPException;
	
	/**
	 * 通过domain和keywords返回domain的值
	 * @param domain
	 * @param keywords
	 * @return
	 * @throws EMPException
	 */
	public CMISDomain getCMISDomainByKeywords(CMISDomain domain,Map<String,String> keywords) throws EMPException;
	
	/**
	 * 根据domain和条件，返回domain的list
	 * @param domainClass
	 * @param condition 	格式为 where condition1 = 'conditionValue' and condition2 = 'condition2Value' ...
	 * @return
	 * @throws EMPException
	 */
	public List<CMISDomain> getCMISDomainByCondition(Class<? extends CMISDomain> domainClass, String condition)throws EMPException;
	
	/**
	 * 更新domain
	 * @param domain
	 * @return
	 * @throws EMPException
	 */
	public String updateCMISDomain(CMISDomain domain) throws EMPException;
	
	/**
	 * 写入domain
	 * @param domain
	 * @return
	 * @throws EMPException
	 */
	public String insertCMISDomain(CMISDomain domain) throws EMPException;
	
	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param condition   格式为 where condition1 = 'conditionValue' and condition2 = 'condition2Value' ... 
	 * @return  删除的数据条数
	 * @throws EMPException
	 */
	public int deleteDateByTableAndCondition(String tableName,String condition) throws EMPException;
	
	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param conditionMap 
	 * @return	删除的数据条数
	 * @throws EMPException
	 */
	public int deleteDateByTableAndConditionMap(String tableName,Map<String,String> conditionMap) throws EMPException;
	
	/**
	 * 根据表名和需要更新的值和条件更新表的数据
	 * @param tableName				表名
	 * @param valueMap				需要更新的字段和值  		
	 * @param conditionMap    		条件字段和值
	 * @return	更新的数据条数
	 * @throws EMPException
	 */
	public int updateDateByTableAndValueAndCondition(String tableName,HashMap<String,String> valueMap,
				HashMap<String,String> conditionMap) throws EMPException;
	
	/**
	 * 根据表名和条件查询指定的字段值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段		返回的字段不能重复  格式为：valueA,valueB,valueC,...valueX	
	 * @param conditionMap    		条件字段和值
	 * @return	IndexedCollection	返回值的iColl
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByTableAndCondition(String tableName,String retValues,
			HashMap<String,String> conditionMap) throws EMPException;
	
	/**
	 * 根据表名和主键条件查询指定的字段值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段	    返回的字段不能重复  格式为：valueA,valueB,valueC,...valueX	
	 * @param conditionMap    		主键和值
	 * @return  KeyedCollection     返回值的kColl
	 * @throws EMPException
	 */
	public KeyedCollection selectValuesByTableAndKeyWords(String tableName,String retValues,
			HashMap<String,String> conditionMap) throws EMPException;
	/**
	 * 根据表名和条件查询指定的字段值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段	    	
	 * @param condition    			查询条件
	 * @param connection    		连接
	 * @return  IndexedCollection     返回值的iColl
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByCondition(String tableName,String retValues,String condition,Connection connection) throws EMPException;

	/**
	 * 根据表名和条件查询指定的字段值，只能返回一个值
	 * @param tableName				表名
	 * @param retValues				需要返回的字段	    	
	 * @param condition    			查询条件
	 * @return  IndexedCollection     返回值的iColl
	 * @throws EMPException
	 */
	public String selectSingleValueByCondition(String tableName,String retValues,String condition) throws EMPException;

	
	/**
	 * 通过查询条件分页查询
	 * @param connection
	 * @param conditionStr
	 * @param pageInfo
	 * @return IndexedCollection
	 * @throws SQLException,InvalidArgumentException,DuplicatedDataNameException
	 **/
	public IndexedCollection queryByPageInfo(Connection connection,String conditionStr,PageInfo pageInfo) throws EMPException;
	
	/**
	 * 向表中插入一条记录
	 * 
	 * @param tableName	表名
	 * @param valueMap	需要插入的字段MAP
	 * @return boolean
	 * @throws EMPException
	 */
	public boolean insertValuesByTableAndFieldMap(String tableName, HashMap<String, String> valueMap) 
						throws EMPException;
}
