package com.yucheng.cmis.biz01line.cus.cuscom.component;

import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComAgent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cussameorg.domain.CusSameOrg;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusComComponent
 * @Version 0.1
 * @Since 2008-9-18
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：
 * @Lastmodified 2008-9-18
 * @Author wqgang
 */
public class CusComComponent extends CMISComponent  {
	/**
	 * @param CusId 客户代码
	 * @return CusSameOrg 同业客户对象
	 * @throws AgentException
	 * @throws EMPException
	 */
	public CusSameOrg getCusSameOrg(String CusId)
			throws EMPException, ComponentException, AgentException {
		CusSameOrg cusSameOrg = new CusSameOrg();
		if (CusId != null && CusId.trim().length() != 0) {
			// 创建对应的代理类
			CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			cusSameOrg = cusComAgent.findCusSameOrgByCusId(CusId);
		}
		return cusSameOrg;
	};

	/**
	 * @param CusId 客户代码
	 * @return CusSameOrg 同业客户对象
	 * @throws EMPJDBCException 
	 * @throws AgentException
	 * @throws EMPException
	 */
	public KeyedCollection getCusSameOrgkColl(String CusId) throws ComponentException, EMPJDBCException {
		KeyedCollection kColl = new KeyedCollection();
		if (CusId != null && CusId.trim().length() != 0) {
			TableModelDAO dao = (TableModelDAO) this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			kColl = dao.queryDetail(PUBConstant.CUSSAMEORG, CusId, this.getConnection());
		}
		return kColl;
	};
	
	/**
	 * 通过客户代码和查询条件获取cusCom对象 condition的处理方式根据表模型的DAO来具体确定，目前暂不考虑
	 * @param innerCusId 内部客户代码
	 * @param condition 查询条件
	 * @return CusCom 对公客户对象
	 * @throws AgentException
	 * @throws EMPException
	 */
	public CusCom getCusCom(String cusId) throws EMPException, ComponentException, AgentException {
		CusCom cusCom = new CusCom();
		if (cusId != null && !"".equals(cusId)) {
			// 创建对应的代理类
			CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			cusCom = cusComAgent.findCusComByCusId(cusId);
		}
		return cusCom;
	};

	/**
	 * 根据贷款卡号检查是否已经在系统中存在
	 * 
	 * @param loanCardId
	 * @return
	 * @throws ComponentException
	 */
	public String checkLoanCardIdExist(String loanCardId,String cusNo)
			throws ComponentException {
		String message = "n";
		try {
			// 创建对应的代理类
			CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			message = cusAgent.checkLoanCardIdExist(loanCardId,cusNo);
		} catch (Exception e) {
			message = "n";
			throw new ComponentException();
		}
		return message;
	}

	/**
	 * <p>
	 * 根据证件类型，证件号码检查其在系统中是否已经存在
	 * </p>
	 * @param certCode 证件号码
	 * @param certType 证件类型
	 * @param cusType 客户类型
	 * @return
	 * @throws ComponentException
	 */
	public String checkCertCodeExist(String certCode, String certType,String cusNo,
			String cusType) throws ComponentException {
		String message = "n";
		try {
			// 创建对应的代理类
			CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			message = cusAgent.checkCertCodeExist(certCode, certType, cusType);
		} catch (Exception e) {
			message = "n";
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 根据帐号判断基本存款账户和一般结算账户是否在系统中存在
	 * 
	 * @param accNo
	 * @param accType
	 * @return
	 * @throws ComponentException
	 */
	public String AccExist(String accNo, String accType,String cusNo)
			throws ComponentException {
		String message = "n";
		try {
			// 创建对应的代理类
			CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			message = cusAgent.AccExist(accNo, accType,cusNo);
		} catch (Exception e) {
			message = "n";
			throw new ComponentException();
		}
		return message;
	}
	/**
	 * 
	 * @param grpNo
	 * @return
	 * @throws ComponentException
	 */
	public int deleteGrpMemberByGrpNo(String grpNo) throws ComponentException{
		int num = 0;
		try{
			CusComAgent cusAgent = (CusComAgent) this
			.getAgentInstance(PUBConstant.CUSCOM);
			num = cusAgent.deleteGrpMemberByGrpNo(grpNo);
		}catch (Exception e) {
			throw new ComponentException();
		}
		return num;
	}

	/**
	 * 根据CUS_ID获取最新财务报表数据
	 * */
	public String getMaxFncStatBaseByCusId(String cusId) throws ComponentException {
		CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		return cusAgent.getMaxFncStatBaseByCusId(cusId);
	}
	
	/**
	 * 根据CUS_ID,CUS_TYPE获取企业客户的高管信息中的法人信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId,String cusType)
			throws ComponentException {
		CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		return cusAgent.queryRelCusListByCusId(cusId);
	}
	
	/**
	 * 根据CUS_ID,CUS_TYPE获取企业客户的实际控制人信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryControlRelCusListByCusId(String cusId)
			throws ComponentException {
		CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		return cusAgent.queryRelCusControlListByCusId(cusId);
	}
	
	/**
	 * 查找上年末的财务报表
	 * @param cusId	客户编号
	 * @param serno	业务流水号
	 * @return 1--成功  0--失败
	 * @throws Exception 
	 * @throws ComponenetException
	 */
	public String queryFncStat(String cusId,String year) throws Exception{
		CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		return cusAgent.queryFncStat(cusId,year);
	}
	
	/**
	 * 执行查询sql
	 */
	public String querySql(String sql)throws ComponentException {
		CusComAgent cusAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
		return cusAgent.querySql(sql);
	}

}
