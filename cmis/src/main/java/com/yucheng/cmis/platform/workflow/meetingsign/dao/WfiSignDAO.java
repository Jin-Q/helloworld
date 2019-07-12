package com.yucheng.cmis.platform.workflow.meetingsign.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.workflow.domain.WfiSignConf;
import com.yucheng.cmis.platform.workflow.domain.WfiSignTask;
import com.yucheng.cmis.platform.workflow.domain.WfiSignVote;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.util.UNIDProducer;

public class WfiSignDAO extends CMISDao {
	
	/**
	 * 根据会签任务ID查询
	 * @param taskId 会签任务ID
	 * @return task
	 * @param connection 数据库连接（可以为null；此参数，是为了方便直接new）
	 * @throws DaoException
	 */
	public WfiSignTask queryWfiSignTask(String taskId, Connection connection) throws DaoException {
		WfiSignTask task = null;
		String sqlId = "selectWfiSignTask";
        try {
        	if(connection == null) {
        		connection = this.getConnection();
        	}
			task = (WfiSignTask) SqlClient.queryFirst(sqlId, taskId, null, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("根据会签任务ID查询失败！异常信息："+e.getMessage());
		}
		return task;
	}
	
	/**
	 * 更新会签任务信息
	 * @param wfiSignTask 待更新的会签任务信息
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public boolean updateWfiSignTask(WfiSignTask wfiSignTask, Connection connection) throws DaoException {
		boolean result = false;
		String sqlId = "updateWfiSignTask";
		String taskId = wfiSignTask.getStTaskId();
		String[] pKeyField = new String[]{"st_task_id"};
		try {
			if(connection == null) {
        		connection = this.getConnection();
        	}
			int count = SqlClient.updateAuto(sqlId, wfiSignTask, pKeyField, connection);
			if(count == 1)
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("更新会签任务信息失败！异常信息："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 新增会签任务信息
	 * @param wfiSignTask 会签任务
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public boolean insertWfiSignTask(WfiSignTask wfiSignTask, Connection connection) throws DaoException {
		boolean result = false;
		String sqlId = "initWfiSignTask";
		try {
			if(connection == null)
				connection = this.getConnection();
			int count = SqlClient.insertAuto(sqlId, wfiSignTask, connection);
			if(count == 1)
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("新增会签任务信息！异常信息："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据会签投票任务ID查询
	 * @param voteId 会签投票任务ID
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public WfiSignVote queryWfiSignVote(String voteId, Connection connection) throws DaoException {
		WfiSignVote vote = new WfiSignVote();
		String sqlId = "selectWfiSignVote";
		if(connection == null)
			connection = this.getConnection();
		try {
			vote = (WfiSignVote) SqlClient.queryFirst(sqlId, voteId, null, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("根据会签投票任务ID查询出错！异常信息："+e.getMessage());
		}
		return vote;
	}
	
	/**
	 * 根据会签任务ID查询会签任务投票列表
	 * @param taskId 会签任务ID
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public List<WfiSignVote> queryWfiSignVoteListByTaskId(String taskId, Connection connection) throws DaoException {
		List<WfiSignVote> list = new ArrayList<WfiSignVote>();
		String sqlId = "selectWfiSignVoteList";
		if(connection == null)
			connection = this.getConnection();
		try {
			list = (List<WfiSignVote>) SqlClient.queryList(sqlId, taskId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("查询会签任务投票列表失败！异常信息："+e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * 更新会签投票任务信息
	 * @param vote 会签投票任务
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public boolean updateWfiSignVote(WfiSignVote vote, Connection connection) throws DaoException {
		boolean result = false;
		String sqlId = "updateWfiSignVote";
		if(connection == null)
			connection = this.getConnection();
		String[] pKeyField = new String[]{"sv_vote_id"};
		try {
			int count = SqlClient.updateAuto(sqlId, vote, pKeyField, connection);
			if(count == 1)
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("更新会签投票任务信息失败！异常信息："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 新增会签投票任务
	 * @param vote 会签投票任务
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public boolean insertWfiSignVote(WfiSignVote vote, Connection connection) throws DaoException {
		boolean result = false;
		String sqlId = "insertWfiSignVote";
		if(connection == null)
			connection = this.getConnection();
		if(vote.getSvVoteId() == null || vote.getSvVoteId().equals("")) {
			vote.setSvVoteId(new UNIDProducer().getUNID());
		}
		try {
			int count = SqlClient.insertAuto(sqlId, vote, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("新增会签投票任务失败！异常信息："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据ID查询会签策略配置
	 * @param signId 会签策略配置ID
	 * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
	 * @return
	 * @throws DaoException
	 */
	public WfiSignConf queryWfiSignConf(String signId, Connection connection) throws DaoException {
		WfiSignConf conf = new WfiSignConf();
		String sqlId = "queryWfiSignConfById";
		if(connection == null)
			connection = this.getConnection();
		try {
			conf = (WfiSignConf) SqlClient.queryFirst(sqlId, signId, null, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("根据ID查询会签策略配置失败！异常信息："+e.getMessage());
		}
		return conf;
	}
	
	
	
    /**
     * 替换流程任务当前执行人
     * @param toUserCd 会签审批人
     * @param exeUserCd 会签安排人
     * @param nodeId 节点ID
     * @param instanceid 实例ID
     * @param connection 据库连接（可以为null；此参数，是为了方便直接new）
     * @return
     * @throws DaoException
     */
    public boolean swapWfUser(String toUserCd, String exeUserCd, String nodeId, String instanceid, Connection connection) throws DaoException {
    	boolean success = false;
    	String sqlId = "updateWfNodeRecord";
        KeyedCollection nodeKcoll = new KeyedCollection();
        nodeKcoll.put("toUserCd", toUserCd);
        nodeKcoll.put("exeUserCd", exeUserCd);
        KeyedCollection paramKcoll = new KeyedCollection();
        paramKcoll.put("nodeid", nodeId);
        paramKcoll.put("instanceid", instanceid);
        try {
        	if(connection == null)
        		connection = this.getConnection();
        	try {
        		int count = SqlClient.update(sqlId, paramKcoll, nodeKcoll, null, connection);
        	}catch (Exception e1) {
    			throw new SQLException("更新wf_node_record当前执行人异常异常");
    		}
        	try {
        		sqlId = "updateWfInstanceNodeProp";
		        int n = SqlClient.update(sqlId, paramKcoll, nodeKcoll, null, connection);
        	}catch (Exception e1) {
    			throw new SQLException("更新wf_instance_node_property当前执行人异常异常");
    		}
        	success=true;
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DaoException("扩展会签替换流程任务当前执行人出错！异常信息："+e1.getMessage());
		}
        return success;
    }

}
