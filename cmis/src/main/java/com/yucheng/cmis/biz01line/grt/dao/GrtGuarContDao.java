package com.yucheng.cmis.biz01line.grt.dao;

import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;
/**
 * CMISDao层不直接写数据库操作，通过调用命名SQL实现数据库操作
 * @author xiaod
 * @create 20130725
 * 待完善，将业务逻辑抽离至CMISComponent层
 */
public class GrtGuarContDao extends CMISDao {
	
	//新增保证人的同时新增保证人与担保合同的关联信息
	public void insertGrtGuarantyRe(KeyedCollection kColl) throws DaoException {
	 try {
			String guarContNo = (String)kColl.getDataValue("guar_cont_no");
			String guarId = (String)kColl.getDataValue("guar_id");
				Map<String, String> insertMap = new HashedMap();
				insertMap.put("guarContNo", guarContNo);
				insertMap.put("guarId", guarId);
				int insertKCollResult = SqlClient.insert("insertGrtGuarantyRe", insertMap, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("新增保证人与担保合同的关联信息失败"+e.getMessage());
		} 
	}
	//删除保证人的同时删除其与担保合同的关联记录
	public int deleteGrtGuarantyReGuar(String guarId) throws DaoException {
		int delKCollResult=0;
		 try {
			 delKCollResult = SqlClient.executeUpd("deleteGrtGuarantyReGuar", guarId, null, null, this.getConnection());
			} catch (Exception e) {
				throw new DaoException("删除保证人时，级联删除其与担保合同的担保记录失败"+e.getMessage());
			} 
			return delKCollResult;
		}
	//删除一笔合同的同时删除其与保证人的关联记录
	public int deleteGrtGuarantyReCont(String guarContN0) throws DaoException{
		int delKCollResult=0;
		 try {
			 delKCollResult = SqlClient.executeUpd("deleteGrtGuarantyReCont", guarContN0, null, null, this.getConnection());
			} catch (Exception e) {
			 throw new DaoException("删除一笔担保合同的同时，级联删除与其关联的保证人信息失败"+e.getMessage());
			} 
			return delKCollResult;
		}
	
	public IndexedCollection queryGrtGuarantyReGuarList(Map<String,String> paramMap) throws DaoException {
		IndexedCollection nextIColl = null;
		try {
			nextIColl = SqlClient.queryList4IColl("queryGrtGuarantyReGuarList", paramMap, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据合同编号，获取与其关联的保证人信息失败"+e.getMessage());
		}
		return nextIColl;
	}
	/**
	 * 通过担保合同编号获得其下关联下的担保信息
	 * @param guarContNo 合同编号
	 * @return KeyedCollection
	 */
	public IndexedCollection queryGrtGuarantyReGuarList(String guarContNo) throws DaoException {
		IndexedCollection returnIColl = new IndexedCollection();
		try {
			returnIColl = (IndexedCollection)SqlClient.queryFirst("queryGrtGuarantyReGuarList", guarContNo, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据合同编号，获取担保合同下关联的担保信息失败"+e.getMessage());
		}
		return returnIColl;
	}
	/**
	 * 借助担保合同和保证人的关联关系表查询担保合同表和保证人信息表中所有相关的保证人信息和担保合同信息
	 * @param guarContNo
	 * @param  客户编号，担保合同编号
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection queryGrtGuaranteeAllList() throws DaoException {
		IndexedCollection returnIColl = new IndexedCollection();
		try {
			returnIColl = (IndexedCollection)SqlClient.queryList4IColl("queryGrtGuaranteeAllList", null, this.getConnection());
			
		} catch (Exception e) {
			throw new DaoException("获取保证人信息列表信息失败"+e.getMessage());
		}
		return returnIColl;
	}
	/**
	 * 通过担保合同编号和保证人客户编号通过关联关系表查出唯一相关的保证人信息和合同信息
	 * @param guarContNo
	 * @param cusId，guarContNo 客户编号，担保合同编号
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection queryGrtGuaranteeAllDetail(String guarContNo,String cusId) throws DaoException{
		KeyedCollection returnkColl = new KeyedCollection();
		try {	
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("guarContNo", guarContNo);
			kColl.addDataField("cusId", cusId);
			returnkColl = (KeyedCollection) SqlClient.queryFirst("queryGrtGuaranteeAllDetail", kColl, null, this.getConnection());
		}catch (Exception e) {
			throw new DaoException("通过担保合同编号和保证人客户编号通过关联关系表查出唯一相关的保证人信息和合同信息失败"+e.getMessage());
		}
		return returnkColl;
		
	}
	/**根据合同编号获取池编号（一个担保合同只能引用一个池作为担保品）*/
	public String getDrfpoNoByGuarContNo(String guarContNo) throws DaoException {
		String result="";
		 try {
			 KeyedCollection resultKc = (KeyedCollection) SqlClient.queryFirst("getDrfpoNoByGuarContNo", guarContNo,null,this.getConnection());
			 result = (String) resultKc.getDataValue("guaranty_id");
			} catch (Exception e) {
				throw new DaoException("获取池编号失败"+e.getMessage());
			} 
			return result;
		}
	/**根据合同编号获取该合同下保证人担保金额的总额*/
	public String getGuarAmtByGuarContNo(String guarContNo) throws DaoException {
		String result="";
		 try {
			 KeyedCollection resultKc = (KeyedCollection) SqlClient.queryFirst("getGuarAmtByGuarContNo", guarContNo,null,this.getConnection());
			 result = resultKc.getDataValue("guar_amt").toString();
			} catch (Exception e) {
				throw new DaoException("根据合同编号获取该合同下保证人担保金额的总额信息失败"+e.getMessage());
			} 
			return result;
		}
	/**根据担保合同编号获取相关联下的押品担保金额累加和*/
	public String queryGrtGuarantyAmt(String guarContNo) throws DaoException {
		String result="";
		 try {
			 KeyedCollection resultKc = (KeyedCollection) SqlClient.queryFirst("queryGrtGuarantyAmt", guarContNo,null,this.getConnection());
			 result = resultKc.getDataValue("guar_amt").toString();
			} catch (Exception e) {
				throw new DaoException("根据担保合同编号["+guarContNo+"]获取相关联下的押品担保金额累加和信息失败"+e.getMessage());
			} 
			return result;
		}
}

