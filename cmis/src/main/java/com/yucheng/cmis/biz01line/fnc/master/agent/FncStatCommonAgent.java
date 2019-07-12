package com.yucheng.cmis.biz01line.fnc.master.agent;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.dao.ExportXLDao;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatCommonDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.RptItemData;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.AgentJDBCException;

/**
 *@Classname FncStaCommonAgent.java
 *@Version 1.0
 *@Since 1.0 Oct 10, 2008 10:18:01 AM
 *@Copyright yuchengtech
 *@Author Eric
 *@Description：具体的报表数据项的数据操作代理类
 *@Lastmodified
 *@Author
 */
public class FncStatCommonAgent extends CMISAgent {
	

	/**
	 * 新增一条报表记录项------------------(暂时未用)
	 * 
	 * @param prptItemData
	 *            报表项目信息
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentException
	 */
	public String addFncItemRecord(RptItemData prptItemData)
			throws AgentException {
		String flagInfo = CMISMessage.MESSAGEDEFAULT; // 信息标识（默认失败）

		// 得到连接
		Connection conn = this.getConnection();

		// 插入报表项目信息数据
		FncStatCommonDao fscDao = new FncStatCommonDao();

		fscDao.insertRptItemData(conn, prptItemData);

		return flagInfo;
	}

	/**
	 * 更新一条报表记录项
	 * 
	 * @param prptItemData
	 *            报表项目信息
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentException
	 */
	public String updateFncItemRecord(RptItemData prptItemData)
			throws AgentException {
		String flagInfo = CMISMessage.SUCCESS; // 信息标识（默认失败）

		// 得到连接
		Connection conn = this.getConnection();

		// 插入报表项目信息数据
		FncStatCommonDao fscDao = new FncStatCommonDao();
		fscDao.updateRptItemData(conn, prptItemData);

		return flagInfo;
	}

	/**
	 * 根据报表项数据删除整张报表数据
	 * 
	 * @param riData
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentException
	 */
	public String removeFncStatRecord(RptItemData riData) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）
		Connection conn = this.getConnection();

		// 更新这个数据项所在报表的所有数据项的数据为0.0
		FncStatCommonDao fscDao = new FncStatCommonDao();
		flagInfo = fscDao.updateFncStatByItem(conn, riData);

		return flagInfo;
	}

	/**
	 * 更新对公客户报表基本信息表的报表状态
	 * 
	 * @param cusId
	 *            客户ID
	 * @param statPrdStyle
	 *            报表周期类型
	 * @param statPrd
	 *            报表期间
	 * @param StateFlg
	 *            状态
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentException
	 */
	public String updateFncStatBaseInfo(String cusId, String statPrdStyle,
			String statPrd, String StateFlg,String statStyle,String fncType) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）

		// 获取连接
		Connection conn = this.getConnection();

		FncStatCommonDao fscDao = new FncStatCommonDao();
		flagInfo = fscDao.updateFncStatBaseInfo(conn, cusId, statPrdStyle,statPrd, StateFlg,statStyle,fncType);

		return flagInfo;
	}

	/**
	 * 根据客户ID,报表年份查询出所有的项目id
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cusId
	 *            客户ID
	 * @param statYear
	 *            报表年份
	 * @param tableName
	 *            表名
	 * @return 项目ID列表
	 * @throws AgentJDBCException
	 */
	public List getFncStatItemIdList(String cusId, String statYear,
			String tableName,String statStyle) throws AgentException {
		List itemIdList = null;
		Connection conn = this.getConnection();

		FncStatCommonDao fscDao = new FncStatCommonDao();
		itemIdList = fscDao.getFncStatItemIdList(conn, cusId, statYear,
				tableName,statStyle);

		return itemIdList;
	}

	/**
	 * 执行批量sql语句
	 * 
	 * @param sqlList
	 *            sql列表
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentException
	 */
	public String optBatchSql(List sqlList) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）

		// 获取连接
		Connection conn = this.getConnection();
		FncStatCommonDao fscDao = new FncStatCommonDao();
		flagInfo = fscDao.optBatchSql(conn, sqlList);

		return flagInfo;
	}

	/**
	 * 根据传入的参数条件取得一条报表基本信息
	 * 
	 * @param pfncStatBase
	 *            报表基本信息对象
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @return
	 * @throws EMPException 
	 */
	public FncStatBase queryDetailFncStatBase(FncStatBase pfncStatBase)
			throws EMPException {

		FncStatBase fncStatBase = null;

		// 获取连接
		Connection conn = this.getConnection();
		FncStatCommonDao fscDao = new FncStatCommonDao();

		fncStatBase = fscDao.queryDetailFncStatBase(pfncStatBase, conn);

		return fncStatBase;
	}

	/**
	 * 组装标签样式对象(带数据的)
	 * 
	 * @param 客户编号
	 *            cusId
	 * @param 报表周期类型
	 *            statPrdStyle (1:月报 2:季报 3:半年报 4:年报)
	 * @param 报表期间
	 *            statPrd 格式：YYYYMM
	 * @param 表名
	 *            tableName
	 * @param 报表种类
	 *            fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
	 * @return FncConfStyles
	 */
	public FncConfStyles queryDetailFncConfStyles(String cusId,
			String statPrdStyle, String statPrd, String styleId,
			String tableName, String fncConfTyp,String statStyle) throws AgentException {

		FncConfStyles fncConfStyles = null;
		FncStatCommonDao fDao = new FncStatCommonDao();
		fncConfStyles = fDao.queryDetailFncConfStyles(cusId, statPrdStyle,
				statPrd, styleId, this.getConnection(), tableName, fncConfTyp,statStyle);
		return fncConfStyles;
	}

	public List QueryItemsList(String cusId, String statPrdStyle,
			String statPrd, String styleId, String tableName, String fncConfTyp,String stat_style)
			throws AgentException {

		FncStatCommonDao fDao = new FncStatCommonDao();
		List itemList = fDao.QueryItemsList(cusId, statPrdStyle, statPrd,
				styleId, this.getConnection(), tableName, fncConfTyp,stat_style);

		return itemList;
	}

	/**
	 * 查看在tableName表中有没存在该条记录
	 * 
	 * @param tableName
	 * @param cusId
	 * @param stat_year
	 * @param stat_item_id
	 * @return int 有反回1,没有返回0
	 * @throws AgentException
	 */
	public int queryOneFncConfStyles(String tableName, String cusId,
			String stat_year, String stat_item_id,String stat_style) {
		FncStatCommonDao fDao = new FncStatCommonDao();
		int count = fDao.queryOneFncConfStyles(tableName, cusId, stat_year,
				stat_item_id, this.getConnection(),stat_style);
		return count;
	}
	/**
	 * 新增一条数据
	 * @param tableName
	 * @param cusId
	 * @param stat_year
	 * @param stat_item_id
	 * @return
	 */
	public int addOneFncConfStyles(String sql) {
		FncStatCommonDao fDao = new FncStatCommonDao();
		int count = fDao.addOneFncConfStyles(sql, this.getConnection());
		return count;
	}
	
	/**
	 * 查看数据库中有没有客户对应的报表信息
	 * @param tableName
	 * @param cusId
	 * @param stat_year
	 * @return
	 */
	public int findOneFncConfStyles(String tableName, String cusId,
			String stat_year,String statStyle) {
		FncStatCommonDao fDao = new FncStatCommonDao();
		int count = fDao.findOneFncConfStyles(tableName, cusId, stat_year,
				this.getConnection(),statStyle);
		return count;
	}


	/**
	 * 组装标签样式对象(带数据的)
	 * 
	 * @param 客户编号
	 *            cusId
	 * @param 报表周期类型
	 *            statPrdStyle (1:月报 2:季报 3:半年报 4:年报)
	 * @param 报表期间
	 *            statPrd 格式：YYYYMM
	 * @param 表名
	 *            tableName
	 * @param 报表种类
	 *            fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
	 * @param Connection
	 * @return FncConfStyles
	 */
	public FncConfStyles getFncConfStyles(String cusId, String statPrdStyle,
			String statPrd, String styleId, String tableName, String fncConfTyp,String statStyle)
			throws AgentException {
		FncConfStyles fncConfStyles = null;
		FncStatCommonDao fDao = new FncStatCommonDao();
		fncConfStyles = fDao.queryDetailFncConfStyles(cusId, statPrdStyle,
				statPrd, styleId, this.getConnection(), tableName, fncConfTyp,statStyle);
		return fncConfStyles;
	}

	public FncStatBase getOneFncStatBase(FncStatBase fncStatBase,
			Connection conn) {
		FncStatBase tempBase = null;
		FncStatCommonDao fscDao = new FncStatCommonDao();
		tempBase = fscDao.getOneFncStatBase(fncStatBase, conn);
		return tempBase;
	}
	
	public FncConfStyles getData(FncStatBase tempBase,Connection conn,String styleType,String tableName)throws AgentException{
		FncConfStyles temp = null;
		FncStatCommonDao fscDao = new FncStatCommonDao();
		temp = fscDao.getData(tempBase,conn,styleType,tableName);
		return temp;
	}

	public String updateFncStatFlg(String tempState, Connection conn,
			FncStatBase tempBase) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 默认失败
		FncStatCommonDao fscDao = new FncStatCommonDao();
		try {
			flagInfo = fscDao.updateFncStatFlg(tempState, conn, tempBase);
		} catch (SQLException e) {
			throw new AgentException(e.getMessage());
		}
		return flagInfo;
	}
	
	/**
	   * 以下是财报导入导出使用
	   */
	  /**
	   * 根据客户编号获取客户的财务报表类型
	   */
	  public String getComFinRepType(String cusId,Connection connection)throws AgentException{
			String repType = "";
			FncStatCommonDao fscDao = new FncStatCommonDao();
			repType = fscDao.getComFinRepType(cusId,connection);
			return repType;
	  }
	  
	  public int queryCount(String styleId, int p,Connection connection){
		  int count = 0;
		  ExportXLDao exportXLDao = new ExportXLDao();
		  count = exportXLDao.queryCount(styleId, p, connection);
		  return count;
	  }

	  public FncIndexRpt getIndexValue(String cusId,String itemId,String statStyle,Connection conn){
		  FncIndexRpt fncIndexRpt = null;
		  FncStatCommonDao fscDao = new FncStatCommonDao();
		  fncIndexRpt = fscDao.getIndexValue(cusId,itemId,statStyle,conn);
		  return fncIndexRpt;
	  }
	  /**
	   * 统计财报数目
	   * @param cusId
	   * @param statPrdStyle
	   * @param statStyle
	   * @param conn
	   * @return
	   * @throws AgentException
	   */
	  public HashMap<String, String> queryCountFncStatBase(String cusId,String statPrdStyle,String statStyle,Connection conn)throws AgentException{
		  HashMap<String, String> rq=new HashMap<String, String>();
		  FncStatCommonDao fscDao = new FncStatCommonDao();
		  rq = fscDao.queryCountFncStatBase(cusId,statPrdStyle,statStyle,conn);
		  return rq;
	  }
}
