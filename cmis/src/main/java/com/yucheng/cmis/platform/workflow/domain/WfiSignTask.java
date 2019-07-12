package com.yucheng.cmis.platform.workflow.domain;

import com.yucheng.cmis.platform.workflow.WorkFlowConstance;

/**
 * Title: WfiSignTask.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Tue May 10 17:13:39 CST 2011
 * @version：1.0
 */
public class WfiSignTask implements com.yucheng.cmis.pub.CMISDomain {
	
	private String stTaskId;
	private String stTaskName;
    private double stRequestDay;
    private String stDuty;
    private String stExeUser;
    private String stExeOrg;
    private String stConfig;
    private String stAdvice;
    private String stMembers;
    private String stLeader;
    private double stTotalCount = 0l;
    private double stVoteCount = 0l;
    private double stAgreeCount = 0l;
    private double stRejectCount = 0l;
    private double stNoideaCount = 0l;
    private String stResult;
    private String stTaskStatus;
    private String serno;
    private String bizType;
    private String stStartTime;
    private String stEndTime;
    private double stTaskTimes = 1l;
    private String wfiNodeId;
    private String wfiInstanceId;
    private String wfiAdviceId;
    private String wfiSign;
    private String wfiSceneId;
    private String wfiBizPage;

    /**
     * @调用父类clone方法
     * @return Object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        // TODO: 定制clone数据
        return result;
    }

    public String getWfiSign() {
        return wfiSign;
    }

    public void setWfiSign(String wfiSign) {
        this.wfiSign = wfiSign;
    }

    public String getWfiSceneId() {
        return wfiSceneId;
    }

    public void setWfiSceneId(String wfiSceneId) {
        this.wfiSceneId = wfiSceneId;
    }

    public String getWfiBizPage() {
        return wfiBizPage;
    }

    public void setWfiBizPage(String wfiBizPage) {
        this.wfiBizPage = wfiBizPage;
    }

    /**
     * @return 返回 stRequestDay
     */
    public double getStRequestDay() {
        return stRequestDay;
    }

    public double getStTotalCount() {
        return stTotalCount;
    }

    public void setStTotalCount(double stTotalCount) {
        this.stTotalCount = stTotalCount;
    }

    public double getStVoteCount() {
        return stVoteCount;
    }

    public void setStVoteCount(double stVoteCount) {
        this.stVoteCount = stVoteCount;
    }

    /**
     * @设置 stRequestDay
     * @param stRequestDay
     */
    public void setStRequestDay(double stRequestDay) {
        this.stRequestDay = stRequestDay;
    }

    /**
     * @return 返回 stDuty
     */
    public String getStDuty() {
        return stDuty;
    }

    /**
     * @设置 stDuty
     * @param stDuty
     */
    public void setStDuty(String stDuty) {
        this.stDuty = stDuty;
    }

    /**
     * @return 返回 stTaskId
     */
    public String getStTaskId() {
        return stTaskId;
    }

    /**
     * @设置 stTaskId
     * @param stTaskId
     */
    public void setStTaskId(String stTaskId) {
        this.stTaskId = stTaskId;
    }

    /**
     * @return 返回 stTaskName
     */
    public String getStTaskName() {
        return stTaskName;
    }

    /**
     * @设置 stTaskName
     * @param stTaskName
     */
    public void setStTaskName(String stTaskName) {
        this.stTaskName = stTaskName;
    }

    /**
     * @return 返回 stExeUser
     */
    public String getStExeUser() {
        return stExeUser;
    }

    /**
     * @设置 stExeUser
     * @param stExeUser
     */
    public void setStExeUser(String stExeUser) {
        this.stExeUser = stExeUser;
    }

    /**
     * @return 返回 stExeOrg
     */
    public String getStExeOrg() {
        return stExeOrg;
    }

    /**
     * @设置 stExeOrg
     * @param stExeOrg
     */
    public void setStExeOrg(String stExeOrg) {
        this.stExeOrg = stExeOrg;
    }

    /**
     * @return 返回 stConfig
     */
    public String getStConfig() {
        return stConfig;
    }

    /**
     * @设置 stConfig
     * @param stConfig
     */
    public void setStConfig(String stConfig) {
        this.stConfig = stConfig;
    }

    /**
     * @return 返回 stAdvice
     */
    public String getStAdvice() {
        return stAdvice;
    }

    /**
     * @设置 stAdvice
     * @param stAdvice
     */
    public void setStAdvice(String stAdvice) {
        this.stAdvice = stAdvice;
    }

    /**
     * @return 返回 stMembers
     */
    public String getStMembers() {
        return stMembers;
    }

    /**
     * @设置 stMembers
     * @param stMembers
     */
    public void setStMembers(String stMembers) {
        this.stMembers = stMembers;
    }

    /**
     * @return 返回 stLeader
     */
    public String getStLeader() {
        return stLeader;
    }

    /**
     * @设置 stLeader
     * @param stLeader
     */
    public void setStLeader(String stLeader) {
        this.stLeader = stLeader;
    }

    /**
     * @return 返回 stAgreeCount
     */
    public double getStAgreeCount() {
        return stAgreeCount;
    }

    /**
     * @设置 stAgreeCount
     * @param stAgreeCount
     */
    public void setStAgreeCount(double stAgreeCount) {
        this.stAgreeCount = stAgreeCount;
    }

    /**
     * @return 返回 stRejectCount
     */
    public double getStRejectCount() {
        return stRejectCount;
    }

    /**
     * @设置 stRejectCount
     * @param stRejectCount
     */
    public void setStRejectCount(double stRejectCount) {
        this.stRejectCount = stRejectCount;
    }

    /**
     * @return 返回 stNoideaCount
     */
    public double getStNoideaCount() {
        return stNoideaCount;
    }

    /**
     * @设置 stNoideaCount
     * @param stNoideaCount
     */
    public void setStNoideaCount(double stNoideaCount) {
        this.stNoideaCount = stNoideaCount;
    }

    /**
     * @return 返回 stResult
     */
    public String getStResult() {
        return stResult;
    }

    /**
     * @设置 stResult
     * @param stResult
     */
    public void setStResult(String stResult) {
        this.stResult = stResult;
    }

    /**
     * @return 返回 stTaskStatus
     */
    public String getStTaskStatus() {
        return stTaskStatus;
    }

    /**
     * @设置 stTaskStatus
     * @param stTaskStatus
     */
    public void setStTaskStatus(String stTaskStatus) {
        this.stTaskStatus = stTaskStatus;
    }

    /**
     * @return 返回 serno
     */
    public String getSerno() {
        return serno;
    }

    /**
     * @设置 serno
     * @param serno
     */
    public void setSerno(String serno) {
        this.serno = serno;
    }

    /**
     * @return 返回 bizType
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * @设置 bizType
     * @param bizType
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * @return 返回 stStartTime
     */
    public String getStStartTime() {
        return stStartTime;
    }

    /**
     * @设置 stStartTime
     * @param stStartTime
     */
    public void setStStartTime(String stStartTime) {
        this.stStartTime = stStartTime;
    }

    /**
     * @return 返回 stEndTime
     */
    public String getStEndTime() {
        return stEndTime;
    }

    /**
     * @设置 stEndTime
     * @param stEndTime
     */
    public void setStEndTime(String stEndTime) {
        this.stEndTime = stEndTime;
    }

    /**
     * @return 返回 stTaskTimes
     */
    public double getStTaskTimes() {
        return stTaskTimes;
    }

    /**
     * @设置 stTaskTimes
     * @param stTaskTimes
     */
    public void setStTaskTimes(double stTaskTimes) {
        this.stTaskTimes = stTaskTimes;
    }

    /**
     * @return 返回 wfiNodeId
     */
    public String getWfiNodeId() {
        return wfiNodeId;
    }

    /**
     * @设置 wfiNodeId
     * @param wfiNodeId
     */
    public void setWfiNodeId(String wfiNodeId) {
        this.wfiNodeId = wfiNodeId;
    }

    /**
     * @return 返回 wfiInstanceId
     */
    public String getWfiInstanceId() {
        return wfiInstanceId;
    }

    /**
     * @设置 wfiInstanceId
     * @param wfiInstanceId
     */
    public void setWfiInstanceId(String wfiInstanceId) {
        this.wfiInstanceId = wfiInstanceId;
    }

    /**
     * @return 返回 wfiAdviceId
     */
    public String getWfiAdviceId() {
        return wfiAdviceId;
    }

    /**
     * @设置 wfiAdviceId
     * @param wfiAdviceId
     */
    public void setWfiAdviceId(String wfiAdviceId) {
        this.wfiAdviceId = wfiAdviceId;
    }

    /**
     * 统计计算票数
     * 
     * @param newResult
     * @param oldResult
     */
    public void calculationVote(String newResult, String oldResult) {
        if (oldResult == null) {
            if (newResult != null)
                this.stVoteCount++;// 添加投票人数
        } else {
            if (oldResult.equals(WorkFlowConstance.AGREE_VOTE)) {
                this.stAgreeCount--;
            } else if (oldResult.equals(WorkFlowConstance.REJECT_VOTE)) {
                this.stRejectCount--;
            } else if (oldResult.equals(WorkFlowConstance.NOIDEA_VOTE)) {
                this.stNoideaCount--;
            }
        }
        if (newResult == null) {
            if (oldResult != null)
                this.stVoteCount--;// 减少投票人数
        } else {
            if (newResult.equals(WorkFlowConstance.AGREE_VOTE)) {
                this.stAgreeCount++;
            } else if (newResult.equals(WorkFlowConstance.REJECT_VOTE)) {
                this.stRejectCount++;
            } else if (newResult.equals(WorkFlowConstance.NOIDEA_VOTE)) {
                this.stNoideaCount++;
            }
        }
    }
}