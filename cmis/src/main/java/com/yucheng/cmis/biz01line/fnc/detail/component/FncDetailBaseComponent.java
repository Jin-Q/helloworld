package com.yucheng.cmis.biz01line.fnc.detail.component;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncDetailBaseAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncBaseDetailComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:43:00 AM  
 *@Copyright 	yuchengtech
 *@Author 		xuyp
 *@Description：查询前期报表明细
 *@Lastmodified 
 *@Author
 */
public class FncDetailBaseComponent extends CMISComponent{

	
	
	public String queryFncDetailBasePk(String cus_id,String ym) throws AgentException{
		String fdbPk = "";
		//得到上期报表年月
		String ym_value = "";
		int year = 0;
		//如果是1一月份的话
		String month = "";
		month = ym.substring(4);
		if(month.equals("01")){
			year = Integer.parseInt(ym.substring(0, 4))-1;
			ym_value = String.valueOf(year)+12;
		}else{
		   year = Integer.parseInt(ym)-1;
		   ym_value = String.valueOf(year);
		}
		//查询上期报表明细基表的主键值
		FncDetailBaseAgent fncBaseDetailAgent = (FncDetailBaseAgent) this.getAgentInstance(PUBConstant.FNCDETAILBASE);
		fdbPk = fncBaseDetailAgent.getFncDetailBasePk(cus_id,ym_value);
		return fdbPk;
		
	}
	public String insertBeforeMess(String beforePk,String thisPk,String modelId,TableModelDAO dao) throws EMPException{
		String info = "";
		//构建代理类
		FncDetailBaseAgent fncBaseDetailAgent = (FncDetailBaseAgent) this.getAgentInstance(PUBConstant.FNCDETAILBASE);
		info = fncBaseDetailAgent.insertBeforeMess(beforePk,thisPk,modelId,dao);
		return info;
		
	}
	
	
}
