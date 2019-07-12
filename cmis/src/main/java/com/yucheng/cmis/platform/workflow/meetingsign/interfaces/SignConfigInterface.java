package com.yucheng.cmis.platform.workflow.meetingsign.interfaces;

import java.util.Map;

public interface SignConfigInterface {
    /**
     * 
     * @param totalCount 参数贷审会成员数
     * @param voteCount  已投票数
     * @param agreeCount  同意票数
     * @param rejectCount  反对票数
     * @param noIdeaCount 复议票数
     * @param voteMap   贷审会成员投票明细<String,String>  =<成员编号, 投票结果(110=同意；111=否决；112=复议；113=未投票)>
     * @param leader  贷审会牵头人
     * @return 返回 贷审会结果  如果 返回结果非贷审会结果字典  贷审会继续 直至  安排人终止 或者全部投票结束
     */
    public String checkSignTaskResult(double totalCount,double voteCount,double agreeCount ,double rejectCount,double noIdeaCount,Map<String,String> voteMap,String leader);
    /**
     * 贷审会意见汇总 模板
     * @param voteList
     * @param leader
     * @return
     */
    public String buildSignAdviceResult(double totalCount,double voteCount,double agreeCount ,double rejectCount,double noIdeaCount,Map<String,String> voteAdviceMap,String leader);
}
