package com.yucheng.cmis.biz01line.iqp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class CatalogManaDao extends CMISDao{
	/**
	 * 根据表名和上级目录ID获得该目录下的子目录
	 * @param tableName 表名
	 * @param parentId 上级目录
	 * @param value 是否含价商品 N--表示 不包含含价商品
	 * @return IndexedCollection
	 * @throws DaoException
	 */
	public IndexedCollection getCatalogICollByParentId(String tableName, String parentId,String serno,String value) throws DaoException{
		IndexedCollection iColl = new IndexedCollection();
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "";
			if(parentId == null || parentId.equals("")){
				/** 获取上一目录为空则表示为最顶级目录 */
				sql = "SELECT * FROM "+tableName+" WHERE SUP_CATALOG_NO IS NULL AND STATUS='1'";
			} else if(value!=null&&"N".equals(value)){
				/** 根据上级目录获得上级目录下的子目录 */
				sql = "SELECT * FROM "+tableName+" WHERE SUP_CATALOG_NO= '"+ parentId+"' AND STATUS='1' and attr_type='01'";  //生效和非含价的
			}else{
				sql = "SELECT * FROM "+tableName+" WHERE SUP_CATALOG_NO= '"+ parentId+"' AND STATUS='1'";  //生效的
			}
			//押品目录准入预览押品类型树
			if(serno==null||serno=="null"||"null".equals(serno)){
				
			}else {
				if(parentId == null || parentId.equals("")){
					/** 获取上一目录为空则表示为最顶级目录 */
					sql = "select * from ( select catalog_no,catalog_name,catalog_path,sup_catalog_no from iqp_mort_catalog_mana where status = '1' union"+
					" select catalog_no,catalog_name,catalog_path,sup_catalog_no from iqp_app_mort_detail where serno='"+serno+"') where SUP_CATALOG_NO IS NULL";
				} else {
					/** 根据上级目录获得上级目录下的子目录 */
					sql = "select * from ( select catalog_no,catalog_name,catalog_path,sup_catalog_no from iqp_mort_catalog_mana where status = '1' union"+
					" select catalog_no,catalog_name,catalog_path,sup_catalog_no from iqp_app_mort_detail where serno='"+serno+"') where SUP_CATALOG_NO= '"+ parentId+"'";
				}
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				KeyedCollection kColl = new KeyedCollection("catalogTree");
				String catalogid = (String)rs.getString("catalog_no");
				String catalogname = (String)rs.getString("catalog_name");
				String cataloglevel = (String)rs.getString("catalog_path");
				String supcatalogid = (String)rs.getString("sup_catalog_no");
				
				kColl.addDataField("catalog_no", catalogid);
				kColl.addDataField("catalog_name", catalogname);
				kColl.addDataField("catalog_path", cataloglevel);
				kColl.addDataField("sup_catalog_no", supcatalogid);
				iColl.add(kColl);
			}
		} catch (Exception e) {
			throw new DaoException(" 根据表名和上级目录ID获得该目录下的子目录出错，错误描述："+e.getMessage());
		} finally {
			if(rs != null){
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return iColl;
	}
	
	/**
	 * 根据目录编号查询该目录下子目录数量
	 * @param catalog_no 上一目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchCatalogManaBySupCatalogNo(String catalog_no) throws DaoException {
		BigDecimal res = null;
		try {
			res = new BigDecimal(SqlClient.queryFirst("searchCatalogManaBySupCatalogNo", catalog_no, null, this.getConnection()).toString());
		} catch (Exception e) {
			throw new DaoException("根据节点编号查询该节点下子目录数量错误，错误描述："+e.getMessage());
		}
		return res;
	}
	/**
	 * 根据目录编号查询该目录下子目录数量（只包含非含价商品）
	 * @param catalog_no 上一目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchCatalogManaNoValueBySupCatalogNo(String catalog_no) throws DaoException {
		BigDecimal res = null;
		try {
			res = new BigDecimal(SqlClient.queryFirst("searchCatalogManaNoValueBySupCatalogNo", catalog_no, null, this.getConnection()).toString());
		} catch (Exception e) {
			throw new DaoException("根据节点编号查询该节点下子目录数量错误，错误描述："+e.getMessage());
		}
		return res;
	}
	/**
	 * 根据目录编号查询该目录下子目录数量(押品目录准入时)
	 * @param catalog_no 上一目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchAppCatalogManaBySupCatalogNo(String catalog_no,String serno) throws DaoException {
		BigDecimal res = null;
		try {
			PreparedStatement pstmt = null;
			Connection conn = this.getConnection();
			ResultSet rs = null;
			String sql="select count(1) as counts from (select catalog_no, catalog_name, catalog_path, sup_catalog_no from iqp_mort_catalog_mana where status = '1' union select catalog_no, catalog_name, catalog_path, sup_catalog_no from iqp_app_mort_detail where serno =" +
					"'"+serno+"'" +
					")where SUP_CATALOG_NO = '"+catalog_no+"'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				res = new BigDecimal((String)rs.getString("counts"));
			}
		} catch (Exception e) {
			throw new DaoException("根据节点编号查询该节点下子目录数量错误，错误描述："+e.getMessage());
		}
		return res;
	}
	/**
	 * 通过目录ID取得最大的下一个目录ID
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNo(String catalog_no) throws DaoException {
		String res = "";
		try {
			Object obj = SqlClient.queryFirst("searchMaxCatalogNo", catalog_no, null, this.getConnection());
			if(null!=obj){  //返回值不为空时
				res = obj.toString();
			}
		} catch (Exception e) {
			throw new DaoException("通过目录ID取得最大的下一个目录ID错误，错误描述："+e.getMessage());
		}
		return res;
	}
	/**
	 * 通过目录ID取得最大的下一个目录ID（准入）
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNoDetail(String catalog_no) throws DaoException {
		String res = "";
		try {
			Object obj = SqlClient.queryFirst("searchMaxCatalogNoDetail", catalog_no, null, this.getConnection());
			if(null!=obj){  //返回值不为空时
				res = obj.toString();
			}
		} catch (Exception e) {
			throw new DaoException("通过目录ID取得最大的下一个目录ID错误，错误描述："+e.getMessage());
		}
		return res;
	}
	/**
	 * 修改一级目录时更新树形字典的押品类别的货物质押对应的名称
	 * @param catalog_no 目录编号
	 * @param updateValue 修改后的值
	 * @return 成功记录数
	 */
	public int updateTreeDicByCatalogNo(String catalog_no,String updateValue) throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateTreeDicByCatalogNo", catalog_no, updateValue, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("修改一级目录["+catalog_no+"]时更新树形字典的押品类别的货物质押对应的名称出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	
	/**
	 *  删除一级目录时同步删除树形字典的押品类别项下货物质押对应数据
	 * @param catalog_no 目录编号
	 * @return 成功记录数
	 */
	public int deleteTreeDicByCatalogNo(String catalog_no) throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("deleteTreeDicByCatalogNo", catalog_no, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("删除一级目录["+catalog_no+"]时同步删除树形字典的押品类别项下货物质押对应数据出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	
	/**
	 * 根据目录编号查询该目录下价格信息数量 
	 * @param catalog_no 目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchValueManaByCatalogNo(String catalog_no) throws DaoException {
		BigDecimal res = null;
		try {
			res = new BigDecimal(SqlClient.queryFirst("searchValueManaByCatalogNo", catalog_no, null, this.getConnection()).toString());
		} catch (Exception e) {
			throw new DaoException("根据目录编号查询该目录下价格信息数量 错误，错误描述："+e.getMessage());
		}
		return res;
	}
	
	/**
	 * 根据目录编号、状态值，修改押品目录的状态
	 * @param catalog_no 目录编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateCatalogManaStatus(String catalog_no,String status) throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateCatalogManaStatus", catalog_no, status, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据目录编号、状态值，修改押品目录的状态出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	* 根据押品价格编号、状态值，修改押品价格的状态
	 * @param value_no 押品价格编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateValueManaStatus(String value_no,String status) throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateValueManaStatus", value_no, status, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品价格编号、状态值，修改押品目录的状态出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 展期专用执行sql方法
	 * @param type  执行sql类型
	 * @param kcoll  传值kcoll
	 * @return 返回值
	 */
	public KeyedCollection excuteSql(String type, KeyedCollection kcoll) throws DaoException {
		int count=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			if(type.equals("IqpExtensionApp")){//贷款展期申请流程后处理
				String serno = kcoll.getDataValue("serno").toString();
				String agr_no = kcoll.getDataValue("agr_no").toString();
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("serno", serno);
				paramMap.put("agr_no", agr_no);
				count = SqlClient.executeUpd("addIqpExtensionAgr", serno , paramMap, null, this.getConnection());
				kcoll.addDataField("count", count);
			}else if(type.equals("ExtensionAppCheck")){
				String bill_no = kcoll.getDataValue("value").toString();
				Object obj = SqlClient.queryFirst("ExtensionAppCheck", bill_no, null, this.getConnection());
				kcoll.addDataField("result", obj);
			}else if(type.equals("ExtensionAgrCheck")){
				String agr_no = kcoll.getDataValue("value").toString();
				Object obj = SqlClient.queryFirst("ExtensionAgrCheck", agr_no, null, this.getConnection());
				kcoll.addDataField("result", obj);
			}else if(type.equals("getSnByHt")){
				String agr_no = kcoll.getDataValue("value").toString();
				Object obj = SqlClient.queryFirst("getSnByHt", agr_no, null, this.getConnection());
				kcoll.addDataField("result", obj);
			}else if (type.equals("updateIqpExtensionPvp")) {
				sql = kcoll.getDataValue("sql").toString();
				pstmt = this.getConnection().prepareStatement(sql);
				pstmt.executeUpdate();
			}else if (type.equals("selectIqpExtensionPvp")) {
				sql = kcoll.getDataValue("sql").toString();
				pstmt = this.getConnection().prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()){
					sql = (String)rs.getString("status");
				}
				kcoll.addDataField("result", sql);
			}else if (type.equals("selectIqpExtensionAgr")) {
				/* added by yangzy 2014/10/23 影像系统锁定接口_XD141014067 start */
				sql = kcoll.getDataValue("sql").toString();
				pstmt = this.getConnection().prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()){
					sql = (String)rs.getString("serno");
				}
				kcoll.addDataField("result", sql);
				/* added by yangzy 2014/10/23 影像系统锁定接口_XD141014067 end */
			}else if(type.equals("DelManageInfo")){ // 不良资产转移，根据合同编号更新主管客户经理与主管机构
				String bill_no = kcoll.getDataValue("bill_no").toString(); //借据编号
				String cont_no = kcoll.getDataValue("cont_no").toString(); //合同编号
				String manager_id = kcoll.getDataValue("manager_id").toString(); //接收人员person
				String manager_br_id = kcoll.getDataValue("manager_br_id").toString(); //接收机构
			
				SqlClient.executeUpd("DelManageInfoPerson", cont_no , manager_id, null, this.getConnection());
				SqlClient.executeUpd("DelManageInfoOrg", cont_no , manager_br_id, null, this.getConnection());
				SqlClient.executeUpd("DelManageInfoOrgBill", bill_no , manager_br_id, null, this.getConnection());
			}else if(type.equals("CheckExtensionDate")){
				String bill_no = kcoll.getDataValue("bill_no").toString();
				kcoll = (KeyedCollection) SqlClient.queryFirst("CheckExtensionDate", bill_no, null, this.getConnection());
			}
		} catch (Exception e) {
			throw new DaoException("执行sql出错，错误原因："+e.getMessage());
		}
		return kcoll;
	}
	/**
	 * 将押品准入明细表记录插入押品目录信息表中
	 * @param serno 业务流水号
	 */
	public int createIqpMortCatalogManaRecord(String serno)throws DaoException {
		int count = 0;
		try {
			count = SqlClient.executeUpd("createIqpMortCatalogManaRecord",serno, null,null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("押品准入明细表记录赋值到押品目录信息表出错，错误原因："+e.getMessage());
		}
		return count;
	}
}
