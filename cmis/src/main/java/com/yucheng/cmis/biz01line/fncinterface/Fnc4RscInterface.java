package com.yucheng.cmis.biz01line.fncinterface;


import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 为五级分类提供财务数据
 * @Classname com.yucheng.cmis.fncinterface.Fnc4RscInterface.java
 * @author wqgang
 * @Since 2009-4-1 下午02:45:47 
 * @Copyright yuchengtech
 * @version 1.0
 */
public interface Fnc4RscInterface {
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
	double getItemValue(String cusId,String itemId,String vData,String fncType,String termType) throws ComponentException;
	
	/**
	 * 根据提供的信息获取某科目的值
	 * @param cusId 	客户ID
	 * @param itemId 	科目ID
	 * @param vDate  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd 
	 *                  如果dd=01 为期初或上月本年累计，如果dd=30为期末或本年累计
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
	 *@param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException 组件异常
	 */
	double getItemValue(String cusId,String itemId,String vData,String fncType,String termType,String statStyle) throws ComponentException;
	
	FncIndexRpt getIndexValue(String cusId,String itemId)throws ComponentException;
}
