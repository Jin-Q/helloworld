package com.yucheng.cmis.biz01line.iqp.op.iqpguarantchangeapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddIqpGuarantChangeAppPageOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpGuarantChangeApp";
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdGuarant = "IqpGuarantInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
			String bill_no = null;
			String oldserno = null;
			KeyedCollection kCollGuarant = new KeyedCollection(modelId);
			try {
				cont_no = (String)context.getDataValue("cont_no");
				bill_no =  (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(cont_no == null || bill_no == null || "".equals(cont_no) || "".equals(bill_no)){ 
				throw new EMPJDBCException("The values cont_no or bill_no cannot be empty!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);  
			/**从合同表和台账表中取数据*/
			KeyedCollection kCollCtr = dao.queryDetail(modelIdCtr, cont_no, connection); 
			oldserno = (String)kCollCtr.getDataValue("serno");
			KeyedCollection kCollOldGuarant = dao.queryDetail(modelIdGuarant,(String)kCollCtr.getDataValue("serno"),connection);
			
			/**计算敞口金额*/
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("cont_amt"));
			
			//获取实时汇率  start
			String cur_type = (String) kCollCtr.getDataValue("cont_cur_type");
			String security_cur_type = (String) kCollCtr.getDataValue("security_cur_type");//保证金币种
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
			KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, context, connection);
			if("failed".equals(kCollRate.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			BigDecimal new_exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			BigDecimal new_security_exchange_rate = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			kCollGuarant.addDataField("new_security_exchange_rate", new_security_exchange_rate);
			//获取实时汇率  end
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("exchange_rate"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			BigDecimal security_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_rate")); //保证金比例
			BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("same_security_amt"));//视同保证金
			
			BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			String caculateAmt = String.valueOf(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
			
			BigDecimal risk_open_amt = new BigDecimal(0);
			risk_open_amt = ((cont_amt.multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
			
			
			/**往信用证申请表中插入数据-------------start----------------------*/
			kCollGuarant.addDataField("apply_date",context.getDataValue("OPENDAY")); 
			kCollGuarant.addDataField("bill_no", bill_no);
			kCollGuarant.addDataField("old_serno", oldserno);
			kCollGuarant.addDataField("cont_no", cont_no); 
			kCollGuarant.addDataField("prd_id", TagUtil.replaceNull4String(kCollCtr.getDataValue("prd_id")));
			kCollGuarant.addDataField("cus_id", TagUtil.replaceNull4String(kCollCtr.getDataValue("cus_id")));
			kCollGuarant.addDataField("assure_main", TagUtil.replaceNull4String(kCollCtr.getDataValue("assure_main")));
			kCollGuarant.addDataField("assure_main_details", TagUtil.replaceNull4String(kCollCtr.getDataValue("assure_main_details")));
			kCollGuarant.addDataField("cont_cur_type", TagUtil.replaceNull4String(kCollCtr.getDataValue("cont_cur_type")));
			kCollGuarant.addDataField("exchange_rate", exchange_rate); 
			kCollGuarant.addDataField("cont_amt", TagUtil.replaceNull4Double(kCollCtr.getDataValue("cont_amt"))); 
			
			kCollGuarant.addDataField("security_cur_type", kCollCtr.getDataValue("security_cur_type"));
			kCollGuarant.addDataField("security_exchange_rate", kCollCtr.getDataValue("security_exchange_rate"));
			kCollGuarant.addDataField("security_amt", kCollCtr.getDataValue("security_amt"));
			
			kCollGuarant.addDataField("security_rate", TagUtil.replaceNull4Double(kCollCtr.getDataValue("security_rate")));
			kCollGuarant.addDataField("same_security_amt", TagUtil.replaceNull4Double(kCollCtr.getDataValue("same_security_amt")));
			kCollGuarant.addDataField("risk_open_amt", risk_open_amt);// 敞口金额
			kCollGuarant.addDataField("risk_open_rate", risk_open_amt.divide(cont_amt.multiply(exchange_rate),2,BigDecimal.ROUND_HALF_EVEN));// 敞口比例
			kCollGuarant.addDataField("cont_start_date", TagUtil.replaceNull4String(kCollCtr.getDataValue("cont_start_date")));
			kCollGuarant.addDataField("cont_end_date", TagUtil.replaceNull4String(kCollCtr.getDataValue("cont_end_date")));
			
			kCollGuarant.addDataField("guarant_type", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("guarant_type")));   
			kCollGuarant.addDataField("guarant_mode", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("guarant_mode")));
			kCollGuarant.addDataField("open_type", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("open_type")));
			kCollGuarant.addDataField("chrg_rate", TagUtil.replaceNull4Double(kCollOldGuarant.getDataValue("chrg_rate")));
			kCollGuarant.addDataField("is_bank_format", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("is_bank_format")));
			kCollGuarant.addDataField("is_agt_guarant", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("is_agt_guarant")));
			kCollGuarant.addDataField("agt_bank_no", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("agt_bank_no")));  
			kCollGuarant.addDataField("agt_bank_name", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("agt_bank_name")));
			kCollGuarant.addDataField("item_name", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("item_name")));  
			kCollGuarant.addDataField("item_amt", TagUtil.replaceNull4Double(kCollOldGuarant.getDataValue("item_amt")));
			kCollGuarant.addDataField("guarant_cont_no", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("cont_no"))); 
			kCollGuarant.addDataField("cont_name", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("cont_name")));
			kCollGuarant.addDataField("guarant_cur_type", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("cur_type")));
			kCollGuarant.addDataField("ben_name", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("ben_name")));
			kCollGuarant.addDataField("ben_addr", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("ben_addr")));
			kCollGuarant.addDataField("ben_acct_org_no", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("ben_acct_org_no"))); 
			kCollGuarant.addDataField("ben_acct_no", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("ben_acct_no")));
			kCollGuarant.addDataField("guarant_pay_type", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("guarant_pay_type")));
			kCollGuarant.addDataField("corre_busnes_cont_amt", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("corre_busnes_cont_amt")));
			kCollGuarant.addDataField("end_date", TagUtil.replaceNull4String(kCollOldGuarant.getDataValue("end_date")));
			
            			
			kCollGuarant.addDataField("input_id", context.getDataValue("currentUserId"));
			kCollGuarant.addDataField("input_br_id", context.getDataValue("organNo"));
			kCollGuarant.addDataField("input_date", context.getDataValue("OPENDAY")); 
			kCollGuarant.addDataField("approve_status", "000");
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			kCollGuarant.addDataField("manager_br_id", (String)kCollCtr.getDataValue("manager_br_id"));
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			/****--------------------------------end--------------------------------------------****/
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","prdname"};    
		    //详细信息翻译时调用	
		    SystemTransUtils.dealName(kCollGuarant, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kCollGuarant, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kCollGuarant, new String[]{"input_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			SInfoUtils.addSOrgName(kCollGuarant, new String[]{"manager_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
		    this.putDataElement2Context(kCollGuarant, context);
		    
		    /**--------------------------------复制业务担保信息start-------------------------------------------------*/
//		    String conditionStr = "where cont_no='"+cont_no+"'";
//		    IndexedCollection iCollRel = dao.queryList(modelIdGrtLoan, conditionStr, connection);
//		    
//		    for(int i=0;i<iCollRel.size();i++){
//		    	KeyedCollection kColl = (KeyedCollection)iCollRel.get(i); 
//		    	if(!"3".equals(kColl.getDataValue("corre_rel"))){
//		    		String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
//		    		kColl.setDataValue("pk_id", pk_id);
//		    		kColl.setDataValue("serno", serno);
//		    		kColl.setDataValue("cont_no", "");
//		    		dao.insert(kColl, connection);
//		    	}
//		    }
		    /**--------------------------------end-------------------------------------------------*/
			
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
