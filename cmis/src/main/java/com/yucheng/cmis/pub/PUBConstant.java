package com.yucheng.cmis.pub;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的作用是为方便应用中常量的管理，提供更加有效的常量存储机制，方便应用系统中类的调用，避免 程序中过多的出现硬编码。
 * 
 * @Classname com.yucheng.cmis.pub.PUBConstant.java
 * @author wqgang
 * @Since 2008-9-11 上午09:22:00
 * @Copyright yuchengtech
 * @version 1.0
 */
public class PUBConstant {
	
	//序列服务日志服务器
	public static final String  EMPSEQ="EMPSEQ";
	/**
	 * 用户状态:注销
	 * */
	public static final String userCancel = "0";
	//操作人
	public static final String  loginuserid = "currentUserId";
	//操作人机构
	public static final String  loginorgid = "organNo";
	//当前用户
	public static final String  currentUserId = "currentUserId";
	//当前机构
	public static final String  organNo = "organNo";
	/**
	 * 用户状态:生效
	 * */
	public static final String userAvailable = "1";	
	/**add by lisj 2014-12-19 需求编号：【FX140619013】 十二级系统查看客户信息功能（系统通用跨域访问方法）begin**/
	//新信贷测试环境的地址IP:168.168.241.89:7001，上线需更改该地址为生产地址，否则十二级分类子系统无法访问新信贷系统个数据
	public static final String  CMISURL = "http://168.168.214.54:7001/cmis";
	/**add by lisj 2014-12-19 需求编号：【FX140619013】 十二级系统查看客户信息功能（系统通用跨域访问方法）end**/
	/**
	 * Operation 中doExecute方法返回值
	 */

	public static final String SUCCESS = "success"; // 成功
	public static final String FAIL = "fail"; // 失败
	public static final String EXISTS = "exists"; // 存在
	public static final String NOTEXISTS = "notexists"; // 不存在

	public static final String YES = "1"; // 是
	public static final String NO = "2"; // 否
   	/** 公用组件	 */
	public final static String PUBOPERA = "PubOpera";
	/**
	 * 资产保全
	 */
	public static final String ARPSEQBUILDER = "ArpSeqBuilderComponent"; // 法律诉讼SEQ
																			// 生成器
	/**
	 * 客户基本信息
	 */
	
	public static final String CUSBASE = "CusBase";
	public static final String CUSBLKCHECKINAPP = "CusBlkCheckinapp"; // 不宜贷款户进入申请
	public static final String CUSBLKLOGOUTAPP = "CusBlkLogoutapp"; // 不宜贷款户注销申请
	// 智能关联搜索里的component名称
	public static final String CusComRelComponent = "CusComIntellectRel";
	public static final String CusIndivRelComponent = "CusComIntellectRel";
	/**
	 * 对公部分(需要引用的地方有 组件、代理类生成ID,表模型ID等)
	 */

	public static final String CUSCOM = "CusCom"; // 客户基本信息表

	public static final String CUSCOMCUSID = "cus_id"; // 客户基本信息表 主关键字 客户ID

	public static final String CUSCOMMANAGER = "CusComManager"; // 对公客户高管信息表

	public static final String CUSCOMAPTITUDE = "CusComAptitude"; // 对公客户资质信息表

	public static final String CUSCOMMNGFAMILY = "CusComMngFamily"; // 对公客户法人家族信息表

	public static final String CUSCOMRESSET = "CusComResset"; // 对公客资产信息表

	public static final String CUSCOMFINABOND = "CusComFinaBond"; // 对公客户融资债券信息表

	public static final String CUSCOMFINASTOCK = "CusComFinaStock"; // 对公客户融资股票信息表

	public static final String CUSCOMRELOTHER = "CusComRelOther"; // 与其他企业关联情况表

	public static final String CUSCOMRELINVEST = "CusComRelInvest"; // 对外投资表

	public static final String CUSCOMRELAPITAL = "CusComRelApital"; // 资本构成表

	public static final String CUSOBISDEPOSIT = "CusObisDeposit"; // 他行交易－他行存款

	public static final String CUSOBISLOAN = "CusObisLoan"; // 他行交易－他行贷款

	public static final String CUSOBISASSURE = "CusObisAssure"; // 他行交易－他行担保

	public static final String CUSDEPTINITEM = "CusDeptInItem"; // 我行交易－我行存款情况

	public static final String CUSEVENT = "CusEvent"; // 客户重大事件

	public static final String CUSCOMREL = "CusComRel";// 关联信息

	public static final String CUSHANDOVERAPP = "CusHandoverApp";// 客户移交申请
	
	public static final String CUSHANDOVERCFG = "CusHandoverCfg";// 客户移交配置

	public static final String CUSHANDOVERLOG = "CusHandoverLog";// 客户移交日志

	public static final String CUSHANDOVERLST = "CusHandoverLst";// 客户移交明细

	public static final String CUSTRUSTEEAPP = "CusTrusteeApp";// 客户托管申请

	public static final String CUSTRUSTEELOG = "CusTrusteeLog";// 客户托管日志

	public static final String CUSTRUSTEELST = "CusTrusteeLst";// 客户托管明细

	public final static String MODIFY_HISTORY_COMPONENT = "ModifyHistory";// 修改历史记录

	public static final String CusLoanRelIface = "CusLoanRelIface";

	public static final String IIQPCUSLOANREL = "IIqpCusLoanRel";// 客户共享申请接口
	/**
	 * 对私部分(需要引用的地方有 组件、代理类生成ID,表模型ID等)
	 * 
	 * 
	 */

	public static final String CUSINDIV = "CusIndiv"; // 个人客户基本信息

	public static final String CUSINDIVRESUME = "CusIndivResume"; // 个人履历

	public static final String CUSINDIVENT = "CusIndivEnt"; // 个人经营情况

	// public static final String CUSINIV = "CusIndiv"; // 个人客户基本信息

	public static final String CUSINDIVFAMBIC = "CusIndivFamBlc"; // 个人客户家庭收支及资产负债

	public static final String CUSINDIVINVTENTERPRISE = "CusIndivInvtEnterprise"; // 个人投资企业信息

	public static final String CUSINDIVSOCREL = "CusIndivSocRel"; // 个人社会关系

	public static final String CUSINDIVENTERPRISE = "CusIndivEnterprise"; // 个人经营情况

	public static final String CUSINDIVOTHASSETS = "CusIndivOthAssets"; // 其他资产情况

	public static final String CUSFARFAMPRODUCE = "CusFarFamProduce"; // 农户生产情况

	public static final String CUSINDIVINSU = "CusIndivInsu"; // 商业保险情况

	public static final String CUSINDIVHOUASSETS = "CusIndivHouAssets"; // 家庭房屋资产情况

	public static final String CUSFARHOUASSETS = "CusFarHouAssets"; // 房屋资产（农户）

	public static final String CUSFARFAMBLC = "CusFarFamBlc"; // 家庭收支情况（农户）

	public static final String CUSINDIVBOND = "CusIndivBond"; // 持有金融债券

	public static final String CUSINDIVFAMLBY = "CusIndivFamLby"; // 负债情况

	public static final String CUSINDIVVEHASSETS = "CusIndivVehAssets"; // 车辆资产情况

	public static final String CUSLOANREL = "CusLoanRel"; // 客户信贷关系信息

	public static final String CUSLOANRELLOG = "CusLoanRelLog"; // 客户信贷关系变更信息

	public static final String CUSINDIVTAX = "CusIndivTax"; // 纳税情况

	public static final String CUSINDIVINCOME = "CusIndivIncome"; // 纳税情况

	public static final String CUSFARCCRBAT = "CusFarCcrBat"; // 个人农户批量评级信息

	public static final String CUSGRPINFO = "CusGrpInfo"; // 集团客户基本信息
	
	public static final String CUSGRPINFOAPPLY = "CusGrpInfoApply"; // 集团客户申请基本信息
	
	public static final String CUSGRPINFOAPPLYCOMPONENT = "CusGrpInfoApplyComponent"; // 集团客户申请基本信息组件
	
	public static final String CUSGRPINFOAPPLYAGENT = "CusGrpInfoApplyAgent"; // 集团客户申请基本信息代理

	public static final String CUSGRPMEMBER = "CusGrpMember"; // 集团客户成员
	
	public static final String CUSGRPMEMBERAPPLY = "CusGrpMemberApply"; // 集团客户申请成员
	
	public static final String CUSGRPMEMBERAPPLYCOMPONENT = "CusGrpMemberApplyComponent"; // 集团客户成员申请基本信息组件
	
	public static final String CUSGRPMEMBERAPPLYAGENT = "CusGrpMemberApplyAgent"; // 集团客户申请成员代理

	public static final String CUSSAMEORG = "CusSameOrg"; //同业客户
	
	public static final String CUSGRT = "CusGrt"; //客户对外担保情况
	public static final String CUSGRPIFACE = "CusGrpIface";	//集团客户接口类
	public static final String CUSBLKIFACE = "CusBlkIface";
	/**
	 * 财务报表部分(需要引用的地方有 组件、代理类生成ID,表模型ID等)
	 * 
	 * 
	 */

	public static final String FNCCONFITEMS = "FncConfItems"; // 报表配置项目列表信息

	public static final String FNCCONFSTYLES = "FncConfStyles"; // 报表样式列表信息

	public static final String FNCCONFDEFFORMAT = "FncConfDefFmt"; // 报表配置定义表

	public static final String FNCCONFTEMPLATE = "FncConfTemplate"; // 财务报表列表

	public static final String FNCINVENTORY = "FncInventory"; // 财务报表明细信息—主要存货明细

	public static final String FNCINVESTMENT = "FncInvestment"; // 财务报表明细信息—主要长期投资明细

	public static final String FNCFIXEDASSET = "FncFixedAsset"; // 财务报表明细信息—主要固定资产明细

	public static final String FNCASSURE = "FncAssure"; // 要对外担保及表外业务明细

	public static final String FNCACCPAYABLE = "FncAccPayable"; // 应付账款及帐龄分析

	public static final String FNCACCRECEIVABLE = "FncAccReceivable"; // 应收账款及帐龄分析

	public static final String FNCOTHERRECEIVABLE = "FncOtherReceivable"; // 其它应收款明细

	public static final String FNCOTHERPAYABLE = "FncOtherPayable"; // 其它应付款明细

	public static final String FNCLOAN = "FncLoan"; // 主要借款明细

	public static final String FNCPROJECT = "FncProject"; // 在建工程明细

	public static final String FNCORDEBT = "FncOrDebt"; // 或有负债

	public static final String FNCDETAILBASE = "FncDetailBase"; // 报表明细基表

	/**
	 * 财务报表主要报表
	 */
	public static final String FNCSTATBASE = "FncStatBase"; // 公司客户报表
	public static final String FNCSTATBASE07 = "FncStatBase07"; // 公司客户报表

	public static final String FNCSTATBS = "FncStatBs"; // 资产负债表

	public static final String FNCSTATIS = "FncStatIs"; // 损益表

	public static final String FNCSTATCFS = "FncStatCfs"; // 现金流量表

	public static final String FNCINDEXRPT = "FncIndexRpt"; // 财务指标表

	public static final String FNCSTATCOMMON = "FncStatCommon"; // 通用财务报表

	public static final String FNC4QC = "Fnc4QueryComponent";// 财报查询
	
	public static final String FNC_PB0001 = "PB0001";// 2002版财报
	public static final String FNC_PB0005 = "PB0005";// 新会计准则财报

	/**
	 * 贷款部分需要引用的地方有 组件、代理类生成ID,表模型ID等)
	 */
	public static final String IQPCOMMAPPINFO = "IqpCommAppInfo";
	public static final String IQPDISCOUNT = "IqpDiscount";
	public static final String IQPDISCOUNTINFO = "IqpDiscountInfo";
	public static final String IQPSYNDICATEDINFO = "IqpSyndctdInfo";
	public static final String IQPLOANAPP = "IqpLoanApp";
	public static final String IQPDISCAPP = "IqpDisc";
	public static final String IQPDISCBILLLIST = "IqpDiscBillList";
	public static final String IQPACCPAPP = "IqpAccpApp";
	public static final String ArpBdConApp = "ArpBdConApp";
	public static final String IQPCVRGAPP = "IqpCvrgApp";
	public static final String IQPLOANEXTEND = "IqpLoanExtend";

	public static final String IQPREPAYSTRATEGYINFO = "IqpRepayTerm";// 还款策略
	public static final String IQPREPAYPLANINFO = "IqpRepayPlan";// 还款计划

	// 贷款申请接口
	public static String IQP4PVP = "Iqp4Pvp";
	public static String IQPINTERFACE = "IqpInterface";

	// 票据批次信息
	public static String IQPBATCH = "IqpBatch";

	// 批次票据明细
	public static String IQPBATCHBILLLST = "IqpBatchBillLst";

	// 转贴现转出(再贴现)申请信息
	public static String IQPREDISCOUT = "IqpRediscout";
	/**
	 * 组件
	 */

	public static final String CUSCOMCOMPONENT = "CusCom";

	/**
	 * 代理
	 */
	public static final String CUSCOMAGENT = "CusCom";

	public static final String CUSINDIVSOCRELCOMPONENT = "CusIndivSocRelComponent";

	public static final String CUSINDIVSOCRELAGENT = "CusIndivSocRelAgent";

	public static final String CUSINDIVBONDCOMPONENT = "CusIndivBondComponent";

	public static final String CUSINDIVBONDAGENT = "CusIndivBondAgent";

	public static final String CUSINDIVINSURANCESCOMPONENT = "CusIndivInsurancesComponent";

	public static final String CUSINDIVINSURANCESAGENT = "CusIndivInsurancesAgent";

	/**
	 * 产品基础信息
	 */

	public static final String PRDBASICINFO = "PrdBasicinfo";

	public static final String PRDBASICINFOCOMPONENT = "PrdBasicinfoComponet";

	public static final String PRDBASICINFOAGENT = "PrdBasicinfoAgent";

	public static final String PRDBASICINFOACPT = "PrdBasicinfoAcpt";

	public static final String PRDORGAPPLY = "PrdOrgApply";

	public static final String PRDORGAPPLYCOMPONENT = "PrdOrgapply";

	public static final String PRDORGAPPLYAGENT = "PrdOrgapply";

	/*
	 * 贷前调用产品借口，调用类
	 */

	public static final String PRD4IQP = "Prd4Iqp";
	public static final String PRDACCOUNT = "PrdAccount";

	/**
	 *文件路径
	 */

	public static String COMPONENT_CONFIG_FILM_DIR = "D:\\svnspace\\CMIS\\src\\com\\yucheng\\cmis\\config\\";

	/**
	 * 产品管理
	 */
	// 产品文档模板
	public static final String PRDDOCTEMP = "PrdDocTemp";
	// 还款方式
	public static final String PRDREPAYMODESTD = "PrdRepayModeStd";
	// 还款方式策略
	public static final String PRDAMORMODESTD = "PrdAmorModeStd";
	// 利率设置
	public static final String PRDRATESTD = "PrdRateStd";
	// add by zhangming 风险拦截项目
	public static String PRDPVRISKITEM = "PrdPvRiskItem";
	// add by zhangming 风险拦截方案
	public static String PRDPREVENTRISK = "PrdPreventRisk";
	// add by zhangming 风险拦截场景
	public static String PRDPVRISKSCENE = "PrdPvRiskScene";
	// add by zhangming 流程机构配置
	public static String WFIWORKFLOW2ORG = "WfiWorkflow2Org";
	// add by zhangming 产品科目配置
	public static String PRDACCOUNTMAP = "PrdAccountMap";
	// add by zhangming 科目列表
	public static String PRDACCOUNTLIST = "PrdAccountList";

	// add by yaomh 贷款合同
	public static String CTRLOANCONT = "CtrLoanCont";

	// add by yaomh 贷款合同接口
	public static String CTRCONTIMPL = "CtrContImpl";

	// add by yaomh 展期协议
	public static String CTRRENEWCONT = "CtrRenewCont";

	// add by small 银承协议
	public static String CTRACCPCONT = "CtrAccpCont";

	// add by small 贴现协议
	public static String CTRDISCCONT = "CtrDiscCont";

	// add by small 转贴现转入协议
	public static String CTRREDISCIN = "CtrReDiscIn";

	// add by small 转贴现转出协议
	public static String CTRREDISCOUT = "CtrReDiscOut";

	// add by small 保函协议
	public static String CTRCVRGCONT = "CtrCvrgCont";

	// add by wak 展期协议
	public static String CTRLOANEXTEND = "CtrLoanExtend";

	// add by small 担保合同
	public static String CTRGUARCONT = "CtrGuarCont";

	// add by small 抵押物基础信息
	public static String GRTGBASICINFO = "GrtGBasicInfo";

	// 抵质押物出入库组件
	public static String GRT_IN_AND_OUT_COMPONENT = "GrtInAndOutComponent";

	// 抵质押物出入库代理类
	public static String GRT_IN_AND_OUT_AGENT = "GrtInAndOutAgent";

	// add by small 质押物基础信息
	public static String GRTPBASICINFO = "GrtPBasicInfo";

	// add by small 担保合同
	public static String GRTGUARCONT = "GrtGuarCont";

	// add by zhangming 风险拦截应用
	public static String IQPPVRISKRESULT = "IqpPvRiskResult";

	// 风险拦截准备参数
	public static String PRDPREPAREAGENT = "PrepareParam";

	/**
	 * 风险预警
	 */
	// 预警方案阀值
	public static final String REWTHRESHOLD = "RewThreshold";
	// 预警方案配置
	public static final String REWSCHEME = "RewScheme";
	// 预警方案模板参数
	public static final String REWTEMPPARM = "RewTempParm";

	// 风险预警消息处理
	public static final String REWSCHEME4INFO = "RiskManage4Info";

	// 风险消息相关domain

	public static final String REWMSGORG = "RewMsgOrg";
	// REW_MSG_CUS
	public static final String REWMSGCUS = "RewMsgCus";
	// REW_MSG_BIZ
	public static final String REWMSGBIZ = "RewMsgBiz";
	// REW_MSG_REALTIME
	public static final String REWMSGREALTIME = "RewMsgRealtime";
	/**
	 * 第三方合作
	 */
	public static final String PrjCooperateAppAgent = "PrjCooperateApp";
	public static final String PrjCooperateAppComponent = "PrjCooperateApp";

	public static final String PrjCopAccAgent = "PrjCopAcc";
	public static final String PrjCopAccComponent = "PrjCopAcc";

	public static final String PrjCopCgAppAgent = "PrjCopCgApp";
	public static final String PrjCopCgAppComponent = "PrjCopCgApp";
	/**
	 * 贷款定价
	 */
	public final static String LPC_MODEL = "LpcModel";

	/**
	 * 担保管理部分(需要引用的地方有 组件、代理类生成ID,表模型ID等)
	 * 
	 * 
	 */
	public final static String GrtAppRelation = "GrtAppRelation";
	public static String GRT_AUTOKEY = "GrtAutoKeyComponent";
	public final static String GrtBase = "GrtBase";
	public final static String GrtImpl = "GrtImpl";

	public final static String GrtMaxGuarContComponent = "GrtMaxGuarContComponent";
	public final static String GrtMaxGuarContAgent = "GrtMaxGuarContAgent";
	public final static String GrtAppRelationAgent = "GrtAppRelation";
	public final static String GrtGuarantessDao = "GrtGuarantessDao";
	public final static String GrtMaxGuarCont = "GrtMaxGuarContDao";
	/**
	 * 担保变更后处理
	 */
	public final static String GrtFlow = "GrtFlow";
	public final static String GrtAftChgComponent = "GrtAftChgComponent";
	public final static String GrtAftChgDao = "GrtAftChgDao";
	public final static String GrtAftChgAgent = "GrtAftChgAgent";
	// 联保人配制------------------------------
	public final static String GrtCosuretyContComponent = "GrtCosuretyContComponent";
	public final static String GrtCosuretyContAgent = "GrtCosuretyContAgent";
	public final static String GrtCosuretyContDao = "GrtCosuretyContDao";
	// ----------------------------------------

	/**
	 *额度管理部分
	 */
	public final static String LmtGrtRel = "LmtGrtRel";
	public final static String LmtAgrDetails = "LmtAgrDetails";
	public final static String LmtContRel = "LmtContRel";
	public final static String LMTARGILC = "LmtArgiLc";
	public final static String LMTARGILCANN = "LmtArgiLcAnn";
	public final static String LMTARGILCCHG = "LmtArgiLcChg";

	// 贷款出账申请------------------------------
	public final static String PVPLOANAPP = "PvpLoanApp"; // 贷款
	public final static String PVPLOANEXTEND = "PvpLoanExtend"; // 贷款展期
	public final static String PVPACCPAPP = "PvpAccpApp"; // 银承
	public final static String PVPACCPAPPLIST = "PvpAccpAppList"; // 银承-票据明细
	public final static String PVPDISCAPP = "PvpDiscApp"; // 贴现
	public final static String PVPCVRGAPP = "PvpCvrgApp"; // 保函
	public final static String PVPAUTHORIZE = "PvpAuthorize"; // 出帐授权
	// ----------------------------------------

	// 台帐------------------------------
	public final static String ACC = "Account"; // 台帐
	public final static String ACCLOAN = "AccLoan"; // 贷款台帐
	public final static String ACCLOANREVOLVER = "AccLoanRevolver";// 循环贷款台帐
	public final static String ACCLOANREVSUB = "AccLoanRevSub"; // 循环贷款子台帐
	public final static String ACCACCP = "AccAccp"; // 银承台帐
	public final static String ACCDISC = "AccDisc"; // 贴现台帐
	public final static String ACCCVRG = "AccCvrg"; // 保函台帐
	public final static String ACCCUSTOM = "AccCustom"; // 客户台帐汇总(日数据)
	public final static String ACCCUSTOMBIZ = "AccCustomBiz"; // 客户台帐汇总(随业务)

	public final static String LOANACCOUNT = "LoanAccount";// 台帐共用

	// ----------------------------------------
	/**
	 * 资产保全
	 */
	public final static String ARPACCUSELOAN = "ArpAccuseLoan";
	public final static String ARPACCUSECOMPONENT = "ArpAccuseComponent";
	public final static String ARPACCUSEAGENT = "ArpAccuseAgent";
	/**
	 * 提醒相关
	 */

	public final static String REMINDAGENT = "RemindProcess";
	public final static String REMINDCOMPONENT = "RemindProcess";
	public final static String REMINDINTERFACE = "RemindProcess";
	public final static String REMINDSERVICE = "RemindService";
	public final static String REMIDPROCESSDAO = "RemindPrcessDao";
	public final static String REMINDSERVERDAO = "RemindServiceDao";
	/* 当前营业日期 */

	public final static String OPENDAY = "OPENDAY";

	/* 上一营业日期 */

	public final static String LAST_OPENDAY = "LAST_OPENDAY";

	public static final String CUSBLKLIST = "CusBlkList"; // 不宜贷款户
	/**
	 * 信贷档案管理
	 */
	// 信贷档案借阅详情
	public static final String DOCBORROWDETAIL = "DocBorrowDetail";

	public final static String PVPREDISCINAPP = "PvpRediscinApp"; // 转贴现转入
	public final static String PVPREDISCOUTAPP = "PvpRediscoutApp"; // 转贴现转出
	public final static String PVPAPP = "PvpApp"; // 通用出账
	
	public final static String DOCTEMPLATE = "DocTemplate";//文档模板组件
	
	// add by small 转贴现转入协议
	public static String CTRREDISCINCONT = "CtrRediscinCont";
	// add by small 转贴现转出协议
	public static String CTRREDISCOUTCONT = "CtrRediscoutCont";

	public final static String ACCREDISCOUT = "AccRediscout"; // 转贴现卖出/再贴现台帐

	// 转贴现转入申请信息
	public static String IQPREDISCIN = "IqpRediscin";

	public final static String ACCBILLDETAILS = "AccBillDetails"; // 贷款交易明细

	public static final String CUSBANKCUSREL = "CusBankCusRel"; // 银行关联客户

	/**
	 * 合同建账
	 */
	public static final String CtrCrtAccPub = "CtrCrtAccPub";
	/*
	 * 按一级行业类型分组
	 */
	public static final String[] businessType = { "EHK", "C" };

	/**
	 * 银承贴现协议恢复额度和最高额担保合同 *
	 */
	public static final String CtrRestore = "CtrRestore";

	/**
	 * 与核心交互接口
	 */
	public static final String DOTRADEINTERFACE = "doTrade";

	/**
	 * ------------------------以下是国际业务的常量定义-------------------------
	 */

	public static final String CustomIface = "CustomIface";
	/**
	 * 放款中心业务登记类型
	 */
	public static final String LmcFilingOther1 = "10"; // 次日移交资料业务
	public static final String LmcFilingOther2 = "20"; // 增殖税发票业务
	public static final String LmcFilingOther3 = "30"; // 保险单移交业务
	public static final String LmcFilingOther4 = "40"; // 绿色通道业务

	/**
	 * 放款中心
	 */
	public final static String LmcFilingPvp = "LmcFilingPvp";
	public final static String LmcFilingCtr = "LmcFilingCtr";
	public final static String LmcUndonePvp = "LmcUndonePvp";
	public final static String LmcFilingOther = "LmcFilingOther";
	public final static String LmcInterFace = "LmcInterFace";

	public static Map<String, String> YZCnNoMap = new HashMap<String, String>();

	public static final String IQPTFLOC = "IqpTfLoc";
	public static final String IQPTFLOCEXT = "IqpTfLocExt";
	public static final String IQPTFREMIT = "IqpTfRemit";
	public static final String IQPTFIBIC = "IqpTfIbic";
	public static final String IQPTFOIFF = "IqpTfOiff";
	public static final String PVPTFOIFF = "PvpTfOiff";
	public static final String PVPTFOINSF = "PvpTfOinsf";
	public static final String IQPTFOINSF = "IqpTfOinsf";
	public static final String IQPTFPCK = "IqpTfPck";
	public static final String IQPTFIBC = "IqpTfIbc";
	public static final String IQPTFPGAS = "IqpTfPgas";

	public static final String LmtContLoan = "LmtContLoan";
	public static final String IQPTFOINVF = "IqpTfOinvf";
	public static final String CTRTFOINVF = "CtrTfOinvf";
	public static final String PVPTFLOC = "PvpTfLoc";
	public static final String PVPTFOBOC = "PVPTFOboc";
	public static final String PVPTFLOCEXT = "PvpTfLocExt";
	public static final String PVPTFIBIC = "PvpTfIbic";
	public static final String PVPTFOINVF = "PvpTfOinvf";
	public static final String PVPTFPGAS = "PvpTfPgas";
	public static final String PVPTFREMIT = "PvpTfRemit";
	public static final String PVPTFPCK = "PvpTfPck";
	public static final String PVPTFIBC = "PvpTfIbc";
	public static final String ACCTFCOMM = "AccTfComm";
	public static final String ACCTFOINVF = "AccTfOinvf";
	public static final String ACCTFLOC = "AccTfLoc";
	public static final String ACCTFIBIC = "AccTfIbic";
	public static final String ACCTFIBC = "AccTfIbc";
	public static final String ACCTFPCK = "AccTfPck";
	public static final String ACCTFPGAS = "AccTfPgas";
	public static final String ACCTFOBOC = "AccTfOboc";
	public static final String ACCTFOIFF = "AccTfOiff";
	public static final String ACCTFOINSF = "AccTfOinsf";

	// 国际业务台账
	public static final String ACCTFREMIT = "AccTfRemit";// 汇出汇款融资
	// 国际业务的台帐接口
	public static final String ACCTFIMPL = "ACCTFIMPL";

	// add by zhoujf 国际业务合同
	public static String CTRTFCONT = "CtrTfCont";
	// add by yaomh 贷款合同向其他模块取数接口
	public static String CTRCONTADDRECIMPL = "AddContRecordImpl";

	/**
	 * 
	 * 贸易融资进口开证业务
	 */
	public static String CTRTFLOC = "CtrTfLoc";
	// 进口代收项下押汇
	public static String CTRTFIBIC = "CtrTfIbic";

	/**
	 * 贸易融资打包贷款业务
	 */
	public static String CTRTFPCK = "CtrTfPck";

	/**
	 * 贸易融资进口信用证项下的押汇
	 */
	public static String CTRTFIBC = "CtrTfIbc";

	/**
	 * 贸易融资提供担保
	 */
	public static String CTRTFPGAS = "CtrTfPgas";

	public static String CTRTFREMIT = "CtrTfRemit";
	/**
	 * 出口保理项下融资业务
	 */
	public static String CTRTFOIFF = "CtrTfOiff";
	public static String CTRTFOINSF = "CtrTfOinsf";
	/**
	 * 进口开证修改
	 */
	public static String CTRTFLOCEXT = "CtrTfLocExt";

	/**
	 * 出账申请与合同及接口实现类
	 */
	public final static String PVPTFIMPL = "PvpTfImpl";
	public final static String CTRTFIMPL = "CtrTfLocImpl";

	/**
	 * 汇率
	 */
	public static final String EXCHANGERATE = "ExchangeRate";
	
	
	public static final String ADD = "add";  		//操作-新增  (列表维护操作有使用)
	public static final String NONE = "none";  		//操作-无操作 (列表维护操作有使用)
	public static final String DELETE = "del"; 	 	//操作-删除 (列表维护操作有使用)
	public static final String MODIFY = "update";  	//操作-修改

	/**
	 * SqlOperator对像反回结果的名称
	 */
	public static final String RESULT_SET = "resultSet";
	
	/**
	 * 分页查询默认每页行数
	 */
	public static final int MAXLINE = 15;
	
	//wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf
	//wf华丽的分隔符
	/**
	 * 审查审批结论
	 */
	public static final String WFI_RESUTL_AGREE = "10";			//同意
	public static final String WFI_RESUTL_DISAGREE = "20";		//否决
	public static final String WFI_RESUTL_CALLBACK = "30";		//打回
	public static final String WFI_RESUTL_RETURNBACK = "40";	//退回
	public static final String WFI_RESUTL_JUMP = "50";			//跳转
	public static final String WFI_RESUTL_AGAIN = "60";			//拿回
	public static final String WFI_RESUTL_CANCEL = "70";		//取消
	public static final String WFI_RESUTL_APP = "00";		//审批中
	/**
	 * 流程审批状态
	 */
	public static final String WFI_STATE_INIT = "000" ;		//待发起
	public static final String WFI_STATE_APPROVE = "111" ;	//审批中
	public static final String WFI_STATE_CANCEL = "990" ;	//取消
	public static final String WFI_STATE_RECOVER = "991" ;	//拿回
	public static final String WFI_STATE_REGAIN = "992" ;	//打回
	public static final String WFI_STATE_PASS = "997" ;		//通过
	public static final String WFI_STATE_DENIAL = "998" ;	//否决(不同意)
	
	/**
	 * 流程应用扩展状态
	 */
	public static final String WFI_APPSIGN_ING = "0";		//审批中
	public static final String WFI_APPSIGN_END = "1";		//终审
	public static final String WFI_APPSIGN_CANCEL = "2";	//取消
	public static final String WFI_APPSIGN_NO = "3";		//拒绝
	
	/**
	 * 流程扩展属性名称
	 */
	public static final String WFI_EXT_SCENE = "scene";			//场景
	public static final String WFI_EXT_SHOWTYPE = "showtype";	//展示风格
	public static final String WFI_EXT_APPLTYPE = "appltype";		//所属分类
	public static final String WFI_EXT_OPINTAB = "opintab";		//审批记录表
	public static final String WFI_EXT_APPLURL = "applurl";            //申请明细URL
	public static final String WFI_EXT_BIZ_PAGE = "includeBizPage";            //业务数据区页面URL
	public static final String WFI_EXT_OPINGUIDE_PAGE = "opinGuidePage";//审批意见书写向导URL
	
	public static final String WFI_EXT_DATA_HIS_PAGE = "bizDataHisPage";            //业务数据区页面URL
	public static final String WFI_EXT_ADVICE_HIS_PAGE = "adviceHisPage";//审批意见书写向导URL
	
	public static final String WFI_EXT_IS_PROCESS = "isHandleBizLogic";//是否处理业务逻辑
	
	public static final String WFI_EXT_APPLY_POLICY ="applyPolicy";
		
	
	public static final String WFI_APPLY_POLICY_YPFJ ="1";//会签策略：一票否决
	
	public static final String WFI_APPLY_POLICY_BSTG ="2";//会签策略：半数通过
	
	public static final String WFI_APPLY_POLICY_SFZETG ="3";//会签策略：三分之二通过
	
	public static final String WFI_APPLY_POLICY_RESULT_TG ="10";//会签结论：通过
	
	public static final String WFI_APPLY_POLICY_RESULT_FJ ="20";//会签结论：否决
	
	
	/** 消息处理状态  初始尚未处理*/
	public static final String WFI_MSG_OPSTATUS_INIT = "00";
	/** 消息处理状态  处理中*/
	public static final String WFI_MSG_OPSTATUS_DOING = "01";
	/** 消息处理状态  异常*/
	public static final String WFI_MSG_OPSTATUS_ERROR = "90";
	/** 消息处理状态  处理完毕*/
	public static final String WFI_MSG_OPSTATUS_END = "99";
	
    /** 风险拦截检查结果  ———— 通过*/
	public static final String WFI_RISKINSPECT_RESULT_PASS = "1";

    /** 风险拦截检查结果  ———— 不通过*/
	public static final String WFI_RISKINSPECT_RESULT_DENY = "2";
	
    /** 风险拦截检查结果  ———— 忽略*/
	public static final String WFI_RISKINSPECT_RESULT_CANCEL = "3";	
	//wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf*=-=*wf

}
