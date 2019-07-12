package com.yucheng.cmis.pub;

public class CusPubConstant {
	  /*
	 * 关联关系状态位
	 */
    public static final String KEYMAN="01";   //关键人关系
	
	public static final String FAMILYCONTROL="02";   //亲属控制关系
	
	public static final String STOCKCONTRO="03";   //股权控制关系
	
	public static final String GROUPREL="04";   //集团成员关系
	
	public static final String FAMILY="21";   //亲属关系
	
	public static final String OTHERREL="99";   //其他关联关系
	/*
	 * 客户信贷关系类型
	 *  10正常 20托管 30取消托管 40共享 50取消共享 60已移交  90 注销
	 */
	public final static String CUS_LOAN_REL_NORMAL = "10";
	public final static String CUS_LOAN_REL_TRUSTEE = "20";
	public final static String CUS_LOAN_REL_TRUSTEE_CANCEL = "30";
	public final static String CUS_LOAN_REL_SHARE = "40";
	public final static String CUS_LOAN_REL_HANSHARE="45";
	public final static String CUS_LOAN_REL_SHARE_CANCEL = "50";
	public final static String CUS_LOAN_REL_HANDOVER = "60";
	public final static String CUS_LOAN_REL_LOGOUT = "90";
	/**
	 * 客户信贷关系相关做操类型
	 * 客户移交 或者 托管 对应的操作类型
	 * 00－登记
     * 10－提交
     * 20－同意
     * 21－撤销
     * 22－否决
     * 30－成功
     * 40 托管收回
     * 50 拒绝
                     新增申请时，状态为“00-登记”
                     提交申请时，状态为“10-提交”
                    监交人审批后，状态为20、21、22
                    接受人接收后，状态为“30-成功”
	 */
	public final static String CUS_CREDIT_REGISTER = "00"; /**/
	public final static String CUS_CREDIT_SUBMIT = "10";
	public final static String CUS_CREDIT_AGREE = "20";
	public final static String CUS_CREDIT_CANCEL = "21";
	public final static String CUS_CREDIT_REJECT = "22";
	public final static String CUS_CREDIT_SUCC = "30";
	public final static String CUS_CREDIT_CALLBACK = "40";
	public final static String CUS_CREDIT_REFUSE = "50";

	/**
	 * 客户移交或者托管 操作机构与转出机构关系
	 */
	public final static String CUS_ORG_TYPE_SAME = "10"; //支行内
	public final static String CUS_CREDIT_DIFF = "21"; //夸支行
	/**
	 * 客户组件接口
	 */
	public static final String CUS_IFACE="CustomIface";
	
	/**
	 * 客户信贷关系接口
	 */
	public static final String CUS_LOAN_REL_IFACE="CusLoanRelIface";
	/**
	 * 个人农户批量评级信息
	 */
	public static final String CUS_FAR_CCR_BAT_IFACE="CusFarCcrBatIface";
	/**
	 * 银行关联客户接口ID
	 */
	public static final String CUSBANKCUSRELIFACE="CusBankCusRelIface";
	/**
         * 客户家庭收支负债接口ID
         */
        public static final String CUSINDIVFAMBLCIFACE="CusIndivFamBlcIface";
	  /**
	 * 不宜贷款户接口ID
	 */
	public static final String CUSBLKIFACE="CusBlkIface";

	/**
	 * 集团客户接口ID
	 */
	public static final String CUSGRPIFACE="CusGrpIface";
	/**
         * 个人社会关系接口ID
         */
        public static final String CUSINDIVSOCREL="CusIndivSocRelIface";

	  /**
	 * 客户状态位
	 */
	public static final String CUSSTATUSDRAFT="00";    //草稿
	
	public static final String CUSSTATUSBASE="10";     //基本信息完整
	
	public static final String CUSSTATUSBASESUB="11"; //-基本信息+从表信息完整
	
	public static final String CUSSTATUSBASESL="12";   //基本信息+从表信息+贷款信息完整
	
	public static final String CUSSTATUSBASESLD="13";  //基本信息+从表信息+贷款信息+贴现信息完整
	
	public static final String CUSSTATUSBASESLDO="14"; //基本信息+从表信息+贷款信息+贴现信息+表外业务信息完整
	
	public static final String CUSSTATUSFORMAL="20";   //正式客户
	
	public static final String CUSSTATUSLOGOUT="90";   //注销
	
	public static final String CUSSTATUSMerger="91";   //合并注销
	
	
	/**
	 * <p>客户管理模块ID</p>
	 */
	public final static String CUS_MODUAL_ID = "cus";
	
	public final static String ROLE_JZZY = "1020";  //集中作业岗
}
