package com.yucheng.cmis.biz01line.pvp.dao;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;

public class PvpDao extends CMISDao {
	/**
	 * 通过合同编号生成借据编号（泉州银行借据规则：合同编号+3位序列号）
	 * @param contNo 合同编号
	 * @return KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getBillNoByContNo(String contNo) throws Exception {
		KeyedCollection kc = null;
		kc = (KeyedCollection)SqlClient.queryFirst("isHasRecord", contNo, null, this.getConnection());
		return kc;
	}
	/**
	 * 根据合同号插入一条新的序列号记录
	 * @param contNo 合同编号
	 * @throws Exception
	 */
	public void insertPvpAutocode(String contNo) throws Exception {
		int result = SqlClient.insert("insertPvpAutocode", contNo, this.getConnection());
	}
	/**
	 * 根据合同号更新序列号记录
	 * @param contNo 合同编号
	 * @param sernum 序列号
	 * @throws Exception
	 */
	public void updatePvpAutocode(String contNo, String sernum) throws Exception {
		int result = SqlClient.executeUpd("updatePvpAutocode",contNo ,sernum,null , this.getConnection());
	}
	
	public String getSernoByContNo(String contNo) throws Exception{
		KeyedCollection kc = null;
		kc = (KeyedCollection)SqlClient.queryFirst("selectSernoByContNo", contNo, null, this.getConnection());
		return (String)kc.getDataValue("serno");
	}
}
