package com.yucheng.cmis.pub;

public class FNCPubConstant {

	/**
	 * 
	 * 
	 * 
	 */
	public static final String FNC_MODEL = "IndModel";

	/**
	 * 资产负债表
	 */
	public static String BS = "01";
	public static String BS_TBALE_NAME = "FNC_STAT_BS";
	/**
	 * 利润表（损益表）
	 */
	public static String IS = "02";
	public static String IS_TABLE_NAME = "FNC_STAT_IS";
	/**
	 * 现金流量表
	 */
	public static String CFS = "03";
	public static String CFS_TABLE_NAME = "FNC_STAT_CFS";
	/**
	 * 财务指标表
	 */
	public static String IND = "04";
	public static String IND_TABLE_NAME = "FNC_INDEX_RPT";
	/**
	 * 所有者权益表
	 */
	public static String SOE = "05";
	public static String SOE_TABLE_NAME = "FNC_STAT_SOE";
	/**
	 * 财务简表表
	 */
	public static String SL = "06";
	public static String SL_TABLE_NAME = "FNC_STAT_SL";
	
	/**
	 * 报表科目查询类型
	 */
	public static String QUERYTYPEALL="ALL";
	public static String QUERYTYPEONE="ONE";
	
	public static String FNCTYPEMONTH="1";
	public static String FNCTYPEMONTQ="2";
	public static String FNCTYPEMONTHY="3";
	public static String FNCTYPEMONTY="4";
	
	public static final String FNC4RSCINTF="Fnc4RscInstance";//财报为风险分类提供的接口实现类
	public static final String FNC4QC="Fnc4QueryComponent";// 财报查询
	public static final String FNC4QA="Fnc4QueryAgent";    // 财报查询
	public static final String FNCSTATBASE="FncStatBase";    // 财报查询
	public static final String FNC4FNAINTF="Fnc4FNAInstance";//财报为风险分类提供的接口实现类

	
	
	/*
	 *报表字段 
	 */
	public static final String STAT_INIT_AMT="STAT_INIT_AMT";
	public static final String STAT_END_AMT="STAT_END_AMT";
	public static final String STAT_INIT_AMT_Q="STAT_INIT_AMT_Q";
	public static final String STAT_END_AMT_Q="STAT_END_AMT_Q";
	public static final String STAT_INIT_AMT_Y="STAT_INIT_AMT_Y";
	public static final String STAT_END_AMT_Y="STAT_END_AMT_Y";
	/*
	 * 利润表
	 */

	/**
	 * 利润表-营业/销售收入ID属性值
	 */
	public static String OPERINCID = "ZS4050000";

	/**
	 * 利润表-营业成本ID属性值
	 */
	public static String OPERCOSID = "opercos";

	/**
	 * 利润表-销售费用ID属性值
	 */
	public static String COSOFSALID = "cosofsal";

	/**
	 * 利润表-管理费用ID属性值
	 */
	public static String MAGEFEEID = "magefee";

	/**
	 * 利润表-财务费用ID属性值
	 */
	public static String FINCOSID = "fincos";

	/**
	 * 利润表-营业利润ID属性值
	 */
	public static String OPERPRFID = "operprf";

	/**
	 * 利润表-利润总额ID属性值
	 */
	public static String TOLPRFID = "tolprf";

	/**
	 * 利润表-净利润ID属性值
	 */
	public static String INETPRFID = "L06010000";

	// ********************新增用于计算财务指标*******************

	/**
	 * 利润表-所得税ID属性值
	 */
	public static String INCTAXID = "inctax";

	/**
	 * 利润表-应付利息ID属性值
	 */
	public static String INTPYID = "intpy";

	/*
	 * 资产负债表
	 */

	/**
	 * 资产负债表-资产ID属性值
	 */
	public static String ASSETID = "asset";

	/**
	 * 资产负债表-货币资金ID属性值
	 */
	public static String CURRENCYID = "currency";

	/**
	 * 资产负债表-交易性金融资产ID属性值
	 */
	public static String TRNFNNASSID = "trnfnnass";

	/**
	 * 资产负债表-应收账款ID属性值
	 */
	public static String ACCRCVID = "accrcv";

	/**
	 * 资产负债表-其他应收款ID属性值
	 */
	public static String OTHRCVID = "othrcv";

	/**
	 * 资产负债表-存货ID属性值
	 */
	public static String STOCKID = "stock";

	/**
	 * 资产负债表-流动资产合计ID属性值
	 */
	public static String TOLCURASSID = "tolcurass";

	/**
	 * 资产负债表-长期股权投资ID属性值
	 */
	public static String LNGEQNID = "lngeqn";

	/**
	 * 资产负债表-固定资产ID属性值
	 */
	public static String CAPASSID = "capass";

	/**
	 * 资产负债表-无形资产ID属性值
	 */
	public static String INTASSID = "intass";

	/**
	 * 资产负债表-其他资产合计ID属性值
	 */
	public static String TOLOTHASSID = "tolothass";

	/**
	 * 资产负债表-负债ID属性值
	 */
	public static String LBLTID = "lblt";

	/**
	 * 资产负债表-短期借款ID属性值
	 */
	public static String SHRBRWID = "shrbrw";

	/**
	 * 资产负债表-应付账款ID属性值
	 */
	public static String ACCPAYID = "accpay";

	/**
	 * 资产负债表-其他应付款ID属性值
	 */
	public static String OTHPAYID = "othpay";

	/**
	 * 资产负债表-一年内到期的非流动负债ID属性值
	 */
	public static String OYNCLID = "oyncl";

	/**
	 * 资产负债表-流动负债合计ID属性值
	 */
	public static String TOLCURLBID = "tolcurlb";

	/**
	 * 资产负债表-长期借款ID属性值
	 */
	public static String LONGTBORWID = "longtborw";

	/**
	 * 资产负债表-非流动负债合计ID属性值
	 */
	public static String TLNNCURLBID = "tlnncurlb";

	// ********************新增用于计算财务指标*******************
	/**
	 * 资产负债表-所有者权益合计ID属性值
	 */
	public static String WISTID = "wist"; // Ownership interest sum total

	/**
	 * 资产负债表-资产总计ID属性值
	 */
	public static String PGTID = "pgt"; // Property grand total

	/**
	 * 资产负债表-负债合计ID属性值
	 */
	public static String DSTID = "dst"; // Debt sum total

	/**
	 * 资产负债表-流动资产合计ID属性值
	 */
	public static String CASTID = "cast"; // Current assets sum total

	/**
	 * 资产负债表-其他流动资产ID属性值 Other current assets
	 */
	public static String OTHCURASSID = "othcurass";

	/**
	 * 资产负债表-其他非流动资产ID属性值 Other non-current assets
	 */
	public static String OTHNONCURASSID = "othnoncurass";

	/**
	 * 商誉ID属性值 Goodwill
	 */
	public static String GOODWILLID = "goodwill";

	/**
	 * 开发支出ID属性值 Development expenditures
	 */
	public static String DVLPEXPID = "dvlpexp";

	/**
	 * 递延所得税资产ID属性值 Deferred tax assets
	 */
	public static String DEFTAXASSID = "deftaxass";

	/**
	 * 长期待摊费用ID属性值 Long-term deferred expenses
	 */
	public static String LTMDEFEXPID = "ltmdefexp";

	/*
	 * 现金流量表
	 */

	/**
	 * 现金流量表-现金流量净额ID属性值
	 */
	public static String CSFLWID = "X01000001";

	/**
	 * 现金流量表-经营活动产生的现金流量净额ID属性值
	 */

	public static String BUSCTGNTCSFLWID = "X01000000";

	/**
	 * 现金流量表-投资活动产生的现金流量净额ID属性值
	 */
	public static String INVCTGNTCSFLWID = "X02000000";

	/**
	 * 现金流量表-筹资活动产生的现金流量净额ID属性值
	 */
	public static String FNDCTGNTCSFLWID = "X03000000";

	// ********************新增用于计算财务指标*******************
	/**
	 * 现金流量表-固定资产折旧、油气资产折耗、生产性生物资产折旧ID属性值
	 */
	public static String DPRCID = "dprc";// Depreciation

	/**
	 * 现金流量表-无形资产摊销ID属性值
	 */
	public static String IAAMRTZID = "iaamrtz";// Intangible asset amortization

	/**
	 * 现金流量表-长期待摊费用摊销ID属性值
	 */
	public static String LABEAMRTZID = "labemrtz";// Long anticipation booth

	// expense amortization

	/*
	 * 财务指标项目表
	 */

	/**
	 * 财务指标项目表-1.盈利指标ID属性值
	 */
	public static String PRFTRGID = "prftrg";

	/**
	 * 财务指标项目表（盈利指标）-毛利率ID属性值
	 */
	public static String GRSSMRGID = "grssmrg";

	/**
	 * 财务指标项目表（盈利指标）-营业利润率ID属性值
	 */
	public static String OPRPRFMRGID = "C01020000";

	/**
	 * 财务指标项目表（盈利指标）-净利润率ID属性值
	 */
	public static String NTPRFMRGID = "ntprfmrg";

	/**
	 * 成本费用利润率
	 */

	public static String COSTPMID = "C01050000";

	/**
	 * 财务指标项目表（盈利指标）-净资产收益率ID属性值
	 */
	public static String RTNQTYID = "C01070000";

	/**
	 * 财务指标项目表（盈利指标）-息税前收益ID属性值
	 */
	public static String EBTID = "ebt";

	/**
	 * 财务指标项目表（盈利指标）-总资产报酬率ID属性值
	 */
	public static String RTNTLSSID = "rtntlss";

	/**
	 * 财务指标项目表（盈利指标）-息税折摊前收益ID属性值
	 */
	public static String TNQNCTXBRKID = "tnqnctxbrk";

	/*
	 * 2. 偿债能力指标
	 */

	/**
	 * 财务指标项目表（偿债能力指标）-2.偿债能力指标ID属性值
	 */
	public static String SLVNDID = "slvnd";

	/**
	 * 财务指标项目表（偿债能力指标）-已获利息倍数ID属性值
	 */
	public static String MLTFNTID = "mltfnt";

	/**
	 * 财务指标项目表（偿债能力指标）-偿付比率ID属性值
	 */
	public static String CVRGID = "cvrg";

	/**
	 * 财务指标项目表（偿债能力指标）-偿付比率1ID属性值
	 */
	public static String CVRG1ID = "cvrg1";

	/**
	 * 财务指标项目表（偿债能力指标）-偿付比率2ID属性值
	 */
	public static String CVRG2ID = "cvrg2";

	/**
	 * 财务指标项目表（偿债能力指标）-现金流动负债比率ID属性值
	 */
	public static String CSFLTHNBLID = "C04050000";

	/**
	 * 财务指标项目表（偿债能力指标）-现金总负债比ID属性值
	 */
	public static String TLLBTHNSHID = "tllbthncsh";

	/*
	 * 3.资本结构和资产周转指标
	 */
	/**
	 * 财务指标项目表（资本结构和资产周转指标）-3.资本结构和资产周转指标ID属性值
	 */
	public static String CSTTID = "cstt";

	/**
	 * 财务指标项目表（资本结构和资产周转指标）-资产负债率ID属性值
	 */
	public static String RTFSSNDLBID = "C03010000";

	/**
	 * 财务指标项目表（资本结构和资产周转指标）-负债权益比ID属性值
	 */
	public static String DBTQRTID = "dbtqrt";

	/**
	 * 财务指标项目表（资本结构和资产周转指标）-总资产周转率ID属性值
	 */
	public static String TLSSTVRTID = "tlsstvrt";

	/*
	 * 4.流动性指标
	 */
	/**
	 * 财务指标项目表（流动性指标）-4.流动性指ID属性值
	 */
	public static String LQDNDID = "lqdnd";

	/**
	 * 财务指标项目表（流动性指标）-流动比率ID属性值
	 */
	public static String CRRTID = "crrt";

	/**
	 * 财务指标项目表（流动性指标）-速动比率ID属性值
	 */
	public static String SDNGBLVID = "C04020000";

	/**
	 * 财务指标项目表（流动性指标）-现金比率ID属性值
	 */
	public static String CSHRTID = "cshrt";

	/**
	 *财务指标项目表-应收帐款周转率
	 */
	public static String RECTO = "C02030000";

	/**
	 *财务指标项目表-或有负债率
	 */
	public static String ORDEBTO = "C05130000";

	/**
	 *财务指标项目表- 资本保值增值率
	 */
	public static String OPAR = "C05110000";

	/**
	 *财务指标项目表-净现金流
	 */
	public static String OCASHF = "X01100000";

	/*
	 * 5.经营活动周转天数
	 */

	/**
	 * 财务指标项目表（经营活动周转天数）-5.经营活动周转天数ID属性值
	 */
	public static String BZTDID = "bztd";

	/**
	 * 财务指标项目表（经营活动周转天数）-存货周转天数ID属性值
	 */
	public static String INVTVDYSID = "invtvdys";

	/**
	 * 财务指标项目表（经营活动周转天数）-应收账款周转天数ID属性值
	 */
	public static String ACCRCTVDYSID = "accrctvdys";

	/**
	 * 财务指标项目表（经营活动周转天数）-应付账款周转天数ID属性值
	 */
	public static String ACCPYTVDYSID = "accpytvdys";

	/*
	 * 6.增长性指标
	 */

	/**
	 * 财务指标项目表（增长性指标）-6.增长性指标ID属性值
	 */
	public static String GRWNDID = "grwnd";

	/**
	 * 财务指标项目表（增长性指标）-营业收入增长率ID属性值
	 */
	public static String OPRNCGRRTID = "C05050000";

	/**
	 * 财务指标项目表（增长性指标）-资本累积率ID属性值
	 */
	public static String RTFCPCCID = "rtfcpcc";
	
	
	/**
	 * 财务指标项目表(主营业务利润率id)
	 */
	public static String ZYYWLRLID = "C01020000";
	
	/**
	 * 财务指标项目表(资本收益率id)
	 */
	public static String ZBSYLID = "C09000201";

	/**
	 * 02主营业务利润
	 */
	public static String ZYYWLRID = "L03010000";
	
	/**
	 * 02[L05010000]利润总额
	 */
	public static String LRZEID = "L05010000";

	public static String YYXJBZBSID = "C09000000";

	/**
	 * 02[L06010000]净利润
	 */
	public static String JLRID = "L06010000";

	/**
	 * 01[Z01020600]平均资本
	 */
	public static String PJZBID = "Z01020600"; 

	/**
	 * 04[C02010000]总资产周转率
	 */
	public static String ZZCZZLID = "C02010000";

	
	/**
	 * 04[C02030001]应收账款周转天数
	 */
	public static String YSZKZZTSID = "C02030001";
	
	/**
	 * 04[C02030000]应收账款周转率
	 */
	public static String YSZKZZLID = "C02030000";


	/**
	 * 04[C02040001]存货周转天数
	 */
	public static String CHZZTSID = "C02040001";
	
	/**
	 * 04[C02040000]存货周转率
	 */
	public static String CHZZLID = "C02040000";

	/**
	 * 04[C02070000]流动资产周转率
	 */
	public static String LDZCZZLID = "C02070000";

	/**
	 * 04[C09000001]资产现金回收率
	 */
	public static String ZCXJHSLID = "C09000001";
	
	/**
	 * 03[X01100000]经营现金净流量
	 */
	public static String JYXJJLLID = "X01100000";
	
	/**
	 * 01[Z01020700]平均资产总额
	 */
	public static String PJZCZEID = "Z01020700";

	/**
	 * 资产负债率 事业单位
	 */
	public static String CAREER_ZCFZL_ID = "CS1010000";
	
	/**
	 * 自主收入占比 事业单位
	 */
	public static String CAREER_ZZSRZB_ID = "CS1020000";
	/**
	 * 或有负债率 事业单位
	 */
	public static String CAREER_HYFZL_ID = "CS1030000";
	/**
	 * 收支节余率 事业单位
	 */
	public static String CAREER_SZJYL_ID = "CS2010000";
	/**
	 * 经费自给率 事业单位
	 */
	public static String CAREER_JFZJL_ID = "CS3010000";
	/**
	 * 总收入增长率 事业单位
	 */
	public static String CAREER_ZSRZZL_ID = "CS3020000";
	/**
	 * 净资产增长率 事业单位
	 */
	public static String CAREER_JZCZZL_ID = "CS3030000";
	/**
	 * 已获利息倍数
	 */

	/**
	 * 04[C04050000]现金流动负债比
	 */
	public static String XJLDFZBID = "C04050000";
	
	/**
	 * 04[C05120001]带息负债比率
	 */
	public static String DXFZBLID = "C05120001";
	
	/**
	 * 01[Z02010100]短期借款
	 */
	public static String DQJKID = "Z02010100";
	
	/**
	 * 01[Z01020800]一年内到期的非流动负债
	 */
	public static String YNNDQDFLDFZID = "Z01020800";
	
	/**
	 * 01[Z02020100]长期借款
	 */
	public static String CQJKID = "Z02020100";
	
	/**
	 * 01[Z02020200]应付债券
	 */
	public static String YFZQID = "Z02020200";
	
	/**
	 * 01[]应付利息
	 */

	/**
	 * 01[Z02000001]总额负债
	 */
	public static String ZEFZID = "Z02000001";

	/**
	 * 04或有负债比
	 */
	public static String HYFZBID = "C05120000";


	/**
	 * 04[C09000002]流动资产比率
	 */
	public static String LDZCBLID = "C09000002";
	
	/**
	 * 01[Z01010000]流动资产
	 */
	public static String LDZCID = "Z01010000";
	
	/**
	 * 01[Z01000001]资产
	 */
	public static String ZCHJID = "Z01000001";

	/**
	 * 04[C09000003]非流动资产比率
	 */
	public static String FLDZCBLID = "C09000003";
	
	/**
	 * 01[Z01030110]固定资产净值
	 */
	public static String GDZCJZID = "Z01030110";
	
	/**
	 * 01[Z01020000]长期投资
	 */
	public static String CQTZID = "Z01020000";
	
	/**
	 * 01[Z01020900]无形资产及其他
	 */
	public static String WXZCJQTID = "Z01020900";
	
	/**
	 * 04[Z02010002]流动负债比率
	 */
	public static String LDFZBLID = "Z02010002";
	
	/**
	 * 01[Z02010000]流动负债
	 */
	public static String LDFZID = "Z02010000";
	
	/**
	 * 01[Z02000001]负债
	 */
	public static String FZHEJID = "Z02000001";
	
	/**
	 * 04[Z04020001]资本公积比率
	 */
	public static String ZBGJBLID = "Z04020001";
	
	/**
	 * 01[Z04020000]资本公积
	 */
	public static String ZBGJID = "Z04020000";
	
	/**
	 * 01[Z04000001]所有者权益
	 */
	public static String SYZQYID = "Z04000001";
	
	/**
	 * 04[C09000004]留存收益比率
	 */
	public static String LCSYBLID = "C09000004";
	
	/**
	 * 01[Z04030000]盈余公积
	 */
	public static String YYGJID = "Z04030000";
	
	/**
	 * 02[L09010000]未分配利润
	 */
	public static String WFPLRID = "L09010000";
	
	/**
	 * 04[C01110000]投资收益比率
	 */
	public static String TZSYBLID = "C01110000";
	
	/**
	 * 02[L04020000]投资收益
	 */
	public static String TZSYID = "L04020000";
	
	/**
	 * 04[C09000005]营业外净收入比率
	 */
	public static String YYWJSRBLID = "C09000005";
	
	/**
	 * 02[L04050000]营业外收入
	 */
	public static String YYWSRID = "L04050000";
	
	/**
	 * 02[L04070000]营业外支出
	 */
	public static String YYWZCID = "L04070000";
	
	/**
	 * 04[C09000006]经营性现金流入比率
	 */
	public static String JYXXJLRBLID = "C09000006";
	
	/**
	 * 03[X01040000]经营性现金流入
	 */
	public static String JYXXJLRID = "X01040000";
	
	/**
	 * 03[X01040100]现金流入总计
	 */
	public static String XXLRZJID = "X01040100";

	/**
	 * 04[C09000007]筹资性现金流入比率
	 */
	public static String CZXXJLRBLID = "C09000007";
	
	/**
	 * 03[X03040000]筹资性现金流入
	 */
	public static String CZXXJLRID = "X03040000";
	
	/**
	 * 03[X01040100]现金流入总计
	 */
	public static String XJLRZJID = "X01040100";

	/**
	 * 04[C09000008]经营性现金流出比率
	 */
	public static String JYXXJLCBLID = "C09000008";
	
	/**
	 * 03[X01090000]经营性现金流出
	 */
	public static String JYXXJLCID = "X01090000";
	
	/**
	 * 03[X01040200]现金流出总计
	 */
	public static String XJLCZJID = "X01040200";

	/**
	 * 04[C09000009]筹资性现金流出比率
	 */
	public static String CZXXJLCBLID = "C09000009";
	
	/**
	 * 03[X03080000]筹资性现金流出
	 */
	public static String CZXXJLCID = "X03080000";
	
	/**
	 * 04[C09000010]经营性现金净额结构比
	 */
	public static String JYXXJJEJGBID = "C09000010";
	
	/**
	 * 03[X01100000]经营性现金净流量
	 */
	public static String JYXXJJLLID = "X01100000";
	
	/**
	 * 03[X05000000]现金及现金等价物净增加额
	 */
	public static String XJJXJDJWJZJEID = "X05000000";
	
	/**
	 * 04[C09000011]筹资性现金净额结构比
	 */
	public static String CZXXJJEJGBID = "C09000011";
	
	/**
	 * 03[X03090000]筹资性现金净流量
	 */
	public static String CZXXJJLLID = "X03090000";
	

	/*
	 * 对借款人财务状况的评价
	 */

	/**
	 * 对借款人财务状况的评价-对借款人财务状况的评价ID属性值
	 */
	public static String FNCDECID = "repdec";

	/**
	 * 对借款人财务状况的评价-财务评价ID属性值
	 */
	public static String FNVLID = "fnvl";

	/*
	 * 利润表
	 */

	/**
	 * 利润表-利润表TEXT属性值
	 */
	public static String STTISTEXT = "利润表";

	/**
	 * 利润表-营业收入TEXT属性值
	 */
	public static String OPERINCTEXT = "营业收入";

	/**
	 * 利润表-营业成本TEXT属性值
	 */
	public static String OPERCOSTEXT = "营业成本";

	/**
	 * 利润表-销售费用TEXT属性值
	 */
	public static String COSOFSALTEXT = "销售费用";

	/**
	 * 利润表-管理费用TEXT属性值
	 */
	public static String MAGEFEETEXT = "管理费用";

	/**
	 * 利润表-财务费用TEXT属性值
	 */
	public static String FINCOSTEXT = "财务费用";

	/**
	 * 利润表-营业利润TEXT属性值
	 */
	public static String OPERPRFTEXT = "营业利润";

	/**
	 * 利润表-利润总额TEXT属性值
	 */
	public static String TOLPRFTEXT = "利润总额";

	/**
	 * 利润表-净利润TEXT属性值
	 */
	public static String INETPRFTEXT = "净利润";

	// ********************新增用于计算财务指标*******************

	/**
	 * 利润表-所得税TEXT属性值
	 */
	public static String INCTAXTEXT = "所得税";

	/**
	 * 利润表-应付利息TEXT属性值
	 */
	public static String INTPYTEXT = "应付利息";

	/*
	 * 资产负债表
	 */

	/**
	 * 资产负债表-资产负债表TEXT属性值
	 */
	public static String BSTEXT = "资产负债表";

	/**
	 * 资产负债表-资产TEXT属性值
	 */
	public static String ASSETTEXT = "资产";

	/**
	 * 资产负债表-货币资金TEXT属性值
	 */
	public static String CURRENCYTEXT = "货币资金";

	/**
	 * 资产负债表-交易性金融资产TEXT属性值
	 */
	public static String TRNFNNASSTEXT = "交易性金融资产";

	/**
	 * 资产负债表-应收账款TEXT属性值
	 */
	public static String ACCRCVTEXT = "应收账款";

	/**
	 * 资产负债表-存货TEXT属性值
	 */
	public static String STOCKTEXT = "存货";

	/**
	 * 资产负债表-流动资产合计TEXT属性值
	 */
	public static String TOLCURASSTEXT = "流动资产合计";

	/**
	 * 资产负债表-长期股权投资TEXT属性值
	 */
	public static String LNGEQNTEXT = "长期股权投资";

	/**
	 * 资产负债表-固定资产TEXT属性值
	 */
	public static String CAPASSTEXT = "固定资产";

	/**
	 * 资产负债表-无形资产TEXT属性值
	 */
	public static String INTASSTEXT = "无形资产";

	/**
	 * 资产负债表-其他资产合计TEXT属性值
	 */
	public static String TOLOTHASSTEXT = "其他资产合计";

	/**
	 * 资产负债表-负债TEXT属性值
	 */
	public static String LBLTTEXT = "负债";

	/**
	 * 资产负债表-短期借款TEXT属性值
	 */
	public static String SHRBRWTEXT = "短期借款";

	/**
	 * 资产负债表-应付账款TEXT属性值
	 */
	public static String ACCPAYTEXT = "应付账款";

	/**
	 * 资产负债表-其他应付款TEXT属性值
	 */
	public static String OTHPAYTEXT = "其他应付款";

	/**
	 * 资产负债表-一年内到期的非流动负债TEXT属性值
	 */
	public static String OYNCLTEXT = "一年内到期的非流动负债";

	/**
	 * 资产负债表-流动负债合计TEXT属性值
	 */
	public static String TOLCURLBTEXT = "流动负债合计";

	/**
	 * 资产负债表-长期借款TEXT属性值
	 */
	public static String LONGTBORWTEXT = "长期借款";

	/**
	 * 资产负债表-非流动负债合计TEXT属性值
	 */
	public static String TLNNCURLBTEXT = "非流动负债合计";

	/**
	 * 资产负债表-其他应收款TEXT属性值
	 */
	public static String OTHRCVTEXT = "其他应收款";

	// ********************新增用于计算财务指标*******************
	/**
	 * 资产负债表-所有者权益合计TEXT属性值
	 */
	public static String WISTTEXT = "所有者权益合计"; // Ownership interest sum total

	/**
	 * 资产负债表-资产总计TEXT属性值
	 */
	public static String PGTTEXT = "资产总计"; // Property grand total

	/**
	 * 资产负债表-负债合计TEXT属性值
	 */
	public static String DSTTEXT = "负债合计"; // Debt sum total

	/**
	 * 资产负债表-流动资产合计TEXT属性值
	 */
	public static String CASTTEXT = "流动资产合计"; // Current assets sum total

	/**
	 * 资产负债表-其他流动资产ID属性值 Other current assets
	 */
	public static String OTHCURASSTEXT = "其他流动资产";

	/**
	 * 资产负债表-其他非流动资产ID属性值 Other non-current assets
	 */
	public static String OTHNONCURASSTEXT = "其他非流动资产";

	/**
	 * 商誉ID属性值 Goodwill
	 */
	public static String GOODWILLTEXT = "商誉";

	/**
	 * 开发支出ID属性值 Development expenditures
	 */
	public static String DVLPEXPTEXT = "开发支出";

	/**
	 * 递延所得税资产ID属性值 Deferred tax assets
	 */
	public static String DEFTAXASSTEXT = "递延所得税资产";

	/**
	 * 资产负债-长期待摊费用ID属性值 Long-term deferred expenses
	 */
	public static String LTMDEFEXPTEXT = "长期待摊费用";

	/*
	 * 现金流量表
	 */

	/**
	 * 现金流量表-现金流量表TEXT属性值
	 */
	public static String CFSTEXT = "现金流量表";

	/**
	 * 现金流量表-经营活动产生的现金流量净额TEXT属性值
	 */
	public static String BUSCTGNTCSFLWIDTEXT = "经营活动产生的现金流量净额";

	/**
	 * 现金流量表-投资活动产生的现金流量净额TEXT属性值
	 */
	public static String INVCTGNTCSFLWTEXT = "投资活动产生的现金流量净额";

	/**
	 * 现金流量表-筹资活动产生的现金流量净额TEXT属性值
	 */
	public static String FNDCTGNTCSFLWTEXT = "筹资活动产生的现金流量净额";

	// ********************新增用于计算财务指标*******************
	/**
	 * 现金流量表-折旧TEXT属性值
	 */
	public static String DPRCTEXT = "固定资产折旧、油气资产折耗、生产性生物资产折旧";// Depreciation

	/**
	 * 现金流量表-无形资产摊销TEXT属性值
	 */
	public static String IAAMRTZEXT = "无形资产摊销";// Intangible asset amortization

	/**
	 * 现金流量表-长期待摊费用摊销TEXT属性值
	 */
	public static String LABEAMRTZTEXT = "长期待摊费用摊销";// Long anticipation booth

	// expense amortization

	/*
	 * 财务指标
	 */

	/**
	 * 财务指标-财务指标TEXT属性值
	 */
	public static String RPTTEXT = "财务指标";

	/**
	 * 财务指标(盈利指标)-盈利指标TEXT属性值
	 */
	public static String PRFTRGTEXT = "盈利指标";

	/**
	 * 财务指标(盈利指标)-毛利率TEXT属性值
	 */
	public static String GRSSMRGTEXT = "毛利率";

	/**
	 * 财务指标(盈利指标)-营业利润率TEXT属性值
	 */
	public static String OPRPRFMRGTEXT = "营业利润率";

	/**
	 * 财务指标(盈利指标)-净利润率TEXT属性值
	 */
	public static String NTPRFMRGTEXT = "净利润率";

	/**
	 * 财务指标(盈利指标)-净资产收益率TEXT属性值
	 */
	public static String RTNQTYTEXT = "净资产收益率";

	/**
	 * 财务指标(盈利指标)-息税前收益TEXT属性值
	 */
	public static String EBTTEXT = "息税前收益";

	/**
	 * 财务指标(盈利指标)-总资产报酬率TEXT属性值
	 */
	public static String RTNTLSSTEXT = "总资产报酬率";

	/**
	 * 财务指标(盈利指标)-息税折摊前收益TEXT属性值
	 */
	public static String TNQNCTXBRKTEXT = "息税折摊前收益";

	/*
	 * 偿债能力指标
	 */

	/**
	 * 财务指标(偿债能力指标)-偿债能力指标TEXT属性值
	 */
	public static String SLVNDTEXT = "偿债能力指标";

	/**
	 * 财务指标(偿债能力指标)-已获利息倍数TEXT属性值
	 */
	public static String MLTFNTTEXT = "已获利息倍数";

	/**
	 * 财务指标(偿债能力指标)-偿付比率TEXT属性值
	 */
	public static String CVRGTEXT = "偿付比率";

	/**
	 * 财务指标(偿债能力指标)-偿付比率1TEXT属性值
	 */
	public static String CVRG1TEXT = "偿付比率1";

	/**
	 * 财务指标(偿债能力指标)-偿付比率2TEXT属性值
	 */
	public static String CVRG2TEXT = "偿付比率2";

	/**
	 * 财务指标(偿债能力指标)-现金流动负债比TEXT属性值
	 */
	public static String CSFLTHNBLTEXT = "现金流动负债比";

	/**
	 * 财务指标(偿债能力指标)-现金总负债比TEXT属性值
	 */
	public static String TLLBTHNSHTEXT = "现金总负债比";

	/*
	 * 3.资本结构和资产周转指标
	 */

	/**
	 * 财务指标(资本结构和资产周转指标)-资本结构和资产周转指标TEXT属性值
	 */
	public static String CSTTTEXT = "资本结构和资产周转指标 ";

	/**
	 * 财务指标(资本结构和资产周转指标)-资产负债率TEXT属性值
	 */
	public static String RTFSSNDLBTEXT = "资产负债率";

	/**
	 * 财务指标(资本结构和资产周转指标)-负债权益比TEXT属性值
	 */
	public static String DBTQRTTEXT = "负债权益比";

	/**
	 * 财务指标(资本结构和资产周转指标)-总资产周转率TEXT属性值
	 */
	public static String TLSSTVRTTEXT = "总资产周转率";

	/*
	 * "4.流动性指标
	 */
	/**
	 * 财务指标(流动性指标)-流动性指标TEXT属性值
	 */
	public static String LQDNDTEXT = "流动性指标";

	/**
	 * 财务指标(流动性指标)-流动比率TEXT属性值
	 */
	public static String CRRTTEXT = "流动比率";

	/**
	 * 财务指标(流动性指标)-速动比率TEXT属性值
	 */
	public static String SDNGBLVTEXT = "速动比率";

	/**
	 * 财务指标(流动性指标)-现金比率TEXT属性值
	 */
	public static String CSHRTTEXT = "现金比率";

	/**
	 * 5.经营活动周转天数
	 */

	/**
	 * 财务指标(经营活动周转天数)-经营活动周转天数TEXT属性值
	 */
	public static String BZTDTEXT = "经营活动周转天数";

	/**
	 * 财务指标(经营活动周转天数)-存货周转天数TEXT属性值
	 */
	public static String INVTVDYSTEXT = "存货周转天数";

	/**
	 * 财务指标(经营活动周转天数)-应收账款周转天数TEXT属性值
	 */
	public static String ACCRCTVDYSTEXT = "应收账款周转天数";

	/**
	 * 财务指标(经营活动周转天数)-应付账款周转天数TEXT属性值
	 */
	public static String ACCPYTVDYSTEXT = "应付账款周转天数";

	/*
	 * 6.增长性指标
	 */
	/**
	 * 财务指标(增长性指标)-增长性指标TEXT属性值
	 */
	public static String GRWNDTEXT = "增长性指标";

	/**
	 * 财务指标(增长性指标)-营业收入增长率TEXT属性值
	 */
	public static String OPRNCGRRTTEXT = "营业收入增长率";

	/**
	 * 财务指标(增长性指标)-资本累积率TEXT属性值
	 */
	public static String RTFCPCCTEXT = "资本累积率";

	/*
	 * 对借款人财务状况的评价
	 */

	/**
	 * 对借款人财务状况的评价-对借款人财务状况的评价TEXT属性值
	 */
	public static String FNCDECTEXT = "对借款人财务状况的评价";

	/**
	 * 对借款人财务状况的评价-财务评价TEXT属性值
	 */
	public static String FNVLIDTEXT = "财务评价";

	/*
	 * 期末主要经营数据
	 */

	/**
	 * 期末主要经营数据-期末主要经营数据TEXT属性值
	 */
	public static String RPTTEXT1 = "期末主要经营数据";

	/**
	 * 期末主要经营数据-期末货币资金TEXT属性值
	 */
	public static String FINALCOINAMTTEXT = "期末货币资金";

	/**
	 * 期末主要经营数据-在建工程TEXT属性值
	 */
	public static String CSTRINPRGTEXT = "在建工程";

	/**
	 * 期末主要经营数据-固定资产TEXT属性值
	 */
	public static String FIXASSTEXT = "固定资产";

	/**
	 * 期末主要经营数据-其他应收款TEXT属性值
	 */
	public static String OTHRCVMNYTEXT = "其他应收款";

	/**
	 * 期末主要经营数据-其他应付款TEXT属性值
	 */
	public static String OTHPAYMNYTEXT = "其他应付款";

	/**
	 * 期末主要经营数据-短期借款TEXT属性值
	 */
	public static String SHORTMNYTEXT = "短期借款";

	/**
	 * 期末主要经营数据-长期借款TEXT属性值
	 */
	public static String LONGMNYTEXT = "长期借款";

	/**
	 * 期末主要经营数据-负债合计TEXT属性值
	 */
	public static String DEBTTOLTEXT = "负债合计";

	/**
	 * 期末主要经营数据-资产总计TEXT属性值
	 */
	public static String GRANDTOLTEXT = "资产总计";

	/**
	 * 期末主要经营数据-实收资本TEXT属性值
	 */
	public static String PUPCAPLTEXT = "实收资本";

	/**
	 * 财务组件接口
	 */
	public static final String FNC_IFACE = "Fnc4Rsc";
}
