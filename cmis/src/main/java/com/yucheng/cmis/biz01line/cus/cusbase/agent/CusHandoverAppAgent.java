package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.dao.CusHandoverAppDao;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverLog;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class CusHandoverAppAgent extends CMISAgent {

   /*
    * 修改状态位
    */
	public String updateStatus(String serno,String statues)throws Exception{
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		// 修改移交申请状态位
		Connection conn=this.getConnection();
//		CusHandoverAppDao dao = new CusHandoverAppDao();
		CusHandoverAppDao dao = (CusHandoverAppDao)this.getDaoInstance("CusHandoverApp");
		int intMessage = dao.updateCusHandoverAppStatus(serno, statues, conn);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// 成功信息
		}
		return strMessage;
	}
	/**
	 * @param cusId    
	 * @return String    
	 * @throws EMPException 
	 */
	public CusHandoverApp checkCusHandoverApp(String cusId,String handoverId) throws Exception {
//		CusHandoverAppDao dao = new CusHandoverAppDao();
		CusHandoverAppDao dao = (CusHandoverAppDao)this.getDaoInstance("CusHandoverApp");
		Connection conn=this.getConnection();
		CusHandoverApp cha =dao.findCusHandoverApp(cusId,handoverId,conn);
		return cha;
	};
	/*
	 * 添加移交日志
	 */
	public String insertCusHandoverLog(CusHandoverLog cusHandoverLog)throws AgentException{
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		// 新增客户基表信息
		int intMessage = this.insertCMISDomain(cusHandoverLog, PUBConstant.CUSHANDOVERLOG);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// 成功信息
		}
		return strMessage;
	}
	
	public boolean hasCusTrusteeList(String serno)throws Exception{
//		CusHandoverAppDao dao = new CusHandoverAppDao();
		CusHandoverAppDao dao = (CusHandoverAppDao)this.getDaoInstance("CusHandoverApp");
		return dao.hasCusTrusteeList(serno, this.getConnection());
	}
	/**
	 * 处理客户移交申请流程
	 * @param serno 移交申请流水号
	 * @throws AgentException
	 */
//	public void cusHandoverApp(String serno)throws AgentException{
//		TableModelDAO dao = this.getTableModelDAO();
//		Connection con = this.getConnection();
//		KeyedCollection kColl = null;
//		IndexedCollection iColl = null;
//		try {
//			kColl = dao.queryFirst("CusHandoverApp", null, " where serno ='"+serno+"'",con );
//			iColl = dao.queryList("CusHandoverLst", " where serno ='"+serno+"'", con);
//			String handoverMode = (String) kColl.getDataValue("handover_mode"); //移交方式
//			String receiverId = (String) kColl.getDataValue("receiver_id"); //接受人
//			String receiverBrId = (String) kColl.getDataValue("receiver_br_id"); //接受机构
//			String handoverId = (String) kColl.getDataValue("handover_id"); //移交人
//			
//			/**
//			 * 根据移交方式 分别对不同表进行操作
//			 */
//			if("1".equals(handoverMode)){	//仅客户资料移交
//				for(int i=0;i<iColl.size();i++){
//					KeyedCollection kc = (KeyedCollection) iColl.get(i);
//					String businessCode = (String) kc.getDataValue("business_code");
//					CusHandoverAppDao cusHandoverAppDao = new CusHandoverAppDao();
//					cusHandoverAppDao.setConnection(this.getConnection());
//					cusHandoverAppDao.updateCom(handoverId, receiverId, receiverBrId, businessCode);
//				}
//				
//			}else if("2".equals(handoverMode)){	//客户与业务移交
//				for(int i=0;i<iColl.size();i++){
//					KeyedCollection kc = (KeyedCollection) iColl.get(i);
//					String businessCode = (String) kc.getDataValue("business_code");
//					
//					CusHandoverAppDao cusHandoverAppDao = new CusHandoverAppDao();
//					cusHandoverAppDao.setConnection(this.getConnection());
//					//移交客户资料
//					cusHandoverAppDao.updateCom(handoverId, receiverId, receiverBrId, businessCode);
//					//移交业务
//					cusHandoverAppDao.updateComAndBusiness(handoverId, receiverId, receiverBrId, businessCode);
//				}
//				
//			}
//		} catch (Exception e) {
//			throw new AgentException(e);
//		}
//	}
	
	public CusHandoverApp getCusHandoverAppByKey(String serno) throws EMPException {
		CusHandoverApp cusHandoverApp = new CusHandoverApp();
		TableModelDAO tmd = this.getTableModelDAO();
		KeyedCollection kcoll = tmd.queryDetail(PUBConstant.CUSHANDOVERAPP, serno, this.getConnection());
		
		ComponentHelper ch = new ComponentHelper();
		if(cusHandoverApp!=null){
			cusHandoverApp = (CusHandoverApp) ch.kcolTOdomain(cusHandoverApp, kcoll);
			return cusHandoverApp;
		}else{
			return null;
		}
	}

}
