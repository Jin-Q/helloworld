package com.yucheng.cmis.biz01line.cus.group.agent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.dao.GroupDao;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
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
 * @Description：本类主要代理集团客户成员基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */

public class CusGrpMemberAgent extends CMISAgent {

	/**
	 * 插入集团客户成员基本信息
	 * 
	 * @param cusGrpInfo   集团客户成员         
	 * @return String    
	 * @throws AgentException
	 */
	public String addRecord(CusGrpMember cusGrpMember) throws AgentException{
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）

		//新增记录
		int count = this.insertCMISDomain(cusGrpMember, PUBConstant.CUSGRPMEMBER);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
		return flagInfo;
	}

	/**
	 * 删除记录
	 * @param grpNo           集团负债编号
	 * @param cusId                 客户代码
	 * @return  信息编码
	 * @throws AgentException
	 */ 
	public String deleteRecord(String grpNo, String cusId ) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		//把联合主键放进Map中
		Map<String,String>pk_values = new HashMap<String,String>();
		pk_values.put("grp_no", grpNo);
		pk_values.put("cus_id", cusId);
		//集团客户成员删除的记录
		int count = this.removeCMISDomainByKeywords(PUBConstant.CUSGRPMEMBER, pk_values);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		
		return flagInfo;
	}

	/**
	 * 根据客户码查询是否存在集团中
	 * 
	 * @param cusId     客户码   
	 * @return String    
	 * @throws EMPException 
	 */
	public CusGrpMember cheakCusGrpMember(String cusId) throws  Exception {
		GroupDao dao = new GroupDao();
		Connection conn=this.getConnection();
		CusGrpMember cgm =dao.findCusGrpInfoByCusId(cusId,conn);
		return cgm;
		
	}

}
