package com.yucheng.cmis.biz01line.cus.cuscom.agent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.cus.cuscom.dao.CusComManagerDao;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.pub.CMISAgent;
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

public class CusComManagerAgent extends CMISAgent {

	/**
	 * 根据CUS_ID,CUS_MRG_CERT_TYP,COM_MRG_CERT_CODE查询高管是否存在
	 * 
	 * @param cusCom 客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public CusComManager checkExist(CusComManager cusComManager)
			throws AgentException {
		CusComManager pcusComManager = new CusComManager();
		String modelId = PUBConstant.CUSCOMMANAGER;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id_rel", cusComManager.getCusIdRel());
		pk_values.put("cus_id", cusComManager.getCusId());
//		pk_values.put("com_mrg_cert_typ", cusComManager.getComMrgCertTyp());
//		pk_values.put("com_mrg_cert_code", cusComManager.getComMrgCertCode());
		// add by loujc 对于高管人一人兼多职校验暂时去掉！ 2009-10-12
		pk_values.put("com_mrg_typ", cusComManager.getComMrgTyp());
		pcusComManager = (CusComManager) this.findCMISDomainByKeywords(cusComManager, modelId, pk_values);
		return pcusComManager;

	}
	/**
	 * <p>根据高管类型查询该客户项下是否已存在</p>
	 * @param cusComManager
	 * @return
	 * @throws AgentException
	 * @author ZhouJianFeng
	 * @throws SQLException 
	 */
	public boolean checkExistByMrgType(CusComManager cusComManager) throws Exception {
		CusComManagerDao dao = new CusComManagerDao();
		Connection conn = this.getConnection();
		return dao.getCntByComMrgType(cusComManager.getComMrgTyp(), cusComManager.getCusId(), conn);
	}

	/**
	 * 根据对公客户号到高管表查询法人代表信息
	 * 
	 * @param cusId
	 * @return
	 * @throws ComponentException
	 */
	public CusComManager getCusComManager(String cusId) throws Exception {
		CusComManager ccm = new CusComManager();
		CusComManagerDao dao = new CusComManagerDao();
		Connection conn = this.getConnection();
		ccm = dao.getCusComManager(cusId, conn);
		return ccm;
	}
}
