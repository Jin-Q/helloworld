package com.yucheng.cmis.platform.workflow.meetingsign.component;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.domain.WfiSignConf;
import com.yucheng.cmis.platform.workflow.domain.WfiSignTask;
import com.yucheng.cmis.platform.workflow.domain.WfiSignVote;
import com.yucheng.cmis.platform.workflow.domain.WfiWorkflow2biz;
import com.yucheng.cmis.platform.workflow.meetingsign.dao.WfiSignDAO;
import com.yucheng.cmis.platform.workflow.meetingsign.interfaces.SignConfigInterface;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.util.UNIDProducer;

public class WfiSignComponent extends CMISComponent {
	
    /**
     * 创建初始化会签任务
     * 
     * @param instanceId
     * @throws ComponentException
     */
    public WfiSignTask initSignTask(String instanceId) throws ComponentException {
        WfiSignTask task = new WfiSignTask();
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
            // 流程结构组件
            WFIComponent wfiComp = (WFIComponent) this.getComponent(WorkFlowConstance.WFI_COMPONENTID);
            KeyedCollection joinInfo = wfiComp.queryWfiJoin(instanceId);
            if (joinInfo.isEmpty()) {
                throw new ComponentException("无法获取到实例ID为：" + instanceId + "的流程信息");
            }
            Context context = this.getContext();
            String stExeUser = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
            String stExeOrg = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
            String bizType = (String) joinInfo.getDataValue("appl_type");
            String serno = (String) context.getDataValue("pkVal");
            String nodeId = (String) context.getDataValue("nextNodeId");
            String wfsign = (String) context.getDataValue("wfSign");
            String openDay = (String) context.getDataValue(CMISConstance.OPENDAY);
            UNIDProducer unidProducer = new UNIDProducer();
            task.setStTaskId(unidProducer.getUNID());
            task.setWfiInstanceId(instanceId);
            task.setSerno(serno);
            task.setWfiNodeId(nodeId);
            task.setBizType(bizType);
            task.setStStartTime(openDay);
            task.setStExeUser(stExeUser);
            task.setStExeOrg(stExeOrg);
            task.setWfiSign(wfsign);
            task.setStTotalCount(0);
            task.setStAgreeCount(0);
            task.setStVoteCount(0);
            task.setStRejectCount(0);
            task.setStNoideaCount(0);
            String signId = "0";// 会签策略ID
            String scene = "";// 场景
            String bizPage = "";// 详细页面页面
            String taskName = "贷审会会议";
            String stDuty = "";
            try {
                signId = WorkFlowUtil.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
                scene = WorkFlowUtil.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_SCENE);
                String wfId = nodeId.split("_")[0];
                String applType = (String) context.getDataValue("applType");
                WfiWorkflow2biz bizConf = wfiComp.getWf2bizConf(applType, nodeId, WorkFlowConstance.WFI_2BIZ_SCOPE_APPROVE);
                bizPage = bizConf.getAppUrl();
                String nodeName = (String) WorkFlowUtil.getWFNodeProperty(nodeId, "nodename");
                String nodeUsersList = (String) WorkFlowUtil.getWFNodeProperty(nodeId, "NodeUsersList");
                taskName = nodeName + "(1)";
                stDuty = nodeUsersList.replaceAll("G\\.", "").replaceAll(";", ",");
            } catch (Exception e) {
                  e.printStackTrace();
            }
            task.setWfiSceneId(scene);
            task.setWfiBizPage(bizPage);
            task.setStConfig(signId);
            task.setStDuty(stDuty);
            task.setStTaskName(taskName);
            task.setStTaskStatus(WorkFlowConstance.WAIT_BEGIN);
            task.setStTaskTimes(1L);
            dao.insertWfiSignTask(task, null);
            
        } catch (Exception e) {
            throw new ComponentException("创建会签任务异常", e.getMessage());
        }
        return task;
    }

    /**
     * 开始会签会议
     * 
     * @param taskId
     * @return
     * @throws ComponentException
     */
    public String beginSignTask(String taskId) throws ComponentException {
        WfiSignTask task = null;
    	OrganizationServiceInterface orgMsi = null;
        String success = "false";
        try { 
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
        	task = dao.queryWfiSignTask(taskId, null);
        	int zsNum = 0;
            if (task != null) {
                String voteUserStr = task.getStMembers();
                double i=0;
                for (String user : voteUserStr.split(";")) {
                    String svExeUser = user.substring(user.indexOf("(") + 1, user.indexOf(")")).trim();
                    if (svExeUser != "") {
                    	orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
            			List<SDuty> dutyList = orgMsi.getDutysByUserId(svExeUser, this.getConnection());
                    	if(dutyList!=null&&dutyList.size()>0){
                    		for(int j=0;dutyList.size()>j;j++){
                    			if("D0204".equals(dutyList.get(j).getDutyno())||"D0203".equals(dutyList.get(j).getDutyno())){
                    				zsNum++;
                    			}
                    		}
                    	}
                    }
                }
                if(zsNum<1){
               	 return "noDuty";
               }
                for (String user : voteUserStr.split(";")) {
                    i++;
                    String svExeUser = user.substring(user.indexOf("(") + 1, user.indexOf(")")).trim();
                    if (svExeUser != "") {
                        WfiSignVote vote = new WfiSignVote();
                        vote.setStTaskId(taskId);
                        vote.setSvExeUser(svExeUser);
                        vote.setSvStartTime((String)this.getContext().getDataValue(CMISConstance.OPENDAY));
                        vote.setSvResult(null);
                        vote.setSvStatus(WorkFlowConstance.WAIT_VOTE);
                        dao.insertWfiSignVote(vote, null);
                    }
                }
              
                task.setStTotalCount(i);
                task.setStTaskStatus(WorkFlowConstance.NOW_VOTE);
                dao.updateWfiSignTask(task, null);
                success = "true";
            }
        } catch (Exception e) {
            throw new ComponentException("开始会签任务异常", e.getMessage());
        }
        return success;
    }

    /**
     * 终止会签任务
     * 
     * @param taskId
     * @return
     * @throws ComponentException
     */
    public boolean finishSignTask(String taskId) throws ComponentException {
        WfiSignTask task = null;
        boolean success = false;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
        	task = dao.queryWfiSignTask(taskId, null);
            if (task != null) {
            	List<WfiSignVote> voteList = dao.queryWfiSignVoteListByTaskId(taskId, null);
                String openDay = (String) this.getContext().getDataValue(CMISConstance.OPENDAY);
                task.setStResult(WorkFlowConstance.NOIDEA_VOTE);
                for (WfiSignVote vote : voteList) {
                    if (vote.getSvResult()==null) {
                        vote.setSvEndTime(openDay);
                        vote.setSvStatus(WorkFlowConstance.AUTO_FINISH);
                        dao.updateWfiSignVote(vote, null);
                    }
                }
                task.setStTaskStatus(WorkFlowConstance.FINISH_VOTE);
                task.setStEndTime(openDay);
                dao.updateWfiSignTask(task, null);
                success = true;
            }
        } catch (Exception e) {
            throw new ComponentException("终止会签任务异常", e.getMessage());
        }
        return success;
    }

    /**
     * 取消会议
     * 
     * @param taskId
     * @return
     * @throws ComponentException
     */
    public boolean cancelSignTask(String taskId) throws ComponentException {
        WfiSignTask task = null;
        boolean success = false;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
        	task = dao.queryWfiSignTask(taskId, null);
            if (task != null) {
                List<WfiSignVote> voteList = dao.queryWfiSignVoteListByTaskId(taskId, null);
                String openDay = (String) this.getContext().getDataValue(CMISConstance.OPENDAY);
                for (CMISDomain obj : voteList) {
                    WfiSignVote vote = (WfiSignVote) obj;
                    if (vote.getSvResult()==null) {
                        vote.setSvEndTime(openDay);
//                        vote.setSvStatus(WorkFlowConstance.AUTO_FINISH);
                        //edit by tangzf 会议取消后，被取消会议对应的任务状态也改为会议取消
                        vote.setSvStatus(WorkFlowConstance.CANCEL_TASK);
                        dao.updateWfiSignVote(vote, null);
                    }
                }
                task.setStTaskStatus(WorkFlowConstance.CANCEL_TASK);
                task.setStEndTime(openDay);
                dao.updateWfiSignTask(task, null);
                success = true;
                Context context = this.getContext();
                // 调用流程接口 退回 上一节点
                WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
                Map paramMap = new HashMap();
                String suggestContent = "线上会签取消";
                paramMap.put("SuggestContent", suggestContent);
                paramMap.put("SuggestControl", "1");
                paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
                try {
                    String instanceId = task.getWfiInstanceId();
                    String userId = task.getStExeUser();
                    WFIVO wfiVo = wfi.wfTakeBack(instanceId, userId, paramMap, this.getConnection());
                    if(WFIVO.SIGN_SUCCESS != wfiVo.getSign()) {
                    	throw new ComponentException("取消会签失败，原因："+wfiVo.getMessage());
                    }
                    /*try {
                        context.addDataField(WorkFlowConstance.WFI_RET_NAME, hm);
                    } catch (DuplicatedDataNameException e) {
                        context.setDataValue(WorkFlowConstance.WFI_RET_NAME, hm);
                    }*/
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            throw new ComponentException("取消会签任务异常", e.getMessage());
        }
        return success;
    }

    /**
     * 提交处理会签任务
     * 
     * @param taskId
     * @return
     */
    public boolean submitSignTask(String taskId) throws ComponentException {
        WfiSignTask task = null;
        boolean success = false;
        WorkflowServiceInterface wfi = null;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
        	task = dao.queryWfiSignTask(taskId, null);
            Connection conn = this.getConnection();
            wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
            String toUserCd = task.getStExeUser();
            String exeUserCd = WorkFlowConstance.SIGN_USER;
            String nodeId = task.getWfiNodeId();
            String instanceid = task.getWfiInstanceId();
            String result = task.getStResult();
            if (result.equals(WorkFlowConstance.AGREE_VOTE)) {
                result = WorkFlowConstance.WFI_RESULT_AGREE;
            } else {
                result = WorkFlowConstance.WFI_RESULT_DISAGREE;
            }
            String commentId = new UNIDProducer().getUNID();
            String loginuserid = (String) this.getContext().getDataValue("loginuserid");
            if(loginuserid!=null&&!loginuserid.equals(toUserCd)){
            	String loginusername = this.getContext().getDataValue("loginusername")+"(受托于"+this.getContext().getDataValue("currentUserName")+"办理)";
            	wfi.saveWFComment4CusTrustee(commentId, instanceid, nodeId, toUserCd, loginusername,result, task.getStAdvice(), task.getStExeOrg(), conn);
            }else{
            	wfi.saveWFComment(commentId, instanceid, nodeId, toUserCd, result, task.getStAdvice(), task.getStExeOrg(), conn);
            }
            
            task.setWfiAdviceId(commentId);
            task.setStTaskStatus(WorkFlowConstance.FINISH_TASK);
            dao.updateWfiSignTask(task, null);
            //更新流程表，汗
            success = dao.swapWfUser(toUserCd, exeUserCd, nodeId, instanceid, null);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ComponentException("提交处理会签任务异常", e.getMessage());
        }
        return success;
    }

    /**
     * 重开贷审会
     * 
     * @param taskId
     * @return
     */
    public String rebeginSignTask(String taskId) throws ComponentException {
        String newTaskId = "";
        WfiSignTask task = null;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
            task = dao.queryWfiSignTask(taskId, null);
            String openDay = (String) this.getContext().getDataValue(CMISConstance.OPENDAY);
            task.setStEndTime(openDay);
            task.setStResult(WorkFlowConstance.NOIDEA_VOTE);
            task.setStTaskStatus(WorkFlowConstance.RESTART_VOTE);
            dao.updateWfiSignTask(task, null);
            //edit by tangzf 会议重开后，被取消会议对应的任务状态也改为重开取消
            List<WfiSignVote> voteList = dao.queryWfiSignVoteListByTaskId(taskId, null);
            for (CMISDomain obj : voteList) {
                WfiSignVote vote = (WfiSignVote) obj;
                if (vote.getSvResult()==null) {
                    vote.setSvEndTime(openDay);
                    vote.setSvStatus(WorkFlowConstance.RESTART_VOTE);
                    dao.updateWfiSignVote(vote, null);
                }
            }
            double taskTimes = task.getStTaskTimes();// 贷审会次数
            // 构建新贷审会任务
            taskTimes = taskTimes + 1L;// 贷审会次数加1
            String stTaskName = task.getStTaskName();
            String taskName = stTaskName.replaceAll("\\d+(?=\\))", String.valueOf((int) taskTimes));
            task.setStTaskId(null);
            task.setStTaskName(taskName);
            task.setStStartTime(openDay);
            task.setStEndTime("");
            task.setStAdvice("");
            task.setStTotalCount(0L);
            task.setStVoteCount(0L);
            task.setStAgreeCount(0L);
            task.setStRejectCount(0L);
            task.setStNoideaCount(0L);
            task.setStLeader("");
            task.setStMembers("");
            task.setStResult("");
            task.setStTaskTimes(taskTimes);
            task.setStTaskStatus(WorkFlowConstance.WAIT_BEGIN);
            task.setStTaskId(new UNIDProducer().getUNID());
            dao.insertWfiSignTask(task, null);
            newTaskId = task.getStTaskId();
        } catch (Exception e) {
            throw new ComponentException("重开会签任务异常", e.getMessage());
        }
        return newTaskId;
    }

    /**
     * 贷审会成员投票
     * 
     * @param voteId
     * @param result
     * @return
     * @throws ComponentException
     */
    public boolean submitVoteTask(String voteId, String result, String svAdvice) throws ComponentException {
        WfiSignVote vote = null;
        WfiSignTask task = null;
        boolean success = false;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
            vote = dao.queryWfiSignVote(voteId, null);
            String taskId = vote.getStTaskId();
            task = dao.queryWfiSignTask(taskId, null);
            String oldResult = vote.getSvResult();
            vote.setSvStatus(WorkFlowConstance.FINISH_VOTE);
            vote.setSvAdvice(svAdvice);
            vote.setSvResult(result);
            vote.setSvEndTime((String)this.getContext().getDataValue(CMISConstance.OPENDAY));
            dao.updateWfiSignVote(vote, null);
            // 計算会签票数
            task.calculationVote(result, oldResult);
            List<WfiSignVote> voteList = dao.queryWfiSignVoteListByTaskId(taskId, null);
            boolean finishTask = calculationSignConfig(task, voteList);// 判断是否结束贷审会
            if (finishTask) {
                String openDay = (String) this.getContext().getDataValue(CMISConstance.OPENDAY);
                for (WfiSignVote obj : voteList) {
                    if (obj.getSvResult()==null) {
                    	obj.setSvEndTime(openDay);
                    	obj.setSvStatus(WorkFlowConstance.AUTO_FINISH);
                        dao.updateWfiSignVote(obj, null);
                    }
                }
                task.setStTaskStatus(WorkFlowConstance.FINISH_VOTE);
            }
            dao.updateWfiSignTask(task, null);
            success = true;
        } catch (Exception e) {
            throw new ComponentException("会签成员投票异常", e.getMessage());
        }
        return success;
    }

    /**
     * 调用贷审会策略 计算贷审会 是否满足会签策略
     * 
     * @param task
     * @return 是否终止贷审会
     * @throws ComponentException
     */
    private boolean calculationSignConfig(WfiSignTask task, List<WfiSignVote> voteList) throws ComponentException {
        boolean success = false;
        WfiSignConf conf = null;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
            String signConfigId = task.getStConfig();
            conf = dao.queryWfiSignConf(signConfigId, null);
            String confClassName = "com.yucheng.cmis.platform.workflow.meetingsign.interfaces.impl.SignConfigImpl";
            if (conf != null) {
                confClassName = conf.getSignClass();
            }
            Map<String, String> voteMap = new HashMap<String, String>();
            Map<String, String> voteAdviceMap = new HashMap<String, String>();
            for (Iterator<WfiSignVote> iter = voteList.iterator(); iter.hasNext();) {
                WfiSignVote vote = (WfiSignVote) iter.next();
                voteMap.put(vote.getSvExeUser(), vote.getSvResult());
                voteAdviceMap.put(vote.getSvExeUser(), vote.getSvAdvice());
            }
            SignConfigInterface imp = (SignConfigInterface) Class.forName(confClassName).newInstance();
            double totalCount = task.getStTotalCount();
            double voteCount = task.getStVoteCount();
            double agreeCount = task.getStAgreeCount();
            double rejectCount = task.getStRejectCount();
            double noIdeaCount = task.getStNoideaCount();
            String leader = task.getStLeader();
            // 判断投票结果对 会议的影响
            String result = imp.checkSignTaskResult(totalCount, voteCount, agreeCount, rejectCount, noIdeaCount, voteMap, leader);
            success = isValidResult(result);
            if (success) {
                // 取出意见汇总
                String stAdvice = imp.buildSignAdviceResult(totalCount, voteCount, agreeCount, rejectCount, noIdeaCount, voteAdviceMap, leader);
                if (stAdvice != null)
                    task.setStAdvice(stAdvice);
                task.setStResult(result);
                task.setStTaskStatus(WorkFlowConstance.FINISH_VOTE);
                task.setStEndTime((String)this.getContext().getDataValue(CMISConstance.OPENDAY));
            }
        } catch (Exception e) {
            throw new ComponentException("调用贷审会策略 计算贷审会结果异常", e.getMessage());
        }
        return success;
    }

    /**
     * 复位贷审会 成员投票
     * 
     * @param voteId
     * @return
     * @throws ComponentException
     */
    public boolean resetSignVote(String voteId) throws ComponentException {
        WfiSignVote vote = null;
        WfiSignTask task = null;
        boolean success = false;
        try {
        	WfiSignDAO dao = (WfiSignDAO) this.getDaoInstance("wfiSignDao");
        	vote = dao.queryWfiSignVote(voteId, null);
            String taskId = vote.getStTaskId();
            task = dao.queryWfiSignTask(taskId, null);
            String oldResult = vote.getSvResult();
            vote.setSvResult("");
            vote.setSvStatus(WorkFlowConstance.WAIT_VOTE);
            vote.setSvEndTime("");
            task.calculationVote(null, oldResult);
            dao.updateWfiSignTask(task, null);
            dao.updateWfiSignVote(vote, null);
            success = true;
        } catch (Exception e) {
            throw new ComponentException("贷审会成员投票复位异常", e.getMessage());
        }
        return success;
    }

    /**
     * 判断是否是有效审批结论
     * @param result
     * @return
     */
    private static boolean isValidResult(String result) {
        return WorkFlowConstance.AGREE_VOTE.equals(result) 
        		|| WorkFlowConstance.REJECT_VOTE.equals(result) 
        		|| WorkFlowConstance.NOIDEA_VOTE.equals(result);
    }

}
