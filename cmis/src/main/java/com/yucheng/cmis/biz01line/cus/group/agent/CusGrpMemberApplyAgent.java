package com.yucheng.cmis.biz01line.cus.group.agent;

import java.sql.Connection;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.dao.CusGrpMemberApplyDao;
import com.yucheng.cmis.biz01line.cus.group.dao.GroupDao;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMemberApply;
import com.yucheng.cmis.biz01line.lmt.dao.LmtPubDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 
 * @Classname CusGrpMemberApplyAgent.java
 * @Version 1.0
 * @Since 1.0 Mar 9, 2010 
 * @Copyright yuchengtech
 * @Author g
 * @Description：本类主要代理集团客户成员申请基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */
public class CusGrpMemberApplyAgent extends CMISAgent{
	/**
	 * 插入集团客户成员申请基本信息
	 * 
	 * @param cusGrpInfoApply  集团客户成员申请     
	 * @return String    
	 * @throws AgentException
	 */
	public String addRecord(CusGrpMemberApply cusGrpMemberApply ) throws AgentException{
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）

		//新增记录
		int count = this.insertCMISDomain(cusGrpMemberApply, PUBConstant.CUSGRPMEMBERAPPLY);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
		return flagInfo;
	}
	public int deteleCusGrpMemberApply(String grp_no,String serno)throws AgentException{
		Connection conn = null;
		try {
			CusGrpMemberApplyDao cusGrpMemberApplyDao = new CusGrpMemberApplyDao();
			
			conn = this.getConnection();
			return cusGrpMemberApplyDao.deteleCusGrpMemberApply(grp_no,serno,conn);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AgentException(ex);
		} finally {
			if (conn != null) {
				this.releaseConnection(conn);
			}
		}
	}
	/**
	 * 根据客户码查询是否存在集团成员中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public int cheakCusGrpMemberApply(String cusId) throws Exception {
		GroupDao dao = new GroupDao();
		Connection conn=this.getConnection();
		int cgma =dao.findCusGrpInfoApplyByCusId(cusId,conn);
		return cgma;
		
	}
	/**
	 * 根据客户码查询是否存在集团成员中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public CusGrpMemberApply cheakCusGrpMemberApplyObject(String cusId) throws  Exception {
		GroupDao dao = new GroupDao();
		Connection conn=this.getConnection();
		CusGrpMemberApply cgma =dao.findCusGrpInfoApplyObjectByCusId(cusId,conn);
		return cgma;
		
	}
	/**
	 * 根据客户码查询是否存在集团成员中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public int cheakCusGrpMemberApplyInt(String cusId) throws  Exception {
		GroupDao dao = new GroupDao();
		Connection conn=this.getConnection();
		int cgma =dao.findCusGrpInfoApplyIntByCusId(cusId,conn);
		return cgma;
		
	}
	
	/**
	 * 将集团信息更新到客户信息
	 */
	public void setGrpToCom(String act_type,String grp_no) throws AgentException {
		try {
			CusGrpMemberApplyDao cmisDao = (CusGrpMemberApplyDao)this.getDaoInstance("CusGrpMemberApplyDao");
			cmisDao.setGrpToCom(act_type,grp_no);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
}

