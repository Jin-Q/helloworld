/**
 * 
 */
package com.yucheng.cmis.platform.workflow.domain;

/**
 * 任务状态
 */
public class TaskStatus {
    /**
     * 等待开始210
     */
    public static final String WAIT_BEGIN = "210";
    /**
     * 等待投票211
     */
    public static final String WAIT_VOTE = "211";
    /**
     * 正在投票212
     */
    public static final String NOW_VOTE = "212";
    /**
     * 投票结束213
     */
    public static final String FINISH_VOTE = "213";
    /**
     * 会议重开214
     */
    public static final String RESTART_VOTE = "214";
    /**
     * 自动弃权215
     */
    public static final String AUTO_FINISH = "215";
    /**
     * 取消会议216
     */
    public static final String CANCEL_TASK = "216";
    /**
     * 会议结束217
     */
    public static final String FINISH_TASK = "217";

}
