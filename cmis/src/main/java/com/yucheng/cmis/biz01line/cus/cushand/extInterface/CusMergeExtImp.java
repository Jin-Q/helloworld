package com.yucheng.cmis.biz01line.cus.cushand.extInterface;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.log.EMPLog;
/**
 * 客户合并初始化实现类
 * @Version bsbcmis
 * @author wuming 2012-8-22 
 * Description:
 */
public class CusMergeExtImp implements HandoverInterface {

	public void afterAction(Map<String,Object> map, Connection conn) {
		// TODO Auto-generated method stub
		EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "开始执行客户合并操作----");
	}

	public Map<String,Object> beforAction(Map<String,Object> map, Connection conn) {
		// TODO Auto-generated method stub
		EMPLog.log(this.getClass().getName(), EMPLog.INFO,0 , "客户合并操作结束----");
		return null;
	}


}
