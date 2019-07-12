package com.yucheng.cmis.biz01line.prd.agent;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpLoanAppDao;
import com.yucheng.cmis.biz01line.prd.dao.PrdPolcySchemeDao;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.pub.CMISAgent;

public class PrdPolcySchemeAgent extends CMISAgent {

	/**
	 * 根据政策值获取已配置流程
	 * @param schemeId 政策ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getFlowIdBySchemeId(String schemeId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getFlowIdBySchemeId(schemeId);
	}
	/**
	 * 根据政策值、流程值获取已配置流程节点
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @return String
	 * @throws Exception
	 */
	public String getFlowNodeBySchemeIdAndFlowId(String schemeId, String flowId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getFlowNodeBySchemeIdAndFlowId(schemeId,flowId);
	}
	/**
	 * 往政策场景关联表中插入关联信息
	 * @param kColl
	 * @throws Exception
	 */
	public void insertPrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.insertPrdSchemeSpaceRel(ifSelect,kColl);
	}
	/**
	 * 根据操作ID对政策关联场景进行操作
	 * @param doId 操作ID,包括场景打开、场景关闭、场景删除
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @param flowNode 流程节点
	 * @param polcyCode 政策代码
	 * @param status 审核状态
	 */
	public void updatePrdSpaceRel(String doId, String schemeId, String flowId,String flowNode, String polcyCode, String status)throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.updatePrdSpaceRel(doId,schemeId,flowId,flowNode,polcyCode,status);
		
	}
	/**
	 * 根据政策方案ID、流程ID、流程节点ID获得节点配置的政策策略
	 * @param schemeId 政策方案ID
	 * @param flowId 流程ID
	 * @param nodeId 流程节点ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowIdAndNodeId(
			String schemeId, String flowId, String nodeId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPlocyListBySchemeIdAndFlowIdAndNodeId(schemeId,flowId,nodeId);
	}
	/**
	 * 产品配置模块【根据产品ID获取产品项下关联的机构】
	 * @param prdId 产品ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdOrgApplyICollByPrdId(String prdId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPrdOrgApplyICollByPrdId(prdId);
	}
	/**
	 * 产品配置关联机构设置，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 产品适用机构iColl
	 * @param prdId 产品编号
	 * @throws Exception
	 */
	public void doPrdOrgApplyByIColl(String prdId, IndexedCollection iColl) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.doPrdOrgApplyByIColl(prdId,iColl);
	}
	/**
	 * 产品配置模块【根据政策ID、流程ID获取政策方案项下流程中所有政策信息】
	 * @param schemeId
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowId(String schemeId,
			String flowId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPlocyListBySchemeIdAndFlowId(schemeId,flowId);
	}
	/**
	 * 根据操作ID对政策关联场景进行操作
	 * @param doId 操作ID,包括场景打开、场景关闭、场景删除
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @param status 审核状态
	 */
	public void doPrdSpaceRel(String doId, String schemeId, String flowId,
			String status) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.doPrdSpaceRel(doId,schemeId,flowId,status);
	}
	/**
	 *  产品配置模块【根据政策方案ID获取政策方案下未配置关联的政策资料】
	 * @param schemeId 政策方案ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdPlocyBySchemeId(String schemeId,String conditionStr) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPrdPlocyBySchemeId(schemeId,conditionStr);
	}
	/**
	 * 产品配置模块【根据政策方案ID和获取的资料列表建立两者关联关系】
	 * @param schemeId 政策方案ID
	 * @param schemeCode 资料代码
	 * @throws Exception
	 */
	public void connPrdPlocyWithPrdScheme(String schemeId,
			String schemeCode) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.connPrdPlocyWithPrdScheme(schemeId,schemeCode);
		
	}
	/**
	 * 产品配置模块【根据政策方案ID获取政策关联表中已关联的政策资料】
	 * @param schemeId
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeId(String schemeId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPlocyListBySchemeId(schemeId);
	}
	/**
	 * 往政策场景关联表中插入关联信息
	 * @param ifSelect 选择标志
	 * @param kColl
	 * @throws Exception
	 */
	public void updatePrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.updatePrdSchemeSpaceRel(ifSelect,kColl);
	}
	/**
	 * 通过政策ID获取政策资料列表数据，特定字段可增加
	 * @param schemeId 方案ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPlocyRelListBySchemeId(String schemeId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPlocyRelListBySchemeId(schemeId);
	}
	/**
	 * 移除政策方案和政策资料的关联关系，同步清理政策下所有节点信息
	 * @param schemeId 方案ID
	 * @param schemeCode 资料ID
	 * @throws Exception
	 */
	public void delPrdPlocyWithPrdScheme(String schemeId, String schemeCode) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		cmisDao.delPrdPlocyWithPrdScheme(schemeId,schemeCode);
	}
	/**
	 * 根据资源表中上级资源ID获得改资源下所有资源列表
	 * @param parentId 上一级资源ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getTabResourceListByParentId(String parentId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getTabResourceListByParentId(parentId);
	}
	/**
	 * 根据数据库名称和条件查询出符合条件的数据集合
	 * @param tableName 表明
	 * @param conndition 条件
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getICollByModelIdAndConndition(String tableName,
			String conndition, PageInfo pageInfo) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getICollByModelIdAndConndition(tableName,conndition,pageInfo);
	}
	/**
	 * 根据表名和上级目录ID获得该目录下的子目录
	 * @param tableName 表名
	 * @param parentId 上级目录
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getCatalogICollByParentId(String tableName,
			String parentId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getCatalogICollByParentId(tableName,parentId);
	}
	/**
	 * 通过产品上级目录检索产品表中的产品信息
	 * @param parentCatalogId 上级目录ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdByParentCatalogId(String parentCatalogId) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPrdByParentCatalogId(parentCatalogId);
	}
	/**
	 * 通过业务线，当前目录ID查询节点下下一节目录信息
	 * @param catalogid 目录ID
	 * @return IndexedCollection
	 */
	public IndexedCollection getNextCatalogByCatalogId(String catalogid) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getNextCatalogByCatalogId(catalogid);
	}
	/**
	 * 通过目录ID获得目录下的产品信息
	 * @param catalogid 目录ID
	 * @return IndexedCollection
	 */
	public IndexedCollection queryPrdBasicinfoByCatalog(Map<String,String> paramMap)  throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.queryPrdBasicinfoByCatalog(paramMap);
	}
	/**
	 * 通过产品编号获得产品信息
	 * @param prdId 产品编号
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getPrdBasicinfoByPrdId(String prdId,Connection connection)  throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getPrdBasicinfoByPrdId(prdId,connection);
	}
	/**
	 * 通过资源ID获取资源操作表中的操作权限
	 * @param resourceid 资源ID
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getActionByResourceId(String resourceid) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getActionByResourceId(resourceid);
	}
	
	/**
	 * 通过业务品种、币种、期限获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getRate(String prdId, String currType, int termM,Connection connection) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getRate(prdId,currType,termM,connection);
	}
	
	/**
     * 通过币种和利率种类获取Libor基准年利率
     * @param currType 币种
     * @param irType 利率种类
     * @param connection  数据库连接
     * @return
     * @throws Exception
     */
	public KeyedCollection getLiborRate(String currType,String openDay, String irType,Connection connection) throws Exception {
		PrdPolcySchemeDao cmisDao = (PrdPolcySchemeDao)this.getDaoInstance(PRDConstant.PRDPOLCYSCHEMEDAO);
		return cmisDao.getLiborRate(currType,openDay,irType,connection);
	}
}
