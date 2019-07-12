package com.yucheng.cmis.platform.workflow.meetingsign.interfaces.impl;

import java.util.Map;

import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.interfaces.SignConfigInterface;

/**
 * 三分之二通过
 * 同意票数大于等于成员总数的三分之二
 */
public class SignByRatioImpl implements SignConfigInterface {
    /*
     * @param totalCount 参数贷审会成员数
     * @param voteCount  已投票数
     * @param agreeCount  同意票数
     * @param rejectCount  反对票数
     * @param noIdeaCount 复议票数
     */
    public String checkSignTaskResult(double totalCount, double voteCount, double agreeCount, double rejectCount, double noIdeaCount, Map<String, String> voteMap, String leader) {
        String result="";
        try{
        	if(voteCount == totalCount) {
        		if((agreeCount/totalCount)>=(2d/3d)) {
                	//三分之二同意,则贷审会结果为通过
                	result=WorkFlowConstance.AGREE_VOTE;
            	}else {
            		//否则贷审会结果为否决
            		result=WorkFlowConstance.REJECT_VOTE;
            	}
        	}
            
        }catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @seecom.yucheng.cmis.wfi.sign.confInterface.SignConfigInterface#
     * buildSignAdviceResult(double, double, double, double, double,
     * java.util.Map, java.lang.String)
     */
    public String buildSignAdviceResult(double totalCount, double voteCount, double agreeCount, double rejectCount, double noIdeaCount, Map<String, String> voteAdviceMap, String leader) {
    	String advice="意见汇总:参加会议"+double2int(voteCount)+"人。同意票数:"+double2int(agreeCount)+";反对票数:"+double2int(rejectCount)+";复议票数:"+double2int(noIdeaCount);
        return advice;
    }
    private int double2int(double i){
        return (int)i;
    }

}
