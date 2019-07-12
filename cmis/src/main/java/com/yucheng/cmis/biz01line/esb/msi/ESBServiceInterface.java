package com.yucheng.cmis.biz01line.esb.msi;

import java.math.BigDecimal;
import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
/**
 * ESB服务对外提供服务接口
 * @author Pansq
 * @version V1.0
 */
@ModualService(modualId="esb",modualName="ESB接口模块",serviceId="esbServices",serviceDesc="ESB模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface")
public interface ESBServiceInterface {

	/**
	 * 获取系统头报文体结构体
	 * @param code 服务代码
	 * @param scene 服务场景
	 * @param consumerId 消费者ID
	 * @param serno 消费者流水号
	 * @param userId 登录人ID
	 * @param orgId 登录机构ID
	 * @param sysId 交易发送系统
	 * @param context 上下文
	 * @return 系统报文头结构体 CompositeData
	 * @throws Exception
	 */
	@MethodService(
			method="getSysHeadCD",desc="获取系统头报文体结构体",
			inParam={
					@MethodParam(paramName="code",paramDesc="服务代码"),
					@MethodParam(paramName="scene",paramDesc="服务场景"),
					@MethodParam(paramName="consumerId",paramDesc="消费者ID"),
					@MethodParam(paramName="serno",paramDesc="消费者流水号"),
					@MethodParam(paramName="userId",paramDesc="登录人ID"),
					@MethodParam(paramName="userName",paramDesc="登录人名称"),
					@MethodParam(paramName="orgId",paramDesc="登录机构ID"),
					@MethodParam(paramName="sysId",paramDesc="交易发送系统"),
					@MethodParam(paramName="context",paramDesc="上下文")
			},
			outParam=@MethodParam(paramName="CompositeData",paramDesc="系统报文头结构体 CompositeData")
	)
	public CompositeData getSysHeadCD(String code, String scene, String consumerId, String serno, String userId,String userName, String orgId, String sysId, Context context) throws Exception;
	
	/**
	 * 获取应用层报文结构体（待定、预留）
	 * @return
	 * @throws Exception
	 */
	public CompositeData getAppHeadCD() throws Exception; 
	
	
	/**
	 * 获取应用层报文头结构体（待定、预留）
	 * @param userId 操作人员ID
	 * @param userName 操作人员名称
	 * @param orgId 操作人员所在机构
	 * @param sysId 系统ID
	 * @return 应用报文头结构体
	 * @throws Exception
	 */
	@MethodService(
			method="getAppHeadCD",desc="获取应用层报文头结构体",
			inParam={
					@MethodParam(paramName="userId",paramDesc="操作人员ID"),
					@MethodParam(paramName="userName",paramDesc="操作人员名称"),
					@MethodParam(paramName="orgId",paramDesc="操作人员所在机构"),
					@MethodParam(paramName="sysId",paramDesc="系统ID")
			},
			outParam=@MethodParam(paramName="CompositeData",paramDesc="应用层报文头结构体 CompositeData")
	)
	public CompositeData getAppHeadCD(String userId, String userName, String orgId, String sysId) throws Exception ;
	
	/**
	 * 获取本地扩展报文头结构体
	 * @return
	 * @throws Exception
	 */
	public CompositeData getLocalHeadCD() throws Exception;
	
	/**
	 * 贷款发放授权接口
	 * @param reqCD 请求报文体（包括系统报文头、应用报文头、报文体）
	 * @return 返回结果结构体
	 * @throws Exception 
	 */
	public CompositeData tradeDKFFSQ(CompositeData reqCD) throws Exception;
	
	/**
	 * 账户信息查询
	 * @param  传入账户账号，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeZHZH",desc="调用esb接口查询账户信息",
			inParam={
					@MethodParam(paramName="acct_no",paramDesc="账号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息及账户信息")
	)
	public KeyedCollection tradeZHZH(String acct_no,Context context,Connection connection) throws Exception;
	
	/**
	 * 保证金账户查询
	 * @param  传入保证金账号，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZH",desc="调用esb接口查询保证金账户信息",
			inParam={
					@MethodParam(paramName="bail_acct_no",paramDesc="账号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息及账户信息")
	)
	public KeyedCollection tradeBZJZH(String bail_acct_no,Context context,Connection connection) throws Exception;
	
	/**
	 * 个人半年日均查询
	 * @param  传入个人客户码，配偶客户码(若无可传空)，context，connection
	 * @return 返回报文头信息及个人半年日均金额
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBNRJ",desc="调用esb接口查询个人半年日均",
			inParam={
					@MethodParam(paramName="cus_id",paramDesc="个人客户码"),
					@MethodParam(paramName="spouse_cus_id",paramDesc="配偶客户码"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息及个人半年日均金额")
	)
	public KeyedCollection tradeBNRJ(String cus_id,String spouse_cus_id,Context context,Connection connection) throws Exception;
	
	
	/**
	 * 还款计划查询
	 * @param  传入保证金账号，context，connection，pageInfo
	 * @return 返回报文头信息及还款计划信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeHKJH",desc="调用esb接口查询还款计划信息",
			inParam={
					@MethodParam(paramName="bill_no",paramDesc="借据号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接"),
					@MethodParam(paramName="pageInfo",paramDesc="分页信息")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息及报文体信息")
	)
	public KeyedCollection tradeHKJH(String bill_no,Context context,Connection connection,PageInfo pageInfo) throws Exception;
	
	/**
	 * 交易流水查询
	 * @param  传入借据号，context，connection,pageInfo
	 * @return 返回报文头信息及交易流水信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeJYLS",desc="调用esb接口查询交易流水信息",
			inParam={
					@MethodParam(paramName="bill_no",paramDesc="借据号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接"),
					@MethodParam(paramName="pageInfo",paramDesc="分页信息")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息及交易流水信息")
	)
	public KeyedCollection tradeJYLS(String bill_no,Context context,Connection connection,PageInfo pageInfo) throws Exception;
	
	/**
	 * 授权撤销接口
	 * @param  授权信息，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZH",desc="调用esb接口授权撤销",
			inParam={
					@MethodParam(paramName="auKColl",paramDesc="授权信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="授权撤销接口")
	)
	public KeyedCollection tradeSQCX(KeyedCollection auKColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 抵质押物权证出/入库
	 * @param  出/入库申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeDZYWQZCRK",desc="调用esb接口对抵质押物权证出/入库授权",
			inParam={
					@MethodParam(paramName="serno",paramDesc="出/入库申请业务流水号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文头信息")
	)
	public KeyedCollection tradeDZYWQZCRK(String serno,Context context,Connection connection) throws Exception;
	
	/**
	 * 贷款核销文件
	 * @param  呆账核销申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeDKHX",desc="呆账核销授权发送文件交易",
			inParam={
					@MethodParam(paramName="serno",paramDesc="呆账核销申请业务流水号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回报文信息")
	)
	public KeyedCollection tradeDKHX(String serno,Context context,Connection connection) throws Exception;
	
	/**
	 * 影像核对归档接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZH",desc="影像核对归档接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeXYHD(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 影像锁定接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZH",desc="影像锁定接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeIMAGELOCKED(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 核算与信贷业务品种映射接口
	 * @param  传入业务信息主键pk_value，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="getPrdBasicCLPM2LM",desc="核算与信贷业务品种映射接口",
			inParam={
					@MethodParam(paramName="pk_value",paramDesc="业务主键"),
					@MethodParam(paramName="prd_id",paramDesc="信贷业务编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public String getPrdBasicCLPM2LM(String pk_value,String prd_id,Context context,Connection connection) throws Exception;
	
	/**
	 * 核算与信贷业务品种映射接口
	 * @param  传入业务信息主键pk_value，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="getPrdBasicAssetstrsf2LM",desc="资产转让科目映射接口",
			inParam={
					@MethodParam(paramName="asset_no",paramDesc="资产包编号"),
					@MethodParam(paramName="bill_no",paramDesc="借据编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public String getPrdBasicAssetstrsf2LM(String asset_no,String bill_no,Context context,Connection connection) throws Exception;
	
	/**
	 * 资产证券化业务品种映射接口  add by zhaozq 2014-08-25 
	 * @param bill_no
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(
			method="getPrdBasicAssetPro2LM",desc="资产证券化科目映射接口",
			inParam={
					@MethodParam(paramName="bill_no",paramDesc="借据编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="核算业务品种编号")
	)
	public String getPrdBasicAssetPro2LM(String bill_no,Context context,Connection connection) throws Exception;
	
	/**
	 * 抵质押临时编号获取接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZH",desc="影像押品编号获取接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeXYYPBHHQ(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 利率调整授权
	 * @param  利率调整申请业务流水号 serno，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeLVTZ",desc="利率调整授权",
			inParam={
					@MethodParam(paramName="serno",paramDesc="利率调整主键"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeLVTZ(String serno,Context context,Connection connection) throws Exception;
	
	/**
	 * 保证金变更授权
	 * @param  保证金变更申请业务流水号 serno，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJBG",desc="保证金变更授权",
			inParam={
					@MethodParam(paramName="serno",paramDesc="保证金变更申请业务编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeBZJBG(String serno,Context context,Connection connection) throws Exception;
	
	/**
	 * 信贷保证金追加实时通知国结
	 * @param  保证金变更授权编号 authorize_no，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJZJTZGJ",desc="信贷保证金追加实时通知国结",
			inParam={
					@MethodParam(paramName="authorize_no",paramDesc="保证金变更授权编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeBZJZJTZGJ(String authorize_no,Context context,Connection connection) throws Exception;
	
	/**
	 * 获取12级分类任务是否完成(02003000002  56)
	 * @param  managerId 客户经理代码
	 * @param  roleId 角色代码
	 * @param  taskType 任务类型
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeSEJFLRW",desc="获取12级分类任务是否完成",
			inParam={
					@MethodParam(paramName="managerId",paramDesc="客户经理代码"),
					@MethodParam(paramName="roleId",paramDesc="角色代码"),
					@MethodParam(paramName="taskType",paramDesc="任务类型"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeSEJFLRW(String managerId,String roleId,String taskType,Context context,Connection connection) throws Exception;
	
	/**
	 * 保证金利息试算
	 * @param  保证金账号
	 * @param  出账流水号
	 * @param  保函修改，信用证改正保证金金额
	 * @param  表名 
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	@MethodService(
			method="tradeBZJLXSS",desc="保证金利息试算",
			inParam={
					@MethodParam(paramName="bail_acct_no",paramDesc="保证金账号"),
					@MethodParam(paramName="serno",paramDesc="出账流水号"),
					@MethodParam(paramName="seAmt",paramDesc="保函修改，信用证改正保证金金额"),
					@MethodParam(paramName="tableName",paramDesc="表名"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection tradeBZJLXSS(String bail_acct_no,String serno,BigDecimal seAmt,String tableName,Context context, Connection connection) throws Exception;

	/**
	 * 自助贷款协议授权信息采集
	 * @param  合同流水号 contNo，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by yangzy 2015/04/08 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	@MethodService(
			method="doWfAgreeForSelfLoan",desc="自助贷款协议授权信息采集",
			inParam={
					@MethodParam(paramName="contNo",paramDesc="合同编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public void doWfAgreeForSelfLoan(String contNo,Context context,Connection connection) throws Exception;
	
	/**
	 * 自助贷款协议授权
	 * @param  合同流水号 contNo，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by yangzy 2015/04/08 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	@MethodService(
			method="trade0200100000101",desc="自助贷款协议授权",
			inParam={
					@MethodParam(paramName="contNo",paramDesc="合同编号"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade0200100000101(String contNo,Context context,Connection connection) throws Exception;

	/**
	 * 法人账户透支改造
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by wangj 2015/05/28 需求编号:XD141222087,法人账户透支改造
	 */
	@MethodService(
			method="trade0200200000503",desc="法人账户透支额度冻结发送交易",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade0200200000503(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 小微自助循环贷款改造
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by lisj 2015-6-1 需求编号：【XD150123005】小微自助循环贷款改造
	 */
	@MethodService(
			method="trade0200200000502",desc="小微自助循环贷款额度冻结发送交易",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade0200200000502(KeyedCollection kColl,Context context,Connection connection) throws Exception;

	
	/**
	 * 无间贷风险拦截
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by wangj 2015/07/01  需求编号:XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 */
	@MethodService(
			method="trade0200200000205",desc="已放贷款试算器交易接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade0200200000205(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	/**
	 * 印花税试算   需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 *        变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 * @param kColl  业务信息
	 * @param context
	 * @param connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	@MethodService(
			method="trade0200200000207",desc="印花税试算交易接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade0200200000207(KeyedCollection kColl,Context context,Connection connection) throws Exception;
	
	/**
	 * 通用银行费用查询    需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 *              变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 * @param kColl  业务信息
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(
			method="trade1100300001602",desc="通用银行费用查询交易接口",
			inParam={
					@MethodParam(paramName="kColl",paramDesc="业务信息"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="响应报文")
	)
	public KeyedCollection trade1100300001602(KeyedCollection kColl,Context context,Connection connection) throws Exception;

	
	public KeyedCollection tradeDZYWQZCRKYM(String serno,Context context,Connection connection) throws Exception;
	
	//modify by jch 2019-03-12 还款计划查询
	public KeyedCollection tradeHKJHYM(String bill_no,Context context,Connection connection,PageInfo pageInfo) throws Exception;

	//modify by jch 2019-03-12 还款明细查询
	public KeyedCollection tradeJYLSYM(String bill_no,Context context,Connection connection,PageInfo pageInfo) throws Exception;
	
	//modify by jch 2019-03-12 授权撤销
	public KeyedCollection tradeSQCXYM(KeyedCollection auKColl,Context context,Connection connection) throws Exception;

	/**
	 * 还款试算接口定义
	 * @param reqParam 接口上送参数
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return KeyedCollection 接口出参
	 * @throws EMPException 统一抛出EMP异常
	 * @author huangtao 
	 */
	public KeyedCollection tradeHKSS(KeyedCollection reqParam,Context context,Connection connection) throws EMPException;
	
}
