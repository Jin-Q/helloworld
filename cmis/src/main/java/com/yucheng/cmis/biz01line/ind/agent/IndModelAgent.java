package com.yucheng.cmis.biz01line.ind.agent;

import java.util.HashMap; 
import java.util.Map; 
 
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.domain.IndModelDomain;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage; 
import com.yucheng.cmis.pub.exception.AgentException;

public class IndModelAgent extends CMISAgent {
	
	/**
	 * 插入模型基本信息 (使用的是父类的方法的代码) 
	 * @param indModel
	 *            模型信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public String insert(IndModelDomain indModel) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; /* 错误信息 */ 
			/* 新增模型信息 */
			int intMessage = this.insertCMISDomain(indModel, IndPubConstant.IND_MODEL);
			if (1 == intMessage) {
				strMessage = CMISMessage.ADDSUCCEESS;
			}
		return strMessage; 
	}
	/**
	 * 修改模型信息
	 * @param indModel
	 * @return
	 * @throws AgentException
	 */
	public String update(IndModelDomain indModel) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; /* 错误信息 */
		/* 修改模型信息 */
		int intMessage = this.modifyCMISDomain(indModel, IndPubConstant.IND_MODEL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		return strMessage; 
	}
	/**
	 * 删除模型信息
	 * @param indModelId
	 * @return
	 * @throws AgentException
	 */
	public String delete(String indModelId)throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; /* 错误信息  */
		/* 根据主键删除模型信息 */
		int count = this.removeCMISDomainByKeyword(IndPubConstant.IND_MODEL_GROUP,
				indModelId); /* 1成功 其他失败  */
		/* 如果失败，给标志信息赋值*/
		if (1 == count) {
			strMessage = CMISMessage.SUCCESS;/* 成功  */
		/*继续删除模型组关系信息*/
			count=0;
			count=this.removeCMISDomainByKeyword(IndPubConstant.IND_MODEL, indModelId);
			if (1 == count) {
				strMessage = CMISMessage.SUCCESS; // 成功  */
			}else{
				strMessage = CMISMessage.ADDDEFEAT;
			}
		}
		
		return strMessage;
	}
	
	/**
	 * 查找模型信息
	 * @param indModelId
	 * @return
	 * @throws AgentException
	 */
	public IndModelDomain findIndModelByModelId(String indModelId)throws AgentException{
		IndModelDomain indModel = new IndModelDomain();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("model_no", indModelId);       
		indModel = (IndModelDomain) this.findCMISDomainByKeywords(indModel,
				IndPubConstant.IND_MODEL, pk_values); 
		return indModel; 
	}
	 
	
/*	
	public static void main(String args[]){
		IndModelAgent agent = new IndModelAgent();
		try{
			agent.genCusComJspFile();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
*/
}
