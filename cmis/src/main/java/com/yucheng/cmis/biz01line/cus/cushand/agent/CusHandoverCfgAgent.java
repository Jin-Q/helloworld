package com.yucheng.cmis.biz01line.cus.cushand.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cushand.domain.CusHandoverCfg;
import com.yucheng.cmis.biz01line.cus.cushand.domain.CusHandoverDetail;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 客户移交方案
 * @author HuChunyan
 * 2011-3-2
 */

public class CusHandoverCfgAgent extends CMISAgent {
	/**
	 * 根据移交方式和移交范围得到流水号
	 * 
	 * @param tableMode 移交方式
	 * @param tableScope 移交范围
	 * @return
	 * @throws AgentException 
	 */
	public String getSerno(String tableMode, String tableScope) throws AgentException {
		Connection conn = null;
		KeyedCollection kc = null;
		String serno = null;
		try {
			conn = this.getConnection();
			TableModelDAO tDao = this.getTableModelDAO();
			String modelId = "CusHandoverCfg";
			kc = tDao.queryFirst(modelId, null, "where table_mode='"
					+ tableMode + "' and table_scope='" + tableScope + "'",
					conn);
			Object dataValue = kc.getDataValue("serno");
			if (dataValue != null) {
				serno = String.valueOf(dataValue);
			}

		} catch (Exception e) {
			throw new AgentException();
		}
		return serno;
	}
/**
 * 根据移交方式和移交范围得到domain对象
 * @param tableMode 移交方式
 * @param tableScope 移交范围
 * @return
 * @throws AgentException
 */
	public CusHandoverCfg geCusHandoverCfg(String tableMode, String tableScope)
			throws AgentException {
		CusHandoverCfg domain = new CusHandoverCfg();
		try {
			Connection conn = this.getConnection();
			TableModelDAO tmd = this.getTableModelDAO();
			ComponentHelper componetHelper = new ComponentHelper();
			String modelId = "CusHandoverCfg";
			String condition = "where table_mode='" + tableMode
					+ "' and table_scope='" + tableScope + "'";
			KeyedCollection kCol = tmd.queryFirst(modelId, null, condition,
					conn);
			if (kCol != null)
				componetHelper.kcolTOdomain(domain, kCol);

		} catch (Exception e) {
			throw new AgentException("出现异常!", e);
		}
		return domain;

	}


	/**
	 * 获取客户移交配置明细数据
	 * @param serno 方案流水号
	 * @return
	 * @throws AgentException
	 */
	@SuppressWarnings("unchecked")
	public List<CusHandoverDetail> getCusHandoverDetailListBySerno(String serno)throws AgentException{
		Connection conn = null;
		List list = new ArrayList<CusHandoverDetail>();
		IndexedCollection iColl=null;
		ComponentHelper ch = new ComponentHelper();
		try {
		conn = this.getConnection();
        TableModelDAO tDao = this.getTableModelDAO();  
        String modelId="CusHandoverDetail";
        String condition="where serno='"+serno+"' order by sort";
		iColl=tDao.queryList(modelId, condition, conn);
		if(iColl!=null&&!iColl.isEmpty()){
			list=ch.icol2domainlist(CusHandoverDetail.class.getName(), iColl);
		}
		
	} catch (Exception e) {
		throw new AgentException("移交信息异常",e);
	}
	return list;
	}
}
