package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class SureOutPoolOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no = "";
		String porder_no = "";
		KeyedCollection IqpBillDetailInfo = new KeyedCollection("IqpBillDetailInfo");
		KeyedCollection IqpCorreInfo = new KeyedCollection("IqpCorreInfo");
		KeyedCollection IqpDrfpoMana = new KeyedCollection("IqpDrfpoMana");//票据管理信息的kColl
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			drfpo_no = (String) context.getDataValue("drfpo_no");
			porder_no = (String) context.getDataValue("porder_no");
			//中文转码
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
			porder_no = URLDecoder.decode(porder_no,"UTF-8");
			String porder[] = porder_no.split(",");
			
			/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 start */
			IndexedCollection iColl = SqlClient.queryList4IColl("queryGuarContNoByPoNo", drfpo_no, connection);
			BigDecimal all_used_amt = new BigDecimal(0.00);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String guar_cont_no = (String) kColl.getDataValue("guar_cont_no");
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				
				KeyedCollection kColl4GrtCont = dao.queryDetail("GrtGuarCont", guar_cont_no, connection);
				BigDecimal used_amt = new BigDecimal(0.00);
				if(kColl4GrtCont.containsKey("guar_cont_type")&&kColl4GrtCont.getDataValue("guar_cont_type")!=null&&"00".equals(kColl4GrtCont.getDataValue("guar_cont_type").toString())){
					//一般担保  --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
					//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
					
					String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no+"'";
				    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
				    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
				    String conditionStr = "";
				    conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";

					/**查询关联表中此担保合同已已经引入的金额*/
					IndexedCollection iColl4RGur =  dao.queryList("GrtLoanRGur", conditionStr, connection);
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
					used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, connection));
				}
				all_used_amt = all_used_amt.add(used_amt);
			}
			KeyedCollection kCollMana_p1 = dao.queryFirst("IqpDrfpoMana", null, "where drfpo_no = '"+drfpo_no+"'", connection);
			BigDecimal bill_amt = BigDecimalUtil.replaceNull(kCollMana_p1.getDataValue("bill_amt"));
			BigDecimal bail_amt = new BigDecimal("0");
			if(kCollMana_p1!=null&&kCollMana_p1.containsKey("bail_acc_no")&&kCollMana_p1.getDataValue("bail_acc_no")!=null&&!"".equals(kCollMana_p1.getDataValue("bail_acc_no"))){
				String bail_acc_no = (String)kCollMana_p1.getDataValue("bail_acc_no");
				KeyedCollection repKColl = null;
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				try{
					repKColl = service.tradeBZJZH(bail_acc_no, context, connection);
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
					KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, context, connection);
					BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
					if(exchange_rate==null){
						throw new Exception("获取不到币种"+CCY+"的汇率！");
					}
					bail_amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate);//余额多少就占多少
				}
			}
			BigDecimal all_drft_amt = new BigDecimal("0");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				KeyedCollection kColl = dao.queryDetail("IqpBillDetailInfo", porder_no, connection);
				BigDecimal drft_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("drft_amt"));
				all_drft_amt = all_drft_amt.add(drft_amt);

			}
			
			/* modified by yangzy 2015/05/04 应收账款类出池校验，在池是否覆盖敞口,在池金额过滤记账中的数据 start */
			BigDecimal inPoolAmt = new BigDecimal("0");
			Map<String, String> selMap = new HashedMap();
			selMap.put("status","01");
			selMap.put("drfpo_no",drfpo_no);
			IndexedCollection nextIColl = SqlClient.queryList4IColl("getDrftAmtByDrfpoNo",selMap, connection);
			if(nextIColl.size()!=0){
				for(int j=0;j<nextIColl.size();j++){
					KeyedCollection kc = (KeyedCollection) nextIColl.get(j);
					inPoolAmt = inPoolAmt.add(BigDecimalUtil.replaceNull(kc.getDataValue("drft_amt")));
				}
			}
			
			if((inPoolAmt.subtract(all_drft_amt)).add(bail_amt).compareTo(all_used_amt)<0){
				context.addDataField("flag","error");
				return "0";
			}
			/* modified by yangzy 2015/05/04 应收账款类出池校验，在池是否覆盖敞口,在池金额过滤记账中的数据 end */
			/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 end */
			
			//更改汇票状态为“解质押”状态
			IqpBillDetailInfo.addDataField("porder_no","");
			IqpBillDetailInfo.addDataField("status","06");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpBillDetailInfo.setDataValue("porder_no",porder_no);
				//dao.update(IqpBillDetailInfo, connection);
			}
			//更改票据池与汇票的关联信息表的状态为“出池记账中”状态
			IqpCorreInfo.addDataField("drfpo_no",drfpo_no);
			IqpCorreInfo.addDataField("porder_no","");
			IqpCorreInfo.addDataField("status","06");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpCorreInfo.setDataValue("porder_no",porder_no);
				dao.update(IqpCorreInfo, connection);
			}
			//构建票据池组件类
			DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			/**查询票据池中处于在池状态的票据票面金额价值总额*/
			Double count = dpoComponent.getDrftAmtByDrfpoNo(drfpo_no,"01");
			//更改票据池状态为有效状态，更新在池票据总金额字段
			IqpDrfpoMana.addDataField("drfpo_no",drfpo_no);
			IqpDrfpoMana.addDataField("status","01");
			IqpDrfpoMana.addDataField("bill_amt", count);
			//dao.update(IqpDrfpoMana, connection);
			
			//返回异步标志，状态更改成功
			//context.addDataField("flag","success");
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//新增出入库申请主表信息
			KeyedCollection kCollInfo = new KeyedCollection("MortStorExwaInfo");
			kCollInfo.addDataField("serno", serno);
			kCollInfo.addDataField("stor_exwa_mode","03");//出库（池）
			kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
			kCollInfo.addDataField("exwa_type", "03");//票据池
			kCollInfo.addDataField("approve_status", "997");
			dao.insert(kCollInfo, connection);
			
			KeyedCollection kCollDetail = new KeyedCollection("MortStorExwaDetail");
			kCollDetail.addDataField("serno",serno);
			kCollDetail.addDataField("guaranty_no","");
			kCollDetail.addDataField("warrant_no","");
			kCollDetail.addDataField("warrant_type","");
			kCollDetail.addDataField("warrant_state","");
			kCollDetail.addDataField("ori_warrant_state","");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				kCollDetail.setDataValue("guaranty_no",drfpo_no);
				kCollDetail.setDataValue("warrant_no",porder_no);
				KeyedCollection kColl = dao.queryDetail("IqpBillDetailInfo", porder_no, connection);
				if(kColl.containsKey("bill_type")&&kColl.getDataValue("bill_type")!=null&&"100".equals(kColl.getDataValue("bill_type"))){
					kCollDetail.setDataValue("warrant_type","53");//银票
				}else{
					kCollDetail.setDataValue("warrant_type","33");//商票
				}
				kCollDetail.setDataValue("warrant_state","9");//出库（池）记账中
				kCollDetail.setDataValue("ori_warrant_state","01");//在池
				dao.insert(kCollDetail, connection);
			}
			
			/**调用生成授权，等待发送报文*/
			MortFlowComponent MortFlowComponent = (MortFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("MortFlowComponent",context,connection );
			MortFlowComponent.doWfAgreeForMort(serno);
			/**调用ESB接口，发送报文*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			try{
				serviceRel.tradeDZYWQZCRK(serno, context, connection);
				context.addDataField("flag","success");
			}catch(Exception e){
				context.addDataField("flag", "票据池出池授权发送失败!");
				throw new EMPException("票据池出池授权发送失败!");
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
