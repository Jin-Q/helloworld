package com.yucheng.cmis.biz01line.esb.pub;

/**
 * 该类存放接口交易过程中常量
 * @author Pansq
 */
public class TradeConstance {
	
	/** ESB中系统级报文头名称 */
	public static final String ESB_SYS_HEAD = "SYS_HEAD";
	/** ESB中应用级报文头名称 */
	public static final String ESB_APP_HEAD = "APP_HEAD";
	/** ESB中本地报文头名称 */
	public static final String ESB_LOCAL_HEAD = "LOCAL_HEAD";
	/** ESB中本地报文头名称 */
	public static final String ESB_BODY = "BODY";
	
	/** ESB中interface注册服务 */
	public static final String ESBINTERFACE = "ESBInterface";
	/** ESB中component注册服务 */
	public static final String ESBCOMPONENT = "ESBComponent";
	/** ESB中dao注册服务 */
	public static final String ESBDAO = "ESBDao";
	/** 信贷作为消费者ID */
	public static final String CONSUMERID = "300008";
	/** 交易发送系统 */
	public static final String TRADE_SYSTEM = "XD";
	/** 泉州银行银行码 */
	public static final String BANK_ID = "BBCFC";
	/** 泉州银行同业业务过渡户账号 */
	public static final String BANK_ACCT_NO = "31510";
	/** 贷款发放授权交易码 */
	public static final String SERVICE_CODE_DKFFSQ = "30210004";
	/** 贷款展期授权交易码 */
	public static final String SERVICE_CODE_DKZQSQ = "02002000011";
	/** 保函发放授权交易码 */
	public static final String SERVICE_CODE_BHFFSQ = "02001000001";
	/** 贷款承诺授权交易码 */
	public static final String SERVICE_CODE_DKCNSQ = "02001000001";
	/** 银承发放授权交易码 */
	public static final String SERVICE_CODE_YCFFSQ = "02001000001";
	/** 贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权交易接口 */
	public static final String SERVICE_CODE_TXFFSQ = "03001000008";
	/** 贷款还款计划维护 */
	public static final String SERVICE_CODE_DKHKSQ = "02002000011";
	/**贷款放款试算器交易接口*/
	public static final String SERVICE_CODE_DKFKSQ = "02002000002";
	/**账户信息获取交易接口*/
	public static final String SERVICE_CODE_ZHXX = "11003000007";
	/**信用证/保函维护获取交易接口*/
	public static final String SERVICE_CODE_XYZBHWH = "06001000003";
	/**质押管理交易码*/
	public static final String SERVICE_CODE_ZYGL = "02002000012";
	/**贷款核销授权交易码*/
	public static final String SERVICE_CODE_DKHXSQ = "30220005";
	/**贷款利率变更*/
	public static final String SERVICE_CODE_LLTZ = "02001000003";
	/**贷款利率信息维护*/
	public static final String SERVICE_CODE_DKLLXXWH = "02002000007";
	
	/**服务交易码：贸易融资信贷出账实时请求（表外业务）- 贷款信息登记*/
	public static final String SERVICE_CODE_GJ = "02001000005";  
	
	/**服务交易码：票据信息查询*/
	public static final String SERVICE_CODE_ECDS1 = "03003000002";  
	/**服务交易码：票据冲正*/
	public static final String SERVICE_CODE_ECDS2 = "03004000002";
	/**服务交易码：票据承兑审批*/
	public static final String SERVICE_CODE_ECDS3 = "03002000002";
	/**服务交易码：票据贴现审批*/
	public static final String SERVICE_CODE_ECDS4 = "03002000005";
	/**服务交易码：票据转贴现审批*/
	public static final String SERVICE_CODE_ECDS5 = "03002000006"; 
	/**服务交易码：票据相关业务计账通知服务*/
	public static final String SERVICE_CODE_ECDS6 = "03002000003"; 

	/**add by lisj 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷 产品需求 begin**/
	/**服务交易码：贷款信息维护*/
	public static final String SERVICE_CODE_LOANINFOMAINTAIN  = "02002000010";
	/**贷款信息维护场景代码*/
	public static final String SERVICE_SCENE_LOANINFOMAINTAIN = "01";
	/**服务交易码：微贷额度登记*/
	public static final String SERVICE_CODE_LOANQUOREG = "02002000005";
	/**微贷额度登记场景代码*/
	public static final String SERVICE_SCENE_LOANQUOREG = "01";
	/**added by yangzy 2015/04/08 需求编号:XD150318023,微贷平台零售自助贷款改造*/
	public static final String SERVICE_SCENE_ZZDKFFSQ = "01";
	/**add by lisj 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷 产品需求 end**/
	
	/**服务交易码：保证金占用查询交易*/
	public static final String SERVICE_CODE_BZJZYCX = "02003000004";
	/**服务交易码：保证金追加实时通知国结*/
	public static final String SERVICE_CODE_BZJZJTZGJ = "11002000080";
	/**服务交易码：保证金变更*/
	public static final String SERVICE_CODE_BZJBG = "30120002";
	/**服务交易码：综合授信额度查询*/
	public static final String SERVICE_CODE_SXQKCX2EBANK = "02003000001";
	/**综合授信额度查询场景代码*/
	public static final String SERVICE_SCENE_SXQKCX2EBANK = "02";
	/**服务交易码：贷款信息查询*/
	public static final String SERVICE_CODE_DKQKCX2EBANK = "02003000002";
	/**贷款信息查询场景代码*/
	public static final String SERVICE_SCENE_DKQKCX2EBANK = "47";
	/**服务交易码：贴现票据查询*/
	public static final String SERVICE_CODE_TXQKCX2EBANK = "03003000002";
	/**贴现票据查询场景代码*/
	public static final String SERVICE_SCENE_TXQKCX2EBANK = "31";
	/**服务交易码：保函业务查询*/
	public static final String SERVICE_CODE_BHQKCX2EBANK = "02003000005";
	/**保函业务查询场景代码*/
	public static final String SERVICE_SCENE_BHQKCX2EBANK = "05";
	/**服务交易码：垫款信息查询*/
	public static final String SERVICE_CODE_DIANKQKCX2EBANK = "02003000002";
	/**垫款信息查询场景代码*/
	public static final String SERVICE_SCENE_DIANKQKCX2EBANK = "26";
	/**服务交易码：委托贷款查询*/
	public static final String SERVICE_CODE_WTDKQKCX2EBANK = "02003000002";
	/**委托贷款查询场景代码*/
	public static final String SERVICE_SCENE_WTDKQKCX2EBANK = "48";
	/**服务交易码：银承用信查询*/
	public static final String SERVICE_CODE_YCQKCX2EBANK = "02003000002";
	/**银承用信查询场景代码*/
	public static final String SERVICE_SCENE_YCQKCX2EBANK = "49";
		/**保证金本金反算查询场景代码*/
	public static final String SERVICE_SCENE_BZJBJFSCX = "58";
	/**服务交易码：保证金利息试算*/
	public static final String SERVICE_CODE_BZJLXSS = "02002000002";
	/**保证金利息试算场景代码*/
	public static final String SERVICE_SCENE_BZJLXSS = "06";
	
	/**保证金账户信息获取场景代码*/
	public static final String SERVICE_SCENE_DKZH = "17";
	/**账户信息获取场景代码*/
	public static final String SERVICE_SCENE_BZJZH = "16";
	/** 贷款发放授权服务场景代码 */
	public static final String SERVICE_SCENE_DKFFSQ = "02";
	/** 贷款展期维护授权服务场景代码 */
	public static final String SERVICE_SCENE_DKZQSQ = "01";
	/** 保函发放授权服务场景代码 */
	public static final String SERVICE_SCENE_BHFFSQ = "08";
	/** 贷款承诺授权服务场景代码 */
	public static final String SERVICE_SCENE_DKCNSQ = "09";
	/** 银承发放授权服务场景代码 */
	public static final String SERVICE_SCENE_YCFFSQ = "07";
	/**  贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权服务场景代码 */
	public static final String SERVICE_SCENE_TXFFSQ = "01";
	/**  贷款放款试算器交易接口服务场景代码 */
	public static final String SERVICE_SCENE_DKFKSQ = "04";
	/**  信用证修改/保函修改交易接口服务场景代码 */
	public static final String SERVICE_SCENE_XYZBHSQ = "01";
	/** 质押出入库授权服务场景代码 */
	public static final String SERVICE_SCENE_ZYCRKSQ = "01";
	/** 信贷利率调整 */
	public static final String SERVICE_SCENE_XDLLTZ = "01";
	/** 利率调整授权 */
	public static final String SERVICE_SCENE_LLTZSQ = "01";
	
	/**服务场景码：贸易融资信贷出账实时请求（表外业务）- 国际结算出帐*/
	public static final String SERVICE_SCENE_GJSQ = "02";
	
	/**资产转让出账授权服务场景代码*/
	public static final String SERVICE_SCENE_ZCZRSQ = "02";
	
	/**保证金占用查询交易服务场景代码*/
	public static final String SERVICE_SCENE_BZJZYCX = "02";
	/**保证金追加实时通知国结服务场景代码*/
	public static final String SERVICE_SCENE_BZJZJTZGJ = "55";
	/**保证金变更服务场景代码*/
	public static final String SERVICE_SCENE_BZJBG = "01";

	/** FTP服务器文件路径 */
	public static final String FTP_REMOTE_FILE_PATH = "/esb_xd/tradetempfile/";
	
	/** 返回码明细 */
	public static final String RETCODE1 = "000000";//成功
	
	/** 系统头反馈状态明细 */
//	public static final String RETSTATUS1 = "S";//成功
	public static final String RETSTATUS1 = "交易成功";//成功
	
	/** 授权从表标识（对应字典项：STD_ZB_BUSI_CLS） */
	/** 保证金信息 */
	public static final String BUSI_TYPE_LX = "05";
	/** 保证金信息 */
	public static final String BUSI_TYPE_BZ = "04";
	/** 账号信息 */
	public static final String BUSI_TYPE_ZH = "03";
	/** 费用信息 */
	public static final String BUSI_TYPE_FY = "02";
	/** 还款计划 */
	public static final String BUSI_TYPE_HK = "01";
	
	/** 费用收付标志 */
	public static final String is_pay_flag = "R";//R: 收;P: 付
	/** 费用扣款账户类型   */
	public static final String acct_type = "FEE";//FEE:收费账户
}
