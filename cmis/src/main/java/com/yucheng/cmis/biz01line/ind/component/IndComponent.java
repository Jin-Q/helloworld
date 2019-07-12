package com.yucheng.cmis.biz01line.ind.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.agent.IndAgent;
import com.yucheng.cmis.biz01line.ind.agent.IndModelAgent;
import com.yucheng.cmis.biz01line.ind.domain.IndGroupDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndLibDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndModelDomain;
import com.yucheng.cmis.biz01line.ind.interfaces.IndGroupScoreIface;
import com.yucheng.cmis.biz01line.ind.interfaces.IndIndexScoreIface;
import com.yucheng.cmis.biz01line.ind.interfaces.IndModelScoreIface;
import com.yucheng.cmis.biz01line.ind.interfaces.IndexValueIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.StringUtil;

/**
 * 
 *@Classname	CMISComponent.java
 *@Version 1.0	
 *@Since   1.0 	Sep 26, 2008 10:10:39 PM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description
 *@Lastmodified liuzr
 *@Author
 */
public class IndComponent extends CMISComponent {
	
	private static final Logger logger = Logger.getLogger(IndComponent.class);
	
	/**
	 * 
	 * @return
	 * @throws ComponentException
	 */
	private final static String agentId= IndPubConstant.IND_AGENT;
	
	public String addModel(IndModelDomain modeldomain)throws ComponentException {
		String strReturnMessage = CMISMessage.ADDDEFEAT;
		//创建代理类
		IndModelAgent modelAgent=(IndModelAgent)this.getAgentInstance(IndPubConstant.IND_MODEL);
		strReturnMessage=modelAgent.insert(modeldomain);
		return strReturnMessage;
	}
	
	public String updateModel(IndModelDomain modeldomain)throws ComponentException{
		
		return "";
	}
	
	/**
	 * 通过模型编号，获取相关组信息的方法
	 * <p>注意对接的表中的每个主要都要设置
	 * @param modelId	表模型ID	
	 * @param conditinonValues 要查询的模型条件
	 * @return	相应的查询对象
	 * @throws ComponentException
	 */
	public <CMISDomain>ArrayList queryIndGroupDomain( String conditionValues) throws ComponentException{
		ArrayList domainList=null;
		String modelId = IndPubConstant.IND_GROUP;
		String Sub_Model_Id = IndPubConstant.IND_MODEL_GROUP;
		String className = IndPubConstant.INDGROUP_className;
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 domainList = indAgent.query(modelId, conditionValues,Sub_Model_Id,className);
		} catch(Exception e){
			logger.error("方法 queryIndGroupDomain 执行失败...."+e.getMessage(), e);
			throw new ComponentException(CMISMessage.QUERYERROR,"查询失败");
  	  	} 	  	
  	  	return domainList;
	}
	
	
	/**
	 * 通过组编号，获取相关提标信息的方法
	 * <p>注意对接的表中的每个主要都要设置
	 * @param modelId	表模型ID	
	 * @param conditinonValues 要查询的模型条件
	 * @return	相应的查询对象
	 * @throws ComponentException
	 */
	public <CMISDomain>ArrayList queryIndGroupIndexDomain( String conditionValues) throws ComponentException{
		ArrayList domainList=null;
		String modelId = IndPubConstant.IND_LIB;
		String Sub_Model_Id = IndPubConstant.IND_GROUP_INDEX;
		String className=IndPubConstant.INDLIB_className;
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 domainList = indAgent.query(modelId, conditionValues,Sub_Model_Id,className);		
		} catch(Exception e){
			logger.error("方法  queryIndGroupIndexDomain 执行失败...."+e.getMessage(), e);
			throw new ComponentException(CMISMessage.QUERYERROR,"查询失败");
  	  	} 	  	
  	  	return domainList;
	}
	/**
	 * 生成企事业分类分析页面
	 * @param modelNo
	 * @throws ComponentException
	 */
	public void genCusComJspFile(String modelNo) throws ComponentException{
		try{
			IndAgent indAgent= (IndAgent)this.getAgentInstance(IndPubConstant.IND_AGENT);
			indAgent.genCusComJspFile(modelNo);
		}catch(AgentException ae){
			logger.error("方法 genCusComJspFile 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
	}
	
	public void genCcrJspFile(String modelNo) throws ComponentException{
		try{
			IndAgent indAgent= (IndAgent)this.getAgentInstance(IndPubConstant.IND_AGENT);
			indAgent.genCcrJspFile(modelNo);
		}catch(AgentException ae){
			logger.error("方法 genCcrJspFile 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
	}
	public void genCcrJspFileWithShuffle(String modelNo) throws ComponentException{
		try{
			IndAgent indAgent= (IndAgent)this.getAgentInstance(IndPubConstant.IND_AGENT);
			indAgent.genCcrJspFileWithShuffle(modelNo);
		}catch(AgentException ae){
			logger.error("方法 genCcrJspFile 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
	}
	/**
	 * 查询模型下的所有组信息
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryModGrpList(String modelNo) throws ComponentException{ 
		ArrayList<HashMap> list=null;
		try{
			IndAgent agent =(IndAgent) this.getAgentInstance(IndPubConstant.IND_AGENT);
			list=agent.queryModGrpList(modelNo);
		}catch(AgentException ae){
			logger.error("方法 queryModGrpList 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		return list;
	}
	/**
	 * 查询组下的所有指标信息
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryGrpIndexesList(String groupNo) throws ComponentException{ 
		ArrayList<HashMap> list=null;
		try{
			IndAgent agent =(IndAgent) this.getAgentInstance(IndPubConstant.IND_AGENT);
			list=agent.queryGrpIndexesList(groupNo);
		}catch(AgentException ae){
			logger.error("方法 queryGrpIndexesList 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		return list;
	}
	/**
	 * 查询模型下的所有组信息
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryIndOptList(String indexNo) throws ComponentException{ 
		ArrayList<HashMap> list=null;
		try{
			IndAgent agent =(IndAgent) this.getAgentInstance(IndPubConstant.IND_AGENT);
			list=agent.queryIndOptsList(indexNo);
		}catch(AgentException ae){
			logger.error("方法 queryIndOptList 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		return list;
	}
	
	/**
	 * 
	 * @param indexno
	 * @param hm
	 * @return
	 * @throws ComponentException 
	 */
	public String getIndexValue(String indexno,HashMap<String,String> hm) throws ComponentException { 
		HashMap<String,String> Map = new HashMap<String,String>(); 
		String retVal = "";
		try {
			// 创建对应的代理类,获取列表
			IndAgent indAgent = (IndAgent) this.getAgentInstance(agentId);
			Map = indAgent.queryIndLibDetail(indexno);
		} catch (Exception e) {
			logger.error("方法 getIndexValue 执行失败...." + e.getMessage(), e);
			throw new ComponentException(CMISMessage.QUERYERROR, "查询失败");
		} 
		String InPutClassPath = Map.get("input_classpath"); 
		if (InPutClassPath!=null&&!InPutClassPath.trim().equals("")) {
			try {
				logger.info("实例化指标"+indexno+",取数类>>>>>>>>>>>>>>>"+InPutClassPath);
				Object obj = Class.forName(InPutClassPath.trim()).newInstance(); 
				IndexValueIface ivi = (IndexValueIface) obj;
				retVal = ivi.getValue(hm, this.getConnection(),this.getContext());
				logger.info("指标"+indexno+",结果值:"+retVal);
				if(retVal==null){
					retVal="";
				}
			} catch (Exception e) {
				logger.error("方法 getIndexValue 执行失败...." + e.getMessage(), e);
				throw new ComponentException(e);
			}
		}
		return retVal;
	}

	/**
	 *  获取最小值
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public String getMinValue(HashMap<String,String> hm)throws ComponentException{
		
		Iterator KeyIter = hm.keySet().iterator();
	    String key = "";
	    String value = "";
	    int minValue = 9999;
	    String keyValue = "";
		while(KeyIter.hasNext()){
			 key=KeyIter.next().toString();
			 value=hm.get(key);
             int t = Integer.parseInt(value);
             minValue = minValue < t ? minValue:t;
		}
		keyValue = (minValue == 9999 ? "": String.valueOf(minValue));
		
		return keyValue;
	}
	
	/**
	 *  获取最大值
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public String getMaxValue(HashMap<String,String> hm)throws ComponentException{
		
	Iterator KeyIter = hm.keySet().iterator();
    String key = "";
    String value = "";
    int maxValue = -9999;
	String keyValue = "";
    while(KeyIter.hasNext()){
		 key=KeyIter.next().toString();
		 value=hm.get(key);
         int t = Integer.parseInt(value);
        	 maxValue = maxValue > t ? maxValue:t;
	}
		 keyValue = (maxValue == -9999 ? "" :String.valueOf(maxValue));
	 return keyValue;
	}
	
	/**
	 *  复制模型
	 * @param key_model_no 生成新的模型编号
	 * @param modle_no 需要复制的模型编号
	 * @return
	 * @throws ComponentException
	 */
	public String modelCopy (String key_model_no,String model_no) throws ComponentException
	{
		String strReturnMessage = CMISMessage.ADDDEFEAT;
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 strReturnMessage = indAgent.modelCopy(key_model_no, model_no);
		} catch(Exception e){
		  logger.error("方法 modelCopy 执行失败...."+e.getMessage(), e);	
	  	  throw new ComponentException(CMISMessage.ADDERROR,"复制模型失败");
  	  	} 	  	
		return strReturnMessage;
	}
	/**
	 * 取组得分
	 * @param grpno 组下各个指标的得分集
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public String getGrpScore(String grpno,HashMap hm) throws ComponentException{
		String retVal=""; 
		IndGroupDomain  indGrpDomain = new IndGroupDomain();
		HashMap Map = new HashMap();
		Map.put("group_no", grpno);
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indGrpDomain=(IndGroupDomain)indAgent.queryIndGroupDetail(IndPubConstant.IND_GROUP, Map);
			 if(indGrpDomain.getRatingRules()!=null&&!indGrpDomain.getRatingRules().trim().equals("")){
				 String cp=indGrpDomain.getRatingRules();
				 try {
					Object obj=Class.forName(cp.trim()).newInstance();
					IndGroupScoreIface  igsi=(IndGroupScoreIface)obj;
				    retVal=igsi.getGrpScore(grpno,hm,this.getConnection());
				} catch (Exception e) {  
					logger.error("方法 getGrpScore 执行失败...."+e.getMessage(), e);	
					 throw new ComponentException(e);
				}  
			 }
		}catch(AgentException ae){
			logger.error("方法 getGrpScore 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		return retVal;
	}
	
	/**
	 * 取企事业和信用评级模型得分
	 * @param modelno
	 * @param hm  模型下各个组的得分集
	 * @return
	 * @throws ComponentException
	 */
	public String getModelScore(String modelno,HashMap<?, ?> hm,ArrayList<?> list) throws ComponentException{
		String retVal=""; 
		IndModelDomain  indModDomain = new IndModelDomain();
		HashMap<String, String> Map = new HashMap<String, String>();
		Map.put("model_no", modelno);
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indModDomain=(IndModelDomain)indAgent.queryIndModelDetail(IndPubConstant.IND_MODEL, Map);
			 if(indModDomain.getRatingRules()!=null&&!indModDomain.getRatingRules().trim().equals("")){
				 String cp=indModDomain.getRatingRules();
				 try {
					Object obj=Class.forName(cp.trim()).newInstance();
					IndModelScoreIface  isi=(IndModelScoreIface)obj;
				    retVal=isi.getModelScore(modelno,hm,list,this.getConnection());
				} catch (Exception e) {  
					logger.error("方法 getModelScore 执行失败...."+e.getMessage(), e);
					throw new ComponentException(e);
				}  
			 }else{
				 String errMsg="模型["+modelno+"]未配置评分规则,无法计算得分";
				 logger.error(errMsg);
				 throw new ComponentException(errMsg);
			 }
		}catch(AgentException ae){
			logger.error("方法 getModelScore 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		return retVal;
	}
	
	/**
	 * 取指标得分
	 * @param grpno  组别编号
	 * @param indexno 指标编号
	 * @param indexval  指标值
	 * @return
	 * @throws ComponentException
	 */
	public String getIndScore(String grpno,String indexno,String indexval,HashMap para) throws ComponentException{
		String retVal=""; 
		try {
			// 创建对应的代理类,获取列表
			
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 HashMap<String,String> grpindDetail=indAgent.queryIndGroupIndexDetail(grpno, indexno); 
			 if(grpindDetail==null||grpindDetail.size()==0){
				 return retVal;
			 }
			 if(indexval==null||indexval.trim().equals("")){
				 return retVal;
			 }
			 double dWeight=0;
			 String sWeight=grpindDetail.get("weight");
			 String sDisPro=grpindDetail.get("ind_dis_type");
			 String sRulePath=grpindDetail.get("rule_classpath");
			 String sScoreWay=grpindDetail.get("score_way");
			 String sFullScore=grpindDetail.get("full_score");
			 String stdScore=grpindDetail.get("ind_std_score");
			 String limitFlag = grpindDetail.get("limit_flag");
			 String limitValue = grpindDetail.get("limit_value");
	
			 
			 	try{
			 		dWeight=Double.valueOf(sWeight).doubleValue();
			 	}catch(Exception e){
			 		dWeight=0;
			 	} 
			 if(dWeight<=0
					 ||sDisPro.equals(IndPubConstant.IND_DISPLAY_TYPE_TEXTAREA)){
				 /*
				  * 如果权重点0分，或者是文本域录入的直接返回0
				  */
				 return "0";
			 }else if(sRulePath!=null&&!sRulePath.trim().equals("")){ 
				 try { 
					   if(sScoreWay!=null&&sScoreWay.trim().equals("3")){
						   double dLimitValue = Double.parseDouble(limitValue);
						   
						   //表达式
						   String orgVal=(String)para.get("orgvalue");
						   
						   double dorgVal = Double.parseDouble(orgVal);
						   //limitFlag为1时  原值大于 极限值时 评分结果为0
						   //limitFlag为2时  原值小于 极限值时 评分结果为0
						   if( "1".equals(limitFlag)&&(dorgVal>dLimitValue)){
							   return "0";
						   }else if("2".equals(limitFlag)&&(dorgVal<dLimitValue)){
							   return "0";
						   }
						   double dFullScore = Double.parseDouble(sFullScore);
						   
						   BigDecimal borgVal = new BigDecimal(dorgVal);
						   BigDecimal bFullScore = new BigDecimal(dFullScore);
						   BigDecimal bLimitValue = new BigDecimal(dLimitValue);
						   
						   BigDecimal oneH = new BigDecimal(100);
						   
						   BigDecimal one = new BigDecimal(1);
						   
						   sRulePath=sRulePath.replaceAll("实际值", borgVal.divide(oneH, 4,BigDecimal.ROUND_HALF_UP).toString());
						   sRulePath=sRulePath.replaceAll("参照值", bFullScore.divide(oneH, 4,BigDecimal.ROUND_HALF_UP).toString());
						   sRulePath=sRulePath.replaceAll("极限值", bLimitValue.divide(oneH, 4,BigDecimal.ROUND_HALF_UP).toString());
						   sRulePath=sRulePath.replaceAll("满分值", stdScore); 
						   retVal=StringUtil.getFormulaValue(sRulePath);
						   if(retVal==null||"".equals(retVal)){
							   retVal="0";
						   }
						   BigDecimal retValD = new BigDecimal(retVal);
						   BigDecimal dstdScore = new BigDecimal(stdScore);
						  
						   BigDecimal zero = new BigDecimal(0);
						   retValD = retValD.divide(one, 2,BigDecimal.ROUND_HALF_UP);
						   if (0<retValD.compareTo(dstdScore)){
							   //如果返回值>0则说明最终结果大于 标准分值,应该取标准分值为结果
							   retVal=dstdScore.toPlainString();
						   }else if(0>retValD.compareTo(zero)){
							   //如果返回值<0则说明最终结果应该取0
							   retVal="0";
						   }else{
							   retVal=retValD.toPlainString();
						   }
						   logger.info("sRulePath>>>>>>>>"+sRulePath+",retVal="+retVal);
					   }else{
						    Object obj=Class.forName(sRulePath.trim()).newInstance();
							IndIndexScoreIface  iisi=(IndIndexScoreIface)obj;
						    retVal=iisi.getIndScore(grpno, indexno, indexval,para,this.getConnection());
					   } 
					} catch (Exception e) {  
						e.printStackTrace();
						logger.error("方法 getIndScore 执行失败...."+e.getMessage(), e);
						throw new ComponentException(e);
					}  
			}else if(sDisPro.equals(IndPubConstant.IND_DISPLAY_TYPE_RADIO)
					 ||sDisPro.equals(IndPubConstant.IND_DISPLAY_TYPE_SELECTONE)){
				 /**
				  * 对于单选的直接取选项的得分
				  */ 
				 HashMap<String,String> indOpt=indAgent.queryIndexOptScore(indexno);
				 if(indOpt!=null&&indOpt.size()>0){
					 retVal=indOpt.get(indexval);
					 if(retVal==null){
						 retVal="";
					 }
				 }
			 }else if(sDisPro.equals(IndPubConstant.IND_DISPLAY_TYPE_CHECKBOX)){
				 /**
				  * 对于多选的，目前取得分最高的(分类里面得分高，级别反而低)
				  */
				 HashMap<String,String> indOpt=indAgent.queryIndexOptScore(indexno);
				 String [] vals=indexval.split(IndPubConstant.IND_CHECKBOX_DELIMITER); 
				 if(vals!=null&&vals.length>0){
					 int iSc=0;
					 for(int i=0;i<vals.length;i++){
						 try{
							 String sScore=indOpt.get(vals[i]);
							 int iScore=Integer.parseInt(sScore);
							 iSc=(iScore > iSc ? iScore:iSc);
						 }catch(Exception ex){
							 logger.error("取指标得分出错:"+ex.getMessage());
						 }
					 } 
					 try{
						 retVal=String.valueOf(iSc);
					 }catch(Exception ex){
						 retVal="";
						 logger.error("取指标得分出错:"+ex.getMessage());
					 }
				 }
				 /* String cp=IndPubConstant.IND_MUTISEL_CAL_SC_CP;
				  try { 
						Object obj=Class.forName(cp).newInstance();
						IndIndexScoreIface  iisi=(IndIndexScoreIface)obj;
					    retVal=iisi.getIndScore(grpno, indexno, indexval, this.getConnection());
					} catch (Exception e) {  
						logger.error("方法 getIndScore 执行失败...."+e.getMessage(), e);
						throw new ComponentException(e);
					} */ 
			 }
		}catch(AgentException ae){
			ae.printStackTrace();
			logger.error("方法 getIndScore 执行失败...."+ae.getMessage(), ae);
			throw new ComponentException(ae);
		}
		logger.info("groupno="+grpno+",indexno="+indexno+",indexval="+indexval+",score="+retVal);
		return retVal;
	}
	
	/**
	 * 查询指标信息
	 * @param index_no 指标编号
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndLibDetail(String index_no) throws ComponentException {
		IndLibDomain  indLibDomain = new IndLibDomain();
		HashMap Map = new HashMap();
		Map.put("index_no", index_no);
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indLibDomain=(IndLibDomain)indAgent.queryIndLibDetail(IndPubConstant.IND_LIB, Map);
		} catch (AgentException e)
		{
			logger.error("方法 queryIndLibDetail 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return indLibDomain;
	}
	
	/**
	 * 查询组信息
	 * @param group_no 组编号
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndGroupDetail(String group_no) throws ComponentException {
		IndGroupDomain  indGroupDomain = new IndGroupDomain();
		HashMap Map = new HashMap();
		Map.put("group_no", group_no);
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indGroupDomain=(IndGroupDomain)indAgent.queryIndGroupDetail(IndPubConstant.IND_GROUP, Map);
		} catch (AgentException e)
		{
			logger.error("方法 queryIndLibDetail 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return indGroupDomain;
	}
	
	/**
	 * 查询模型信息
	 * @param model_no 模型编号
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndModelDetail(String model_no) throws ComponentException {
		IndModelDomain  indModelDomain = new IndModelDomain();
		HashMap Map = new HashMap();
		Map.put("model_no", model_no);
		try {
			// 创建对应的代理类,获取列表
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indModelDomain=(IndModelDomain)indAgent.queryIndModelDetail(IndPubConstant.IND_MODEL, Map);
		} catch (AgentException e)
		{
			logger.error("方法 queryIndLibDetail 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return indModelDomain;
	}
	
	
	/**
	 * 将指标得分数据插入指标得分结果表中
	 * @param hs
	 * @return
	 * @throws ComponentException 
	 */	
	public int insertIndResultVal(HashMap<String,String> hs) throws ComponentException 
	{
		int count = 0;
		 try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			count = indAgent.insertIndResultVal(hs);
		} catch (AgentException e) { 
			logger.error("方法 insertIndResultVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return count ;
	}
	
	/**
	 * 根据指标值，指标选项值获取指标名称
	 * @param indexNo  指标编号
	 * @param indexValue 指标选项值
	 * @return
	 * @throws ComponentException
	 */
	public String queryIndDesc(String indexNo,String indexValue) throws ComponentException{
		String Reason = "";
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 Reason = indAgent.queryIndDesc(indexNo,indexValue);
		} catch (AgentException e) { 
			logger.error("方法 queryIndDesc 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return Reason;
	}
	
	/**
	 * 根据编号，日期删除指标结果表里面的记录
	 * @param serno  编号
	 * @param ind_date 日期
	 * @return
	 * @throws ComponentException
	 */
	
	public int deleteIndResultVal(String serno,String ind_date) throws ComponentException
	{
		int count =0;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 count = indAgent.deleteIndResultVal(serno,ind_date);
		} catch (AgentException e) { 
			logger.error("方法 deleteIndResultVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return count;
	}
	/**
	 * 查询指标结果值是否生成
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws ComponentException
	 */
	public String queryIndResVal(String serno,int year,int month,int day,String indexno) throws ComponentException{
		String retVal="";
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 retVal = indAgent.queryIndResVal(serno, year, month, day, indexno);
		} catch (AgentException e) { 
			logger.error("方法 queryIndResVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return retVal;
	}
	/**
	 * 
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws ComponentException
	 */
	public void delIndResVal(String serno,int year,int month,int day,String indexno) throws ComponentException{
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indAgent.delIndResVal(serno, year, month, day, indexno);
		} catch (AgentException e) { 
			logger.error("方法 queryIndResVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	 
	}
	/**
	 * 查询指标结果值是否生成
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws ComponentException
	 */
	public String queryIndResValByNo(String serno,String indexno) throws ComponentException{
		String retVal="";
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 retVal = indAgent.queryIndResValByNo(serno, indexno);
		} catch (AgentException e) { 
			logger.error("方法 queryIndResVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return retVal;
	}
	
	public  ArrayList<HashMap>  queryIndResValList(String serno) throws ComponentException{
		 ArrayList<HashMap>  arr = null;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 arr = indAgent.queryIndResValList(serno);
		} catch (AgentException e) { 
			logger.error("取定量指标值失败"+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return arr;
	}
	
	/**
	 * 
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws ComponentException
	 */
	public void delIndResValByNo(String serno,String indexno) throws ComponentException{
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId);
			 indAgent.delIndResValByNo(serno, indexno);
		} catch (AgentException e) { 
			logger.error("方法 queryIndResVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	 
	}
	/**
	 * 为信用评级取得模型编号
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public String getModelNoForCcr(HashMap<String,String> hm) throws ComponentException {
		String retVal=null;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId); 
			 retVal=indAgent.getModelNoForCcr(hm);
		} catch (AgentException e) { 
			logger.error("方法 queryIndResVal 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	
		return retVal;
	}
	/**
	 *  为信用评级取得指标中设置的报表指标编号列表
	 * @param modelNo
	 * @return
	 * @throws ComponentException 
	 */
	public ArrayList getFncIndexArray(String modelNo) throws ComponentException{
		ArrayList retVal=null;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId); 
			 retVal=indAgent.queryFncIndexRpt(modelNo);
		} catch (AgentException e) { 
			logger.error("方法 getFncIndexArray 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	
				
	return retVal;	
	}
	
	/**
	 * 根据模型编号获取模型得分
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public String getModelAllScore(String modelNo) throws ComponentException{
		String retValue = "";
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId); 
			 retValue=indAgent.getModelAllScore(modelNo);
		} catch (AgentException e) { 
			logger.error("方法 getFncIndexArray 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	
		return retValue;
	}
	
	public ArrayList<HashMap> queryGroupIndexesWithShuffle(Map map) throws ComponentException{
		ArrayList<HashMap> list = null;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId); 
			 list=indAgent.queryGroupIndexesWithShuffle(map);
		} catch (AgentException e) { 
			logger.error("方法 getFncIndexArray 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	
		return list;
	}
	
	public ArrayList<HashMap> queryIndexOptsWithShuffle(Map map) throws ComponentException{
		ArrayList<HashMap> list = null;
		try {
			 IndAgent indAgent= (IndAgent)this.getAgentInstance(agentId); 
			 list=indAgent.queryIndexOptsWithShuffle(map);
		} catch (AgentException e) { 
			logger.error("方法 getFncIndexArray 执行失败...."+e.getMessage(), e);
			throw new ComponentException(e);
		}	
		return list;
	}
	
}
