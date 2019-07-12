package com.yucheng.cmis.platform.shuffle.msi.msiimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.ecc.shuffle.rule.RuleBase;
import com.ecc.shuffle.rule.RuleSet;
import com.ecc.shuffle.upgrade.RuleSetInstance;
import com.ecc.shuffle.upgrade.ext.Trans;
import com.ecc.shuffleserver.factory.ServerFactory;
import com.ecc.shufflestudio.editor.RuleSetWrapper;
import com.ecc.shufflestudio.editor.param.Parameter;
import com.ecc.shufflestudio.editor.param.ParametersWrapper;
import com.ecc.shufflestudio.ui.view.ReturnObj;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class ShuffleServiceImple extends CMISModualService implements ShuffleServiceInterface{
	

	public Map fireTargetRule(String ruleSetId, String ruleId, Map inputValueMap)
			throws Exception {
		RuleSetInstance rsi=new RuleSetInstance(ruleSetId, ruleId);
		Map map=null;
		try{
			map = rsi.fireTargetRules(inputValueMap);
		}catch(Exception e){
			throw new Exception("执行后台规则["+ruleSetId+"]_["+ruleId+"]异常，异常信息："+e.getCause().getMessage());
		}
		return map;
	}

	public Map fireTargetRules(String ruleSetId, Map inputValueMap)
			throws Exception {
		RuleSetInstance rsi=new RuleSetInstance(ruleSetId);
		Map map=null;
		try{
			map = rsi.fireTargetRules(inputValueMap);
		}catch(Exception e){
			throw new Exception("执行后台规则集["+ruleSetId+"]异常，异常信息："+e.getCause().getMessage());
		}
		return map;
	}

	public Map fireTargetTrans(String transId, Map inputValueMap)
			throws Exception {
		RuleSetInstance rsi=new RuleSetInstance();
		Map map=null;
	    map = rsi.fireTargetTrans(transId,inputValueMap);
		return map;
	}

	public ArrayList getParameterInfo(String ruleSetId, String paramName)
			throws Exception {
		//取得所有参数信息
		ServerFactory factory = new ServerFactory();
		Map map = new HashMap();
		map.put("ruleSetId",ruleSetId );
		ReturnObj obj = new ReturnObj();	
		obj=factory.getRuleSet(map);
		RuleSetWrapper ruleSet=(RuleSetWrapper) obj.getObj();
		ParametersWrapper paramWrapper=ruleSet.getParamWrapper();
		List paramlist=paramWrapper.getParameters();
		
		//根据指标名称index_name取得参数信息
		Parameter parameter=null;
		for(int i=0;i<paramlist.size();i++){
			parameter=(Parameter) paramlist.get(i);
			if(paramName.equals(parameter.getName())){
				break;
			}	
		}
		//根据参数信息产生所有选项信息
		
		return (ArrayList) parameter.getSelectList();
	}

	public Map getRuleMapByTransId(String transId) throws Exception {
		Trans trans=(Trans)RuleBase.getInstance().transMap.get(transId);
		return trans.ruleMap;
	}

	public String getRuleInfoDesc(String rule) throws Exception {
		String ruleInfo="";
		Map ruleSetMap=RuleBase.getInstance().ruleSets;
		String ruleSetId=rule.split("\\$")[0];
		String ruleId=rule.split("\\$")[1];
		ruleSetId = ruleSetId.toUpperCase();  //规则集名称编译必须大写
		RuleSet ruleSet=(RuleSet) ruleSetMap.get(ruleSetId);
		String rulesetname=ruleSet.name;
		String rulename=ruleSet.getRule(ruleId).name;
		ruleInfo=ruleSetId+" 【"+rulesetname+"】"+ruleId+"【"+rulename+"】";
		return ruleInfo;
	}
}
