package com.yucheng.cmis.biz01line.iqp.component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.biz01line.pvp.component.PvpBizFlowComponent;
import com.yucheng.cmis.biz01line.pvp.msi.PvpServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;


public class IqpBizFlowComponent extends CMISComponent {
	
	private static final String TOMODEL = "CtrLoanCont";//目标表模型
	private static final String AUTHORIZESUBMODEL = "PvpAuthorizeSub";//授权信息从表
	private static final String IQPCUSACCT= "IqpCusAcct";//账户信息表
	private static final String CTRCONTMODEL = "CtrLoanCont";//合同
	private static final String IQPFEE= "IqpAppendTerms";//账户信息表
	private static final String PrdRepayMode= "PrdRepayMode";//还款方式
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	/**
	 * 普通贷款申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpLoan(String serno,String bizType)throws ComponentException {
		KeyedCollection mainKColl = null;
		KeyedCollection subKColl = null;
		IqpLoanAppComponent iqpLoanAppComponent = null;
		TableModelDAO dao = null;
		String cont_no = "";
		try {
			dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;//普通贷款
			String cont_status = "100";
			mainKColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
			subKColl = dao.queryDetail("IqpLoanAppSub", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			mainKColl.addKeyedCollection(subKColl);
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			/** ----------计算合同到期日期start--------- */
//			String apply_date = (String)mainKColl.getDataValue("apply_date");
//			String termType = (String)subKColl.getDataValue("term_type");
//			String term = (String)subKColl.getDataValue("apply_term");
//			String type = "";
//			if("001".equals(termType)){
//				type = "Y";
//			}else if("002".equals(termType)){
//				type = "M";
//			}else if("003".equals(termType)){
//				type = "D";
//			}
//			toDefKColl.addDataField("cont_end_date",DateUtils.getAddDate(type, apply_date, Integer.parseInt(term)));//合同到期日
			/** ----------计算合同到期日期end--------- */
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, TOMODEL, this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
			/**添加授信关系*/
			iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
			
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
		/** 合同生成时实时生成合同评分。原因：出账队列放合同签订之前处理，出账队列通过后在生成待签订合同  2014-10-09 唐顺岩    ***/
		try{
			this.getConnection().commit();   //计算合同得分时 将原有流程事务提交，
			KeyedCollection cont_kcoll = new KeyedCollection();
			cont_kcoll.setName("CtrLoanCont");
			/**modified by lisj 2014年11月18日  需求:【XD140818051】个人业务合同评分配置改造 begin **/
			String prd_id = (String) mainKColl.getDataValue("prd_id");
			String prdLine ="";
			KeyedCollection prdBaseInfoKColl = dao.queryDetail("PrdBasicinfo", prd_id, this.getConnection());
			if(prdBaseInfoKColl !=null && prdBaseInfoKColl.size() >0){
				prdLine = (String) prdBaseInfoKColl.getDataValue("prdowner");
			}	
		 if(!"BL300".equals(prdLine)){
			if(subKColl!=null){
				String is_close_loan = (String) subKColl.getDataValue("is_close_loan");
				if(!"200024".equals(prd_id)&&!"500032".equals(prd_id)&&!"400020".equals(prd_id)&&!"400024".equals(prd_id)&&!"400023".equals(prd_id)&&!"400022".equals(prd_id)
						&&!"700021".equals(prd_id)&&!"700020".equals(prd_id)&&!"400021".equals(prd_id)&&!"100063".equals(prd_id)&&!"300020".equals(prd_id)
						&&!"300021".equals(prd_id)&&!"300023".equals(prd_id)&&!"600020".equals(prd_id)&&!"300024".equals(prd_id)&&!"300022".equals(prd_id)&&"2".equals(is_close_loan)){
					BigDecimal cont_number = iqpLoanAppComponent.getContNumberByRuleSet(cont_no);
					cont_kcoll.put("cont_number", cont_number);
					cont_kcoll.put("cont_no", cont_no);
					dao.update(cont_kcoll, this.getConnection());
				}
			  }
			}else{
				  BigDecimal guarModeScore = iqpLoanAppComponent.getGuarModeScoreBySerno(serno);//担保模式得分
				  BigDecimal intRatPriModeScore = iqpLoanAppComponent.getIntRatPriModeScoreBySerno(serno);//利率定价模式得分
				  if(intRatPriModeScore==null){
					  intRatPriModeScore = new BigDecimal(0);
				  }
				  BigDecimal specAddScore =new BigDecimal(0);//特殊加分
				  BigDecimal cont_number = new BigDecimal(0);//合同评分
				  String is_close_loan="";
				  //处理特殊加分
				  if(subKColl !=null){
					  is_close_loan = (String) subKColl.getDataValue("is_close_loan");//无间贷标志 1,是 2,否
				  }
				  if("100029".equals(prd_id)){
					  specAddScore = specAddScore.add(new BigDecimal(1));
				  }else if("100034".equals(prd_id)){
					  specAddScore = specAddScore.add(new BigDecimal(0.5));
				  }else if("100035".equals(prd_id)){
					  specAddScore = specAddScore.add(new BigDecimal(1));
				  }else if("100037".equals(prd_id)){
					  specAddScore = specAddScore.add(new BigDecimal(1));
				  }else if("1".equals(is_close_loan)){
					  specAddScore = specAddScore.add(new BigDecimal(0.3));
				  }			  
				  cont_number= cont_number.add(guarModeScore).add(intRatPriModeScore).add(specAddScore).setScale(2, BigDecimal.ROUND_HALF_UP);
				  cont_kcoll.put("cont_number", cont_number);
				  cont_kcoll.put("cont_no", cont_no);
				  dao.update(cont_kcoll, this.getConnection());
			}
			/**modified by lisj 2014年11月18日  需求:【XD140818051】个人业务合同评分配置改造 end **/
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("业务申请流程在生成合同评分时异常结束，错误原因："+e.getMessage());
		}
		/** 合同评分在合同生成时即生成。原因：出账队列放合同签订之前处理，出账队列通过后在生成待签订合同  END ***/	
	}
	
	/**
	 * 银承贷款申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAccp(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
			KeyedCollection subKColl = dao.queryDetail("IqpLoanAppSub", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			mainKColl.addKeyedCollection(subKColl);
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			/** ----------计算合同到期日期start--------- */
//			String apply_date = (String)mainKColl.getDataValue("apply_date");
//			String termType = (String)subKColl.getDataValue("term_type");
//			String term = (String)subKColl.getDataValue("apply_term");
//			String type = "";
//			if("001".equals(termType)){
//				type = "Y";
//			}else if("002".equals(termType)){
//				type = "M";
//			}else if("003".equals(termType)){
//				type = "D";
//			}
//			toDefKColl.addDataField("cont_end_date",DateUtils.getAddDate(type, apply_date, Integer.parseInt(term)));//合同到期日
			/** ----------计算合同到期日期end--------- */
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, TOMODEL, this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
			/**添加授信关系*/
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 贴现贷款申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpDisc(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;//贴现
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
			KeyedCollection subKColl = dao.queryDetail("IqpDiscApp", serno, this.getConnection());//贴现自己的从表
			String prdId = (String) mainKColl.getDataValue("prd_id");
			mainKColl.addKeyedCollection(subKColl);
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, TOMODEL, this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
			/** 贴现需要更新票据批次表，返写合同号 */
			String condition = " where serno = '"+serno+"'";
			KeyedCollection batchKColl = dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			batchKColl.setDataValue("cont_no", cont_no);
			dao.update(batchKColl, this.getConnection());
			
			/**添加授信关系*/
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 展期申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpExtend(String serno,String bizType)throws ComponentException {
		
		try {
			String bizTypeNo = "2";//展期
			/**
			 * 参考普通贷款
			 * */
			throw new ComponentException("未配置流程后处理！");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 转帖现申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpRpddscnt(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpRpddscnt", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			String rpddscnt_type = (String) mainKColl.getDataValue("rpddscnt_type");
			//根据不同的转贴现类型，生成不同的业务号
			if(rpddscnt_type.equals("01")||rpddscnt_type.equals("02")){//买入返售、买入买断
				bizTypeNo = AppConstant.IQPCONTTYPE4ZR;//转贴现转入
			}else if(rpddscnt_type.equals("03")||rpddscnt_type.equals("04")||rpddscnt_type.equals("06")){
				bizTypeNo = AppConstant.IQPCONTTYPE4ZC;//转贴现转出
			}else if(rpddscnt_type.equals("05")){
				bizTypeNo = AppConstant.IQPCONTTYPE4NZ;//内转
			}
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, "CtrRpddscntCont", this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
			/** 贴现需要更新票据批次表，返写合同号 */
			String condition = " where serno = '"+serno+"'";
			KeyedCollection batchKColl = dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			batchKColl.setDataValue("cont_no", cont_no);
			dao.update(batchKColl, this.getConnection());
			
			/**添加授信关系*/
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 保函申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpGuarant(String serno,String bizType)throws ComponentException {
		
		try {
			//保函
			this.doWfAgreeForIqpLoan(serno, bizType);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 委托贷款申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpCsgnLoan(String serno, String bizType)throws ComponentException {
		
		try {
			String bizTypeNo = "W";//委托贷款委托合同100063
			this.doWfAgreeForIqpLoan(serno, bizType);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 国结贷款申请流程审批通过，判断是否使用额度合同，
	 * 如果使用额度合同则自动生成合同、出账、台帐、授权信息等。
	 * 非额度合同下沿用贷款流程
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfLoan(String serno,Context context,Connection connection) throws ComponentException {
//		String modelCollection ="";
		String userFlag = "false";//是否使用额度合同,默认不使用额度合同
		String bizTypeNo = AppConstant.IQPCONTTYPE4GJ;//国结
		String cont_status = "100";//直接生效
		String pvp_status = "000";//出账状态
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection mainKColl = dao.queryDetail("IqpLoanApp", serno, connection);
			KeyedCollection subKColl = dao.queryDetail("IqpLoanAppSub", serno, connection);
			String prdId = (String) mainKColl.getDataValue("prd_id");
			String apply_amount = (String) mainKColl.getDataValue("apply_amount");
			mainKColl.addKeyedCollection(subKColl);
			
			/** 1.判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			/** 判断是否存在贸易融资相关从表，验证从表中额度合同是否使用 */ 

			String isLimitContPay = (String)mainKColl.getDataValue("is_limit_cont_pay");
			String limit_cont_no = null;
			if(isLimitContPay != null && isLimitContPay.equals("1")){//使用额度合同
				userFlag = "true";
				cont_status = "200";
				pvp_status = "997";//-----------------------出账审批状态字段预留--------------------
				limit_cont_no = (String)mainKColl.getDataValue("limit_cont_no");
			}
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, connection, context);
			/**2.封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			/** ----------2.1.计算合同到期日期start--------- */
			String apply_date = (String)context.getDataValue("OPENDAY");
			String termType = (String)subKColl.getDataValue("term_type");
			String term = (String)subKColl.getDataValue("apply_term");
			String type = "";
			if("001".equals(termType)){
				type = "Y";
			}else if("002".equals(termType)){
				type = "M";
			}else if("003".equals(termType)){
				type = "D";
			}
			String cont_end_date = DateUtils.getAddDate(type, apply_date, Integer.parseInt(term));
			toDefKColl.addDataField("cont_start_date",context.getDataValue("OPENDAY"));//合同起始日期
			toDefKColl.addDataField("cont_end_date",cont_end_date);//合同到期日
			
			//使用额度合同的直接生成出账信息，所以合同签订日期默认为当前日期
			if("true".equals(userFlag)){
				toDefKColl.addDataField("ser_date", context.getDataValue("OPENDAY"));
			}
			
/** 计算合同余额，计算额度合同下所关联的借据余额（待定，额度合同模块尚未完成） */
			
			
			/** ----------计算合同到期日期end--------- */
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			/** 2.2.生成合同信息，需要更新合同余额 */
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, TOMODEL, context, connection);
			/** 需要更新合同中文合同编号 */
			String cont_cn = null;
			if(limit_cont_no != null && !"".equals(limit_cont_no)){
				KeyedCollection ctrLimtContKColl = dao.queryDetail("CtrLimitCont", limit_cont_no, connection);
				cont_cn = (String)ctrLimtContKColl.getDataValue("cont_cn");
				KeyedCollection kCollCont = dao.queryDetail("CtrLoanCont", cont_no, connection);
				kCollCont.put("cn_cont_no", cont_cn);
				//kCollCont.put("cont_balance", 0);//合同余额
				dao.update(kCollCont, connection);
			}
			
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 通过申请流水号判断是否存在特殊表需要更新数据 */
			this.updateSpecialModel(serno, cont_no, dao, connection);
			
			/** 3.对于贸易融资产品(置用情况下)，需要配置出账映射，即需要一次性生成合同记录和出账记录,台帐信息、授权信息 */
			if("true".equals(userFlag)){
				KeyedCollection pvpDefKColl = new KeyedCollection();
				String pvpSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",  connection, context);
				if(pvpSerno == null || pvpSerno.trim().length() == 0){
					throw new EMPException("生成出账流水号失败！");
				}
				pvpDefKColl.addDataField("serno", pvpSerno);//业务编号
				pvpDefKColl.addDataField("cont_no", cont_no);//合同编号
				pvpDefKColl.addDataField("cont_amt", apply_amount);//合同金额
				pvpDefKColl.addDataField("cont_balance", "0");//合同余额
				pvpDefKColl.addDataField("approve_status", pvp_status);//出账状态
				PvpServiceInterface pvpService = (PvpServiceInterface)serviceJndi.getModualServiceById("pvpServices", "pvp");
				String billNo = pvpService.getBillNoByContNo(cont_no, context, connection);
				pvpDefKColl.addDataField("bill_no", billNo);//借据编号
				/** 3.1.生成出账信息 */
				KeyedCollection pvpKColl = service.insertMsgByKModelFromPrdMap(prdId, "pvp", mainKColl, pvpDefKColl, "PvpLoanApp", context, connection);
				String pvpFlag = (String)pvpKColl.getDataValue("code");
				if(!pvpFlag.equals("success")){
					throw new EMPException("生成出账信息失败！请检查产品配置是否正确！");
				}
			/*********************************************授权信息直接调用出账统一处理START************************************************/
				PvpBizFlowComponent pvpBizFlowComponent = (PvpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance("PvpBizFlowComponent", this.getContext(), this.getConnection());
                if(prdId.equals("500027")){//远期信用证项下汇票贴现走贴现交易
                	pvpBizFlowComponent.doWfAgreeForIqpTfLoanForYQXYZTX(pvpSerno);
                	pvpBizFlowComponent.deductContBalance(pvpSerno);
                	pvpBizFlowComponent.updateContDate(pvpSerno);
				}else if(prdId.equals("500028")){//延期信用证项下应收款买入
                	pvpBizFlowComponent.doWfAgreeForIqpTfLoanForYQXYZMR(pvpSerno);
                	pvpBizFlowComponent.deductContBalance(pvpSerno);
                	pvpBizFlowComponent.updateContDate(pvpSerno);
				}else if(prdId.equals("500029")){//福费廷
                	pvpBizFlowComponent.doWfAgreeForIqpTfLoanForFft(pvpSerno);
                	pvpBizFlowComponent.deductContBalance(pvpSerno);
                	pvpBizFlowComponent.updateContDate(pvpSerno);
				}else if(prdId.equals("500032") || prdId.equals("400020") || prdId.equals("700020") || prdId.equals("700021")){
                	pvpBizFlowComponent.doWfAgreeForIqpTfBw(pvpSerno);
                	pvpBizFlowComponent.deductContBalance(pvpSerno);
                	pvpBizFlowComponent.updateContDate(pvpSerno);
                }else{
                	pvpBizFlowComponent.doWfAgreeForIqpTfLoan(pvpSerno);
                	pvpBizFlowComponent.deductContBalance(pvpSerno);
                	pvpBizFlowComponent.updateContDate(pvpSerno);
                }
            /*********************************************授权信息直接调用出账统一处理END************************************************/
			}
			
			 /**添加授信关系*/
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 资产转受让申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAsset(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpAssetstrsf", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			String takeover_type = (String) mainKColl.getDataValue("takeover_type");//转让方式
			//modified by lcj 20140217 根据老信贷资产转受让合同编号生成规则，应按普通贷款处理即可
//			//根据不同的转让方式，生成不同的业务号
//			if(takeover_type.equals("01")||takeover_type.equals("02")){//转出
//				bizTypeNo = "6";
//			}else if(takeover_type.equals("03")||takeover_type.equals("04")){//转入
//				bizTypeNo = "5";
//			}
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, "CtrAssetstrsfCont", this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
			/**添加授信关系*/
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 资产流转业务申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAssetTrans(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpAssetTransApp", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, "CtrAssetTransCont", this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
//			/**添加授信关系*/
//			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
//			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
//			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 资产流转业务申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAssetPro(String serno,String bizType)throws ComponentException {
		
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String bizTypeNo = bizType;
			String cont_status = "100";
			KeyedCollection mainKColl = dao.queryDetail("IqpAssetProApp", serno, this.getConnection());
			String prdId = (String) mainKColl.getDataValue("prd_id");
			this.getContext().put("inputOrg", mainKColl.getDataValue("input_br_id"));
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizTypeNo, bizTypeNo, this.getConnection(), this.getContext());
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息*/
			KeyedCollection toDefKColl = new KeyedCollection();
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status",cont_status);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", mainKColl, toDefKColl, "CtrAssetProCont", this.getContext(), this.getConnection());
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！请检查产品配置是否正确！");
			}
			
			/** 判断该笔业务下是否存在需要特殊处理的表，需在主表单合同生成成功后才生成tab页签数据（即业务所挂接的从表信息，更新从表信息中的合同编号） */
			this.updateSpecialModel(serno, cont_no, dao, this.getConnection());
			
//			/**添加授信关系*/
//			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
//			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
//			iqpLoanAppComponent.updateLmtRelation(serno, cont_no);
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	public void updateSpecialModel(String serno,String cont_no,TableModelDAO dao,Connection connection) throws EMPException {
		/** 通过申请流水号判断是否存在特殊表需要更新数据 */
		String condition ="where serno='"+serno+"'";
		IndexedCollection cmIColl = dao.queryList("CusManager", condition, connection);
		if(cmIColl != null && cmIColl.size() > 0){
			for(int i=0;i<cmIColl.size();i++){
				KeyedCollection cmKColl = (KeyedCollection)cmIColl.get(i);
				String sernoHelp = (String)cmKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					KeyedCollection updateKColl = new KeyedCollection();
					updateKColl.addDataField("serno", serno);
					updateKColl.addDataField("cont_no", cont_no);
					updateKColl.setName("CusManager");
					dao.update(updateKColl, connection);
				}
			}
		}
		
		KeyedCollection kCollDeclInfo = dao.queryDetail("IqpGreenDeclInfo", serno, connection);
		String sernoSelect = (String)kCollDeclInfo.getDataValue("serno");
		if(sernoSelect != null && sernoSelect.trim().length() > 0){
			kCollDeclInfo.put("cont_no", cont_no);
			dao.update(kCollDeclInfo, connection);
		}
		
		IndexedCollection icaIColl = dao.queryList("IqpCusAcct", condition, connection);
		if(icaIColl != null && icaIColl.size() > 0){
			for(int i=0;i<icaIColl.size();i++){
				KeyedCollection icaKColl = (KeyedCollection)icaIColl.get(i);
				String sernoHelp = (String)icaKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					icaKColl.setDataValue("cont_no", cont_no);
					dao.update(icaKColl, connection);
				}
			}
		}
		
		IndexedCollection baiIColl = dao.queryList("PubBailInfo", condition, connection);
		if(baiIColl != null && baiIColl.size() > 0){ 
			for(int i=0;i<baiIColl.size();i++){
				KeyedCollection baiKColl = (KeyedCollection)baiIColl.get(i);
				String sernoHelp = (String)baiKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					baiKColl.setDataValue("cont_no", cont_no);
					dao.update(baiKColl, connection);
				}
			}
		}
		
		IndexedCollection grtIColl = dao.queryList("GrtLoanRGur", condition, connection);
		if(grtIColl != null && grtIColl.size() > 0){ 
			for(int i=0;i<grtIColl.size();i++){
				KeyedCollection grtKColl = (KeyedCollection)grtIColl.get(i);
				String sernoHelp = (String)grtKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					grtKColl.setDataValue("cont_no", cont_no);
					dao.update(grtKColl, connection); 
				}
			}
		}
		
		IndexedCollection assetIColl = dao.queryList("IqpAssetTransList", condition, connection);
		if(assetIColl != null && assetIColl.size() > 0){ 
			for(int i=0;i<assetIColl.size();i++){
				KeyedCollection assetKColl = (KeyedCollection)assetIColl.get(i);
				String sernoHelp = (String)assetKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					assetKColl.setDataValue("cont_no", cont_no);
					dao.update(assetKColl, connection); 
				}
			}
		}
		
		IndexedCollection assetProIColl = dao.queryList("IqpAssetProList", condition, connection);
		if(assetProIColl != null && assetProIColl.size() > 0){ 
			for(int i=0;i<assetProIColl.size();i++){
				KeyedCollection assetProKColl = (KeyedCollection)assetProIColl.get(i);
				String sernoHelp = (String)assetProKColl.getDataValue("serno"); 
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					assetProKColl.setDataValue("cont_no", cont_no);
					dao.update(assetProKColl, connection); 
				}
			}
		}
	} 
	
	/**
	 * 流程中对比出当前登录人的岗位
	 * @param dutyNoList  当前登录人的岗位
	 * @param wfiDuty     流程中岗位
	 * @throws ComponentException
	 * @author WangS
	 */
	public String findCurrentDuty(String dutyNoList,String wfiDutyNoList)throws ComponentException {
		try {
			String[] dutyList = dutyNoList.split(",");
			String[] wfiDutyList = wfiDutyNoList.split(",");
			for(int i=0;i<dutyList.length;i++){
				if(dutyList[i] != null && !dutyList[i].trim().equals("")){
		    		String duty = dutyList[i];
		    		for(int j=0;j<wfiDutyList.length;j++){
		    			if(wfiDutyList[j] != null && !wfiDutyList[j].trim().equals("")){
		    				String wfiDuty = wfiDutyList[j];
		    				if(duty.equals(wfiDuty)){
		    					return wfiDuty;
		    				}
		    			}
		    		}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程中对比出当前登录人的岗位异常"+e.getMessage());
		}
	    return null;
	}
	
	/**
	 * 统计传入岗位单日累计 金额
	 * @param appl_type  
	 * @param rpddscnt_type     
	 * @param dutyno     
	 * @throws ComponentException
	 * @author WangS
	 */
	public BigDecimal getAllAmt(String appl_type,String rpddscnt_type,String dutyno,BigDecimal total_amt,String pkVal)throws ComponentException {
		BigDecimal amt_total = new BigDecimal(0);
		try {
			DataSource dataSource = (DataSource)this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			String openDay = (String)this.getContext().getDataValue("OPENDAY");
			/* modified by yangzy 2015/02/04 变更公司业务部票据业务权限_XD141225091 start */
			//String sql_select = "select sum(x.amt) as amt_total from " +
			//		            "(select nvl(sum(m.amt),0) as amt " +
			//		            "from wfi_join m where m.instanceid " +
			//		            "in(select a.instanceid from " +
			//		            "wf_node_record a,wf_comment f where substr(a.nodeendtime,1,10) ='"+openDay+"' " +
			//		            "and a.instanceid = f.instanceid and f.commentsign != '60'" +
			//		            "and a.nodeendtime = f.commenttime and a.instanceid " +
			//		            "in(select b.instanceid from wfi_join b where b.appl_type='"+appl_type+"'  " +
			//		            "and b.pk_value in(select d.serno from iqp_rpddscnt d where d.rpddscnt_type='"+rpddscnt_type+"') " +
			//		            "and a.currentnodeuser in(select c.actorno from s_dutyuser c where c.dutyno='"+dutyno+"'))) " +
			//		            "union all select nvl(sum(m.amt),0) as amt from wfi_join_his m where m.instanceid " +
			//		            "in(select a.instanceid from wf_node_recordend a  where substr(a.nodeendtime,1,10) ='"+openDay+"' " +
			//		            "and a.instanceid in(select b.instanceid from wfi_join_his b where b.appl_type='"+appl_type+"'  " +
			//		            "and b.pk_value in(select d.serno from iqp_rpddscnt d where d.rpddscnt_type='"+rpddscnt_type+"') " +
			//		            "and a.currentnodeuser in(select c.actorno from s_dutyuser c where c.dutyno='"+dutyno+"')))) x";
			String sql_select =  " select nvl(sum(m.amt), 0) as amt_total                                  "
								+"   from wfi_join_his m                                                   "
								+"  where m.instanceid in                                                  "
								+"        (select a.instanceid                                             "
								+"           from wf_node_recordend a                                      "
								+"          where substr(a.nodeendtime, 1, 10) = '"+openDay+"'             "
								+"            and a.instanceid in                                          "
								+"                (select b.instanceid                                     "
								+"                   from wfi_join_his b                                   "
								+"                  where b.appl_type = '"+appl_type+"'                    "
								+"                    and b.pk_value in                                    "
								+"                        (select d.serno                                  "
								+"                           from iqp_rpddscnt d                           "
								+"                          where d.rpddscnt_type = '"+rpddscnt_type+"'))) ";
			
			logger.info("统计传入岗位单日累计 金额sql:"+sql_select);
			IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(res_value.size()>0){
				KeyedCollection kColl = (KeyedCollection)res_value.get(0);
				amt_total= BigDecimalUtil.replaceNull(kColl.getDataValue("amt_total"));
			}
			
			//String sql_select_4Cur = "select a.nodeendtime from wf_node_record a " +
			//		                 "where  a.instanceid in "+
            //                         "(select b.instanceid from wfi_join b "+
            //                         "where b.appl_type = '"+appl_type+"' and b.pk_value ='"+pkVal+"') "+
            //                         "and a.currentnodeuser in (select c.actorno from s_dutyuser c where c.dutyno = '"+dutyno+"')";
			//IndexedCollection res_value_4Cur = TableModelUtil.buildPageData(null, dataSource, sql_select_4Cur);
			//logger.info("统计传入岗位单日累计 金额sql2:"+sql_select_4Cur);
			//if(res_value_4Cur.size()>0){
			//	KeyedCollection kColl = (KeyedCollection)res_value_4Cur.get(0);
			//	String nodeendtime = (String)kColl.getDataValue("nodeendtime");
			//	if("".equals(nodeendtime) || nodeendtime == null ){
					amt_total = amt_total.add(total_amt);
			//	}
			//}
			/* modified by yangzy 2015/02/04 变更公司业务部票据业务权限_XD141225091 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("统计传入岗位单日累计 金额异常:"+e.getMessage());
		}
		return amt_total;
	}
	
	
	/**
	 * 定向资管委托贷款计算单户金额
	 * @param cus_id  
	 * @param apply_amount     
	 * @throws ComponentException
	 * @author WangS
	 */
	public BigDecimal caculateAmt4Cus(String principal_loan_typ,String cus_id,BigDecimal apply_amount)throws ComponentException {
		BigDecimal amt_total = new BigDecimal(0);
		try {
			String sql_select = "select nvl(sum(cont.cont_amt * rate.base_remit),0) as amt from ctr_loan_cont cont,ctr_loan_cont_sub sub,prd_rate_maintain rate "+
                                "where cont.cont_no=sub.cont_no "+
                                "and sub.principal_loan_typ ='"+principal_loan_typ+"' "+
                                "and cont.cont_cur_type=rate.fount_cur_type "+
                                "and rate.comp_cur_type='CNY' "+
                                "and cont.cus_id='"+cus_id+"' "+
                                "and cont.cont_status in('100','200')";
			logger.info("定向资管委托贷款计算单户金额sql:"+sql_select);
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(res_value.size()>0){
				KeyedCollection kColl = (KeyedCollection)res_value.get(0);
				BigDecimal amt = BigDecimalUtil.replaceNull(kColl.getDataValue("amt"));
				amt_total = apply_amount.add(amt);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("定向资管委托贷款计算单户金额异常:"+e.getMessage());
		}
		logger.info("定向资管委托贷款计算单户金额:"+amt_total);
		return amt_total;
	}
	/**
	 * 需求编号：XD140911058
	 * 统计委托贷款单户总金额
	 * 2014-09-11
	 * Edited by FCL 
	 * @param cus_id
	 * @param apply_amount 当前申请金额
	 * @return
	 * @throws ComponentException
	 */
	public BigDecimal caculateAmt4Cus(String cus_id,BigDecimal apply_amount)throws ComponentException {
		BigDecimal amt_total = new BigDecimal(0);
		try {
			String sql_select = "select nvl(sum(cont.cont_amt * rate.base_remit),0) as amt from ctr_loan_cont cont,ctr_loan_cont_sub sub,prd_rate_maintain rate "+
			"where cont.cont_no=sub.cont_no "+
			"and cont.prd_id in ('100063','100065') "+
			"and cont.cont_cur_type=rate.fount_cur_type "+
			"and rate.comp_cur_type='CNY' "+
			"and cont.cus_id='"+cus_id+"' "+
			"and cont.cont_status in('100','200')";
			logger.info("委托贷款计算单户金额sql:"+sql_select);
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(res_value.size()>0){
				KeyedCollection kColl = (KeyedCollection)res_value.get(0);
				BigDecimal amt = BigDecimalUtil.replaceNull(kColl.getDataValue("amt"));
				amt_total = apply_amount.add(amt);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("委托贷款计算单户金额异常:"+e.getMessage());
		}
		logger.info("委托贷款计算单户金额:"+amt_total);
		return amt_total;
	}
	
	
}
