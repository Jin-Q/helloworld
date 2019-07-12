package com.yucheng.cmis.platform.workflow.msi;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.pub.CMISComponent;

public class TestBizProcess extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/*SRole role = new SRole();
		role.setRoleno("test");
		role.setRolename("testname");
		try {
			SqlClient.insertAuto("testBizprocxx", role, getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		}*/
		Connection connection = this.getConnection();
		TableModelDAO dao = this.getTableModelDAO();
		KeyedCollection kcoll = dao.queryAllDetail(wfiMsg.getTableName(), wfiMsg.getPkValue(), connection);
		kcoll.setDataValue("memo", "9");
		dao.update(kcoll, connection);
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		//throw new EMPException("审批否决，不允许！");

	}

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = this.getConnection();
		TableModelDAO dao = this.getTableModelDAO();
		KeyedCollection kcoll = dao.queryAllDetail(wfiMsg.getTableName(), wfiMsg.getPkValue(), connection);
		kcoll.setDataValue("memo", "9");
		dao.update(kcoll, connection);
		if(1==1) {
			throw new EMPException();
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// TODO Auto-generated method stub
		return null;
	}

}
