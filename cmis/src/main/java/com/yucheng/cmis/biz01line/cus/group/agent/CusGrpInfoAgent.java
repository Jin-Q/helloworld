package com.yucheng.cmis.biz01line.cus.group.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.dao.GroupDao;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusComAgent.java
 * @Version 1.0
 * @Since 1.0 Sep 12, 2008 4:55:31 PM
 * @Copyright yuchengtech
 * @Author
 * @Description：本类主要代理集团客户基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */

public class CusGrpInfoAgent extends CMISAgent {

	/**
	 * 插入集团客户基本信息
	 * 
	 * @param cusGrpInfo
	 *            集团客户信息对象
	 * @return String
	 * @throws AgentException
	 */
	public String addRecord(CusGrpInfo cusGrpInfo) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）

		// 新增记录
		int count = this.insertCMISDomain(cusGrpInfo, PUBConstant.CUSGRPINFO); // 1成功
																				// 其他失败
		// 如果失败，给标志信息赋值
		if (1 == count) {
			flagInfo = CMISMessage.SUCCESS; // 成功
		}
		return flagInfo;
	}

	/*
	 * 根据客户ID查找集团客户基本信息
	 * 
	 * @param grpNo 集团编号
	 * 
	 * @return 集团客户基本信息
	 * 
	 * @throws EMPException
	 */
	public CusGrpInfo getCusGrpInfoDomainByGrpNo(String grpNo) throws EMPException {

		CusGrpInfo cusGrpInfo = new CusGrpInfo();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("grp_no", grpNo);
		cusGrpInfo = (CusGrpInfo) this.findCMISDomainByKeywords(cusGrpInfo,
				PUBConstant.CUSGRPINFO, pk_values);

		return cusGrpInfo;
	}

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
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		rt = dao.findCusGrpInfoByMemCusId(cusId, conn);
		return rt;
	}

	/**
	 * 删除记录
	 * 
	 * @param grpNo
	 *            集团负债编号
	 * @param cusId
	 *            客户代码
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String grpNo) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		// 进行删除操作
		int count = this.removeCMISDomainByKeyword(PUBConstant.CUSGRPINFO,grpNo);
		if (1 == count) {
			flagInfo = CMISMessage.SUCCESS; // 成功
		}
		return flagInfo;
	}

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
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		int intStr = dao.CheakParCusId(cusId, conn);
		if (intStr == 0) {
			str = "canInsert";
		}
		return str;
	}

	// 根据集团号删除集团成员信息 add by zhoujf 2009.07.26
	public int deleteGrpMemberByGrpNo(String grpNo) throws Exception{
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		int flag = 0;
		try {
			flag = dao.deleteGrpMemberByGrpNo(grpNo, conn);
		} catch (ComponentException e) {
			throw e;
		}
		return flag;
	}

	public CusGrpInfo getCusGrpInfoDomainByCusId(String cus_id)  throws ComponentException{
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		CusGrpInfo cgi = null;
		try {
			cgi = dao.getCusGrpInfoDomainByCusId(cus_id, conn);
		} catch (Exception e) {
			throw new ComponentException(e);
		}
		return cgi;
	}
	

}
