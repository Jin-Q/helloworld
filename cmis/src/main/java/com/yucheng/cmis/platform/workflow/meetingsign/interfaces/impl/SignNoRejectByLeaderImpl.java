package com.yucheng.cmis.platform.workflow.meetingsign.interfaces.impl;

import java.util.Map;

import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.interfaces.SignConfigInterface;

/**
 * 一票否决
 * @author 南部大区信贷业务部  MOHEN
 * @Email:zhouxuan@yuchengtech.com
 * 2011-5-20下午10:27:58
 * TODO
 */
public class SignNoRejectByLeaderImpl implements SignConfigInterface {
    /*
     * (non-Javadoc)
     * @seecom.yucheng.cmis.wfi.sign.confInterface.SignConfigInterface#
     * checkSignTaskResult(double, double, double, double, double,
     * java.util.Map, java.lang.String)
     */
    public String checkSignTaskResult(double totalCount, double voteCount, double agreeCount, double rejectCount, double noIdeaCount, Map<String, String> voteMap, String leader) {
        String result="";
        try{
            if(rejectCount>0){//一票 否决 则贷审会结果为否决
                result=WorkFlowConstance.REJECT_VOTE;
            }else{
                if(totalCount==agreeCount){//全票通过则为 通过
                    result=WorkFlowConstance.AGREE_VOTE;
                }else if(noIdeaCount > 0){//否则为复议
                    result=WorkFlowConstance.NOIDEA_VOTE;
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
