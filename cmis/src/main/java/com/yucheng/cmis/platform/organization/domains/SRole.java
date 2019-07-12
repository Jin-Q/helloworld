package com.yucheng.cmis.platform.organization.domains;

/**
 * <p>
 * 	角色DOMAIN
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
public class SRole {
	private String roleno = null;//角色码
	private String rolename = null;//角色名称
	private String orderno = null;//排序字段
	private String type = null;//类型
	private String memo = null;//备注
	private String orgid = null;//机构号
	
	/**
	 * 角色码
	 * @return 角色码
	 */
	public String getRoleno() {
		return roleno;
	}
	/**
	 * 角色码
	 * @param roleNo 角色码
	 */
	public void setRoleno(String roleno) {
		this.roleno = roleno;
	}
	
	/**
	 * 角色名称
	 * @return 角色名称
	 */
	public String getRolename() {
		return rolename;
	}
	
	/**
	 * 角色名称
	 * @param roleName 角色名称
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	/**
	 * 排序字段
	 * @return 排序字段
	 */
	public String getOrderno() {
		return orderno;
	}
	
	/**
	 * 排序字段
	 * @param orderNo 排序字段
	 */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	/**
	 * 类型
	 * @return 类型
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 类型
	 * @param type 类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 备注
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}
	
	/**
	 * 备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	/**
	 * 机构号
	 * @return 机构号
	 */
	public String getOrgid() {
		return orgid;
	}
	
	/**
	 * 机构号
	 * @param orgId 机构号
	 */
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	
	

}
