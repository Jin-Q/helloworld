package com.yucheng.cmis.platform.shuffle.msi;

import java.util.ArrayList;
import java.util.Map;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 
 * <p>
 * 	规则管理模块对外提供的服务接口
 * </P
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
@ModualService(serviceId="shuffleServices",serviceDesc="规则引擎对外提供的服务接口",
				modualId="shuffle",modualName="规则管理模块",className="com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface")
public interface ShuffleServiceInterface {

	/**
	 * 执行规则集ruleSetId下的规则ruleId
	 * @param ruleSetId 规则集ID
	 * @param ruleId 规则ID
	 * @param inputValueMap 输入参数
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="fireTargetRule",desc="执行单条规则",
			inParam={
			@MethodParam(paramName="ruleSetId",paramDesc="规则集ID"),
			@MethodParam(paramName="ruleId",paramDesc="规则ID"),
			@MethodParam(paramName="inputValueMap",paramDesc="输入参数Map"),
		    },
		    outParam=@MethodParam(paramName="outputMap",paramDesc="Map 规则输出键值对"))
	public Map<String,String> fireTargetRule(String ruleSetId,String ruleId,Map<String,String> inputValueMap) throws Exception;
	
	/**
	 * 执行规则集ruleSetId下的所有规则
	 * @param ruleSetId 规则集ID
	 * @param inputValueMap 输入参数
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="fireTargetRules",desc="执行规则集下所有规则",
			inParam={
			@MethodParam(paramName="ruleSetId",paramDesc="规则集ID"),
			@MethodParam(paramName="inputValueMap",paramDesc="输入参数Map"),
		    },
		    outParam=@MethodParam(paramName="outputMap",paramDesc="Map 规则输出键值对"))
	public Map<String,String> fireTargetRules(String ruleSetId,Map<String,String> inputValueMap)throws Exception;
	
	/**
	 * 执行规则交易下所有规则
	 * @param transId 规则交易Id
	 * @param inputValueMap 输入参数
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="fireTargetTrans",desc="执行规则集下所有规则",
			inParam={
			@MethodParam(paramName="transId",paramDesc="规则交易ID"),
			@MethodParam(paramName="inputValueMap",paramDesc="输入参数Map"),
		    },
		    outParam=@MethodParam(paramName="outputMap",paramDesc="Map 规则输出键值对"))
	public Map<String,String> fireTargetTrans(String transId,Map<String,String> inputValueMap)throws Exception;
	
	/**
	 * 
	 * @param transId 规则交易Id
	 * @param inputValueMap 输入参数
	 * @return
	 * @throws Exception
	 */
	
	@MethodService(method="getParameterInfo",desc="根据规则集id，参数名称查询查询指标下的所有选项信息",
			inParam={
			@MethodParam(paramName="ruleSetId",paramDesc="规则集ID"),
			@MethodParam(paramName="paramName",paramDesc="参数名称"),
		    },
		    outParam=@MethodParam(paramName="list",paramDesc="ArrayList<String> 规则下所有的选项信息[desc:code]"))
	public ArrayList<?> getParameterInfo(String ruleSetId,String paramName) throws Exception;
	
	@MethodService(method="getRuleMapByTransId",desc="根据规则交易id查询项下的匹配规则",
			inParam={
			@MethodParam(paramName="transId",paramDesc="规则交易ID")
		    },
		    outParam=@MethodParam(paramName="ruleMap",paramDesc="ruleMap.put(rulesetid,List);List里面放ruleid,rulesetid为大写格式"))
	public Map<?,?> getRuleMapByTransId(String transId)throws Exception;
	
	@MethodService(method="getRuleInfoDesc",desc="根据【规则集id】_【规则id】取得规则说明信息",
			inParam={
			@MethodParam(paramName="rule",paramDesc="【规则集ID】_【规则ID】")
		    },
		    outParam=@MethodParam(paramName="string",paramDesc="规则集ID【规则集名称】 规则ID【规则名称】"))
	public String getRuleInfoDesc(String rule)throws Exception;
}
