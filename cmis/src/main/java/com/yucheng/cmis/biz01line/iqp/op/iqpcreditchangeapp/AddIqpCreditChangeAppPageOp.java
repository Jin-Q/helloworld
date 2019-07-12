package com.yucheng.cmis.biz01line.iqp.op.iqpcreditchangeapp;

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

public class AddIqpCreditChangeAppPageOp extends CMISOperation {
	
	//operation TableModel 
	private final String modelId = "IqpCreditChangeApp";
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdCredit = "IqpCredit";
	
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
			KeyedCollection kCollCredit = new KeyedCollection(modelId); 
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
			KeyedCollection kCollOldCredit = dao.queryDetail(modelIdCredit,(String)kCollCtr.getDataValue("serno"),connection);
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
			BigDecimal new_exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			kCollCredit.put("new_exchange_rate_security", new_exchange_rate_security);
			//获取实时汇率  end
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("exchange_rate"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			BigDecimal security_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_rate")); //保证金比例
			BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("same_security_amt"));//视同保证金
			
			BigDecimal risk_open_amt = new BigDecimal(0);
			
			BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
			floodact_perc = BigDecimalUtil.replaceNull(kCollOldCredit.getDataValue("floodact_perc"));
			BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			String caculateAmt = String.valueOf(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
			String changeAmt = nf.format(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(changeAmt);
			risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
		
			/**往信用证申请表中插入数据-------------start----------------------*/
			kCollCredit.addDataField("apply_date",context.getDataValue("OPENDAY")); 
			kCollCredit.addDataField("bill_no", bill_no);  
			kCollCredit.addDataField("old_serno", oldserno);
			kCollCredit.addDataField("cont_no", cont_no); 
			kCollCredit.addDataField("prd_id", kCollCtr.getDataValue("prd_id"));
			kCollCredit.addDataField("cus_id", kCollCtr.getDataValue("cus_id"));
			kCollCredit.addDataField("assure_main", kCollCtr.getDataValue("assure_main"));
			kCollCredit.addDataField("assure_main_details", kCollCtr.getDataValue("assure_main_details"));
			kCollCredit.addDataField("cont_cur_type", kCollCtr.getDataValue("cont_cur_type"));
			kCollCredit.addDataField("exchange_rate", exchange_rate);
			kCollCredit.addDataField("cont_amt", kCollCtr.getDataValue("cont_amt"));
			kCollCredit.addDataField("security_rate", kCollCtr.getDataValue("security_rate"));
			kCollCredit.addDataField("security_cur_type", kCollCtr.getDataValue("security_cur_type"));
			kCollCredit.addDataField("security_exchange_rate", kCollCtr.getDataValue("security_exchange_rate"));
			kCollCredit.addDataField("security_amt", kCollCtr.getDataValue("security_amt"));
			kCollCredit.addDataField("same_security_amt", TagUtil.replaceNull4Double(kCollCtr.getDataValue("same_security_amt")));
			kCollCredit.addDataField("risk_open_amt", risk_open_amt);// 敞口金额 
			kCollCredit.addDataField("risk_open_rate", risk_open_amt.divide(cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate),2,BigDecimal.ROUND_HALF_EVEN));// 敞口比例
			kCollCredit.addDataField("cont_start_date", kCollCtr.getDataValue("cont_start_date"));
			kCollCredit.addDataField("cont_end_date", kCollCtr.getDataValue("cont_end_date"));
			kCollCredit.addDataField("credit_type", kCollOldCredit.getDataValue("credit_type"));   
			kCollCredit.addDataField("credit_term_type", kCollOldCredit.getDataValue("credit_term_type"));
			kCollCredit.addDataField("chrg_rate", kCollOldCredit.getDataValue("chrg_rate"));
			kCollCredit.addDataField("fast_day", kCollOldCredit.getDataValue("fast_day"));
			kCollCredit.addDataField("floodact_perc", kCollOldCredit.getDataValue("floodact_perc"));
			kCollCredit.addDataField("shortact_perc", kCollOldCredit.getDataValue("shortact_perc"));
			kCollCredit.addDataField("is_revolv_credit", kCollOldCredit.getDataValue("is_revolv_credit")); 
			//kCollCredit.addDataField("is_internal_cert", kCollOldCredit.getDataValue("is_internal_cert"));
			kCollCredit.addDataField("is_ctrl_gclaim", kCollOldCredit.getDataValue("is_ctrl_gclaim"));  
			//kCollCredit.addDataField("busnes_cont", kCollOldCredit.getDataValue("busnes_cont"));
			kCollCredit.addDataField("beneficiar", kCollOldCredit.getDataValue("beneficiar"));
			//kCollCredit.addDataField("beneficiar_country", kCollOldCredit.getDataValue("beneficiar_country"));
			kCollCredit.addDataField("cdt_cert_no", TagUtil.replaceNull4String(kCollOldCredit.getDataValue("cdt_cert_no")));
			kCollCredit.addDataField("end_date", TagUtil.replaceNull4String(kCollOldCredit.getDataValue("end_date")));
            			
			kCollCredit.addDataField("input_id", context.getDataValue("currentUserId"));
			kCollCredit.addDataField("input_br_id", context.getDataValue("organNo"));
			kCollCredit.addDataField("input_date", context.getDataValue("OPENDAY")); 
			kCollCredit.addDataField("approve_status", "000");
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			kCollCredit.addDataField("manager_br_id", (String)kCollCtr.getDataValue("manager_br_id"));
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			/****--------------------------------end--------------------------------------------****/
			
			//dao.insert(kCollCredit, connection);  
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","prdname"};     
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kCollCredit, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kCollCredit, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kCollCredit, new String[]{"input_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			SInfoUtils.addSOrgName(kCollCredit, new String[]{"manager_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
		    
		    this.putDataElement2Context(kCollCredit, context); 
			
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
