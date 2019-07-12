package com.yucheng.cmis.biz01line.prd.component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.prd.agent.PrdPolcySchemeAgent;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class PrdPolcySchemeComponent extends CMISComponent {
	
	/**
	 * 根据政策值获取已配置流程
	 * @param schemeId 政策ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getFlowIdBySchemeId(String schemeId) throws Exception{
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getFlowIdBySchemeId(schemeId);
	}
	
	/**
	 * 根据政策值、流程值获取已配置流程节点
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @return String
	 * @throws Exception
	 */
	public String getFlowNodeBySchemeIdAndFlowId(String schemeId, String flowId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getFlowNodeBySchemeIdAndFlowId(schemeId,flowId);
	}
	
	/**
	 * 根据政策方案ID、流程ID、流程节点ID获得节点配置的政策策略
	 * @param schemeId 政策方案ID
	 * @param flowId 流程ID
	 * @param nodeId 流程节点ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowIdAndNodeId(String schemeId, String flowId, String nodeId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPlocyListBySchemeIdAndFlowIdAndNodeId(schemeId,flowId,nodeId);
	}
	/**
	 * 往政策场景关联表中插入关联信息
	 * @param kColl
	 * @throws Exception
	 */
	public void insertPrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.insertPrdSchemeSpaceRel(ifSelect,kColl);
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
	public void updatePrdSpaceRel(String doId, String schemeId, String flowId, String flowNode, String polcyCode, String status) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.updatePrdSpaceRel(doId,schemeId,flowId,flowNode,polcyCode,status);
	}
	
	/**
	 * 产品配置模块【根据产品ID获取产品项下关联的机构】
	 * @param prdId 产品ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdOrgApplyICollByPrdId(String prdId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPrdOrgApplyICollByPrdId(prdId);
	}
	
	/**
	 * 产品配置关联机构设置，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 产品适用机构iColl
	 * @param prdId 产品编号
	 * @throws Exception
	 */
	public void doPrdOrgApplyByIColl(String prdId, IndexedCollection iColl) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.doPrdOrgApplyByIColl(prdId,iColl);
	}
	
	/**
	 * 产品配置模块【根据政策ID、流程ID获取政策方案项下流程中所有政策信息】
	 * @param schemeId
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowId(String schemeId, String flowId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPlocyListBySchemeIdAndFlowId(schemeId,flowId);
	}
	
	/**
	 * 根据操作ID对政策关联场景进行操作
	 * @param doId 操作ID,包括场景打开、场景关闭、场景删除
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @param status 审核状态
	 */
	public void doPrdSpaceRel(String doId, String schemeId, String flowId, String status) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.doPrdSpaceRel(doId,schemeId,flowId,status);
	}
	
	/**
	 *  产品配置模块【根据政策方案ID获取政策方案下未配置关联的政策资料】
	 * @param schemeId 政策方案ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdPlocyBySchemeId(String schemeId,String conditionStr)throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPrdPlocyBySchemeId(schemeId,conditionStr);
	}
	
	/**
	 * 产品配置模块【根据政策方案ID和获取的资料列表建立两者关联关系】
	 * @param schemeId 政策方案ID
	 * @param schemeCode 资料代码
	 * @throws Exception
	 */
	public void connPrdPlocyWithPrdScheme(String schemeId, String schemeCode) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.connPrdPlocyWithPrdScheme(schemeId,schemeCode);
		
	}
	
	/**
	 * 产品配置模块【根据政策方案ID获取政策关联表中已关联的政策资料】
	 * @param schemeId
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPlocyListBySchemeId(String schemeId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPlocyListBySchemeId(schemeId);
		
	}
	
	/**
	 * 往政策场景关联表中插入关联信息
	 * @param ifSelect 选择标志
	 * @param kColl
	 * @throws Exception
	 */
	public void updatePrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.updatePrdSchemeSpaceRel(ifSelect,kColl);
	}
	
	/**
	 * 通过政策ID获取政策资料列表数据，特定字段可增加
	 * @param schemeId 方案ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public  IndexedCollection getPlocyRelListBySchemeId(String schemeId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPlocyRelListBySchemeId(schemeId);
		
	}

	/**
	 * 移除政策方案和政策资料的关联关系，同步清理政策下所有节点信息
	 * @param schemeId 方案ID
	 * @param schemeCode 资料ID
	 * @throws Exception
	 */
	public void delPrdPlocyWithPrdScheme(String schemeId, String schemeCode) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		cmisAgent.delPrdPlocyWithPrdScheme(schemeId,schemeCode);
	}
	
	/**
	 * 根据资源表中上级资源ID获得改资源下所有资源列表
	 * @param parentId 上一级资源ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getTabResourceListByParentId(String parentId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getTabResourceListByParentId(parentId);
	}
	
	/**
	 * 根据数据库名称和条件查询出符合条件的数据集合
	 * @param tableName 表明
	 * @param conndition 条件
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getICollByModelIdAndConndition(String tableName, String conndition, PageInfo pageInfo) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getICollByModelIdAndConndition(tableName,conndition,pageInfo);
	}
	
	/**
	 * 根据表名和上级目录ID获得该目录下的子目录
	 * @param tableName 表名
	 * @param parentId 上级目录
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getCatalogICollByParentId(String tableName, String parentId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getCatalogICollByParentId(tableName,parentId);
	}

	/**
	 * 通过产品上级目录检索产品表中的产品信息
	 * @param parentCatalogId 上级目录ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getPrdByParentCatalogId(String parentCatalogId) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		return cmisAgent.getPrdByParentCatalogId(parentCatalogId);
	}

	/**
	 * 通过业务线查询节点下下一节点信息，包含目录和产品
	 * @param bizline 所属业务线
	 * @param parentNodeId 上一节点
	 * @return IndexedCollection
	 */
	public IndexedCollection getNextCatalogByBizline(String bizline, String catalogid) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		IndexedCollection nextNodeIColl = new IndexedCollection();
		IndexedCollection nextPrdIColl = null;
		IndexedCollection nextCatalogIColl = null;
		if(catalogid == null || catalogid == ""){
			/**加载业务线跟节点以及其一级目录*/
			//nextNodeIColl = cmisAgent.getNextCatalogByBizlineAndCatalogId(bizline,catalogid);
			/**递归方式遍历每一级子节点下的子节点，直至最底层，返回结果*/
			
		}else {
			/**加载业务线请求节点以及其一级目录，并且判断项下所有节点是否显示*/
			/** 获得下一节点信息，并前遍历判断其子节点是否显示 */
			boolean result = this.isLeaf(bizline, catalogid);
			if(result){
				//查询下一节点目录信息、产品信息
				if(this.queryHashCatalogInCatalog(bizline,catalogid)){
					nextCatalogIColl = cmisAgent.getNextCatalogByCatalogId(catalogid);
					for(int i=0;i<nextCatalogIColl.size();i++){
						KeyedCollection kcput = null;
						kcput = (KeyedCollection)nextCatalogIColl.get(i);
						if(this.isLeaf(bizline, (String)kcput.getDataValue("catalogid"))){
							KeyedCollection kc = new KeyedCollection();
							kc.addDataField("catalogid", kcput.getDataValue("catalogid"));
							kc.addDataField("catalogname", kcput.getDataValue("catalogname"));
							kc.addDataField("cataloglevel", kcput.getDataValue("cataloglevel"));
							kc.addDataField("supcatalogid", kcput.getDataValue("supcatalogid"));
							nextNodeIColl.add(kc);
						}
					}
				}
				if(this.queryHashPrdInCatalog(bizline,catalogid)){
					nextPrdIColl = this.getNextPrdByBizlineAndCatalog(bizline,catalogid);
					for(int i=0;i<nextPrdIColl.size();i++){
						KeyedCollection kcput = null;
						kcput = (KeyedCollection)nextPrdIColl.get(i);
						KeyedCollection kc = new KeyedCollection();
						kc.addDataField("catalogid", kcput.getDataValue("catalogid"));
						kc.addDataField("catalogname", kcput.getDataValue("catalogname"));
						kc.addDataField("cataloglevel", kcput.getDataValue("cataloglevel"));
						kc.addDataField("supcatalogid", kcput.getDataValue("supcatalogid"));
						nextNodeIColl.add(kc);
					}
				}
			}else {
				//无下级节点、目录信息,不做任何处理，返回空
			}
		}
		return nextNodeIColl;
	}
	
	/**
	 * 通过业务线目录ID获得当前目录下有效产品信息
	 * @param bizline 业务线
	 * @param catalogid 目录ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getNextPrdByBizlineAndCatalog(String bizline, String catalogid) throws Exception {
		IndexedCollection nextPrdIColl = new IndexedCollection();
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		/** 取系统营业时间 */
		String currentDate = (String)this.getContext().getDataValue(PUBConstant.OPENDAY);
		Map<String,String> paramMap = new HashedMap();
		paramMap.put("catalogid", catalogid);
		paramMap.put("currentdate", currentDate);
		IndexedCollection ic = cmisAgent.queryPrdBasicinfoByCatalog(paramMap);
		/** 取出业务线下产品 */
		if(ic != null && ic.size() > 0){
			/**判断产品是否在所属业务线下*/
			for(int i=0;i<ic.size();i++){
				KeyedCollection kColl = (KeyedCollection)ic.get(i);
				boolean help = true;
				KeyedCollection kc = new KeyedCollection();
				String prdowner  = (String)kColl.getDataValue("prdowner");
				if(prdowner != null){
					if(prdowner.indexOf(",") != -1){
						String[] bizlines = prdowner.split(",");
						for(int j=0;j<bizlines.length;j++){
							if(bizline.indexOf(",") != -1){
								String[] lines = bizline.split(",");
								for(int k=0;k<lines.length;k++){
									if(lines[k].equals(bizlines[j])&&help){
										help = false;
										kc.addDataField("catalogid", kColl.getDataValue("prdid"));
										kc.addDataField("catalogname", kColl.getDataValue("prdname"));
										kc.addDataField("cataloglevel", kColl.getDataValue("supcatalog"));
										kc.addDataField("supcatalogid", kColl.getDataValue("supcatalog"));
										nextPrdIColl.add(kc);
									}
								}
							}else {
								if(bizline.equals(bizlines[j])){
									kc.addDataField("catalogid", kColl.getDataValue("prdid"));
									kc.addDataField("catalogname", kColl.getDataValue("prdname"));
									kc.addDataField("cataloglevel", kColl.getDataValue("supcatalog"));
									kc.addDataField("supcatalogid", kColl.getDataValue("supcatalog"));
									nextPrdIColl.add(kc);
								}
							}
						}
					}else {
						if(bizline.indexOf(",") != -1){
							String[] lines = bizline.split(",");
							for(int k=0;k<lines.length;k++){
								if(lines[k].equals(prdowner)&&help){
									help = false;
									kc.addDataField("catalogid", kColl.getDataValue("prdid"));
									kc.addDataField("catalogname", kColl.getDataValue("prdname"));
									kc.addDataField("cataloglevel", kColl.getDataValue("supcatalog"));
									kc.addDataField("supcatalogid", kColl.getDataValue("supcatalog"));
									nextPrdIColl.add(kc);
								}
							}
						}else {
							if(bizline.equals(prdowner)){
								kc.addDataField("catalogid", kColl.getDataValue("prdid"));
								kc.addDataField("catalogname", kColl.getDataValue("prdname"));
								kc.addDataField("cataloglevel", kColl.getDataValue("supcatalog"));
								kc.addDataField("supcatalogid", kColl.getDataValue("supcatalog"));
								nextPrdIColl.add(kc);
							} 
						}
					}
				}
			}
		}
		return nextPrdIColl;
	}
	/**
	 * 通过业务线和节点ID判断当前目录下是否有产品
	 * @param bizline 业务线
	 * @param catalogid 目录ID
	 * @return boolean
	 */
	public boolean queryHashPrdInCatalog(String bizline, String catalogid) throws Exception {
		/** 获取产品线目录下一级目录下的产品信息 */
		IndexedCollection prdIColl = null;
		boolean result = false;
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		/** 取系统营业时间 */
		String currentDate = (String)this.getContext().getDataValue(PUBConstant.OPENDAY);
		Map<String,String> paramMap = new HashedMap();
		paramMap.put("catalogid", catalogid);
		paramMap.put("currentdate", currentDate);
		prdIColl = cmisAgent.queryPrdBasicinfoByCatalog(paramMap);
		if(prdIColl != null && prdIColl.size() > 0){
			/**判断产品是否在所属业务线下*/
			for(int i=0;i<prdIColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)prdIColl.get(i);
				String prdowner  = (String)kColl.getDataValue("prdowner");
				if(prdowner != null){
					if(prdowner.indexOf(",") != -1){
						String[] bizlines = prdowner.split(",");
						for(int j=0;j<bizlines.length;j++){
							if(bizline.indexOf(",") != -1){
								String[] lines = bizline.split(",");
								for(int k=0;k<lines.length;k++){
									if(lines[k].equals(bizlines[j])){
										result = true;
										break;
									}
								}
							}else {
								if(bizline.equals(bizlines[j])){
									result = true;
								}
							}
						}
					}else {
						if(bizline.indexOf(",") != -1){
							String[] lines = bizline.split(",");
							for(int k=0;k<lines.length;k++){
								if(lines[k].equals(prdowner)){
									result = true;
									break;
								}
							}
						}else {
							if(bizline.equals(prdowner)){
								result = true;
							} 
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * 通过业务线和节点ID判断当前目录下是否有下级目录
	 * @param bizline 业务线
	 * @param catalogid 目录ID
	 * @return boolean
	 * @throws Exception
	 */
	public boolean queryHashCatalogInCatalog(String bizline, String catalogid) throws Exception {
		/** 获取产品线目录下一级目录下的目录信息 */
		IndexedCollection catalogIColl = null;
		boolean result = false;
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		catalogIColl = cmisAgent.getNextCatalogByCatalogId(catalogid);
		if(catalogIColl != null && catalogIColl.size() > 0){
			/**判断目录是否在所属业务线下*/
			result = true;
		}else {
			result = false;
		}
		return result;
	}
	/**
	 * 判断当前目录下所有子目录中是否有产品
	 * @param bizline
	 * @param catalogid
	 * @return
	 * @throws Exception
	 */
	public boolean isLeaf(String bizline, String catalogid) throws Exception {
		boolean result = false;
		IndexedCollection prdIColl = null;
		IndexedCollection catalogIColl = null;
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		boolean hasPrd = false;
		boolean hasCat = false;
		hasPrd = this.queryHashPrdInCatalog(bizline,catalogid);
		hasCat = this.queryHashCatalogInCatalog(bizline, catalogid);
		if(hasPrd == false && hasCat == false){
			result = false;//无下级目录，无下级产品
		}else if(hasPrd){
			result = true;//有下级产品
		}else if(hasCat == true && hasPrd == false){//有下级目录，无下级产品
			catalogIColl = cmisAgent.getNextCatalogByCatalogId(catalogid);
			for(int i=0;i<catalogIColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)catalogIColl.get(i);
				String subCatalog = (String)kColl.getDataValue("catalogid");
				result = this.isLeaf(bizline, subCatalog);
				if(result){
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 产品配置映射关系使用，将源表中数据迁移根据映射关系迁移到目标表中
	 * @param prdId 产品ID
	 * @param mapType 映射类型
	 * @param kModel 源表模型值封装成的数据集
	 * @param toDefKColl 默认数据集
	 * @param toTable 目标表
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 结果集
	 * @throws Exception
	 */
	public KeyedCollection insertMsgByKModelFromPrdMap(String prdId,String mapType, KeyedCollection kModel, KeyedCollection toDefKColl,
			String toTable, Context context, Connection connection) throws Exception {
		/**
		 * 1.通过产品编号查询产品配置的映射关系
		 * 2.解析取得的映射关系，从KeyedCollection中取值
		 * 3.生成插入数据的目标表模型KeyedCollection
		 */
		KeyedCollection resultKColl = new KeyedCollection();
		resultKColl.addDataField("code", "success");//返回结果代码
		resultKColl.addDataField("msg", "");//反馈信息
		
		PrdBasicinfo prdBasicinfo = new PrdBasicinfo();
		try {
			PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
			KeyedCollection kc = cmisAgent.getPrdBasicinfoByPrdId(prdId,connection);
			if(kc == null){
				resultKColl.setDataValue("code", "failed");
				resultKColl.setDataValue("msg", "未获取到有效产品配置信息！");
			}else {
				ComponentHelper componetHelper = new ComponentHelper();
				componetHelper.kcolTOdomain(prdBasicinfo, kc);
				if(prdBasicinfo == null){
					resultKColl.setDataValue("code", "failed");
					resultKColl.setDataValue("msg", "产品中未配置改产品信息！");
					throw new Exception("产品中未配置改产品信息！");
				}else {
					TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
					if("cont".equals(mapType)){
						/** 合同映射解析 */
						String contMapXml = prdBasicinfo.getContmapping();
						if(contMapXml == null){
							resultKColl.setDataValue("code", "failed");
							resultKColl.setDataValue("msg", "未配置映射信息！");
							throw new Exception("未配置映射信息");
						}
						KeyedCollection mainK = this.getInsertMainKColl(kModel, toDefKColl, toTable, contMapXml, context);
						dao.insert(mainK, connection);
						List<KeyedCollection> subM = (List<KeyedCollection>) this.getInsertSubKColl(kModel, toDefKColl, contMapXml, context);
						for(int k=0;k<subM.size();k++){
							KeyedCollection subK = subM.get(k);
							if(subK != null && subK.size() > 0){
								dao.insert(subK, connection);
							}
						}
						int a = this.insertICollFromKModel2toFrom(kModel, toDefKColl,contMapXml, context, connection);
				        resultKColl.setDataValue("msg", "成功！");//反馈信息
					}else if("pvp".equals(mapType)){
						/** 出账映射解析 */
						String pvpMapXml = prdBasicinfo.getPvpmapping();
						if(pvpMapXml == null){
							resultKColl.setDataValue("code", "failed");
							resultKColl.setDataValue("msg", "未配置映射信息！");
							throw new Exception("未配置映射信息");
						}
						KeyedCollection mainK = this.getInsertMainKColl(kModel, toDefKColl, toTable, pvpMapXml, context);
						dao.insert(mainK, connection);
						List<KeyedCollection> subM = (List<KeyedCollection>) this.getInsertSubKColl(kModel, toDefKColl, pvpMapXml, context);
						for(int k=0;k<subM.size();k++){
							KeyedCollection subK = subM.get(k);
							if(subK != null && subK.size() > 0){
								dao.insert(subK, connection);
							}
						}
						int a = this.insertICollFromKModel2toFrom(kModel, toDefKColl,pvpMapXml, context, connection);
				        resultKColl.setDataValue("msg", "成功！");//反馈信息
					}else {
						resultKColl.setDataValue("code", "failed");
						resultKColl.setDataValue("msg", "未配置所属映射策略！");
						throw new Exception("未配置所属映射策略");
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("未配置映射信息"+e);
		}
		return resultKColl;
	}
	/**
	 * 解析映射关系，暂时废弃
	 * @param kModel 源表数据
	 * @param toTable 目标表模型
	 * @param toDefKColl 目标表初始化值
	 * @param mapXml 映射的xml
	 * @param context 上下文
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection analyXML2KColl(KeyedCollection kModel, KeyedCollection toDefKColl, String toTable, String mapXml, Context context) throws Exception {
		KeyedCollection insertKColl = new KeyedCollection(toTable);
		for(int i=0;i<toDefKColl.size();i++){
			DataElement element = (DataElement) toDefKColl.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				insertKColl.addDataField(aField.getName(), aField.getValue());
			}
		}
		String mapXmlHelp = mapXml.replaceAll("~","\"");
		/** 解析XML映射关系 */
		DOMParser parser = new DOMParser();
		java.io.StringReader reader = new java.io.StringReader(mapXmlHelp);
		parser.parse(new InputSource(reader));
        Document doc = parser.getDocument();
        NodeList mList = doc.getElementsByTagName("m");
        for(int i=0;i<mList.getLength();i++){
        	String st_f = "";//源表字段
        	String st_t = "";//目标表字段
        	if(mList.item(i).getAttributes().getNamedItem("f") != null){
        		st_f = (String) mList.item(i).getAttributes().getNamedItem("f").getNodeValue();
        	}
        	if (mList.item(i).getAttributes().getNamedItem("t") != null) {
        		st_t = (String) mList.item(i).getAttributes().getNamedItem("t").getNodeValue();
            }
        	if(st_f != null && !st_f.trim().equals("") && st_t != null && !st_f.trim().equals("")){
        		/** 解析系统参数映射 */
        		if(st_f.indexOf("sys:") == 0){
        			st_f = st_f.substring(4, st_f.length()).trim();
        			st_f = (String)context.getDataValue(st_f);
        			if(insertKColl.containsKey(st_t)){
        				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
        			}else {
        				insertKColl.addDataField(st_t, st_f);
        			}
        		}else if(kModel.containsKey(st_f)){
        			/**映射解析字段*/
        			String st_f_value = (String)kModel.getDataValue(st_f);
        			if(st_f_value == null){
        				st_f_value = "";
        			}
        			if(insertKColl.containsKey(st_t)){
        				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
        			}else {
        				insertKColl.addDataField(st_t, st_f_value);
        			}
        		}
        	}
        }
		return insertKColl;
	}
	/**
	 * 将源表模型读取的KeyedCollection转换为插入目标模型的KeyedCollection
	 * @param kModel 源表KeyedCollection值
	 * @param toDefKColl 默认KColl值
	 * @param toTable 目标表模型值
	 * @param mapXml 映射XML
	 * @param context 上下文
	 * @return 插入目标模型的KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection analyInsertKCollFromXmlMapping(KeyedCollection kModel, KeyedCollection toDefKColl, String toTable, String mapXml, Context context) throws Exception {
		String insertKCollName = "";
		if(toTable == null || toTable == ""){
			insertKCollName = kModel.getName();
		}else {
			insertKCollName = toTable;
		}
		KeyedCollection insertKColl = new KeyedCollection(insertKCollName);
		KeyedCollection insertSubKColl = new KeyedCollection(insertKCollName);
		
		String mapXmlHelp = mapXml.replaceAll("~","\"");
		/** 解析XML映射关系 */
		DOMParser parser = new DOMParser();
		java.io.StringReader reader = new java.io.StringReader(mapXmlHelp);
		parser.parse(new InputSource(reader));
        Document doc = parser.getDocument();
        NodeList mList = doc.getElementsByTagName("m");
        for(int i=0;i<mList.getLength();i++){
        	String st_f = "";//源表字段
        	String st_t = "";//目标表字段
        	if(mList.item(i).getAttributes().getNamedItem("f") != null){
        		st_f = (String) mList.item(i).getAttributes().getNamedItem("f").getNodeValue();
        	}
        	if (mList.item(i).getAttributes().getNamedItem("t") != null) {
        		st_t = (String) mList.item(i).getAttributes().getNamedItem("t").getNodeValue();
            }
        	if(st_f != null && !st_f.trim().equals("") && st_t != null && !st_f.trim().equals("")){
        		/** 1.解析目标从表单映射关系 */
        		if(st_t.indexOf(".") != -1){
        			String tModelName = st_t.substring(0,st_t.indexOf("."));
					String tCloumnName= st_t.substring(st_t.indexOf(".")+1,st_t.length());
					if(toTable == null || toTable == ""){
						insertSubKColl.setName(tModelName);
					}
        			if(st_f.indexOf("sys:") != -1){
        				/** 1.1.解析系统参数映射 */
        				st_f = st_f.substring(4, st_f.length()).trim();
        				if(context.containsKey(st_f)){
        					st_f = (String)context.getDataValue(st_f);
        					if(insertSubKColl.containsKey(tCloumnName)){
                				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                			}else {
                				insertSubKColl.addDataField(tCloumnName, st_f);
                			}
        				}else {
        					throw new Exception("获取系统参数"+st_f+"失败！");
        				}
        			}else if(st_f.indexOf(".") != -1){
        				/** 1.2.解析从表参数映射 */
        				if(st_f.substring(0, st_f.indexOf(".")).equals(kModel.getName())){
        					/**去得改表模型下映射数据*/
        					if(kModel.containsKey(st_f.substring(st_f.indexOf(".")+1,st_f.length()))){
        						String st_f_value = (String)kModel.getDataValue(st_f.substring(st_f.indexOf(".")+1,st_f.length()));
        						if(insertSubKColl.containsKey(tCloumnName)){
                    				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                    			}else {
                    				insertSubKColl.addDataField(tCloumnName, st_f_value);
                    			}
        					}else {
        						throw new Exception("源表单数据中未包含配置的映射数据");
        					}
        				}
        			}else {
        				/** 1.3.解析主表参数映射 */
        				if(kModel.containsKey(st_f.substring(st_f.indexOf(".")+1,st_f.length()))){
        					String st_f_value = (String)kModel.getDataValue(st_f.substring(st_f.indexOf(".")+1,st_f.length()));
        					if(insertSubKColl.containsKey(tCloumnName)){
                				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                			}else {
                				insertSubKColl.addDataField(tCloumnName, st_f_value);
                			}
        				}
        			}
        			 /** 赋予映射表单初始值 */
        	    	for(int j=0;j<toDefKColl.size();j++){
        				DataElement element = (DataElement) toDefKColl.getDataElement(j);
        				if (element instanceof DataField) {
        					DataField aField = (DataField) element;
        					if(insertSubKColl.containsKey(aField.getName())){
        						
        					}else {
        						insertSubKColl.addDataField(aField.getName(), aField.getValue());
        					}
        				}
        			}
        		}else {
        			if(st_f.indexOf("sys:") != -1){
        				/** 2.1.解析系统参数映射 */
        				st_f = st_f.substring(4, st_f.length()).trim();
        				if(context.containsKey(st_f)){
        					String st_f_value = (String)context.getDataValue(st_f);
        					if(insertKColl.containsKey(st_t)){
                				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                			}else {
                				insertKColl.addDataField(st_t, st_f_value);
                			}
        				}else {
        					throw new Exception("获取系统参数"+st_f+"失败！");
        				}
        			}else if(st_f.indexOf(".") != -1){
        				/** 2.2.解析从表参数映射 */
        				if(st_f.substring(0, st_f.indexOf(".")).equals(kModel.getName())){
        					/**去得改表模型下映射数据*/
        					if(kModel.containsKey(st_f.substring(st_f.indexOf(".")+1,st_f.length()))){
        						String st_f_value = (String)kModel.getDataValue(st_f.substring(st_f.indexOf(".")+1,st_f.length()));
        						if(insertKColl.containsKey(st_t)){
                    				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                    			}else {
                    				insertKColl.addDataField(st_t, st_f_value);
                    			}
        					}else {
        						throw new Exception("源表单数据中未包含配置的映射数据");
        					}
        				}
        			}else {
        				/** 2.3.解析主表参数映射 */
        				if(kModel.containsKey(st_f.substring(st_f.indexOf(".")+1,st_f.length()))){
        					String st_f_value = (String)kModel.getDataValue(st_f.substring(st_f.indexOf(".")+1,st_f.length()));
        					if(insertKColl.containsKey(st_t)){
                				/**若插入表单中已经存在所付默认值，以默认字段为准，若有额外需求变动if-else顺序即可*/
                			}else {
                				insertKColl.addDataField(st_t, st_f_value);
                			}
        				}
        			}
        			 /** 赋予映射表单初始值 */
        			for(int j=0;j<toDefKColl.size();j++){
        				DataElement element = (DataElement) toDefKColl.getDataElement(j);
        				if (element instanceof DataField) {
        					DataField aField = (DataField) element;
        					if(insertKColl.containsKey(aField.getName())){
        						
        					}else {
        						insertKColl.addDataField(aField.getName(), aField.getValue());
        					}
        				}
        			}
        		}
        	}
        }
        if(toTable == null || toTable == ""){
			return insertSubKColl;
		}else {
			return insertKColl;
		}
	}
	/**
	 * 解析出源表模型主主表单模型的KColl值
	 * @param kModel 源表模型KColl值
	 * @param toDefKColl 默认KColl值
	 * @param toTable 目标表模型值
	 * @param mapXml 映射XML
	 * @param context 上下文
	 * @return 主表单封装成的KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getInsertMainKColl(KeyedCollection kModel, KeyedCollection toDefKColl, String toTable, String mapXml, Context context) throws Exception {
		KeyedCollection insertKColl = new KeyedCollection(toTable);
		Iterator iterator;
		for(iterator = kModel.values().iterator();iterator.hasNext();){
			DataElement element = (DataElement)iterator.next();
			if(element instanceof DataField){
				 DataField aField = (DataField)element;
				 insertKColl.addDataField(aField.getName(), aField.getValue());
			}
		}
		
		insertKColl = this.analyInsertKCollFromXmlMapping(insertKColl, toDefKColl, toTable, mapXml, context);
		return insertKColl;
	}
	/**
	 * 解析出源表模型中的从表模型的KColl值，支持多从表
	 * @param kModel 源表模型KColl值
	 * @param toDefKColl 默认KColl值
	 * @param mapXml 映射XML
	 * @param context 上下文
	 * @return 从表KeyedCollection封装成的List集合
	 * @throws Exception
	 */
	public List<KeyedCollection> getInsertSubKColl(KeyedCollection kModel, KeyedCollection toDefKColl, String mapXml, Context context) throws Exception {
		List<KeyedCollection> list = new ArrayList<KeyedCollection>();
		Iterator iterator;
		for(iterator = kModel.values().iterator();iterator.hasNext();){
			DataElement element = (DataElement)iterator.next();
			if(element instanceof KeyedCollection){
				KeyedCollection subKColl = (KeyedCollection)element;
				subKColl = this.analyInsertKCollFromXmlMapping(subKColl, toDefKColl, "", mapXml, context);
				list.add(subKColl);
			}
		}
		return list;
	}
	/**
	 * 解析出源表模型中的子表单KColl值，插入目标表中
	 * @param kModel 源表模型KColl值
	 * @param toDefKColl 默认KColl值
	 * @param mapXml 映射XML
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 插入子表单结果
	 * @throws Exception
	 */
	public int insertICollFromKModel2toFrom(KeyedCollection kModel, KeyedCollection toDefKColl, String mapXml, Context context, Connection connection) throws Exception {
		int result = 0;
		Iterator iterator;
		for(iterator = kModel.values().iterator();iterator.hasNext();){
			DataElement element = (DataElement)iterator.next();
			if(element instanceof IndexedCollection){
				TableModelDAO tDao = new TableModelDAO();
				IndexedCollection icoll = (IndexedCollection)element;
				for(int i=0;i<icoll.size();i++){
					KeyedCollection insertSonKColl = (KeyedCollection)icoll.get(i);
					insertSonKColl = this.analyInsertKCollFromXmlMapping(insertSonKColl, toDefKColl, "", mapXml, context);
					result = tDao.insert(insertSonKColl, connection);
				}
			}
		}
		return result;
	}
	/**
	 * 通过资源ID获取资源操作表中的操作权限
	 * @param resourceid 资源ID
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getActionByResourceId(String resourceid) throws Exception {
		IndexedCollection actIColl = new IndexedCollection();
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		actIColl = cmisAgent.getActionByResourceId(resourceid);
		return actIColl;
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
		KeyedCollection kColl = new KeyedCollection();
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		kColl = cmisAgent.getRate(prdId, currType, termM,connection);
		return kColl;
	}
	
	/**
	 * 查询LIBOR牌告基准利率 
	 * @param currType 币种
	 * @param irType  利率种类
	 * @return 
	 * @throws Exception
	 */
	public KeyedCollection getLiborRate(String currType,String openDay, String irType,Connection connection) throws Exception{
		KeyedCollection returnKColl = new KeyedCollection();
		KeyedCollection kc = null;
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		kc = cmisAgent.getLiborRate(currType,openDay,irType,connection);
		if(kc != null && kc.size() > 0){
			returnKColl.addDataField("flag", "success");
			returnKColl.addDataField("msg", "查询成功！");
			returnKColl.addDataField("rateValue",kc.getDataValue("libor_rate"));
		}else {
			returnKColl.addDataField("flag", "fault");
			returnKColl.addDataField("msg", "查询失败，请查看LIBOR利率是否维护正确！");
			returnKColl.addDataField("rateValue",0);
		}
		return returnKColl;
	}

	/**
	 * 通过指定的目录父节点，获取指定目录下所有产品信息
	 * @param fatherCatalogId 指定目录父节点
	 * @param bizLine 业务条线
	 * @param connection 数据库连接
	 * @return IndexedCollection
	 * @throws Exception 异常信息
	 */
	public KeyedCollection getAllShowPrdTreeByFatherCatalogId(String fatherCatalogId, String bizLine, Connection connection) throws Exception {
		TableModelDAO dao = this.getTableModelDAO();
		KeyedCollection allKColl = new KeyedCollection();
		KeyedCollection kColl = dao.queryDetail("PrdCatalog", fatherCatalogId, connection);
		allKColl.addDataField("catalogid", kColl.getDataValue("catalogid"));
		allKColl.addDataField("catalogname", kColl.getDataValue("catalogname"));
		allKColl.addDataField("cataloglevel", kColl.getDataValue("cataloglevel"));
		allKColl.addDataField("supcatalogid", kColl.getDataValue("supcatalogid"));
		allKColl.addIndexedCollection(this.getNextShowPrdTreeByFatherCatalogId(fatherCatalogId, bizLine, connection));
		return kColl;
	}
	/**
	 * 通过父节点目录ID获取当前目录下子目录信息,产品信息
	 * @param fatherCatalogId 父目录节点ID
	 * @param bizLine 业务条线
	 * @param connection 数据库连接
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getNextShowPrdTreeByFatherCatalogId(String fatherCatalogId, String bizLine, Connection connection) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		IndexedCollection iColl = new IndexedCollection(fatherCatalogId);
		TableModelDAO dao = this.getTableModelDAO();
		KeyedCollection catKColl = dao.queryDetail("PrdCatalog", fatherCatalogId, connection);
		catKColl.setName("PrdCatalogDetails");
		iColl.addDataElement(catKColl);
		/** 判断目录下是否有子目录 */
		if(this.queryHashCatalogInCatalog(bizLine,fatherCatalogId)){
			IndexedCollection nextIColl = cmisAgent.getNextCatalogByCatalogId(fatherCatalogId);
			for(int i=0;i<nextIColl.size();i++){
				KeyedCollection nextCatKColl = (KeyedCollection)nextIColl.get(i);
				String nextCatalogId = (String)nextCatKColl.getDataValue("catalogid");
				nextCatKColl.addIndexedCollection(this.getNextShowPrdTreeByFatherCatalogId(nextCatalogId, bizLine, connection));
				iColl.addDataElement(nextCatKColl);
				
				/** 向目录下添加产品 *//*
				if(this.isLeaf(bizLine, nextCatalogId)){
					IndexedCollection nextPrdIColl = this.getNextPrdByBizlineAndCatalog(bizLine,nextCatalogId);
					if(nextPrdIColl != null && nextPrdIColl.size() > 0){
						nextPrdIColl.setName(nextCatalogId);
						nextCatKColl.addIndexedCollection(nextPrdIColl);
					}
				}
				*//** 向目录下添加子目录 *//*
				if(this.queryHashCatalogInCatalog(bizLine, nextCatalogId)){
					nextCatKColl.addIndexedCollection(this.getNextShowPrdTreeByFatherCatalogId(nextCatalogId, bizLine, connection));
				}*/
			}
		}
		/** 判断目录下是否有产品 */
		if(this.isLeaf(bizLine, fatherCatalogId)){
			IndexedCollection nextPrdIColl = this.getNextPrdByBizlineAndCatalog(bizLine,fatherCatalogId);
			if(nextPrdIColl != null && nextPrdIColl.size() > 0){
				for(int i=0;i<nextPrdIColl.size();i++){
					iColl.addDataElement((KeyedCollection)nextPrdIColl.get(i));
				}
			}
		}
		return iColl;
	}
	/**
	 * 通过父节点，加载父节点下所有有效产品的子节点
	 * @param fatherCatalogId 父节点ID
	 * @param bizLine 业务条线
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getShowPrdTreeByFatherCatalogId(String fatherCatalogId, String bizLine, Connection connection) throws Exception {
		PrdPolcySchemeAgent cmisAgent = (PrdPolcySchemeAgent)this.getAgentInstance(PRDConstant.PRDPOLCYSCHEMEAGENT);
		KeyedCollection showKColl = new KeyedCollection("KCollList");
		TableModelDAO dao = this.getTableModelDAO();
		if(this.isLeaf(bizLine, fatherCatalogId)){
			/** 判断目录下是否有子目录 */
			IndexedCollection nextIColl = cmisAgent.getNextCatalogByCatalogId(fatherCatalogId);
			IndexedCollection nextCatIColl = new IndexedCollection("PrdCatalogList");
			if(nextIColl != null && nextIColl.size() > 0){
				nextIColl.setName("PrdCatalogList");
				//showKColl.addIndexedCollection(nextIColl);
				for(int i=0;i<nextIColl.size();i++){
					KeyedCollection nextCatKColl = (KeyedCollection)nextIColl.get(i);
					String nextCatalogId = (String)nextCatKColl.getDataValue("catalogid");
					if(this.isLeaf(bizLine, nextCatalogId)){
						//nextCatIColl.addDataElement(nextCatKColl);
						KeyedCollection nextKcoll = this.getShowPrdTreeByFatherCatalogId(nextCatalogId, bizLine, connection);
						nextCatKColl.addKeyedCollection(nextKcoll);
						nextCatIColl.addDataElement(nextCatKColl);
					}
				}
				showKColl.addIndexedCollection(nextCatIColl);
				
			}
			/** 目录下产品信息 */
			IndexedCollection nextPrdIColl = this.getNextPrdByBizlineAndCatalog(bizLine,fatherCatalogId);
			nextPrdIColl.setName("PrdBasicInfoList");
			showKColl.addIndexedCollection(nextPrdIColl);
			/** 目录本身信息 */
			KeyedCollection catKColl = dao.queryDetail("PrdCatalog", fatherCatalogId, connection);
			catKColl.setName("PrdCatalogDetails");
			showKColl.addKeyedCollection(catKColl);
		}
		return showKColl;
	}
}
