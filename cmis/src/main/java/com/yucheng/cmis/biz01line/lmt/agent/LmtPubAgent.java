package com.yucheng.cmis.biz01line.lmt.agent;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.lmt.dao.LmtPubDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

/**
 * @author TSY
 * @since 1.2
 */
public class LmtPubAgent extends CMISAgent {

	/**
	 * 根据授信协议编号查找授信额度台帐对象
	 * @param lmtSerno 授信协议编号
	 * @return 授信额度台帐对象列表
	 * @throws EMPJDBCException
	 * @throws CMISException
	 */
	public List<CMISDomain> queryLmtAgrDetailsListByLmtSerno(String lmtSerno) throws EMPJDBCException, CMISException {
		TableModelDAO dao = this.getTableModelDAO();
		Connection connection = this.getConnection();
		ComponentHelper componetHelper = new ComponentHelper();
		String openDay = this.getOpenDay();

		String conditionStr = "where agr_no = '" + lmtSerno + "' and to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd')>='"+openDay+"'";        
		IndexedCollection iColl = dao.queryList("LmtAgrDetails",null,conditionStr,connection);
		return componetHelper.icol2domainlist("com.yucheng.cmis.biz01line.lmt.domain.LmtAgrDetails", iColl);
	}

	/**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 */
	public int updateLmtApplyAmt(String serno)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.updateLmtApplyAmt(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 * @param lmt_type 授信类别（区分条线）
	 * @param lrisk_type 是否非低风险
	 */
	public int updateLmtApplyAmt(String serno,String lrisk_type)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.updateLmtApplyAmt(serno,lrisk_type);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新个人授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtAppIndivAmt(String serno)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.updateLmtAppIndivAmt(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新集团授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtGrpAppAmt(String serno)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.updateLmtGrpAppAmt(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据授信申请流水号，删除授信分项中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtApplyDetailsBySerno(String serno)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.deleteLmtApplyDetailsBySerno(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据集团授信申请流水号，删除成员授信申请中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtGrpMemberAppBySerno(String serno)throws AgentException {
		int count=0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.deleteLmtGrpMemberAppBySerno(serno);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据合作方流水号，删除拟按揭设备信息
	 * @param serno 合作方业务流水号
	 */
	public int deleteLmtSchedEquip(String serno)throws AgentException {
		int count = 0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.deleteLmtSchedEquip(serno);
		}catch (Exception e) {
			throw new AgentException("根据合作方机械设备项目编号，删除拟按揭设备信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据授信流水号/协议编号查询循环额度、一次性额度
	 * @param serno 个人授信申请流水号
	 * @param tablename 查询表名
	 */
	public KeyedCollection selectLmtAppIndivAmt(String serno,String tablename)throws AgentException {
		KeyedCollection kColl = null;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			kColl = lmtPubDao.selectLmtAppIndivAmt(serno,tablename);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据授信协议编号查询循环额度、一次性额度
	 * @param agr_no 授信协议号
	 */
	public KeyedCollection selectLmtAgrAmtByAgr(String agr_no)throws AgentException {
		KeyedCollection kColl = null;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			kColl = lmtPubDao.selectLmtAgrAmtByAgr(agr_no);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据授信协议编号查询循环额度、一次性额度
	 * @param serno 个人授信申请流水号
	 * @param lmt_type 授信类别（区分条线）
	 */
	public KeyedCollection selectLmtAgrDetailsAmt(String serno)throws AgentException {
		KeyedCollection kColl = null;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			kColl = lmtPubDao.selectLmtAgrDetailsAmt(serno);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws DaoException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws AgentException {
		int count = 0;
		try {
			LmtPubDao lmtPubDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			count = lmtPubDao.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
	
	/**
	 * 授信变更时将原有台账台账与担保合同关系复制给新的授信申请
	 * @param limit_code 额度编号
	 * @param org_limit_code 原额度编号（台账编号）
	 * @param serno 当前业务流水号
	 */
	public int createRLmtAppGuarContRecord(String limit_code,String org_limit_code,String serno)throws AgentException {
		try {
			LmtPubDao cmisDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			return cmisDao.createRLmtAppGuarContRecord(limit_code, org_limit_code, serno);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 变更保存时将原有授信台账复制到申请分项历史表
	 * @param agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 * @param lrisk_type 低风险业务类型
	 */
	public int createLmtAppDetailsHisRecord(String agr_no,String serno,String lrisk_type)throws AgentException {
		try {
			LmtPubDao cmisDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			return cmisDao.createLmtAppDetailsHisRecord(agr_no, serno, lrisk_type);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 变更保存时将原有授协议账复制到申请历史表
	 * @param grp_agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 */
	public int createLmtApplyRecord(String grp_agr_no,String serno)throws AgentException {
		try {
			LmtPubDao cmisDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			return cmisDao.createLmtApplyRecord(grp_agr_no, serno);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 无返回值命名sql
	 * @param submitType
	 * @param serno
	 * @param LIMIT_CODE
	 */
	public void doVirtualSubmit(String submitType, String serno , String LIMIT_CODE) throws AgentException {
		try {
			LmtPubDao cmisDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			cmisDao.doVirtualSubmit(submitType,serno,LIMIT_CODE);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 有返回值命名sql
	 * @param submitType
	 * @param serno
	 * @param LIMIT_CODE
	 */
	public String getAgrno(String submitType,String serno) throws AgentException {
		try {
			LmtPubDao cmisDao = (LmtPubDao)this.getDaoInstance("LmtPubDao");
			return cmisDao.getAgrno(submitType,serno);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
}
