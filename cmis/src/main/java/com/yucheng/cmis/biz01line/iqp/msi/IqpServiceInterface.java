package com.yucheng.cmis.biz01line.iqp.msi;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 业务模块对外接口
 * @author MQ
 */
@ModualService(modualId="iqp",modualName="业务管理",serviceId="iqpServices",serviceDesc="业务模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface")
public interface IqpServiceInterface {
	/**
	 * 根据授信协议编号查询占用余额和业务占用明细。
	 * @param arg_no   授信协议编号
	 * @param lmt_type 授信类型 01：单一法人 02:同业客户 03:合作方
	 * @return iColl IndexedCollection数据集
	 */
	@MethodService(method="getAgrUsedInfoByArgNo", desc="根据授信台账编号/授信合作方协议编号查询授信占用",
		inParam={
			@MethodParam(paramName="arg_no",paramDesc="授信协议编号"),
			@MethodParam(paramName="lmt_type",paramDesc="授信类型"),
			@MethodParam(paramName="connection",paramDesc="数据库连接"),
			@MethodParam(paramName="context",paramDesc="上下文")
		},
		outParam={
			@MethodParam(paramName="KeyedCollection",paramDesc="返回数据集")
		},
		example="getAgrUsedInfoByArgNo(arg_no,lmt_type,connection,context)")
	public KeyedCollection getAgrUsedInfoByArgNo(String arg_no,String lmt_type,Connection connection, Context context) throws EMPException;
    
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param arg_no 授信台帐编号
	 * @param pageInfo 分页信息
	 * @param conditionStr 查询条件
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getHistoryContByLimitAccNo", desc="通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）",
			inParam={
				@MethodParam(paramName="limit_ind",paramDesc="授信台帐类型（1,单一法人额度、2,合作方额度）"),
				@MethodParam(paramName="arg_no",paramDesc="授信台帐编号"),
				@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
				@MethodParam(paramName="conditionStr",paramDesc="查询条件"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam={
				@MethodParam(paramName="IndexedCollection",paramDesc="返回存量合同数据集")
			},
			example="getHistoryContByLimitAccNo(limit_ind,arg_no,pageInfo,conditionStr,context,connection)")
	public IndexedCollection getHistoryContByLimitAccNo(String limit_ind, String arg_no, PageInfo pageInfo,String conditionStr, Context context, Connection connection) throws EMPException;
	
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（查询待发起状态的业务申请）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param agr_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getIqpByLimitAccNo", desc="通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）",
			inParam={
			@MethodParam(paramName="limit_ind",paramDesc="授信台帐类型（1,单一法人额度、2,合作方额度）"),
			@MethodParam(paramName="agr_no",paramDesc="授信台帐编号"),
			@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	},
	outParam={
			@MethodParam(paramName="IndexedCollection",paramDesc="返回存量合同数据集")
	},
	example="getIqpByLimitAccNo(limit_ind,arg_no,pageInfo,context,connection)")
	public IndexedCollection getIqpByLimitAccNo(String limit_ind, String agr_no, PageInfo pageInfo, Context context, Connection connection) throws EMPException;
	
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param arg_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getHistoryContByLimitArgNo", desc="通过授信授信协议编号获取授信台帐存量业务（目前只查询存量合同信息）",
			inParam={
				@MethodParam(paramName="arg_no",paramDesc="授信协议编号"),
				@MethodParam(paramName="conditionStr",paramDesc="查询条件"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam={
				@MethodParam(paramName="IndexedCollection",paramDesc="返回存量合同数据集")
			},
			example="getHistoryContByLimitArgNo(arg_no,conditionStr,context,connection)")
	public IndexedCollection getHistoryContByLimitArgNo(String arg_no, String conditionStr, Context context, Connection connection) throws EMPException;	
	
	/**
	 * 通过授信台帐类型、授信台帐编号获取授信台帐存量业务（目前只查询待发起业务信息）
	 * @param limit_ind 授信台帐类型（1,单一法人额度、2,合作方额度）
	 * @param agr_no 授信台帐编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getIqpByLimitAgrNoForSame", desc="通过授信授信协议编号获取授信台帐存量业务（目前只查询待发起业务信息）",
			inParam={
			@MethodParam(paramName="agr_no",paramDesc="授信协议编号"),
			@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	},
	outParam={
			@MethodParam(paramName="IndexedCollection",paramDesc="返回存量合同数据集")
	},
	example="getIqpByLimitAgrNoForSame(arg_no,pageInfo,context,connection)")
	public IndexedCollection getIqpByLimitAgrNoForSame(String agr_no, PageInfo pageInfo, Context context, Connection connection) throws EMPException;	
	/**
	 * 更新客户信用评级、评级日期
	 * @param overseeOrgId	监管机构编号
	 * @param cdtEval	信用评级
	 * @param evalTime 评级日期
	 * @param connection 数据库连接
	 * @return 
	 * @throws EMPException
	 */
	@MethodService(method="updateCdtEval",desc="评级结束更新监管机构客户评级信息",
			inParam={
				@MethodParam(paramName="overseeOrgId",paramDesc="监管机构编号"),
				@MethodParam(paramName="cdtEval",paramDesc="信用评级"),
				@MethodParam(paramName="evalTime",paramDesc="评级日期"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="修改结果"))
	public String updateCdtEval(String overseeOrgId, String cdtEval, String evalTime, Connection connection) throws Exception ;
	
	/**
	 * 取借据信息pop
	 * @param cusId	客户编号
	 * @param OrgId	机构号
	 * @param connection 数据库连接
	 * @return 所选借据信息kcoll
	 * @throws EMPException
	 */
	@MethodService(method="queryBillNoPop",desc="取借据信息pop",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户编号"),
				@MethodParam(paramName="OrgId",paramDesc="机构号"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回数据集"))
	public String queryBillNoPop(String cusId, String OrgId , Connection connection) throws Exception ;
	
	
	/**
	 * 新增一般担保合同后保存业务担保合同关系表
	 * @param kColl	业务合同关系集合  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 是否保存成功
	 * @throws EMPException
	 */
	@MethodService(method="addGrtLoanRGur",desc="新增一般担保合同后保存业务担保合同关系表",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="业务担保合同关系集合"),
			@MethodParam(paramName="context",paramDesc="上下文"),  
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="String",paramDesc="返回是否保存成功"))
	public int addGrtLoanRGur(KeyedCollection kColl,Context context, Connection connection) throws Exception ;
	
	
	/**
	 * 修改查看担保合同查询业务担保合同关系表
	 * @param pk_id	业务合同关系表主键  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同kColl
	 * @throws EMPException
	 */
	@MethodService(method="selectGetLoanRGur",desc="修改查看担保合同查询业务担保合同关系表",
			inParam={
			@MethodParam(paramName="pk_id",paramDesc="业务流水号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="业务担保合同kColl")) 
	public KeyedCollection selectGetLoanRGur(String pk_id, Context context, Connection connection) throws Exception ;
	
	
	/**
	 * 修改删除担保合同查询业务担保合同关系表此担保合同条数
	 * @param guar_cont_no	担保合同编号  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 条数 
	 * @throws EMPException
	 */
	@MethodService(method="checkGetLoanRGurNum",desc="修改删除担保合同查询业务担保合同关系表此担保合同条数",
			inParam={
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="int",paramDesc="业务合同表中此担保合同条数")) 
	public int checkGetLoanRGurNum(String guar_cont_no, Context context, Connection connection) throws Exception ;
	
	/**
	 * 担保合同模块查询业务担保合同关联表中新增的担保变更
	 * @param pageInfo  翻页信息
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同iColl
	 * @throws EMPException
     *  modified by yangzy 2015/04/14 需求：XD150325024，集中作业扫描岗权限改造
	 */
	@MethodService(method="getInsertGuarChange",desc="担保合同模块查询业务担保合同关联表中新增的担保变更",
			inParam={
			@MethodParam(paramName="pageInfo",paramDesc="翻页信息"), 
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="int",paramDesc="业务合同表中此担保合同条数"))
	public IndexedCollection getInsertGuarChange(KeyedCollection queryData,PageInfo pageInfo,String conditionStrT ,Context context, Connection connection) throws Exception;
	
	/**
	 * 根据查询条件返回台账信息
	 * @param queryData
	 * @param pageInfo（可传空）
	 * @param context
	 * @param connection
	 * @param acc_type（1-贷款台账，2-银承台账，3-票据台账，4-垫款台账，5-资产转让台账）
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getAccByQueryData",desc="根据查询条件返回台账信息",
			inParam={
			@MethodParam(paramName="queryData",paramDesc="查询信息"),
			@MethodParam(paramName="pageInfo",paramDesc="翻页信息（不需分页传空）"), 
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
			@MethodParam(paramName="acc_type",paramDesc="台账类型")
	},
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="符合条件的台账信息"))
	public IndexedCollection getAccByQueryData(KeyedCollection queryData,PageInfo pageInfo, Context context, Connection connection,String acc_type) throws Exception;
	
	/**
	 * 通用查询借据信息pop
	 */
	@MethodService(method="queryAccLoanPop.do", desc="通用查询借据信息pop",
			inParam={
				@MethodParam(paramName="condition",paramDesc="查询条件")
			},
			outParam={
				@MethodParam(paramName="bill_no",paramDesc="借据编号"),
				@MethodParam(paramName="cont_no",paramDesc="合同编号"),
				@MethodParam(paramName="prd_id",paramDesc="产品编号"),
				@MethodParam(paramName="cus_id",paramDesc="客户编号"),
				@MethodParam(paramName="cur_type",paramDesc="币种"),
				@MethodParam(paramName="loan_amt",paramDesc="贷款金额"),
				@MethodParam(paramName="loan_balance",paramDesc="贷款余额"),
				@MethodParam(paramName="start_date",paramDesc="起贷日期"),
				@MethodParam(paramName="end_date",paramDesc="止贷日期"),
				@MethodParam(paramName="acc_status",paramDesc="台账状态")
			},methodType=MethodType.JSP,
			example="queryAccLoanPop.do?condition=Com")
	public void queryAccLoanPop();
	/**
	 * 通过币种从汇率表中取得币种对应的汇率信息
	 * @param currType 币种
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return double
	 * @throws Exception
	 */
	@MethodService(method="getHLByCurrType",desc="通过币种从汇率表中取得币种对应的汇率信息",
			inParam={
			@MethodParam(paramName="currType",paramDesc="币种"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="币种对应的汇率,以及反馈信息"))
	public KeyedCollection getHLByCurrType(String currType, Context context, Connection connection) throws Exception;
	
	/**
	 * 通过担保合同编号获取其所关联的业务合同
	 * @param GuarContNo 担保合同编号/池编号
	 * @param type 参数类型1--担保合同编号，2--池编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 担保所关联合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getHistoryContByGuarContNo", desc="通过担保合同编号获取其所关联的业务合同",
			inParam={
				@MethodParam(paramName="GuarContNo",paramDesc="担保合同编号"),
				@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam={
				@MethodParam(paramName="IndexedCollection",paramDesc="担保所关联合同集合")
			},
			example="getHistoryContByLimitAccNo(limit_ind,arg_no,pageInfo,context,connection)")
	public IndexedCollection getHistoryContByGuarContNo(String GuarContNo,String type,PageInfo pageInfo, Context context, Connection connection) throws EMPException;
	
	/**
	 * 根据查询条件返回台账信息iColl
	 * @param condition
	 * @param context
	 * @param 分页信息
	 * @param connection
	 * @param acc_type（1-贷款台账，2-银承台账，3-票据台账，4-垫款台账，5-资产转让台账 , 0-主管客户经理）
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getAccByCondition",desc="根据查询条件返回台账信息",
			inParam={
			@MethodParam(paramName="condition",paramDesc="查询条件"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
			@MethodParam(paramName="acc_type",paramDesc="台账类型")
	},
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="符合条件的台账信息"))
	public IndexedCollection getAccByCondition(String condition,Context context,PageInfo pageInfo, Connection connection,String acc_type) throws Exception;
	
	/**
	 * 根据合同编号更新主管客户经理与主管机构
	 * @param kColl
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getAccByCondition",desc="根据合同编号更新主管客户经理与主管机构",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="更新信息"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="String",paramDesc="处理结果"))
	public String delManageInfo(KeyedCollection kColl,Context context, Connection connection) throws Exception;
	
	/**
	 * 根据查询条件返回台账信息kColl
	 * @param condition
	 * @param context
	 * @param connection
	 * @param acc_type（1-贷款台账，2-银承台账，3-票据台账，4-垫款台账，5-资产转让台账）
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getAccByCondition",desc="根据查询条件返回台账信息",
			inParam={
			@MethodParam(paramName="condition",paramDesc="查询条件"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
			@MethodParam(paramName="acc_type",paramDesc="台账类型")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="符合条件的台账信息"))
	public KeyedCollection getAccByConditionKcoll(String condition,Context context, Connection connection,String acc_type) throws Exception;
	
	/**
	 * 根据serno统计业务申请阶段保证金的占用总额
	 * @param serno
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getBailInfo4IqpApp",desc="根据serno统计业务申请阶段保证金的占用总额",
			inParam={
			@MethodParam(paramName="serno",paramDesc="业务流水号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="BigDecimal",paramDesc="业务申请阶段保证金的占用"))
	public BigDecimal getBailInfo4IqpApp(String serno,Context context, Connection connection) throws Exception;
	
	/**
	 * 根据serno统计合同之后保证金占用总额
	 * @param serno
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getBailInfo4cont",desc="根据cont_no统计合同之后保证金占用总额",
			inParam={
			@MethodParam(paramName="serno",paramDesc="业务流水号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="BigDecimal",paramDesc="业务申请之后阶段保证金的占用"))
	public BigDecimal getBailInfo4cont(String serno,Context context, Connection connection) throws Exception;
	
	/**
	 * 根据cont_no统计合同之后保证金占用总额(去除本笔业务for信用证、保函修改)
	 * @param cont_no
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getBailInfo4Change",desc="根据cont_no统计合同之后保证金占用总额",
			inParam={
			@MethodParam(paramName="serno",paramDesc="业务流水号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="BigDecimal",paramDesc="业务申请之后阶段保证金的占用"))
	public BigDecimal getBailInfo4Change(String cont_no,Context context, Connection connection) throws Exception;
	
	/**
	 * 根据授信台账编号查询是否发生业务（判断是否可以一票否决等）
	 * @param limit_code 授信台账编号 字符串 
	 * @param context
	 * @param connection
	 * @return true/false结果
	 * @throws Exception
	 */
	@MethodService(method="checkIqp4DisAgree",desc="根据授信台账编号查询是否发生业务（判断是否可以一票否决）",
			inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信台账编号字符串"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="Boolean",paramDesc="结果"))
	public Boolean checkIqp4DisAgree(String limit_code, Context context, Connection connection) throws Exception;
	
	/**
	 * 根据授信台账编号查询项下业务是否结清
	 * @param limit_code 授信协议编号 字符串 
	 * @param context
	 * @param connection
	 * @return true/false结果
	 * @throws Exception
	 */
	@MethodService(method="checkIqpInfo4Lmt",desc="根据授信台账编号查询项下业务是否结清",
			inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信台账编号字符串"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="Boolean",paramDesc="结果"))
	public Boolean checkIqpInfo4Lmt(String limit_code, Context context, Connection connection) throws Exception;
	
	/**
	 * 根据条件更新业务表信息
	 * @param kColl (默认参数1.type 其他按需扩展)
	 * @param context
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	@MethodService(method="updateIqpByCondition",desc="根据条件更新业务表信息",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="所有条件集合kColl"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="需要的返回值kColl"))
	public KeyedCollection updateIqpByCondition(KeyedCollection kColl, Context context, Connection connection) throws Exception;
	
	/**
	 * 根据授信台账编号/授信合作方协议编号查询授信占用。
	 * (filed:lmt_amt授信占用、is_grp是否集团融资模式、grp_lmt_amt集团授信占用)
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getLmtAmtByArgNo",desc="根据授信台账编号/授信合作方协议编号查询授信占用",
			inParam={
			@MethodParam(paramName="agr_no",paramDesc="授信台账编号/授信合作方协议编号"),
			@MethodParam(paramName="lmt_type",paramDesc="01/02/03"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="需要的返回值kColl"))
	public KeyedCollection getLmtAmtByArgNo(String agr_no,String lmt_type,Connection connection,Context context) throws Exception;
	
	/**
	 * 根据担保合同编号获取其与业务关联表信息
	 * @param guar_cont_no 担保合同编号
	 * @param connection
	 * @return IndexedCollection 业务关联表信息iColl
	 * @throws Exception
	 */
	@MethodService(method="getIqpGuarContReByGuarContNo",desc="根据担保合同编号获取其与业务关联表信息",
			inParam={
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="业务关联表信息"))
	public IndexedCollection getIqpGuarContReByGuarContNo(String guar_cont_no,Connection connection) throws Exception;
	
	/**
	 * 通过业务流水号查询是否集团下成员，如果是，判断是否超过集团占用总额
	 * @param serno 业务流水号
	 * @param connection
	 * @param context
	 * @return KeyedCollection 集团编号 集团授信总额
	 * @throws Exception
	 */
	@MethodService(method="getGrpInfo",desc="通过业务流水号查询是否集团下成员，如果是，判断是否超过集团占用总额",
			inParam={
			@MethodParam(paramName="serno",paramDesc="业务流水号"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc=" 集团编号 集团授信总额"))
	public KeyedCollection getGrpInfo(String serno,Connection connection,Context context) throws Exception;
	
	
	/**
	 * 通过客户码查询客户的存量台账
	 * @param cus_id 客户码 
	 * @param connection
	 * @param context
	 * @return IndexedCollection 返回的台账数据
	 * @throws Exception
	 */
	@MethodService(method="getAccViewByCusId",desc="通过客户码查询客户的存量台账",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接"),
	},
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="返回台账数据"))
	public IndexedCollection getAccViewByCusId(String cus_id,Connection connection,Context context) throws Exception;
	
	
	/**
	 * 通过融资性担保公司授信台帐编号获取授信台帐存量业务（查询待发起状态的业务申请）
	 * @param cus_id 融资性担保公司客户ID
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getIqp4LmtAgrFinGuar", desc="通过融资性担保公司授信台帐编号获取授信台帐存量业务（查询待发起状态的业务申请）",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="融资性担保公司客户ID"),
			@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	},
	outParam={
			@MethodParam(paramName="IndexedCollection",paramDesc="返回存量业务数据集")
	},
	example="getIqp4LmtAgrFinGuar(cus_id,pageInfo,context,connection)")
	public IndexedCollection getIqp4LmtAgrFinGuar(String cus_id, PageInfo pageInfo, Context context, Connection connection) throws EMPException;	
	
	/**
	 * 通过融资性担保公司授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）
	 * @param cus_id 融资性担保公司客户ID
	 * @param pageInfo 分页信息
	 * @param conditionStr 查询条件
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getHisCont4LmtAgrFinGuar", desc="通过融资性担保公司授信台帐编号获取授信台帐存量业务（目前只查询存量合同信息）",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="融资性担保公司客户ID"),
			@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
			@MethodParam(paramName="conditionStr",paramDesc="查询条件"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	},
	outParam={
			@MethodParam(paramName="IndexedCollection",paramDesc="返回存量合同数据集")
	},
	example="getHisCont4LmtAgrFinGuar(cus_id,pageInfo,conditionStr,context,connection)")
	public IndexedCollection getHisCont4LmtAgrFinGuar(String cus_id, PageInfo pageInfo,String conditionStr, Context context, Connection connection) throws EMPException;
	
	
	/**
	 * 通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计）
	 * @param cus_id 客户ID
	 * @param connection 数据库连接
	 * @return 存量合同集合
	 * @throws EMPException
	 */
	@MethodService(method="getCusBizBalanceByCusId", desc="通过客户码获取贷款余额、贷款累计欠息、银承票面金额、保函金额等（贷后检查统计）",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	},
	outParam={
			@MethodParam(paramName="KeyedCollection",paramDesc="返回存量业务数据集")
	},
	example="getCusBizBalanceByCusId(cus_id,connection)")
	public KeyedCollection getCusBizBalanceByCusId(String cus_id, Connection connection) throws EMPException;
	
	/**
	 * 通过担保合同编号查询担保合同占用金额（最高额担保）
	 * @param guar_cont_no_value 担保合同编号
	 * @param connection 数据库连接
	 * @param context context
	 * @throws EMPException 
	 */
	@MethodService(method="getAmtForGuarCont", desc="通过担保合同编号查询担保合同占用金额（最高额担保）",
			inParam={
			@MethodParam(paramName="guar_cont_no_value",paramDesc="担保合同编号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接"),
			@MethodParam(paramName="context",paramDesc="context")
	},
	outParam={
			@MethodParam(paramName="BigDecimal",paramDesc="占用金额")
	},
	example="getAmtForGuarCont(guar_cont_no_value,context,connection)")
	public BigDecimal getAmtForGuarCont(String guar_cont_no_value,Context context,Connection connection) throws EMPException;
	
	/**
	 * 根据cont_no统计本笔业务的保证金金额
	 * @param cont_no 合同编号
	 * @param drft_amt 票面金额（单张）
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getBailAmt", desc="根据cont_no统计本笔业务的保证金金额",
			inParam={
			@MethodParam(paramName="cont_no",paramDesc="合同编号"),
			@MethodParam(paramName="drft_amt",paramDesc="票面金额（单张）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接"),
			@MethodParam(paramName="context",paramDesc="context")
	},
	outParam={
			@MethodParam(paramName="BigDecimal",paramDesc="保证金金额")
	},
	example="getBailAmt(cont_no,drft_amt,context,connection)")
	public BigDecimal getBailAmt(String cont_no,BigDecimal drft_amt, Context context,Connection connection) throws EMPException;
	
	/**
	 * 根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getLoanBalanceByContNo", desc="根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)",
			inParam={
			@MethodParam(paramName="cont_no",paramDesc="合同编号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接"),
			@MethodParam(paramName="context",paramDesc="context")
	},
	outParam={
			@MethodParam(paramName="BigDecimal",paramDesc="有效台账的总贷款余额")
	},
	example="getLoanBalanceByContNo(cont_no,context,connection)")
	public BigDecimal getLoanBalanceByContNo(String cont_no,Context context,Connection connection) throws Exception;
}
