package com.yucheng.cmis.biz01line.mort.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * CMISDao层不直接写数据库操作，通过调用命名SQL实现数据库操作
 * @author Xiaod
 * @create 20130806
 */
public class MortCommenOwnerDao extends CMISDao {

	/**
	 * 押品管理模块
	 * @param guarantyNo 押品编号
	 * @return
	 * @throws ComponentException
	 */
	public IndexedCollection getCommenList(String guarantyNo) throws DaoException {
		IndexedCollection iColl = new IndexedCollection();
		try {
			IndexedCollection iCollResult = SqlClient.queryList4IColl("queryCommenList", guarantyNo, this.getConnection());
			for(int i=0;i<iCollResult.size();i++){
				KeyedCollection returnKColl = new KeyedCollection("MortCommenOwner");
				KeyedCollection kColl = (KeyedCollection)iCollResult.get(i);
				returnKColl.put("commen_owner_no", kColl.getDataValue("cus_id"));
				returnKColl.put("commen_owner_no_displayname", kColl.getDataValue("cus_name"));
				returnKColl.put("cus_type", kColl.getDataValue("cus_type"));
				returnKColl.put("cert_type",kColl.getDataValue("cert_type"));
				returnKColl.put("cert_code",kColl.getDataValue("cert_code"));
				iColl.add(returnKColl);
			}
		} catch (Exception e) {
			throw new DaoException("根据押品编号关联查询权证信息表获取主权证信息失败，失败原因："+e.getMessage());
		}
		return iColl;
	}
	/**
	 * 押品管理模块 
	 * 根据押品编号查权证信息表获取主权证信息
	 * @param guarantyNo 押品编号
	 * @return
	 * @throws ComponentException
	 */
	public KeyedCollection getMortGuarantyCertiInfoDetail(String guarantyNo) throws DaoException{
		KeyedCollection rekColl = new KeyedCollection();
		try {
			IndexedCollection iCollResult = SqlClient.queryList4IColl("queryMortGuarantyCertiInfoDetail", guarantyNo, this.getConnection());
			if(null!=iCollResult && iCollResult.size()>0){  //权证为空时返回空对象，否决会抛异常
				rekColl = (KeyedCollection) iCollResult.get(0);	
			}
		} catch (Exception e) {
			throw new DaoException("根据押品编号关联查询权证信息表获取主权证信息失败，失败原因："+e.getMessage());
		}
		return rekColl;
	}
	/**
	 * 押品管理模块级联删除未生效的押品记录及其关联信息
	 * @param guarantyNo 押品编号
	 * @return 
	 * @throws ComponentException
	 */
	public int deleteAllByGuarantyNo(String guarantyNo ) throws DaoException {
		int count = 0;
		try {
			count+=SqlClient.executeUpd("deleteMortInsurRecord",guarantyNo,null,null,this.getConnection());
			count+=SqlClient.executeUpd("deleteMortSuddenRecord",guarantyNo,null,null,this.getConnection());
			count+=SqlClient.executeUpd("deleteMortEvalValueRecord",guarantyNo,null,null,this.getConnection());
			count+=SqlClient.executeUpd("deleteMortCertiRecord",guarantyNo,null,null,this.getConnection());
			count+=SqlClient.executeUpd("deleteMortCommenRecord",guarantyNo,null,null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号关联删除相关押品信息失败，失败原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 根据押品编号关联删除货物与监管协议关系记录（货物登记模块）
	 * @param guarantyNo 押品编号
	 * @return 
	 * @throws ComponentException
	 */
	public int deleteCarOverReByGuarantyNo(String guarantyNo ) throws DaoException {
		int count = 0;
		try {
			count+=SqlClient.executeUpd("deleteCarOverReRecord",guarantyNo,null,null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号关联删除货物与监管协议关系记录失败，失败原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 押品管理模块（担保合同关联下的担保品列表）
	 * @param guarantyContNo 押品编号
	 * @return
	 * @throws ComponentException
	 */
	public IndexedCollection queryMortTabList(String guarantyContNo) throws DaoException{
		IndexedCollection iCollResult = null;
		try {
		 iCollResult = SqlClient.queryList4IColl("queryMortTabList", guarantyContNo, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("查询抵质押品与担保合同的关联信息表失败，失败原因："+e.getMessage());
		}
		return iCollResult;
	}
	
	/**
	 * 货物登记模块（商链通）对货物与监管协议关系的新增
	 * @param kc 需要插入数据库的键值对
	 * @return
	 * @throws ComponentException
	 */
	public int insertIqpCargoOverseeReRecord(KeyedCollection kc) throws DaoException{
		int result = 0;
		try {
			Map<String, String> insertMap = new HashedMap();
			insertMap.put("agr_type",(String)kc.getDataValue("agr_type"));
			insertMap.put("agr_no",(String)kc.getDataValue("agr_no"));
			insertMap.put("guaranty_no",(String)kc.getDataValue("guaranty_no"));
		 result = SqlClient.insert("insertIqpCargoOverseeReRecord",insertMap, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("货物与监管协议关系记录新增失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 货物登记模块（商链通）根据押品编号查询获取监管协议与货物的关系表获取监管协议编号
	 * @param guarantyNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryCarOverReRecordDetail(String guarantyNo) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryCarOverReRecordDetail", guarantyNo, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号查询获取监管协议与货物的关系表获取监管协议编号失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	/**
	 * 根据押品编号获取押品下关联的货物库存总价值（商链通）
	 * @param guarantyContNo 押品编号
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryIdentyTotalInfo(String guarantyContNo,String status) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			Map<String, String> insertMap = new HashedMap();
			insertMap.put("guaranty_no", guarantyContNo);
			insertMap.put("cargo_status", status);
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryIdentyTotalInfo", insertMap, null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	
	/**
	 * 根据押品编号获取押品下关联的货物库存总价值（商链通）
	 * @param guarantyContNo 押品编号
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryCargoReplListTotalInfo(String serno,String status) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			Map<String, String> insertMap = new HashMap<String, String>();
			insertMap.put("serno", serno);
			insertMap.put("cargo_status", status);
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryCargoReplListTotalInfo", insertMap, null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	
	/**
	 * 批量更改押品关联下货物的状态（商链通）
	 * @param ic 需要做状态修改的货物编号集
	 * @param status 状态01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return
	 * @throws DaoException 
	 * @throws ComponentException
	 */
	public void updateStatusBatchCheck(String status,IndexedCollection iColl,Connection conn) throws DaoException{
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "update Mort_Cargo_Pledge a set a.cargo_status ='"+status+"' where a.cargo_id='"+kCollTmp.getDataValue("cargo_id")+"'";
				ps.addBatch(sql);
			}
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new DaoException(" 批量更改押品关联下货物的状态信息失败，失败原因："+e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 批量更改押品关联下货物的状态（货物出入库清单表）
	 * @param ic 需要做状态修改的货物编号集
	 * @param status 状态01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return
	 * @throws DaoException 
	 * @throws ComponentException
	 */
	public void updateStatusBatchCheckRepl(String status,IndexedCollection iColl,Connection conn) throws DaoException{
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "update MORT_CARGO_REPL_LIST a set a.cargo_status ='"+status+"' where a.cargo_id='"+kCollTmp.getDataValue("cargo_id")+"'";
				ps.addBatch(sql);
			}
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new DaoException(" 批量更改押品关联下货物出入库清单表中的状态信息失败，失败原因："+e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 *更改非登记状态的押品状态（商链通）
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int updateStatusBatch(String bfStatus,String status,String guarantyNo,Context context) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", bfStatus);
			insertKc.put("guaranty_no", guarantyNo);
			KeyedCollection valueKc = new KeyedCollection();
			valueKc.put("cargoStatus", status);
			if("02".equals(status)){
				//valueKc.put("storageDate", "");
				//valueKc.setDataValue("storageDate",context.getDataValue("OPENDAY"));
				valueKc.put("storageDate",context.getDataValue("OPENDAY"));
				result = SqlClient.executeUpd("updateStatusStorageBatch",insertKc ,valueKc, null, this.getConnection());
			}else if("03".equals(status)){
				//valueKc.put("exwareDate", "");
				//valueKc.setDataValue("exwareDate",context.getDataValue("OPENDAY"));
				valueKc.put("exwareDate",context.getDataValue("OPENDAY"));  //
				result = SqlClient.executeUpd("updateStatusExwareDateBatch",insertKc ,valueKc, null, this.getConnection());
			}else{
				result = SqlClient.executeUpd("updateStatusExwareDateBatch",insertKc ,valueKc, null, this.getConnection());
			}
		} catch (Exception e) {
			throw new DaoException("更改非登记状态的押品状态失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 根据押品编号查询入库管理表中处于登记 状态的记录
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryRegiRecord(String guarantyNo) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryRegiRecord", guarantyNo, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号查询入库管理表中处于登记 状态的记录失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	/**
	 * 根据押品编号查询出库管理表中处于登记 状态的记录
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryRegiOutStorRecord(String guarantyNo) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryRegiOutStorRecord", guarantyNo, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号查询入库管理表中处于登记 状态的记录失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	
	/**
	 *  获取押品记录（pop）
	 * @param conStr 查询条件（没有查询查询条件是 conStr 为1=1）
	 * @return res_value 
	 * @throws ComponentException
	 */
	public IndexedCollection getGuarantyInfoList(String conStr,PageInfo pageInfo,DataSource dataSource) throws DaoException{
		IndexedCollection res_value = null;
		String sql_select ="";
		try {
			if("".equals(conStr)){
			   sql_select =SqlClient.joinQuerySql("queryGuarantyRecordPop","1=1",null);
			}else{
				sql_select =SqlClient.joinQuerySql("queryGuarantyRecordPop",conStr,null);
			}
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		} catch (Exception e) {
			throw new DaoException("获取押品记录失败，失败原因："+e.getMessage());
		}
		return res_value;
	}
	
	/**
	 * 根据押品编号查询处于登记状态的提货清单记录，根据返回的状态值判断需要做的操作是新增操作或者是修改操作
	 * @param guarantyContNo 押品编号 cargoId 货物编号,op_type 操作类型（1--货物置换，2--保证金提货，3--货物出库）
	 * @return resultKc (result true--新增操作，false--修改操作 )
	 * @throws ComponentException
	 */
	public KeyedCollection queryMortDelivList(String guarantyNo,String cargoId,String op_type) throws DaoException{
		KeyedCollection paraKc = new KeyedCollection();
		KeyedCollection resultKc = new KeyedCollection();
		String result;
		IndexedCollection resultIc = null;
		try {
			paraKc.put("guaranty_no", guarantyNo);
			paraKc.put("op_type", op_type);
			resultIc = (IndexedCollection) SqlClient.queryList4IColl("queryMortDelivList", paraKc, this.getConnection());
			if(resultIc.size()==0){//提货清单里面没有相关押品下的提货记录，此为新增操作
				result = "true";
				resultKc.put("result", result);
				resultKc.put("serno","");
			}else{
				int count = 0;
				for(int i=0;i<resultIc.size();i++){
					KeyedCollection kc = (KeyedCollection) resultIc.get(i);
					if(cargoId.equals(kc.getDataValue("cargo_id")))//存在时，修改操作
					{
						result="false";
						resultKc.put("serno",kc.getDataValue("serno"));
						resultKc.put("result",result);
						break;
					}else{
						count++;
					}
				}
				if(count==resultIc.size()){//不存在时，新增操作
					result = "true";
					resultKc.put("result", result);
					resultKc.put("serno","");
				}
			}
		} catch (Exception e) {
			throw new DaoException("根据押品编号查询处于登记状态的提货清单记录失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	/**
	 *根据提货流水将处于出库待记账状态的提货清单记录更新为出库状态
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 05--出库待记账 03--出库
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortDelivList(String bfStatus,String status,String serno) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", bfStatus);
			insertKc.put("serno", serno);
			result = SqlClient.executeUpd("updateMortDelivList",insertKc ,status, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("更改保证金提货清单的货物状态失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 *根据保证金提货流水更新其货物的在库数量和在库总价值
	 * @param serno 流水
	 * @return  
	 * @throws ComponentException
	 */
	public int updatemortCargoPledge(String serno) throws DaoException{
		int result = 0;
		try {
			result = SqlClient.executeUpd("updatemortCargoPledge",serno ,null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号更新其货物的在库数量和在库总价值失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 *批量更改押品关联下货物的状态（商链通）货物置换清单 
	 * @param status 货物状态 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public void updateStatusBatchRepl(String status,IndexedCollection iColl,Connection conn) throws DaoException{
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "update mort_cargo_repl_list a set a.cargo_status ='"+status+"' where a.cargo_id='"+kCollTmp.getDataValue("cargo_id")+"'";
				ps.addBatch(sql);
			}
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new DaoException(" 批量更改押品关联下货物的状态（货物置换清单）失败，失败原因："+e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 根据押品编号获取押品下关联的货物库存总价值（货物置换清单）
	 * @param guarantyContNo 押品编号
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @param oper 操作类型 1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryIdentyTotalInfoRepl(String guarantyContNo,String status,String oper) throws DaoException{
		KeyedCollection resultKc = null;
		try {
			Map<String, String> insertMap = new HashedMap();
			insertMap.put("guaranty_no", guarantyContNo);
			insertMap.put("cargo_status", status);
			insertMap.put("oper", oper);
			resultKc = (KeyedCollection) SqlClient.queryFirst("queryIdentyTotalInfoRepl", insertMap, null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
		return resultKc;
	}
	/**
	 * 批量更改押品关联下货物的状态（货物质押清单）
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @param serno 业务编号 
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int updateStatusBatchCargoRepl(String bfStatus,String status,String guarantyNo,String serno) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", bfStatus);
			insertKc.put("guaranty_no", guarantyNo);
			insertKc.put("serno", serno);
			result = SqlClient.executeUpd("updateStatusBatchCargoRepl",insertKc ,status, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("批量更改押品关联下货物的状态（货物质押清单）失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 货物置换时，新增货物记录
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertmortCargoPledge(String status,String guarantyNo) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", status);
			insertKc.put("guaranty_no", guarantyNo);
			result = SqlClient.executeUpd("insertmortCargoPledge",insertKc ,null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("货物置换时，新增货物记录失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 货物置换时，新增货物记录
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertmortCargoPledge4ZH(String status,String serno) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", status);
			insertKc.put("serno", serno);
			result = SqlClient.executeUpd("insertmortCargoPledge4ZH",insertKc ,null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("货物置换时，新增货物记录失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 货物置换时，新增提货记录
	 * @param status 货物状态 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertMortDelivList(String status,String guarantyNo) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", status);
			insertKc.put("guaranty_no", guarantyNo);
			result = SqlClient.executeUpd("insertMortDelivList",insertKc ,null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("货物置换时，新增提货记录失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 货物动态出入库时，向提货清单表中存入货物的历史数据记录
	 * @param oper 操作类型（1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货）
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertMortDelivListByStatus(String serno,String oper,String status,String guarantyNo) throws DaoException{
		int result = 0;
		try {
			KeyedCollection insertKc = new KeyedCollection();
			insertKc.put("cargo_status", status);
			insertKc.put("guaranty_no", guarantyNo);
			
			KeyedCollection valueKc = new KeyedCollection();
			valueKc.put("serno",serno);
			valueKc.put("oper",oper);
			
			Connection conn = this.getConnection() ;
			result = SqlClient.executeUpd("insertMortDelivListByStatus",insertKc,valueKc, null, conn);
		} catch (Exception e) {
			throw new DaoException("货物动态出入库时，向提货清单表中存入货物的历史数据记录失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 删除保证金提货流水记录的同时删除提货清单中的数据（保证金流水只有登记状态时，可以进行删除）
	 * @param guarantyNo 押品编号
	 * @return 
	 * @throws ComponentException
	 */
	public int deleteMortDelivListByGuarantyNo(String guarantyNo ) throws DaoException {
		int count = 0;
		try {
			count=SqlClient.executeUpd("deleteMortDelivListByGuarantyNo",guarantyNo,null,null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据押品编号关联删除保证金提货流水记录的同时删除提货清单中的数据失败，失败原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 根据出库申请业务编号删除出库申请明细信息
	 * @param serno 业务编号
	 * @return 
	 * @throws ComponentException
	 */
	public int deleteMortStorExwaDetailBySerno(String serno ) throws DaoException {
		int count = 0;
		try {
			count=SqlClient.executeUpd("deleteMortStorExwaDetailBySerno",serno,null,null,this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据出库申请业务编号删除出库申请明细信息失败，失败原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 补货入库时，保存操作，将申请流水号赋值给新增补货的货物
	 * @param sernoBf 业务流水号(更改之前)
	 * @param serno 业务流水号（更改之后）
	 * @param cargo_status 货物状态
	 * @param guaranty_no 押品编号
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortCargoReplList(String serno,String sernoBf,String cargo_status, String guaranty_no) throws DaoException{
		int result = 0;
		try {
			KeyedCollection valueKc = new KeyedCollection();
			valueKc.put("serno",sernoBf);
			valueKc.put("guaranty_no",guaranty_no);
			valueKc.put("cargo_status",cargo_status);
			result = SqlClient.executeUpd("updateMortCargoReplList",valueKc ,serno, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("补货入库时，保存操作，将申请流水号赋值给新增补货的货物失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 补货入库记账时，将数据插入货物清单表（结果表）
	 * @param oper 操作类型（1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货）
	 * @param sero 业务流水号
	 * @return 
	 * @throws ComponentException
	 */
	public int insertMortCargoPledgeBySerno(String serno,String oper) throws DaoException{
		int result = 0;
		try {
			KeyedCollection valueKc = new KeyedCollection();
			valueKc.put("serno",serno);
			valueKc.put("oper",oper);
			
			Connection conn = this.getConnection() ;
			result = SqlClient.executeUpd("insertMortCargoPledgeBySerno",valueKc,null, null, conn);
		} catch (Exception e) {
			throw new DaoException("补货入库记账时，将数据插入货物清单表（结果表）失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 提货或者货物置换时，更新提货清单中关联业务流水号字段
	 * @param re_serno 关联业务流水号
	 * @param op_type 操作类型（1--货物置换，2--保证金提货）
	 * @param guaranty_no 押品编号
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortDelivListOper(String re_serno,String guaranty_no,String op_type) throws DaoException{
		int result = 0;
		try {
			KeyedCollection valueKc = new KeyedCollection();
			valueKc.put("guaranty_no",guaranty_no);
			valueKc.put("op_type",op_type);
			result = SqlClient.executeUpd("updateMortDelivListOper",valueKc ,re_serno, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("提货或者货物置换时，更新提货清单中关联业务流水号字段失败，失败原因："+e.getMessage());
		}
		return result;
	}
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws DaoException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws DaoException {
	    PreparedStatement state = null;
	    TableModelLoader modelLoader = (TableModelLoader)this.getContext().getService(CMISConstance.ATTR_TABLEMODELLOADER);
        TableModel refModel = modelLoader.getTableModel(model);
      
	    int j;
	    try{
	        List<TableModelField> deleteFieldList = new ArrayList<TableModelField>();
	        String strSQL = null;
	     
	        StringBuffer strSQLBuf = new StringBuffer((new StringBuilder("DELETE FROM ")).append(refModel.getDbTableName()).append(" WHERE ").toString());
	        for(Iterator<String> iterator = conditionFields.keySet().iterator(); iterator.hasNext();){
	            String fieldId = (String)iterator.next();
	            TableModelField modelField = refModel.getModelField(fieldId);
	            if(modelField != null){
	                deleteFieldList.add(modelField);
	                if(iterator.hasNext())
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? AND ").toString());
	                else
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? ").toString());
	            }
	        }
	
	        if(deleteFieldList.isEmpty())
	            throw new DaoException((new StringBuilder("Delete TableModel[")).append(refModel.getId()).append("]未获取到字段信息").toString());
	        strSQL = strSQLBuf.toString();
	        Connection con = this.getConnection();  //得到数据库连接
	        state = con.prepareStatement(strSQL);
	        for(int i = 0; i < deleteFieldList.size(); i++){
	            TableModelField field = (TableModelField)deleteFieldList.get(i);
	            Object fieldValue = conditionFields.get(field.getId());
	            if(fieldValue != null){
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value = ").append(fieldValue).toString());
	                state.setObject(i + 1, fieldValue);
	            } else{
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value with null").toString());
	                state.setNull(i + 1, field.getColumnType());
	            }
	        }
	
	        int result = state.executeUpdate();
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder(String.valueOf(result))).append(" records in tableModel [").append(refModel.getId()).append("] has been DELETE.").toString());
	        state.close();
	        j = result;
	    }
	    catch(SQLException se){
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, (new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString(), se);
	        throw new DaoException((new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString());
	    }
	    if(state != null){
	        try{
	            state.close();
	        }
	        catch(SQLException sqlexception) { }
	    }
	    return j;
	}
	/**
	 * 根据表明和条件删除表中数据
	 * 
	 * @param tableName
	 * @param condition
	 */
	public int deleteDateByTableAndCondition(String tableName,
			String condition) throws EMPException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		Connection conn = this.getConnection();  //得到数据库连接
		int count = 0; // 更新的记录条数
		try {
			stmt = conn.createStatement();
			String sql = "delete from " + tableName + " " + condition;
			System.out.println("SQL= " + sql);
			
			count = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}
}