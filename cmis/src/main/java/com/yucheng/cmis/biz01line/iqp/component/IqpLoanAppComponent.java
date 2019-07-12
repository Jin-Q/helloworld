package com.yucheng.cmis.biz01line.iqp.component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.agent.IqpLoanAppAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpLoanAppComponent extends CMISComponent {
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	/**
	 * 通过币种获取汇率
	 * @param currType
	 * @return 汇率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getHLByCurrType(String currType) throws Exception {
		logger.info("---------------通过币种获取汇率   开始---------------");
		KeyedCollection returnKColl = new KeyedCollection();
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		KeyedCollection kc = cmisAgent.getHLByCurrType(currType);
		if(null != kc && kc.containsKey("base_remit") && null != kc.getDataValue("base_remit")){
			returnKColl.addDataField("flag", "success");
			returnKColl.addDataField("msg", "");
			returnKColl.addDataField("sld", kc.getDataValue("base_remit"));
		}else {
			returnKColl.addDataField("flag", "failed");
			returnKColl.addDataField("msg", "汇率表中未取到匹配汇率，请确认汇率表汇率配置");
			returnKColl.addDataField("sld", "");
		}
		logger.info("---------------通过币种获取汇率   结束---------------");
		return returnKColl;
	}
	/**
	 * 通过客户码查询客户所属业务类型
	 * @param cus_id
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getBelglineByKhm(String cus_id) throws Exception {
		logger.info("--------------- 通过客户码查询客户所属业务类型   开始---------------");
		KeyedCollection returnKColl = new KeyedCollection();
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		KeyedCollection kc = cmisAgent.getBelglineByKhm(cus_id);
		if(kc != null){
			returnKColl.addDataField("flag", "success");
			returnKColl.addDataField("msg", "");
			returnKColl.addDataField("sld", kc.getDataValue("belg_line"));
		}else {
			returnKColl.addDataField("flag", "failed");
			returnKColl.addDataField("msg", "未查询到客户所属业务条线，请检查！");
			returnKColl.addDataField("sld", "");
		}
		logger.info("--------------- 通过客户码查询客户所属业务类型   结束---------------");
		return returnKColl;
	}
	
	/**
	 * 通过业务品种、币种、期限类型、期限获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termType 期限类型
	 * @param term 期限
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getRate(String prdId, String currType, String termType, String term,Context context,Connection connection) throws Exception {
		logger.info("---------------通过业务品种、币种、期限类型、期限获取利率信息   开始---------------");
		KeyedCollection returnKColl = new KeyedCollection();
		/** 期限转换 */
		int termM = 0;
		if("001".equals(termType)){//年
			termM = Integer.parseInt(term)*12;
		}else if("002".equals(termType)){//月
			termM = Integer.parseInt(term);
		}else if("003".equals(termType)){//日
			System.out.println((Integer.parseInt(term)*12)%365); 
			int termHelp = (Integer.parseInt(term)*12)%365;
			if(termHelp == 0){
				termM = (Integer.parseInt(term)*12/365);
			}else {
				termM = (Integer.parseInt(term)*12/365)+1;
			}
		}else {
			logger.error("未获得正常期限类型");
			throw new Exception("未获得正常期限类型！");
		}
		
//		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
//		KeyedCollection kc = cmisAgent.getRate(prdId,currType,termM);
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
		KeyedCollection kc = service.getPrdRate(prdId, currType, termM, context, connection);
		
		if(kc != null){
			returnKColl.addDataField("flag", "success");
			returnKColl.addDataField("msg", "");
			returnKColl.addDataField("rate", kc.getDataValue("base_rate_m"));
			returnKColl.addDataField("code", kc.getDataValue("base_remit_type"));
		}else {
			returnKColl.addDataField("flag", "failed");
			returnKColl.addDataField("msg", "利率表中未取到匹配利率，请确认利率表配置");
			returnKColl.addDataField("rate", "");
			returnKColl.addDataField("code", "");
		}
		logger.info("---------------通过业务品种、币种、期限类型、期限获取利率信息   结束---------------");
		return returnKColl;
	}
	/**
	 * 通过流水号查询业务申请中改流水号关联表下的所有tab表,删除表中数据
	 * @param serno 业务流水号
	 * @return 
	 * @throws Exception
	 */
	public int delSubTablesRecordBySerno(String serno) throws Exception {
		logger.info("---------------通过流水号查询业务申请中改流水号关联表下的所有tab表,删除表中数据   开始---------------");
		int result = 0;
		List list = new ArrayList();
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		list = cmisAgent.querySubTablesBySerno(serno);
		/** 取出表名 */
		for(int i=0;i<list.size();i++){
			String tname = (String)list.get(i);
			/** 删除表中记录 */
			result = cmisAgent.delSubTablesRecordBySerno(tname,serno);
			if(result <= 0){
				result = 0;
			}
		}
		logger.info("---------------通过流水号查询业务申请中改流水号关联表下的所有tab表,删除表中数据   结束---------------");
		return result;
	}
	
	/**
	 * 通过流水号删除业务担保合同关系表
	 * @param serno 业务流水号
	 * @return 
	 * @throws Exception
	 */
	public int delSubTableGrtLoanRGuarBySerno(String serno,TableModelDAO dao,Context context) throws Exception{
		logger.info("---------------通过流水号删除业务担保合同关系表   开始---------------");
		int result = 0;
		String condition = "where serno='"+serno+"'";
		IndexedCollection iColl = dao.queryList("GrtLoanRGur", condition, this.getConnection());
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
			String pk_id_value = (String)kColl.getDataValue("pk_id");
			/**如果业务担保合同关系表中数据是一条，且担保合同状态为登记状态,则可以删除担保合同记录*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			GrtServiceInterface grtService = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
			int num = service.checkGetLoanRGurNum(guar_cont_no, context, this.getConnection());
			boolean guar_cont_state = grtService.getGuarContStatusByGuarContNo(guar_cont_no, this.getConnection());
			if(num == 1 && guar_cont_state){ 
				guar_cont_no = "'"+guar_cont_no+"'";  
				grtService.deleteGuarContInfo(guar_cont_no, this.getConnection(), context);
			}
			
			int countRel = dao.deleteByPk("GrtLoanRGur", pk_id_value, this.getConnection()); 
		}
		logger.info("---------------通过流水号删除业务担保合同关系表   结束---------------");
		return result;
	}
	/**
	 * 往票据批次关联表中插入关联数据
	 * @param param 数据Map
	 * @return
	 * @throws Exception
	 */
	public int insertIqpBatchBillRel(Map<String,String> param) throws Exception {
		logger.info("---------------往票据批次关联表中插入关联数据   开始---------------");
		int result = 0;
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		result = cmisAgent.insertIqpBatchBillRel(param);
		logger.info("---------------往票据批次关联表中插入关联数据   结束---------------");
		return result;
	}
	/**
	 * 通过汇票号码获取汇票信息
	 * @param porderno 汇票号码
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getPorderMsgByPorderNo(String porderno) throws Exception {
		logger.info("---------------通过汇票号码获取汇票信息   开始---------------");
		KeyedCollection kc = null;
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		kc = cmisAgent.getPorderMsgByPorderNo(porderno);
		if(kc != null && kc.size() > 0){
			kc.addDataField("code", "0000");
			kc.addDataField("msg", "存在该票据信息，自动赋值");
		}else {
			kc = new KeyedCollection();
			kc.addDataField("code", "9999");
			kc.addDataField("msg", "不存在该票据信息，可以新增");
		}
		logger.info("---------------通过汇票号码获取汇票信息   结束---------------");
		return kc;
	}
	/**
	 * 通过批次号获取当前批次下所有票据信息，从而更新批次信息
	 * @param batchNo 批次号
	 * @return
	 * @throws Exception
	 */
	public int updateIqpBatchMng(String batchNo) throws Exception {
		logger.info("---------------通过批次号获取当前批次下所有票据信息，从而更新批次信息   开始---------------");
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		/** 取得每张票据的信息，从新计算票据的利息 */
		logger.info("---------------通过批次号获取当前批次下所有票据信息，从而更新批次信息   结束---------------");
		return 0;
	}
	
	public int deleteTabByAssetno(String asset_no) throws Exception{
		int result=1;
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		result = cmisAgent.deleteTabByAssetno(asset_no);
		return result;
	}
	
	/**
	 * 根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）
	 * （循环额度处理方式，调用前先判断是否使用的是循环额度）
	 * 取生效合同和未生效合同
	 * 
	 * 注：需区分授信占用和风险敞口占用，风险敞口占用包含了不使用授信部分，而授信占用=使用了授信的风险敞口占用。
	 * 此方法为，计算合同的风险敞口占用，如果用来计算授信占用需过滤出使用授信的合同。
	 * 
	 * 
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）   开始---------------");
 		BigDecimal lmt_amt = null;
		String prd_id = "";
		String cont_status = "";
		String serno = "";
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！getLmtAmtByContNo()合同编号:"+cont_no, null);
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**遍历CtrLoanCont表*/
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			cont_status = (String) kColl.getDataValue("cont_status");
			serno = (String) kColl.getDataValue("serno");
			BigDecimal exchange_rate = null;
			BigDecimal cont_amt = null;
			if(cont_status!=null&&!cont_status.equals("")){
				prd_id = (String) kColl.getDataValue("prd_id");
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！cont_status:"+cont_status, null);	
				if("200".equals(cont_status) || "100".equals(cont_status)){//统计未生效合同及已生效合同
								
					cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
					
					//获取实时汇率  start
					String cur_type = (String) kColl.getDataValue("cont_cur_type");
					String security_cur_type = (String) kColl.getDataValue("security_cur_type");//保证金币种
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					
					/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
					//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
					//KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, this.getContext(), this.getConnection());
					//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！汇率获取:"+security_cur_type+"     "  + cur_type, null);		
					//if("failed".equals(kCollRate.getDataValue("flag"))){
					//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					//}
					//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
					//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					//}
					//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！汇率获取:", null);	
					//exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
					//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
					exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
					BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
					/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
					
					//获取实时汇率  end
					
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！getLmtAmtByContNo()获取汇率:"+exchange_rate, null);
					
					BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate")); //保证金比例
					BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("same_security_amt"));//视同保证金
					
					BigDecimal risk_open_amt = new BigDecimal(0);
					risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！getLmtAmtByContNo()敞口:"+risk_open_amt, null);
					
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
							prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
							prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")||prd_id.equals("100865")){//modify by jiangcuihua 业务品种
						
						//判断是否为信用证业务
						if("700020".equals(prd_id) || "700021".equals(prd_id)){
							KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, this.getConnection());
							if(kCollCredit.containsKey("serno")){
								floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
							}
						}
						//申请金额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
						//计算结果进百
						//进百后乘保证金汇率
						BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
						securityAmt = this.carryCurrency(securityAmt);
						risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
						if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
							risk_open_amt = new BigDecimal(0);
						}
					}
					
					risk_open_amt = this.caculateLimitSpac(serno, risk_open_amt);
					
					if(prd_id.equals("100039")||prd_id.equals("100050")){//循环贷款,授信占用=合同金额（取敞口金额）
						lmt_amt = risk_open_amt;
					}else if(prd_id.equals("300021")||prd_id.equals("300020")){//贴现贷款,产生的为票据台账,授信占用=未核销票据金额*敞口比例
						KeyedCollection drftkcoll = (KeyedCollection)SqlClient.queryFirst("queryDrftAmt4CtrLoan", cont_no, null, this.getConnection());
						lmt_amt = BigDecimalUtil.replaceNull(drftkcoll.getDataValue("amt"));
						lmt_amt = (lmt_amt.multiply(risk_open_amt)).divide(cont_amt.multiply(new BigDecimal(1).add(floodact_perc)),2,BigDecimal.ROUND_HALF_EVEN);
					}else if(prd_id.equals("200024")){//银承贷款,产生的为银承台账,授信占用=合同金额（取敞开金额）
						String condtitionStr = "where cont_no='"+cont_no+"'";
						IndexedCollection iColl = dao.queryList("AccView", condtitionStr, this.getConnection());
						if(iColl.size()>0){
							BigDecimal loan_balance_all = this.getAccpBalanceByContNo(cont_no);
							lmt_amt = (loan_balance_all.multiply(risk_open_amt)).divide(cont_amt,2,BigDecimal.ROUND_HALF_EVEN);
							lmt_amt = lmt_amt.add(this.getAccPadBalByContNo(cont_no));
						}else{
							lmt_amt = risk_open_amt;
						}
					}else{//其他类型业务,生成贷款台账,授信占用=(合同余额+台账余额)*敞口比例
						BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_balance")).multiply(new BigDecimal(1).add(floodact_perc));
						BigDecimal loan_balance_all = this.getLoanBalanceByContNo(cont_no);
						lmt_amt = cont_balance.add(loan_balance_all);
						lmt_amt = (lmt_amt.multiply(risk_open_amt)).divide(cont_amt.multiply(new BigDecimal(1).add(floodact_perc)),2,BigDecimal.ROUND_HALF_EVEN);
						//敞口比例采用敞口金额/合同金额算出。为防止四舍五入精度丢失，把除法放到最后并保留2位金额
						lmt_amt = lmt_amt.add(this.getAccPadBalByContNo(cont_no));
					}
				}else{
					lmt_amt = new BigDecimal("0");//无授信占用
				}
			}else{
				//throw new Exception("根据合同号获取授信占用失败，失败原因：合同状态为空！");
				lmt_amt = new BigDecimal("0");//无授信占用
			}
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败！getLmtAmtByContNo():"+e.getMessage(), null);
			logger.error("根据合同号获取授信占用失败！"+e.getStackTrace());
			throw new Exception("根据合同号获取授信占用失败，失败原因："+e.getMessage());
		}
		System.out.println(lmt_amt);
		logger.info("---------------根据合同编号查询该合同的授信占用:"+cont_no+"授信占用:"+lmt_amt+"---------------");
		logger.info("---------------根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）   结束---------------");
		return lmt_amt;
		
	}
	
	/**
	 * 根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）
	 * （一次性额度处理方式，调用前先判断是否使用的是一次性额度）
	 * 除撤销状态合同，都统计合同金额
	 * 
	 * 注：需区分授信占用和风险敞口占用，风险敞口占用包含了不适用授信部分，而授信占用=使用了授信的风险敞口占用。
	 * 此方法为，计算合同的风险敞口占用，如果用来计算授信占用需过滤出使用授信的合同。
	 * 
	 * 
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getOneLmtAmtByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）   开始---------------");
		BigDecimal lmt_amt = null;
		String cont_status = "";
		String serno = "";
		BigDecimal exchange_rate = null;
		BigDecimal cont_amt = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**遍历CtrLoanCont表*/
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			cont_status = (String) kColl.getDataValue("cont_status");
			serno = (String) kColl.getDataValue("serno");
			String prd_id = (String) kColl.getDataValue("prd_id");
			if(cont_status!=null&&!cont_status.equals("")){
				if(!"700".equals(cont_status)&&!"800".equals(cont_status)){//除撤销和作废状态合同
					
					cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));

					//获取实时汇率  start
					String cur_type = (String) kColl.getDataValue("cont_cur_type");
					String security_cur_type = (String) kColl.getDataValue("security_cur_type");//保证金币种
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					
					/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
					//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
					//KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, this.getContext(), this.getConnection());
					//if("failed".equals(kCollRate.getDataValue("flag"))){
					//	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "汇率表中未取到匹配汇率，请确认汇率表汇率配置！"+cur_type+"!!", null);	
					//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					//}
					//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
					//	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "汇率表中未取到匹配汇率，请确认汇率表汇率配置！"+security_cur_type+"!!", null);
					//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					//}
					//exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
					//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
					exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
					BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
					/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
					
					//获取实时汇率  end
					
					BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate")); //保证金比例
					BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("same_security_amt"));//视同保证金
					
					BigDecimal risk_open_amt = new BigDecimal(0);
					risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
					
					if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
							prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
							prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
						
						//判断是否为信用证业务
						BigDecimal floodact_perc = new BigDecimal(0);
						if("700020".equals(prd_id) || "700021".equals(prd_id)){
							KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, this.getConnection());
							if(kCollCredit.containsKey("serno")){
								floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
								//risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
								//risk_open_amt = this.caculateLimitSpac(serno, risk_open_amt);
								//lmt_amt = risk_open_amt;
							}
						}
						
						BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
						nf.setGroupingUsed(false);
						String caculateAmt = String.valueOf(securityAmt);
						securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
						String changeAmt = nf.format(securityAmt);
						securityAmt = BigDecimalUtil.replaceNull(changeAmt);
						risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
						if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
							risk_open_amt = new BigDecimal(0);
						}
					}
					
					risk_open_amt = this.caculateLimitSpac(serno, risk_open_amt);
					lmt_amt = risk_open_amt;
					
				}else{
					lmt_amt = new BigDecimal("0");//无授信占用
				}
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败，失败原因：合同状态为空", null);
				throw new Exception("根据合同号获取授信占用失败，失败原因：合同状态为空！");
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取授信占用失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同号获取授信占用失败，失败原因："+e.getMessage());
		}
		System.out.println(lmt_amt);
		logger.info("---------------根据合同编号查询该合同的授信占用:"+cont_no+"授信占用:"+lmt_amt+"---------------");
		logger.info("---------------根据合同编号查询该合同的授信占用。（授信占用=金额*敞口比例*汇率）   结束---------------");
		return lmt_amt;
	}
	
	/**
	 * 根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（授信占用=敞口金额）
	 * 
	 * 注：需区分授信占用和风险敞口占用，风险敞口占用包含了不适用授信部分，而授信占用=使用了授信的风险敞口占用。
	 * 此方法为，计算申请的风险敞口占用，如果用来计算授信占用需过滤出使用授信的申请。
	 * 
	 * @param serno 申请编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtBySerno(String serno) throws Exception {
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（授信占用=敞口金额）   开始---------------");
		BigDecimal lmt_amt = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**遍历IqpLoanApp表*/
			KeyedCollection kColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
			String prd_id = (String) kColl.getDataValue("prd_id");
			BigDecimal apply_amount = BigDecimalUtil.replaceNull(kColl.getDataValue("apply_amount"));
			logger.info("融资性担保公司授信额度及限额检查，getLmtAmtBySerno：prd_id: "+prd_id);
			if(prd_id != null && !"".equals(prd_id)){
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
				//获取实时汇率  start
				String cur_type = (String) kColl.getDataValue("apply_cur_type");
				String security_cur_type = (String) kColl.getDataValue("security_cur_type");//保证金币种
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
				//KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, this.getContext(), this.getConnection());
				//logger.info("融资性担保公司授信额度及限额检查，获取汇率: "+security_cur_type);
				//if("failed".equals(kCollRate.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				//获取实时汇率  end
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				//risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				//业务申请交易保证金是否覆盖（计算方法与前台一致申请金额减保证金金额 ）
				risk_open_amt = apply_amount.multiply(exchange_rate).subtract(apply_amount.multiply(security_rate).multiply(exchange_rate).setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc  = new BigDecimal(0);
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, this.getConnection());
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
							//risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
							//lmt_amt = risk_open_amt;//（敞口金额）
							//lmt_amt = this.caculateLimitSpac(serno, lmt_amt);
						}
					}
					
					BigDecimal securityAmt = apply_amount.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
					if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
						risk_open_amt = new BigDecimal(0);
					}
				}
				logger.info("融资性担保公司授信额度及限额检查，查询业务申请 ");
				
				//modified by licj 20131022 begin 查询业务申请的授信占用，应去除被取消与否决的申请
				String approve_status = (String)kColl.getDataValue("approve_status");
				//if(risk_open_amt!=null&&!risk_open_amt.equals("")){
				if((risk_open_amt != null && !risk_open_amt.equals(""))
						&& (approve_status != null && !("990").equals(approve_status) && !("998").equals(approve_status))){
				//modified by licj 20131022 end
					lmt_amt = risk_open_amt;//（敞口金额）
					logger.info("融资性担保公司授信额度及限额检查，查询lmt_amt： "+lmt_amt);
					lmt_amt = this.caculateLimitSpac(serno, lmt_amt);
				}else{
					lmt_amt = new BigDecimal("0");
				}
			}else{
				lmt_amt = new BigDecimal(0);
			}
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据申请编号获取授信占用失败！"+e.getMessage(), null);
			logger.error("根据申请编号获取授信占用失败！"+e.getStackTrace());
			throw new Exception("根据申请编号获取授信占用失败，失败原因："+e.getMessage());
		}
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用"+serno+"授信占用:"+lmt_amt+"---------------");
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（授信占用=敞口金额）   结束---------------");
		return lmt_amt;
	}
	
	/**保函修改、信用证修改专用 
	 * （授信占用=敞口金额）
	 * @param serno 申请编号
	 * @return lmt_amt
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtBySerno4IqpChange(String tableName,String serno) throws Exception {
		logger.info("---------------保函修改、信用证修改授信占用专用   开始---------------");
		BigDecimal lmt_amt = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**遍历IqpLoanApp表*/
			KeyedCollection kColl = dao.queryDetail(tableName, serno, this.getConnection());
			String prd_id = (String) kColl.getDataValue("prd_id");
			String cont_no = (String) kColl.getDataValue("cont_no");
			//获取修改后的申请金额
			BigDecimal apply_amount = new BigDecimal(0);
			String cur_type;
			if("IqpCreditChangeApp".equals(tableName)){
				apply_amount = BigDecimalUtil.replaceNull(kColl.getDataValue("new_apply_amt"));
				cur_type = (String) kColl.getDataValue("new_cur_type");
			}else{
				apply_amount = BigDecimalUtil.replaceNull(kColl.getDataValue("new_cont_amt"));
				cur_type = (String) kColl.getDataValue("new_cur_type");
			}
			
			if(prd_id != null && !"".equals(prd_id)){
				//获取实时汇率  start
				String security_cur_type = (String) kColl.getDataValue("security_cur_type");//保证金币种
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
				KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, this.getContext(), this.getConnection());
				if("failed".equals(kCollRate.getDataValue("flag"))){
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "汇率表中未取到匹配汇率，请确认汇率表汇率配置！"+cur_type+"!!", null);	
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "汇率表中未取到匹配汇率，请确认汇率表汇率配置！"+security_cur_type+"!!", null);	
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				//获取实时汇率  end
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_rate")); //修改后保证金比例
				BigDecimal same_security_amt = new BigDecimal(0);//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc  = new BigDecimal(0);
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						floodact_perc = BigDecimalUtil.replaceNull(kColl.getDataValue("new_floodact_perc"));
					}
					
					BigDecimal securityAmt = apply_amount.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(changeAmt);
					risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
					if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
						risk_open_amt = new BigDecimal(0);
					}
				}
				
				lmt_amt = risk_open_amt;//（敞口金额）
				lmt_amt = this.caculateLimitSpac4IqpChange(cont_no, lmt_amt);
			}else{
				lmt_amt = new BigDecimal(0);
			}
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据信用证修改/保函修改申请编号获取授信占用失败，失败原因："+e.getMessage(), null);	
			e.printStackTrace();
			throw new Exception("根据信用证修改/保函修改申请编号获取授信占用失败，失败原因："+e.getMessage());
		}
		logger.info("---------------保函修改、信用证修改授信占用专用本次"+serno+"授信占用:"+lmt_amt+"---------------");
		logger.info("---------------保函修改、信用证修改授信占用专用   结束---------------");
		return lmt_amt;
	}
	
	/**
	 * 根据合同编号查询该合同的授信占用。（同业授信专用）
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtByContNoForBank(String cont_no) throws Exception {
		logger.info("---------------根据合同编号查询该合同的授信占用。（同业授信专用）   开始---------------");
		BigDecimal lmt_amt1 = null;
		BigDecimal lmt_amt2 = null;
		String cont_status = "";
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			/**遍历CtrRpddscntCont表，转贴现表*/
			String condition = "where cont_no='"+cont_no+"'";
			KeyedCollection rpkColl = dao.queryDetail("CtrRpddscntCont", cont_no, this.getConnection());
			cont_status = (String) rpkColl.getDataValue("cont_status");
			if(cont_status!=null&&!cont_status.equals("")){
				if("200".equals(cont_status) || "100".equals(cont_status)){//仅统计生效合同
					//BigDecimal rpay_amt = new BigDecimal(rpkColl.getDataValue("bill_total_amt")+"");//票面总金额
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryDrftAmt4CtrSp", cont_no, null, this.getConnection());
					lmt_amt1 = BigDecimalUtil.replaceNull(kColl.getDataValue("amt"));
				}else{
					lmt_amt1 = new BigDecimal("0");//无授信占用
				}
			}else{
				lmt_amt1 = new BigDecimal("0");//无授信占用
			}
			
			/**遍历CtrAssetstrsfCont表，资产转受让合同表*/
			KeyedCollection assetkColl = dao.queryDetail("CtrAssetstrsfCont", cont_no, this.getConnection());
			cont_status = (String) assetkColl.getDataValue("cont_status");
			if(cont_status!=null&&!cont_status.equals("")){
				if("200".equals(cont_status) || "100".equals(cont_status)){//仅统计生效合同
					String sql_select = "select a.cont_no, sum(a.loan_balance*b.base_remit) loan_balance "+
                                 "from acc_loan a,prd_rate_maintain b "+
                                 "where acc_status not in ('6', '8', '9', '10', '11') "+
                                 "and a.cont_no = '"+cont_no+"' "+
                                 "and b.comp_cur_type ='CNY' "+
                                 "and a.cur_type = b.fount_cur_type "+
                                 "group by a.cont_no";
					IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
					KeyedCollection kc = null;
					BigDecimal loan_balance_all = new BigDecimal(0);
					if(res_value.size()>0){
						kc = (KeyedCollection) res_value.get(0);
						loan_balance_all = new BigDecimal(kc.getDataValue("loan_balance")+"");
					}
					lmt_amt2 = loan_balance_all;
				}else{
					lmt_amt2 = new BigDecimal("0");//无授信占用
				}
			}else{
				lmt_amt2 = new BigDecimal("0");//无授信占用
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号获取同业授信占用失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同号获取同业授信占用失败，失败原因："+e.getMessage());
		}
		logger.info("---------------根据合同编号查询该合同的授信占用。（同业授信专用）本次:"+cont_no+"授信占用:"+lmt_amt1+""+lmt_amt2+"---------------");
		logger.info("---------------根据合同编号查询该合同的授信占用。（同业授信专用）   结束---------------");
		return lmt_amt1.add(lmt_amt2);
	}
	
	/**
	 * 根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（同业授信专用）
	 * @param serno 申请编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtBySernoForBank(String serno) throws Exception {
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（同业授信专用）   开始---------------");
		BigDecimal lmt_amt1 = null;
		BigDecimal lmt_amt2 = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**遍历IqpRpddscnt表，转贴现表*/
			KeyedCollection rpkColl = dao.queryDetail("IqpRpddscnt", serno, this.getConnection());
			String rpay_amt = (String) rpkColl.getDataValue("bill_total_amt");//取票面金额
			if(rpay_amt!=null&&!rpay_amt.equals("")){
				lmt_amt1 = new BigDecimal(rpay_amt);
			}else{
				lmt_amt1 = new BigDecimal("0");
			}
			
			/**遍历IqpAssetstrsf表，资产转受让合同表*/
			KeyedCollection assetkColl = dao.queryDetail("IqpAssetstrsf", serno, this.getConnection());
			String takeover_total_amt = (String) assetkColl.getDataValue("takeover_total_amt");//取转让金额
			if(takeover_total_amt!=null&&!takeover_total_amt.equals("")){
				lmt_amt2 = new BigDecimal(takeover_total_amt);
			}else{
				lmt_amt2 = new BigDecimal("0");
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（同业授信专用）授信占用失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据申请编号获取同业授信占用失败，失败原因："+e.getMessage());
		}
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（同业授信专用）本次:"+serno+"授信占用:"+lmt_amt1+""+lmt_amt2+"---------------");
		logger.info("---------------根据申请编号查询该业务申请（未生成合同的业务）的授信占用。（同业授信专用）   结束---------------");
		return lmt_amt1.add(lmt_amt2);
	}
	
	/**
	 * 根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLoanBalanceByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)   开始---------------");
		BigDecimal loan_balance_all = new BigDecimal("0");
		String cont_status = "";
		IndexedCollection iColl = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			cont_status = (String) kColl.getDataValue("cont_status");
			String prd_id = (String)kColl.getDataValue("prd_id");
			if(cont_status.equals("200")){//仅统计生效合同
				if("700020".equals(prd_id) || "700021".equals(prd_id) || "400020".equals(prd_id)){
					iColl = SqlClient.queryList4IColl("queryLoanBalanceByContNo4Spe", cont_no, this.getConnection());
				}else{
					iColl = SqlClient.queryList4IColl("queryLoanBalanceByContNo", cont_no, this.getConnection());
				}
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					loan_balance_all = new BigDecimal(kc.getDataValue("loan_balance")+"");
				}
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同号获取总贷款余额失败!");
		}
		logger.info("---------------根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)本次:"+cont_no+"总贷款余额:"+loan_balance_all+"---------------");
		logger.info("---------------根据合同编号获取该合同下所有有效台账的总贷款余额(仅统计贷款台账acc_loan)   结束---------------");
		return loan_balance_all;
	}
	
	
	/**
	 * 根据合同编号获取该合同下所有有效银承台账的总贷款余额(仅统计贷款台账acc_accp)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getAccpBalanceByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号获取该合同下所有有效银承台账的总贷款余额(仅统计贷款台账acc_accp)   开始---------------");
		BigDecimal loan_balance_all = new BigDecimal("0");
		String cont_status = "";
		IndexedCollection iColl = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			cont_status = (String) kColl.getDataValue("cont_status");
			if(cont_status.equals("200")){//仅统计生效合同
				iColl = SqlClient.queryList4IColl("queryAccpBalanceByContNo", cont_no, this.getConnection());
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					loan_balance_all = BigDecimalUtil.replaceNull(kc.getDataValue("loan_balance"));
				}
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同编号获取该合同下所有有效银承台账的总贷款余额(仅统计贷款台账acc_accp)失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同号获取总贷款余额失败!");
		}
		logger.info("---------------根据合同编号获取该合同下所有有效银承台账的总贷款余额(仅统计贷款台账acc_accp)本次:"+cont_no+"总贷款余额:"+loan_balance_all+"---------------");
		logger.info("---------------根据合同编号获取该合同下所有有效银承台账的总贷款余额(仅统计贷款台账acc_accp)   结束---------------");
		return loan_balance_all;
	}
	
	/**
	 * 根据合同编号获取该合同下垫款余额(仅统计垫款台账acc_pad)
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getAccPadBalByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号获取该合同下垫款余额(仅统计垫款台账acc_pad)   开始---------------");
		BigDecimal pad_bal = new BigDecimal("0");
		IndexedCollection iColl = null;
		try{
			iColl = SqlClient.queryList4IColl("getAccPadBalByContNo", cont_no, this.getConnection());
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kc = (KeyedCollection) iColl.get(i);
				pad_bal = pad_bal.add(BigDecimalUtil.replaceNull(kc.getDataValue("pad_bal")));
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同编号获取该合同下垫款余额(仅统计垫款台账acc_pad)失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同编号获取该合同下垫款余额!");
		}
		logger.info("---------------根据合同编号获取该合同下垫款余额(仅统计垫款台账acc_pad)本次:"+cont_no+"垫款余额:"+pad_bal+"---------------");
		logger.info("---------------根据合同编号获取该合同下垫款余额(仅统计垫款台账acc_pad)   结束---------------");
		return pad_bal;
	}
	
	/**
	 * 根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)。
	 * @param cont_no 合同编号
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getWFPvpAmtByContNo(String cont_no) throws Exception {
		logger.info("---------------根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)  开始---------------");
		BigDecimal pvp_amt_all = new BigDecimal("0");
		String cont_status = "";
		IndexedCollection iColl = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			cont_status = (String) kColl.getDataValue("cont_status");
			if(cont_status.equals("200")){//仅统计生效合同
				iColl = SqlClient.queryList4IColl("queryWFPvpAmtByContNo", cont_no, this.getConnection());
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					pvp_amt_all = new BigDecimal(kc.getDataValue("pvp_amt")+"");
				}
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同编号获取该合同下所有未审批通过的出账金额失败!");
		}
		logger.info("---------------根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)本次:"+cont_no+"出账金额:"+pvp_amt_all+"---------------");
		logger.info("---------------根据合同编号获取该合同下所有未审批通过的出账金额(仅统计贷款台账acc_loan)  结束---------------");
		return pvp_amt_all;
	}
	
	/**
	 * 根据客户号获取该客户下所有使用单一法人授信的授信占用。
	 * 
	 * 注：在授信关系表中有值，即使用了授信，删除申请时会删除关系。
	 * 
	 * @param cus_id 客户码
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtByCusId(String cus_id) throws Exception {
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用  开始---------------");
		BigDecimal lmt_amt = new BigDecimal("0");
		Connection connection = null;
		
		try{
			connection = this.getConnection();
			
			/**第一步，获取所有需要统计的合同（使用循环额度）
			 * 1生效或未生效状态合同
			 * 2在关系表中存在
			 * */
			IndexedCollection contiColl = SqlClient.queryList4IColl("queryContInfoForLmtByCusId", cus_id, connection);
			
			/**第二步，统计所有合同的授信占用*/
			if(contiColl.size()>0){
				KeyedCollection contkColl = new KeyedCollection();
				for(int i=0;i<contiColl.size();i++){
					contkColl = (KeyedCollection) contiColl.get(i);
					String cont_no = (String) contkColl.getDataValue("cont_no");
					lmt_amt = lmt_amt.add(this.getLmtAmtByContNo(cont_no));
				}
			}
			
			/**增加第三步，获取所有需要统计的合同（使用一次性额度）
			 * 1除撤销状态合同
			 * 2在关系表中存在
			 * */
			IndexedCollection onecontiColl = SqlClient.queryList4IColl("queryContInfoForOneLmtByCusId", cus_id, connection);
			
			/**增加第四步，统计所有合同的授信占用*/
			if(onecontiColl.size()>0){
				KeyedCollection onecontkColl = new KeyedCollection();
				for(int i=0;i<onecontiColl.size();i++){
					onecontkColl = (KeyedCollection) onecontiColl.get(i);
					String cont_no = (String) onecontkColl.getDataValue("cont_no");
					lmt_amt = lmt_amt.add(this.getOneLmtAmtByContNo(cont_no));
				}
			}
			
			/**第三步，获取所有需要统计的申请
			 * 1在关系表中存在
			 * 2关系表中合同编号为空
			 * */
			IndexedCollection iqpIColl = SqlClient.queryList4IColl("queryIqpInfoForLmtByCusId", cus_id, connection);
			
			/**第四步，统计所有申请的授信占用*/
			if(iqpIColl.size()>0){
				KeyedCollection iqpkColl = new KeyedCollection();
				for(int i=0;i<iqpIColl.size();i++){
					iqpkColl = (KeyedCollection) iqpIColl.get(i);
					String serno = (String) iqpkColl.getDataValue("serno");
					lmt_amt = lmt_amt.add(this.getLmtAmtBySerno(serno));
				}
			}
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据客户号获取该客户下所有使用单一法人授信的授信占用失败，失败原因："+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据客户号获取该客户下所有使用单一法人授信的授信占用失败!");
		}
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用  结束---------------");
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用本次:"+cus_id+"授信占用:"+lmt_amt+"---------------");
		return lmt_amt;
	}
	
	/**
	 * 添加授信关系
	 * @param lmt_type
	 * @param serno
	 * @param agr_no
	 * @throws Exception
	 */
	public void doLmtRelation(String lmt_type, String serno,String agr_no) throws Exception {
		logger.info("---------------添加授信关系  开始---------------");
		Connection connection = null;
		try{
			if(lmt_type==null||lmt_type.equals("")){
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "授信类型为空，无法创建授信关系", null);
				throw new Exception("授信类型为空，无法创建授信关系！");
			}
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			String condition = "where serno='"+serno+"'";
			KeyedCollection kColl = dao.queryDetail("RBusLmtInfo", serno, connection);
			KeyedCollection kCollCredit = dao.queryFirst("RBusLmtcreditInfo",null, condition, connection);
			
			
			if(lmt_type.equals("01")){//单一法人
				if(kColl.getDataValue("serno")==null){
					kColl.setDataValue("serno", serno);
					kColl.setDataValue("agr_no", agr_no);
					dao.insert(kColl, connection);
				}else{
					kColl.setDataValue("agr_no", agr_no);
					dao.update(kColl, connection);
				}
				//检查是否存在第三方授信关联记录，如果有则删除。
	        	if(!"".equals(kCollCredit.getDataValue("serno")) && kCollCredit.getDataValue("serno")!=null){
	        		String old_agr_no = (String)kCollCredit.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", old_agr_no);
	        		dao.deleteAllByPks("RBusLmtcreditInfo", hashMap, connection);
	        	}
			}else if(lmt_type.equals("02")){//同业授信
				//检查是否已经存在自有授信关联记录，如果有，则删除。
	        	if(!"".equals(kColl.getDataValue("serno")) && kColl.getDataValue("serno")!=null){
	        		dao.deleteAllByPk("RBusLmtInfo", serno, connection);
	        	}
	        	if(!"".equals(kCollCredit.getDataValue("serno")) && kCollCredit.getDataValue("serno")!=null){
	        		String old_agr_no = (String)kCollCredit.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", old_agr_no);
	        		dao.deleteAllByPks("RBusLmtcreditInfo", hashMap, connection);
	        		
	        		kCollCredit.put("agr_no", agr_no);
	        		kCollCredit.put("lmt_type", lmt_type);
	        		kCollCredit.put("serno", serno);
	        		kCollCredit.put("cont_no", "");
	            	dao.insert(kCollCredit, connection);
	        	}else{
	        		kCollCredit.put("agr_no", agr_no);
	        		kCollCredit.put("lmt_type", lmt_type);
	        		kCollCredit.put("serno", serno);
	        		kCollCredit.put("cont_no", "");
	            	dao.insert(kCollCredit, connection);
	        	}
			}else if(lmt_type.equals("03")){
				//检查是否已经存在自有授信关联记录，如果有，则删除。
	        	if(!"".equals(kColl.getDataValue("serno")) && kColl.getDataValue("serno")!=null){
	        		dao.deleteAllByPk("RBusLmtInfo", serno, connection);
	        	}
	        	if(!"".equals(kCollCredit.getDataValue("serno")) && kCollCredit.getDataValue("serno")!=null){
	        		String old_agr_no = (String)kCollCredit.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", old_agr_no);
	        		dao.deleteAllByPks("RBusLmtcreditInfo", hashMap, connection);
	        		
	        		kCollCredit.put("agr_no", agr_no);
	        		kCollCredit.put("lmt_type", lmt_type);
	        		kCollCredit.put("serno", serno);
	        		kCollCredit.put("cont_no", "");
	            	dao.insert(kCollCredit, connection);
	        	}else{
	        		kCollCredit.put("agr_no", agr_no);
	        		kCollCredit.put("lmt_type", lmt_type);
	        		kCollCredit.put("serno", serno);
	        		kCollCredit.put("cont_no", "");
	            	dao.insert(kCollCredit, connection);
	        	}
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "未知授信类型，无法创建授信关系！", null);
				throw new Exception("未知授信类型，无法创建授信关系！");
			}
			logger.info("---------------添加授信关系  结束---------------");
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "添加授信关系失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("添加授信关系失败!");
		}
	}
	
	/**
	 * 根据流水号更新授信关系表的合同号
	 * @param serno
	 * @param cont_no
	 * @throws Exception
	 */
	public void updateLmtRelation(String serno,String cont_no) throws Exception {
		logger.info("---------------根据流水号更新授信关系表的合同号 开始---------------");
		Connection connection = null;
		try{
			/** 通过申请流水号判断是否存在需要更新数据 */
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection lmtKColl = dao.queryDetail("RBusLmtInfo", serno, connection);
			if(lmtKColl.getDataValue("serno")!=null&&!lmtKColl.getDataValue("serno").equals("")){
				lmtKColl.setDataValue("cont_no", cont_no);
				dao.update(lmtKColl, connection);
			}
			String condition = "where serno='"+serno+"'";
			//KeyedCollection lmtCreditKColl = dao.queryDetail("RBusLmtcreditInfo", serno, connection);
			IndexedCollection lmtCreditIColl = dao.queryList("RBusLmtcreditInfo", condition, connection);
			for(int i=0;i<lmtCreditIColl.size();i++){
				KeyedCollection lmtCreditKColl = (KeyedCollection)lmtCreditIColl.get(i);
				if(lmtCreditKColl.getDataValue("serno")!=null&&!lmtCreditKColl.getDataValue("serno").equals("")){
					lmtCreditKColl.setDataValue("cont_no", cont_no);
					dao.update(lmtCreditKColl, connection);
				}
			}
			logger.info("---------------根据流水号更新授信关系表的合同号 结束---------------");
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据流水号更新授信关系表的合同号失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据流水号更新授信关系表的合同号失败!");
		}
	}
	
	/**
	 * 根据流水号删除授信关系
	 * @param serno
	 * @throws Exception
	 */
	public void deleteLmtRelation(String serno) throws Exception {
		logger.info("---------------根据流水号删除授信关系 开始---------------");
		Connection connection = null;
		try{
			/** 通过申请流水号判断是否存在需要更新数据 */
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			//删除单一法人授信关系
			dao.deleteByPk("RBusLmtInfo", serno, connection);
			//删除合作方或同业授信关系
			SqlClient.delete("deleteLmtcreditBySerno", serno, connection);
			logger.info("---------------根据流水号删除授信关系 结束---------------");
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据流水号删除授信关系失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据流水号删除授信关系失败!");
		}
	}
	
	public int updateGrtLoanRGurLvlYB(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.updateGrtLoanRGurLvlYB(map, connection);
	}  
	public int updateGrtLoanRGurLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.updateGrtLoanRGurLvlZGE(map, connection); 
	}
	/**
	 * 信用证修改时，关联关系为正常的改为续作
	 * @cont_no 合同编号
	 * @return 修改结果
	 * @throws Exception 
	 */
	public int updateGrtLoanRGurCorreRel(String serno,Connection connection) throws Exception{
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.updateGrtLoanRGurCorreRel(serno, connection);      
	}
	/**
	 * 通过担保合同编号字符串查询合同编号
	 * guar_cont_no 担保合同字符串 
	 * @return 修改结果
	 * @throws Exception 
	 */
	public IndexedCollection getGrtLoanRGur(IndexedCollection GrtGuarContIColl,Connection connection) throws Exception{
		String guar_cont_no_str = "";
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		//把担保合同编号拼装成一个String
		for(int i=0;i<GrtGuarContIColl.size();i++){ 
			KeyedCollection kColl = (KeyedCollection)GrtGuarContIColl.get(i); 
			String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
			guar_cont_no_str += "'"+guar_cont_no+"',";
		}
		if(guar_cont_no_str.length()>1){
			guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
		}
		return cmisAgent.getGrtLoanRGur(guar_cont_no_str, connection);       
	}
	
	/**
	 * 通过规则计算合同评分
	 * 合同评分计算（业务类中实现）
     *1.通过传入参数【合同编号】调用客户接口，获取客户所属业务条线
	 *2.获取【存贷总分基数】：调用规则【存贷比总分基数】获取
	 *3.获取【业务模式得分】
	 *（1）业务条线为：公司条线 or 小微条线 时，调用规则【对公业务模式得分】获取
	 *（2）业务条线为：个人条线 时，调用规则【个人业务模式得分】获取
	 *4.获取【利率定价得分】：调用业务类方法【利率定价得分】获取
	 *5.获取【存款贡献度得分】：调用业务类方法【存款贡献度得分】获取
	 *6.合同评分 = 存贷比总分基数 * （业务模式得分 + 利率定价得分 + 存款贡献度得分）
	 * @return
	 */
	public BigDecimal getContNumberByRuleSet(String cont_no) throws Exception {
		logger.info("--------------通过规则计算合同评分 开始---------------");
		BigDecimal cont_number = new BigDecimal("0");
		Map modelMap=new HashMap();
		modelMap.put("IN_合同编号", cont_no);
		Map outMap=new HashMap();
		try {
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			outMap=shuffleService.fireTargetRule("lldj", "lldj_16", modelMap);
			cont_number = new BigDecimal(outMap.get("OUT_合同评分")+"");
		} catch (Exception e1) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "通过规则计算合同评分失败,失败原因:"+e1.getMessage(), null);
			throw new Exception("通过规则计算合同评分失败"+e1.getMessage());
		}
		logger.info("--------------通过规则计算合同评分 结束---------------");
		return cont_number;
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
	public BigDecimal getIntRatPriModeScoreBySerno(String serno) throws Exception{
		BigDecimal intRatPriModeScore = new BigDecimal("0");
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		intRatPriModeScore = cmisAgent.getIntRatPriModeScoreBySerno(serno);
		return intRatPriModeScore;
	}
	public BigDecimal getGuarModeScoreBySerno(String serno) throws Exception {
		BigDecimal guarModeScore = new BigDecimal("0");
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		guarModeScore = cmisAgent.getGuarModeScoreBySerno(serno);
		return guarModeScore;
	}
	/**
	 * 删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMem(Map<String, String> map,Connection connection) throws Exception{
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.removeIqpAppMem(map, connection);
	}
	
	/**
	 *删除入网退网申请删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMemByApp(String serno,Connection connection) throws Exception{
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.removeIqpAppMemByApp(serno, connection);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppMemMana(IndexedCollection iColl,String serno,Connection conn) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppMemMana(iColl, serno, conn);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppOverseeAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppOverseeAgr(iColl, serno, conn, context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDesbuyPlan(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppDesbuyPlan(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppBconCoopAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppBconCoopAgr(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDepotAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppDepotAgr(iColl, serno, conn,context);
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppPsaleCont(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
		IqpLoanAppAgent cmisAgent = (IqpLoanAppAgent)this.getAgentInstance(AppConstant.IQPLOANAPPAGENT);
		return cmisAgent.insertIqpAppPsaleCont(iColl, serno, conn,context);
	}
	
	/**
	 * 贸易融资业务置换额度占用,无间贷、借新还旧额度占用
	 * @param serno     业务流水号
	 * @param risk_open_amt  风险敞口金额
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public BigDecimal caculateLimitSpac(String serno,BigDecimal risk_open_amt) throws Exception{
		logger.info("--------------贸易融资业务置换额度占用,无间贷、借新还旧额度占用 开始---------------");
		BigDecimal res = new BigDecimal(0);
	try{
		//1.使用业务编号，查询业务申请表获取业务申请信息 
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
		KeyedCollection kCollSub = dao.queryDetail("IqpLoanAppSub", serno, this.getConnection());
		String is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
		String loan_form = (String)kCollSub.getDataValue("loan_form");
		String repay_bill = (String)kCollSub.getDataValue("repay_bill");
		String prd_id = (String)kColl.getDataValue("prd_id");
		if(prd_id == null || "".equals(prd_id)){
			KeyedCollection kCollCtr = dao.queryFirst("CtrLoanCont", null, "where serno='"+serno+"'", this.getConnection());
			String cont_no_select = (String)kCollCtr.getDataValue("cont_no");
			KeyedCollection kCollCtrSub = dao.queryDetail("CtrLoanContSub", cont_no_select, this.getConnection());
			is_close_loan = (String)kCollCtrSub.getDataValue("is_close_loan");
			loan_form = (String)kCollCtrSub.getDataValue("loan_form");
			repay_bill = (String)kCollCtrSub.getDataValue("repay_bill");
			prd_id = (String)kCollCtr.getDataValue("prd_id");
		}
		//2.判断【是否无间贷】=“是” OR 【贷款形式】=“借新还旧” 
		if("1".equals(is_close_loan) || "3".equals(loan_form)){
			//2.1计算原借据下敞口余额
			/* modified by yangzy 2014/12/20 融资性担保公司担保占用改造(增加存量过渡数据) start */
			KeyedCollection kCollRes = null;
			if(repay_bill!=null&&!"".equals(repay_bill)){
				kCollRes = (KeyedCollection)SqlClient.queryFirst("oldBillRiskOpenAmt", repay_bill, null, this.getConnection());
			}
			/* modified by yangzy 2014/12/20 融资性担保公司担保占用改造(增加存量过渡数据) end */
			if(kCollRes != null){
				if(kCollRes != null && kCollRes.containsKey("risk_open_amt_bal")){
					BigDecimal risk_open_amt_bal = BigDecimalUtil.replaceNull(kCollRes.getDataValue("risk_open_amt_bal"));
				    //2.2当前业务占用授信额度 = 当前业务占用授信额度 - 原借据下敞口余额 
				    res = risk_open_amt.subtract(risk_open_amt_bal);
				    if(risk_open_amt.compareTo(risk_open_amt_bal)<=0){
				    	res = new BigDecimal(0);
				    }
				}else{
					res = risk_open_amt;
				}
			}else{
				res = risk_open_amt;
			}
		}else{
			res = risk_open_amt;
		}
		logger.info("计算调试，查询lmt_amt： "+res);
		//3.判断【产品代码】 为 “提贷担保” “同业代付”“信托收据贷款” “应收款买入” “汇票贴现” “福费廷” “出口议付”
		//“出口托收贷款”“出口商票融资”
		if("500032".equals(prd_id) || "500020".equals(prd_id) || "500021".equals(prd_id)|| "500028".equals(prd_id)|| "500027".equals(prd_id)|| "500029".equals(prd_id)|| "500026".equals(prd_id)|| "500025".equals(prd_id)|| "500024".equals(prd_id)){
			//3.1获取置换业务借据编号
			KeyedCollection kCollRes = (KeyedCollection)SqlClient.queryFirst("getReplaceBillNO", serno, null, this.getConnection());
			//kCollRes = new KeyedCollection("123");
			//kCollRes.put("rpled_serno", "HT93505010041130800339001");
			if(kCollRes != null){
				String rpled_serno = (String)kCollRes.getDataValue("rpled_serno");
			    if(rpled_serno != null && !"".equals(rpled_serno)){
			    	//3.1.1计算原借据下敞口金额
					KeyedCollection kCollReslt = (KeyedCollection)SqlClient.queryFirst("oldBillRiskOpenAmt", rpled_serno, null, this.getConnection());
				    if(kCollReslt != null && kCollReslt.containsKey("risk_open_amt_bal")){
				    	BigDecimal risk_open_amt_bal = BigDecimalUtil.replaceNull(kCollReslt.getDataValue("risk_open_amt_bal"));
					    //3.1.2当前业务占用授信额度 = 当前业务占用授信额度 - 原借据下敞口余额
					    res = risk_open_amt.subtract(risk_open_amt_bal);
					    if(risk_open_amt.compareTo(risk_open_amt_bal)<=0){
					    	res = new BigDecimal(0);
					    }
				    }
			    }
			}else{
				res = risk_open_amt;
			}
		}
		logger.info("计算调试，查询res结果： "+res);
	  }catch(Exception e){
		 logger.error("贸易融资业务置换额度占用,无间贷、借新还旧额度占用查询失败！"+e.getStackTrace());
		 throw new Exception("贸易融资业务置换额度占用,无间贷、借新还旧额度占用查询失败!");
	  }
	  logger.info("--------------贸易融资业务置换额度占用,无间贷、借新还旧额度占用 结束---------------");
	  return res;
	}
	/**
	 * 贸易融资业务置换额度占用,无间贷、借新还旧额度占用(保函修改、信用证修改授信占用专用)
	 * @param serno     业务流水号
	 * @param risk_open_amt  风险敞口金额
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public BigDecimal caculateLimitSpac4IqpChange(String cont_no,BigDecimal risk_open_amt) throws Exception{
		logger.info("--------------贸易融资业务置换额度占用,无间贷、借新还旧额度占用 开始---------------");
		BigDecimal res = new BigDecimal(0);
	try{
		//1.使用业务编号，查询业务申请表获取业务申请信息 
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		String is_close_loan = "";
		String loan_form = "";
		String repay_bill = "";
		String prd_id = "";
		if(prd_id == null || "".equals(prd_id)){
			KeyedCollection kCollCtr = dao.queryFirst("CtrLoanCont", null, "where cont_no='"+cont_no+"'", this.getConnection());
			String cont_no_select = (String)kCollCtr.getDataValue("cont_no");
			KeyedCollection kCollCtrSub = dao.queryDetail("CtrLoanContSub", cont_no_select, this.getConnection());
			is_close_loan = (String)kCollCtrSub.getDataValue("is_close_loan");
			loan_form = (String)kCollCtrSub.getDataValue("loan_form");
			repay_bill = (String)kCollCtrSub.getDataValue("repay_bill");
			prd_id = (String)kCollCtr.getDataValue("prd_id");
		}
		//2.判断【是否无间贷】=“是” OR 【贷款形式】=“借新还旧” 
		if("1".equals(is_close_loan) || "3".equals(loan_form)){
			//2.1计算原借据下敞口余额
			KeyedCollection kCollRes = (KeyedCollection)SqlClient.queryFirst("oldBillRiskOpenAmt", repay_bill, null, this.getConnection());
			if(kCollRes != null){
				if(kCollRes != null && kCollRes.containsKey("risk_open_amt_bal")){
					BigDecimal risk_open_amt_bal = BigDecimalUtil.replaceNull(kCollRes.getDataValue("risk_open_amt_bal"));
				    //2.2当前业务占用授信额度 = 当前业务占用授信额度 - 原借据下敞口余额 
				    res = risk_open_amt.subtract(risk_open_amt_bal);
				    if(risk_open_amt.compareTo(risk_open_amt_bal)<=0){
				    	res = new BigDecimal(0);
				    }
				}else{
					res = risk_open_amt;
				}
			}else{
				res = risk_open_amt;
			}
		}else{
			res = risk_open_amt;
		}
		logger.info("计算调试，查询res结果： "+res);
	  }catch(Exception e){
		 logger.error("贸易融资业务置换额度占用,无间贷、借新还旧额度占用查询失败！"+e.getStackTrace());
		 throw new Exception("贸易融资业务置换额度占用,无间贷、借新还旧额度占用查询失败!");
	  }
	  logger.info("--------------贸易融资业务置换额度占用,无间贷、借新还旧额度占用 结束---------------");
	  return res;
	}
	/**
	 * 票据明细发生改变时，更改业务申请主表及其从表数据
	 * @param tableModel 表模型
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String updateIqpInfoByBillDetail(String tableModel,String serno,KeyedCollection kColl,TableModelDAO dao) throws Exception {
		/**场景区分 1.直贴业务，引入批次操作,更新贴现从表和业务申请主表信息
		 */
		logger.info("--------------票据明细发生改变时，更改业务申请主表及其从表数据 开始---------------");
		try {
			if("IqpLoanApp".equals(tableModel)){
				//获取业务申请主表数据，贴现从表数据
				KeyedCollection iqpLoanKcoll = dao.queryAllDetail("IqpLoanApp", serno, this.getConnection());
				//KeyedCollection discKcoll = dao.queryAllDetail("IqpDiscApp", serno, this.getConnection());
				//更新业务申请主表信息
				iqpLoanKcoll.put("apply_amount", (String)kColl.getDataValue("bill_total_amt"));//申请金额 
				/**计算敞口金额*/
				BigDecimal apply_amount = BigDecimalUtil.replaceNull(kColl.getDataValue("bill_total_amt"));
				
				//获取实时汇率  start
				String cur_type = (String) iqpLoanKcoll.getDataValue("apply_cur_type");
				KeyedCollection kCollRate = this.getHLByCurrType(cur_type);
				if("failed".equals(kCollRate.getDataValue("flag"))){
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//获取实时汇率  end
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(iqpLoanKcoll.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(iqpLoanKcoll.getDataValue("same_security_amt"));//视同保证金
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				iqpLoanKcoll.put("risk_open_amt", risk_open_amt);
				dao.update(iqpLoanKcoll, this.getConnection());
				KeyedCollection discKcoll = new KeyedCollection("IqpDiscApp");
				//从批次包中取票据数据插入贴现从表中
				discKcoll.put("serno", serno);
				discKcoll.put("disc_date",(String)kColl.getDataValue("fore_disc_date"));//贴现日期
				discKcoll.put("bill_qty", (String)kColl.getDataValue("bill_qnt"));//票据数量
				discKcoll.put("disc_rate", (String)kColl.getDataValue("int_amt"));//贴现利息
				discKcoll.put("net_pay_amt", (String)kColl.getDataValue("rpay_amt"));//实付总金额
				dao.update(discKcoll, this.getConnection());
			}
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "票据明细发生改变时，更改业务申请主表及其从表数据失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("票据明细发生改变时，更改业务申请主表及其从表数据失败!");
		}
		logger.info("--------------票据明细发生改变时，更改业务申请主表及其从表数据 开始---------------");
		return null;
	}
	
	
	/**
	 * 票据明细发生改变时，更改业务业务授信关系信息
	 * @param tableModel 表模型
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String updateIqpLmtRel(String tableModel,String serno,KeyedCollection kColl,TableModelDAO dao) throws Exception {
		logger.info("--------------票据明细发生改变时，更改业务业务授信关系信息 开始---------------");
		try {
			if("IqpLoanApp".equals(tableModel)){
				//获取业务申请主表数据，贴现从表数据
				KeyedCollection iqpLoanKcoll = dao.queryAllDetail("IqpLoanApp", serno, this.getConnection());
				String prd_id = (String)iqpLoanKcoll.getDataValue("prd_id");
				
				KeyedCollection kCollRel =new KeyedCollection();
	        	String lmt_type = "";
	        	if("300021".equals(prd_id)){
	        		//删除关系表数据
					this.deleteLmtRelation(serno);
	        		lmt_type="90";//占用承兑行
	        		String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no in (select a.porder_no from iqp_batch_bill_rel a where a.batch_no in (select b.batch_no from Iqp_Batch_Mng b where b.serno = '"+serno+"'))))";
	        		IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, this.getConnection());
	        		if(iCollCus.size()==0){
	        			throw new Exception("请检查是否录入票据信息或票据承兑行总行行号不存在!");
	        		}
	        		for(int i=0;i<iCollCus.size();i++){
	        			KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
	        			String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
	        			if(null!=cus_id && !"".equals(cus_id)){
	        				 String openDay = (String)this.getContext().getDataValue("OPENDAY");
	        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", this.getConnection());
	        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
	        				 if(agr_no == null || "".equals(agr_no)){
	        					 throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
	        				 }
	        				 KeyedCollection kCollCreditRel = new KeyedCollection("RBusLmtcreditInfo");
	        				 kCollCreditRel.put("agr_no", agr_no);
	        				 kCollCreditRel.put("lmt_type", lmt_type);
	        				 kCollCreditRel.put("serno", serno);
	        				 kCollCreditRel.put("cont_no", "");
	        				 HashMap<String,String> map = new HashMap<String,String>();
	        				 map.put("agr_no", agr_no);
	        				 map.put("serno", serno);
	        				 kCollRel = dao.queryDetail("RBusLmtcreditInfo", map, this.getConnection());
	        				 String selectSerno = (String)kCollRel.getDataValue("serno");
	        				 if(selectSerno == null || "".equals(selectSerno)){
	        					 dao.insert(kCollCreditRel, this.getConnection());
	        				 }else{
	        					 dao.update(kCollCreditRel, this.getConnection());
	        				 }
	        			}else{
	        				throw new EMPException("承兑行总行行号不存在!");
	        			}
	        		}
	        	}
			}else if("IqpRpddscnt".equals(tableModel)){
				KeyedCollection kCollIqpRpddscnt = dao.queryDetail(tableModel, serno, this.getConnection());
				String limit_ind = (String) kCollIqpRpddscnt.getDataValue("limit_ind");
				String rpddscnt_type = (String) kCollIqpRpddscnt.getDataValue("rpddscnt_type");//转贴现方式
				KeyedCollection kCollRel =new KeyedCollection();
				if("4".equals(limit_ind)){
					if("01".equals(rpddscnt_type) || "04".equals(rpddscnt_type)){//!'01':'买入买断', !'04':'卖出回购'
		        		//删除关系表数据
						this.deleteLmtRelation(serno);
		        		String lmt_type="90";//占用承兑行
		        		String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no in (select a.porder_no from iqp_batch_bill_rel a where a.batch_no in (select b.batch_no from Iqp_Batch_Mng b where b.serno = '"+serno+"'))))";
		        		IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, this.getConnection());
		        		if(iCollCus.size()==0){
		        			throw new Exception("请检查是否录入票据信息或票据承兑行总行行号不存在!");
		        		}
		        		for(int i=0;i<iCollCus.size();i++){
		        			KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
		        			String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
		        			if(null!=cus_id && !"".equals(cus_id)){
		        				 String openDay = (String)this.getContext().getDataValue("OPENDAY");
		        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", this.getConnection());
		        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
		        				 if(agr_no == null || "".equals(agr_no)){
		        					 throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
		        				 }
		        				 KeyedCollection kCollCreditRel = new KeyedCollection("RBusLmtcreditInfo");
		        				 kCollCreditRel.put("agr_no", agr_no);
		        				 kCollCreditRel.put("lmt_type", lmt_type);
		        				 kCollCreditRel.put("serno", serno);
		        				 kCollCreditRel.put("cont_no", "");
		        				 HashMap<String,String> map = new HashMap<String,String>();
		        				 map.put("agr_no", agr_no);
		        				 map.put("serno", serno);
		        				 kCollRel = dao.queryDetail("RBusLmtcreditInfo", map, this.getConnection());
		        				 String selectSerno = (String)kCollRel.getDataValue("serno");
		        				 if(selectSerno == null || "".equals(selectSerno)){
		        					 dao.insert(kCollCreditRel, this.getConnection());
		        				 }else{
		        					 dao.update(kCollCreditRel, this.getConnection());
		        				 }
		        			}else{
		        				throw new EMPException("承兑行总行行号不存在!");
		        			}
		        		}
					}
				}
			}
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "票据明细发生改变时，更改业务业务授信关系信息失败,失败原因:"+e.getMessage(), null);
			throw new EMPException(e);
		}
		logger.info("--------------票据明细发生改变时，更改业务业务授信关系信息 结束---------------");
		return null;
	}
	
	/**
	 * 剔除票据明细时，删除业务授信关系表
	 * @param tableModel 表模型
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String deleteIqpLmtRel(String tableModel,String serno,String porder_no,TableModelDAO dao) throws Exception {
		logger.info("--------------剔除票据明细时，删除业务授信关系表 开始---------------");
		try {
			if("IqpLoanApp".equals(tableModel)){
				//获取业务申请主表数据，贴现从表数据
				KeyedCollection iqpLoanKcoll = dao.queryAllDetail("IqpLoanApp", serno, this.getConnection());
				String prd_id = (String)iqpLoanKcoll.getDataValue("prd_id");
				
				if("300021".equals(prd_id)){
					String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no ='"+porder_no+"'))";
					IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, this.getConnection());
					if(iCollCus.size()==0){
						throw new Exception("请检查是否录入票据信息或票据承兑行总行行号不存在!");
					}
					for(int i=0;i<iCollCus.size();i++){
						KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
						String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
						if(null!=cus_id && !"".equals(cus_id)){
							 String openDay = (String)this.getContext().getDataValue("OPENDAY");
	        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", this.getConnection());
	        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
							if(agr_no == null || "".equals(agr_no)){
								throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
							}
							HashMap<String,String> map = new HashMap<String,String>();
							map.put("agr_no", agr_no);
							map.put("serno", serno);
							dao.deleteAllByPks("RBusLmtcreditInfo", map, this.getConnection());
						}else{
							throw new EMPException("承兑行总行行号不存在!");
						}
					}
				}
			}else if("IqpRpddscnt".equals(tableModel)){
				KeyedCollection kCollIqpRpddscnt = dao.queryDetail(tableModel, serno, this.getConnection());
				String limit_ind = (String) kCollIqpRpddscnt.getDataValue("limit_ind");
				String rpddscnt_type = (String) kCollIqpRpddscnt.getDataValue("rpddscnt_type");//转贴现方式
				if("4".equals(limit_ind)){
					if("01".equals(rpddscnt_type) || "04".equals(rpddscnt_type)){//!'01':'买入买断', !'04':'卖出回购'
						String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no ='"+porder_no+"'))";
						IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, this.getConnection());
						if(iCollCus.size()==0){
							throw new EMPException("请检查是否录入票据信息或票据承兑行总行行号不存在!");
						}
						for(int i=0;i<iCollCus.size();i++){
							KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
							String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
							if(null!=cus_id && !"".equals(cus_id)){
								 String openDay = (String)this.getContext().getDataValue("OPENDAY");
		        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", this.getConnection());
		        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
								if(agr_no == null || "".equals(agr_no)){
									throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
								}
								HashMap<String,String> map = new HashMap<String,String>();
								map.put("agr_no", agr_no);
								map.put("serno", serno);
								dao.deleteAllByPks("RBusLmtcreditInfo", map, this.getConnection());
							}else{
								throw new EMPException("承兑行总行行号不存在!");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "剔除票据明细时，删除业务授信关系表失败,失败原因:"+e.getMessage(), null);
			throw new EMPException(e);
		}
		logger.info("--------------剔除票据明细时，删除业务授信关系表 结束---------------");
		return null;
	}
	
	/**
	 * 通过客户码统计该客户的风险敞口总额
	 * cus_id  客户码 
	 * @return 风险敞口总额
	 * @throws Exception 
	 */
	public BigDecimal getRiskOpenAmtByCusId(String cus_id,String belong_net,Context context,Connection connection) throws Exception{
		logger.info("---------------通过客户码统计该客户的风险敞口总额 开始---------------");
		BigDecimal risk_open_amt = new BigDecimal(0.00);
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		try{
			/**第一步，计算未生成合同部分风险敞口，即申请时风险敞口*/
			String iqpCondition = " where cus_id='"+cus_id+"' and belong_net='"+belong_net+"' and biz_type not in('7','8') and approve_status in('000','111','992','993') ";
			IndexedCollection iqpIColl = dao.queryList("IqpLoanApp", iqpCondition, connection);
			KeyedCollection iqpkc = null;
			String serno = "";
			for(int i=0;i<iqpIColl.size();i++){
				iqpkc = (KeyedCollection) iqpIColl.get(i);
				serno = (String) iqpkc.getDataValue("serno");
				risk_open_amt = risk_open_amt.add(this.getLmtAmtBySerno(serno));//查询该申请下风险敞口，并循环累加
			}
			/**第二步，计算有合同部分风险敞口*/
			String condition = " where cus_id='"+cus_id+"' and belong_net='"+belong_net+"' and biz_type not in('7','8') ";
			IndexedCollection iColl = dao.queryList("CtrLoanCont", condition, connection);
			KeyedCollection kc = null;
			String cont_no = "";
			for(int i=0;i<iColl.size();i++){
				kc = (KeyedCollection) iColl.get(i);
				cont_no = (String) kc.getDataValue("cont_no");
				risk_open_amt = risk_open_amt.add(this.getOneLmtAmtByContNo(cont_no));
			}
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "通过客户码统计该客户的风险敞口总额失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("通过客户码统计该客户的风险敞口总额失败!原因:"+e.getMessage());
		}
		logger.info("---------------通过客户码统计该客户的风险敞口总额 结束---------------");
		return risk_open_amt;
     }
	
	/**
	 * 通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数
	 * guar_cont_no  担保合同编号 
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public IndexedCollection getGuarBailMultipleByGrtNo(String guar_cont_no,DataSource dataSource) throws Exception{
		logger.info("---------------通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数 开始---------------");
		IndexedCollection res_value = new IndexedCollection();
		try{
			String sql_select = "select cus_com.Guar_Bail_Multiple from cus_com cus_com,cus_base cus_base where cus_com.cus_id=cus_base.cus_id and cus_base.cus_type='A2' and cus_com.cus_id in(select grt.cus_id from grt_guarantee grt where grt.guar_id in(select grtRe.Guaranty_Id from grt_guaranty_re grtRe where grtRe.Guar_Cont_No='"+guar_cont_no+"'))";
			res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数!原因:"+e.getMessage());
		}
		logger.info("---------------通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数 结束---------------");
		return res_value;
	}
	
	/**
	 * 根据客户号获取该客户下所有使用单一法人授信的授信占用(供查询集团客户下使用，因为：去除低风险的授信占用)。
	 * @param cus_id 客户码
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getLmtAmtByCusId4Grp(String cus_id,Context context,Connection connection) throws Exception {
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用(供查询集团客户下使用，因为：去除低风险的授信占用) 开始---------------");
		BigDecimal lmt_amt = new BigDecimal("0");
		try{
			
			/**第一步，获取所有需要统计的合同（使用循环额度）
			 * 1生效或未生效状态合同
			 * 2在关系表中存在
			 * */
			IndexedCollection contiColl = SqlClient.queryList4IColl("queryContInfoForLmtByCusId", cus_id, connection);
			
			/**第二步，统计所有合同的授信占用*/
			if(contiColl.size()>0){
				KeyedCollection contkColl = new KeyedCollection();
				for(int i=0;i<contiColl.size();i++){
					contkColl = (KeyedCollection) contiColl.get(i);
					String cont_no = (String) contkColl.getDataValue("cont_no");
					String flag = this.getIsLrisk(null, cont_no, connection, context);
					if(!"can".equals(flag)){
						lmt_amt = lmt_amt.add(this.getLmtAmtByContNo(cont_no));
					}
				}
			}
			
			/**增加第三步，获取所有需要统计的合同（使用一次性额度）
			 * 1除撤销状态合同
			 * 2在关系表中存在
			 * */
			IndexedCollection onecontiColl = SqlClient.queryList4IColl("queryContInfoForOneLmtByCusId", cus_id, connection);
			
			/**增加第四步，统计所有合同的授信占用*/
			if(onecontiColl.size()>0){
				KeyedCollection onecontkColl = new KeyedCollection();
				for(int i=0;i<onecontiColl.size();i++){
					onecontkColl = (KeyedCollection) onecontiColl.get(i);
					String cont_no = (String) onecontkColl.getDataValue("cont_no");
					String flag = this.getIsLrisk(null, cont_no, connection, context);
					if(!"can".equals(flag)){
						lmt_amt = lmt_amt.add(this.getOneLmtAmtByContNo(cont_no));
					}
				}
			}
			
			/**第三步，获取所有需要统计的申请
			 * 1在关系表中存在
			 * 2关系表中合同编号为空
			 * */
			IndexedCollection iqpIColl = SqlClient.queryList4IColl("queryIqpInfoForLmtByCusId", cus_id, connection);
			
			/**第四步，统计所有申请的授信占用*/
			if(iqpIColl.size()>0){
				KeyedCollection iqpkColl = new KeyedCollection();
				for(int i=0;i<iqpIColl.size();i++){
					iqpkColl = (KeyedCollection) iqpIColl.get(i);
					String serno = (String) iqpkColl.getDataValue("serno");
					String flag = this.getIsLrisk(serno, null, connection, context);
					if(!"can".equals(flag)){
						lmt_amt = lmt_amt.add(this.getLmtAmtBySerno(serno));
					}
				}
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据客户号获取该客户下所有使用单一法人授信的授信占用失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据客户号获取该客户下所有使用单一法人授信的授信占用失败!");
		}
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用(供查询集团客户下使用，因为：去除低风险的授信占用) 结束---------------");
		logger.info("---------------根据客户号获取该客户下所有使用单一法人授信的授信占用(供查询集团客户下使用，因为：去除低风险的授信占用)客户："+cus_id+"授信占用： "+lmt_amt+"---------------");
		return lmt_amt;
	}
	/**
	 * 根据合同号或者业务编号查询使用的授信是否为低风险授信或联保授信
	 * @param cont_no 
	 * @param serno 
	 * @return
	 * @throws Exception
	 */
	public String getIsLrisk(String serno,String cont_no,Connection connection ,Context context) throws Exception {
		logger.info("---------------根据合同号或者业务编号查询使用的授信是否为低风险授信或联保授信 开始---------------");
		String flag=null;
		String condition = null;
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			//如果业务流水不为空，根据serno查询关系表，得到授信协议号,查询是否为低风险授信
			//如果合同编号不为空,根据cont_no查询关系表，得到授信协议号,查询是否为低风险授信
			if(serno != null && !"".equals(serno)){
				condition = " where serno = '"+serno+"' and cont_no is null ";
			}else if(cont_no != null && !"".equals(cont_no)){
				condition = " where cont_no = '"+cont_no+"'";
			}
				
        	String modelId = "RBusLmtInfo";
			IndexedCollection iqpIColl = dao.queryList(modelId, condition, connection);
			if(iqpIColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)iqpIColl.get(0);
				String agr_no = (String)kColl.getDataValue("agr_no");
				String str = "'"+agr_no+"'";
				IndexedCollection lmtIColl = lmtServiceInterface.queryLmtAgrDetailsByLimitCodeStr(str, null, dataSource);
				if(lmtIColl.size()>0){
				    KeyedCollection kCollLmt = (KeyedCollection)lmtIColl.get(0);
				    String lrisk_type = (String)kCollLmt.getDataValue("lrisk_type");
				    String sub_type = (String)kCollLmt.getDataValue("sub_type");
				    if("10".equals(lrisk_type) || "03".equals(sub_type)){
				    	flag = "can";
				    }else{
				    	flag = "canNot";
				    }
				}
			}
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同号或者业务编号查询使用的授信是否为低风险授信失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("根据合同号或者业务编号查询使用的授信是否为低风险授信!");
		}
		logger.info("---------------根据合同号或者业务编号查询使用的授信是否为低风险授信或联保授信 结束---------------");
		return flag;
	}
	/**
	 * 通过日期取下一个工作日
	 * cur_date  当前日期
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public String getNextWorkDate(String cur_date,DataSource dataSource) throws Exception{
		logger.info("---------------通过日期取下一个工作日开始---------------");
		IndexedCollection iColl = new IndexedCollection();
		String nextWordDate = "";
		try{
			if(cur_date != null && !"".equals(cur_date)){
				cur_date = cur_date.replaceAll("-", "");
				String sql_select = "select case when  wf.workdayflg='N' then TO_CHAR(TO_DATE(wf.nextworkdate,'yyyy-mm-dd'),'yyyy-mm-dd') else TO_CHAR(TO_DATE(wf.curdate,'yyyy-mm-dd'),'yyyy-mm-dd') end as nextworkdate from wf_freedate wf where wf.curdate='"+cur_date+"'";
				iColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
				if(iColl.size()>0){
				   KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				   nextWordDate = (String)kColl.getDataValue("nextworkdate");
				}else{
					throw new Exception("未初始化本年的节假日,请先初始化！");
				}
			}
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "通过日期取下一个工作日失败,失败原因:"+e.getMessage(), null);
			e.printStackTrace();
			throw new Exception("通过日期取下一个工作日失败!原因:"+e.getMessage());
		}
		logger.info("---------------通过日期取下一个工作日结束---------------");
		return nextWordDate;
	}
	
	/**
	 * 计算结果进百
	 * 需计算的金额  
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public BigDecimal carryCurrency(BigDecimal securityAmt) throws EMPException{
		logger.info("---------------计算结果进百开始---------------");
		try{
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			String caculateAmt = String.valueOf(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
			String changeAmt = nf.format(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(changeAmt);
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "计算结果进百计算失败,失败原因:"+e.getMessage(), null);
			throw new EMPException("计算结果进百计算失败!原因:"+e.getMessage());
		}
		logger.info("---------------计算结果进百结束---------------");
		return securityAmt;
	}
	
	
	/**
	 * 根据合同编号查询属于哪张合同表
	 * cont_no 合同编号
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public String getCtrName(String cont_no) throws EMPException{
		logger.info("---------------根据合同编号查询属于哪张合同表开始---------------");
		String tableName = "";
		try{
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			String sql_select = "select 'CtrLoanCont' as tablename from Ctr_Loan_Cont ctr where ctr.cont_no='"+cont_no+"' union all " +
					     "select 'CtrAssetstrsfCont' as tablename from Ctr_Assetstrsf_Cont ctrAss where ctrAss.cont_no='"+cont_no+"' union all " +
					     "select 'CtrRpddscntCont' as tablename from Ctr_Rpddscnt_Cont ctrRp where ctrRp.cont_no='"+cont_no+"' union all " +
					     "select 'CtrAssetTransCont' as tablename from Ctr_Asset_Trans_Cont ctrAssTr where ctrAssTr.cont_no='"+cont_no+"' union all " +
					     "select 'CtrAssetProCont' as tablename from Ctr_Asset_Pro_Cont ctrPro where ctrPro.cont_no='"+cont_no+"'";
			IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				tableName = (String)kColl.getDataValue("tablename");
			}
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据合同编号查询属于哪张合同表失败,失败原因:"+e.getMessage(), null);
			throw new EMPException("根据合同编号查询属于哪张合同表失败!原因:"+e.getMessage());
		}
		logger.info("---------------根据合同编号查询属于哪张合同表结束---------------");
		return tableName;
	}
	
	/**
	 * 根据业务编号查询属于哪张业务表
	 * serno 业务编号
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public String getIqpName(String serno) throws EMPException{
		logger.info("---------------根据业务编号查询属于哪张业务表开始---------------");
		String tableName = "";
		try{
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			String sql_select = "select 'IqpLoanCont' as tablename from Iqp_Loan_App loan where loan.serno='"+serno+"' union all " +
			"select 'IqpCreditChangeApp' as tablename from Iqp_Credit_Change_App creditChange where creditChange.serno='"+serno+"' union all " +
			"select 'IqpGuarantChangeApp' as tablename from Iqp_Guarant_Change_App guarantChange where guarantChange.serno='"+serno+"' union all " +
			"select 'IqpGuarChangeApp' as tablename from Iqp_Guar_Change_App guarChange where guarChange.serno='"+serno+"'";
			IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				tableName = (String)kColl.getDataValue("tablename");
			}
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "根据业务编号查询属于哪张业务表失败,失败原因:"+e.getMessage(), null);
			throw new EMPException("根据业务编号查询属于哪张业务表失败!原因:"+e.getMessage());
		}
		logger.info("---------------根据业务编号查询属于哪张业务表结束---------------");
		return tableName;
	}
	
	/**
	 * 资产流转签订的时候，选出资产清单中（根据借据编号）查询出台账到期日最大的一笔
	 * serno 业务编号
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public String getMaxDate4CtrAssetTrans(String serno) throws EMPException{
		logger.info("---------------根据业务编号查询属于哪张业务表开始---------------");
		String date = "";
		try{
			DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
			String sql_select = "select  max(a.end_date) as end_date from acc_loan a where a.bill_no "+
                                "in(select a.bill_no from Iqp_Asset_Trans_List a where a.serno='"+serno+"')";
			IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				date = (String)kColl.getDataValue("end_date");
			}
		}catch (Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "资产流转签订的时候，选出资产清单中（根据借据编号）查询出台账到期日最大的一笔失败,失败原因:"+e.getMessage(), null);
			throw new EMPException("资产流转签订的时候，选出资产清单中（根据借据编号）查询出台账到期日最大的一笔失败!原因:"+e.getMessage());
		}
		logger.info("---------------资产流转签订的时候，选出资产清单中（根据借据编号）查询出台账到期日最大的一笔结束---------------");
		return date;
	}
	
	/**
	 * 贸易融资业务置换额度占用,无间贷、借新还旧担保占用
	 * @param serno     业务流水号
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String caculateGuarAmtSp(String serno,String cont_no,String pk_value) throws Exception{
		logger.info("---------------贸易融资业务置换额度占用,无间贷、借新还旧担保占用  开始---------------");
		String res = "";
	try{
		//
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		String is_close_loan = "";
		String loan_form = "";
		String repay_bill = "";
		String prd_id = "";
		if(serno != null && !"".equals(serno)){
			KeyedCollection kColl = dao.queryDetail("IqpLoanApp", serno, this.getConnection());
			KeyedCollection kCollSub = dao.queryDetail("IqpLoanAppSub", serno, this.getConnection());
			is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
			loan_form = (String)kCollSub.getDataValue("loan_form");
			repay_bill = (String)kCollSub.getDataValue("repay_bill");
			prd_id = (String)kColl.getDataValue("prd_id");
		}else{
			KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			KeyedCollection kCollCtrSub = dao.queryDetail("CtrLoanContSub", cont_no, this.getConnection());
			is_close_loan = (String)kCollCtrSub.getDataValue("is_close_loan");
			loan_form = (String)kCollCtrSub.getDataValue("loan_form");
			repay_bill = (String)kCollCtrSub.getDataValue("repay_bill");
			prd_id = (String)kCollCtr.getDataValue("prd_id");
		}
		
		//2.判断【是否无间贷】=“是” OR 【贷款形式】=“借新还旧” 
		if("1".equals(is_close_loan) || "3".equals(loan_form)){
			if(!"".equals(repay_bill) && repay_bill != null){
				String condition = "where bill_no='"+repay_bill+"'";
				KeyedCollection kCollAcc = dao.queryFirst("AccView",null, condition, this.getConnection());
				String status = (String)kCollAcc.getDataValue("status");
				//原借据状态不为'8','9','10','11'
				//'0':'出帐未确认', '1':'正常', '10':'闭卷', '11':'撤销', '2':'正回购卖出', '3':'逆回购买入', '4':'逆回购到期', '5':'正回购到期', '6':'垫款', '7':'已扣款', '8':'退回未用', '9':'结清/核销'
				if(!"8".equals(status) && !"9".equals(status) && !"10".equals(status) && !"11".equals(status)){
					//再判断本笔业务借据状态
					if(!"".equals(cont_no) && cont_no != null){
						KeyedCollection kCollAccThis = dao.queryFirst("AccView",null, "where cont_no='"+cont_no+"'", this.getConnection());
						String statesThis = (String)kCollAccThis.getDataValue("status");
						if(!"0".equals(statesThis) && !"8".equals(statesThis) && !"9".equals(statesThis) && !"10".equals(statesThis) && !"11".equals(statesThis)){
							res = "2";//占用自己担保
						}else{
							
							res = "1";//占用原借据担保
						}
					}else{
						
						res = "1";//占用原借据担保
					}
				}else{
					res = "2";//占用自己担保
				}
			}
		}else{
			res = "2";//占用自己担保
		}
		logger.info("计算调试： "+res);
		//3.判断【产品代码】 为 “提贷担保” “同业代付”“信托收据贷款” “应收款买入” “汇票贴现” “福费廷” “出口议付”
		//“出口托收贷款”“出口商票融资”
		if("500032".equals(prd_id) || "500020".equals(prd_id) || "500021".equals(prd_id)|| "500028".equals(prd_id)|| "500027".equals(prd_id)|| "500029".equals(prd_id)|| "500026".equals(prd_id)|| "500025".equals(prd_id)|| "500024".equals(prd_id)){
			//3.1获取置换业务借据编号
			if(cont_no != null && !"".equals(cont_no)){
				KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
				serno = (String)kCollCtr.getDataValue("serno");
			}
			KeyedCollection kCollRes = (KeyedCollection)SqlClient.queryFirst("getReplaceBillNO", serno, null, this.getConnection());
			if(kCollRes != null){
				String rpled_serno = (String)kCollRes.getDataValue("rpled_serno");
			    if(rpled_serno != null && !"".equals(rpled_serno)){
			    	String condition = "where bill_no='"+rpled_serno+"'";
					KeyedCollection kCollAcc = dao.queryFirst("AccView",null, condition, this.getConnection());
					String status = (String)kCollAcc.getDataValue("status");
					if(!"8".equals(status) && !"9".equals(status) && !"10".equals(status) && !"11".equals(status)){
						//再判断本笔业务借据状态
						if(!"".equals(cont_no) && cont_no != null){
							KeyedCollection kCollAccThis = dao.queryFirst("AccView",null, "where cont_no='"+cont_no+"'", this.getConnection());
							String statesThis = (String)kCollAccThis.getDataValue("status");
							if(!"0".equals(statesThis) && !"8".equals(statesThis) && !"9".equals(statesThis) && !"10".equals(statesThis) && !"11".equals(statesThis)){
								res = "2";//占用自己担保
							}else{
								KeyedCollection kCollGrtLoan = dao.queryDetail("GrtLoanRGur", pk_value, this.getConnection());
								BigDecimal guar_amt = BigDecimalUtil.replaceNull(kCollGrtLoan.getDataValue("guar_amt"));
								String guar_cont_no = (String)kCollGrtLoan.getDataValue("guar_cont_no");
								String cont_no_select = (String)kCollAcc.getDataValue("cont_no");
								String conditionStr = "where guar_cont_no='"+guar_cont_no+"' and cont_no='"+cont_no_select+"' and corre_rel='1'";
								KeyedCollection kCollGrt = dao.queryFirst("GrtLoanRGur", null, conditionStr, this.getConnection());
								BigDecimal guar_amt_rep = BigDecimalUtil.replaceNull(kCollGrt.getDataValue("guar_amt"));
								if(guar_amt.compareTo(guar_amt_rep)>=0){
									res = "2";//占用原借据担保
								}else{
									res = "1";//占用原借据担保
								}
							}
						}else{
							KeyedCollection kCollGrtLoan = dao.queryDetail("GrtLoanRGur", pk_value, this.getConnection());
							BigDecimal guar_amt = BigDecimalUtil.replaceNull(kCollGrtLoan.getDataValue("guar_amt"));
							String guar_cont_no = (String)kCollGrtLoan.getDataValue("guar_cont_no");
							String cont_no_select = (String)kCollAcc.getDataValue("cont_no");
							String conditionStr = "where guar_cont_no='"+guar_cont_no+"' and cont_no='"+cont_no_select+"' and corre_rel='1'";
							KeyedCollection kCollGrt = dao.queryFirst("GrtLoanRGur", null, conditionStr, this.getConnection());
							BigDecimal guar_amt_rep = BigDecimalUtil.replaceNull(kCollGrt.getDataValue("guar_amt"));
							if(guar_amt.compareTo(guar_amt_rep)>=0){
								res = "2";//占用原借据担保
							}else{
								res = "1";//占用原借据担保
							}
						}
					}else{
						res = "2";
					}
			    }
			}else{
				res = "2";
			}
		}
		logger.info("贸易融资业务置换额度占用,无间贷、借新还旧担保占用，查询res结果： "+res);
	  }catch(Exception e){
		  EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "贸易融资业务置换额度占用,无间贷、借新还旧担保占用失败,失败原因:"+e.getMessage(), null);
		 throw new Exception("贸易融资业务置换额度占用,无间贷、借新还旧担保占用查询失败!");
	  }
	  return res;
	}
}
