/**
 * 
 */
package com.yucheng.cmis.platform.riskmanage.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.platform.riskmanage.dao.RiskManageDao;
import com.yucheng.cmis.platform.riskmanage.domain.IqpPvRiskResult;
import com.yucheng.cmis.platform.riskmanage.domain.PrdPreventRisk;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * @author zhangpu
 * @since 1.2
 */
public class RiskManageAgent extends CMISAgent {
	
	/**
	 * 日志类
	 */
	//private Logger log = Logger.getLogger(RiskManageAgent.class);
	
    /**
     * 新增
     * 
     * @param domain 对象
     * @param modelId 表模型ID
     * @return 新增状态（成功/失败）
     * @throws Exception
     */
    public String insert(CMISDomain domain, String modelId) throws Exception {
        String strMessage = CMISMessage.ADDDEFEAT; // 错误信息

        // 新增对象
        int intMessage = this.insertCMISDomain(domain, modelId);
        if (1 == intMessage) {
            strMessage = CMISMessage.ADDSUCCEESS;// 添加成功
        }
        return strMessage;
    }

    /**
     * 根据主键删除单表数据
     * 
     * @param domainKey 主键名称
     * @param domainId 主键值
     * @param modelId 表模型ID
     * @return 删除状态（成功/失败）
     * @throws Exception 
     */
    public String delete(String domainKey, String domainId, String modelId)
            throws Exception {
        String strMessage = CMISMessage.DELETEERROR; // 错误信息

        // 创建MAP存储 对象编号
        Map<String, String> pkValues = new HashMap<String, String>();
        pkValues.put(domainKey, domainId);

        // 根据主键删除信息
        int count = this.removeCMISDomainByKeywords(modelId, pkValues); // 1成功
                                                                        // 其他失败
        // 如果成功，给标志信息赋值
        if (1 == count) {
            strMessage = CMISMessage.SUCCESS; // 成功
        }
        return strMessage;
    }
    
    /**
     * 删除主子表数据
     * @param modelId 表模型ID
     * @param pkId 主键值
     * @param pkKey 主键名称
     * @return 删除状态（成功/失败）
     * @throws Exception
     */
    public String deleteByPk(String modelId,String pkId,String pkKey) throws Exception {
        String strMessage = CMISMessage.DELETEERROR; // 错误信息       
        Connection conn = null;
        try {
            conn = this.getConnection();

            // 得到表模型
            TableModelDAO tDao = this.getTableModelDAO();

            // 删除数据
            int count = tDao.deleteAllByPk(modelId, pkId, conn);
            if (1 == count) {
                strMessage = CMISMessage.SUCCESS; // 成功
            }
        } catch (Exception e) {
            EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "删除失败\n"
                    + e.toString());
            throw new AgentException(CMISMessage.QUERYERROR, "删除失败");
        }
        return strMessage;
    }
    
    /**
     * 删除子表数据
     * @param modelId 表模型ID
     * @param pkId 主键ID
     * @param pkValue 主键值
     * @return 删除状态（成功/失败）
     * @throws Exception
     */
    public String deleteSubByPk(String modelId,String pkId,String pkValue) throws Exception {
        String strMessage = CMISMessage.DELETEERROR; // 错误信息       
        Connection conn = null;
        int count = 0;
        try {
            conn = this.getConnection();
            RiskManageDao Rdao = new RiskManageDao();
            count = Rdao.deleteSubTabByPk(modelId, pkId, pkValue, conn);
            if (1 == count) {
                strMessage = CMISMessage.SUCCESS; // 成功
            }
        } catch (Exception e) {
            EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "删除失败\n"
                    + e.toString());
            throw new AgentException(CMISMessage.QUERYERROR, "删除失败");
        }    
        return strMessage;
    }

    /**
     * 更新
     * @param domain 对象
     * @param modelId 表模型ID
     * @return 更新状态（成功/失败）
     * @throws Exception
     */
    public String update(CMISDomain domain, String modelId) throws Exception {
        String strMessage = CMISMessage.MODIFYDEFEAT; // 错误信息

        // 更新信息
        int count = this.modifyCMISDomain(domain, modelId);// 1成功
        // 其他失败

        // 如果成功，给标志信息赋值
        if (1 == count) {
            strMessage = CMISMessage.SUCCESS; // 成功
        }
        return strMessage;
    }

	/**
	 * 查询
	 * 
	 * @param modelId 表模型ID
	 * @param list 展现列集合
	 * @param conditionStr 条件
	 * @param pageInfo 页面信息
	 * @param classPath 类路径
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	public List query(String modelId, List list,
			String conditionStr, PageInfo pageInfo,String classPath) throws EMPException {
		Connection conn = null;
		IndexedCollection iCol = null;
		List result = new ArrayList();
		try {
			conn = this.getConnection();

			// 得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			// 查询数据
			iCol = tDao.queryList(modelId, list, conditionStr, pageInfo, conn);

			// 将DAO返回的ICOL转换成LIST
			ComponentHelper cHelper = new ComponentHelper();
			CMISDomain domain = (CMISDomain) Class.forName(classPath + modelId)
					.newInstance();			
			result = cHelper.icol2domainlist(domain, iCol);

		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
		} 
		return result;
	}

    /**
     * 查询
     * 
     * @param modelId 表模型ID
     * @param conditionStr 条件
     * @return CMISDomain 
     * @throws EMPException
     */
    public CMISDomain queryDetail(String modelId, String conditionStr)
            throws EMPException {
        Connection conn = null;
        //KeyedCollection kCol = null;
        
        //这个domain必须实例化，否则无法返回
        CMISDomain domain = new PrdPreventRisk();
        
        try {
            conn = this.getConnection();
            //conditionStr必须是唯一主键不是条件sql语句
            domain=this.findCMISDomainByKeyword(domain, modelId, conditionStr);
            
         /*   // 得到表模型
            TableModelDAO tDao = this.getTableModelDAO();

            // 查询数据
            kCol = tDao.queryAllDetail(modelId, conditionStr, conn);
            
            // 将KCOL转换成DOMAIN
            ComponentHelper cHelper = new ComponentHelper();
            domain = cHelper.kcolTOdomain(modelId, kCol);
          */
        } catch (Exception e) {
            EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
                    + e.toString());
            throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
        }
        return domain;
    }
    
    /**
     * 获取风险拦截项目详情mp
     * @author zhangpu
     * @param itemId 条目编号
     * @return 详情mp
     * @throws AgentException
     */
    public Map queryPrdPvRiskItemDetail(String itemId) throws AgentException{
		
    	TableModelDAO tDao=this.getTableModelDAO();
    	Connection con = this .getConnection();
         
	    IndexedCollection intm = null;
	    try {
			intm=tDao.queryList(PUBConstant.PRDPVRISKITEM, "where ITEM_ID ='" + itemId + "'", con);
		} catch (EMPException e) {
			// TODO Auto-generated catch block
			throw new AgentException(e);
		}
		
		if(intm.size()==0){
			throw new AgentException("获取风险条目明细失败");
		}
    	
    	return (Map) intm.get(0);
    	
    }
    /**
     * 装载所有条目
     * @param preventId 方案编号
     * @return 条目 map
     * @throws AgentException
     */
    public Map queryItemList(String preventId) throws AgentException{
    	
    	TableModelDAO tDao=this.getTableModelDAO();
    	Connection con = this .getConnection();
        Map mp = new HashMap();
    	
	    IndexedCollection intm = null;
	    try {
			intm=tDao.queryList(PUBConstant.PRDPVRISKITEM, "where PREVENT_ID ='" + preventId + "'", con);
		} catch (EMPException e) {
			throw new AgentException(e);
		}
		
		
		for(int i=0;i<intm.size();i++){
			
			KeyedCollection keyCol = (KeyedCollection) intm.get(i);
			String itemId = "";
			try {
				itemId = (String) keyCol.getDataValue("ITEM_ID".toLowerCase());
			} catch (EMPException e) {
				throw new AgentException(e);
			}
			mp.put(itemId.trim(), keyCol);
		}
		
		return mp;
    	
    }
    
    /**
     * 装载所有的风险拦截方案
     * @param allItems 项目编号
     * @return
     * @throws AgentException
     */
    public Map queryAllSchemes(Map allItems) throws AgentException{
    	
    	Iterator it = allItems.keySet().iterator();
    	String strs ="";
    	Map mp = new HashMap();
    	
    	while (it.hasNext()) {
    		String key = (String) it.next();
    		KeyedCollection keyCold = (KeyedCollection) allItems.get(key);
    		try {
				strs=strs+"'"+((String) keyCold.getDataValue("RISK_INDEX_ID".toLowerCase())).trim()+"',";
			}catch (EMPException e) {
				throw new AgentException(e);
			}
    	}
    	
    	if(strs.length()!=0){
    		strs=strs.substring(0, strs.length()-1);
    	}
    	
    	TableModelDAO tDao=this.getTableModelDAO();
    	Connection con = this .getConnection();
    	
    	//REW_SCHEME
	    IndexedCollection intm = null;
	    try {
			intm=tDao.queryList("RewScheme", "where trim(SCHEME_ID) in(" + strs + ")", con);
		} catch (EMPException e) {
			throw new AgentException(e);
		}
        
		for(int i=0;i<intm.size();i++){
			
			KeyedCollection keyCol = (KeyedCollection) intm.get(i);
			String itemId = "";
			try {
				itemId = (String) keyCol.getDataValue("SCHEME_ID".toLowerCase());
			} catch (EMPException e) {
				throw new AgentException(e);
			}
			mp.put(itemId.trim(), keyCol);
		}
		return mp;
    }
    
    /**
     * 删除风险拦截应用中相应的应用场景
     * @param serno 业务流水号
     * @param wfid 流程类型
     * @param nodeId 场景类型
     * @param itemId 
     * @return 是否成功
     * @throws AgentException 
     */
    public boolean deleteRiskResult(String serno,String wfid,String nodeId) throws AgentException{
    	
    	Connection con = this .getConnection();
    	Map param=new HashMap();
    	param.put("SERNO", serno);
    	param.put("wfid", wfid);
        param.put("node_id", nodeId);
        //param.put("item_id", itemId);
    	
    	RiskManageDao riskManageDao=new RiskManageDao();
    	try{
    	 riskManageDao.deleteTableBycondition("IQP_PV_RISK_RESULT", param, con);
    	}catch(Exception e){
    		throw new AgentException(e);
    	}
		return false;
    	
    }
    /**
     * 查询场景
     * @author zhangpu
     * @param tableModel 查询表名
     * @param condition 查询条件
     * @return 列表
     * @throws AgentException
     */
    public List queryPrdPvRiskSenceList(String tableModel,String condition) throws AgentException{
		
    	TableModelDAO tDao=this.getTableModelDAO();
    	Connection con = this .getConnection();
         
	    IndexedCollection intm = null;
	    try {
			intm=tDao.queryList(tableModel, condition, con);
		} catch (EMPException e) {
			// TODO Auto-generated catch block
			throw new AgentException(e);
		}
		
    	
    	return intm;
    	
    }

	/**
	 * 获取最新消息keycoll
	 * @param msgIcoll
	 * @return 记录明细
	 * @throws EMPException 
	 */
	public KeyedCollection getLastMsg(IndexedCollection msgIcoll) throws EMPException {
		// TODO Auto-generated method stub
		
		String currentDate="";
		String maxDate="";
		KeyedCollection keMax=null;
		
		for(int i=0;i<msgIcoll.size();i++){
			
			KeyedCollection ktmp=(KeyedCollection) msgIcoll.get(i);
			try{
			currentDate=(String) ktmp.getDataValue("GEN_DATE".toLowerCase());
			}catch(EMPException e){
				return ktmp;
			}
			if(i==0){
				maxDate=(String) ktmp.getDataValue("GEN_DATE".toLowerCase());
				keMax=ktmp;
			}
			
			if(maxDate.compareTo(currentDate)<0){
				maxDate=currentDate;
				keMax=ktmp;
			}
		}
		
		return keMax;
	}
    /**
     *删除实时消息池中对应的数据
     * @param serno 业务流水号
     * @param schemeId 方案编号
     * @param bizType 业务类型 
     * @return 是否成功
     * @throws AgentException 
     */
    public boolean deleteRealTimeMsg(String serno) throws AgentException{
    	
    	Connection con = this .getConnection();
    	Map param=new HashMap();
    	param.put("BIZ_PK", serno);
    	//param.put("BIZ_TYPE", bizType);
    	
    	RiskManageDao riskManageDao=new RiskManageDao();
    	try{
    	 riskManageDao.deleteTableBycondition("REW_MSG_REALTIME", param, con);
    	}catch(Exception e){
    		throw new AgentException(e);
    	}
		return false;
    	
    }

	/**
	 * 返回某流程风险拦截结果条目
	 * @param serno 业务流水号
	 * @param wfid 流程类型
	 * @param nodeId 场景类型
	 * @return 拦截条目domain list
	 * @throws AgentException
	 */
	public List getAllRiskItems(String serno, String wfid, String nodeId) throws AgentException{
		// TODO Auto-generated method stub
		
		RiskManageDao riskManageDao=new RiskManageDao();
		Connection con=this.getConnection();
		List ls=null;
		//
		String condition =" SERNO ='"+serno+"' and wfid='"+wfid+"' and node_id='"+nodeId+"'";
		if(nodeId == null){///为空则不用场景这个条件
			condition =" SERNO ='"+serno+"' and wfid='"+wfid+"' and risk_level!='00'";
		}
		
		try{
			ls=riskManageDao.ListgetAllItem(condition, con);
		}catch(Exception e){
			throw new AgentException(e);
		}
		return ls;
	}
	
	/**
	 * 返回风险拦截是否通过
	 * @param serno 业务流水号
	 * @param wfid 流程类型
	 * @param nodeId 场景类型
	 * @return 是否通过
	 * @throws AgentException
	 */
	public boolean getIfAccessOfListItem(String serno, String wfid, String nodeId) throws AgentException{
		
		List ls=this.getAllRiskItems(serno, wfid, nodeId);
		
	    return getIfAccessOfListItem(ls);
	}

	/**
	 * 返回风险拦截是否通过
	 * @param  resultItem 检查结果明细
	 * @return 是否通过
	 * @throws AgentException
	 */
	public boolean getIfAccessOfListItem(List resultItem) throws AgentException{
		if(resultItem==null){
			return true;
		}else{
			for(int i=0;i<resultItem.size();i++){
				
				IqpPvRiskResult iqpPvRiskResult=(IqpPvRiskResult) resultItem.get(i);
				String access=iqpPvRiskResult.getPassState();
				String riskLevel=iqpPvRiskResult.getRiskLevel();
				if(access.trim().equals(PUBConstant.WFI_RISKINSPECT_RESULT_DENY)&&riskLevel.equals(PUBConstant.WFI_RISKINSPECT_RESULT_DENY)){
					return false;
				}		
			}
		}
		return true;
	}

	/**
	 * 根据产品编号，期限，担保方式 添加科目dic
	 * @param prdPk 产品pk
	 * @param term 产品期限（单位月）
	 * @param guarantyType 担保方式
	 * @throws ComponentException
	 */
	public Object setDicOfAccount(String prdPk, String term, String guarantyType) {
		// TODO Auto-generated method stub
		return null;
	}
	public PrdPreventRisk getPrdPreventRisk(String preventId) throws AgentException{
		
		PrdPreventRisk prdPreventRisk= new PrdPreventRisk();
		prdPreventRisk=(PrdPreventRisk) this.findCMISDomainByKeyword(prdPreventRisk, "PrdPreventRisk", preventId);
		
		return prdPreventRisk;
		
	}

	/**
	 * 清理风险拦截条目
	 * @param serno 业务流水
	 */ 
	public void cleanRiskItems(String serno) {
		// TODO Auto-generated method stub
		RiskManageDao riskManageDao=new RiskManageDao();
		Connection con = this.getConnection();
		Map condition = new HashMap();
		Map condition2= new HashMap();
		condition2.put("BIZ_PK".toLowerCase(), serno);
		condition.put("serno", serno);
		try {
			riskManageDao.deleteTableBycondition("iqp_pv_risk_result", condition, con);
			riskManageDao.deleteTableBycondition("REW_MSG_REALTIME".toLowerCase(), condition2, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param prdPk
	 * @return
	 */
	public IndexedCollection getAllPreventRisk(String prdPk){
		
		TableModelDAO tDao = this.getTableModelDAO();
		Connection con = this.getConnection();
		IndexedCollection riskIcol=null;
		try {
			riskIcol=tDao.queryList("PrdRiskMp", " where PRD_PK ='"+prdPk+"'", con);
		} catch (EMPJDBCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return riskIcol;
		
	}

	public List getPreRiskItems(String serno) throws AgentException {
		RiskManageDao riskManageDao=new RiskManageDao();
		Connection con=this.getConnection();
		List ls=null;		
		try{
			ls=riskManageDao.getPreRiskItems(serno, con);
		}catch(Exception e){
			throw new AgentException(e);
		}
		return ls;
	}
	
	
	/**
	 * @author wangak
	 * @param cusId
	 * @return
	 * @throws AgentException
	 */
	public String getCusTypeFromCusLoanRel(String cusId)throws AgentException{
		Map new_map =new HashMap();
		new_map =this.queryDataOfMapByCondition("select distinct cus_type from cus_loan_rel where cus_status in ('20','03') and cus_id='"+cusId+"'");
		String cus_type =new_map.get("cus_type").toString();
		return cus_type;
	}
	
	/**
	 * @author wangak
	 * @param cusId
	 * @return
	 * @throws AgentException
	 */
	public String getCusIdFromComLoanEntr(String contNo)throws AgentException{
		Map new_map =new HashMap();
		new_map =this.queryDataOfMapByCondition("select principal_cus_id from ctr_com_loan_entr  where cont_no='"+contNo+"'");
		String cus_id =new_map.get("principal_cus_id").toString();
		return cus_id;
	}
	
	
	/**
	 * 
	 * @param tablename
	 * @param cusType
	 * @param dbfs
	 * @param term_type
	 * @return
	 * @throws EMPException
	 */
	public List queryDataOfMapListByCondition(String tablename,String cusType,String dbfs,String term_type) throws EMPException {
		Connection conn = null;
		IndexedCollection iCol = null;
		List result = new ArrayList();
		try {
			conn = this.getConnection();
			StringBuffer sBuffer =new StringBuffer();
			sBuffer.append("select * from "+tablename).append(" where cus_type ='"+cusType+"' ")
			.append(" and guaranty_type ='"+dbfs+"' and  term_type ='"+term_type+"' ");
			result =this.queryDataOfMapListByCondition(sBuffer.toString());


		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
					+ e.toString());
			throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
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
	public IndexedCollection queryListByPreventId(String preventIdValue,
			Connection connection)throws Exception {
		RiskManageDao riskManageDao=new RiskManageDao();
		return riskManageDao.queryListByPreventId(preventIdValue,connection);
	}
	/**
	 * 执行SQL，返回执行结果，特殊情况使用
	 * @param sql 执行SQL
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection doSqlExecute(String sql ,Connection connection)throws Exception {
		RiskManageDao riskManageDao=new RiskManageDao();
		return riskManageDao.doSqlExecute(sql,connection);
	}
	/**
	 * 执行删除SQL语句
	 * @param sql 删除SQL
	 * @param connection
	 * @throws Exception
	 */
	public void doDeleteSqlExecute(String sql ,Connection connection) throws Exception {
		RiskManageDao riskManageDao=new RiskManageDao();
		riskManageDao.doDeleteSqlExecute(sql,connection);
	}
}
