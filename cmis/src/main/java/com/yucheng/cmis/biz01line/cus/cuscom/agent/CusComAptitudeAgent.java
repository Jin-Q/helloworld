package com.yucheng.cmis.biz01line.cus.cuscom.agent;

import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComAptitude;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 
 * @Classname CusComAgent.java
 * @Version 1.0
 * @Since 1.0 Sep 12, 2008 4:55:31 PM
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：本类主要代理客户基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */

public class CusComAptitudeAgent extends CMISAgent {
	

	/**
	 * 插入客户基本信息 (使用的是父类的方法的代码)
	 * 
	 * @param cusCom
	 *            客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public String insert(CusComAptitude cusComAptitude) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
			// 新增
			int intMessage = this.insertCMISDomain(cusComAptitude, PUBConstant.CUSCOMAPTITUDE);
			
			if (1 == intMessage) {
				strMessage = CMISMessage.ADDSUCCEESS;// "添加成功！";
			}
		return strMessage;

	}
	public CusComAptitude checkExist(CusComAptitude cusComAptitude) throws AgentException {
		CusComAptitude pcusComAptitude=new CusComAptitude();
		String modelId = PUBConstant.CUSCOMAPTITUDE;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusComAptitude.getCusId());	
		pk_values.put("com_apt_code", cusComAptitude.getComAptCode());
		pcusComAptitude = (CusComAptitude)this.findCMISDomainByKeywords(cusComAptitude, modelId, pk_values);
		return pcusComAptitude;

	}
}
