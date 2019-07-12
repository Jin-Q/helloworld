package com.yucheng.cmis.platform.workflow.domain;

/**
 * 信贷流程基础值对象
 * <p>
 * 主要用于工作流引擎接入服务接口传值
 * 
 * @author liuhw 2013-6-14
 */
public class WFIVO {

	/** 标识表示操作结果为成功*/
	public static final int SIGN_SUCCESS = 0;// 成功
	/** 标识表示操作结果为失败*/
	public static final int SIGN_FAIL = 1;// 失败
	/** 标识表示操作结果为成功但有异常*/
	public static final int SIGN_SUCCESSWITHEXCEPTION = 2;// 成功但有异常
	/** 标识表示操作结果为部分成功*/
	public static final int SIGN_PARTSUCCESS = 3;// 部分成功
	/** 标识表示操作结果为未知*/
	public static final int SIGN_OTHER = 4;// 未知
	/** 标识表示操作结果为结束*/
	public static final int SIGN_END = 5;// 结束
	
	/**
	 * 标识
	 */
	private int sign = 4;
	
	/**
	 * 信息
	 */
	private String message = null;//信息
	
	/**
	 * 流程实例号
	 */
	private String instanceId = null;//流程实例号
	
	/**
	 * 流程标识
	 */
	private String wfSign = null; //流程标识
	
	/**
	 * 当前办理人
	 */
	private String currentUserId = null;//当前办理人
	
	/**
	 * 当前节点ID
	 */
	private String currentNodeId = null;//当前节点ID
	/**
	 * 当前节点名称
	 */
	private String currentNodeName = null;//当前节点名称
	
	/**
	 * 下一节点Id
	 */
	private String nextNodeId = null;//下一节点Id
	
	/**
	 * 下一节点名称
	 */
	private String nextNodeName = null;//下一节点名称
	
	/**
	 * 下一节点办理人
	 */
	private String nextNodeUser = null;//下一节点办理人
	
	/**
	 * 下一办理人名称
	 */
	private String nextNodeUserName = null;
	
	/**
	 * 流程ID
	 */
	private String wfId;
	
	/**
	 * 流程名称
	 */
	private String wfName;
	
	/**
	 * 流程版本
	 */
	private String wfVer;
	
	/**
	 * 应用模块ID，与echainForms.xml的app[id]对应
	 */
	private String wfAppId;
	
	/**
	 * 应用模块名称，与echainForms.xml的app[id]对应
	 */
	private String wfAppName;
	
	/**
	 * 流程主表单
	 */
	private String wfmainformid;
	
	/**
	 * 流程管理员
	 */
	private String wfAdmin;
	
	/**
	 * 流程阅读者
	 */
	private String wfReaders;
	
	/**
	 * 流程实例启动者
	 */
	private String author;
	
	/**
	 * @return 操作结果标识
	 */
	public int getSign() {
		return sign;
	}

	/**
	 * @param 操作结果标识
	 */
	public void setSign(int sign) {
		this.sign = sign;
	}

	/**
	 * @return 操作结果信息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param 操作结果信息
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 当前流程实例号
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param 当前流程实例号
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return 流程标识
	 */
	public String getWfSign() {
		return wfSign;
	}

	/**
	 * @param 流程标识
	 */
	public void setWfSign(String wfSign) {
		this.wfSign = wfSign;
	}

	/**
	 * @return 当前办理人
	 */
	public String getCurrentUserId() {
		return currentUserId;
	}

	/**
	 * @param 当前办理人
	 */
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	/**
	 * @return 当前节点ID
	 */
	public String getCurrentNodeId() {
		return currentNodeId;
	}

	/**
	 * @param 当前节点ID
	 */
	public void setCurrentNodeId(String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	/**
	 * @return 当前节点名称
	 */
	public String getCurrentNodeName() {
		return currentNodeName;
	}

	/**
	 * @param 当前节点名称
	 */
	public void setCurrentNodeName(String currentNodeName) {
		this.currentNodeName = currentNodeName;
	}

	/**
	 * @return 下一节点Id
	 */
	public String getNextNodeId() {
		return nextNodeId;
	}

	/**
	 * @param 下一节点Id
	 */
	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

	/**
	 * @return 下一节点名称
	 */
	public String getNextNodeName() {
		return nextNodeName;
	}

	/**
	 * @param 下一节点名称
	 */
	public void setNextNodeName(String nextNodeName) {
		this.nextNodeName = nextNodeName;
	}

	/**
	 * @return 下一节点办理人
	 */
	public String getNextNodeUser() {
		return nextNodeUser;
	}

	/**
	 * @param 下一节点办理人
	 */
	public void setNextNodeUser(String nextNodeUser) {
		this.nextNodeUser = nextNodeUser;
	}

	/**
	 * @return 下一办理人名称
	 */
	public String getNextNodeUserName() {
		return nextNodeUserName;
	}

	/**
	 * @param nextNodeUserName 下一办理人名称 to set
	 */
	public void setNextNodeUserName(String nextNodeUserName) {
		this.nextNodeUserName = nextNodeUserName;
	}

	/**
	 * @return the wfId
	 */
	public String getWfId() {
		return wfId;
	}

	/**
	 * @return the wfName
	 */
	public String getWfName() {
		return wfName;
	}

	/**
	 * @param wfId the wfId to set
	 */
	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	/**
	 * @param wfName the wfName to set
	 */
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}

	/**
	 * @return the wfVer
	 */
	public String getWfVer() {
		return wfVer;
	}

	/**
	 * @return the wfAppId
	 */
	public String getWfAppId() {
		return wfAppId;
	}

	/**
	 * @return the wfAppName
	 */
	public String getWfAppName() {
		return wfAppName;
	}

	/**
	 * @return the wfmainformid
	 */
	public String getWfmainformid() {
		return wfmainformid;
	}

	/**
	 * @return the wfAdmin
	 */
	public String getWfAdmin() {
		return wfAdmin;
	}

	/**
	 * @return the wfReaders
	 */
	public String getWfReaders() {
		return wfReaders;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param wfVer the wfVer to set
	 */
	public void setWfVer(String wfVer) {
		this.wfVer = wfVer;
	}

	/**
	 * @param wfAppId the wfAppId to set
	 */
	public void setWfAppId(String wfAppId) {
		this.wfAppId = wfAppId;
	}

	/**
	 * @param wfAppName the wfAppName to set
	 */
	public void setWfAppName(String wfAppName) {
		this.wfAppName = wfAppName;
	}

	/**
	 * @param wfmainformid the wfmainformid to set
	 */
	public void setWfmainformid(String wfmainformid) {
		this.wfmainformid = wfmainformid;
	}

	/**
	 * @param wfAdmin the wfAdmin to set
	 */
	public void setWfAdmin(String wfAdmin) {
		this.wfAdmin = wfAdmin;
	}

	/**
	 * @param wfReaders the wfReaders to set
	 */
	public void setWfReaders(String wfReaders) {
		this.wfReaders = wfReaders;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
