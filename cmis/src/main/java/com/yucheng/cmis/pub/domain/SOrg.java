package com.yucheng.cmis.pub.domain;
/**
 * Title: SOrg.java
 * Description: 
 * @author：wqgang
 * @create date：Sat May 09 16:00:02 CST 2009
 * @version：1.0
 */
public class SOrg  {
	private String organno;
	private String suporganno;
	private String locate;
	private String organname;
	private String organshortform;
	private String enname;
	private int orderno;
	private String distno;
	private String launchdate;
	private int organlevel;
	private String fincode;
	private int state;
	private String organchief;
	private String telnum;
	private String address;
	private String postcode;
	private String control;
	private String artiOrganno;
	private String distname;
	private String organchilds;
	
	
	
	
	/**
	 * @return the organchilds
	 */
	public String getOrganchilds() {
		return organchilds;
	}
	/**
	 * @param organchilds the organchilds to set
	 */
	public void setOrganchilds(String organchilds) {
		this.organchilds = organchilds;
	}
	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		SOrg result = new SOrg();
		result.setAddress(this.getAddress());
		result.setArtiOrganno(this.getArtiOrganno());
		result.setControl(this.getControl());
		result.setDistname(this.getDistname());
		result.setEnname(this.getEnname());
		result.setFincode(this.getFincode());
		result.setLaunchdate(this.getLaunchdate());
		result.setLocate(this.getLocate());
		result.setOrderno(this.getOrderno());
		result.setOrganchief(this.getOrganchief());
		result.setOrganlevel(this.getOrganlevel());
		result.setOrganname(this.getOrganname());
		result.setOrganno(this.getOrganno());
		result.setOrganshortform(this.getOrganshortform());
		result.setPostcode(this.getPostcode());
		result.setState(this.getState());
		result.setTelnum(this.getTelnum());
		
		return result;
	}
	/**
 	 * @return 返回 organno
 	 */
	public String getOrganno(){
		return organno;
	}
	/**
 	 * @设置 organno
 	 * @param organno
 	 */
	public void setOrganno(String organno){
		this.organno = organno;
	}
	/**
 	 * @return 返回 suporganno
 	 */
	public String getSuporganno(){
		return suporganno;
	}
	/**
 	 * @设置 suporganno
 	 * @param suporganno
 	 */
	public void setSuporganno(String suporganno){
		this.suporganno = suporganno;
	}
	/**
 	 * @return 返回 locate
 	 */
	public String getLocate(){
		return locate;
	}
	/**
 	 * @设置 locate
 	 * @param locate
 	 */
	public void setLocate(String locate){
		this.locate = locate;
	}
	/**
 	 * @return 返回 organname
 	 */
	public String getOrganname(){
		return organname;
	}
	/**
 	 * @设置 organname
 	 * @param organname
 	 */
	public void setOrganname(String organname){
		this.organname = organname;
	}
	/**
 	 * @return 返回 organshortform
 	 */
	public String getOrganshortform(){
		return organshortform;
	}
	/**
 	 * @设置 organshortform
 	 * @param organshortform
 	 */
	public void setOrganshortform(String organshortform){
		this.organshortform = organshortform;
	}
	/**
 	 * @return 返回 enname
 	 */
	public String getEnname(){
		return enname;
	}
	/**
 	 * @设置 enname
 	 * @param enname
 	 */
	public void setEnname(String enname){
		this.enname = enname;
	}
	/**
 	 * @return 返回 orderno
 	 */
	public int getOrderno(){
		return orderno;
	}
	/**
 	 * @设置 orderno
 	 * @param orderno
 	 */
	public void setOrderno(int orderno){
		this.orderno = orderno;
	}
	/**
 	 * @return 返回 distno
 	 */
	public String getDistno(){
		return distno;
	}
	/**
 	 * @设置 distno
 	 * @param distno
 	 */
	public void setDistno(String distno){
		this.distno = distno;
	}
	/**
 	 * @return 返回 launchdate
 	 */
	public String getLaunchdate(){
		return launchdate;
	}
	/**
 	 * @设置 launchdate
 	 * @param launchdate
 	 */
	public void setLaunchdate(String launchdate){
		this.launchdate = launchdate;
	}
	/**
 	 * @return 返回 organlevel
 	 */
	public int getOrganlevel(){
		return organlevel;
	}
	/**
 	 * @设置 organlevel
 	 * @param organlevel
 	 */
	public void setOrganlevel(int organlevel){
		this.organlevel = organlevel;
	}
	/**
 	 * @return 返回 fincode
 	 */
	public String getFincode(){
		return fincode;
	}
	/**
 	 * @设置 fincode
 	 * @param fincode
 	 */
	public void setFincode(String fincode){
		this.fincode = fincode;
	}
	/**
 	 * @return 返回 state
 	 */
	public int getState(){
		return state;
	}
	/**
 	 * @设置 state
 	 * @param state
 	 */
	public void setState(int state){
		this.state = state;
	}
	/**
 	 * @return 返回 organchief
 	 */
	public String getOrganchief(){
		return organchief;
	}
	/**
 	 * @设置 organchief
 	 * @param organchief
 	 */
	public void setOrganchief(String organchief){
		this.organchief = organchief;
	}
	/**
 	 * @return 返回 telnum
 	 */
	public String getTelnum(){
		return telnum;
	}
	/**
 	 * @设置 telnum
 	 * @param telnum
 	 */
	public void setTelnum(String telnum){
		this.telnum = telnum;
	}
	/**
 	 * @return 返回 address
 	 */
	public String getAddress(){
		return address;
	}
	/**
 	 * @设置 address
 	 * @param address
 	 */
	public void setAddress(String address){
		this.address = address;
	}
	/**
 	 * @return 返回 postcode
 	 */
	public String getPostcode(){
		return postcode;
	}
	/**
 	 * @设置 postcode
 	 * @param postcode
 	 */
	public void setPostcode(String postcode){
		this.postcode = postcode;
	}
	/**
 	 * @return 返回 control
 	 */
	public String getControl(){
		return control;
	}
	/**
 	 * @设置 control
 	 * @param control
 	 */
	public void setControl(String control){
		this.control = control;
	}
	/**
 	 * @return 返回 artiOrganno
 	 */
	public String getArtiOrganno(){
		return artiOrganno;
	}
	/**
 	 * @设置 artiOrganno
 	 * @param artiOrganno
 	 */
	public void setArtiOrganno(String artiOrganno){
		this.artiOrganno = artiOrganno;
	}
	/**
 	 * @return 返回 distname
 	 */
	public String getDistname(){
		return distname;
	}
	/**
 	 * @设置 distname
 	 * @param distname
 	 */
	public void setDistname(String distname){
		this.distname = distname;
	}
}