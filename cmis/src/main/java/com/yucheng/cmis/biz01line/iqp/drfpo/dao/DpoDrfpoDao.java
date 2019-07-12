package com.yucheng.cmis.biz01line.iqp.drfpo.dao;

import java.math.BigDecimal;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class DpoDrfpoDao extends CMISDao{
	
	/**
	 * 通过汇票号码获取汇票信息（票据池时使用）
	 * @param porderno 汇票号码
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getPorderMsgByPorderNoDrfpo(String porderno,String drfpo_no) throws DaoException {
		KeyedCollection kc = null;
		try {
			kc = (KeyedCollection)SqlClient.queryFirst("getPorderMsgByPorderNoDrfpo", porderno, null, this.getConnection());
			if(kc != null && kc.size() > 0){
				String status = (String) kc.getDataValue("status");
				if("01".equals(status)){//处于登记状态时
					//查看此票据有没有存在于某个池中，如果存在则返回此票据的责任人和责任机构
					KeyedCollection k = (KeyedCollection)SqlClient.queryFirst("getManagementInfoByPorderNo", porderno, null, this.getConnection());
					if(null==k){
						kc.addDataField("code", "0000");
						kc.addDataField("msg", "存在该票据信息，自动赋值");
					}else{
						SInfoUtils.addSOrgName(k, new String[] { "manager_br_id"});
						SInfoUtils.addUSerName(k, new String[] { "manager_id"});
						if(drfpo_no.equals(k.getDataValue("drfpo_no"))){
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于本票据池中");
						}else{
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于其他票据池中，票据池编号"+k.getDataValue("drfpo_no")+";主管客户经理："+k.getDataValue("manager_id_displayname")+";主管机构："+k.getDataValue("manager_br_id_displayname"));
						}
					}
				}else if("03".equals(status)){//处于托收状态时
					//查看此票据有没有存在于某个池中，如果存在则返回此票据的责任人和责任机构
					KeyedCollection k = (KeyedCollection)SqlClient.queryFirst("getManagementInfoByPorderNo", porderno, null, this.getConnection());
					if(null!=k){
						SInfoUtils.addSOrgName(k, new String[] { "manager_br_id"});
						SInfoUtils.addUSerName(k, new String[] { "manager_id"});
						if(drfpo_no.equals(k.getDataValue("drfpo_no"))){
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于本票据池中，票据托收状态");
						}else{
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于其他票据池中，票据池编号"+k.getDataValue("drfpo_no")+";主管客户经理："+k.getDataValue("manager_id_displayname")+";主管机构："+k.getDataValue("manager_br_id_displayname"));
						}					
					}else{
						kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
						kc.addDataField("msg", "该笔票据存在非法操作！");
					}
				}else if("05".equals(status)){//处于质押状态时
					//查看此票据有没有存在于某个池中，如果存在则返回此票据的责任人和责任机构
					KeyedCollection k = (KeyedCollection)SqlClient.queryFirst("getManagementInfoByPorderNo", porderno, null, this.getConnection());
					if(null!=k){
						SInfoUtils.addSOrgName(k, new String[] { "manager_br_id"});
						SInfoUtils.addUSerName(k, new String[] { "manager_id"});
						if(drfpo_no.equals(k.getDataValue("drfpo_no"))){
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于本票据池中，为在池状态");
						}else{
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于其他票据池中，票据池编号"+k.getDataValue("drfpo_no")+";主管客户经理："+k.getDataValue("manager_id_displayname")+";主管机构："+k.getDataValue("manager_br_id_displayname"));
						}	
					}else{
						kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
						kc.addDataField("msg", "该笔票据存在非法操作！");
					}
				}else if("06".equals(status)){//解质押状态时
					//查看此票据有没有存在于某个池中，如果存在则返回此票据的责任人和责任机构
					KeyedCollection k = (KeyedCollection)SqlClient.queryFirst("getManagementInfoByPorderNo", porderno, null, this.getConnection());
					if(null!=k){
						if(drfpo_no.equals(k.getDataValue("drfpo_no"))){
							kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
							kc.addDataField("msg", "该笔票据已存在于本票据池中，为解质押状态，不能重复进行新增！");
						}else{
							kc.addDataField("code", "0000");
							kc.addDataField("msg", "存在该票据信息，自动赋值");
						}
					}else{
						kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
						kc.addDataField("msg", "该笔票据存在非法操作！");
					}
				}else if("04".equals(status)){//核销状态（已完成其使命，关系表中保留其尸体作为纪念）
					kc.addDataField("code", "0001");//不能在其他的票据池中进行新增
					kc.addDataField("msg", "该笔票据已经被核销掉，不能在本票据池中进行新增操作！");
				}			
			}else {
				kc = new KeyedCollection();
				kc.addDataField("code", "9999");
				kc.addDataField("msg", "不存在该票据信息，可以新增");
			}
		} catch (Exception e) {
			throw new DaoException("通过汇票号码获取汇票信息信息失败"+e.getMessage());
		} 
		return kc;
	}
	/**
	 * 通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录
	 * @param drfpoNo 票据池编号
	 * @return
	 * @throws Exception
	 */
	public int deleteDrfpoByDrfpoNo(String drfpoNo) throws DaoException{
		int delKCollResult=0;
		 try {
			 delKCollResult = SqlClient.executeUpd("deleteDrfpoRelationByDrfpoNo", drfpoNo, null, null, this.getConnection());
			 delKCollResult += SqlClient.executeUpd("deleteDrfpoByDrfpoNo", drfpoNo, null, null, this.getConnection());
		 	} catch (Exception e) {
				throw new DaoException(" 通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录失败"+e.getMessage());
			} 
			return delKCollResult;
		}
	/**
	 * 通过池编号来查询相关的汇票信息
	 * @param drfpoNo 票据池编号 Stutas 票据在池状态
	 * @return
	 * @throws Exception
	 * */
	public IndexedCollection getPorderListByDrfpoNo(String drfpoNo,String status)throws DaoException {
		IndexedCollection nextIColl = null;
		DataSource dataSource = null;
		dataSource = (DataSource)this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
		try {
			String sql_select = "select '"+drfpoNo+"' as drfpo_no,a.porder_no,a.bill_type,a.is_ebill,a.bill_isse_date,a.porder_end_date,a.drft_amt,c.status " +
					"from iqp_bill_detail_info a,iqp_corre_info c where a.porder_no in (select b.porder_no from iqp_corre_info b " +
					"where b.status in("+status+") and b.drfpo_no='"+drfpoNo+"') and a.porder_no = c.porder_no";
			nextIColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
		} catch (Exception e) {
			throw new DaoException("通过池编号来查询相关的汇票信息"+e.getMessage());
		} 
		return nextIColl;
	}
	/**
	 * 根据池编号获得所属池下的票据信息
	 * @param drfpoNo 汇票号码
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getBillInfoByDrfpoNo(String drfpoNo)throws DaoException {
		IndexedCollection nextIColl = null;
		try {
			nextIColl = SqlClient.queryList4IColl("getBillInfoByDrfpoNo",drfpoNo, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据池编号获得所属池下的票据信息"+e.getMessage());
		} 
		return nextIColl;
	}
	/**
	 * 通过池编号和票据的池状态来查询汇票票面金额
	 * @param drfpoNo 票据池编号 Stutas 票据在池状态 （00-待入池，01-在池，02-托收，03-出池）
	 * @return
	 * @throws Exception
	 * */
	public Double getDrftAmtByDrfpoNo(String drfpoNo,String status)throws DaoException {
		double inPoolAmt = 0.0;
		Map<String, String> selMap = new HashedMap();
		KeyedCollection kc = new KeyedCollection();
		IndexedCollection nextIColl = null;
		try {
			selMap.put("status",status);
			selMap.put("drfpo_no",drfpoNo);
			nextIColl = SqlClient.queryList4IColl("getDrftAmtByDrfpoNo",selMap, this.getConnection());
			if(nextIColl.size()!=0){
				for(int i=0;i<nextIColl.size();i++){
					kc = (KeyedCollection) nextIColl.get(i);
					inPoolAmt+=((BigDecimal) kc.getDataValue("drft_amt")).doubleValue() ;
				}
			}
			
		} catch (Exception e) {
			throw new DaoException("通过池编号和票据的池状态来查询汇票票面金额信息失败"+e.getMessage());
		} 
		return inPoolAmt;
	}
}
