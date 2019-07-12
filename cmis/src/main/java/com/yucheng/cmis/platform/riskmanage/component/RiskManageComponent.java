package com.yucheng.cmis.platform.riskmanage.component;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.RISKPUBConstant;
import com.yucheng.cmis.platform.riskmanage.agent.RiskManageAgent;
import com.yucheng.cmis.platform.riskmanage.domain.IqpPvRiskResult;
import com.yucheng.cmis.platform.riskmanage.domain.PrdPreventRisk;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * @author zhangpu
 * @since 1.2
 */
public class RiskManageComponent extends CMISComponent {

    /**
     * 新增
     * 
     * @param domain
     *            对象
     * @param modelId
     *            表模型ID
     * @param agentId
     *            AgentID
     * @return String
     * @throws EMPException EMPException
     */
    public String insert(CMISDomain domain, String modelId,String agentId) throws EMPException {
        String strReturnMessage = CMISMessage.ADDDEFEAT;// 返回信息
        try {
            // 创建对应的代理类
            RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
            strReturnMessage = riskManageAgent.insert(domain, modelId);// 得到插入结果
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return strReturnMessage;
    }

    /**
     * 根据主键删除单表数据
     * 
     * @param domainKey 主键名称
     * @param domainId 主键值
     * @param modelId 表模型ID
     * @param agentId AgentID
     * @return 状态
     * @throws EMPException EMPException
     */
    public String delete(String domainKey,String domainId,String modelId,String agentId) throws EMPException {
        // 返回信息
        String strReturnMessage = CMISMessage.DEFEAT;
        try {
            // 创建对应的代理类
            RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
            strReturnMessage = riskManageAgent.delete(domainKey,domainId,modelId);
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return strReturnMessage;
    }

    /**
     * 更新
     * @param domain 对象
     * @param modelId 表模型ID
     * @param agentId agentID
     * @return 状态
     * @throws EMPException EMPException
     */
    public String update(CMISDomain domain,String modelId,String agentId) throws EMPException {
        // 返回信息
        String strReturnMessage = CMISMessage.MODIFYDEFEAT;
        try {
            // 创建对应的代理类
            RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
            strReturnMessage = riskManageAgent.update(domain,modelId);
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return strReturnMessage;
    }

    /**
     * 查询
     * @param modelId 表模型ID
     * @param list 展现字段集合
     * @param conditionStr 查询条件
     * @param pageInfo 页面信息
     * @param agentId AgentID
     * @param classPath 类路径
     * @return List
     * @throws EMPException EMPException
     */
    public List query(String modelId, List list,
            String conditionStr, PageInfo pageInfo,String agentId,String classPath) throws EMPException {
        List result = null;
        try {
            // 创建对应的代理类
            RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
            result = riskManageAgent.query(modelId, list, conditionStr,
                    pageInfo,classPath);
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return result;
    }

    /**
     * 查询
     * 
     * @param modelId 表模型ID
     * @param conditionStr 查询条件
     * @param agentId AgentID
     * @return CMISDomain 对象
     * @throws EMPException EMPException
     */
    public CMISDomain queryDetail(String modelId, String conditionStr,String agentId)
            throws EMPException {
        CMISDomain domain = null;
        try {
            // 创建对应的代理类
        	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
            domain = riskManageAgent.queryDetail(modelId, conditionStr);
            
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return domain;
    }
    
    /**
     * 删除子表数据
     * @param modelId 表模型ID
     * @param pkId 主键ID
     * @param pkValue 主键值
     * @param agentId AGENTID
     * @return 状态
     * @throws EMPException EMPException
     */
    public String deleteSubByPk(String modelId,String pkId,String pkValue,String agentId) throws EMPException{
    	// 返回信息
        String strReturnMessage = CMISMessage.DEFEAT;
        try {
            // 创建对应的代理类
        	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
        	strReturnMessage = riskManageAgent.deleteSubByPk(modelId, pkId, pkValue);
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return strReturnMessage;
    }
    
    /**
     * 删除主子表数据
     * @param modelId 表模型ID
     * @author zhangpu
     * @param pkId 主键值
     * @param pkKey 主键名称
     * @param agentId AGENT编号
     * @return 状态
     * @throws EMPException EMPException
     */
    public String deleteByPk(String modelId,String pkId,String pkKey,String agentId) throws EMPException {
    	// 返回信息
        String strReturnMessage = CMISMessage.DEFEAT;
        try {
            // 创建对应的代理类
        	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(agentId);
        	strReturnMessage = riskManageAgent.deleteByPk(modelId, pkId, pkKey);
        } catch (Exception e) {
            
            throw new EMPException(e);
        }
        return strReturnMessage;
    }
    /**
     * 风险拦截调用风险预警方案，完成调用插入风险拦截应用表
     * @param modelId 业务实体表模型
     * @param serno 业务流水号
     * @param preventIdLst 拦截方案清单
     * @param wfid 流程id
     * @param nodeId 流程节点
     * @throws ComponentException
     */
    public void doInspect(String modelId, String serno,String[] preventIdLst, String wfid,String nodeId)throws ComponentException{
    	
    	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
		//删除风险拦截应用
		riskManageAgent.deleteRiskResult(serno, wfid, nodeId);
    	try{
	    	//执行清单中f每个风险拦截条目
	    	for(int i=0;i<preventIdLst.length;i++){
	    		if(preventIdLst[i] != null && !preventIdLst[i].trim().equals("")){
		    		String preventId2=preventIdLst[i];
					this.doEveryInspect(modelId, serno, wfid, nodeId, preventId2);
	    		}
	    	}
    	}catch(Exception e1){
    		e1.printStackTrace();
    		throw new ComponentException(e1);
    	}
    }
    
  
    /**
     * 执行每个风险拦截
     * @param modelId 业务实体表模型 
     * @param prdPk 产品主键
     * @param serno 业务流水号
     * @param wfid 流程ID
     * @param scenceId 流程节点
     * @param preventId 方案编号
     * @throws ComponentException
     */
    public  void doEveryInspect(String modelId, String serno,String wfid,String nodeId,String preventId) throws ComponentException{
    	   	
    	try {
    		
	    	RiskManageAgent riskManageAgent = (RiskManageAgent) this
				.getAgentInstance("RiskManage");
	    	
	    	// 判断该方案是否启用
	    	if(preventId!=null){
	        	PrdPreventRisk prdPreventRisk = null;
	        	prdPreventRisk=riskManageAgent.getPrdPreventRisk(preventId);
	        	//判断
	        	String ifCanUse=prdPreventRisk.getUsedInd();
	        	if(ifCanUse!=null){
	        		if(ifCanUse.equals("2"))
	        			return ;
	        	}
	    	}
	
	    	List scenceList=null;
	    	Map<String, String> returnMap = new HashMap<String, String>();
	    	HashMap paramp=new HashMap();
	    	paramp.put("serno", serno);
	    	//查询该拦截的所有场景列表PRD_PV_RISK_SCENE
	    	String conditionStr = " where PREVENT_ID='"+preventId+"'";
	    	scenceList=riskManageAgent.queryPrdPvRiskSenceList("PrdPvRiskScene", conditionStr);

    		//循环调用风险拦截规则
	    	// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
					.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi
						.getModualServiceById("shuffleServices",
								"shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0,
						"getModualServiceById error!", e);
				throw new EMPException(e);
			}
	    	UNIDGenerator unidGenerator = new UNIDGenerator();
	    	for(int i=0;i<scenceList.size();i++){
	    		//场景临时kcoll
	    		KeyedCollection kcoll=(KeyedCollection) scenceList.get(i);
	    		//项目序号
	    		String itemId=(String) kcoll.getDataValue("item_id");
	    		//项目描述
	    		String itemDescrib=(String) kcoll.getDataValue("item_name");
	    		//拦截级别
	    		String preventLevel=(String) kcoll.getDataValue("risk_level");
	    		//检查是否通过
	    		String passState="";
	    		
	    		//根据itemId查询风险阻拦项目
	    		Map itemMap= riskManageAgent.queryPrdPvRiskItemDetail(itemId);
	    		itemDescrib=(String) itemMap.get("item_desc");
	    		//外部链接
	    		String linkUrl=(String) itemMap.get("link_url");
	    		if(linkUrl!=null){
	    			linkUrl=linkUrl.replace("$_pkVal", serno);
	    		}else{
	    			linkUrl="";
	    		}
	    		String item_class = (String) itemMap.get("item_class");
	    		//根据表模型获取物理表名
	    		
				TableModelLoader modelLoader = (TableModelLoader)this.getContext().getService(CMISConstance.ATTR_TABLEMODELLOADER);
				TableModel tableModel = modelLoader.getTableModel(modelId);
				String phyTable = tableModel.getDbTableName();
	    		
	    		/** 判断使用风险拦截实现类检查，还是使用规则检查 */
				if(item_class != null && item_class.trim().length() > 0){
					RiskManageInterface riskManagerInterface = (RiskManageInterface)Class.forName(item_class.trim()).newInstance();
					returnMap = riskManagerInterface.getResultMap(modelId,serno,this.getContext(),this.getConnection());
				}else {
					//规则输入参数
		    		Map inputValueMap=new HashMap();
		    		inputValueMap.put("IN_SERNO", serno);
		    		inputValueMap.put("IN_TABNAME", phyTable);
					//取得规则信息：【规则集ID】_【规则ID】
		    		String itemRule=(String) itemMap.get("item_rules");
		    		
		    		//获得规则检查结果
		    		returnMap=shuffleService.fireTargetRule(itemRule.split("\\$")[0],itemRule.split("\\$")[1] , inputValueMap);	
				}
	    		//提示信息
	    		String msg = returnMap.get("OUT_提示信息");
	    		//如果未取得提示信息设默认值
	    		if(msg == null || "".equals(msg)){
	    			msg="无提示信息！";
	    		}	
	    		passState=RISKPUBConstant.WFI_RISKINSPECT_RESULT.get(returnMap.get("OUT_是否通过"));
	    		
	    			
	    		//插入风险拦截应用表 		
	    		
	    		String resultId = unidGenerator.getUNID();
	    		IqpPvRiskResult iqpResult=new IqpPvRiskResult();
	    		
	    		iqpResult.setResultId(resultId);
	    		iqpResult.setSerno(serno);
	    		iqpResult.setWfid(wfid);
	    		iqpResult.setNodeId(nodeId);
	    		iqpResult.setItemId(itemId);
	    		iqpResult.setRiskLevel(preventLevel);
	    		iqpResult.setPassState(passState);
	    		iqpResult.setItemName(itemDescrib);
	    		iqpResult.setItemDesc(msg);
	    		iqpResult.setLinkUrl(linkUrl);
	    		try {
					riskManageAgent.insert(iqpResult, "IqpPvRiskResult");
				} catch (Exception e) {
					throw new ComponentException(e.getMessage());
				}
				returnMap.clear();
    	
	
	    	}
    	}catch(Exception e){
			throw new ComponentException(e.getMessage());
		}
    	
    }

	/**
	 * 返回某流程风险拦截结果条目
	 * @param serno 业务流水号
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return 拦截条目domain list
	 * @throws ComponentException
	 */
	public List getAllRiskItems(String serno, String wfid, String nodeId) throws ComponentException {
		// TODO Auto-generated method stub
    	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
    	
    	return riskManageAgent.getAllRiskItems(serno,wfid, nodeId);
    	
    	
	}
	/**
	 * 返回风险拦截是否通过
	 * @param serno 业务流水号
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return 是否通过
	 * @throws ComponentException 
	 */
	public boolean getIfAccessOfListItem(String serno, String wfid,
			String nodeId) throws ComponentException {
		// TODO Auto-generated method stub
	   	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
    	
    	return riskManageAgent.getIfAccessOfListItem(serno,wfid, nodeId);
 
	}

	/**
	 * 返回风险拦截是否通过
	 * @param  resultItem 检查结果明细
	 * @return 是否通过
	 * @throws ComponentException 
	 */
	public boolean getIfAccessOfListItem(List resultItem) throws ComponentException {
		// TODO Auto-generated method stub
	   	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
    	
    	return riskManageAgent.getIfAccessOfListItem(resultItem);
 
	}
	/**
	 * 清理风险拦截条目
	 * @param serno 业务流水号
	 */
	public void cleanRiskItems(String serno)  throws ComponentException {
		// TODO Auto-generated method stub
	   	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
	   	riskManageAgent.cleanRiskItems(serno);
		
	}

	public List getPreRiskItems(String serno) throws AgentException {
	   	RiskManageAgent riskManageAgent = (RiskManageAgent) this
		.getAgentInstance("RiskManage");
    	
    	return riskManageAgent.getPreRiskItems(serno);
	}
 
	/**
	 * <p>判断流程当前场景下是否需要进行风险拦截检查</p>
	 * @param preventIdLst 拦截方案清单
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return true 需要进行风险拦截检查 false 不需要
	 * @throws ComponentException
	 */
	public boolean isNeedRiskInspected(String[] preventIdLst, String wfid, String nodeId)
			throws ComponentException{
		
		if(preventIdLst == null || preventIdLst.length <= 0){
			throw new ComponentException("参数没有拦截方案清单");
		}
		
		StringBuffer sqlPrvId = new StringBuffer();
		for(int n=0; n<preventIdLst.length; n++){
			sqlPrvId.append(",'").append(preventIdLst[n]).append("'");
		}
		
		if(sqlPrvId.length() < 2){
			throw new ComponentException("参数没有拦截方案清单");
		}
		
    	String conditionStr = " where wfid = '" + wfid + "' and scene_id = '" + nodeId + "' and PREVENT_ID in (" + sqlPrvId.substring(1) + ")";
    	RiskManageAgent riskManageAgent = (RiskManageAgent) this
			.getAgentInstance("RiskManage");
    	
		List scenceList=riskManageAgent.queryPrdPvRiskSenceList("PrdPvRiskScene", conditionStr);
		if(scenceList == null || scenceList.size() <= 0){
		   return false;
		}else{
			return true;
		}
	}
	
	
	
	  /**
	   * @author wangak
	   * @param cusId
	   * @return
	   * @throws ComponentException
	   */
	  public String getCusTypeFromCusLoanRel(String cusId) throws ComponentException {
	       RiskManageAgent riskManageAgent = (RiskManageAgent) this
	    .getAgentInstance("RiskManage");
	       
	       return  riskManageAgent.getCusTypeFromCusLoanRel(cusId);
	  }
	  
	  
	  /**
	   * @author wangak
	   * @param contNo
	   * @return
	   * @throws ComponentException
	   */
	  public String getCusIdFromComLoanEntr(String contNo) throws ComponentException {
	       RiskManageAgent riskManageAgent = (RiskManageAgent) this
	    .getAgentInstance("RiskManage");
	       
	       return  riskManageAgent.getCusIdFromComLoanEntr(contNo);
	  }
	  
	    public List query(String tablename,String cusType,String dbfs,String term_type,String componentID) throws EMPException {
	        List result = null;
	        try {
	            // 创建对应的代理类
	            RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance(componentID);
	            result = riskManageAgent.queryDataOfMapListByCondition(tablename,cusType,dbfs,term_type);
	        } catch (Exception e) {
	            
	            throw new EMPException(e);
	        }
	        return result;
	    }
	    /**
	     * 通过拦截方案ID获取拦截场景列表
	     * @param preventIdValue 拦截方案ID
	     * @param connection
	     * @return
	     * @throws Exception
	     */
		public IndexedCollection queryListByPreventId(String preventIdValue, Connection connection) throws Exception {
	    	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance("RiskManage");
	    	return riskManageAgent.queryListByPreventId(preventIdValue,connection);
		}
		/**
		 * 执行SQL，返回执行结果，特殊情况使用
		 * @param sql 执行SQL
		 * @param connection
		 * @return
		 * @throws Exception
		 */
		public IndexedCollection doSqlExecute(String sql ,Connection connection) throws Exception {
	    	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance("RiskManage");
	    	return riskManageAgent.doSqlExecute(sql,connection);
		}
		/**
		 * 执行删除SQL语句
		 * @param sql 删除SQL
		 * @param connection
		 * @throws Exception
		 */
		public void doDeleteSqlExecute(String sql ,Connection connection) throws Exception {
	    	RiskManageAgent riskManageAgent = (RiskManageAgent)this.getAgentInstance("RiskManage");
	    	riskManageAgent.doDeleteSqlExecute(sql,connection);
		}
}
