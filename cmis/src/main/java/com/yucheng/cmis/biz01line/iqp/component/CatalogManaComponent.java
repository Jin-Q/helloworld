package com.yucheng.cmis.biz01line.iqp.component;

import java.math.BigDecimal;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.agent.CatalogManaAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CatalogManaComponent extends CMISComponent{
	/**
	 * 根据表名和上级目录ID获得该目录下的子目录
	 * @param tableName 表名
	 * @param parentId 上级目录
	 * @param value 是否包含含价商品（Y--包含）
	 * @return IndexedCollection
	 * @throws ComponentException
	*/
	public IndexedCollection getCatalogICollByParentId(String tableName, String parentId,String serno,String value) throws ComponentException {
		try{
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.getCatalogICollByParentId(tableName,parentId,serno,value);
		}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 判断目录下是否存在子目录
	 * @param catalog_no 押品目录ID
	 * @return
	 * @throws ComponentException
	*/
	public boolean isLeaf(String catalog_no) throws ComponentException {
		boolean result = false;
		BigDecimal res = null;
		try{
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			res = catalogManaAgent.searchCatalogManaBySupCatalogNo(catalog_no);
			if(null != res && res.compareTo(new BigDecimal(0.0))>0){ //如果跟0比较大于0说明有子目录
				result = true;
			}else {
				result = false;//无下级目录				
			}
		}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}	
		return result;
	}
	/**
	 * 判断目录下是否存在子目录（只包含非含价商品）
	 * @param catalog_no 押品目录ID
	 * @return
	 * @throws ComponentException
	*/
	public boolean isLeafNoValue(String catalog_no) throws ComponentException {
		boolean result = false;
		BigDecimal res = null;
		try{
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			res = catalogManaAgent.searchCatalogManaNoValueBySupCatalogNo(catalog_no);
			if(null != res && res.compareTo(new BigDecimal(0.0))>0){ //如果跟0比较大于0说明有子目录
				result = true;
			}else {
				result = false;//无下级目录				
			}
		}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}	
		return result;
	}
	/**
	 * 判断目录下是否存在子目录（押品目录准入时）
	 * @param catalog_no 押品目录ID
	 * @return
	 * @throws ComponentException
	*/
	public boolean isLeaf(String catalog_no,String serno) throws ComponentException {
		boolean result = false;
		BigDecimal res = null;
		try{
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			res = catalogManaAgent.searchAppCatalogManaBySupCatalogNo(catalog_no,serno);
			if(null != res && res.compareTo(new BigDecimal(0.0))>0){ //如果跟0比较大于0说明有子目录
				result = true;
			}else {
				result = false;//无下级目录				
			}
		}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}	
		return result;
	}
	
	/**
	 * 判断目录下是否存在价格信息
	 * @param catalog_no 押品目录ID
	 * @return
	 * @throws ComponentException
	*/
	public boolean searchValueManaByCatalogNo(String catalog_no) throws ComponentException {
		boolean result = false;
		BigDecimal res = null;
		try{
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			res = catalogManaAgent.searchValueManaByCatalogNo(catalog_no);
			if(null != res && res.compareTo(new BigDecimal(0.0))>0){ //如果跟0比较大于0说明存在有效价格信息
				result = true;
			}else {
				result = false;	  //无有效价格信息			
			}
		}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}	
		return result;
	}
	
	/**
	 * 根据目录编号、状态值，修改押品目录的状态
	 * @param catalog_no 目录编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateCatalogManaStatus(String catalog_no,String status) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.updateCatalogManaStatus(catalog_no, status);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 *  删除一级目录时同步删除树形字典的押品类别项下货物质押对应数据
	 * @param catalog_no 目录编号
	 * @return 成功记录数
	 */
	public int deleteTreeDicByCatalogNo(String catalog_no) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.deleteTreeDicByCatalogNo(catalog_no);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 通过目录ID取得最大的下一个目录ID
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNo(String catalog_no) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.searchMaxCatalogNo(catalog_no);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	/**
	 * 通过目录ID取得最大的下一个目录ID（准入）
	 * @param catalog_no 目录ID
	 * @return String
	 */
	public String searchMaxCatalogNoDetail(String catalog_no) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.searchMaxCatalogNoDetail(catalog_no);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	/**
	 * 修改一级目录时更新树形字典的押品类别的货物质押对应的名称
	 * @param catalog_no 目录编号
	 * @param updateValue 修改后的值
	 * @return 成功记录数
	 */
	public int updateTreeDicByCatalogNo(String catalog_no,String updateValue) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.updateTreeDicByCatalogNo(catalog_no, updateValue);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 根据押品价格编号、状态值，修改押品价格的状态
	 * @param value_no 押品价格编号
	 * @param status 修改后的状态
	 * @return 成功记录数
	 */
	public int updateValueManaStatus(String value_no,String status)throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.updateValueManaStatus(value_no, status);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 根据目录编号删除该目录下对应的供应商
	 * @param model 表模型
	 * @param conditionFields 目录编号
	 * @return 成功记录数
	 */
	public int deleteIqpCommoProvider(String model, Map<String,String> conditionFields)throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.deleteIqpCommoProvider(model, conditionFields);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 通用执行sql方法
	 * @param type  执行sql类型
	 * @param kcoll  传值kcoll
	 * @return 返回值
	 */
	public KeyedCollection excuteSql(String type, KeyedCollection kcoll) throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.excuteSql(type,kcoll);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	/**
	 * 将押品准入明细表记录插入押品目录信息表中
	 * @param serno 业务流水号
	 */
	public int createIqpMortCatalogManaRecord(String serno)throws ComponentException {
		try {
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			return catalogManaAgent.createIqpMortCatalogManaRecord(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
}
