package com.yucheng.cmis.biz01line.cus.group.component;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBaseAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpInfoApplyAgent;
import com.yucheng.cmis.biz01line.cus.group.agent.CusGrpMemberApplyAgent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfoApply;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMemberApply;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 
 * @Classname CusGrpInfoApplyAgent.java
 * @Version 1.0
 * @Since 1.0 Mar 9, 2010
 * @Copyright yuchengtech
 * @Author g
 * @Description：
 * @Lastmodified
 * @Author
 */
public class CusGrpInfoApplyComponent extends CMISComponent{
	/**
	 * 插入集团客户基本信息
	 * 
	 * @param cusGrpInfoApply
	 *            集团客户信息对象
	 * @return String
	 * @throws ComponentException
	 */
	public String addCusGrpInfoApply(CusGrpInfoApply cusGrpInfoApply)
			throws Exception {
		// 返回信息
		String strReturnMessage = "";
		//String groupType = "1";
//		String groupType = "3";
//		String innerCusId = cusGrpInfoApply.getParentCusId();
		CusGrpMemberApply cusGrpMemberApply = this.getCusGrpMember(cusGrpInfoApply);
		// 新增操作
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
//		System.out.println("cusGrpInfoApplyAgent:"+cusGrpInfoApplyAgent);
		cusGrpInfoApplyAgent.addRecord(cusGrpInfoApply);
		// 往集团客户成员表中 插入母公司记录
		CusGrpMemberApplyAgent cusGrpMemberApplyAgent = (CusGrpMemberApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPMEMBERAPPLYAGENT);
		cusGrpMemberApplyAgent.addRecord(cusGrpMemberApply);
		/*// 更新客户表中集团客户类型和集团编号
		CusComAgent cusComAgent = (CusComAgent) this
				.getAgentInstance(PUBConstant.CUSCOM);
		cusComAgent.updateGrpModeAndNo(groupType, grpNo, innerCusId);*/

		return strReturnMessage;
	};
	/**
	 * 通过集团客户基本信息对象，构建一个集团成员的对象（只适用新增集团客户时候的于母公司）
	 * 
	 * @param cusGrpInfoApply
	 *            集团客户信息对象
	 * @return pcusGrpMemberApply 集团成员的对象
	 * @throws ComponentException
	 */
	private CusGrpMemberApply getCusGrpMember(CusGrpInfoApply cusGrpInfoApply)
			throws Exception {
		// 返回信息
		CusGrpMemberApply pcusGrpMemberApply = new CusGrpMemberApply();
		pcusGrpMemberApply.setSerno(cusGrpInfoApply.getSerno());
		pcusGrpMemberApply.setGrpNo(cusGrpInfoApply.getGrpNo());
		pcusGrpMemberApply.setCusId(cusGrpInfoApply.getParentCusId());
		pcusGrpMemberApply.setCusName(cusGrpInfoApply.getParentCusName());
		pcusGrpMemberApply.setGrpCorreType("3");
		pcusGrpMemberApply.setManagerId(cusGrpInfoApply.getManagerId());
		pcusGrpMemberApply.setManagerBrId(cusGrpInfoApply.getManagerBrId());
		pcusGrpMemberApply.setInputId(cusGrpInfoApply.getInputId());
		pcusGrpMemberApply.setInputBrId(cusGrpInfoApply.getInputBrId());
		pcusGrpMemberApply.setInputDate(cusGrpInfoApply.getInputDate());
		pcusGrpMemberApply.setGenType("1");
		
		CusBaseAgent cusBaseAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		try {
			CusBase cusBase = cusBaseAgent.getCusBaseDomainByCusId(cusGrpInfoApply.getParentCusId());
			pcusGrpMemberApply.setCusType(cusBase.getCusType());
		} catch (EMPException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return pcusGrpMemberApply;
	};
	
	/**
	 * 查询指定的客户码是否作为母公司已存在
	 * 
	 * @param cusId
	 *            客户代码
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String CheakParCusIdApply(String cusId) throws Exception {
		String str = "have";
		// 代理类
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		str = cusGrpInfoApplyAgent.CheakParCusIdApply(cusId);
		return str;
	}
	/**
	 * 判断该集团客户是否有正在进行的一般授信或是一般授信变更操作
	 * @param serno 申请序列号
	 */
	public String checkLmtApplyAndLmtModAppBySerno(String serno) throws ComponentException{
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		String msg = "";
		msg = cusGrpInfoApplyAgent.checkLmtApplyAndLmtModAppBySerno(serno);
		return msg;
	}
	/**
	 * 审批通过后将插入到cus_grp_info和cus_grp_member
	 * 关联集团新增申请
	 * @param serno 申请序列号
	 *            
	 * @return 信息编码
	 * @throws ComponentException
	 */
	public void insertCusGrpInfoAndCusGrpMember(String serno) throws ComponentException{
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		cusGrpInfoApplyAgent.insertCusGrpInfoAndCusGrpMember(serno);
		
	}
	
	
	/**
	 * 审批通过后将修改到cus_grp_info和cus_grp_member
	 * 关联集团变更
	 * @param serno 申请序列号
	 *            
	 * @return 信息编码
	 * @throws ComponentException
	 */
	public void updateCusGrpInfoAndCusGrpMember(String serno) throws ComponentException{
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		cusGrpInfoApplyAgent.updateCusGrpInfoAndCusGrpMember(serno);
		
	}
	/**
	 * 查询该集团下是否存在集团成员 是 返回1，否 返回0
	 * 
	 * @param grp_no 集团编号
	 *            
	 * @return 信息编码
	 * @throws ComponentException
	 */
	public int checkCusGrpMember(String serno) throws Exception{
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		int flag = cusGrpInfoApplyAgent.checkCusGrpMember(serno);
		return flag;
	}
	/**
	 * 判断该申请集团向下的成员是否已经存在别的有效集团中 是 返回1，否 返回0
	 * 
	 * @param grp_no 集团编号
	 *            
	 * @return 信息编码
	 * @throws ComponentException
	 */
	public int isExistCusGrpMember(String serno) throws Exception{
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		int flag = cusGrpInfoApplyAgent.isExistCusGrpMember(serno);
		return flag;
	}
	/**
	 * 插入集团客户基本信息(变更)
	 * 
	 * @param cusGrpInfoApply
	 *            集团客户信息对象
	 * @return String
	 * @throws ComponentException
	 */
	public String addCusGrpInfoApplyForMod(CusGrpInfoApply cusGrpInfoApply,String serno)
			throws Exception {
		// 返回信息
		String strReturnMessage = "";
		// 新增操作
		CusGrpInfoApplyAgent cusGrpInfoApplyAgent = (CusGrpInfoApplyAgent) this
				.getAgentInstance(PUBConstant.CUSGRPINFOAPPLYAGENT);
		cusGrpInfoApplyAgent.addRecord(cusGrpInfoApply);
		// 往集团客户成员表中 插入母公司记录
		String grpNo = cusGrpInfoApply.getGrpNo();
		cusGrpInfoApplyAgent.addCusGrpMemberApplyForMod(grpNo,serno);
		return strReturnMessage;
	};
}

