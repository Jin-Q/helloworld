package com.yucheng.cmis.biz01line.cus.cuscom.agent;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.ModifyHistory;
import com.yucheng.cmis.biz01line.cus.cuscom.dao.ModifyHistoryDao;
import com.yucheng.cmis.pub.CMISAgent;

public class ModifyHistoryAgent extends CMISAgent {
	public void insertBlobData(ModifyHistory modifyHistory) throws EMPException {
		ModifyHistoryDao dao = new ModifyHistoryDao();
		dao.insertBlobData(modifyHistory, this.getConnection());
	}

	/**
	 * 从数据库中取到修改的详细信息，把它转换成KColl
	 * @param keyId
	 * @return
	 * @throws EMPException
	 */
	public String getDetailKColl(String keyId) throws EMPException {
		ModifyHistoryDao dao = new ModifyHistoryDao();
		return dao.getDetailKColl(keyId, this.getConnection());
	}
	
	/**author 徐凯希
	 * 从数据库中取到修改的详细信息，把它转换成KColl
	 * @param keyId
	 * @return
	 * @throws EMPException
	 */
	public String getColCnName(String keyId, String phyTable) throws Exception {
//		String sql = " select column_name,comments  from USER_COL_COMMENTS where  table_name in ('CUS_COM','CUS_INDIV') and COLUMN_NAME ='"+keyId.toUpperCase()+"'";
		String sql = " select column_name,comments  from USER_COL_COMMENTS where  table_name in ('"+phyTable.toUpperCase()+"') and COLUMN_NAME ='"+keyId.toUpperCase()+"'";
		Connection conn = this.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = null;
		String cnName = "";
		if(ps.execute())
		{
			rs = ps.getResultSet();
			if(rs.next()){
				cnName = rs.getString("comments");
			}
		}
		//cnName = new String(cnName.getBytes("UTF-8"),"GB2312");
		if(rs!=null)
			rs.close();
		if(ps!=null)
			ps.close();
		return cnName;
	}
	
	/**author 徐凯希
	 * 从数据库中取到修改的详细信息，把它转换成KColl
	 * @param keyId
	 * @return
	 * @throws EMPException
	 */
	public String getValueCnName(String opttype,String value) throws Exception {
//		String sql = "select cnname from s_dic " +
//				" where opttype in( select  OPTTYPE from Modify_History_cfg where  COLUMN_NAME = '"+colId+"') and enname = '"+value+"'";
		String sql = "select cnname from s_dic where opttype ='"+opttype+"' and enname = '"+value+"'";
		Connection conn = this.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = null;
		String cnName = "";
		if(ps.execute())
		{
			rs = ps.getResultSet();
			if(rs.next())
			{
				cnName = rs.getString("cnname");
			}
		}
		//cnName = new String(cnName.getBytes("UTF-8"),"GB2312");
		if(rs!=null)
			rs.close();
		if(ps!=null)
			ps.close();
		if(cnName==null) cnName="";
		if(cnName.equals("")) cnName =  value;
		return cnName;
	}
	
	/**
	 * 根据表模型删除原来的配置字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String deleteModifyCfgByModelId(String model_id) throws Exception {
		ModifyHistoryDao dao = new ModifyHistoryDao();
		return dao.deleteModifyCfgByModelId(model_id, this.getConnection());
	}
	
	/**
	 * 根据表模型插入字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertModifyCfgByModelId(IndexedCollection iColl) throws Exception {
		ModifyHistoryDao dao = new ModifyHistoryDao();
		return dao.insertModifyCfgByModelId(iColl,this.getConnection());
	}
}
