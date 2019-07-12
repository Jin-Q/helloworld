package com.yucheng.cmis.biz01line.lmt.component;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.lmt.agent.LmtPubAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * @author TSY
 * @since 1.2
 */
public class LmtPubComponent extends CMISComponent {

    /**
     * 根据授信协议编号查找授信额度台帐对象
     * @param lmtSerno 授信协议编号
     * @return 授信额度台帐对象列表
     * @throws CMISException
     * @throws EMPJDBCException
     */
    public List<CMISDomain> queryLmtAgrDetailsListByLmtSerno(String lmtSerno) throws CMISException, EMPJDBCException {
    	LmtPubAgent LmtAgrDetailsAgent = (LmtPubAgent) this.getAgentInstance("LmtPubAgent");
        return LmtAgrDetailsAgent.queryLmtAgrDetailsListByLmtSerno(lmtSerno);
    }

    /**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 */
	public int updateLmtApplyAmt(String serno)throws ComponentException {
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.updateLmtApplyAmt(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 * @param lmt_type 授信类别（区分条线）
	 * @param lrisk_type 是否非低风险
	 */
	public int updateLmtApplyAmt(String serno,String lrisk_type)throws ComponentException {
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.updateLmtApplyAmt(serno,lrisk_type);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	 /**
	 * 根据流水号更新个人授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtAppIndivAmt(String serno)throws ComponentException {
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.updateLmtAppIndivAmt(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新集团授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtGrpAppAmt(String serno)throws ComponentException {
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.updateLmtGrpAppAmt(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据授信申请流水号，删除授信分项中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtApplyDetailsBySerno(String serno)throws ComponentException{
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.deleteLmtApplyDetailsBySerno(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据集团授信申请流水号，删除成员授信申请中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtGrpMemberAppBySerno(String serno)throws ComponentException{
		int count=0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.deleteLmtGrpMemberAppBySerno(serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据合作方流水号，删除拟按揭设备信息
	 * @param serno 合作方业务流水号
	 */
	public int deleteLmtSchedEquip(String serno)throws ComponentException {
		int count = 0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.deleteLmtSchedEquip(serno);
		}catch (Exception e) {
			throw new ComponentException("根据合作方机械设备项目编号，删除拟按揭设备信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据客户码查询客户有效的授信台账
	 * @param cus_id 客户码
	 * @param limit_ind 循环额度/一次性额度
	 * @param lmt_type 授信类型（01-单一法人   02-同业客户   03-合作方）
	 * @param guar_type 担保方式
	 * @param limit_type 额度类型（01-循环额度 02-一次性额度）
	 * @param conditionStr 查询条件字段
	 * @param dataSource 数据源
	 * @param pageInfo 分页信息
	 */
	public IndexedCollection queryLmtAgrDetails(String cus_id,String limit_ind,String lmt_type,String guar_type,String limit_type,String conditionStr,DataSource dataSource,PageInfo pageInfo)throws ComponentException {
		IndexedCollection iColl = new IndexedCollection();
		StringBuffer buf = new StringBuffer("");
		try {
			/**modified by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin**/
			if("01".equals(lmt_type)){  //单一法人（包含联保小组）
				buf.append("SELECT CUS_ID,AGR_NO,SUB_TYPE,LIMIT_CODE,NVL((SELECT PRDNAME FROM PRD_BASICINFO WHERE PRDID = LIMIT_NAME),LIMIT_NAME) LIMIT_NAME,");
				buf.append("LIMIT_TYPE,GUAR_TYPE,CUR_TYPE,CRD_AMT TOTAL_CRD_AMT,ENABLE_AMT-NVL(FROZE_AMT,0.00) CRD_AMT,START_DATE,END_DATE,LMT_STATUS,PRD_ID,CORE_CORP_CUS_ID ");
				buf.append("FROM LMT_AGR_DETAILS WHERE LMT_STATUS='10' AND SUB_TYPE IN('01','03','05') AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ");
				buf.append("AND CUS_ID='"+cus_id+"' AND GUAR_TYPE='"+guar_type+"' AND LIMIT_TYPE='"+limit_type+"'"+conditionStr);
				/**modified by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）end**/
			}
			if("02".equals(lmt_type)){  //同业客户
				buf.append("SELECT CUS_ID,AGR_NO,CUR_TYPE,LMT_AMT,LMT_AMT ENABLE_AMT,START_DATE,END_DATE,LMT_STATUS,'' PRD_ID ");
				buf.append("FROM LMT_INTBANK_ACC WHERE LMT_STATUS='10' AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO) ");
				buf.append("AND CUS_ID=(SELECT CUS_ID FROM CUS_SAME_ORG WHERE SAME_ORG_NO='"+cus_id+"')");
				buf.append("AND LIMIT_TYPE='"+limit_ind+"'"+conditionStr);
			}
			
			iColl = TableModelUtil.buildPageData(pageInfo,dataSource, buf.toString());
		}catch (Exception e) {
			throw new ComponentException("根据客户码查询客户有效授信台账出错，错误原因："+e.getMessage());
		}
		return iColl;
	}
	
	
	/**
	 * 根据查询类型查询第三方额度信息
	 * @param cus_id 客户码
	 * @param select_type 查询类型（1-合作方 2-圈商 3-行业 4-商票贴现 5-同业）
	 * @param guar_type 担保方式
	 * @param conditionStr 查询条件字段
	 * @param dataSource 数据源
	 * @param pageInfo 分页信息
	 */
	public IndexedCollection queryThirdPartyLmtInfo(String cus_id,String select_type,String guar_type,String conditionStr,DataSource dataSource,PageInfo pageInfo)throws ComponentException {
		IndexedCollection iColl = new IndexedCollection();
		StringBuffer buf = new StringBuffer("");
		try {
			if("2".equals(select_type)){  //圈商
				//conditionStr = conditionStr.replace("AGR_NO", "BIZ_AREA_NO");
				buf.append("SELECT BIZ_AREA_TYPE SUB_TYPE,BIZ_AREA_NAME,AGR_NO,CUR_TYPE,LMT_TOTL_AMT CRD_AMT,SINGLE_MAX_AMT SINGLE_AMT,START_DATE,END_DATE FROM LMT_AGR_BIZ_AREA WHERE AGR_STATUS='002' "+conditionStr);
				//通过名单的流水号查询入/退圈申请表中关联的商圈编号（BIZ_AREA_NO）
				buf.append(" AND AGR_NO IN(SELECT AGR_NO FROM LMT_NAME_LIST WHERE CUS_STATUS='1' AND SUB_TYPE='02' AND CUS_ID='"+cus_id+"') AND INSTR(GUAR_TYPE,'"+guar_type+"')>0 ");//分项类型为圈商、客户状态为入圈
				buf.append("AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO)"); //到期日期之前使用
			}else if("3".equals(select_type)){  //行业授信
				//行业授信如果是名单制管理需要通过名单过滤，如果是非名单制行业授信全行共享
				buf.append("SELECT INDUS_TYPE SUB_TYPE,AGR_NO,CUR_TYPE,INDUS_AMT CRD_AMT,SINGLE_AMT,START_DATE,END_DATE,SUIT_PRD PRD_ID FROM LMT_INDUS_AGR WHERE IS_LIST_MANA='1' AND AGR_STATUS = '002' "+conditionStr);
				buf.append(" AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO)"); //到期日期之前使用
				buf.append(" AND AGR_NO IN(SELECT AGR_NO FROM LMT_INDUS_LIST_MANA WHERE CUS_ID='"+cus_id+"')");
				buf.append(" UNION ALL ");
				//非名单制行业授信全行共享
				buf.append(" SELECT INDUS_TYPE SUB_TYPE,AGR_NO,CUR_TYPE,INDUS_AMT CRD_AMT,SINGLE_AMT,START_DATE,END_DATE,SUIT_PRD PRD_ID FROM LMT_INDUS_AGR WHERE IS_LIST_MANA='2' AND AGR_STATUS = '002' "+conditionStr);
				buf.append(" AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO)"); //到期日期之前使用
			}else if("4".equals(select_type)){  //第三方需要包含商票保贴额度
				buf.append("SELECT CUS_ID,AGR_NO,SUB_TYPE,LIMIT_CODE,LIMIT_TYPE,GUAR_TYPE,CUR_TYPE,ENABLE_AMT-NVL(FROZE_AMT,0.00) CRD_AMT,START_DATE,END_DATE,PRD_ID ");
				buf.append("FROM LMT_AGR_DETAILS WHERE LMT_STATUS='10' AND SUB_TYPE IN('01','05') AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO) ");
				buf.append("AND INSTR(PRD_ID,'300020')>0 "+conditionStr+"");
			}else if("5".equals(select_type)){  //同业客户
				buf.append("SELECT CUS_ID,AGR_NO,CUR_TYPE,LMT_AMT,LMT_AMT ENABLE_AMT,START_DATE,END_DATE,LMT_STATUS,'' PRD_ID ");
				buf.append("FROM LMT_INTBANK_ACC WHERE LMT_STATUS='10' AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO) "+conditionStr);
				//buf.append("AND CUS_ID=(SELECT CUS_ID FROM CUS_SAME_ORG WHERE SAME_ORG_NO='"+cus_id+"')");
			}else{
				//剔除联保小组
				buf.append("SELECT CUS_ID,COOP_TYPE SUB_TYPE ,AGR_NO,CUR_TYPE,LMT_TOTL_AMT CRD_AMT,SINGLE_MAX_AMT SINGLE_AMT,START_DATE,END_DATE FROM LMT_AGR_JOINT_COOP ");
				buf.append("WHERE COOP_TYPE<>'010' AND AGR_STATUS = '002' "+conditionStr);
				buf.append("AND END_DATE >= (SELECT OPENDAY FROM PUB_SYS_INFO)"); //到期日期之前使用
			}
			
			iColl = TableModelUtil.buildPageData(pageInfo,dataSource, buf.toString());
		}catch (Exception e) {
			throw new ComponentException("查询第三方额度信息错误，错误描述："+e.getMessage());
		}
		return iColl;
	}
	
	/**
	 * 查询授信复议申请信息（对公、个人）
	 * @param conditionStr 查询条件字段
	 * @param dataSource 数据源
	 * @param pageInfo 分页信息
	 */
	public IndexedCollection queryLmtRediInfo(String conditionStr,String type,DataSource dataSource,PageInfo pageInfo)throws ComponentException {
		IndexedCollection iColl = new IndexedCollection();
		StringBuffer buf = new StringBuffer("");
		try {
			if("indiv".equals(type)){
				buf.append("SELECT RES.* FROM (SELECT 'INDIV' LX,SERNO,APP_TYPE,CUS_ID,CUR_TYPE,CRD_TOTL_AMT,APP_DATE,INPUT_ID,INPUT_BR_ID,APPROVE_STATUS,LRISK_TYPE,MANAGER_ID,MANAGER_BR_ID FROM LMT_APP_INDIV_REDI) RES");
			}else{
				buf.append("SELECT RES.* FROM (SELECT 'COM' LX,SERNO,APP_TYPE,CUS_ID,CUR_TYPE,CRD_TOTL_AMT,APP_DATE,INPUT_ID,INPUT_BR_ID,APPROVE_STATUS,LRISK_TYPE,MANAGER_ID,MANAGER_BR_ID FROM LMT_REDI_APPLY) RES ");				
			}
			buf.append(conditionStr);
			
			iColl = TableModelUtil.buildPageData(pageInfo,dataSource, buf.toString());
		}catch (Exception e) {
			throw new ComponentException("查询授信复议申请信息出错，错误原因："+e.getMessage());
		}
		return iColl;
	}
	
	/**
	 * 根据授信流水号/协议编号查询循环额度、一次性额度
	 * @param serno 个人授信申请流水号
	 * @param tablename 查询表名
	 */
	public KeyedCollection selectLmtAppIndivAmt(String serno,String tablename)throws ComponentException {
		KeyedCollection kColl = null;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			kColl = lmtPubAgent.selectLmtAppIndivAmt(serno,tablename);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据授信协议编号查询循环额度、一次性额度
	 * @param agr_no 授信协议号
	 */
	public KeyedCollection selectLmtAgrAmtByAgr(String agr_no)throws ComponentException {
		KeyedCollection kColl = null;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			kColl = lmtPubAgent.selectLmtAgrAmtByAgr(agr_no);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 *根据授信协议编号查询循环额度、一次性额度
	 * @param serno 个人授信申请流水号
	 * @param lmt_type 授信类别（区分条线）
	 */
	public KeyedCollection selectLmtAgrDetailsAmt(String serno)throws ComponentException {
		KeyedCollection kColl = null;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			kColl = lmtPubAgent.selectLmtAgrDetailsAmt(serno);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws AgentException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields ) throws ComponentException {
		int count = 0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 授信变更时将原有台账台账与担保合同关系复制给新的授信申请
	 * @param limit_code 额度编号
	 * @param org_limit_code 原额度编号（台账编号）
	 * @param serno 当前业务流水号
	 */
	public int createRLmtAppGuarContRecord(String limit_code,String org_limit_code,String serno)throws ComponentException {
		int count = 0;
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			count = lmtPubAgent.createRLmtAppGuarContRecord(limit_code, org_limit_code, serno);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	
	/**
	 * 变更保存时将原有授信台账复制到申请分项历史表
	 * @param agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 * @param lrisk_type 低风险业务类型
	 */
	public int createLmtAppDetailsHisRecord(String agr_no,String serno,String lrisk_type)throws ComponentException {
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			return lmtPubAgent.createLmtAppDetailsHisRecord(agr_no, serno, lrisk_type);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 变更保存时将原有授信台账复制到申请分项表
	 * @param agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 * @param lrisk_type 低风险业务类型
	 */
	public int createLmtAppDetailsRecord(String agr_no,String serno,String lrisk_type)throws ComponentException {
		int ret = -1;
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = this.getTableModelDAO();
			String openDay = this.getContext().getDataValue("OPENDAY").toString();
			IndexedCollection agr_details = dao.queryList("LmtAgrDetails", " WHERE AGR_NO='"+agr_no+"' AND SUB_TYPE IN('01','05') AND LMT_STATUS in ('10','40') " +
					//" AND LRISK_TYPE = '"+lrisk_type+"' AND LMT_TYPE='"+lmt_type+"'" +  //复制授信时加授信类别
					" AND LRISK_TYPE = '"+lrisk_type+"'" + 
					" AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= '"+openDay+"' ", connection);
			for (Iterator iterator = agr_details.iterator(); iterator.hasNext();) {
				KeyedCollection kColl = (KeyedCollection) iterator.next();
				//把台账数据过渡到申请分项表中
				KeyedCollection kColl4App = new KeyedCollection("LmtAppDetails");
				kColl4App.addDataField("serno",serno);
				kColl4App.addDataField("cus_id",kColl.getDataValue("cus_id"));
				kColl4App.addDataField("core_corp_cus_id",kColl.getDataValue("core_corp_cus_id"));  //核心企业客户码
				kColl4App.addDataField("core_corp_duty",kColl.getDataValue("core_corp_duty"));  //核心企业责任
				kColl4App.addDataField("org_limit_code",kColl.getDataValue("limit_code"));
				kColl4App.addDataField("sub_type",kColl.getDataValue("sub_type"));
				kColl4App.addDataField("limit_type",kColl.getDataValue("limit_type"));
				kColl4App.addDataField("limit_name",kColl.getDataValue("limit_name"));
				kColl4App.addDataField("prd_id",kColl.getDataValue("prd_id"));
				kColl4App.addDataField("cur_type",kColl.getDataValue("cur_type"));
				kColl4App.addDataField("crd_amt",kColl.getDataValue("crd_amt"));
				kColl4App.addDataField("guar_type",kColl.getDataValue("guar_type"));
				kColl4App.addDataField("term_type",kColl.getDataValue("term_type"));
				kColl4App.addDataField("term",kColl.getDataValue("term"));
				kColl4App.addDataField("is_adj_term",kColl.getDataValue("is_adj_term"));
				kColl4App.addDataField("is_pre_crd",kColl.getDataValue("is_pre_crd"));
				kColl4App.addDataField("start_date",kColl.getDataValue("start_date"));
				kColl4App.addDataField("end_date",kColl.getDataValue("end_date"));
				kColl4App.addDataField("lrisk_type",kColl.getDataValue("lrisk_type"));
				//if("03".equals(kColl.getDataValue("lmt_type"))){  //供应链授信
				//	kColl4App.addDataField("lmt_type",lmt_type);   //授信类别（区分条线用）
				//}else{
				//	kColl4App.addDataField("lmt_type",kColl.getDataValue("lmt_type"));   //授信类别（区分条线用）
				//}
				kColl4App.addDataField("green_indus",kColl.getDataValue("green_indus"));   //绿色信贷类型
				kColl4App.addDataField("green_pro_type",kColl.getDataValue("green_pro_type"));   //绿色授信项目类型
				kColl4App.addDataField("froze_amt",kColl.getDataValue("froze_amt"));   //冻结金额
				kColl4App.addDataField("update_flag","02");
				
				String limit_Code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all",connection, this.getContext());
				
				kColl4App.addDataField("limit_code",limit_Code);
				ret += dao.insert(kColl4App, connection);
				
				//分项变更时将担保合同与授信台账的关系也变更过去
				this.createRLmtAppGuarContRecord(limit_Code, (String)kColl.getDataValue("limit_code"), serno);
			}
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * 变更保存时将原有授信协议复制到申请历史表
	 * @param grp_agr_no 集团授信协议编号
	 * @param serno 变更业务流水号
	 */
	public int createLmtApplyRecord(String grp_agr_no,String serno)throws ComponentException {
		try {
			LmtPubAgent lmtPubAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			return lmtPubAgent.createLmtApplyRecord(grp_agr_no, serno);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
	
	/**
	 * 无返回值命名sql
	 * @param submitType
	 * @param serno
	 * @param LIMIT_CODE
	 */
	public void doVirtualSubmit(String submitType, String serno , String LIMIT_CODE) throws ComponentException {
		try {
			LmtPubAgent cmisAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			cmisAgent.doVirtualSubmit(submitType,serno,LIMIT_CODE);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		
	}
	
	/**
	 * 有返回值命名sql
	 * @param submitType
	 * @param serno
	 * @param LIMIT_CODE
	 */
	public String getAgrno(String submitType,String serno) throws ComponentException {
		try {
			LmtPubAgent cmisAgent = (LmtPubAgent)this.getAgentInstance("LmtPubAgent");
			return cmisAgent.getAgrno(submitType,serno);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}		
	}
	
}
