package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusObisAssure.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Thu Mar 12 08:54:33 CST 2009
 * @version��1.0
 */
public class CusObisAssure  implements com.yucheng.cmis.pub.CMISDomain{
	private double seq;
	private String cusId;
	private String gtyTyp;
	private double gtyAmt;
	private double gtyBlc;
	private String gtyStrDt;
	private String gtyEndDt;
	private String gtyBusBch;
	private String gtyBusBchDec;
	private String validFlg;
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
 	 * @return ���� gtyTyp
 	 */
	public String getGtyTyp(){
		return gtyTyp;
	}
	/**
 	 * @���� gtyTyp
 	 * @param gtyTyp
 	 */
	public void setGtyTyp(String gtyTyp){
		this.gtyTyp = gtyTyp;
	}
	/**
 	 * @return ���� gtyAmt
 	 */
	public double getGtyAmt(){
		return gtyAmt;
	}
	/**
 	 * @���� gtyAmt
 	 * @param gtyAmt
 	 */
	public void setGtyAmt(double gtyAmt){
		this.gtyAmt = gtyAmt;
	}
	/**
 	 * @return ���� gtyBlc
 	 */
	public double getGtyBlc(){
		return gtyBlc;
	}
	/**
 	 * @���� gtyBlc
 	 * @param gtyBlc
 	 */
	public void setGtyBlc(double gtyBlc){
		this.gtyBlc = gtyBlc;
	}
	/**
 	 * @return ���� gtyStrDt
 	 */
	public String getGtyStrDt(){
		return gtyStrDt;
	}
	/**
 	 * @���� gtyStrDt
 	 * @param gtyStrDt
 	 */
	public void setGtyStrDt(String gtyStrDt){
		this.gtyStrDt = gtyStrDt;
	}
	/**
 	 * @return ���� gtyEndDt
 	 */
	public String getGtyEndDt(){
		return gtyEndDt;
	}
	/**
 	 * @���� gtyEndDt
 	 * @param gtyEndDt
 	 */
	public void setGtyEndDt(String gtyEndDt){
		this.gtyEndDt = gtyEndDt;
	}
	/**
 	 * @return ���� gtyBusBch
 	 */
	public String getGtyBusBch(){
		return gtyBusBch;
	}
	/**
 	 * @���� gtyBusBch
 	 * @param gtyBusBch
 	 */
	public void setGtyBusBch(String gtyBusBch){
		this.gtyBusBch = gtyBusBch;
	}
	/**
 	 * @return ���� gtyBusBchDec
 	 */
	public String getGtyBusBchDec(){
		return gtyBusBchDec;
	}
	/**
 	 * @���� gtyBusBchDec
 	 * @param gtyBusBchDec
 	 */
	public void setGtyBusBchDec(String gtyBusBchDec){
		this.gtyBusBchDec = gtyBusBchDec;
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
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 
}