package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class IqpActrecBondOutPoOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";
	private final String modelIdMana = "IqpActrecpoMana";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String cont_no = null;
			String invc_no = null;
			String po_no = null;
			try{
				cont_no = context.getDataValue("cont_no").toString();
				invc_no = context.getDataValue("invc_no").toString();
				po_no = context.getDataValue("po_no").toString();
			}catch(Exception e){}
			
			if (cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("The value of param[" + cont_no
						+ "] cannot be null!");
			
			if (invc_no == null || invc_no.length() == 0)
				throw new EMPJDBCException("The value of param[" + invc_no
						+ "] cannot be null!");
			
			//中文转码
			cont_no = URLDecoder.decode(cont_no,"UTF-8");
			invc_no = URLDecoder.decode(invc_no,"UTF-8");
			po_no = URLDecoder.decode(po_no,"UTF-8");
			
			String cont_no_arr[] = cont_no.split(",");
			String invc_no_arr[] = invc_no.split(",");
			String po_no_arr[] = po_no.split(",");
			
			/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 start */
			IndexedCollection iColl = SqlClient.queryList4IColl("queryGuarContNoByPoNo", po_no_arr[0], conn);
			BigDecimal all_used_amt = new BigDecimal(0.00);
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String guar_cont_no = (String) kColl.getDataValue("guar_cont_no");
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
				
				KeyedCollection kColl4GrtCont = dao.queryDetail("GrtGuarCont", guar_cont_no, conn);
				BigDecimal used_amt = new BigDecimal(0.00);
				if(kColl4GrtCont.containsKey("guar_cont_type")&&kColl4GrtCont.getDataValue("guar_cont_type")!=null&&"00".equals(kColl4GrtCont.getDataValue("guar_cont_type").toString())){
					//一般担保  --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
					//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
					
					String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no+"'";
				    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, conn);
				    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
				    String conditionStr = "";
				    conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";

					/**查询关联表中此担保合同已已经引入的金额*/
					IndexedCollection iColl4RGur =  dao.queryList("GrtLoanRGur", conditionStr, conn);
					for(int j=0;j<iColl4RGur.size();j++){
					   KeyedCollection kColl1 = (KeyedCollection)iColl4RGur.get(j);
					   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
					   if(is_per_gur != null && !"".equals(is_per_gur)){
						   String pk_id = (String)kColl1.getDataValue("pk_id");
						   String cont_no_p1 = (String)kColl1.getDataValue("cont_no");
						   if(cont_no_p1 != null && !"".equals(cont_no_p1)){
							   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no_p1,pk_id);
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
					used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, conn));
				}
				all_used_amt = all_used_amt.add(used_amt);
			}
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start 
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
			KeyedCollection kColl4Query = new KeyedCollection();
			kColl4Query.put("po_no", po_no_arr[0]);
			IndexedCollection iqpContIColl =  SqlClient.queryList4IColl("queryIqpContByPoNoForFact", kColl4Query, conn);//存量业务（已包含了当前申请）
			BigDecimal loan_amt = new BigDecimal("0");//业务总敞口
			for(int j=0;j<iqpContIColl.size();j++){
				KeyedCollection iqpContKColl = (KeyedCollection) iqpContIColl.get(j);
				String contNo = (String) iqpContKColl.getDataValue("cont_no");
				String iqpserno = (String) iqpContKColl.getDataValue("serno");
				if(contNo==null){
					BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtBySerno(iqpserno);//（不需判断是否使用额度，不需判断授信额度标识，即统计该流水的敞口金额汇总）
					loan_amt = loan_amt.add(iqp_amt);
				}else{
					BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(contNo);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
					loan_amt = loan_amt.add(iqp_amt);
				}
			}
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end 
			KeyedCollection kCollMana_p1 = dao.queryFirst(modelIdMana, null, "where po_no = '"+po_no_arr[0]+"'", conn);
			BigDecimal crd_rgtchg_amt = BigDecimalUtil.replaceNull(kCollMana_p1.getDataValue("crd_rgtchg_amt"));
			BigDecimal pledge_rate = BigDecimalUtil.replaceNull(kCollMana_p1.getDataValue("pledge_rate"));
			BigDecimal bail_amt = new BigDecimal("0");
			/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造  begin**/
			IndexedCollection IqpBADList = dao.queryList("IqpBailaccDetail", "where po_no = '"+po_no_arr[0]+"'", conn);
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start
			KeyedCollection kCMana = dao.queryFirst(modelIdMana, null, "where po_no = '"+po_no_arr[0]+"'", conn);
			String poType = (String)kCMana.getDataValue("po_type");
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end
			if(IqpBADList !=null && IqpBADList.size()>0){
				for(int i=0;i<IqpBADList.size();i++){
					KeyedCollection temp  = (KeyedCollection) IqpBADList.get(i);
					if(temp!=null&&temp.containsKey("bail_acc_no")&&temp.getDataValue("bail_acc_no")!=null&&!"".equals(temp.getDataValue("bail_acc_no"))){
						String bail_acc_no = (String)temp.getDataValue("bail_acc_no");
						KeyedCollection repKColl = null;
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
						IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
						//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start
						if("1".equals(poType)){
							try{
								repKColl = service.tradeBZJZH(bail_acc_no, context, conn);
							}catch(Exception e){
								throw new Exception("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
							}
							if(!TagUtil.haveSuccess(repKColl, context)){
								//交易失败信息
								String retMsg = (String) repKColl.getDataValue("RET_MSG");
								throw new Exception("ESB通讯接口【获取保证金账户信息】交易失败："+retMsg);
							}else{
								KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
								String CCY = (String) bodyKColl.getDataValue("CCY");//保证金币种
								KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, context, conn);
								BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
								if(exchange_rate==null){
									throw new Exception("获取不到币种"+CCY+"的汇率！");
								}
								//bail_amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate);//余额多少就占多少
								bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate));
							}
						}else{
							try{
								repKColl = service.tradeZHZH(bail_acc_no, context, conn);
							}catch(Exception e){
								throw new Exception("ESB通讯接口【获取保理账户信息】交易失败："+e.getMessage());
							}
							if(!TagUtil.haveSuccess(repKColl, context)){
								//交易失败信息
								String retMsg = (String) repKColl.getDataValue("RET_MSG");
								throw new Exception("ESB通讯接口【获取保理账户信息】交易失败："+retMsg);
							}else{
								KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
								String CCY = (String) bodyKColl.getDataValue("CCY");//币种
								KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, context, conn);
								BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
								if(exchange_rate==null){
									throw new Exception("获取不到币种"+CCY+"的汇率！");
								}
								//bail_amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate);//余额多少就占多少
								bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("BALANCE")).abs().multiply(exchange_rate));
							}
						}
						//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end
					}
				}
			}
			/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造  end**/
			BigDecimal all_bond_amt = new BigDecimal("0");
			Map<String,String> pkMap_p1 = new HashMap<String,String>();
			for(int i=0;i<invc_no_arr.length;i++){
				pkMap_p1.put("cont_no", cont_no_arr[i]);
				pkMap_p1.put("invc_no", invc_no_arr[i]);
				KeyedCollection kColl = dao.queryDetail(modelId, pkMap_p1, conn);
				BigDecimal bond_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("bond_amt"));
				all_bond_amt = all_bond_amt.add(bond_amt);

			}
			/* added by yangzy 2015/05/04 应收账款类出池校验，在池是否覆盖敞口,在池金额过滤记账中的数据 start */
			KeyedCollection ActrecbondKcoll = (KeyedCollection)SqlClient.queryFirst("queryIqpActrecbondDetail", po_no_arr[0], null, conn);
			if(ActrecbondKcoll!=null&&ActrecbondKcoll.getDataValue("invcquant")!=null){
				crd_rgtchg_amt = (BigDecimal)ActrecbondKcoll.getDataValue("bondamt");
			}
			/* added by yangzy 2015/05/04 应收账款类出池校验，在池是否覆盖敞口,在池金额过滤记账中的数据 end */
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start 
			if((crd_rgtchg_amt.subtract(all_bond_amt)).multiply(pledge_rate).add(bail_amt).compareTo(all_used_amt.add(loan_amt))<0){
				if("1".equals(poType)){
					context.addDataField("flag","error1");
				}else{
					context.addDataField("flag","error2");
				}
				return "0";
			}
			//modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end 
			/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 end */
			
			//更新应收账款明细为出池记账中
			//TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			for(int i=0;i<invc_no_arr.length;i++){
				pkMap.put("cont_no", cont_no_arr[i]);
				pkMap.put("invc_no", invc_no_arr[i]);
				KeyedCollection kColl = dao.queryDetail(modelId, pkMap, conn);
				kColl.setDataValue("status", "6");
				dao.update(kColl, conn);
			}
			
			
			//更新池状态为生效
			KeyedCollection kCollMana = dao.queryFirst(modelIdMana, null, "where po_no = '"+po_no_arr[0]+"'", conn);
			kCollMana.setDataValue("status", "2");
			IqpActrecBondComponent component = new IqpActrecBondComponent();
			String sAmt = component.getAllInvcAndBondAmt(po_no, conn);
			kCollMana.setDataValue("invc_quant", Integer.parseInt(sAmt
					.split("@")[0]));
			kCollMana.setDataValue("invc_amt", Double.parseDouble(sAmt
					.split("@")[1]));
			kCollMana.setDataValue("crd_rgtchg_amt", Double.parseDouble(sAmt
					.split("@")[2]));
			//dao.update(kCollMana, conn);
			//context.addDataField("flag", "success");
			//this.putDataElement2Context(kColl, context);
			String po_type = (String)kCollMana.getDataValue("po_type");
			String exwa_type = "";
			if(po_type!=null&&"1".equals(po_type)){
				exwa_type = "01";//应收账款池
			}else{
				exwa_type = "02";//保理池
			}
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", conn, context);
			//新增出入库申请主表信息
			KeyedCollection kCollInfo = new KeyedCollection("MortStorExwaInfo");
			kCollInfo.addDataField("serno", serno);
			kCollInfo.addDataField("stor_exwa_mode","03");//出库（池）
			kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
			kCollInfo.addDataField("exwa_type", exwa_type);
			kCollInfo.addDataField("approve_status", "997");
			dao.insert(kCollInfo, conn);
			
			KeyedCollection kCollDetail = new KeyedCollection("MortStorExwaDetail");
			kCollDetail.addDataField("serno",serno);
			kCollDetail.addDataField("guaranty_no","");
			kCollDetail.addDataField("warrant_no","");
			kCollDetail.addDataField("warrant_type","");
			kCollDetail.addDataField("warrant_state","");
			kCollDetail.addDataField("ori_warrant_state","");
			for(int i=0;i<invc_no_arr.length;i++){
				invc_no = invc_no_arr[i];
				po_no = po_no_arr[i];
				cont_no = cont_no_arr[i];
				kCollDetail.setDataValue("guaranty_no",po_no);
				kCollDetail.setDataValue("warrant_no",invc_no+"#"+cont_no);
				kCollDetail.setDataValue("warrant_type","99");//其他
				kCollDetail.setDataValue("warrant_state","9");//出库（池）记账中
				kCollDetail.setDataValue("ori_warrant_state","2");//在池
				dao.insert(kCollDetail, conn);
			}
			
			/**调用生成授权，等待发送报文*/
			MortFlowComponent MortFlowComponent = (MortFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("MortFlowComponent",context,conn );
			MortFlowComponent.doWfAgreeForMort(serno);
			/**调用ESB接口，发送报文*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			try{
				serviceRel.tradeDZYWQZCRK(serno, context, conn);
				context.addDataField("flag","success");
			}catch(Exception e){
				context.addDataField("flag", "票据池出池授权发送失败!");
				throw new EMPException("票据池出池授权发送失败!");
			}
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}

}
