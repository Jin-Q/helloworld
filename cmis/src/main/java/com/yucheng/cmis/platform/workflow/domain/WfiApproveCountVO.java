package com.yucheng.cmis.platform.workflow.domain;

/**
 * 审批计数对象
 * @author 
 *
 */
public class WfiApproveCountVO implements com.yucheng.cmis.pub.CMISDomain {

	/**
	 * 申请类型
	 */
	private String applType;
	
	/**
	 * 节点号
	 */
	private String nodeid;
	
	/**
	 * 机构码
	 */
	private String orgid;
	
	/**
	 * 用户码
	 */
	private String actorno;
	
	/**
	 * 审批日期
	 */
	private String approveDate;
	
	/**
	 * 审批数量
	 */
	private int approveQnt;
	
	
	/**
	 * 生效标志
	 */
	private String status;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplType() {
		return applType;
	}

	public void setApplType(String applType) {
		this.applType = applType;
	}


	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getActorno() {
		return actorno;
	}

	public void setActorno(String actorno) {
		this.actorno = actorno;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public int getApproveQnt() {
		return approveQnt;
	}

	public void setApproveQnt(int approveQnt) {
		this.approveQnt = approveQnt;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
	}
	
}
