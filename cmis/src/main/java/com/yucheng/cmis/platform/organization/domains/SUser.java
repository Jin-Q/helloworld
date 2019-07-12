package com.yucheng.cmis.platform.organization.domains;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: SUser.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Mon Jun 08 20:01:29 CST 2009
 * @version：1.0
 */
public class SUser implements CMISDomain{
	private String actorno;
	private String actorname;
	private String nickname;
	private String state;
	private String password;
	private String startdate;
	private String passwvalda;
	private String firedate;
	private String birthday;
	private String telnum;
	private String idcardno;
	private String allowopersys;
	private String lastlogdat;
	private String creater;
	private String creattime;
	private String usermail;
	private double wrongpinnum;
	private String isadmin;
	private String memo;
	private String ipmask;
	private double orderno;
	private String question;
	private String answer;
	private String orgid;
	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();

		return result;
	}
	/**
 	 * @return 返回 actorno
 	 */
	public String getActorno(){
		return actorno;
	}
	/**
 	 * @设置 actorno
 	 * @param actorno
 	 */
	public void setActorno(String actorno){
		this.actorno = actorno;
	}
	/**
 	 * @return 返回 actorname
 	 */
	public String getActorname(){
		return actorname;
	}
	/**
 	 * @设置 actorname
 	 * @param actorname
 	 */
	public void setActorname(String actorname){
		this.actorname = actorname;
	}
	/**
 	 * @return 返回 nickname
 	 */
	public String getNickname(){
		return nickname;
	}
	/**
 	 * @设置 nickname
 	 * @param nickname
 	 */
	public void setNickname(String nickname){
		this.nickname = nickname;
	}
	/**
 	 * @return 返回 state
 	 */
	public String getState(){
		return state;
	}
	/**
 	 * @设置 state
 	 * @param state
 	 */
	public void setState(String state){
		this.state = state;
	}
	/**
 	 * @return 返回 password
 	 */
	public String getPassword(){
		return password;
	}
	/**
 	 * @设置 password
 	 * @param password
 	 */
	public void setPassword(String password){
		this.password = password;
	}
	/**
 	 * @return 返回 startdate
 	 */
	public String getStartdate(){
		return startdate;
	}
	/**
 	 * @设置 startdate
 	 * @param startdate
 	 */
	public void setStartdate(String startdate){
		this.startdate = startdate;
	}
	/**
 	 * @return 返回 passwvalda
 	 */
	public String getPasswvalda(){
		return passwvalda;
	}
	/**
 	 * @设置 passwvalda
 	 * @param passwvalda
 	 */
	public void setPasswvalda(String passwvalda){
		this.passwvalda = passwvalda;
	}
	/**
 	 * @return 返回 firedate
 	 */
	public String getFiredate(){
		return firedate;
	}
	/**
 	 * @设置 firedate
 	 * @param firedate
 	 */
	public void setFiredate(String firedate){
		this.firedate = firedate;
	}
	/**
 	 * @return 返回 birthday
 	 */
	public String getBirthday(){
		return birthday;
	}
	/**
 	 * @设置 birthday
 	 * @param birthday
 	 */
	public void setBirthday(String birthday){
		this.birthday = birthday;
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
 	 * @return 返回 idcardno
 	 */
	public String getIdcardno(){
		return idcardno;
	}
	/**
 	 * @设置 idcardno
 	 * @param idcardno
 	 */
	public void setIdcardno(String idcardno){
		this.idcardno = idcardno;
	}
	/**
 	 * @return 返回 allowopersys
 	 */
	public String getAllowopersys(){
		return allowopersys;
	}
	/**
 	 * @设置 allowopersys
 	 * @param allowopersys
 	 */
	public void setAllowopersys(String allowopersys){
		this.allowopersys = allowopersys;
	}
	/**
 	 * @return 返回 lastlogdat
 	 */
	public String getLastlogdat(){
		return lastlogdat;
	}
	/**
 	 * @设置 lastlogdat
 	 * @param lastlogdat
 	 */
	public void setLastlogdat(String lastlogdat){
		this.lastlogdat = lastlogdat;
	}
	/**
 	 * @return 返回 creater
 	 */
	public String getCreater(){
		return creater;
	}
	/**
 	 * @设置 creater
 	 * @param creater
 	 */
	public void setCreater(String creater){
		this.creater = creater;
	}
	/**
 	 * @return 返回 creattime
 	 */
	public String getCreattime(){
		return creattime;
	}
	/**
 	 * @设置 creattime
 	 * @param creattime
 	 */
	public void setCreattime(String creattime){
		this.creattime = creattime;
	}
	/**
 	 * @return 返回 usermail
 	 */
	public String getUsermail(){
		return usermail;
	}
	/**
 	 * @设置 usermail
 	 * @param usermail
 	 */
	public void setUsermail(String usermail){
		this.usermail = usermail;
	}
	/**
 	 * @return 返回 wrongpinnum
 	 */
	public double getWrongpinnum(){
		return wrongpinnum;
	}
	/**
 	 * @设置 wrongpinnum
 	 * @param wrongpinnum
 	 */
	public void setWrongpinnum(double wrongpinnum){
		this.wrongpinnum = wrongpinnum;
	}
	/**
 	 * @return 返回 isadmin
 	 */
	public String getIsadmin(){
		return isadmin;
	}
	/**
 	 * @设置 isadmin
 	 * @param isadmin
 	 */
	public void setIsadmin(String isadmin){
		this.isadmin = isadmin;
	}
	/**
 	 * @return 返回 memo
 	 */
	public String getMemo(){
		return memo;
	}
	/**
 	 * @设置 memo
 	 * @param memo
 	 */
	public void setMemo(String memo){
		this.memo = memo;
	}
	/**
 	 * @return 返回 ipmask
 	 */
	public String getIpmask(){
		return ipmask;
	}
	/**
 	 * @设置 ipmask
 	 * @param ipmask
 	 */
	public void setIpmask(String ipmask){
		this.ipmask = ipmask;
	}
	/**
 	 * @return 返回 orderno
 	 */
	public double getOrderno(){
		return orderno;
	}
	/**
 	 * @设置 orderno
 	 * @param orderno
 	 */
	public void setOrderno(double orderno){
		this.orderno = orderno;
	}
	/**
 	 * @return 返回 question
 	 */
	public String getQuestion(){
		return question;
	}
	/**
 	 * @设置 question
 	 * @param question
 	 */
	public void setQuestion(String question){
		this.question = question;
	}
	/**
 	 * @return 返回 answer
 	 */
	public String getAnswer(){
		return answer;
	}
	/**
 	 * @设置 answer
 	 * @param answer
 	 */
	public void setAnswer(String answer){
		this.answer = answer;
	}
	/**
 	 * @return 返回 orgid
 	 */
	public String getOrgid(){
		return orgid;
	}
	/**
 	 * @设置 orgid
 	 * @param orgid
 	 */
	public void setOrgid(String orgid){
		this.orgid = orgid;
	}
}