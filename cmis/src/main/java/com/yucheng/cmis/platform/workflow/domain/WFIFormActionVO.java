package com.yucheng.cmis.platform.workflow.domain;

/**
 * 节点高级操作对象
 * <p>true表示允许权限，其他值表示无权限
 * @author liuhw 2013-6-17
 */
public class WFIFormActionVO {
	
	/**
	 * 流程跟踪
	 */
	private boolean track;
	
	/**
	 * 查看意见
	 */
	private boolean viewcomment;
	
	/**
	 * 撤办
	 */
	private boolean cancel;
	
	/**
	 * 挂起
	 */
	private boolean hang;
	
	/**
	 * 唤醒
	 */
	private boolean wake;
	
	/**
	 * 流程设置
	 */
	private boolean setwf;
	
	/**
	 * 签收
	 */
	private boolean signin;
	
	/**
	 * 撤销签收
	 */
	private boolean signoff;
	
	/**
	 * 撤销任务认领
	 */
	private boolean tasksignoff;
	
	/**
	 * 跳转
	 */
	private boolean jump;
	
	/**
	 * 保存
	 */
	private boolean save;
	
	/**
	 * 提交
	 */
	private boolean submit;
	
	/**
	 * 填写意见
	 */
	private boolean setcomment;
	
	/**
	 * 转办
	 */
	private boolean change;
	
	/**
	 * 退回
	 */
	private boolean returnback;
	
	/**
	 * 抄送
	 */
	private boolean announce;
	
	/**
	 * 调用子流名称
	 */
	private String callsubflow;
	
	/**
	 * 打回
	 */
	private boolean callback;
	
	/**
	 * 审批协助
	 */
	private boolean assist;
	
	/**
	 * 会办
	 */
	private boolean gather;
	
	/**
	 * 虚拟办结(暂无)
	 */
	private boolean nodevirend;
	
	/**
	 * 撤销办理人
	 */
	private boolean withdraw;
	
	/**
	 * 重办
	 */
	private boolean again;
	
	/**
	 * 发起人追回
	 */
	private boolean againFirst;
	
	/**
	 * 手工催办
	 */
	private boolean urge;
	
	/**
	 * 业务否决（流程结束）
	 */
	private boolean reject;

	/**
	 * @return 流程跟踪
	 */
	public boolean isTrack() {
		return track;
	}

	/**
	 * @param 流程跟踪
	 */
	public void setTrack(boolean track) {
		this.track = track;
	}

	/**
	 * @return 查看意见
	 */
	public boolean isViewcomment() {
		return viewcomment;
	}

	/**
	 * @param viewcomment 查看意见 to set
	 */
	public void setViewcomment(boolean viewcomment) {
		this.viewcomment = viewcomment;
	}

	/**
	 * @return 撤办
	 */
	public boolean isCancel() {
		return cancel;
	}

	/**
	 * @param cancel 撤办 to set
	 */
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	/**
	 * @return 挂起
	 */
	public boolean isHang() {
		return hang;
	}

	/**
	 * @param hang 挂起 to set
	 */
	public void setHang(boolean hang) {
		this.hang = hang;
	}

	/**
	 * @return 唤醒
	 */
	public boolean isWake() {
		return wake;
	}

	/**
	 * @param wake 唤醒 to set
	 */
	public void setWake(boolean wake) {
		this.wake = wake;
	}

	/**
	 * @return 流程设置
	 */
	public boolean isSetwf() {
		return setwf;
	}

	/**
	 * @param setwf 流程设置 to set
	 */
	public void setSetwf(boolean setwf) {
		this.setwf = setwf;
	}

	/**
	 * @return 签收
	 */
	public boolean isSignin() {
		return signin;
	}

	/**
	 * @param signin 签收 to set
	 */
	public void setSignin(boolean signin) {
		this.signin = signin;
	}

	/**
	 * @return 撤销签收
	 */
	public boolean isSignoff() {
		return signoff;
	}

	/**
	 * @param signoff 撤销签收 to set
	 */
	public void setSignoff(boolean signoff) {
		this.signoff = signoff;
	}

	/**
	 * @return 撤销任务认领
	 */
	public boolean isTasksignoff() {
		return tasksignoff;
	}

	/**
	 * @param tasksignoff 撤销任务认领 to set
	 */
	public void setTasksignoff(boolean tasksignoff) {
		this.tasksignoff = tasksignoff;
	}

	/**
	 * @return 跳转
	 */
	public boolean isJump() {
		return jump;
	}

	/**
	 * @param jump 跳转 to set
	 */
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	/**
	 * @return 保存
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * @param save 保存 to set
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

	/**
	 * @return 提交
	 */
	public boolean isSubmit() {
		return submit;
	}

	/**
	 * @param submit 提交 to set
	 */
	public void setSubmit(boolean submit) {
		this.submit = submit;
	}

	/**
	 * @return 填写意见
	 */
	public boolean isSetcomment() {
		return setcomment;
	}

	/**
	 * @param setcomment 填写意见 to set
	 */
	public void setSetcomment(boolean setcomment) {
		this.setcomment = setcomment;
	}

	/**
	 * @return 转办
	 */
	public boolean isChange() {
		return change;
	}

	/**
	 * @param change 转办 to set
	 */
	public void setChange(boolean change) {
		this.change = change;
	}

	/**
	 * @return 退回
	 */
	public boolean isReturnback() {
		return returnback;
	}

	/**
	 * @param returnback 退回 to set
	 */
	public void setReturnback(boolean returnback) {
		this.returnback = returnback;
	}

	/**
	 * @return 抄送
	 */
	public boolean isAnnounce() {
		return announce;
	}

	/**
	 * @param announce 抄送 to set
	 */
	public void setAnnounce(boolean announce) {
		this.announce = announce;
	}

	/**
	 * @return 调用子流名称
	 */
	public String getCallsubflow() {
		return callsubflow;
	}

	/**
	 * @param callsubflow 调用子流名称 to set
	 */
	public void setCallsubflow(String callsubflow) {
		this.callsubflow = callsubflow;
	}

	/**
	 * @return 打回
	 */
	public boolean isCallback() {
		return callback;
	}

	/**
	 * @param callback 打回 to set
	 */
	public void setCallback(boolean callback) {
		this.callback = callback;
	}

	/**
	 * @return 审批协助
	 */
	public boolean isAssist() {
		return assist;
	}

	/**
	 * @param assist 审批协助 to set
	 */
	public void setAssist(boolean assist) {
		this.assist = assist;
	}

	/**
	 * @return 会办
	 */
	public boolean isGather() {
		return gather;
	}

	/**
	 * @param gather 会办 to set
	 */
	public void setGather(boolean gather) {
		this.gather = gather;
	}

	/**
	 * @return 虚拟办结(暂无)
	 */
	public boolean isNodevirend() {
		return nodevirend;
	}

	/**
	 * @param nodevirend 虚拟办结(暂无) to set
	 */
	public void setNodevirend(boolean nodevirend) {
		this.nodevirend = nodevirend;
	}

	/**
	 * @return 撤销办理人
	 */
	public boolean isWithdraw() {
		return withdraw;
	}

	/**
	 * @param withdraw 撤销办理人 to set
	 */
	public void setWithdraw(boolean withdraw) {
		this.withdraw = withdraw;
	}

	/**
	 * @return 重办
	 */
	public boolean isAgain() {
		return again;
	}

	/**
	 * @param again 重办 to set
	 */
	public void setAgain(boolean again) {
		this.again = again;
	}

	/**
	 * @return 发起人追回
	 */
	public boolean isAgainFirst() {
		return againFirst;
	}

	/**
	 * @param againFirst 发起人追回
	 */
	public void setAgainFirst(boolean againFirst) {
		this.againFirst = againFirst;
	}

	/**
	 * @return 手工催办
	 */
	public boolean isUrge() {
		return urge;
	}

	/**
	 * @param urge 手工催办 to set
	 */
	public void setUrge(boolean urge) {
		this.urge = urge;
	}

	/**
	 * @return 业务否决（流程结束）
	 */
	public boolean isReject() {
		return reject;
	}

	/**
	 * @param reject 业务否决（流程结束） to set
	 */
	public void setReject(boolean reject) {
		this.reject = reject;
	}
	
}
