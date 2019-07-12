package com.yucheng.cmis.platform.workflow.demo.dao;

import java.sql.SQLException;

import com.yucheng.cmis.platform.workflow.domain.WfiDemo;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

/**
 * 流程示例dao
 * @author liuhw
 *
 */
public class WfiDemoDao extends CMISDao {

	/**
	 * 新增
	 * @param wfiDemo
	 * @return
	 * @throws DaoException
	 */
	public int addWfiDemo(WfiDemo wfiDemo) throws DaoException {
		int count = -1;
		String sqlId = "insertWfiDemo";
		try {
			count = SqlClient.insertAuto(sqlId, wfiDemo, getConnection());
		} catch (SQLException e) {
			throw new DaoException("wfiDemoDao", "新增流程示例表失败！", e);
		}
		return count;		
	}

}
