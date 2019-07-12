package com.yucheng.cmis.biz01line.iqp.agent;

import java.math.BigDecimal;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.dao.CatalogManaDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

public class CatalogManaAgent extends CMISAgent{
	/**
	 * 根据表名和上级目录ID获得该目录下的子目录
	 * @param tableName 表名
	 * @param parentId 上级目录
	 * @param value 是否包含含价商品（Y--包含）
	 * @return IndexedCollection
	 * @throws AgentException
	 */
	public IndexedCollection getCatalogICollByParentId(String tableName, String parentId,String serno,String value) throws AgentException {
		try{
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.getCatalogICollByParentId(tableName,parentId,serno,value);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 根据目录编号查询该节点下子目录数量
	 * @param catalog_no 目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchCatalogManaBySupCatalogNo(String catalog_no) throws AgentException {
		try{
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchCatalogManaBySupCatalogNo(catalog_no);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 根据目录编号查询该节点下子目录数量（只包含非含价商品）
	 * @param catalog_no 目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchCatalogManaNoValueBySupCatalogNo(String catalog_no) throws AgentException {
		try{
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchCatalogManaNoValueBySupCatalogNo(catalog_no);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 根据目录编号查询该节点下子目录数量（押品目录准入时）
	 * @param catalog_no 目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchAppCatalogManaBySupCatalogNo(String catalog_no,String serno) throws AgentException {
		try{
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchAppCatalogManaBySupCatalogNo(catalog_no,serno);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 根据目录编号查询该目录下价格信息数量 
	 * @param catalog_no 目录ID
	 * @return BigDecimal
	 */
	public BigDecimal searchValueManaByCatalogNo(String catalog_no) throws AgentException {
		try{
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchValueManaByCatalogNo(catalog_no);
		}catch(Exception e){
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 通过目录ID取得最大的下一个目录ID
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNo(String catalog_no) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchMaxCatalogNo(catalog_no);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 通过目录ID取得最大的下一个目录ID（准入）
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNoDetail(String catalog_no) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.searchMaxCatalogNoDetail(catalog_no);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 修改一级目录时更新树形字典的押品类别的货物质押对应的名称
	 * @param catalog_no 目录编号
	 * @param updateValue 修改后的值
	 * @return 成功记录数
	 */
	public int updateTreeDicByCatalogNo(String catalog_no,String updateValue) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.updateTreeDicByCatalogNo(catalog_no, updateValue);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 *  删除一级目录时同步删除树形字典的押品类别项下货物质押对应数据
	 * @param catalog_no 目录编号
	 * @return 成功记录数
	 */
	public int deleteTreeDicByCatalogNo(String catalog_no) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.deleteTreeDicByCatalogNo(catalog_no);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 根据目录编号、状态值，修改押品目录的状态
	 * @param catalog_no 目录编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateCatalogManaStatus(String catalog_no,String status) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.updateCatalogManaStatus(catalog_no, status);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	* 根据押品价格编号、状态值，修改押品价格的状态
	 * @param value_no 押品价格编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateValueManaStatus(String value_no,String status)throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.updateValueManaStatus(value_no, status);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	 /**
	 * 根据目录编号删除该目录下对应的供应商
	 * @param model 表模型
	 * @param conditionFields 目录编号
	 * @return 成功记录数
	 */
	public int deleteIqpCommoProvider(String model, Map<String,String> conditionFields)throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			this.getContext();
			return catalogManaDao.deleteByField(model, conditionFields);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 执行sql方法
	 * @param type  执行sql类型
	 * @param kcoll  传值kcoll
	 * @return 返回值
	 */
	public KeyedCollection excuteSql(String type, KeyedCollection kcoll) throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.excuteSql(type,kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 将押品准入明细表记录插入押品目录信息表中
	 * @param serno 业务流水号
	 */
	public int createIqpMortCatalogManaRecord(String serno)throws AgentException {
		try {
			CatalogManaDao catalogManaDao = (CatalogManaDao)this.getDaoInstance("CatalogManaDao");
			return catalogManaDao.createIqpMortCatalogManaRecord(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
}
