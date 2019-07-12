/**
 * 
 */
package com.yucheng.cmis.platform.workflow.domain;

/**
 * 任务状态
 */
public class VoteStatus {

    /**
     * 同意110
     */
    public static final String AGREE_VOTE = "110";
    /**
     * 否决111
     */
    public static final String REJECT_VOTE = "111";
    /**
     * 复议112
     */
    public static final String NOIDEA_VOTE = "112";
    /**
     * 判断是否是有效审批结论
     * @param result
     * @return
     */
    public static boolean isValidResult(String result) {
        return AGREE_VOTE.equals(result) || REJECT_VOTE.equals(result) || NOIDEA_VOTE.equals(result);
    }

}
