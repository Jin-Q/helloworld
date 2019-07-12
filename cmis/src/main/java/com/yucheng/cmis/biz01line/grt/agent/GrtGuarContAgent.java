package com.yucheng.cmis.biz01line.grt.agent;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.grt.dao.GrtGuarContDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
public class GrtGuarContAgent extends CMISAgent {

	//新增保证人的同时新增保证人与担保合同的关联信息
	public void insertGrtGuarantyRe(KeyedCollection kColl) throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			cmisDao.insertGrtGuarantyRe(kColl);
		} catch (Exception e) {
			throw new AgentException("新增保证人信息时，新增保证人与关联合同的关联信息出错，错误原因："+e.getMessage());
		}
	}
	//删除保证人的同时删除其与担保合同的关联记录
	public int deleteGrtGuarantyReGuar(String guarId) throws AgentException {
		int count=0;
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			count = cmisDao.deleteGrtGuarantyReGuar(guarId);
		} catch (Exception e) {
			throw new AgentException("根据保证人编号，删除保证人与担保合同的关联信息出错，错误原因："+e.getMessage());
		}
		return count;
		
	}
	//删除一笔合同的同时删除其与保证人的关联记录
	public int deleteGrtGuarantyReCont(String guarContN0) throws AgentException {
		int count=0;
		try {
		GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
		count = cmisDao.deleteGrtGuarantyReCont(guarContN0);
		} catch (Exception e) {
			throw new AgentException("根据担保合同编号，级联删除保证人信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 通过合同编号获得其下关联下的担保信息
	 * @param guarContNo 合同编号
	 * @return KeyedCollection
	 */
	public IndexedCollection queryGrtGuarantyReGuarList(String guarContN0) throws AgentException {
		try {
		GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
		return cmisDao.queryGrtGuarantyReGuarList(guarContN0);
		} catch (Exception e) {
			throw new AgentException("根据担保合同编号，获得其下关联的担保信息出错，错误原因："+e.getMessage());
		}
	}
	
	/**
	 * 借助担保合同和保证人的关联关系表查询担保合同表和保证人信息表中所有相关的保证人信息和担保合同信息
	 * @param guarContNo
	 * @param  客户编号，担保合同编号
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection queryGrtGuaranteeAllList() throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			return cmisDao.queryGrtGuaranteeAllList();
		} catch (Exception e) {
			throw new AgentException("根据担保合同和保证人关系表，查询担保合同列表和保证人列表信息出错，错误原因："+e.getMessage());
		}
	}
	/**
	 * 通过担保合同编号和保证人客户编号通过关联关系表查出唯一相关的保证人信息和合同信息
	 * @param guarContNo
	 * @param cusId，guarContNo 客户编号，担保合同编号
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection queryGrtGuaranteeAllDetail(String guarContNo,String cusId) throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			return cmisDao.queryGrtGuaranteeAllDetail(guarContNo,cusId);
		} catch (Exception e) {
			throw new AgentException("根据担保合同和保证人关系表，查询担保合同列表和保证人列表信息出错，错误原因："+e.getMessage());
		}
	}
	/**根据合同编号获取池编号（一个担保合同只能引用一个池作为担保品）*/
	public String getDrfpoNoByGuarContNo(String guarContNo) throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			return cmisDao.getDrfpoNoByGuarContNo(guarContNo);
		} catch (Exception e) {
			throw new AgentException("根据合同编号，获取与其关联的票据池编号出错，错误原因："+e.getMessage());
		}
	}
	/**根据合同编号获取该合同下保证人担保金额的总额*/
	public String getGuarAmtByGuarContNo(String guarContNo) throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			return cmisDao.getGuarAmtByGuarContNo(guarContNo);
		} catch (Exception e) {
			throw new AgentException("根据担保合同编号，获取该笔合同下的保证人担保金额总额信息出错，错误原因："+e.getMessage());
		}
	}
	/**根据担保合同编号获取相关联下的押品担保金额累加和*/
	public String queryGrtGuarantyAmt(String guarContNo) throws AgentException {
		try {
			GrtGuarContDao cmisDao = (GrtGuarContDao)this.getDaoInstance("GrtGuarCont");
			return cmisDao.queryGrtGuarantyAmt(guarContNo);
		} catch (Exception e) {
			throw new AgentException("根据担保合同编号["+guarContNo+"]获取相关联下的押品担保金额累加和信息出错，错误原因："+e.getMessage());
		}
	}
}