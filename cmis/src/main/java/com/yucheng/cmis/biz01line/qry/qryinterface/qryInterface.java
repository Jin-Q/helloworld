package com.yucheng.cmis.biz01line.qry.qryinterface;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;

public abstract class qryInterface {
	/**
	 * 抽取数据插入临时表
	 * @param conditionMap  所有条件（键值对形式）
	 * @param operationMap  对条件的操作方式（如like,in and so on）（键值对形式）
	 * @param context       
	 * @param con           数据库连接
	 * @throws EMPException
	 */
	public abstract void analyseData(HashMap<String,String> conditionMap, 
                                        HashMap<String,String> operationMap,
                                        Context context,
                                        Connection con) throws EMPException;
}
