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
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusGrpInfoComponent
 * @Version 0.1
 * @Since 2008-9-18
 * @Copyright yuchengtech
 * @Description：
 * @Lastmodified 2008-9-18
 */
public class CusGrpInfoComponent extends CMISComponent {

	/**
	 * 插入集团客户基本信息
	 * 
	 * @param cusGrpInfo
	 *            集团客户信息对象
	 * @return String
	 * @throws ComponentException
	 */
	public String addCusGrpInfo(CusGrpInfo cusGrpInfo)
			throws ComponentException {
		// 返回信息
		String strReturnMessage = "";
		String grpNo = cusGrpInfo.getGrpNo();
		//String groupType = "1";
		String groupType = "3";
		String innerCusId = cusGrpInfo.getParentCusId();
		CusGrpMember cusGrpMember = this.getCusGrpMember(cusGrpInfo);
		// 新增操作
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFO);
		cusGrpInfoAgent.addRecord(cusGrpInfo);
		// 往集团客户成员表中 插入母公司记录
		CusGrpMemberAgent cusGrpMemberAgent = (CusGrpMemberAgent) this
				.getAgentInstance(PUBConstant.CUSGRPMEMBER);
		
//		cusGrpMember.setCusType(cusGrpMemberAgent.getCusType(cusGrpMember.getCusId()));
		cusGrpMemberAgent.addRecord(cusGrpMember);
		// 更新客户表中集团客户类型和集团编号
		CusComAgent cusComAgent = (CusComAgent) this
				.getAgentInstance(PUBConstant.CUSCOM);
		cusComAgent.updateGrpModeAndNo(groupType, grpNo, innerCusId);

		return strReturnMessage;
	};

	/**
	 * 删除对公客户资产信息
	 * 
	 * @param grpNo
	 *            集团号
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws EMPException
	 * @throws Exception
	 */
	public String removeCusGrpInfo(String grpNo, String parentCusId)
			throws EMPException {

		// 创建对公客户资产信息的代理类
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this.getAgentInstance(PUBConstant.CUSGRPINFO);
		cusGrpInfoAgent.deleteRecord(grpNo);
		return null;
	};
	/**
	 * 删除对公客户资产信息
	 * 
	 * @param grpNo
	 *            集团号
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws EMPException
	 * @throws Exception
	 */
	public String updateGrpModeAndNo(String grpNo)
			throws EMPException {

		// 更新客户表中集团客户类型和集团编号 add by gaozh 090701
		CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		cusComAgent.updateGrpJiesanModeAndNo("9", grpNo);
		return null;
	};

	/*
	 * 根据集团成员的客户ID查找集团基本信息
	 * 
	 * @param cusId 集团成员的客户ID
	 * 
	 * @return rt
	 * 
	 * @throws EMPException
	 */
	public List<CusGrpInfo> findCusGrpInfoByMemCusId(String cusId)
			throws Exception {

		List<CusGrpInfo> rt = new ArrayList<CusGrpInfo>();
		// 代理类
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFO);
		rt = cusGrpInfoAgent.findCusGrpInfoByMemCusId(cusId);
		return rt;
	}

	/**
	 * 通过集团客户基本信息对象，构建一个集团成员的对象（只适用新增集团客户时候的于母公司）
	 * 
	 * @param cusGrpInfo
	 *            集团客户信息对象
	 * @return pcusGrpMember 集团成员的对象
	 * @throws ComponentException
	 */
	private CusGrpMember getCusGrpMember(CusGrpInfo cusGrpInfo)
			throws ComponentException {
		// 返回信息
		CusGrpMember pcusGrpMember = new CusGrpMember();
		pcusGrpMember.setGrpNo(cusGrpInfo.getGrpNo());
		pcusGrpMember.setCusId(cusGrpInfo.getParentCusId());
		pcusGrpMember.setGenType("1");
		pcusGrpMember.setGrpCorreType("3");
		pcusGrpMember.setInputId(cusGrpInfo.getInputId());
		pcusGrpMember.setInputBrId(cusGrpInfo.getInputBrId());
		pcusGrpMember.setInputDate(cusGrpInfo.getInputDate());
		return pcusGrpMember;
	};

	/**
	 * 查询指定的客户码是否作为母公司已存在
	 * 
	 * @param cusId
	 *            客户代码
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String CheakParCusId(String cusId) throws Exception {
		String str = "have";
		// 代理类
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFO);
		str = cusGrpInfoAgent.CheakParCusId(cusId);
		return str;
	}

	// 为客户信息概括信息提供接口 根据母公司客户码删除 集团客户、及其下成员公司、关联关系信息 gaozh090629
	public boolean delCusGrpInfoByCusId(String cusId) throws Exception {
		boolean delRet = false;
		List<CusGrpInfo> list = findCusGrpInfoByMemCusId(cusId);
		if (list == null || list.size() <= 0) {
			delRet = true;
			return delRet;
		}
		for (int i = 0; i < list.size(); i++) {
			CusGrpInfo cusGrp = list.get(i);
			removeCusGrpInfo(cusGrp.getGrpNo(), cusId);
		}

		delRet = true;
		return delRet;
	}

	/**
	 * 根据集团客户号获取集团客户DOMAIN
	 * */
	public CusGrpInfo getCusGrpInfoDomainByGrpNo(String grpNo)
			throws EMPException {
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFO);
		return cusGrpInfoAgent.getCusGrpInfoDomainByGrpNo(grpNo);
	}

	// 根据集团号删除集团成员信息 add by zhoujf 2009.07.26
	public int deleteGrpMemberByGrpNo(String grpNo) throws Exception{
		CusGrpInfoAgent cusGrpInfoAgent = (CusGrpInfoAgent) this.getAgentInstance(PUBConstant.CUSGRPINFO);
		return cusGrpInfoAgent.deleteGrpMemberByGrpNo(grpNo);
	}
	
	/**
	 * 根据客户号查询客户所在集团并返回集团信息
	 * @param cusId
	 * @return
	 * @throws ComponentException
	 */
	public CusGrpInfo getCusGrpInfoDomainByCusId(String cusId) throws ComponentException{
		//代理类
		CusGrpInfo cusGrpInfo = null;
		try {
			CusGrpInfoAgent cusGrpInfoAgent;
			cusGrpInfoAgent = (CusGrpInfoAgent) this.getAgentInstance(PUBConstant.CUSGRPINFO);
			cusGrpInfo =  cusGrpInfoAgent.getCusGrpInfoDomainByCusId(cusId);
		} catch (Exception e) {
			throw new ComponentException(e);
		}
		return cusGrpInfo;
	}
}
