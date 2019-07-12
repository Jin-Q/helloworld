package com.yucheng.cmis.biz01line.cus.cusbase.component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusBaseAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComAgent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.agent.CusIndivAgent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;


public class CusBaseComponent extends CMISComponent {

	private static final Logger logger = Logger.getLogger(CusBaseComponent.class);

	public CusBase CheakCusBase(CusBase cusBase) throws Exception {

		CusBase cusBaseFromDB = new CusBase();
		String certCode = cusBase.getCertCode();
		String certType = cusBase.getCertType();

		// 创建对应的代理类
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusBaseFromDB = cusAgent.findCusBaseByComCert(certCode, certType);
		return cusBaseFromDB;
	}

	public String addCusBase(CusBase cusBase) throws AgentException {

		// 返回信息
		String retBaseMessage = CMISMessage.ADDDEFEAT;
		// 创建对应的代理类
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		String cusName = cusBase.getCusName();
		// 设置最早开户日期
		cusBase.setOpenDate(this.getCurDate());
		// 客户简称
		cusBase.setCusShortName(cusName);
		// 新增Cus_Base表数据
		retBaseMessage = cusAgent.insertCusBase(cusBase);
		return retBaseMessage;
	}

	public String addCus(CusBase cusBase, KeyedCollection kColl) throws EMPException {
		String retCusMessage = CMISMessage.DEFEAT;
		CusCom cusCom = null;
		CusIndiv cusIndiv = null;

		String certType = cusBase.getCertType();
		ComponentHelper cHelper = new ComponentHelper();
		
		// 创建对应的代理类
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		if ("20".equals(certType)||"a".equals(certType)||"b".equals(certType)||"X".equals(certType)) {// 对公客户
			cusCom = new CusCom();
			cusCom = (CusCom) cHelper.kcolTOdomain(cusCom, kColl);
			String cusType = cusBase.getCusType();
			if(cusType==null||"".equals(cusType)){
				throw new EMPException("客户类型为空，开户失败，请检查！");
			}
			if(cusType.startsWith("D")){//事业客户用企事业报表
				cusCom.setComFinRepType("PB0015");
			}else if("A2".equals(cusType)){//担保公司客户
				cusCom.setComFinRepType("PB0002");
			}else{
				cusCom.setComFinRepType("PB0001");
			}
			retCusMessage = cusAgent.insertCusCom(cusCom);
			
//			String indivDtOfBirth = null;
//			if (certType.equals("10") || certType.equals("17")) {
//				if (certCode.length() == 15) {
//					indivDtOfBirth = "19" + certCode.substring(6, 12);
//				} else {
//					indivDtOfBirth = certCode.substring(6, 14);
//				}
//				indivDtOfBirth = indivDtOfBirth.substring(0, 4) + "-"
//						+ indivDtOfBirth.substring(4, 6) + "-"
//						+ indivDtOfBirth.substring(6, 8);
//				cusIndiv.setIndivDtOfBirth(indivDtOfBirth);
//			}
			
		} else {// 个人客户
			cusIndiv = new CusIndiv();
			cusIndiv = (CusIndiv) cHelper.kcolTOdomain(cusIndiv, kColl);
			retCusMessage = cusAgent.insertCusIndiv(cusIndiv);
		}
		return retCusMessage;
	}
	
	/**
	 * 根据客户码查询客户基表信息
	 * @param cusId
	 * @return
	 * @throws ComponentException
	 */
	public CusBase getCusBase(String cusId) throws ComponentException {
		if (cusId == null || cusId.trim().length() == 0) {
			throw new ComponentException("传入客户号为空");
		}
		CusBase cusBase = null;
		try {
			CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
			cusBase = cusAgent.getCusBaseDomainByCusId(cusId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ComponentException("读取客户信息基表失败，获取不到客户编号为【" + cusId+ "】的客户信息");
		}
		return cusBase;
	}

	/**
	 * 通过客户代码和查询条件获取cusIndiv对象 condition的处理方式根据表模型的DAO来具体确定，目前暂不考虑
	 * 
	 * @param cusId 客户代码
	 * @return CusIndiv 对私客户对象
	 * @throws Exception 
	 */
	public CusIndiv getCusIndiv(String cusId) throws Exception {
		CusIndiv cusIndiv = new CusIndiv();
		if (cusId == null || cusId.trim().length() == 0) {
			throw new Exception("传入客户号为空");
		} else {
			// 创建对应的代理类
			CusIndivAgent cusIndivAgent = (CusIndivAgent) this.getAgentInstance(PUBConstant.CUSINDIV);
			cusIndiv = cusIndivAgent.findCusIndivByCusId(cusId);
		}
		return cusIndiv;
	}

	/**
	 * 通过客户代码和查询条件获取cusCom对象 condition的处理方式根据表模型的DAO来具体确定，目前暂不考虑
	 * 
	 * @param cusId 客户代码
	 * @return CusCom 对公客户对象
	 * @throws Exception 
	 */
	public CusCom getCusCom(String cusId) throws Exception {
		CusCom cusCom = null;
		if (cusId == null || cusId.trim().length() == 0) {
			throw new Exception("传入客户号为空");
		} else {
			// 创建对应的代理类
			CusComAgent cusComAgent = (CusComAgent) this.getAgentInstance(PUBConstant.CUSCOM);
			cusCom = cusComAgent.findCusComByCusId(cusId);
		}
		return cusCom;
	}

	//根据客户Id名称查询对应属性放入iColl中
	public void getICollCusById(IndexedCollection iColl, Map<String,String> baseColMap, Map<String,String> colMap, String cusId)
		throws Exception {
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusAgent.getICollCusById(iColl, baseColMap, colMap, cusId);
	}
	
	//根据客户Id名称查询对应属性放入iColl中
	public void getKCollCusById(KeyedCollection kColl, Map<String,String> baseColMap, Map<String,String> colMap, String cusId)
		throws AgentException {
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusAgent.getKCollCusById(kColl, baseColMap, colMap, cusId);
	}
	
	// 传入icoll 字段mapping
	public void getICollCus(IndexedCollection iColl, Map<String,String> colMap, Map<String,String> pkMap)
			throws Exception {
		CusBaseAgent cusAgent = (CusBaseAgent) this
				.getAgentInstance(PUBConstant.CUSBASE);
		cusAgent.getICollCus(iColl, colMap, pkMap);
	}

	public void getKCollCus(KeyedCollection kColl, Map<String,String> colMap, Map<String,String> pkMap)
			throws AgentException {
		CusBaseAgent cusAgent = (CusBaseAgent) this
				.getAgentInstance(PUBConstant.CUSBASE);
		cusAgent.getKCollCus(kColl, colMap, pkMap);
	}

	public CusBase getCusBaseByCert(String certCode, String certType)
			throws Exception {
		CusBase cusBaseFromDB = new CusBase();
		// 创建对应的代理类
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusBaseFromDB = cusAgent.findCusBaseByComCert(certCode, certType);
		return cusBaseFromDB;
	}
	
	public KeyedCollection getCusBaseByCert1(String certCode, String certType)
			throws Exception {
		KeyedCollection cusBaseFromDB = new KeyedCollection();
		// 创建对应的代理类
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusBaseFromDB = cusAgent.findCusBaseByComCert1(certCode, certType);
		return cusBaseFromDB;
	}
	

	/**
	 * 修改客户信息根据CusIndiv对象更新数据库数据
	 * 
	 * @param cusIndiv
	 * @return String 返回String类型用于描述更新状态
	 * @throws EMPException
	 */
	public String modifyCusBase(CusBase cusBase) throws EMPException,ComponentException {
		// 返回信息
		String strReturnMessage = CMISMessage.MODIFYDEFEAT;
		try {
			CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
			strReturnMessage = cusAgent.update(cusBase);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		}
		return strReturnMessage;
	};

	public boolean delCusTrusteeRecord(String consignor_id, String trustee_id) throws Exception{
		CusBaseAgent cusAgent = (CusBaseAgent) this
		.getAgentInstance(PUBConstant.CUSBASE);
		return cusAgent.delCusTrusteeRecord(consignor_id, trustee_id);
	}
	
	/**
	 * 根据 客户经理ID  从客户信息基表中 获取该客户经理管户的客户对象LIST
	 * @param custMgr 	          客户经理ID
	 * @return cusBaseList         客户对象的LIST
	 * @throws ComponentException 
	 */
   public List<CMISDomain> findCusListByCustMgr(String custMgr) throws ComponentException{
		List<CMISDomain> cusBaseList = new ArrayList<CMISDomain>();
		CusBaseAgent cusBaseAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusBaseList = cusBaseAgent.findCusListByCustMgr(custMgr);
		return cusBaseList;
	}
   
   public void delSubmitRecord(String tableName, String serno) throws ComponentException, SQLException{
		CusBaseAgent cusAgent = (CusBaseAgent) this.getAgentInstance(PUBConstant.CUSBASE);
		cusAgent.delSubmitRecord(tableName, serno);
	}
   
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model
	 * @param conditionFields
	 * @return
	 * @throws AgentException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields ) throws ComponentException {
		int count = 0;
		try {
			CusBaseAgent cusAgent = (CusBaseAgent)this.getAgentInstance("CusBase");
			count = cusAgent.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new ComponentException("删除关联信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 插入强制变更正式客户 记录信息
	 * @param cus_id
	 * @return
	 * @throws ComponentException
	 */
	public int coerceChangeCusOp(String cus_id,Context context,Connection connection) throws EMPException {
		int count = 0;
		try {
			String pk = CMISSequenceService4JXXD.querySequenceFromDB("PK", "fromDate", connection, context);
			String handle_id = (String)context.getDataValue("currentUserId");
			String handle_br_id = (String)context.getDataValue("organNo");
			String openday = (String)context.getDataValue("OPENDAY");
			Map<String,String> map = new HashMap<String,String>();
			map.put("pk_id", pk);
			map.put("cus_id", cus_id);
			map.put("handle_id", handle_id);
			map.put("handle_br_id", handle_br_id);
			map.put("change_date", openday);
			count = SqlClient.insert("insertWriteCoerceChange", map, connection);
		}catch (Exception e) {
			throw new EMPException("插入强制变更正式客户 记录信息，错误原因："+e.getMessage());
		}
		return count;
	}
   
}
