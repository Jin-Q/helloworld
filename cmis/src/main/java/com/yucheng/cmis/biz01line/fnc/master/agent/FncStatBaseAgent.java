package com.yucheng.cmis.biz01line.fnc.master.agent;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatCommonDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatCfs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatIs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSl;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSoe;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;
  /**
 *@Classname	FncStatBaseAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-8 上午10:43:24  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncStatBaseAgent extends CMISAgent {
	/**
	 * 
	 * @param pfncStatBase 公司客户报表对象
	 * @return      flagInfo 信息编码
	 * @throws AgentException
	 */
	public String addRecord(FncStatBase pfncStatBase) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncStatBase,PUBConstant.FNCSTATBASE); // 1成功  其他失败
		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}

	/**
	 * 
	 * @param fncStatBs
	 * @param conn
	 * @return
	 */
	public FncStatBs QueryFncStatBs(FncStatBs fncStatBs,Connection conn){
		FncStatBs tempBs = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempBs = dao.QueryFncStatBs(fncStatBs,conn);
		return tempBs;
	}
	/**
	 * 
	 * @param fncStatCfs
	 * @param conn
	 * @return
	 */
	public FncStatCfs QueryFncStatCfs(FncStatCfs fncStatCfs,Connection conn){
		FncStatCfs tempCfs = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempCfs = dao.QueryFncStatCfs(fncStatCfs,conn);
		return tempCfs;
	}
	/**
	 * 
	 * @param fncIndexRpt
	 * @param conn
	 * @return
	 */
	public FncIndexRpt QueryFncIndexRpt(FncIndexRpt fncIndexRpt,Connection conn){
		FncIndexRpt tempFi = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempFi = dao.QueryFncIndexRpt(fncIndexRpt,conn);
		return tempFi;
	}
	/**
	 * 
	 * @param fncStatIs
	 * @param conn
	 * @return
	 */
	public FncStatIs QueryFncStatIs(FncStatIs fncStatIs,Connection conn){
		FncStatIs tempIs = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempIs = dao.QueryFncStatIs(fncStatIs,conn);
		return tempIs;
	}
	/**
	 * 
	 * @param fncStatSl
	 * @param conn
	 * @return
	 */
	public FncStatSl QueryFncStatSl(FncStatSl fncStatSl,Connection conn){
		FncStatSl tempSl = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempSl = dao.QueryFncStatSl(fncStatSl,conn);
		return tempSl;
	}
	/**
	 * 
	 * @param fncStatSoe
	 * @param conn
	 * @return
	 */
	public FncStatSoe QueryFncStatSoe(FncStatSoe fncStatSoe,Connection conn){
		FncStatSoe tempSl = null;
		FncStatCommonDao dao = new FncStatCommonDao();
		tempSl = dao.QueryFncStatSoe(fncStatSoe,conn);
		return tempSl;
	}
	/**
	 * 
	 * @param fncStatBs
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncStatBsRecord(FncStatBs fncStatBs,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		try {
			flagInfo = dao.insertFncStatBs(fncStatBs,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	/**
	 * 
	 * @param fncStatCfs
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncStatCfsRecord(FncStatCfs fncStatCfs,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		try {
			flagInfo = dao.insertFncStatCfs(fncStatCfs,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	/**
	 * 
	 * @param fncIndexRpt
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncIndexRptRecord(FncIndexRpt fncIndexRpt,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		try {
			flagInfo = dao.insertFncIndexRpt(fncIndexRpt,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	/**
	 * 
	 * @param fncStatIs
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncStatIsRecord(FncStatIs fncStatIs,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		try {
			flagInfo = dao.insertFncStatIs(fncStatIs,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	/**
	 * 新增财务简表信息
	 * @param fncStatSl
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncStatSlRecord(FncStatSl fncStatSl,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		try {
			flagInfo = dao.insertFncStatSl(fncStatSl,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	/**
	 * 
	 * @param fncStatSoe
	 * @param conn
	 * @return
	 * @throws AgentException
	 */
	public String addFncStatSoeRecord(FncStatSoe fncStatSoe,Connection conn) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		FncStatCommonDao dao = new FncStatCommonDao();
		flagInfo = dao.insertFncStatSoe(fncStatSoe,conn);
		return flagInfo;
	}
/**
 * 
 * @param pfncStatBase  公司客户报表对象
 * @return   flagInfo 信息编码
 * @throws AgentException
 */
	public String updateRecord(FncStatBase pfncStatBase)
			throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncStatBase, PUBConstant.FNCSTATBASE);// 1成功  其他失败
		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;

	}
/**
 * 
 * @param cusId  客户代码
 * @param statPrdStyle   报表周期类型
 * @param statPrd      报表期间
 * @return pfncStatBase 公司客户报表对象
 * @throws AgentException
 */
	public FncStatBase queryDetail(String cusId,String statPrdStyle,String statPrd,String stat_style) throws AgentException {

		FncStatBase pfncStatBase = new FncStatBase();
		
         //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("stat_prd_style", statPrdStyle);
		pk_values.put("stat_prd", statPrd);
		pk_values.put("stat_style", stat_style);

		//进行查询操作
		pfncStatBase = (FncStatBase) this.findCMISDomainByKeywords(
				pfncStatBase, PUBConstant.FNCSTATBASE, pk_values);

		return pfncStatBase;
	}
	
	public FncStatBase queryDetail(FncStatBase pfncStatBase) throws AgentException {

		FncStatBase temp = new FncStatBase();
		
         //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", pfncStatBase.getCusId());
		pk_values.put("stat_prd_style", pfncStatBase.getStatPrdStyle());
		pk_values.put("stat_prd", pfncStatBase.getStatPrd());
		pk_values.put("stat_style", pfncStatBase.getStatStyle());//报表口径
		pk_values.put("fnc_type", pfncStatBase.getFncType());//财报类型
		//进行查询操作
		temp = (FncStatBase) this.findCMISDomainByKeywords(temp, PUBConstant.FNCSTATBASE, pk_values);

		return temp;
	}
	/**
	 * @param cusId  客户代码
	 * @param statPrdStyle   报表周期类型
	 * @param statPrd      报表期间
	 * @return   flagInfo 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String cusId,String statPrdStyle,String statPrd) throws AgentException {
		
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		 //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("stat_prd_style", statPrdStyle);
		pk_values.put("stat_prd", statPrd);
		
        //进行删除操作
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCSTATBASE, pk_values);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;
	}
	
	/**
	 * 根据财务报表编号得到其配置信息
	 * @param cus_fin_type
	 * @param conn
	 * @return fncTemp
	 */
	public FncConfTemplate findFncConfTemplate(String cus_fin_type,Connection conn){
		FncConfTemplate fncTemp = null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt = conn.createStatement();
			String sql = "select FNC_ID,FNC_NAME,FNC_BS_STYLE_ID,FNC_PL_STYLE_ID,FNC_CF_STYLE_ID,FNC_FI_STYLE_ID," +
					"FNC_RI_STYLE_ID,FNC_SMP_STYLE_ID,FNC_STYLE_ID1,FNC_STYLE_ID2 " +
					"from FNC_CONF_TEMPLATE where FNC_ID='"+cus_fin_type+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				fncTemp = new FncConfTemplate();
				fncTemp.setFncId(rs.getString("FNC_ID"));
				fncTemp.setFncName(rs.getString("FNC_NAME"));
				fncTemp.setFncBsStyleId(rs.getString("FNC_BS_STYLE_ID"));
				fncTemp.setFncPlStyleId(rs.getString("FNC_PL_STYLE_ID"));
				fncTemp.setFncCfStyleId(rs.getString("FNC_CF_STYLE_ID"));
				fncTemp.setFncFiStyleId(rs.getString("FNC_FI_STYLE_ID"));
				fncTemp.setFncRiStyleId(rs.getString("FNC_RI_STYLE_ID"));
				fncTemp.setFncSmpStyleId(rs.getString("FNC_SMP_STYLE_ID"));
				fncTemp.setFncStyleId1(rs.getString("FNC_STYLE_ID1"));
				fncTemp.setFncStyleId2(rs.getString("FNC_STYLE_ID2"));
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fncTemp;
	}

}
