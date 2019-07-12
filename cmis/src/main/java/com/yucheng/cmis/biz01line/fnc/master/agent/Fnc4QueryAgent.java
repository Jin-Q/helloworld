package com.yucheng.cmis.biz01line.fnc.master.agent;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatCommonDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.Fnc4Query;
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
 * 
 * @Classname com.yucheng.cmis.fnc.master.agent.Fnc4QueryAgent.java
 * @author wqgang
 * @Since 2009-4-9 下午03:36:25 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class Fnc4QueryAgent extends CMISAgent {
	
	public double QueryItemValue(Fnc4Query fq){
		double rv = 0;
		FncStatCommonDao dao = new FncStatCommonDao();
		Connection conn=this.getConnection();
		//String fncType=fq.getFncType();
		/*try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.100.2.129:1521:CMIS", "cmis", "cmis");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
         catch (SQLException e) {
		
			e.printStackTrace();
		}*/
	 		
		rv=dao.QueryItemValue(fq, conn);
		return rv;
	}
	
	
	
	/**
	 * 
	 * @param pfncStatBase 公司客户报表对象
	 * @return      flagInfo 信息编码
	 * @throws AgentException
	 */
	public String addRecord(FncStatBase pfncStatBase) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncStatBase,
				PUBConstant.FNCSTATBASE); // 1成功  其他失败

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
		int count = this.modifyCMISDomain(pfncStatBase,
				PUBConstant.FNCSTATBASE);// 1成功  其他失败

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
	public FncStatBase queryDetail(String cusId,String statPrdStyle,String statPrd) throws AgentException {

		FncStatBase pfncStatBase = new FncStatBase();
		
         //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("stat_prd_style", statPrdStyle);
		pk_values.put("stat_prd", statPrd);

		//进行查询操作
		pfncStatBase = (FncStatBase) this.findCMISDomainByKeywords(
				pfncStatBase, PUBConstant.FNCSTATBASE, pk_values);

		return pfncStatBase;
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
	
	public String findCusFinById(String cus_id,Connection conn){
		String cus_fin_type = null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt = conn.createStatement();
			String sql = "select COM_FIN_REP_TYPE " +
					"from CUS_COM where CUS_ID='"+cus_id+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cus_fin_type = rs.getString("COM_FIN_REP_TYPE");
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
		return cus_fin_type;
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

	public ArrayList<String> getItemIdFromDef(FncConfTemplate fncTemp ,ArrayList<String> srcList) throws AgentException{
		
		if(fncTemp == null || srcList == null || srcList.size() == 0)return new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		sb.append("select fnc_conf_items.item_id,fnc_conf_items.def_item_id from fnc_conf_items , fnc_conf_def_fmt  where fnc_conf_items.item_id=fnc_conf_def_fmt.item_id");
		sb.append(" and fnc_conf_items.def_item_id in (");
		for(String tmp :srcList){
			i++ ;
			if(!StringUtils.isBlank(tmp)){
				sb.append("'").append(tmp).append("'");
			}else{
				continue;
			}
			
			if(i != srcList.size()){
				sb.append(",");
			}
		}
		sb.append(")");
		
		sb.append(" and fnc_conf_def_fmt.style_id in (");
		
		if(!StringUtils.isBlank(fncTemp.getFncBsStyleId())){// 资产样式编号
			sb.append("'").append(fncTemp.getFncBsStyleId()).append("',");
		}
		
		if(!StringUtils.isBlank(fncTemp.getFncPlStyleId())){// 损益表编号
			sb.append("'").append(fncTemp.getFncPlStyleId()).append("',");
		}
        
		if(!StringUtils.isBlank(fncTemp.getFncCfStyleId())){// 现金流量表编号
			sb.append("'").append(fncTemp.getFncCfStyleId()).append("',");
		}
		
		if(!StringUtils.isBlank(fncTemp.getFncFiStyleId())){// 财务指标表编号
			sb.append("'").append(fncTemp.getFncFiStyleId()).append("',");
		}
		
		if(!StringUtils.isBlank(fncTemp.getFncRiStyleId())){// 所有者权益变动表编号
			sb.append("'").append(fncTemp.getFncRiStyleId()).append("',");
		}
		
		if(!StringUtils.isBlank(fncTemp.getFncSmpStyleId())){// 财务简表编号
			sb.append("'").append(fncTemp.getFncSmpStyleId()).append("',");
		}
		//去除最后的  , 
		int idx = sb.lastIndexOf(",");
		if(idx == -1){ //如果 就没有, 虚拟一个  ''否则sql语句报错
			sb.append("''");
		}else{
			sb.deleteCharAt(idx);
		}
				
        sb.append(")");
		
        
        sb.insert(0, " ");
        
        
        //查询转换
        Statement stmt=null;
		ResultSet rs=null;
		
		//复制一份，主要是用来 和以前的排序一样  ，找到一样的替换，没找到不管
		ArrayList<String> result = (ArrayList<String>)srcList.clone();
		try {
			stmt = this.getConnection().createStatement();
		    //System.err.println(sb.toString());
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "执行的sql语句为："+sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				//取到获取的 映射条目 id
				/**
				 * 如果报数据下标越界 说明一配置错误
				 * 目标条目号  在 实际该客户报表的条目中 有两条映射，修改配置就解决这个问题
				 * 
				 * 配置的条目如果 该报表没有条目 却显示不出来，那么可以 把 映射的条目编号。在 fnc_conf_items 中增加 条目id  为 def_item_id 的数据
				 */
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "获取的条目id为"+rs.getString(2));
				int curIdx  = result.lastIndexOf(rs.getString(2));
				result.remove(curIdx);
				result.add(curIdx,rs.getString(1));
			}
			
		}  catch (SQLException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "",e);
			throw new AgentException(e);
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
        
		
        return result;
	}
}
