package com.yucheng.cmis.biz01line.lmt.msi;

import java.math.BigDecimal;
import java.sql.Connection;

import javax.sql.DataSource;

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
 * 授信模块向外提供的接口
 * @author 唐顺岩
 */
@ModualService(modualId="lmt",modualName="授信管理",serviceId="lmtServices",serviceDesc="授信模块对外提供应用服务接口",
		className="com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface")
public interface LmtServiceInterface {
	
	/**
	 * 更改客户额度为不可用，主要用于客户条线变更及集团客户变更。
	 * @param opt_type 操作类型（1-条线变更  2-集团变更）
	 * @param cus_id_str 客户码/集团编号
	 * @param adj_cusIds_bef 变更前名单（操作类型为集团变更时传入）
	 * @param adj_cusIds_aft 变更后名单（操作类型为集团变更时传入）
	 * @return res_value 返回成功(Y)或失败(N)
	 */
	@MethodService(method="updateLmtUnUse", desc="更改客户额度为不可用，主要用于客户条线变更及集团客户变更",
		inParam={
			@MethodParam(paramName="opt_type",paramDesc="操作类型（1-条线变更  2-集团变更）"),
			@MethodParam(paramName="cus_id_str",paramDesc="客户码/集团编号（条线变更传客户码、集团变更传集团编号）"),
			@MethodParam(paramName="adj_cusIds_bef",paramDesc="变更前名单（集团融资模式由'统一授信分配额度'改为'单独授信汇总额度'时必传）"),
			@MethodParam(paramName="adj_cusIds_aft",paramDesc="变更后名单（集团融资模式由'单独授信汇总额度'改为'统一授信分配额度'时必传）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
		},
		outParam={
			@MethodParam(paramName="res_value",paramDesc="返回成功(Y)或失败(N)")
		},
		methodType=MethodType.JAVA,
		example="updateLmtUnUse(opt_type,cus_id_str,adj_cusIds_bef,adj_cusIds_aft,connection)")
	public String updateLmtUnUse(String opt_type,String cus_id_str,String adj_cusIds_bef,String adj_cusIds_aft,Connection connection) throws EMPException;
	
	/**
	 * 查询客户是否存在有效授信
	 * @param cus_id 客户码
	 * @param cus_type 客户类型（1-单一法人  2-同业客户）
	 * @return res_value 返回是否存在有效授信（1-是  2-否）
	 */
	@MethodService(method="searchIsExistLmt", desc="查询客户是否存在有效授信",
		inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="cus_type",paramDesc="客户类型（1-单一法人  2-同业客户）")
		},
		outParam={
			@MethodParam(paramName="res_value",paramDesc="是否存在有效授信（1-是  2-否）")
		},
		methodType=MethodType.JAVA,
		example="searchIsExistLmt(cus_id,cus_type)")
	public String searchIsExistLmt(String cus_id,String cus_type) throws EMPException;
	
	
	/**
	 * 根据查询查询类型（1-存量授信 2-否决历史）查询客户存量授信或否决历史
	 * @param cus_id 客户码
	 * @param select_type 查询类型（1-存量授信 2-否决历史）
	 * @param connection 数据库连接
	 * @return iColl IndexedCollection数据集
	 */
	@MethodService(method="searchLmtAgrInfoList", desc="根据查询查询类型（1-存量授信 2-否决历史）查询客户存量授信或否决历史",
		inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="select_type",paramDesc="查询类型（1-存量授信 2-否决历史）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
		},
		outParam={
			@MethodParam(paramName="IndexedCollection",paramDesc="返回数据集")
		},
		methodType=MethodType.JAVA,
		example="searchLmtAgrInfoList(cus_id,select_type,connection)")
	public IndexedCollection searchLmtAgrInfoList(String cus_id,String select_type,Connection connection) throws EMPException;
	
	
	/**
	 * 根据客户码、分项类型（01-一般授信05-供应链授信）查询客户授信协议信息
	 * @param cusId 客户码
	 * @param subType 分项类型（01-一般授信05-供应链授信）
	 * @param connection 数据库连接
	 * @return kColl KeyedCollection数据集
	 */
	@MethodService(method="getLmtAgrInfoMsg", desc="根据客户码、分项类型（01-一般授信05-供应链授信）查询客户授信协议信息",
		inParam={
			@MethodParam(paramName="cusId",paramDesc="客户码"),
			@MethodParam(paramName="subType",paramDesc="分项类型（01-一般授信05-供应链授信）"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		methodType=MethodType.JAVA,
		outParam=@MethodParam(paramName="KeyedCollection",paramDesc="授信协议kColl"))
	public KeyedCollection getLmtAgrInfoMsg(String cusId,String subType, Connection connection)throws EMPException;
	
	
	/**
	 * 根据客户码、分项类型（01-一般授信05-供应链授信）、核心企业客户码（05-供应链授信 时为必传参数）查询授信台账记录
	 * @param cusId 客户码
	 * @param subType 分项类型（01-一般授信05-供应链授信）
	 * @param coreCorpCusId 核心企业客户码（05-供应链授信 时为必传参数）
	 * @param connection 数据库连接
	 * @return iColl IndexedCollection数据集
	 */
	@MethodService(method="searchLmtAgrDetailsList", desc="根据客户码、分项类型（01-一般授信05-供应链授信）查询授信台账记",
		inParam={
			@MethodParam(paramName="cusId",paramDesc="客户码"),
			@MethodParam(paramName="subType",paramDesc="分项类型（01-一般授信05-供应链授信）"),
			@MethodParam(paramName="coreCorpCusId",paramDesc="核心企业客户码（05-供应链授信 时为必传参数）"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		methodType=MethodType.JAVA,
		outParam=@MethodParam(paramName="KeyedCollection",paramDesc="授信协议kColl"))
	public IndexedCollection searchLmtAgrDetailsList(String cusId,String subType, String coreCorpCusId, Connection connection)throws EMPException;
	
	
	/**
	 * 根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额
	 * @param agrNo	授信协议号（单一法人为授信额度编码）
	 * @param subType	授信类型（01-一般授信 02-同业授信 03-第三方授信）
	 * @param connection	数据库连接
	 * @return String	返回授信总金额
	 */
	@MethodService(method="searchLmtAgrAmtByAgrNO", desc="根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额",
		inParam={
			@MethodParam(paramName="agrNo",paramDesc="授信协议号（单一法人为授信额度编码）"),
			@MethodParam(paramName="lmtType",paramDesc="授信类型（01-一般授信 02-同业授信 03-第三方授信）"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		methodType=MethodType.JAVA,
		outParam=@MethodParam(paramName="String",paramDesc="金额"))
	public String searchLmtAgrAmtByAgrNO(String agrNo,String lmtType, Connection connection)throws EMPException;

	/**
	 * 授信项下担保合同签订时实时更新授信启用金额
	 * @param limit_code	授信额度编码
	 * @param guar_amt		担保金额
	 * @param connection	数据库连接
	 */
	@MethodService(method="updateLmtEnableamt", desc="授信项下担保合同签订时实时更新授信启用金额",
		inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信额度编码"),
			@MethodParam(paramName="guar_amt",paramDesc="担保金额"),
			@MethodParam(paramName="type",paramDesc="分项类别（01-其他，02-联保）"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		outParam={ },
		methodType=MethodType.JAVA,
		example="updateLmtEnableamt(String limit_code,BigDecimal guar_amt, Connection connection)")
	public void updateLmtEnableamt(String limit_code,BigDecimal guar_amt,String type, Connection connection)throws EMPException;
	
	/**
	 * 授信项下担保合同注销时实时更新授信启用金额
	 * @param limit_code	授信额度编码
	 * @param guar_amt		担保金额
	 * @param connection	数据库连接
	 */
	@MethodService(method="updateLmtEnableamtOff", desc="授信项下担保合同注销时实时更新授信启用金额",
		inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信额度编码"),
			@MethodParam(paramName="guar_amt",paramDesc="担保金额"),
			@MethodParam(paramName="type",paramDesc="分项类别（01-其他，02-联保）"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		outParam={ },
		methodType=MethodType.JAVA,
		example="updateLmtEnableamtOff(String limit_code,BigDecimal guar_amt, Connection connection)")
	public void updateLmtEnableamtOff(String limit_code,BigDecimal guar_amt,String type, Connection connection)throws EMPException;
	
	/**
	 * 根据授信额度编号查询关联担保合同
	 * @param limit_code	授信额度编码
	 * @param pageInfo	         分页
	 * @param dataSource	
	 */
	@MethodService(method="searchGuarContByLimitCode", desc="根据授信额度编号查询关联担保合同",
		inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信额度编码"),
			@MethodParam(paramName="pageInfo",paramDesc="分页"),
			@MethodParam(paramName="dataSource",paramDesc="数据库连接")
		},
		outParam={ },
		methodType=MethodType.JAVA,
		example="searchGuarContByLimitCode(String limit_code,PageInfo pageInfo,DataSource dataSource)")
	public IndexedCollection searchGuarContByLimitCode(String limit_code, PageInfo pageInfo,DataSource dataSource)throws EMPException;	
	
	/**
	 * 新增担保合同后保存授信担保合同关系表
	 * @param kColl	授信担保合同关系集合  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 是否保存成功
	 * @throws EMPException
	 */
	@MethodService(method="addGrtLoanRGur",desc="新增担保合同后保存授信担保合同关系表",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="授信担保合同关系集合"),
			@MethodParam(paramName="guarType",paramDesc="担保合同类型"),
			@MethodParam(paramName="context",paramDesc="上下文"),  
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="String",paramDesc="返回是否保存成功"))
	public int addRLmtAppGuarCont(KeyedCollection kColl,String guarType,Context context, Connection connection) throws Exception ;  
	
	/**
	 * 修改查看担保合同查询授信担保合同关系表
	 * @param limit_code   授信担保合同关系表 联合主键
	 * @param guar_cont_no 授信担保合同关系表 联合主键  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 授信担保合同kColl
	 * @throws EMPException
	 */
	@MethodService(method="selectGetLoanRGur",desc="修改查看担保合同查询授信担保合同关系表",
			inParam={ 
			@MethodParam(paramName="limit_code",paramDesc="授信担保合同关系表 联合主键"),   
			@MethodParam(paramName="guar_cont_no",paramDesc="授信担保合同关系表 联合主键"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="授信担保合同kColl")) 
	public KeyedCollection selectRLmtAppGuarCont(String limit_code,String guar_cont_no, Context context, Connection connection) throws Exception ;
	
	
	/**
	 * 修改删除担保合同查询授信担保合同关系表此担保合同条数
	 * @param guar_cont_no	担保合同编号  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同kColl
	 * @throws EMPException
	 */
	@MethodService(method="checkRLmtAppGuarContNum",desc="修改删除担保合同查询授信担保合同关系表此担保合同条数",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="int",paramDesc="业务合同表中此担保合同条数")) 
	public int checkRLmtAppGuarContNum(String guar_cont_no, Context context, Connection connection) throws Exception ;
	
	
	/**
	 * 根据联保授信协议查询联保小组成员信息
	 * @param joint_agr_no	联保协议编号
	 * @param connection 数据库连接
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	@MethodService(method="searchLmtJointNameList", desc="根据联保授信协议查询联保小组成员信息",
		inParam={
			@MethodParam(paramName="joint_agr_no",paramDesc="联保协议编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
		},
		methodType=MethodType.JAVA,
		outParam=@MethodParam(paramName="KeyedCollection",paramDesc="授信协议kColl"))
	public IndexedCollection searchLmtJointNameList(String joint_agr_no,Connection connection)throws EMPException;
	
	/**
	 * 生成联保授信协议与担保合同关系
	 * @param guar_cont_no	担保合同编号  
	 * @param joint_agr_no	联保协议编号
	 * @param amt			担保合同金额
	 * @param is_per_gur	是否阶段性担保
	 * @param is_add_guar	是否追加
	 * @param context 		上下文
	 * @param connection 	数据库连接
	 * @return boolean
	 * @throws EMPException
	 */
	@MethodService(method="createRLmtGuarContByJoint",desc="生成联保授信协议与担保合同关系",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="joint_agr_no",paramDesc="联保协议编号"),
			@MethodParam(paramName="amt",paramDesc="担保合同金额"),
			@MethodParam(paramName="is_per_gur",paramDesc="是否阶段性担保"),
			@MethodParam(paramName="is_add_guar",paramDesc="是否追加"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="boolean",paramDesc="是否成功")) 
	public boolean createRLmtGuarContByJoint(String guar_cont_no, String joint_agr_no ,String amt,String is_per_gur,String is_add_guar,Context context,Connection connection)throws Exception;
	
	/**
	 * 根据授信额度编码串查询授信额度详细信息
	 * @param limitCodeStr	授信额度编码串  
	 * @param pageInfo	数据分页对象
	 * @param dataSource 	数据连接池对象
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	@MethodService(method="queryLmtAgrDetailsByLimitCodeStr",desc="生成联保授信协议与担保合同关系",
			inParam={ 
			@MethodParam(paramName="limitCodeStr",paramDesc="授信额度编码串"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="授信信息列表")) 
	public IndexedCollection queryLmtAgrDetailsByLimitCodeStr(String limitCodeStr,PageInfo pageInfo,DataSource dataSource)throws Exception;
	/**
	 * 根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加
	 * @param guar_cont_no	担保合同编号  
	 * @param type	入口，是授信模块进入还是担保管理(不为空时为担保管理进入)
	 * @return KeyedCollection
	 * @throws EMPException
	 */
	@MethodService(method="queryRLmtGuarContInfo",desc="根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="type",paramDesc="入口，是授信模块进入还是担保管理(不为空时为担保管理进入)"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="返回担保合合同与授信关系表数据")) 
	public KeyedCollection queryRLmtGuarContInfo(String guar_cont_no,String type,Connection connection)throws Exception;
	
	/**
	 * 根据授信品种编号获取授信下的担保编号列表
	 * @param limitCodeStr 授信品种编号列，表字符串类型为：'1212','21121'
	 * @param pageInfo 翻页信息
	 * @param dataSource 数据库连接池
	 * @return 担保合同列表,表字符串类型为：'1212','21121'
	 * @throws Exception
	 */
	@MethodService(method="queryGuarNoListByLimiCodeList",desc="根据授信品种编号获取授信下的担保编号列表",
			inParam={ 
			@MethodParam(paramName="limitCodeStr",paramDesc="授信额度编码串"),
			@MethodParam(paramName="pageInfo",paramDesc="翻页信息"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接池")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="String",paramDesc="担保编号列表")) 
	public String queryGuarNoListByLimiCodeList(String limitCodeStr, PageInfo pageInfo,DataSource dataSource) throws Exception; 
	
	/**
	 * 提供业务授信审批意见的serno(单一法人授信)
	 * @param cus_line
	 * @param limit_acc_no
	 * @return serno
	 * @throws Exception
	 */
	@MethodService(method="getSernoForIqpAcc",desc="提供业务授信审批意见的serno(单一法人授信)",
			inParam={ 
			@MethodParam(paramName="cus_line",paramDesc="客户条线"),
			@MethodParam(paramName="limit_acc_no",paramDesc="授信台帐编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接池")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="String",paramDesc="担保编号列表")) 
	public String getSernoForIqpAcc(String cus_line,String limit_acc_no,Connection connection) throws Exception; 
	
	/**
	 * 提供业务授信审批意见的serno(合作方授信)
	 * @param limit_credit_no
	 * @return serno
	 * @throws Exception
	 */
	@MethodService(method="getSernoForIqpCredit",desc="提供业务授信审批意见的serno(合作方授信)",
			inParam={ 
			@MethodParam(paramName="limit_credit_no",paramDesc="第三方授信编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接池")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="String",paramDesc="担保编号列表")) 
	public String getSernoForIqpCredit(String limit_credit_no,Connection connection) throws Exception;
	
	/**
	 * 根据圈商协议编号获取圈商信息(圈商)
	 * @param agr_no
	 * @return KeyedCollection
	 * @throws Exception
	 */
	@MethodService(method="getLmtAgrBizArea",desc="根据圈商协议编号获取圈商信息(圈商)",
			inParam={
			@MethodParam(paramName="agr_no",paramDesc="圈商协议编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="圈商信息")) 
	public KeyedCollection getLmtAgrBizArea(String agr_no,Connection connection) throws Exception;
	
	/**
	 * 根据圈商协议编号获取圈商下有效成员(圈商)
	 * @param agr_no
	 * @return IndexedCollection
	 * @throws Exception
	 */
	@MethodService(method="getLmtAgrBizAreaMember",desc="根据圈商协议编号获取圈商信息(圈商)",
			inParam={
			@MethodParam(paramName="agr_no",paramDesc="圈商协议编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="圈商成员信息")) 
	public IndexedCollection getLmtAgrBizAreaMember(String agr_no,Connection connection) throws Exception;
	
	/**
	 * 根据合作方客户码获取合作方信息
	 * @param cus_id
	 * @return KeyedCollection
	 * @throws Exception
	 */
	@MethodService(method="getAgrCoopInfo",desc="根据合作方客户码获取合作方信息",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="合作方客户码"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="合作方信息")) 
	public KeyedCollection getAgrCoopInfo(String agr_no,Connection connection) throws Exception; 
	/**
	 * 根据担保合同编号获取其与授信关联信息
	 * @param guar_cont_no	担保合同编号
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	@MethodService(method="getLmtGuarReByGuarContNo",desc="根据担保合同编号获取其与授信关联信息",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="授信与担保合同关联表信息列表")) 
	public IndexedCollection getLmtGuarReByGuarContNo(String guar_cont_no,Connection connection )throws Exception;
	
	/**
	 * 根据担保合同编号获取其与授信关联信息（授信与担保合同关系表[申请表]）
	 * @param guar_cont_no	担保合同编号
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	@MethodService(method="getLmtAppGuarReByGuarContNo",desc="根据担保合同编号获取其与授信关联信息（授信与担保合同关系表[申请表]）",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="授信与担保合同关联表信息列表")) 
	public IndexedCollection getLmtAppGuarReByGuarContNo(String guar_cont_no,Connection connection )throws Exception;
	
	/**
	 * 根据担保合同编号删除担保合同与授信关联关系表记录（结果表）
	 * @param guar_cont_no
	 * @return int
	 * @throws Exception
	 */
	@MethodService(method="deleteRLmtGuarCont",desc="根据担保合同编号删除担保合同与授信关联关系表（结果表）",
			inParam={
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="int",paramDesc="所删除记录条数")) 
	public int deleteRLmtGuarCont(String guar_cont_no,Connection connection) throws Exception;
	
	/**
	 * 根据授信额度编号查询授信与担保关系表
	 * @param limit_code	授信额度编码
	 * @param Context	
	 * @param connection	
	 */
	@MethodService(method="getRLmtGuarContByLimitCode",desc="根据授信额度编号查询授信与担保关系表",
			inParam={
			@MethodParam(paramName="limit_code",paramDesc="授信额度编码"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="授信与担保关系")) 
	public IndexedCollection getRLmtGuarContByLimitCode(String limit_code,Context context, Connection connection) throws Exception;
	
	
	/**
	 * 根据担保合同编号更新授信和担保合同关系表状态(结果表)
	 * @param guar_cont_no	担保合同编号  
	 * @param connection	
	 * @return Int
	 * @throws EMPException
	 */
	@MethodService(method="updateLmtGuarStatus",desc="根据担保合同编号更新授信和担保合同关系表状态(结果表)",
			inParam={
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="Int",paramDesc="更新条数")) 
	public int updateLmtGuarStatus(String guar_cont_no,Connection connection) throws Exception;
	
}
