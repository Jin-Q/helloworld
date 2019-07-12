package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.formula.functions.Value;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateCusComRepRecordOp extends CMISOperation {

	private final String modelId = "CusCom";
	private final String modelIdBase = "CusBase";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			KeyedCollection kCollBase = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollBase = (KeyedCollection) context.getDataElement(modelIdBase);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId+ "] cannot be empty!");
			if (kCollBase == null || kCollBase.size() == 0) 
				throw new EMPJDBCException("The values to update[" + modelIdBase + "] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			String cus_id = (String) kCollBase.getDataValue("cus_id");
			kColl.addDataField("cus_id", cus_id);
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败！");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败！");
			}
			String certType = kCollBase.getDataValue("cert_type").toString();
			String certCode = kCollBase.getDataValue("cert_code").toString();
			
			//新增前先去查询核心是否有该客户
			
			if(certType==null || "".equals(certType) || certCode==null || "".equals(certCode)){
				throw new Exception("证件类型或证件号码不允许为空！");
			}
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body"); 
			//组装报文头
			qryheadKcoll.put("SvcCd", "20130001");
			qryheadKcoll.put("ScnCd", "01");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			//BODY
			qrybodyColl.addDataField("QryInd","2");//2-按证件查询
			qrybodyColl.addDataField("AcctNoCrdNo",""); 
			qrybodyColl.addDataField("CstNo",""); 
			qrybodyColl.addDataField("IdentTp",getCertType(certType)); 
			qrybodyColl.addDataField("IdentNo",certCode); 
			qrybodyColl.addDataField("CtcTelNo",""); 
			qrybodyColl.addDataField("TaskSrlNo","");  
			KeyedCollection retKColl = new KeyedCollection(); 
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			String opType = "01";
			String cstNo = "";
			if(retKColl.containsKey("BODY")){
				KeyedCollection reBodyKcoll = (KeyedCollection) retKColl.get("BODY");
			    if(reBodyKcoll.containsKey("CstNo")&&NewStringUtils.isNotBlank((String)reBodyKcoll.getDataValue("CstNo"))){
			    	cstNo = (String)reBodyKcoll.getDataValue("CstNo");
			    	opType = "02";
			    }
				
			}
			
			if("01".equals(opType)){
				
				
				
				KeyedCollection headKcoll = new KeyedCollection();
				KeyedCollection bodyKcoll = new KeyedCollection();
				//esb开户
				headKcoll.put("SvcCd", "20120001");
				headKcoll.put("ScnCd", "03");
				headKcoll.addDataField("TxnMd","ONLINE");
				headKcoll.addDataField("UsrLngKnd","CHINESE");
				headKcoll.addDataField("jkType","cbs"); 
				 
				bodyKcoll.addDataField("OprtnTp",opType);//操作类型
				bodyKcoll.addDataField("CstNo",cstNo);//客户号
				bodyKcoll.addDataField("CstGrpgTp","");//客户分组
				bodyKcoll.addDataField("CstTp",getComCusType(valueOf(kCollBase.getDataValue("cus_type")))[0]);//客户类型码值对应待处理
				bodyKcoll.addDataField("CstClCtgry",getComCusType(valueOf(kCollBase.getDataValue("cus_type")))[1]);//客户分类类别码值对应待处理valueOf(kCollBase.getDataValue("cus_type"))
				bodyKcoll.addDataField("CstShrtNm",valueOf(kCollBase.getDataValue("cus_short_name")));//客户简称
				bodyKcoll.addDataField("CstEnNm",valueOf(kColl.getDataValue("cus_en_name")));//
				bodyKcoll.addDataField("CstChinNm",valueOf(kCollBase.getDataValue("cus_name")));//客户中文名称
				bodyKcoll.addDataField("HlpMmryNm","");//助记名称
				bodyKcoll.addDataField("Adr",valueOf(kColl.getDataValue("street_post")));//地址
				bodyKcoll.addDataField("RsdnAdrCtyCd",valueOf(kCollBase.getDataValue("cus_country")));//居住地址国家代码
				bodyKcoll.addDataField("RskCntrlCtyCd",valueOf(kCollBase.getDataValue("cus_country")));//风险控制国家代码
				bodyKcoll.addDataField("LoProvCd",kColl.getDataValue("reg_state_code").toString().substring(0, 2));//所在省份代码
				bodyKcoll.addDataField("CstCityCd",valueOf(kColl.getDataValue("reg_state_code")));//客户城市代码
				bodyKcoll.addDataField("CstNtntyCd",valueOf(kCollBase.getDataValue("cus_country")));//客户国籍代码
				bodyKcoll.addDataField("DmstOvrsFlg","I");//境内境外标志
				bodyKcoll.addDataField("IdyTp",valueOf(kColl.getDataValue("com_cll_type")));//行业类型
				bodyKcoll.addDataField("CstBsnKnd","");//客户业务种类
				bodyKcoll.addDataField("RsrvFldInf","");
				bodyKcoll.addDataField("RsrvFldInf1","");
				bodyKcoll.addDataField("RsrvFldInf2","");
				bodyKcoll.addDataField("RsrvFldInf3","");
				bodyKcoll.addDataField("RsrvFldInf4","");
				bodyKcoll.addDataField("SmyDsc","");//摘要
				bodyKcoll.addDataField("InnrCstFlg",getInnerCus(valueOf(kColl.getDataValue("is_ours_rela_cust"))));//内部客户标志
				bodyKcoll.addDataField("CstExgRatePrfrncRto","");//客户汇率优惠比例
				bodyKcoll.addDataField("RskRto","");//风险权重
				bodyKcoll.addDataField("CstSt","1");//客户状态
				bodyKcoll.addDataField("CstStDsc","");//客户状态描述
				bodyKcoll.addDataField("AcctTxnSt","A");//账户交易状态
				bodyKcoll.addDataField("ClsDt","");//关闭日期
				bodyKcoll.addDataField("CstInd","N");//客户标识
				bodyKcoll.addDataField("CnlTp","");//渠道类型
				bodyKcoll.addDataField("FnlAmdtTlrNo",valueOf(kCollBase.getDataValue("cust_mgr")));//最后修改柜员号
				bodyKcoll.addDataField("WrtLngTp","");//书写语言种类
				bodyKcoll.addDataField("CmnctnLngTp","");//交流语言种类
				bodyKcoll.addDataField("FnlModDt",valueOf(context.getDataValue(CMISConstance.OPENDAY)).replaceAll("-", ""));//最后更改日期
				bodyKcoll.addDataField("TempCstFlg","");//临时客户标志
				bodyKcoll.addDataField("CrtTlrNo",valueOf(kCollBase.getDataValue("cust_mgr")));//创建柜员号
				bodyKcoll.addDataField("CrtDt",valueOf(kCollBase.getDataValue("input_date")).replaceAll("-", ""));//创建日期
				bodyKcoll.addDataField("PrntLngTp","");//打印语言种类
				bodyKcoll.addDataField("DataIncomInd","");//资料不全标识
				bodyKcoll.addDataField("PEPInd","");//PEP标识
				bodyKcoll.addDataField("CstLvl","");//客户等级/客户级别
				bodyKcoll.addDataField("EvltLvlDt","");//评级日期
				bodyKcoll.addDataField("RcvTaxFlg","N");//收税标志
				bodyKcoll.addDataField("CstAlsNm","");//客户别名
				bodyKcoll.addDataField("CstMgrCd","CMS01");//客户经理
				bodyKcoll.addDataField("PrftCnrlCd","");//利润中心代码
				bodyKcoll.addDataField("CntrlBrBnkCd",valueOf(context.getDataValue(CMISConstance.ATTR_ORGID)));//控制分行代码
				bodyKcoll.addDataField("DstcCd",valueOf(kColl.getDataValue("post_addr")));//地区代码/区域代码
				bodyKcoll.addDataField("Pstcd",valueOf(kColl.getDataValue("post_code")));//邮政编码
				bodyKcoll.addDataField("CntrlDeptCd","");//控制部门代码
				bodyKcoll.addDataField("BlcklistCstFlg","N");//黑名单客户标志
				bodyKcoll.addDataField("PblcAgntNm","");//代办人姓名
				bodyKcoll.addDataField("PblcAgntIdentTp","");//代理人证件类型
				bodyKcoll.addDataField("PblcAgntIdentNo","");//代理人证件号码
				bodyKcoll.addDataField("PblcAgntIdentExprtnDt","");//代办人证件到期日期
				bodyKcoll.addDataField("SwftNo","");//SWIFT编号
				bodyKcoll.addDataField("CnrlBnkNo","");//中央银行行号
				bodyKcoll.addDataField("BnkCd","");//银行代码
				bodyKcoll.addDataField("NtnTaxTxtnRgstCtfNo",valueOf(kColl.getDataValue("nat_tax_reg_code")));//国税税务登记证号
				bodyKcoll.addDataField("TxnInstCd","");//机构代码
				bodyKcoll.addDataField("EcnmSpclZoneCd","");//经济特区代码
				bodyKcoll.addDataField("RgstCtyCd","CHN");//注册国家代码
				bodyKcoll.addDataField("RunCtyCd","CHN");//运行国家代码
				bodyKcoll.addDataField("EmplyNum",valueOf(kColl.getDataValue("com_employee")));//员工数
				bodyKcoll.addDataField("CoFoundDt",valueOf(kColl.getDataValue("com_str_date")).replaceAll("-", ""));//公司成立日期
				bodyKcoll.addDataField("IvsrNm","");//投资人名称
				bodyKcoll.addDataField("CoPlanDsc","");//公司计划
				bodyKcoll.addDataField("FrgnExgRgstCtfNo","");//外汇登记证编号
				bodyKcoll.addDataField("FrgnExgRgstCtfIssuAdr","");//外汇登记证签发地
				bodyKcoll.addDataField("RgstrtnCptlCcy",valueOf(kColl.getDataValue("reg_cur_type")));//注册资本币种
				bodyKcoll.addDataField("RgstrtnCptlAmt",valueOf(kColl.getDataValue("reg_cap_amt")));//注册资本
				bodyKcoll.addDataField("ActRcvCptlCcy",valueOf(kColl.getDataValue("paid_cap_cur_type")));//实收资本币种
				bodyKcoll.addDataField("ActRcvCptlAmt",valueOf(kColl.getDataValue("paid_cap_amt")));//实收资本
				bodyKcoll.addDataField("CorprtnScopDsc","");//经营范围
				bodyKcoll.addDataField("OrgInstCd",valueOf(kCollBase.getDataValue("cert_code1")));//组织机构代码
				bodyKcoll.addDataField("FrgnExgLvlCtfNo","");//外汇等级证号
				bodyKcoll.addDataField("IOEBsnCorprtnQualfNo","");//进出口业务经营资格编号
				bodyKcoll.addDataField("LglPrsnRprsNm","");//法人代表名称
				bodyKcoll.addDataField("LglPrsnRprsIdentTp","");//法人代表证件类型
				bodyKcoll.addDataField("LglPrsnRprsIdentNo","");//法人代表证件号码
				bodyKcoll.addDataField("LglPrsnRprsIdentExprtnDt","");//法人代表证件到期日期
				bodyKcoll.addDataField("AprvCtfLtrNo","");//外商投资批准证书号
				bodyKcoll.addDataField("FIPrmtNo","");//金融机构许可证号
				bodyKcoll.addDataField("TxnEmailAdr",valueOf(kColl.getDataValue("email")));//交易用Email
				bodyKcoll.addDataField("EcnmTp","");//经济类型
				bodyKcoll.addDataField("SAICBsnLicAnulChkDt","");//工商执照年检日期
				bodyKcoll.addDataField("LoanCrdNo","");//贷款卡号
				bodyKcoll.addDataField("EndTp","");//终止类型
				bodyKcoll.addDataField("CstEndDt","");//客户终止日期
				bodyKcoll.addDataField("OfclWebstAdr",valueOf(kColl.getDataValue("web_url")));//官方网站
				bodyKcoll.addDataField("BnkLdrFlg","");//银行负责人标志
				bodyKcoll.addDataField("SpfLoanLdrFlg","");//指定贷款负责人标志
				bodyKcoll.addDataField("CrdtAcctLvl","");//贷方账户级别
				bodyKcoll.addDataField("SpfLoanSdlnLdrFlg","");//指定贷款副负责人标志
				bodyKcoll.addDataField("SpfBnkSdlnLdrFlg","");//指定银行副负责人标志
				bodyKcoll.addDataField("SpfCoScrtyFlg","");//指定公司秘书标志
				bodyKcoll.addDataField("MinHldngFlg","");//最小控股标志
				bodyKcoll.addDataField("RskCntrlFlg","");//风险控制标志
				bodyKcoll.addDataField("IntrmdyValFlg","");//中介推崇标志
				bodyKcoll.addDataField("TelFaxInsrFlg","");//电话传真指令标志
				bodyKcoll.addDataField("TelFaxInsrCstFlg","");//电话传真指令客户标志
				bodyKcoll.addDataField("OtsdFltIntRate","");//外部浮动利率
				bodyKcoll.addDataField("DfltPrblRate","");//违约概率(PD)
				bodyKcoll.addDataField("BrwLvl","");//借款人等级
				bodyKcoll.addDataField("MktPcpTp","");//市场参与者类型
				bodyKcoll.addDataField("MainMgtCoNm",valueOf(kColl.getDataValue("admin_org")));//主管单位名称
				bodyKcoll.addDataField("EntpSclTp",getComScale(valueOf(kColl.getDataValue("com_scale"))));//企业规模
				bodyKcoll.addDataField("BscAcctNo",valueOf(kColl.getDataValue("bas_acc_no")));//基本账户账号
				bodyKcoll.addDataField("BscAcctOpnAcctBnkNo",valueOf(kColl.getDataValue("bas_acc_bank")));//基本户开户行行号
				bodyKcoll.addDataField("SpclIdyPrmtNo",valueOf(kColl.getDataValue("com_sp_lic_no")));//特殊行业许可证书号
				bodyKcoll.addDataField("BsnLicSt","");//营业执照状态
				bodyKcoll.addDataField("TaxCtfVldDt","");//税务证有效期
				bodyKcoll.addDataField("FrgnExgCtfVldDt","");//外汇证有效期
				bodyKcoll.addDataField("RgstRgstrtnNoTp",getRegType(valueOf(kColl.getDataValue("reg_type"))));//登记注册号类型09
				bodyKcoll.addDataField("RgstRgstrtnNo",valueOf(kColl.getDataValue("reg_code")));//登记注册号
				bodyKcoll.addDataField("GrpFrzFlg","");//群冻结标志
				bodyKcoll.addDataField("FrzRsn","");//冻结原因
	
				IndexedCollection identInfIColl = new IndexedCollection();
				identInfIColl.setName("IdentInfArry");
				
				KeyedCollection identInfKColl = new KeyedCollection();
				identInfKColl.addDataField("CstNo", cstNo);
				identInfKColl.addDataField("IdentTp", getCertType(valueOf(kCollBase.getDataValue("cert_type"))));
				identInfKColl.addDataField("IdentNo", valueOf(kCollBase.getDataValue("cert_code")));
				identInfKColl.addDataField("IssuCtyCd", valueOf(kCollBase.getDataValue("cus_country")));
				identInfKColl.addDataField("IssuCityCd", valueOf(kColl.getDataValue("reg_state_code")));
				identInfKColl.addDataField("IssuDt", valueOf(kColl.getDataValue("com_ins_reg_date")).replaceAll("-", ""));
				identInfKColl.addDataField("IssuAdr", "");
				identInfKColl.addDataField("ExprtnDt", valueOf(kColl.getDataValue("com_ins_exp_date")).replaceAll("-", ""));
				identInfKColl.addDataField("ChiefFlg", "Y");
				identInfKColl.addDataField("AuthInstCd", "");
				identInfKColl.addDataField("IssuAdrDstcCd", valueOf(kColl.getDataValue("reg_state_code")));
				identInfKColl.addDataField("OprtnTp", opType);
				identInfKColl.addDataField("IssuProvCd", valueOf(kColl.getDataValue("reg_state_code")).substring(0, 2));
				identInfKColl.addDataField("LastChkDt", "");
				identInfIColl.add(identInfKColl);
				bodyKcoll.addDataElement(identInfIColl);
				
				IndexedCollection cstCtcInfIColl = new IndexedCollection();
				cstCtcInfIColl.setName("CstCtcInfArry");
				KeyedCollection cstCtcInfKColl = new KeyedCollection();
				cstCtcInfKColl.addDataField("CstNo", cstNo);
				cstCtcInfKColl.addDataField("CtcPrsnNm", valueOf(kCollBase.getDataValue("cus_name")));
				cstCtcInfKColl.addDataField("CtcMd", "");
				cstCtcInfKColl.addDataField("ChiefFlg", "y");
				cstCtcInfKColl.addDataField("CtcMdTp", "01");
				cstCtcInfKColl.addDataField("OprtnTp", opType);
				cstCtcInfKColl.addDataField("CtcTelNo", valueOf(kColl.getDataValue("phone")));
				cstCtcInfIColl.add(cstCtcInfKColl);
				bodyKcoll.addDataElement(cstCtcInfIColl);
						
				bodyKcoll.addDataField("LglPrsnCd","");
				bodyKcoll.addDataField("LglPrsnIdentIssuDt","");
				bodyKcoll.addDataField("IOEBsnPrvgFlg","");
				bodyKcoll.addDataField("TxtnRgstCtfyDsc","");
				bodyKcoll.addDataField("NtnTaxTxtnRgstCtfVldDt",valueOf(kColl.getDataValue("nat_tax_reg_end_dt")).replaceAll("-", ""));
				bodyKcoll.addDataField("CityTaxVldDt",valueOf(kColl.getDataValue("loc_tax_reg_end_dt")).replaceAll("-", ""));
				bodyKcoll.addDataField("CityTaxRgstNo",valueOf(kColl.getDataValue("loc_tax_reg_code")));
				bodyKcoll.addDataField("SpclEcnmAreaFlg",valueOf(kColl.getDataValue("com_sp_business")));
				bodyKcoll.addDataField("SpclEcnmAreaTp","");
				bodyKcoll.addDataField("TUAFlg","N");
				bodyKcoll.addDataField("CtyEcnmDeptCd",valueOf(kColl.getDataValue("econ_dep")));
				bodyKcoll.addDataField("IvsrEcnmTp","");
				bodyKcoll.addDataField("EntpEcnmTp","");
				bodyKcoll.addDataField("BnkInnrIdyTp",valueOf(kColl.getDataValue("com_cll_type")));
				bodyKcoll.addDataField("CtyStdPrimIdyTp","");
				bodyKcoll.addDataField("CtyStdScdLvlIdyTp","");
				bodyKcoll.addDataField("CtyStdThrLvlIdyTp","");
				bodyKcoll.addDataField("CtyStdFourLvlIdyTp","");
				bodyKcoll.addDataField("RgstCtyNm","");
				bodyKcoll.addDataField("Rmk","");
				//bodyKcoll.addDataField("OprtnTp",opType);
				
				IndexedCollection relPrsnInfIColl = new IndexedCollection();
				relPrsnInfIColl.setName("RelPrsnInfArry");
				KeyedCollection relPrsnInfKColl = new KeyedCollection();
				relPrsnInfKColl.addDataField("RelPrsnTp","103");//关联人类型
				relPrsnInfKColl.addDataField("IdentTp",getCertType(valueOf(kCollBase.getDataValue("cert_type"))));
				relPrsnInfKColl.addDataField("IdentNo",valueOf(kCollBase.getDataValue("cert_code")));
				relPrsnInfKColl.addDataField("RelPrsnNm",valueOf(kColl.getDataValue("com_operator")));
				relPrsnInfKColl.addDataField("IssuDt","");
				relPrsnInfKColl.addDataField("ExprtnDt","");
				relPrsnInfKColl.addDataField("OprtnTp",opType);
				relPrsnInfKColl.addDataField("CtcTelNo",valueOf(kColl.getDataValue("fina_per_phone")));
				relPrsnInfIColl.addDataElement(relPrsnInfKColl);
				bodyKcoll.addDataElement(relPrsnInfIColl);
				
				IndexedCollection qryPswdInfIColl = new IndexedCollection();
				qryPswdInfIColl.setName("QryPswdInfArry");
				KeyedCollection qryPswdInfKColl = new KeyedCollection();  
				qryPswdInfKColl.addDataField("PswdTp", "QY");
				qryPswdInfKColl.addDataField("QryPswdStrg", "000000");
				qryPswdInfKColl.addDataField("PswdTkEffDt", ((String)context.getDataValue("OPENDAY")).replace("-", "")); //密码生效日期
				qryPswdInfIColl.add(qryPswdInfKColl);
				bodyKcoll.addDataElement(qryPswdInfIColl);
				
				bodyKcoll.addDataField("GrpCstFlg","");
				bodyKcoll.addDataField("CstInsrStdDsc","");
				
	//			IndexedCollection benfInfIColl = new IndexedCollection();
	//			benfInfIColl.setName("BenfInfArry");
	//			KeyedCollection  benfInfKColl = new KeyedCollection();  
	//			benfInfKColl.addDataField("OprtnTp",opType);
	//			benfInfKColl.addDataField("BenfNm","");
	//			benfInfKColl.addDataField("IdentTp","");
	//			benfInfKColl.addDataField("IdentNo","");
	//			benfInfKColl.addDataField("IssuDt","");
	//			benfInfKColl.addDataField("ExprtnDt","");
	//			benfInfKColl.addDataField("BnftMd","");
	//			benfInfKColl.addDataField("CstNo","");
	//			benfInfIColl.add(benfInfKColl);
	//			bodyKcoll.addDataElement(benfInfIColl);
				
				IndexedCollection cmnctnAdrInfIColl = new IndexedCollection();
				cmnctnAdrInfIColl.setName("CmnctnAdrInfArry");
				KeyedCollection cmnctnAdrInfKColl = new KeyedCollection(); 
				cmnctnAdrInfKColl.addDataField("OprtnTp", opType);
				cmnctnAdrInfKColl.addDataField("CstNo", cstNo);
				cmnctnAdrInfKColl.addDataField("AdrTp", "01");//联系地址
				cmnctnAdrInfKColl.addDataField("CtyNm", "CHN");
				cmnctnAdrInfKColl.addDataField("ProvNm", valueOf(kColl.getDataValue("post_addr")).substring(0, 2));
				cmnctnAdrInfKColl.addDataField("CityCd", valueOf(kColl.getDataValue("post_addr")));
				cmnctnAdrInfKColl.addDataField("DstcCd", valueOf(kColl.getDataValue("post_addr")));
				cmnctnAdrInfKColl.addDataField("Adr", valueOf(kColl.getDataValue("street_post")));
				cmnctnAdrInfKColl.addDataField("Pstcd", valueOf(kColl.getDataValue("post_code")));
				cmnctnAdrInfKColl.addDataField("AdrMd", "");
				cmnctnAdrInfKColl.addDataField("PrfrdIdentFlg", "Y"); 
				cmnctnAdrInfIColl.addDataElement(cmnctnAdrInfKColl);
				KeyedCollection cmnctnRgAdrInfKColl = new KeyedCollection(); 
				cmnctnRgAdrInfKColl.addDataField("OprtnTp", opType);
				cmnctnRgAdrInfKColl.addDataField("CstNo", cstNo);
				cmnctnRgAdrInfKColl.addDataField("AdrTp", "02");//注册地址
				cmnctnRgAdrInfKColl.addDataField("CtyNm", "CHN");
				cmnctnRgAdrInfKColl.addDataField("ProvNm", valueOf(kColl.getDataValue("reg_addr")).substring(0, 2));
				cmnctnRgAdrInfKColl.addDataField("CityCd", valueOf(kColl.getDataValue("reg_addr")));
				cmnctnRgAdrInfKColl.addDataField("DstcCd", valueOf(kColl.getDataValue("reg_addr")));
				cmnctnRgAdrInfKColl.addDataField("Adr", valueOf(kColl.getDataValue("reg_addr")));
				cmnctnRgAdrInfKColl.addDataField("Pstcd", "");
				cmnctnRgAdrInfKColl.addDataField("AdrMd", "");
				cmnctnRgAdrInfKColl.addDataField("PrfrdIdentFlg", "Y");
				cmnctnAdrInfIColl.addDataElement(cmnctnRgAdrInfKColl);
				bodyKcoll.addDataElement(cmnctnAdrInfIColl);
						
				bodyKcoll.addDataField("EvdnDsc","");
				bodyKcoll.addDataField("OpnAcctPrmtNo","");
				bodyKcoll.addDataField("InstCrdtCdCtfCd","");
				bodyKcoll.addDataField("OrgInstCdCtfCd","");
				bodyKcoll.addDataField("ObtSelCtfyDclrFlg","Y");
				bodyKcoll.addDataField("RevIdtyTp","0");
	
				retKColl = ESBUtil.sendEsbMsg(headKcoll,bodyKcoll);
				KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
				KeyedCollection body=(KeyedCollection)retKColl.getDataElement("BODY");
				IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				if(!"000000".equals(retCd)){
					connection.rollback();
					context.addDataField("flag", PUBConstant.FAIL);
					context.addDataField("message", (String)retObj.getDataValue("RetInf"));
					return "0";
					//throw new EMPException((String)retObj.getDataValue("RetInf"));
				}else{
					String hxCusId=valueOf(body.getDataValue("CstNo"));
	//				KeyedCollection cusKcoll=new KeyedCollection("CusBase");
	//				cusKcoll.addDataField("cus_id", cus_id);
	//				cusKcoll.addDataField("hx_cus_id", hxCusId);
	//				dao.update(cusKcoll, connection);
					KeyedCollection cusKcoll=new KeyedCollection();
					cusKcoll.put("cus_id", cus_id);
					SqlClient.update("updateCusBase", cusKcoll, hxCusId, null, connection);
				}
			}else{
				KeyedCollection cusKcoll=new KeyedCollection();
				cusKcoll.put("cus_id", cus_id);
				SqlClient.update("updateCusBase", cusKcoll, cstNo, null, connection);
			}
			//将提示信息中的数据完成
			/*String cusId = (String) kColl.getDataValue("cus_id");
			String condition = " where cus_id='" + cusId + "' and end_flag='1' and opr_type in('1','3')";
			IndexedCollection iColl = dao.queryList("CusSubmitInfo", condition, connection);
			if(iColl.size() == 1){
				KeyedCollection kCollSub = (KeyedCollection) iColl.get(0);
				kCollSub.setDataValue("end_flag", "0");  //设置为完成，并将办结时间补充
				kCollSub.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
				dao.update(kCollSub, connection); 
			}else{
				context.addDataField("flag", PUBConstant.FAIL);
				context.addDataField("message", "补录失败！");
				return "0";
			}*/
			
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("message", "补录成功！");
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	public String valueOf(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	//证件类型转换
		public String getCertType(String oldCertType){
			Map<String, String> tCertType=new HashMap<String, String>();
			tCertType.put("0", "101");
			tCertType.put("6", "116");
			tCertType.put("7", "102");
			tCertType.put("8", "117");
			tCertType.put("9", "121");
			tCertType.put("a", "231");
			tCertType.put("b", "211");
			tCertType.put("1", "104");
			tCertType.put("2", "103");
			tCertType.put("3", "107");
			tCertType.put("4", "113");
			tCertType.put("c", "120");
			tCertType.put("X", "232");
			tCertType.put("d", "122");
			tCertType.put("e", "123");
			tCertType.put("f", "124");
			tCertType.put("5", "125");
			tCertType.put("99", "120");
			tCertType.put("20", "231");
			return tCertType.get(oldCertType);
		}
	//是否内部客户
	public String getInnerCus(String oldInnerCus){
		Map<String, String> tCertType=new HashMap<String, String>();
		tCertType.put("01", "Y");
		tCertType.put("02", "Y");
		tCertType.put("03", "N");
		return tCertType.get(oldInnerCus);
	}
	
	//对公客户类型
	/*public String getComCusType(String oldComCusType){
		if(oldComCusType.startsWith("A")){
			return "02";
		}else if(oldComCusType.startsWith("J")){
			return "03";
		}else if(oldComCusType.startsWith("Z")){
			return "01";
		}else if(oldComCusType.startsWith("B")||oldComCusType.startsWith("C")||oldComCusType.startsWith("D")||oldComCusType.startsWith("H")){
			return "05";
		}else{
			return "09";
		}
		
	}*/
	
	
	/**
	 * 客户类型的映射
	 * @param oldComCusType 根据上面cus_type得到的信贷系统中的客户类型 valueOf(kCollBase.getDataValue("cus_type"))
	 * @return 根据信贷客户类型所映射的esb核心系统的客户类型数组:esbComCusType[0]=CLIENT_TYPE;esbComCusType[1]=CATEGORY_TYPE;
	 */
	public String[] getComCusType(String oldComCusType){
		String [] esbComCusType = new String [2];
		//esbComCusType[0]表示 CLIENT_TYPE
		//esbComCusType[1]表示CATEGORY_TYPE
		switch(oldComCusType){
			case "A121":
			case "A122":
				esbComCusType[0] = "02";
				esbComCusType[1] = "201";
				break;
			case "A111":
			case "A112":
				esbComCusType[0] = "02";
				esbComCusType[1] = "207";
				break;
			case "A131":
			case "A132":
				esbComCusType[0] = "02";
				esbComCusType[1] = "208";
				break;
			case "A141":
			case "A142":
				esbComCusType[0] = "02";
				esbComCusType[1] = "209";
				break;
			case "Z3":
			case "I1":
				esbComCusType[0] = "02";
				esbComCusType[1] = "210";
				break;
			case "A3":
				esbComCusType[0] = "02";
				esbComCusType[1] = "212";
				break;
			case "D1":
			case "D2":
				esbComCusType[0] = "05";
				esbComCusType[1] = "404";
				break;
			case "E1":
			case "E2":
				esbComCusType[0] = "09";
				esbComCusType[1] = "501";
				break;
			case "B1":
			case "B2":
				esbComCusType[0] = "02";
				esbComCusType[1] = "221";
				break;
			case "J11":
				esbComCusType[0] = "03";
				esbComCusType[1] = "301";
				break;
			case "J131":
				esbComCusType[0] = "03";
				esbComCusType[1] = "303";
				break;
			case "J12":
				esbComCusType[0] = "03";
				esbComCusType[1] = "304";
				break;
			case "J21":
				esbComCusType[0] = "03";
				esbComCusType[1] = "306";
				break;
			case "J9":
				esbComCusType[0] = "03";
				esbComCusType[1] = "307";
				break;
			case "J181":
			case "J182":
				esbComCusType[0] = "03";
				esbComCusType[1] = "308";
				break;
			case "J133":
				esbComCusType[0] = "03";
				esbComCusType[1] = "312";
				break;
			case "J135":
				esbComCusType[0] = "03";
				esbComCusType[1] = "313";
				break;
			case "J14":
				esbComCusType[0] = "03";
				esbComCusType[1] = "319";
				break;
			case "J134":
				esbComCusType[0] = "03";
				esbComCusType[1] = "320";
				break;
			case "J31":
				esbComCusType[0] = "03";
				esbComCusType[1] = "322";
				break;
			case "J41":
				esbComCusType[0] = "03";
				esbComCusType[1] = "323";
				break;
			case "J132":
				esbComCusType[0] = "03";
				esbComCusType[1] = "324";
				break;
			case "J16":
				esbComCusType[0] = "03";
				esbComCusType[1] = "325";
				break;
			case "J22":
				esbComCusType[0] = "03";
				esbComCusType[1] = "327";
				break;
			case "J19":
				esbComCusType[0] = "03";
				esbComCusType[1] = "328";
				break;
			default:
				esbComCusType[0] = "09";
				esbComCusType[1] = "599";
				break;	
		}
		
		return esbComCusType;
	}
		
	//登记注册类型
	public String getRegType(String oldRegType){
		if("510".equals(oldRegType)||"410".equals(oldRegType)||"420".equals(oldRegType)){
			return "04";
		}else if("520".equals(oldRegType)){
			return "03";
		}else if("550".equals(oldRegType)){
			return "05";
		}else if("530".equals(oldRegType)||"540".equals(oldRegType)||"560".equals(oldRegType)||"570".equals(oldRegType)){
			return "02";
		}else if("580".equals(oldRegType)){
			return "09";
		}else{
			return "01";
		}
	}
	
	//企业规模
	public String getComScale(String comScale){
		if("10".equals(comScale)){
			return "2";
		}else if("20".equals(comScale)){
			return "3";
		}else if("30".equals(comScale)){
			return "3";
		}else if("31".equals(comScale)){
			return "5";
		}else if("90".equals(comScale)){
			return "9";
		}else{
			return "9";
		}
	}
	
}
