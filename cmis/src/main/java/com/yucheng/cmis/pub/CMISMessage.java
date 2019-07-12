package com.yucheng.cmis.pub;
/**
 * 
 *@Classname	  CMISMessage
 *@Version       0.1
 *@Since   	      2008-9-18
 *@Copyright 	  yuchengtech
 *@Author 	      wqgang	
 *@Description：返回值的返回码规范（暂行，待返回信息机制确定）
 *				  
 *@Lastmodified 2008-9-18
 *@Author        wqgang
 */
public class CMISMessage {
	
	
	/**
	 * 通用成功返回信息码
	 */
	public final static String SUCCESS="success";
	/**
	 * 通用失败返回信息吗
	 */
	public final static String DEFEAT="fail"; 
	
	/**
	 * 通用新增成功
	 */
	public final static String ADDSUCCEESS="100100";
	/**
	 * 通用新增失败
	 */
	public final static String ADDDEFEAT="100900";
	/**
	 * 已经存在相应记录，新增失败
	 */
	public final static String ADDDEFEAT91="100901";
	
	
	/**
	 * 通用修改成功
	 */
	public final static String MODIFYSUCCEESS="200100";
	/**
	 * 通用修改失败
	 */
	public final static String MODIFYDEFEAT="200900";
	
	/**
	 * 通用查询成功
	 */
	public final static String QUERYSUCCEESS="300100";
	/**
	 * 通用查询失败
	 */
	public final static String QUERYDEFEAT="300900";
	
	
	
	
	/**
	 * 以下是异常处理代码
	 */
	/** context中数据源为空*/
	public final static String DATASOURCENULLERROR = "datasourcenull_error";
	
	/** 数据源异常*/
	public final static String DATASOURCEERROR = "datasource_error";
	
	/** 获取数据库连接异常*/
	public final static String CONNECTIONERROR = "connection_error";
	
	/** 通用新增失败代码*/
	public final static String ADDERROR = "add_error";
	
	/** 通用修改失败代码*/
	public final static String UPDATEERROR = "update_error";
	
	/** 通用删除失败代码*/
	public final static String DELETEERROR = "delete_error";
	
	/** 通用查询失败代码*/
	public final static String QUERYERROR = "query_error";
	
	/** 通用打出默认异常信息代码*/
	public final static String MESSAGEDEFAULT = "999999";
	
	/**
	 * 贷前调查关系信息异常
	 */
	public final static String IQPDISCBILLADD="IQPDISCBILLADD"; //"汇票号重复，请检查。信息添加失败
	public final static String IQPACCPAPPNOVICEADD="IQPACCPAPPNOVICEADD"; //"发票号重复，请检查。信息添加失败
	public final static String IQPCOMMAPPINFOADD="IQPCOMMAPPINFOADD"; //"共同申请人信息重复，请检查。信息添加失败
	public final static String IQPDISCIQPGUARANTORAAPP="IQPDISCIQPGUARANTORAAPP"; //此人不存在联保协议。信息添加失败
        /**
	 * 客户信贷关系信息异常
	 */
	public final static String CUSLOANRELADDOPIDTYPE="CUSLOANRELADDOPIDTYPE"; //"客户客户经理关系新增失败（客户经理类型有误）"
	public final static String CUSLOANRELUPDATEOPIDTYPE="CUSLOANRELUPDATEOPIDTYPE"; //"客户客户经理关系更新失败（客户经理类型有误）"
	public final static String CUSLOANRELDELETECUSID="CUSLOANRELDELETECUSID"; //"客户客户经理关系删除失败（客户代码错误）
	public final static String CUSLOANRELDELETELOANOPTID="CUSLOANRELDELETELOANOPTID";//"客户客户经理关系删除失败（客户经理代码错误
	public final static String CUSLOANRELDELETELOANCHID="CUSLOANRELDELETELOANCHID"; //"客户客户经理关系删除失败（主管行代码错误）
	
	public final static String CUSLOANRELQUERYCUSID="CUSLOANRELQUERYCUSID"; //"客户客户经理关系查询失败（客户代码错误）
	public final static String CUSLOANRELQUERYLOANOPTID="CUSLOANRELQUERYLOANOPTID";//"客户客户经理关系查询失败（客户经理代码错误
	public final static String CUSLOANRELQUERYLOANCHID="CUSLOANRELQUERYLOANCHID"; //"客户客户经理关系查询失败（主管行代码错误）

	/**
	 * 客户信贷关系变更信息异常
	 */
	public final static String CUSLOANRELLOGADDOPIDTYPE="CUSLOANRELLOGADDOPIDTYPE"; //"客户客户经理关系变更新增失败（客户经理类型有误）"
	public final static String CUSLOANRELLOGUPDATEOPIDTYPE="CUSLOANRELLOGUPDATEOPIDTYPE"; //"客户客户经理关系变更更新失败（客户经理类型有误）"
	public final static String CUSLOANRELLOGDELETECUSID="CUSLOANRELLOGDELETECUSID"; //"客户客户经理关系变更删除失败（客户代码错误）
	public final static String CUSLOANRELLOGDELETELOANOPTID="CUSLOANRELLOGDELETELOANOPTID";//"客户客户经理关系变更删除失败（客户经理代码错误
	public final static String CUSLOANRELLOGDELETELOANCHID="CUSLOANRELLOGDELETELOANCHID"; //"客户客户经理关系变更删除失败（主管行代码错误）
	
	public final static String CUSLOANRELLOGQUERYCUSID="CUSLOANRELLOGQUERYCUSID"; //"客户客户经理关系变更查询失败（客户代码错误）
	public final static String CUSLOANRELLOGQUERYLOANOPTID="CUSLOANRELLOGQUERYLOANOPTID";//"客户客户经理关系变更查询失败（客户经理代码错误
	public final static String CUSLOANRELLOGQUERYLOANCHID="CUSLOANRELLOGQUERYLOANCHID"; //"客户客户经理关系变更查询失败（主管行代码错误）; //"客户客户经理关系查询失败（主管行代码错误）
	 
}
