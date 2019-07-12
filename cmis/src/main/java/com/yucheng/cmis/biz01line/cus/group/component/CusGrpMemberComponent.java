package com.yucheng.cmis.biz01line.cus.group.component;

import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComAgent;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpInfoAgent;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpMemberAgent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
 
/**
 * @Classname CusGrpMemberComponent
 * @Version 0.1
 * @Since 2008-9-18
 * @Copyright yuchengtech
 * @Description：
 * @Lastmodified 2008-9-18
 */
public class CusGrpMemberComponent extends CMISComponent {

	/**
	 * 插入集团成员基本信息
	 * 
	 * @param cusGrpMember   集团成员信息对象          
	 * @return String    
	 * @throws EMPException 
	 */
	public String addCusGrpMember(CusGrpMember cusGrpMember) throws  EMPException {
		// 返回信息
		//String strReturnMessage="";
		String grpNo = cusGrpMember.getGrpNo();
		//String groupType = "2";
		String groupType = "4";
		String CusId = cusGrpMember.getCusId();

		//往集团客户成员表中 新增记录
		CusGrpMemberAgent cusGrpMemberAgent = (CusGrpMemberAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBER);
		cusGrpMemberAgent.addRecord(cusGrpMember);
		//更新客户表中集团客户类型和集团编号		
		CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		cusComAgent.updateGrpModeAndNo(groupType, grpNo, CusId);
		
		return null;
	};
	/**
	 * 删除集团客户成员信息
	 * @param groNo   集团编号
	 * @param cusId    集团成员客户代码
	 * @return  flagInfo  信息编码
	 * @throws EMPException 
	 */
	public String removeCusGrpMember(String grpNo, String cusId) throws EMPException {
		
//		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//通过代理类进行删除操作
		CusGrpMemberAgent cusGrpMemberAgent = (CusGrpMemberAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBER);
		cusGrpMemberAgent.deleteRecord(grpNo,cusId);
		
		//更新客户表中集团客户类型和集团编号  add by gaozh 090701		
		CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		cusComAgent.updateGrpModeAndNo("9", "", cusId);
		return null;
	};

	//为客户信息概括信息提供接口 根据子公司客户码、子公司的母公司客户码删除成员客户信息、关联关系信息 gaozh090629
	public boolean delCusGrpMemberByCusId(String PCusId,String CCusId) throws Exception{
		boolean delRet=false;
		List<CusGrpInfo> list=new ArrayList<CusGrpInfo>();
		//代理类
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this.getAgentInstance(PUBConstant.CUSGRPINFO);
		list = cusGrpInfoAgent.findCusGrpInfoByMemCusId(PCusId);
		
		if(list==null||list.size()<=0){
			delRet=true;
			return delRet;
		}
		for(int i = 0;i<list.size();i++){
			CusGrpInfo cusGrp = list.get(i);
			removeCusGrpMember(cusGrp.getGrpNo(),CCusId);
		}
		delRet=true;	
		return delRet;
	}
	
	/**
	 * @param cusId    
	 * @return String    
	 * @throws EMPException 
	 */
	public CusGrpMember cheakCusGrpMember(String cusId) throws Exception {
		CusGrpMember  cheakCgm = new CusGrpMember();
		CusGrpMemberAgent cusGrpMemberAgent = (CusGrpMemberAgent) this.getAgentInstance(PUBConstant.CUSGRPMEMBER);
		cheakCgm = cusGrpMemberAgent.cheakCusGrpMember(cusId);
		return cheakCgm;
	};
	
}

