package com.yucheng.cmis.biz01line.fncinterface;


import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 为财务分析提供财务接口
 * @Classname com.yucheng.cmis.fncinterface.Fnc4FnaInterface.java
 * @author wqgang
 * @Since 2009-4-15 下午02:54:16 
 * @Copyright yuchengtech
 * @version 1.0
 */
public interface Fnc4FnaInterface {
	/**
	 * 根据提供的信息获取某科目的值
	 * @param itemId 	科目ID
	 * @return 
	 * @throws ComponentException 组件异常
	 */
	FncConfItems getFncConfItems(String itemId) throws ComponentException;
	
	/**
	 * 根据提供的信息获取科目的值列表
	 * @param list
	 * @return
	 * @throws ComponentException
	 */
	List<FncConfItems4Query> getFncConfItemsList(List<String> list) throws ComponentException;
	
	
	/**
	 * 根据提供的信息从缓存中获取某科目的对象
	 * @param itemId 	科目ID
	 * @return 
	 * @throws EMPException 
	 */

	public FncConfItems4Query getFncConfItemsFormCashe(String itemId)
			throws EMPException ;
	/**
	 * 根据提供的信息从缓存中获取科目的对象列表
	 * @param list
	 * @return
	 * @throws EMPException 
	 */
	public List<FncConfItems4Query> getFncConfItemsListFormCashe(List<String> list)
			throws EMPException ;
	
	

	/**
	 * 根据提供的信息获取某科目的值
	 * @param itemId 	科目ID
	 * @param vData  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd
	 *                  如果dd=01 为期初，如果dd=30为期末
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException 组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType) throws ComponentException;
	
	
	/**
	 * 根据提供的信息获取某科目的值
	 * @param itemIdList 	科目ID列表
	 * @param vData  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd
	 *                  如果dd=01 为期初，如果dd=30为期末
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
	 * @return 对应可还没的值，即使没有只也返回‘0’
	 * @throws ComponentException 组件异常
	 */
	public List<String> getItemValueList(String cusId,List<String> list,
			String vData,String fncType,String termType) throws ComponentException ;
	
	/**
	 * 根据提供的信息获查询对象列表（仅含对应的数据data1或data2）
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4datax(String cusId,List<FncConfItems4Query> list,
			String vData,String termType) throws ComponentException ;
	
	/**
	 * 根据提供的信息获查询对象列表(含data1和data2)
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */
	
	public List<FncConfItems4Query> getFncConfItemsList4dataAll(String cusId,List<FncConfItems4Query> list,
			String vData,String termType) throws ComponentException ;
	/**
	 * 是否存在符合条件的财报
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 * @throws ComponentException
	 */
//	public boolean isExistStatBase(String cusId,String statPrdStyle,String statPrd) throws ComponentException;
	
	/**
	 * 根据提供的信息获取某科目的值
	 * @param itemId 	科目ID
	 * @param vData  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd
	 *                  如果dd=01 为期初，如果dd=30为期末
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
 
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException 组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType,String statStyle) throws ComponentException;
	
	
	/**
	 * 根据提供的信息获取某科目的值
	 * @param itemIdList 	科目ID列表
	 * @param vData  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd
	 *                  如果dd=01 为期初，如果dd=30为期末
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
	 * @param statStyle  报表口径  1 本部 2合并 3未知 *
	 * @return 对应可还没的值，即使没有只也返回‘0’
	 * @throws ComponentException 组件异常
	 */
	public List<String> getItemValueList(String cusId,List<String> list,
			String vData,String fncType,String termType,String statStyle) throws ComponentException ;
	
	/**
	 * 根据提供的信息获查询对象列表（仅含对应的数据data1或data2）
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @param statStyle  报表口径  1 本部 2合并 3未知 	 * 
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4datax(String cusId,List<FncConfItems4Query> list,
			String vData,String termType,String statStyle) throws ComponentException ;
	
	/**
	 * 根据提供的信息获查询对象列表(含data1和data2)
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @param statStyle  报表口径  1 本部 2合并 3未知 	 * 
	 * @return
	 * @throws ComponentException
	 */
	
	public List<FncConfItems4Query> getFncConfItemsList4dataAll(String cusId,List<FncConfItems4Query> list,
			String vData,String termType,String statStyle) throws ComponentException ;
	/**
	 * 是否存在符合条件的财报
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 */
//	public boolean isExistStatBase(String cusId,String statPrdStyle,String statPrd,String statStyle) throws ComponentException;
	
	/**
	 * 返回一个项目的N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 */
	public double[] getFncConfItemValues4Query(String cusId,
			String item_id, String vDate, String termType,
			String[] date,String statStyle,String fncConfTyp) throws ComponentException;
	
	public FncStatBase findOneFncStatBase(String cusId,String statPrdStyle,String statPrd,String stat_style)throws ComponentException;
	
	/**
	 * 统计财报数目
	 * @param cusId
	 * @param statPrdStyle 报表周期类型
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 * @throws EMPException 
	 */
	public HashMap<String, String>  getNumStatBase(String cusId,String statPrdStyle,String statStyle)throws EMPException;
}
