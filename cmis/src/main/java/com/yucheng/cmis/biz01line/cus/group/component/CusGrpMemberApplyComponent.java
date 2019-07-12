package com.yucheng.cmis.biz01line.cus.group.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpMemberApplyAgent;
import com.yucheng.cmis.biz01line.cus.group.dao.CusGrpMemberApplyDao;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMemberApply;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 
 * @Classname CusGrpMemberApplyComponent.java
 * @Version 1.0
 * @Since 1.0 Mar 9, 2010
 * @Copyright yuchengtech
 * @Author g
 * @Description：本类主要代理集团客户成员申请基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */
public class CusGrpMemberApplyComponent extends CMISComponent{
	public int deteleCusGrpMemberApply(String grp_no,String serno)throws ComponentException {
		try {
			CusGrpMemberApplyAgent cusGrpMemberApplyAgent =(CusGrpMemberApplyAgent)this
														.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
			return cusGrpMemberApplyAgent.deteleCusGrpMemberApply(grp_no,serno);
		} catch (ComponentException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		
	}
	/**
	 * 根据客户码查询是否存在 集团的申请表中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public int cheakCusGrpMemberApply(String cusId) throws Exception {
		
		CusGrpMemberApplyAgent cusGrpMemberApplyAgent = (CusGrpMemberApplyAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
		int cheakCgma = cusGrpMemberApplyAgent.cheakCusGrpMemberApply(cusId);
		return cheakCgma;
	}
	
	/**
	 * 根据客户码查询是否存在 集团的申请表中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public CusGrpMemberApply cheakCusGrpMemberApplyObject(String cusId) throws Exception {
		
		CusGrpMemberApplyAgent cusGrpMemberApplyAgent = (CusGrpMemberApplyAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
		CusGrpMemberApply cheakCgma = cusGrpMemberApplyAgent.cheakCusGrpMemberApplyObject(cusId);
		return cheakCgma;
	}
	
	
	/**
	 * 根据客户码查询是否存在 集团的申请表中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public int cheakCusGrpMemberApplyInt(String cusId) throws Exception {
		
		CusGrpMemberApplyAgent cusGrpMemberApplyAgent = (CusGrpMemberApplyAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
		int cheakCgma = cusGrpMemberApplyAgent.cheakCusGrpMemberApplyInt(cusId);
		return cheakCgma;
	}
	/**
	 * 插入集团成员基本信息
	 * 
	 * @param cusGrpMember   集团成员信息对象          
	 * @return String    
	 * @throws EMPException 
	 */
	public String addCusGrpMemberApply(CusGrpMemberApply cusGrpMemberApply) throws  EMPException {

		//往集团客户成员表中 新增记录
		CusGrpMemberApplyAgent cusGrpMemberApplyAgent = (CusGrpMemberApplyAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
		cusGrpMemberApplyAgent.addRecord(cusGrpMemberApply);
		
		return null;
	};
	
	/**
	 * 将集团信息更新到客户信息
	 */
	public void setGrpToCom(String act_type,String grp_no)throws ComponentException {
		try {
			CusGrpMemberApplyAgent cmisDao = (CusGrpMemberApplyAgent)this.getAgentInstance("CusGrpMemberApplyAgent");
			cmisDao.setGrpToCom(act_type,grp_no);
		} catch (ComponentException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		
	}
}
