package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusObisLoan.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Thu Mar 12 08:54:33 CST 2009
 * @version��1.0
 */
public class CusObisLoan  implements com.yucheng.cmis.pub.CMISDomain{
	private double seq;
	private String cusId;
	private String cusTyp;
	private String loanTyp;
	private String orgName;
	private String contNo;
	private String loanNo;
	private String contCurTyp;
	private double contAmt;
	private double loanBlc;
	private double interestBlc1;
	private double interestBlc2;
	private double gtyPerc;
	private String gtyMainTyp;
	private String loanStrDt;
	private String loanEndDt;
	private double extendTm;
	private double refinanceTm;
	private String loanForm4;
	private String loanForm5;
	private String lawSuitFlg;
	private String validFlg;
	private String remarks;
	private String cusBchId;
	private double contRate;
	/**
 	 * @return ���� seq
 	 */
	public double getSeq(){
		return seq;
	}
	/**
 	 * @���� seq
 	 * @param seq
 	 */
	public void setSeq(double seq){
		this.seq = seq;
	}
	/**
 	 * @return ���� cusId
 	 */
	public String getCusId(){
		return cusId;
	}
	/**
 	 * @���� cusId
 	 * @param cusId
 	 */
	public void setCusId(String cusId){
		this.cusId = cusId;
	}
	/**
 	 * @return ���� cusTyp
 	 */
	public String getCusTyp(){
		return cusTyp;
	}
	/**
 	 * @���� cusTyp
 	 * @param cusTyp
 	 */
	public void setCusTyp(String cusTyp){
		this.cusTyp = cusTyp;
	}
	/**
 	 * @return ���� loanTyp
 	 */
	public String getLoanTyp(){
		return loanTyp;
	}
	/**
 	 * @���� loanTyp
 	 * @param loanTyp
 	 */
	public void setLoanTyp(String loanTyp){
		this.loanTyp = loanTyp;
	}
	/**
 	 * @return ���� orgName
 	 */
	public String getOrgName(){
		return orgName;
	}
	/**
 	 * @���� orgName
 	 * @param orgName
 	 */
	public void setOrgName(String orgName){
		this.orgName = orgName;
	}
	/**
 	 * @return ���� contNo
 	 */
	public String getContNo(){
		return contNo;
	}
	/**
 	 * @���� contNo
 	 * @param contNo
 	 */
	public void setContNo(String contNo){
		this.contNo = contNo;
	}
	/**
 	 * @return ���� loanNo
 	 */
	public String getLoanNo(){
		return loanNo;
	}
	/**
 	 * @���� loanNo
 	 * @param loanNo
 	 */
	public void setLoanNo(String loanNo){
		this.loanNo = loanNo;
	}
	/**
 	 * @return ���� contCurTyp
 	 */
	public String getContCurTyp(){
		return contCurTyp;
	}
	/**
 	 * @���� contCurTyp
 	 * @param contCurTyp
 	 */
	public void setContCurTyp(String contCurTyp){
		this.contCurTyp = contCurTyp;
	}
	/**
 	 * @return ���� contAmt
 	 */
	public double getContAmt(){
		return contAmt;
	}
	/**
 	 * @���� contAmt
 	 * @param contAmt
 	 */
	public void setContAmt(double contAmt){
		this.contAmt = contAmt;
	}
	/**
 	 * @return ���� loanBlc
 	 */
	public double getLoanBlc(){
		return loanBlc;
	}
	/**
 	 * @���� loanBlc
 	 * @param loanBlc
 	 */
	public void setLoanBlc(double loanBlc){
		this.loanBlc = loanBlc;
	}
	/**
 	 * @return ���� interestBlc1
 	 */
	public double getInterestBlc1(){
		return interestBlc1;
	}
	/**
 	 * @���� interestBlc1
 	 * @param interestBlc1
 	 */
	public void setInterestBlc1(double interestBlc1){
		this.interestBlc1 = interestBlc1;
	}
	/**
 	 * @return ���� interestBlc2
 	 */
	public double getInterestBlc2(){
		return interestBlc2;
	}
	/**
 	 * @���� interestBlc2
 	 * @param interestBlc2
 	 */
	public void setInterestBlc2(double interestBlc2){
		this.interestBlc2 = interestBlc2;
	}
	/**
 	 * @return ���� gtyPerc
 	 */
	public double getGtyPerc(){
		return gtyPerc;
	}
	/**
 	 * @���� gtyPerc
 	 * @param gtyPerc
 	 */
	public void setGtyPerc(double gtyPerc){
		this.gtyPerc = gtyPerc;
	}
	/**
 	 * @return ���� gtyMainTyp
 	 */
	public String getGtyMainTyp(){
		return gtyMainTyp;
	}
	/**
 	 * @���� gtyMainTyp
 	 * @param gtyMainTyp
 	 */
	public void setGtyMainTyp(String gtyMainTyp){
		this.gtyMainTyp = gtyMainTyp;
	}
	/**
 	 * @return ���� loanStrDt
 	 */
	public String getLoanStrDt(){
		return loanStrDt;
	}
	/**
 	 * @���� loanStrDt
 	 * @param loanStrDt
 	 */
	public void setLoanStrDt(String loanStrDt){
		this.loanStrDt = loanStrDt;
	}
	/**
 	 * @return ���� loanEndDt
 	 */
	public String getLoanEndDt(){
		return loanEndDt;
	}
	/**
 	 * @���� loanEndDt
 	 * @param loanEndDt
 	 */
	public void setLoanEndDt(String loanEndDt){
		this.loanEndDt = loanEndDt;
	}
	/**
 	 * @return ���� extendTm
 	 */
	public double getExtendTm(){
		return extendTm;
	}
	/**
 	 * @���� extendTm
 	 * @param extendTm
 	 */
	public void setExtendTm(double extendTm){
		this.extendTm = extendTm;
	}
	/**
 	 * @return ���� refinanceTm
 	 */
	public double getRefinanceTm(){
		return refinanceTm;
	}
	/**
 	 * @���� refinanceTm
 	 * @param refinanceTm
 	 */
	public void setRefinanceTm(double refinanceTm){
		this.refinanceTm = refinanceTm;
	}
	/**
 	 * @return ���� loanForm4
 	 */
	public String getLoanForm4(){
		return loanForm4;
	}
	/**
 	 * @���� loanForm4
 	 * @param loanForm4
 	 */
	public void setLoanForm4(String loanForm4){
		this.loanForm4 = loanForm4;
	}
	/**
 	 * @return ���� loanForm5
 	 */
	public String getLoanForm5(){
		return loanForm5;
	}
	/**
 	 * @���� loanForm5
 	 * @param loanForm5
 	 */
	public void setLoanForm5(String loanForm5){
		this.loanForm5 = loanForm5;
	}
	/**
 	 * @return ���� lawSuitFlg
 	 */
	public String getLawSuitFlg(){
		return lawSuitFlg;
	}
	/**
 	 * @���� lawSuitFlg
 	 * @param lawSuitFlg
 	 */
	public void setLawSuitFlg(String lawSuitFlg){
		this.lawSuitFlg = lawSuitFlg;
	}
	/**
 	 * @return ���� validFlg
 	 */
	public String getValidFlg(){
		return validFlg;
	}
	/**
 	 * @���� validFlg
 	 * @param validFlg
 	 */
	public void setValidFlg(String validFlg){
		this.validFlg = validFlg;
	}
	/**
 	 * @return ���� remarks
 	 */
	public String getRemarks(){
		return remarks;
	}
	/**
 	 * @���� remarks
 	 * @param remarks
 	 */
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	/**
 	 * @return ���� cusBchId
 	 */
	public String getCusBchId(){
		return cusBchId;
	}
	/**
 	 * @���� cusBchId
 	 * @param cusBchId
 	 */
	public void setCusBchId(String cusBchId){
		this.cusBchId = cusBchId;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		}
	public double getContRate() {
		return contRate;
	}
	public void setContRate(double contRate) {
		this.contRate = contRate;
	} 
}