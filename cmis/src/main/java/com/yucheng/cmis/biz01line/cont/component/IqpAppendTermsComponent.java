package com.yucheng.cmis.biz01line.cont.component;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.cont.agent.IqpAppendTermsAgent;
import com.yucheng.cmis.biz01line.cont.dao.IqpAppendTermsDao;
import com.yucheng.cmis.biz01line.cont.pub.ContConstant;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IqpAppendTermsComponent extends CMISComponent{
	
	/**
	 * 附加条款tab页，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 附加条款
	 * @param serno 业务编号
	 * @throws Exception 
	 */
	public int insertIqpAppendTermsByIcoll(String serno, IndexedCollection iColl) throws Exception{
		IqpAppendTermsAgent cmisAgent = (IqpAppendTermsAgent)this.getAgentInstance(ContConstant.IQPAPPENDTERMSAGENT);
		int m =cmisAgent.insertIqpAppendTermsByIColl(serno,iColl);		
		return m;
	}

	
	/**更新业务担保合同状态
	 * @param cont_no 合同状态
	 */
	public int updateGrtLoanRGur(String cont_no,DataSource dataSource,Connection connection) throws Exception{
		IqpAppendTermsAgent cmisAgent = (IqpAppendTermsAgent)this.getAgentInstance(ContConstant.IQPAPPENDTERMSAGENT);
		int m = cmisAgent.updateGrtLoanRGur(cont_no,dataSource, connection);
		return m;
	}
}
