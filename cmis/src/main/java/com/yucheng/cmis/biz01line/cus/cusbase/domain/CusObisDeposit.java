package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusObisDeposit.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Thu Mar 12 08:54:33 CST 2009
 * @version��1.0
 */
public class CusObisDeposit  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private double seq;
	private String cusTyp;
	private String cusBchId;
	private String orgName;
	private String depTyp;
	private double depPer;
	private String accNo;
	private String accCurTyp;
	private String accTyp;
	private String accCls;
	private String accSt;
	private double accBlc;
	private String crtUsrId;
	private String crtDt;
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
 	 * @return ���� depTyp
 	 */
	public String getDepTyp(){
		return depTyp;
	}
	/**
 	 * @���� depTyp
 	 * @param depTyp
 	 */
	public void setDepTyp(String depTyp){
		this.depTyp = depTyp;
	}
	/**
 	 * @return ���� depPer
 	 */
	public double getDepPer(){
		return depPer;
	}
	/**
 	 * @���� depPer
 	 * @param depPer
 	 */
	public void setDepPer(double depPer){
		this.depPer = depPer;
	}
	/**
 	 * @return ���� accNo
 	 */
	public String getAccNo(){
		return accNo;
	}
	/**
 	 * @���� accNo
 	 * @param accNo
 	 */
	public void setAccNo(String accNo){
		this.accNo = accNo;
	}
	/**
 	 * @return ���� accCurTyp
 	 */
	public String getAccCurTyp(){
		return accCurTyp;
	}
	/**
 	 * @���� accCurTyp
 	 * @param accCurTyp
 	 */
	public void setAccCurTyp(String accCurTyp){
		this.accCurTyp = accCurTyp;
	}
	/**
 	 * @return ���� accTyp
 	 */
	public String getAccTyp(){
		return accTyp;
	}
	/**
 	 * @���� accTyp
 	 * @param accTyp
 	 */
	public void setAccTyp(String accTyp){
		this.accTyp = accTyp;
	}
	/**
 	 * @return ���� accCls
 	 */
	public String getAccCls(){
		return accCls;
	}
	/**
 	 * @���� accCls
 	 * @param accCls
 	 */
	public void setAccCls(String accCls){
		this.accCls = accCls;
	}
	/**
 	 * @return ���� accSt
 	 */
	public String getAccSt(){
		return accSt;
	}
	/**
 	 * @���� accSt
 	 * @param accSt
 	 */
	public void setAccSt(String accSt){
		this.accSt = accSt;
	}
	/**
 	 * @return ���� accBlc
 	 */
	public double getAccBlc(){
		return accBlc;
	}
	/**
 	 * @���� accBlc
 	 * @param accBlc
 	 */
	public void setAccBlc(double accBlc){
		this.accBlc = accBlc;
	}
	/**
 	 * @return ���� crtUsrId
 	 */
	public String getCrtUsrId(){
		return crtUsrId;
	}
	/**
 	 * @���� crtUsrId
 	 * @param crtUsrId
 	 */
	public void setCrtUsrId(String crtUsrId){
		this.crtUsrId = crtUsrId;
	}
	/**
 	 * @return ���� crtDt
 	 */
	public String getCrtDt(){
		return crtDt;
	}
	/**
 	 * @���� crtDt
 	 * @param crtDt
 	 */
	public void setCrtDt(String crtDt){
		this.crtDt = crtDt;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 
}