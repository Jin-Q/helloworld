package com.yucheng.cmis.platform.permission.domain;
/**
 * Title: SRowright.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Wed Sep 30 21:33:38 CST 2009
 * @version��1.0
 */
public class SRowright  implements com.yucheng.cmis.pub.CMISDomain{
	private String pk1;
	private String resourceid;
	private String cnname;
	private String readtemp;
	private String writetemp;
	private String memo;
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
 	 * @return ���� pk1
 	 */
	public String getPk1(){
		return pk1;
	}
	/**
 	 * @���� pk1
 	 * @param pk1
 	 */
	public void setPk1(String pk1){
		this.pk1 = pk1;
	}
	/**
 	 * @return ���� resourceid
 	 */
	public String getResourceid(){
		return resourceid;
	}
	/**
 	 * @���� resourceid
 	 * @param resourceid
 	 */
	public void setResourceid(String resourceid){
		this.resourceid = resourceid;
	}
	/**
 	 * @return ���� cnname
 	 */
	public String getCnname(){
		return cnname;
	}
	/**
 	 * @���� cnname
 	 * @param cnname
 	 */
	public void setCnname(String cnname){
		this.cnname = cnname;
	}
	/**
 	 * @return ���� readtemp
 	 */
	public String getReadtemp(){
		return readtemp;
	}
	/**
 	 * @���� readtemp
 	 * @param readtemp
 	 */
	public void setReadtemp(String readtemp){
		this.readtemp = readtemp;
	}
	/**
 	 * @return ���� writetemp
 	 */
	public String getWritetemp(){
		return writetemp;
	}
	/**
 	 * @���� writetemp
 	 * @param writetemp
 	 */
	public void setWritetemp(String writetemp){
		this.writetemp = writetemp;
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
}