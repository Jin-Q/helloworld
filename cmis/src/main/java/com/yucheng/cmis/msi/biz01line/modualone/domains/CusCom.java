package com.yucheng.cmis.msi.biz01line.modualone.domains;

/**
 * <p>
 * 	客户信息Domain
 * 	<ul>描述：
 * 			modualone模块接口需要使用的domain
 * 	</ul>
 * </p>
 * @author yuhq
 * @version 3.0
 * @since 3.0
 */
public class CusCom {

	private String cusId = null;
	private String cusName = null;
	private String certType = null;
	private String certCode = null;
	private int age = 0;
	private String sex = null;
	
	/**
	 * 获取客户ID
	 * @return 客户ID
	 */
	public String getCusId() {
		return cusId;
	}
	
	/**
	 * 设置客户ID
	 * @param cusId　客户ID
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	/**
	 * 获取客户名称
	 * @return　客户名称
	 */
	public String getCusName() {
		return cusName;
	}
	
	/**
	 * 设置客户名称
	 * @param cusName 客户名称
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
	/**
	 * 获取证件类型
	 * @return 客户名称
	 */
	public String getCertType() {
		return certType;
	}
	
	/**
	 * 设置客户名称
	 * @param certType 客户名称
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	
	/**
	 * 获取证件号码
	 * @return　证件号码
	 */
	public String getCertCode() {
		return certCode;
	}
	
	/**
	 * 设置证件号码
	 * @param certCode　证件号码
	 */
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	
	/**
	 * 获取年龄
	 * @return　年龄
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * 设置年龄
	 * @param age 年龄
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * 获取性别　
	 * @return　性别　
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * 设置性别　
	 * @param sex 性别　
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
