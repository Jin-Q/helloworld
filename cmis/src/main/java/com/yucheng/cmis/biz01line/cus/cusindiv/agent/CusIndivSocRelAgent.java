package com.yucheng.cmis.biz01line.cus.cusindiv.agent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cuscom.dao.CusIndivSocRelDao;
import com.yucheng.cmis.biz01line.cus.cusindiv.dao.CusIndivDao;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusComAgent.java
 * @Version 1.0
 * @Since 1.0 Sep 12, 2008 4:55:31 PM
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：本类主要代理客户基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */

public class CusIndivSocRelAgent extends CMISAgent {

	public CusIndivSocRel checkExist(CusIndivSocRel cusIndivSocRel)
			throws AgentException {
		CusIndivSocRel pcusIndivSocRel = new CusIndivSocRel();
		String modelId = PUBConstant.CUSINDIVSOCREL;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusIndivSocRel.getCusId());
		pk_values.put("cus_id_rel", cusIndivSocRel.getCusIdRel());
		pcusIndivSocRel = (CusIndivSocRel) this.findCMISDomainByKeywords(cusIndivSocRel, modelId, pk_values);
		return pcusIndivSocRel;
	}

	/**
	 * 根据CUS_ID检查该客户父母情况
	 * 
	 * @param cusId
	 * @return
	 * @throws AgentException
	 */
	public String checkParents(String cusId) throws Exception {
		Connection conn = null;
		conn = this.getConnection();
		CusIndivDao dao = new CusIndivDao();
		return dao.checkParents(cusId, conn);
	}
	
	/**
	 * 根据CUS_ID检查该客户是否存在配偶信息
	 * 
	 * @param cusId
	 * @return
	 * @throws AgentException
	 */
	public String checkExist(String cusId) throws Exception {
		Connection conn = null;
		conn = this.getConnection();
		CusIndivDao dao = new CusIndivDao();
		return dao.checkSPS(cusId, conn);
	}
	
	public int deleteCusIndivSocRel(String cusId,String cusIdRel,String relType) throws Exception{
		Connection conn = null;
		conn = this.getConnection();
		CusIndivDao dao = new CusIndivDao();
		int i = 0;
		try {
			i = dao.deleteCusIndivSocRel(cusId, cusIdRel, relType, conn);
		} catch (ComponentException e) {
			throw new ComponentException();
		} catch (EMPException e) {
			throw e;
		}
		return i;
	}
	/**
	 * 根据主客户ID得到配偶的CusIndivSocRel的对象
	 * @param cusid 主客户ID关联 CusIndivSocRel.cus_id_rel 字段
	 * @return CusIndivSocRel
	 * @throws AgentException 
	 * @throws InvalidArgumentException 
	 * @throws ObjectNotFoundException 
	 */
	public CusIndivSocRel getSpouseCusIndivSocRel(String cusid) throws AgentException, ObjectNotFoundException, InvalidArgumentException  {
		CusIndivSocRel domain = new CusIndivSocRel();
		/*
		 * 是否有配偶的条件
		 * indiv_cus_rel  = 1  //配偶关系
		 * 关联客户客户码  cus_id = cusid
		 */
		try {
			Connection conn = this.getConnection();
			TableModelDAO tmd =  this.getTableModelDAO();
			ComponentHelper componetHelper = new ComponentHelper();
			String modelId = "CusIndivSocRel";
			String condition = "where indiv_cus_rel=1 and cus_id='" + cusid + "'";
			//查询 cus_id 在Cus_Indiv_Soc_Rel表中的信息
			KeyedCollection kCol = tmd.queryFirst(modelId, null, condition, conn);
			if (kCol != null)
				componetHelper.kcolTOdomain(domain, kCol);
			
		} catch (Exception e) {
			throw new AgentException("出现异常!", e);
		} 
		return domain;
	}
	
	/**
	 * 检查社会关系是否存在
	 * 
	 * @param cusId
	 * @param relType
	 * @return
	 * @throws ComponentException
	 */
	public String checkSocRelExist(String cusId, String relType,String cusIdRel)
			throws ComponentException {
		CusIndivSocRelDao ccmfDao = new CusIndivSocRelDao();
		Connection conn = this.getConnection();
		return ccmfDao.checkSocRelExist(cusId, relType,cusIdRel, conn);
	}

	/**
	 * 插入CUS_INDIV_SOC_REL
	 * 
	 * @param cusCom
	 * @return
	 * @throws AgentException
	 */
	public String insert(CusIndivSocRel cusIndivSocRel) throws Exception {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息

		CusIndivSocRel cusIndivSocRel1 = new CusIndivSocRel();
		String indivCusRel = cusIndivSocRel.getIndivCusRel();
		if("2".equals(indivCusRel)){
			cusIndivSocRel1.setIndivCusRel("3");
		}else if("3".equals(indivCusRel)){
			cusIndivSocRel1.setIndivCusRel("2");
		}else{
			cusIndivSocRel1.setIndivCusRel(cusIndivSocRel.getIndivCusRel());// 与客户关系
		}
		cusIndivSocRel1.setCusId(cusIndivSocRel.getCusIdRel());// 客户代码
		cusIndivSocRel1.setCusIdRel(cusIndivSocRel.getCusId());// 关联客户代码
		cusIndivSocRel1.setIndivFamilyFlg(cusIndivSocRel.getIndivFamilyFlg());// 是否家庭成员
		cusIndivSocRel1.setInputBrId(cusIndivSocRel.getInputBrId());
		cusIndivSocRel1.setInputId(cusIndivSocRel.getInputId());
		cusIndivSocRel1.setInputDate(this.getContext().getDataValue("OPENDAY").toString());

		int intMessage = this.insertCMISDomain(cusIndivSocRel,PUBConstant.CUSINDIVSOCREL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		intMessage = this.insertCMISDomain(cusIndivSocRel1,PUBConstant.CUSINDIVSOCREL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		return strMessage;

	}

	/**
	 * 更新CUS_INDIV_SOC_REL
	 * 
	 * @param cusIndiv
	 * @param cusId
	 * @param relType
	 * @return
	 * @throws ComponentException
	 */
	public String update(CusIndivSocRel cusIndivSocRel) throws Exception {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息

		CusIndivSocRel cusIndivSocRel1 = new CusIndivSocRel();
		String indivCusRel = cusIndivSocRel.getIndivCusRel();
		if("2".equals(indivCusRel)){
			cusIndivSocRel1.setIndivCusRel("3");
		}else if("3".equals(indivCusRel)){
			cusIndivSocRel1.setIndivCusRel("2");
		}else{
			cusIndivSocRel1.setIndivCusRel(cusIndivSocRel.getIndivCusRel());// 与客户关系
		}
		cusIndivSocRel1.setCusId(cusIndivSocRel.getCusIdRel());// 客户代码
		cusIndivSocRel1.setCusIdRel(cusIndivSocRel.getCusId());// 关联客户代码
		cusIndivSocRel1.setLastUpdId(cusIndivSocRel.getInputId());
		cusIndivSocRel1.setLastUpdDate(this.getContext().getDataValue("OPENDAY").toString());

		int intMessage = this.modifyCMISDomain(cusIndivSocRel,PUBConstant.CUSINDIVSOCREL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		intMessage = this.modifyCMISDomain(cusIndivSocRel1,PUBConstant.CUSINDIVSOCREL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		return strMessage;
	}
}
