package com.yucheng.cmis.platform.organization.domains;
/**
 * Title: SDuty.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Sun Oct 18 22:11:29 CST 2009
 * @version��1.0
 */
public class SDuty  implements com.yucheng.cmis.pub.CMISDomain{
	private String dutyno;
	private String dutyname;
	private String organno;
	private String depno;
	private String orderno;
	private String type;
	private String memo;
	private String orgid;
	/**
	 * @���ø���clone����
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: ����clone���
		return result;
	}
	/**
 	 * @return ���� dutyno
 	 */
	public String getDutyno(){
		return dutyno;
	}
	/**
 	 * @���� dutyno
 	 * @param dutyno
 	 */
	public void setDutyno(String dutyno){
		this.dutyno = dutyno;
	}
	/**
 	 * @return ���� dutyname
 	 */
	public String getDutyname(){
		return dutyname;
	}
	/**
 	 * @���� dutyname
 	 * @param dutyname
 	 */
	public void setDutyname(String dutyname){
		this.dutyname = dutyname;
	}
	/**
 	 * @return ���� organno
 	 */
	public String getOrganno(){
		return organno;
	}
	/**
 	 * @���� organno
 	 * @param organno
 	 */
	public void setOrganno(String organno){
		this.organno = organno;
	}
	/**
 	 * @return ���� depno
 	 */
	public String getDepno(){
		return depno;
	}
	/**
 	 * @���� depno
 	 * @param depno
 	 */
	public void setDepno(String depno){
		this.depno = depno;
	}
	/**
 	 * @return ���� orderno
 	 */
	public String getOrderno(){
		return orderno;
	}
	/**
 	 * @���� orderno
 	 * @param orderno
 	 */
	public void setOrderno(String orderno){
		this.orderno = orderno;
	}
	/**
 	 * @return ���� type
 	 */
	public String getType(){
		return type;
	}
	/**
 	 * @���� type
 	 * @param type
 	 */
	public void setType(String type){
		this.type = type;
	}
	/**
 	 * @return ���� memo
 	 */
	public String getMemo(){
		return memo;
	}
	/**
 	 * @���� memo
 	 * @param memo
 	 */
	public void setMemo(String memo){
		this.memo = memo;
	}
	/**
 	 * @return ���� orgid
 	 */
	public String getOrgid(){
		return orgid;
	}
	/**
 	 * @���� orgid
 	 * @param orgid
 	 */
	public void setOrgid(String orgid){
		this.orgid = orgid;
	}
}