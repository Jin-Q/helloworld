package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.dao.CusTrusteeAppDao;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeApp;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeLog;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class CusTrusteeAppAgent extends CMISAgent {

   /*
    * 修改状态位
    */
	public String updateStatus(String serno,String statues)throws AgentException{
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		Connection connection =this.getConnection();
		// 修改移交申请状态位
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
		int intMessage = dao.updateCusHandoverAppStatus(serno, statues, connection);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// 成功信息
		}
		return strMessage;
	}
	/*
	 * 添加托管日志
	 */
	public String insertCusTrusteeLog(CusTrusteeLog cusTrusteeLog)throws AgentException{
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		// 新增托管日志信息
		int intMessage = this.insertCMISDomain(cusTrusteeLog, PUBConstant.CUSTRUSTEELOG);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// 成功信息
		}
		return strMessage;
	}
	
	public String updateLogRetractDate(String serno, String date) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
		// 新增托管日志信息
		int intMessage = dao.updateLogRetractDate(serno, date, this.getConnection());
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// 成功信息
		}
		return strMessage;
	}
	
	public boolean hasCusTrusteeList(String serno) throws AgentException {
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
		// 新增托管日志信息
		boolean intMessage = dao.hasCusTrusteeList(serno, this.getConnection());
		return intMessage;
	}
	/**
	 * 处理托管申请流程
	 * @param serno
	 * @return
	 * @throws AgentException
	 */
	public void cusTrusteeApp(String serno) throws AgentException {
		TableModelDAO dao = this.getTableModelDAO();
		try {
			KeyedCollection kColl= dao.queryFirst("CusTrusteeApp", null, " where serno='"+serno+"'", this.getConnection());
			String consignor_id=(String) kColl.getDataValue("consignor_id");
			String trustee_id=(String)kColl.getDataValue("trustee_id");
			
			String sql=" insert into cus_trustee values ('"+consignor_id+"','"+trustee_id+"')";
			this.executeSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgentException(e.getMessage());
		}
		
	}
	
	public boolean getExistCusTrustee(String consignorId,String trusteeId) throws AgentException{
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
		boolean intMessage = dao.getExistCusTrustee(consignorId,trusteeId, this.getConnection());
		return intMessage;
	}
	public void callback(String consignor_id,String trusteeId) throws AgentException {
		try {
			String sql="delete from cus_trustee where consignor_id = '"+consignor_id+"' and trustee_id ='"+trusteeId+"'";
			this.executeSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgentException(e.getMessage());
		}
		
	}
	public boolean checkCusHandoverApp(String managerId, String consignorId,
			Connection con) {
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
	    return 	dao.checkCusHandoverApp(managerId, consignorId, con);
		
		
	}
	public boolean hasCusTrustee(String consignorId,String trustee_id,Connection con) throws AgentException{
		CusTrusteeAppDao dao = new CusTrusteeAppDao();
	    return 	(dao.hasCusTrustee(consignorId, trustee_id, con) || dao.hasCusTrusteeApp(consignorId, trustee_id, con));
	}
	
	public CusTrusteeApp getCusTrusteeAppByKey(String serno)
	throws EMPException {
		CusTrusteeApp cusTrusteeApp = new CusTrusteeApp();
		TableModelDAO tmd = this.getTableModelDAO();
		KeyedCollection kcoll = tmd.queryDetail(PUBConstant.CUSTRUSTEEAPP, serno, this.getConnection());
		
		ComponentHelper ch = new ComponentHelper();
		if(cusTrusteeApp!=null){
			cusTrusteeApp = (CusTrusteeApp) ch.kcolTOdomain(cusTrusteeApp, kcoll);
			return cusTrusteeApp;
		}else{
			return null;
		}
	}

}
