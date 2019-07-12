package com.yucheng.cmis.biz01line.iqp.component;

import java.math.BigDecimal;

import javax.sql.DataSource;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.agent.IqpBailAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class IqpBailComponent extends CMISComponent {
	
	private static final String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
	private static final String IQPBAILSUBDISMODEL = "IqpBailSubDis";//保证金变更申请主表
	private static final String IQPBAILSUBDISDETAILMODEL = "IqpBailSubDisDetail";//保证金变更申请从表
	private static final String PUBBAILINFOMODEL = "PubBailInfo";//保证金明细表
	
	/**
	 * 查询所有已生效的合同
	 * @param pageInfo 分页信息
	 * @return returnIColl已生效的合同信息
	 * @throws Exception
	 */
	public IndexedCollection queryPubBailInfoList(String conditionStr,String condition,PageInfo pageInfo,DataSource dataSource)  throws Exception {
		IqpBailAgent iqpBailAgent = (IqpBailAgent)this.getAgentInstance(AppConstant.IQPBAILAGENT);
		return iqpBailAgent.queryPubBailInfoList(conditionStr,condition,pageInfo,dataSource);
	}
	
	
	/**
	 * 根据合同编号获取保证金金额（保证金金额=合同金额*保证金比例）
	 * @param cont_no 合同编号
	 * @return returnKColl保证金比例和保证金金额kc
	 * @throws Exception
	 */
	public KeyedCollection getBailAmtByContNo(String cont_no)  throws Exception {
		// TODO Auto-generated method stub
		IqpBailAgent iqpBailAgent = (IqpBailAgent)this.getAgentInstance(AppConstant.IQPBAILAGENT);
		return iqpBailAgent.getBailAmtByContNo(cont_no);
	}
	/**
	 * 根据业务编号删除保证金追加/提取申请信息及其明细
	 * @param serno 合同编号
	 * @return result删除的记录数
	 * @throws Exception
	 */
	public int deleteBailAppBySerno(String serno) throws Exception{
		// TODO Auto-generated method stub
		IqpBailAgent iqpBailAgent = (IqpBailAgent)this.getAgentInstance(AppConstant.IQPBAILAGENT);
		return iqpBailAgent.deleteBailAppBySerno(serno);
	}
	
	public void doWfAgreeForBail(String serno)throws ComponentException {
		try {
			/**
			 * 权证出入库申请审批通过执行操作：
			 * 1.生成授权信息,注：授权表中交易码由：【交易码+交易场景】组成
			 */
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			/** 1.通过业务流水号查询出生成保证金变更授权所需要的字段*/
			KeyedCollection iqpbailsubdisColl = dao.queryDetail(IQPBAILSUBDISMODEL, serno, this.getConnection());
			
			String add_flag = (String)iqpbailsubdisColl.getDataValue("addflag");//保证金变更类型：1增加 2追加
			String tran_id = TradeConstance.SERVICE_CODE_BZJBG + TradeConstance.SERVICE_SCENE_BZJBG;//交易码
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//授权编号
			String input_br_id=(String)iqpbailsubdisColl.getDataValue("input_br_id");//机构代码
			String adj_bail_perc = (String)iqpbailsubdisColl.getDataValue("adjusted_bail_perc");//合同保证金比例
			String adj_bail_amt = (String)iqpbailsubdisColl.getDataValue("adjusted_bail_amt");//合同保证金金额
			String adjust_amt = (String)iqpbailsubdisColl.getDataValue("adjust_bail_amt");//账号保证金金额
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//交易流水号
			
			String contract_no = "";//合同号
			String bail_acct_no = "";//保证金账号
			String acct_type = "";//账户类型
			String gl_code = "";//账号科目代码
			String acct_name = "";//户名
			String cur_type = "";//币种
			
			if("1".equals(add_flag)){//增加
				//根据申请号查询保证金详细信息
				String condition = " where serno='"+serno+"'";
				KeyedCollection kCollBailInfo = dao.queryFirst(PUBBAILINFOMODEL, null, condition, this.getConnection());
				bail_acct_no = (String)kCollBailInfo.getDataValue("bail_acct_no");//保证金账号
				acct_type = (String)kCollBailInfo.getDataValue("bail_type");//账户类型
				gl_code = (String)kCollBailInfo.getDataValue("bail_acct_gl_code");//账号科目代码
				acct_name = (String)kCollBailInfo.getDataValue("bail_acct_name");//户名
				cur_type = (String)kCollBailInfo.getDataValue("cur_type");//币种
				contract_no = (String)iqpbailsubdisColl.getDataValue("cont_no");//合同号
			}else{//追加
				KeyedCollection disDetColl = dao.queryFirst(IQPBAILSUBDISDETAILMODEL, null, "where serno='"+serno+"'", this.getConnection());
				contract_no = (String)disDetColl.getDataValue("cont_no");//合同号
				bail_acct_no = (String)disDetColl.getDataValue("bail_acct_no");//保证金账号
				//根据保证金账号查询保证金详细信息
				String condition = " where bail_acct_no='"+bail_acct_no+"'";
				KeyedCollection kCollBailInfo = dao.queryFirst(PUBBAILINFOMODEL, null, condition, this.getConnection());
				acct_type = (String)kCollBailInfo.getDataValue("bail_type");//账户类型
				gl_code = (String)kCollBailInfo.getDataValue("bail_acct_gl_code");//账号科目代码
				acct_name = (String)kCollBailInfo.getDataValue("bail_acct_name");//户名
				cur_type = (String)kCollBailInfo.getDataValue("cur_type");//币种
			}
			
			authorizeKColl.put("serno",serno);//此处是追加申请流水号，非原业务流水号
			authorizeKColl.put("authorize_no",authSerno);//授权编号
			authorizeKColl.put("tran_id",tran_id);//交易码
			authorizeKColl.put("tran_serno",tranSerno);  //交易流水号
			authorizeKColl.put("cont_no",contract_no);  //合同号
			authorizeKColl.put("fldvalue01","GEN_GL_NO@"+authSerno);//授权编号
			authorizeKColl.put("fldvalue02","CONTRACT_NO@"+contract_no);//合同号
			authorizeKColl.put("fldvalue03","DUEBILL_NO@"+"");//借据号
			authorizeKColl.put("fldvalue04","BRANCH_ID@"+input_br_id);//授权编号
			authorizeKColl.put("fldvalue05","GUARANTEE_ACCT_NO@"+bail_acct_no);//保证金账号
			authorizeKColl.put("fldvalue06","GUARANTEE_NO@"+"");  //保证金编号
			authorizeKColl.put("fldvalue07","ACCT_TYPE@"+acct_type);  //账户类型
			authorizeKColl.put("fldvalue08","GUARANTEE_EXPIRY_DATE@"+"");  //保证金到期日
			authorizeKColl.put("fldvalue09","ACCT_CODE@"+"");  //账户代码
			authorizeKColl.put("fldvalue10","CA_TT_FLAG@"+"");  //钞汇标志
			authorizeKColl.put("fldvalue11","ACCT_GL_CODE@"+gl_code);  //账号科目代码
			authorizeKColl.put("fldvalue12","ACCT_NAME@"+acct_name);  //户名
			authorizeKColl.put("fldvalue13","CCY@"+cur_type);  //币种
			authorizeKColl.put("fldvalue14","CONTRACT_GUARANTEE_PER@"+adj_bail_perc);  //合同保证金比例
			authorizeKColl.put("fldvalue15","CONTRACT_GUARANTEE_AMT@"+adj_bail_amt);  //合同保证金金额
			authorizeKColl.put("fldvalue16","ACCT_GUARANTEE_AMT@"+adjust_amt);  //账号保证金金额
			authorizeKColl.put("fldvalue17","GUARANTEE_MOD_TYPE@"+add_flag);  //保证金变更类型
			/** 抽取发往核算系统的授权信息--end-- */
			dao.insert(authorizeKColl, this.getConnection());
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	}
	
	/**
	 * 获取保证金占用公用方法（仅统计生成合同后）
	 * @param acctNo
	 * @throws ComponentException
	 */
	public BigDecimal getBailAmtByAcctNo4Ctr(String acctNo)throws ComponentException {
		BigDecimal bailAmt = new BigDecimal(0.0);
		try {
			BigDecimal nomalAmt = this.getNomalBailAmtByAcctNo4Ctr(acctNo);
			BigDecimal LbAmt = this.getLbBailAmtByAcctNo4Ctr(acctNo);
			BigDecimal FinAmt = this.getFinBailAmtByAcctNo4Ctr(acctNo);
			BigDecimal backAmt = this.getBackBailAmtByAcctNo4Ctr(acctNo);
			bailAmt = nomalAmt.add(LbAmt).add(FinAmt).add(backAmt);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("获取保证金占用失败："+e.getMessage());
		}
		return bailAmt;
	}
	
	/**
	 * 获取普通业务保证金占用（仅统计生成合同后）
	 * @param acctNo
	 * @throws ComponentException
	 */
	public BigDecimal getNomalBailAmtByAcctNo4Ctr(String acctNo)throws ComponentException {
		BigDecimal nomalAmt = new BigDecimal(0.0);
		try {
			/**1、普通业务保证金占用（仅能录入一笔保证金）*/
			KeyedCollection nomalkColl = (KeyedCollection) SqlClient.queryFirst("getOutStndAmt", acctNo, null, this.getConnection());
			nomalAmt = (BigDecimal)nomalkColl.getDataValue("totlamt");
			if(nomalAmt==null||"".equals(nomalAmt)){
				nomalAmt = new BigDecimal(0.0);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("获取普通业务保证金占用失败："+e.getMessage());
		}
		return nomalAmt;
	}
	
	/**
	 * 使用联保的保证金占用（仅统计生成合同后）
	 * 实现逻辑：只要联保小组未解散，就占用保证金所有余额
	 * @param acctNo
	 * @throws ComponentException
	 */
	public BigDecimal getLbBailAmtByAcctNo4Ctr(String acctNo)throws ComponentException {
		BigDecimal LbAmt = new BigDecimal(0.0);
		try {
			/**2、联保情况，保证金占用（联保中可以多笔保证金）*/
			//判断是否联保小组的保证金
			BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryIsJointCoopByAcctNo", acctNo, null, this.getConnection());
			int j = Integer.parseInt(count.toString());
			if(j>0){
				/*** 调用esb模块实时接口取交易明细 ***/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				
				KeyedCollection repKColl = null;
				try{
					repKColl = service.tradeBZJZH(acctNo, this.getContext(), this.getConnection());
				}catch(Exception e){
					throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
				}
				if(!TagUtil.haveSuccess(repKColl, this.getContext())){
					//交易失败信息
					String retMsg = (String) repKColl.getDataValue("RET_MSG");
					throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+retMsg);
				}else{
					KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
					String CCY = (String) bodyKColl.getDataValue("CCY");//保证金币种
					KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, this.getContext(), this.getConnection());
					BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
					if(exchange_rate==null){
						throw new ComponentException("获取不到币种"+CCY+"的汇率！");
					}
					LbAmt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate);//余额多少就占多少
					
				}
			}else{
				LbAmt = new BigDecimal(0.0);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("获取联保的保证金占用："+e.getMessage());
		}
		
		return LbAmt;
	}
	
	/**
	 * 担保公司中的保证金占用（仅统计生成合同后）
	 * @param acctNo
	 * @throws ComponentException
	 */
	public BigDecimal getFinBailAmtByAcctNo4Ctr(String acctNo)throws ComponentException {
		BigDecimal FinAmt = new BigDecimal(0.0);
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/**3、担保公司所担保业务，保证金占用（担保公司中可以多笔保证金）*/
			//获取保证金账号所属的融资担保公司所担保的业务
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			IndexedCollection contIColl =  SqlClient.queryList4IColl("queryContNoFromFinGuarByBailAcctNo", acctNo, this.getConnection());
			BigDecimal cal_lmt_amt = new BigDecimal("0");//除放大倍数后占用
			for(int k=0;k<contIColl.size();k++){
				KeyedCollection contKColl = (KeyedCollection) contIColl.get(k);
				String cont_no = (String) contKColl.getDataValue("cont_no");
				KeyedCollection ctrKColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
				BigDecimal ass_sec_multiple = new BigDecimal(ctrKColl.getDataValue("ass_sec_multiple")+"");
				if(cont_no!=null){
					BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
					cal_lmt_amt = cal_lmt_amt.add(iqp_amt.divide(ass_sec_multiple,2,BigDecimal.ROUND_UP));
				}
			}
			
			//获取担保公司下，除本次账号的其他账号总余额
			BigDecimal bail_amt = new BigDecimal("0");//除放大倍数后占用
			IndexedCollection acctIColl =  SqlClient.queryList4IColl("queryAcctNoFromFinGuarByBailAcctNo", acctNo, this.getConnection());
			for(int i=0;i<acctIColl.size();i++){
				KeyedCollection acctKColl = (KeyedCollection) acctIColl.get(i);
				String bail_acct_no = (String) acctKColl.getDataValue("bail_acct_no");
				
				if(bail_acct_no.equals(acctNo)){//不统计本次账号
					continue;
				}
				
				/*** 调用esb模块实时接口取交易明细 ***/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				KeyedCollection repKColl = null;
				try{
					repKColl = service.tradeBZJZH(bail_acct_no, this.getContext(), this.getConnection());
				}catch(Exception e){
					throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
				}
				if(!TagUtil.haveSuccess(repKColl, this.getContext())){
					//交易失败信息
					String retMsg = (String) repKColl.getDataValue("RET_MSG");
					throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+retMsg);
				}else{
					KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
					String CCY = (String) bodyKColl.getDataValue("CCY");//保证金币种
					KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, this.getContext(), this.getConnection());
					BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
					if(exchange_rate==null){
						throw new ComponentException("获取不到币种"+CCY+"的汇率！");
					}
					bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate));
				}
			}
			
			//保证金占用
			if(bail_amt.compareTo(cal_lmt_amt)>0){
				FinAmt = new BigDecimal(0.0);
			}else{
				FinAmt = cal_lmt_amt.subtract(bail_amt);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("获取担保公司中的保证金占用："+e.getMessage());
		}
		return FinAmt;
	}
	
	/**
	 * 票据池中的保证金占用（仅统计生成合同后）
	 * @param acctNo
	 * @throws ComponentException
	 */
	public BigDecimal getBackBailAmtByAcctNo4Ctr(String acctNo)throws ComponentException {
		BigDecimal backAmt = new BigDecimal(0.0);
		try {
			// added by yangzy 2015-2-15 需求编号【HS141110017】保理业务改造 begin 
			/**4、使用票据池所担保业务，保证金占用（票据池中的回款保证金）*/
			//KeyedCollection backkColl = (KeyedCollection) SqlClient.queryFirst("getBillOutStndAmt", acctNo, null, this.getConnection());
			//backAmt = (BigDecimal)backkColl.getDataValue("totlamt");
			//if(backAmt==null||"".equals(backAmt)){
			//	backAmt = new BigDecimal(0.0);
			//}
			IndexedCollection backiColl = SqlClient.queryList4IColl("getBillOutStndAmtList", acctNo, this.getConnection());
			if(backiColl!=null && backiColl.size()>0){
				for(int i=0;i<backiColl.size();i++){
					KeyedCollection kColl = (KeyedCollection) backiColl.get(i);
					String guar_cont_no = (String) kColl.getDataValue("guar_cont_no");
					BigDecimal billAmt = (BigDecimal)kColl.getDataValue("bill_amt");
					IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
					
					//获取担保合同该池下，除本次账号的其他账号总余额
					BigDecimal bail_amt = new BigDecimal("0");//除放大倍数后占用
					IndexedCollection acctIColl =  SqlClient.queryList4IColl("getBillPoAcctList", guar_cont_no, this.getConnection());
					for(int j=0;j<acctIColl.size();j++){
						KeyedCollection acctKColl = (KeyedCollection) acctIColl.get(j);
						String bail_acct_no = (String) acctKColl.getDataValue("bail_acc_no");
						
						if(bail_acct_no.equals(acctNo)){//不统计本次账号
							continue;
						}
						
						/*** 调用esb模块实时接口取交易明细 ***/
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
						IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
						KeyedCollection repKColl = null;
						try{
							repKColl = service.tradeBZJZH(bail_acct_no, this.getContext(), this.getConnection());
						}catch(Exception e){
							throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
						}
						if(!TagUtil.haveSuccess(repKColl, this.getContext())){
							//交易失败信息
							String retMsg = (String) repKColl.getDataValue("RET_MSG");
							throw new ComponentException("ESB通讯接口【获取保证金账户信息】交易失败："+retMsg);
						}else{
							KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
							String CCY = (String) bodyKColl.getDataValue("CCY");//保证金币种
							KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, this.getContext(), this.getConnection());
							BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
							if(exchange_rate==null){
								throw new ComponentException("获取不到币种"+CCY+"的汇率！");
							}
							bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate));
						}
					}					
					//billAmt=billAmt.subtract(bail_amt);
					
					TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
					KeyedCollection kColl4GrtCont = dao.queryDetail("GrtGuarCont", guar_cont_no, this.getConnection());
					BigDecimal used_amt = new BigDecimal(0.00);
					BigDecimal backAmt1 = new BigDecimal(0.00);
					if(kColl4GrtCont.containsKey("guar_cont_type")&&kColl4GrtCont.getDataValue("guar_cont_type")!=null&&"00".equals(kColl4GrtCont.getDataValue("guar_cont_type").toString())){
						//一般担保  --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
						//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
						
						String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no+"'";
					    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, this.getConnection());
					    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
					    String conditionStr = "";
					    conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";

						/**查询关联表中此担保合同已已经引入的金额*/
						IndexedCollection iColl4RGur =  dao.queryList("GrtLoanRGur", conditionStr, this.getConnection());
						for(int j=0;j<iColl4RGur.size();j++){
						   KeyedCollection kColl1 = (KeyedCollection)iColl4RGur.get(j);
						   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
						   if(is_per_gur != null && !"".equals(is_per_gur)){
							   String pk_id = (String)kColl1.getDataValue("pk_id");
							   String cont_no = (String)kColl1.getDataValue("cont_no");
							   if(cont_no != null && !"".equals(cont_no)){
								   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
								   if("2".equals(res)){
									   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
								   }else{
									   used_amt = used_amt.add(new BigDecimal(0));
								   }
							   }else{
								   String sernoSelect = (String)kColl1.getDataValue("serno");
								   String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
								   if("2".equals(res)){
									   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
								   }else{
									   used_amt = used_amt.add(new BigDecimal(0));
								   }
							   }
						   }
						}
					}else{
						//最高额担保
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
						used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, this.getContext(), this.getConnection()));
					}
					if(used_amt.compareTo(billAmt.add(bail_amt))>0){
						backAmt1 = used_amt.subtract(billAmt).subtract(bail_amt);
					}
					backAmt=backAmt.add(backAmt1);
				}
				backAmt = BigDecimalUtil.replaceNull(Math.ceil(backAmt.doubleValue()/100)*100);
			}
			// added by yangzy 2015-2-15 需求编号【HS141110017】保理业务改造 end 
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("获取票据池中的保证金占用："+e.getMessage());
		}
		return backAmt;
	}
}
