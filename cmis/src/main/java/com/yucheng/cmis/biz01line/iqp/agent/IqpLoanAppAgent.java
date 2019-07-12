package com.yucheng.cmis.biz01line.iqp.agent;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpLoanAppDao;
import com.yucheng.cmis.pub.CMISAgent;

public class IqpLoanAppAgent extends CMISAgent {

	/**
	 * 通过币种获取汇率
	 * @param currType
	 * @return 汇率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getHLByCurrType(String currType) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.getHLByCurrType(currType);
	}
	
	/**
	 * 通过客户码查询客户所属业务条线
	 * @param currType
	 * @return 客户条线以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getBelglineByKhm(String cus_id) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.getBelglineByKhm(cus_id);
	}
	
	/**
	 * 通过业务品种、币种、期限获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getRate(String prdId, String currType, int termM) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.getRate(prdId,currType,termM);
	}
	/**
	 * 通过流水号查询所有业务申请表中是否有记录数据
	 * @param serno 流水号
	 * @return 表名、流水号组成的集合
	 * @throws Exception
	 */
	public List querySubTablesBySerno(String serno)throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.querySubTablesBySerno(serno);
	}
	/**
	 * 通过表名和流水号删除表中记录
	 * @param tname 表名
	 * @param serno 流水号
	 * @return
	 * @throws Exception
	 */
	public int delSubTablesRecordBySerno(String tname, String serno) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.delSubTablesRecordBySerno(tname,serno);
	}
	/**
	 * 往票据批次关联表中插入关联数据
	 * @param param 数据Map
	 * @return
	 * @throws Exception
	 */
	public int insertIqpBatchBillRel(Map<String, String> param) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpBatchBillRel(param);
	}
	/**
	 * 通过汇票号码获取汇票信息
	 * @param porderno 汇票号码
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getPorderMsgByPorderNo(String porderno) throws Exception {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.getPorderMsgByPorderNo(porderno);
	}
	/**
	 * 通过asset_no删除Tab页
	 * @param asset_no
	 * @return
	 */
	public int deleteTabByAssetno(String asset_no) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.deleteTabByAssetno(asset_no);
	}
	
	public int updateGrtLoanRGurLvlYB(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.updateGrtLoanRGurLvlYB(map, connection);
	}
	public int updateGrtLoanRGurLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.updateGrtLoanRGurLvlZGE(map, connection);
	}
	/**
	 * 信用证修改时，关联关系为正常的改为续作
	 * @cont_no 合同编号
	 * @return 修改结果
	 * @throws Exception 
	 */
	public int updateGrtLoanRGurCorreRel(String serno,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.updateGrtLoanRGurCorreRel(serno, connection);     
	}
	/**
	 * 通过担保合同编号字符串查询合同编号
	 * guar_cont_no 担保合同字符串 
	 * @return 修改结果
	 * @throws Exception 
	 */
	public IndexedCollection getGrtLoanRGur(String guar_cont_no,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.getGrtLoanRGur(guar_cont_no, connection);    
	}
	
	/**
	 * 删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMem(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.removeIqpAppMem(map, connection);
	}
	/**
	 *删除入网退网申请删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMemByApp(String serno,Connection connection) throws Exception{
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.removeIqpAppMemByApp(serno, connection);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppMemMana(IndexedCollection iColl,String serno,Connection conn) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppMemMana(iColl, serno, conn);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppOverseeAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppOverseeAgr(iColl, serno, conn, context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDesbuyPlan(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppDesbuyPlan(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppBconCoopAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppBconCoopAgr(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDepotAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppDepotAgr(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppPsaleCont(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		return cmisDao.insertIqpAppPsaleCont(iColl, serno, conn,context);
	}
	/**@author lisj
	 * @time 2014年11月18日 
	 * @description 需求:【XD140818051】零售合同评分改造
	 * 1.通过流水号计算零售个人业务合同评分
	 * 2.通过传入参数【流水号】调用客户接口，获取客户所属业务条线
	 * 3.获取零售合同评分配置信息进行评分计算
	 * 4.零售合同评分 = 担保模式得分 + 利率定价得分 + 特殊加分
	 * @throws Exception 
	 */
	public BigDecimal getIntRatPriModeScoreBySerno(String serno) throws Exception {
		BigDecimal intRatPriModeScore = new BigDecimal("0");
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		intRatPriModeScore = cmisDao.getIntRatPriModeScoreBySerno(serno);
		return intRatPriModeScore;
	}

	public BigDecimal getGuarModeScoreBySerno(String serno) throws Exception {
		BigDecimal guarModeScore = new BigDecimal("0");
		IqpLoanAppDao cmisDao = (IqpLoanAppDao)this.getDaoInstance(AppConstant.IQPLOANAPPDAO);
		guarModeScore = cmisDao.getGuarModeScoreBySerno(serno);
		return guarModeScore;
	}

}
