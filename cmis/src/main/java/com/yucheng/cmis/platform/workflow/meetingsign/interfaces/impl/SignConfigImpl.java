package com.yucheng.cmis.platform.workflow.meetingsign.interfaces.impl;

import java.util.Map;

import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.interfaces.SignConfigInterface;


public class SignConfigImpl implements SignConfigInterface {
    /*
     * (non-Javadoc)
     * @seecom.yucheng.cmis.wfi.sign.confInterface.SignConfigInterface#
     * checkSignTaskResult(double, double, double, double, double,
     * java.util.Map, java.lang.String)
     */
    public String checkSignTaskResult(double totalCount, double voteCount, double agreeCount, double rejectCount, double noIdeaCount, Map<String, String> voteMap, String leader) {
        // TODO Auto-generated method stub
        return WorkFlowConstance.AGREE_VOTE;
    }

    /*
     * (non-Javadoc)
     * @seecom.yucheng.cmis.wfi.sign.confInterface.SignConfigInterface#
     * buildSignAdviceResult(double, double, double, double, double,
     * java.util.Map, java.lang.String)
     */
    public String buildSignAdviceResult(double totalCount, double voteCount, double agreeCount, double rejectCount, double noIdeaCount, Map<String, String> voteAdviceMap, String leader) {
        // TODO Auto-generated method stub
        return "意见汇总";
    }

}
