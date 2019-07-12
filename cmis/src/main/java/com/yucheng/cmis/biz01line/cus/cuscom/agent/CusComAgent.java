package com.yucheng.cmis.biz01line.cus.cuscom.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.dao.CusComDao;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.biz01line.cus.cussameorg.domain.CusSameOrg;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
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

public class CusComAgent extends CMISAgent {

	/**
	 * 根据客户ID查找其基本信息
	 * 
	 * @param CusId 客户ID
	 * @return 客户基本信息
	 * @throws EMPException
	 */
	public CusCom findCusComByCusId(String cusId) throws EMPException {
		CusCom cusCom = new CusCom();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		cusCom = (CusCom) this.findCMISDomainByKeywords(cusCom,PUBConstant.CUSCOM, pk_values);

		return cusCom;
	}
	/**
	 * 根据客户ID查找其基本信息
	 * 
	 * @param CusId 客户ID
	 * @return 客户基本信息
	 * @throws EMPException
	 */
	public CusSameOrg findCusSameOrgByCusId(String cusId) throws EMPException {
		CusSameOrg cusSameOrg = new CusSameOrg();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		cusSameOrg = (CusSameOrg) this.findCMISDomainByKeywords(cusSameOrg,PUBConstant.CUSSAMEORG, pk_values);

		return cusSameOrg;
	}

	/**
	 * 根据内部客户码，更新对公客户的集团客户类型和集团编号
	 * 
	 * @param GrpMode
	 *            集团客户类型
	 * @param GrpNo
	 *            集团编号
	 * @param innerCusId
	 *            客户代码
	 * @throws ComponentException
	 */
	public String updateGrpModeAndNo(String GrpMode, String GrpNo,
			String CusId) throws ComponentException {
		Connection conn = null;
		conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		int intReturnMessage = cusComDao.updateGrpModeAndNo(GrpMode, GrpNo,CusId, conn);
		if (intReturnMessage != 1) {
			throw new ComponentException();
		}
		return null;
	}
	
	/**
	 * 根据内部客户码，更新对公客户的集团客户类型和集团编号
	 * 
	 * @param GrpMode
	 *            集团客户类型
	 * @param GrpNo
	 *            集团编号
	 * @param innerCusId
	 *            内部客户代码
	 * @throws ComponentException
	 */
	public String updateGrpJiesanModeAndNo(String GrpMode, String GrpNo) throws ComponentException {
		Connection conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		int intReturnMessage = cusComDao.updateGrpJiesanModeAndNo(GrpMode, GrpNo, conn);
		if (intReturnMessage != 1) {
			throw new ComponentException();
		}
		return null;
	}

	/**
	 * 根据贷款卡号检查是否已经在系统中存在
	 * 
	 * @param loanCardId
	 * @return y 已经存在　ｎ　不存在
	 * @throws ComponentException
	 */
	public String checkLoanCardIdExist(String loanCardId,String cusNo)
			throws ComponentException {
		Connection conn = null;
		conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		return cusComDao.checkLoanCardIdExist(loanCardId,cusNo ,conn);
	}

	/**
	 * 
	 * 根据证件类型，证件号码检查其在系统中是否已经存在
	 * @param certCode 证件号码
	 * @param certType 证件类型
	 * @return
	 * @throws ComponentException
	 */
	public String checkCertCodeExist(String certCode, String certType,
			String cusType) throws ComponentException {
		Connection conn = null;
		conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		return cusComDao.checkCertCodeExist(certCode, certType, conn);
	}

	/**
	 * 根据帐号判断基本存款账户和一般结算账户是否在系统中存在
	 * 
	 * @param accNo
	 * @param accType
	 * @return
	 * @throws ComponentException
	 */
	public String AccExist(String accNo, String accType ,String cusNo)
			throws ComponentException {
		Connection conn = null;
		conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		return cusComDao.AccExist(accNo, accType,cusNo, conn);
	}
	/**
	 * 
	 * @param grpNo
	 * @return
	 * @throws ComponentException
	 */
	public int deleteGrpMemberByGrpNo(String grpNo)throws ComponentException {
		Connection conn = null;
		conn = this.getConnection();
//		CusComDao cusComDao = new CusComDao();
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		return cusComDao.deleteGrpMemberByGrpNo(grpNo, conn);
	}
	
	/**
	 * 根据CUS_ID获取最新财务报表数据
	 * */
	public String getMaxFncStatBaseByCusId(String cusId) throws ComponentException {
		String sql  = "select max(stat_prd) as stat_prd  from fnc_stat_base where cus_id='"+cusId+"' ";
		HashMap<String,String> hm = this.queryDataOfMapByCondition(sql);
		String stat_prd = hm.get("stat_prd");

		return stat_prd;
	}
	/**
	 * 根据关键人客户编号查询所在企业信息中的法人代表
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId) throws ComponentException {
		List<CMISDomain> list = new ArrayList<CMISDomain>();
		list = this.findListByCondition(CusComManager.class, " where cus_id = '"+cusId+"' and com_mrg_typ = '02' ");
		return list;
	}
	/**
	 * 根据关键人客户编号查询所在企业信息中的实际控制人
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusControlListByCusId(String cusId) throws ComponentException {
		List<CMISDomain> list = new ArrayList<CMISDomain>();
		list = this.findListByCondition(CusComManager.class, " where cus_id = '"+cusId+"' and com_mrg_typ = '01' ");
		return list;
	}
	
	/**
	 * 查找上年末的财务报表
	 * @param cusId	客户编号
	 * @param serno	业务流水号
	 * @return 1--成功  0--失败
	 * @throws ComponenetException
	 */
	public String queryFncStat(String cusId,String year) throws Exception{
		TableModelDAO tmd = this.getTableModelDAO();
		IndexedCollection iColl = new IndexedCollection();
		try {
			//											报表开始时间								客户码 				标志的 第9个为2
			 iColl = tmd.queryList("FncStatBase", " where stat_prd ='"+year+"' and cus_id = '"+cusId+"' and substr(state_flg,9,1) = '2'", this.getConnection());
		} catch (EMPJDBCException e) {
			throw e;
		}
		if(iColl.size()==0){
			return "0";
		}else{
			return "1";
		}
	}
	
	/**
	 * <p>
	 * 执行查询sql
	 * </p>
	 */
	public String querySql(String sql)throws ComponentException {
		CusComDao cusComDao = (CusComDao)this.getDaoInstance("CusCom");
		return cusComDao.querySql( sql);
	}

}
