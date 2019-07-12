package com.yucheng.cmis.biz01line.cus.cuscom.domain;

/**
 * Title: CusCom.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Mon Jan 11 14:45:16 CST 2010
 * @version：1.0
 */
public class CusCom implements com.yucheng.cmis.pub.CMISDomain {
	private String cusId;
	private String cusEnName;
	private String investType;
	private String comSubTyp;
	private String comScale;
	private String comHoldType;
	private String comInsCode;
	private String comCllType;
	private String comCllName;
	private String comStrDate;
	private double comEmployee;
	private String comInsRegDate;
	private String comInsExpDate;
	private String comInsAnnDate;
	private String comInsOrg;
	private String regCode;
	private String regType;
	private String adminOrg;
	private String apprOrg;
	private String apprDocNo;
	private String regStateCode;
	private String regAreaName;
	private String regAddr;
	private String enRegAddr;
	private String regCurType;
	private double regCapAmt;
	private String paidCapCurType;
	private double paidCapAmt;
	private String comMainOptScp;
	private String regStartDate;
	private String regEndDate;
	private String regAudit;
	private String regAuditDate;
	private String regAuditEndDate;
	private String natTaxRegCode;
	private String natTaxRegOrg;
	private String natTaxRegDt;
	private String natTaxRegEndDt;
	private String natTaxAnnDate;
	private String locTaxRegCode;
	private String locTaxRegOrg;
	private String locTaxRegDt;
	private String locTaxRegEndDt;
	private String locTaxAnnDate;
	private String fnaMgr;
	private String comOperator;
	private String postAddr;
	private String postCode;
	private String finaPerPhone;
	private String faxCode;
	private String email;
	private String webUrl;
	private String basAccFlg;
	private String basAccLicence;
	private String basAccBank;
	private String basAccNo;
	private String basAccDate;
	private String comFinRepType;
	private String comMrkFlg;
	private String comMrkArea;
	private String comStockId;
	private String comGrpMode;
	private String grpNo;
	private String comOutCrdGrade;
	private String comOutCrdDt;
	private String comOutCrdOrg;
	private String comMainProduct;
	private String comProdEquip;
	private String comFactProd;
	private double comOptAera;
	private String comOptOwner;
	private String comSpBusiness;
	private String comSpLicNo;
	private String comSpDetail;
	private String comSpLicOrg;
	private String comSpStrDate;
	private String comSpEndDate;
	private String comOptSt;
	private String comImpttFlg;
	private String comPrepFlg;
	private String comDhghFlg;
	private String comClEntp;
	private String comHdEnterprise;
	private String comInitLoanDate;
	private String comCityFlg;
	private String comMngOrg;
	private String acuAddr;
	private String goverFinTer;
	private String legalPhone;
	private String corpQlty;
	private String econDep;
	private String regAreaCode;
	private String isOursRelaCust;
	private String bankDuty;
	private String equiNo;
	private String finaPerTel;
	private String strCus;
	private String houExp;
	private String strCusEndDt;
	private String rowLice;
	private String custMgrPhone;
	private String rowLiceEndDt;
	private String comGuarCrdGrade2;
	private String comCoopExp;
	private String comGuarTyp;
	private String comGuarMulti;
	private String cusBankRel;
	private String bankEquiAmt;
	private String parentLoanCard;
	private String parentCertCode;
	private String parentCusName;
	private String street;
	private String guarCls;
	private String guarBailMultiple;
	
	public String getLegalPhone() {
		return legalPhone;
	}

	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}


	/**
	 * @return 返回 comGrpMode
	 */
	public String getComGrpMode() {
		return comGrpMode;
	}

	/**
	 * @设置 comGrpMode
	 * @param comGrpMode
	 */
	public void setComGrpMode(String comGrpMode) {
		this.comGrpMode = comGrpMode;
	}

	/**
	 * @return 返回 grpNo
	 */
	public String getGrpNo() {
		return grpNo;
	}

	/**
	 * @设置 grpNo
	 * @param grpNo
	 */
	public void setGrpNo(String grpNo) {
		this.grpNo = grpNo;
	}


	/**
	 * @return 返回 comOutCrdGrade
	 */
	public String getComOutCrdGrade() {
		return comOutCrdGrade;
	}

	/**
	 * @设置 comOutCrdGrade
	 * @param comOutCrdGrade
	 */
	public void setComOutCrdGrade(String comOutCrdGrade) {
		this.comOutCrdGrade = comOutCrdGrade;
	}

	/**
	 * @return 返回 comOutCrdDt
	 */
	public String getComOutCrdDt() {
		return comOutCrdDt;
	}

	/**
	 * @设置 comOutCrdDt
	 * @param comOutCrdDt
	 */
	public void setComOutCrdDt(String comOutCrdDt) {
		this.comOutCrdDt = comOutCrdDt;
	}

	/**
	 * @return 返回 comOutCrdOrg
	 */
	public String getComOutCrdOrg() {
		return comOutCrdOrg;
	}

	/**
	 * @设置 comOutCrdOrg
	 * @param comOutCrdOrg
	 */
	public void setComOutCrdOrg(String comOutCrdOrg) {
		this.comOutCrdOrg = comOutCrdOrg;
	}


	/**
	 * @return 返回 comMainProduct
	 */
	public String getComMainProduct() {
		return comMainProduct;
	}

	/**
	 * @设置 comMainProduct
	 * @param comMainProduct
	 */
	public void setComMainProduct(String comMainProduct) {
		this.comMainProduct = comMainProduct;
	}

	/**
	 * @return 返回 comProdEquip
	 */
	public String getComProdEquip() {
		return comProdEquip;
	}

	/**
	 * @设置 comProdEquip
	 * @param comProdEquip
	 */
	public void setComProdEquip(String comProdEquip) {
		this.comProdEquip = comProdEquip;
	}

	/**
	 * @return 返回 comFactProd
	 */
	public String getComFactProd() {
		return comFactProd;
	}

	/**
	 * @设置 comFactProd
	 * @param comFactProd
	 */
	public void setComFactProd(String comFactProd) {
		this.comFactProd = comFactProd;
	}

	/**
	 * @return 返回 comOptAera
	 */
	public double getComOptAera() {
		return comOptAera;
	}

	/**
	 * @设置 comOptAera
	 * @param comOptAera
	 */
	public void setComOptAera(double comOptAera) {
		this.comOptAera = comOptAera;
	}

	/**
	 * @return 返回 comOptOwner
	 */
	public String getComOptOwner() {
		return comOptOwner;
	}

	/**
	 * @设置 comOptOwner
	 * @param comOptOwner
	 */
	public void setComOptOwner(String comOptOwner) {
		this.comOptOwner = comOptOwner;
	}

	/**
	 * @return 返回 comSpBusiness
	 */
	public String getComSpBusiness() {
		return comSpBusiness;
	}

	/**
	 * @设置 comSpBusiness
	 * @param comSpBusiness
	 */
	public void setComSpBusiness(String comSpBusiness) {
		this.comSpBusiness = comSpBusiness;
	}

	/**
	 * @return 返回 comSpLicNo
	 */
	public String getComSpLicNo() {
		return comSpLicNo;
	}

	/**
	 * @设置 comSpLicNo
	 * @param comSpLicNo
	 */
	public void setComSpLicNo(String comSpLicNo) {
		this.comSpLicNo = comSpLicNo;
	}

	/**
	 * @return 返回 comSpDetail
	 */
	public String getComSpDetail() {
		return comSpDetail;
	}

	/**
	 * @设置 comSpDetail
	 * @param comSpDetail
	 */
	public void setComSpDetail(String comSpDetail) {
		this.comSpDetail = comSpDetail;
	}

	/**
	 * @return 返回 comSpLicOrg
	 */
	public String getComSpLicOrg() {
		return comSpLicOrg;
	}

	/**
	 * @设置 comSpLicOrg
	 * @param comSpLicOrg
	 */
	public void setComSpLicOrg(String comSpLicOrg) {
		this.comSpLicOrg = comSpLicOrg;
	}

	/**
	 * @return 返回 comSpStrDate
	 */
	public String getComSpStrDate() {
		return comSpStrDate;
	}

	/**
	 * @设置 comSpStrDate
	 * @param comSpStrDate
	 */
	public void setComSpStrDate(String comSpStrDate) {
		this.comSpStrDate = comSpStrDate;
	}

	/**
	 * @return 返回 comSpEndDate
	 */
	public String getComSpEndDate() {
		return comSpEndDate;
	}

	/**
	 * @设置 comSpEndDate
	 * @param comSpEndDate
	 */
	public void setComSpEndDate(String comSpEndDate) {
		this.comSpEndDate = comSpEndDate;
	}

	/**
	 * @return 返回 comOptSt
	 */
	public String getComOptSt() {
		return comOptSt;
	}

	/**
	 * @设置 comOptSt
	 * @param comOptSt
	 */
	public void setComOptSt(String comOptSt) {
		this.comOptSt = comOptSt;
	}

	/**
	 * @return 返回 comImpttFlg
	 */
	public String getComImpttFlg() {
		return comImpttFlg;
	}

	/**
	 * @设置 comImpttFlg
	 * @param comImpttFlg
	 */
	public void setComImpttFlg(String comImpttFlg) {
		this.comImpttFlg = comImpttFlg;
	}

	/**
	 * @return 返回 comPrepFlg
	 */
	public String getComPrepFlg() {
		return comPrepFlg;
	}

	/**
	 * @设置 comPrepFlg
	 * @param comPrepFlg
	 */
	public void setComPrepFlg(String comPrepFlg) {
		this.comPrepFlg = comPrepFlg;
	}

	/**
	 * @return 返回 comDhghFlg
	 */
	public String getComDhghFlg() {
		return comDhghFlg;
	}

	/**
	 * @设置 comDhghFlg
	 * @param comDhghFlg
	 */
	public void setComDhghFlg(String comDhghFlg) {
		this.comDhghFlg = comDhghFlg;
	}

	/**
	 * @return 返回 comClEntp
	 */
	public String getComClEntp() {
		return comClEntp;
	}

	/**
	 * @设置 comClEntp
	 * @param comClEntp
	 */
	public void setComClEntp(String comClEntp) {
		this.comClEntp = comClEntp;
	}

	/**
	 * @return 返回 comHdEnterprise
	 */
	public String getComHdEnterprise() {
		return comHdEnterprise;
	}

	/**
	 * @设置 comHdEnterprise
	 * @param comHdEnterprise
	 */
	public void setComHdEnterprise(String comHdEnterprise) {
		this.comHdEnterprise = comHdEnterprise;
	}


	/**
	 * @return 返回 comInitLoanDate
	 */
	public String getComInitLoanDate() {
		return comInitLoanDate;
	}

	/**
	 * @设置 comInitLoanDate
	 * @param comInitLoanDate
	 */
	public void setComInitLoanDate(String comInitLoanDate) {
		this.comInitLoanDate = comInitLoanDate;
	}

	/**
	 * @return 返回 comCityFlg
	 */
	public String getComCityFlg() {
		return comCityFlg;
	}

	/**
	 * @设置 comCityFlg
	 * @param comCityFlg
	 */
	public void setComCityFlg(String comCityFlg) {
		this.comCityFlg = comCityFlg;
	}

	/**
	 * @return 返回 comMngOrg
	 */
	public String getComMngOrg() {
		return comMngOrg;
	}

	/**
	 * @设置 comMngOrg
	 * @param comMngOrg
	 */
	public void setComMngOrg(String comMngOrg) {
		this.comMngOrg = comMngOrg;
	}

	/**
	 * @return 返回 acuAddr
	 */
	public String getAcuAddr() {
		return acuAddr;
	}

	/**
	 * @设置 acuAddr
	 * @param acuAddr
	 */
	public void setAcuAddr(String acuAddr) {
		this.acuAddr = acuAddr;
	}

	/**
	 * @return 返回 cusId
	 */
	public String getCusId() {
		return cusId;
	}

	/**
	 * @设置 cusId
	 * @param cusId
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}


	/**
	 * @return 返回 cusEnName
	 */
	public String getCusEnName() {
		return cusEnName;
	}

	/**
	 * @设置 cusEnName
	 * @param cusEnName
	 */
	public void setCusEnName(String cusEnName) {
		this.cusEnName = cusEnName;
	}


	/**
	 * @return 返回 cusBankRel
	 */
	public String getCusBankRel() {
		return cusBankRel;
	}

	/**
	 * @设置 cusBankRel
	 * @param cusBankRel
	 */
	public void setCusBankRel(String cusBankRel) {
		this.cusBankRel = cusBankRel;
	}

	/**
	 * @return 返回 investType
	 */
	public String getInvestType() {
		return investType;
	}

	/**
	 * @设置 investType
	 * @param investType
	 */
	public void setInvestType(String investType) {
		this.investType = investType;
	}

	/**
	 * @return 返回 comSubTyp
	 */
	public String getComSubTyp() {
		return comSubTyp;
	}

	/**
	 * @设置 comSubTyp
	 * @param comSubTyp
	 */
	public void setComSubTyp(String comSubTyp) {
		this.comSubTyp = comSubTyp;
	}

	/**
	 * @return 返回 comScale
	 */
	public String getComScale() {
		return comScale;
	}

	/**
	 * @设置 comScale
	 * @param comScale
	 */
	public void setComScale(String comScale) {
		this.comScale = comScale;
	}

	/**
	 * @return 返回 comHoldType
	 */
	public String getComHoldType() {
		return comHoldType;
	}

	/**
	 * @设置 comHoldType
	 * @param comHoldType
	 */
	public void setComHoldType(String comHoldType) {
		this.comHoldType = comHoldType;
	}


	/**
	 * @return 返回 comInsCode
	 */
	public String getComInsCode() {
		return comInsCode;
	}

	/**
	 * @设置 comInsCode
	 * @param comInsCode
	 */
	public void setComInsCode(String comInsCode) {
		this.comInsCode = comInsCode;
	}

	/**
	 * @return 返回 comCllType
	 */
	public String getComCllType() {
		return comCllType;
	}

	/**
	 * @设置 comCllType
	 * @param comCllType
	 */
	public void setComCllType(String comCllType) {
		this.comCllType = comCllType;
	}

	/**
	 * @return 返回 comCllName
	 */
	public String getComCllName() {
		return comCllName;
	}

	/**
	 * @设置 comCllName
	 * @param comCllName
	 */
	public void setComCllName(String comCllName) {
		this.comCllName = comCllName;
	}

	/**
	 * @return 返回 comStrDate
	 */
	public String getComStrDate() {
		return comStrDate;
	}

	/**
	 * @设置 comStrDate
	 * @param comStrDate
	 */
	public void setComStrDate(String comStrDate) {
		this.comStrDate = comStrDate;
	}

	/**
	 * @return 返回 comEmployee
	 */
	public double getComEmployee() {
		return comEmployee;
	}

	/**
	 * @设置 comEmployee
	 * @param comEmployee
	 */
	public void setComEmployee(double comEmployee) {
		this.comEmployee = comEmployee;
	}

	/**
	 * @return 返回 comInsRegDate
	 */
	public String getComInsRegDate() {
		return comInsRegDate;
	}

	/**
	 * @设置 comInsRegDate
	 * @param comInsRegDate
	 */
	public void setComInsRegDate(String comInsRegDate) {
		this.comInsRegDate = comInsRegDate;
	}

	/**
	 * @return 返回 comInsExpDate
	 */
	public String getComInsExpDate() {
		return comInsExpDate;
	}

	/**
	 * @设置 comInsExpDate
	 * @param comInsExpDate
	 */
	public void setComInsExpDate(String comInsExpDate) {
		this.comInsExpDate = comInsExpDate;
	}

	/**
	 * @return 返回 comInsAnnDate
	 */
	public String getComInsAnnDate() {
		return comInsAnnDate;
	}

	/**
	 * @设置 comInsAnnDate
	 * @param comInsAnnDate
	 */
	public void setComInsAnnDate(String comInsAnnDate) {
		this.comInsAnnDate = comInsAnnDate;
	}

	/**
	 * @return 返回 comInsOrg
	 */
	public String getComInsOrg() {
		return comInsOrg;
	}

	/**
	 * @设置 comInsOrg
	 * @param comInsOrg
	 */
	public void setComInsOrg(String comInsOrg) {
		this.comInsOrg = comInsOrg;
	}

	/**
	 * @return 返回 regCode
	 */
	public String getRegCode() {
		return regCode;
	}

	/**
	 * @设置 regCode
	 * @param regCode
	 */
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	/**
	 * @return 返回 regType
	 */
	public String getRegType() {
		return regType;
	}

	/**
	 * @设置 regType
	 * @param regType
	 */
	public void setRegType(String regType) {
		this.regType = regType;
	}

	/**
	 * @return 返回 adminOrg
	 */
	public String getAdminOrg() {
		return adminOrg;
	}

	/**
	 * @设置 adminOrg
	 * @param adminOrg
	 */
	public void setAdminOrg(String adminOrg) {
		this.adminOrg = adminOrg;
	}

	/**
	 * @return 返回 apprOrg
	 */
	public String getApprOrg() {
		return apprOrg;
	}

	/**
	 * @设置 apprOrg
	 * @param apprOrg
	 */
	public void setApprOrg(String apprOrg) {
		this.apprOrg = apprOrg;
	}

	/**
	 * @return 返回 apprDocNo
	 */
	public String getApprDocNo() {
		return apprDocNo;
	}

	/**
	 * @设置 apprDocNo
	 * @param apprDocNo
	 */
	public void setApprDocNo(String apprDocNo) {
		this.apprDocNo = apprDocNo;
	}

	/**
	 * @return 返回 regStateCode
	 */
	public String getRegStateCode() {
		return regStateCode;
	}

	/**
	 * @设置 regStateCode
	 * @param regStateCode
	 */
	public void setRegStateCode(String regStateCode) {
		this.regStateCode = regStateCode;
	}

	/**
	 * @return 返回 regAreaName
	 */
	public String getRegAreaName() {
		return regAreaName;
	}

	/**
	 * @设置 regAreaName
	 * @param regAreaName
	 */
	public void setRegAreaName(String regAreaName) {
		this.regAreaName = regAreaName;
	}

	/**
	 * @return 返回 regAddr
	 */
	public String getRegAddr() {
		return regAddr;
	}

	/**
	 * @设置 regAddr
	 * @param regAddr
	 */
	public void setRegAddr(String regAddr) {
		this.regAddr = regAddr;
	}

	/**
	 * @return 返回 enRegAddr
	 */
	public String getEnRegAddr() {
		return enRegAddr;
	}

	/**
	 * @设置 enRegAddr
	 * @param enRegAddr
	 */
	public void setEnRegAddr(String enRegAddr) {
		this.enRegAddr = enRegAddr;
	}

	/**
	 * @return 返回 regCurType
	 */
	public String getRegCurType() {
		return regCurType;
	}

	/**
	 * @设置 regCurType
	 * @param regCurType
	 */
	public void setRegCurType(String regCurType) {
		this.regCurType = regCurType;
	}

	/**
	 * @return 返回 regCapAmt
	 */
	public double getRegCapAmt() {
		return regCapAmt;
	}

	/**
	 * @设置 regCapAmt
	 * @param regCapAmt
	 */
	public void setRegCapAmt(double regCapAmt) {
		this.regCapAmt = regCapAmt;
	}

	/**
	 * @return 返回 paidCapCurType
	 */
	public String getPaidCapCurType() {
		return paidCapCurType;
	}

	/**
	 * @设置 paidCapCurType
	 * @param paidCapCurType
	 */
	public void setPaidCapCurType(String paidCapCurType) {
		this.paidCapCurType = paidCapCurType;
	}

	/**
	 * @return 返回 paidCapAmt
	 */
	public double getPaidCapAmt() {
		return paidCapAmt;
	}

	/**
	 * @设置 paidCapAmt
	 * @param paidCapAmt
	 */
	public void setPaidCapAmt(double paidCapAmt) {
		this.paidCapAmt = paidCapAmt;
	}

	/**
	 * @return 返回 comMainOptScp
	 */
	public String getComMainOptScp() {
		return comMainOptScp;
	}

	/**
	 * @设置 comMainOptScp
	 * @param comMainOptScp
	 */
	public void setComMainOptScp(String comMainOptScp) {
		this.comMainOptScp = comMainOptScp;
	}


	/**
	 * @return 返回 regStartDate
	 */
	public String getRegStartDate() {
		return regStartDate;
	}

	/**
	 * @设置 regStartDate
	 * @param regStartDate
	 */
	public void setRegStartDate(String regStartDate) {
		this.regStartDate = regStartDate;
	}

	/**
	 * @return 返回 regEndDate
	 */
	public String getRegEndDate() {
		return regEndDate;
	}

	/**
	 * @设置 regEndDate
	 * @param regEndDate
	 */
	public void setRegEndDate(String regEndDate) {
		this.regEndDate = regEndDate;
	}

	/**
	 * @return 返回 regAudit
	 */
	public String getRegAudit() {
		return regAudit;
	}

	/**
	 * @设置 regAudit
	 * @param regAudit
	 */
	public void setRegAudit(String regAudit) {
		this.regAudit = regAudit;
	}

	/**
	 * @return 返回 regAuditDate
	 */
	public String getRegAuditDate() {
		return regAuditDate;
	}

	/**
	 * @设置 regAuditDate
	 * @param regAuditDate
	 */
	public void setRegAuditDate(String regAuditDate) {
		this.regAuditDate = regAuditDate;
	}

	/**
	 * @return 返回 regAuditEndDate
	 */
	public String getRegAuditEndDate() {
		return regAuditEndDate;
	}

	/**
	 * @设置 regAuditEndDate
	 * @param regAuditEndDate
	 */
	public void setRegAuditEndDate(String regAuditEndDate) {
		this.regAuditEndDate = regAuditEndDate;
	}

	/**
	 * @return 返回 natTaxRegCode
	 */
	public String getNatTaxRegCode() {
		return natTaxRegCode;
	}

	/**
	 * @设置 natTaxRegCode
	 * @param natTaxRegCode
	 */
	public void setNatTaxRegCode(String natTaxRegCode) {
		this.natTaxRegCode = natTaxRegCode;
	}

	/**
	 * @return 返回 natTaxRegOrg
	 */
	public String getNatTaxRegOrg() {
		return natTaxRegOrg;
	}

	/**
	 * @设置 natTaxRegOrg
	 * @param natTaxRegOrg
	 */
	public void setNatTaxRegOrg(String natTaxRegOrg) {
		this.natTaxRegOrg = natTaxRegOrg;
	}

	/**
	 * @return 返回 natTaxRegDt
	 */
	public String getNatTaxRegDt() {
		return natTaxRegDt;
	}

	/**
	 * @设置 natTaxRegDt
	 * @param natTaxRegDt
	 */
	public void setNatTaxRegDt(String natTaxRegDt) {
		this.natTaxRegDt = natTaxRegDt;
	}

	/**
	 * @return 返回 natTaxRegEndDt
	 */
	public String getNatTaxRegEndDt() {
		return natTaxRegEndDt;
	}

	/**
	 * @设置 natTaxRegEndDt
	 * @param natTaxRegEndDt
	 */
	public void setNatTaxRegEndDt(String natTaxRegEndDt) {
		this.natTaxRegEndDt = natTaxRegEndDt;
	}

	/**
	 * @return 返回 natTaxAnnDate
	 */
	public String getNatTaxAnnDate() {
		return natTaxAnnDate;
	}

	/**
	 * @设置 natTaxAnnDate
	 * @param natTaxAnnDate
	 */
	public void setNatTaxAnnDate(String natTaxAnnDate) {
		this.natTaxAnnDate = natTaxAnnDate;
	}

	/**
	 * @return 返回 locTaxRegCode
	 */
	public String getLocTaxRegCode() {
		return locTaxRegCode;
	}

	/**
	 * @设置 locTaxRegCode
	 * @param locTaxRegCode
	 */
	public void setLocTaxRegCode(String locTaxRegCode) {
		this.locTaxRegCode = locTaxRegCode;
	}

	/**
	 * @return 返回 locTaxRegOrg
	 */
	public String getLocTaxRegOrg() {
		return locTaxRegOrg;
	}

	/**
	 * @设置 locTaxRegOrg
	 * @param locTaxRegOrg
	 */
	public void setLocTaxRegOrg(String locTaxRegOrg) {
		this.locTaxRegOrg = locTaxRegOrg;
	}

	/**
	 * @return 返回 locTaxRegDt
	 */
	public String getLocTaxRegDt() {
		return locTaxRegDt;
	}

	/**
	 * @设置 locTaxRegDt
	 * @param locTaxRegDt
	 */
	public void setLocTaxRegDt(String locTaxRegDt) {
		this.locTaxRegDt = locTaxRegDt;
	}

	/**
	 * @return 返回 locTaxRegEndDt
	 */
	public String getLocTaxRegEndDt() {
		return locTaxRegEndDt;
	}

	/**
	 * @设置 locTaxRegEndDt
	 * @param locTaxRegEndDt
	 */
	public void setLocTaxRegEndDt(String locTaxRegEndDt) {
		this.locTaxRegEndDt = locTaxRegEndDt;
	}

	/**
	 * @return 返回 locTaxAnnDate
	 */
	public String getLocTaxAnnDate() {
		return locTaxAnnDate;
	}

	/**
	 * @设置 locTaxAnnDate
	 * @param locTaxAnnDate
	 */
	public void setLocTaxAnnDate(String locTaxAnnDate) {
		this.locTaxAnnDate = locTaxAnnDate;
	}

	/**
	 * @return 返回 fnaMgr
	 */
	public String getFnaMgr() {
		return fnaMgr;
	}

	/**
	 * @设置 fnaMgr
	 * @param fnaMgr
	 */
	public void setFnaMgr(String fnaMgr) {
		this.fnaMgr = fnaMgr;
	}

	/**
	 * @return 返回 comOperator
	 */
	public String getComOperator() {
		return comOperator;
	}

	/**
	 * @设置 comOperator
	 * @param comOperator
	 */
	public void setComOperator(String comOperator) {
		this.comOperator = comOperator;
	}

	/**
	 * @return 返回 postAddr
	 */
	public String getPostAddr() {
		return postAddr;
	}

	/**
	 * @设置 postAddr
	 * @param postAddr
	 */
	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}

	/**
	 * @return 返回 postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @设置 postCode
	 * @param postCode
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}


	/**
	 * @return 返回 faxCode
	 */
	public String getFaxCode() {
		return faxCode;
	}

	/**
	 * @设置 faxCode
	 * @param faxCode
	 */
	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}

	/**
	 * @return 返回 email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @设置 email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return 返回 webUrl
	 */
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 * @设置 webUrl
	 * @param webUrl
	 */
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/**
	 * @return 返回 basAccFlg
	 */
	public String getBasAccFlg() {
		return basAccFlg;
	}

	/**
	 * @设置 basAccFlg
	 * @param basAccFlg
	 */
	public void setBasAccFlg(String basAccFlg) {
		this.basAccFlg = basAccFlg;
	}

	/**
	 * @return 返回 basAccLicence
	 */
	public String getBasAccLicence() {
		return basAccLicence;
	}

	/**
	 * @设置 basAccLicence
	 * @param basAccLicence
	 */
	public void setBasAccLicence(String basAccLicence) {
		this.basAccLicence = basAccLicence;
	}

	/**
	 * @return 返回 basAccBank
	 */
	public String getBasAccBank() {
		return basAccBank;
	}

	/**
	 * @设置 basAccBank
	 * @param basAccBank
	 */
	public void setBasAccBank(String basAccBank) {
		this.basAccBank = basAccBank;
	}

	/**
	 * @return 返回 basAccNo
	 */
	public String getBasAccNo() {
		return basAccNo;
	}

	/**
	 * @设置 basAccNo
	 * @param basAccNo
	 */
	public void setBasAccNo(String basAccNo) {
		this.basAccNo = basAccNo;
	}

	/**
	 * @return 返回 basAccDate
	 */
	public String getBasAccDate() {
		return basAccDate;
	}

	/**
	 * @设置 basAccDate
	 * @param basAccDate
	 */
	public void setBasAccDate(String basAccDate) {
		this.basAccDate = basAccDate;
	}


	/**
	 * @return 返回 comFinRepType
	 */
	public String getComFinRepType() {
		return comFinRepType;
	}

	/**
	 * @设置 comFinRepType
	 * @param comFinRepType
	 */
	public void setComFinRepType(String comFinRepType) {
		this.comFinRepType = comFinRepType;
	}

	/**
	 * @return 返回 comMrkFlg
	 */
	public String getComMrkFlg() {
		return comMrkFlg;
	}

	/**
	 * @设置 comMrkFlg
	 * @param comMrkFlg
	 */
	public void setComMrkFlg(String comMrkFlg) {
		this.comMrkFlg = comMrkFlg;
	}

	/**
	 * @return 返回 comMrkArea
	 */
	public String getComMrkArea() {
		return comMrkArea;
	}

	/**
	 * @设置 comMrkArea
	 * @param comMrkArea
	 */
	public void setComMrkArea(String comMrkArea) {
		this.comMrkArea = comMrkArea;
	}

	/**
	 * @return 返回 comStockId
	 */
	public String getComStockId() {
		return comStockId;
	}

	/**
	 * @设置 comStockId
	 * @param comStockId
	 */
	public void setComStockId(String comStockId) {
		this.comStockId = comStockId;
	}

	public String getFinaPerPhone() {
		return finaPerPhone;
	}

	public void setFinaPerPhone(String finaPerPhone) {
		this.finaPerPhone = finaPerPhone;
	}

	public String getCorpQlty() {
		return corpQlty;
	}

	public void setCorpQlty(String corpQlty) {
		this.corpQlty = corpQlty;
	}

	public String getEconDep() {
		return econDep;
	}

	public void setEconDep(String econDep) {
		this.econDep = econDep;
	}

	public String getRegAreaCode() {
		return regAreaCode;
	}

	public void setRegAreaCode(String regAreaCode) {
		this.regAreaCode = regAreaCode;
	}

	public String getIsOursRelaCust() {
		return isOursRelaCust;
	}

	public void setIsOursRelaCust(String isOursRelaCust) {
		this.isOursRelaCust = isOursRelaCust;
	}

	public String getBankDuty() {
		return bankDuty;
	}

	public void setBankDuty(String bankDuty) {
		this.bankDuty = bankDuty;
	}

	public String getEquiNo() {
		return equiNo;
	}

	public void setEquiNo(String equiNo) {
		this.equiNo = equiNo;
	}

	public String getFinaPerTel() {
		return finaPerTel;
	}

	public void setFinaPerTel(String finaPerTel) {
		this.finaPerTel = finaPerTel;
	}

	public String getStrCus() {
		return strCus;
	}

	public void setStrCus(String strCus) {
		this.strCus = strCus;
	}

	public String getHouExp() {
		return houExp;
	}

	public void setHouExp(String houExp) {
		this.houExp = houExp;
	}

	public String getStrCusEndDt() {
		return strCusEndDt;
	}

	public void setStrCusEndDt(String strCusEndDt) {
		this.strCusEndDt = strCusEndDt;
	}

	public String getRowLice() {
		return rowLice;
	}

	public void setRowLice(String rowLice) {
		this.rowLice = rowLice;
	}

	public String getCustMgrPhone() {
		return custMgrPhone;
	}

	public void setCustMgrPhone(String custMgrPhone) {
		this.custMgrPhone = custMgrPhone;
	}

	public String getRowLiceEndDt() {
		return rowLiceEndDt;
	}

	public void setRowLiceEndDt(String rowLiceEndDt) {
		this.rowLiceEndDt = rowLiceEndDt;
	}

	public String getComGuarCrdGrade2() {
		return comGuarCrdGrade2;
	}

	public void setComGuarCrdGrade2(String comGuarCrdGrade2) {
		this.comGuarCrdGrade2 = comGuarCrdGrade2;
	}

	public String getComCoopExp() {
		return comCoopExp;
	}

	public void setComCoopExp(String comCoopExp) {
		this.comCoopExp = comCoopExp;
	}

	public String getComGuarTyp() {
		return comGuarTyp;
	}

	public void setComGuarTyp(String comGuarTyp) {
		this.comGuarTyp = comGuarTyp;
	}

	public String getComGuarMulti() {
		return comGuarMulti;
	}

	public void setComGuarMulti(String comGuarMulti) {
		this.comGuarMulti = comGuarMulti;
	}

	public String getBankEquiAmt() {
		return bankEquiAmt;
	}

	public void setBankEquiAmt(String bankEquiAmt) {
		this.bankEquiAmt = bankEquiAmt;
	}

	public String getParentLoanCard() {
		return parentLoanCard;
	}

	public void setParentLoanCard(String parentLoanCard) {
		this.parentLoanCard = parentLoanCard;
	}

	public String getParentCertCode() {
		return parentCertCode;
	}

	public void setParentCertCode(String parentCertCode) {
		this.parentCertCode = parentCertCode;
	}

	public String getParentCusName() {
		return parentCusName;
	}

	public void setParentCusName(String parentCusName) {
		this.parentCusName = parentCusName;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	public String getGoverFinTer() {
		return goverFinTer;
	}

	public void setGoverFinTer(String goverFinTer) {
		this.goverFinTer = goverFinTer;
	}
	
	/**
	 * @return 返回 GuarCls
	 */
	public String getGuarCls() {
		return guarCls;
	}

	/**
	 * @设置 GuarCls
	 * @param GuarCls
	 */
	public void setGuarCls(String GuarCls) {
		this.guarCls = GuarCls;
	}
	
	/**
	 * @return 返回 GuarBailMultiple
	 */
	public String getGuarBailMultiple() {
		return guarBailMultiple;
	}

	/**
	 * @设置 GuarBailMultiple
	 * @param GuarBailMultiple
	 */
	public void setGuarBailMultiple(String GuarBailMultiple) {
		this.guarBailMultiple = GuarBailMultiple;
	}
}