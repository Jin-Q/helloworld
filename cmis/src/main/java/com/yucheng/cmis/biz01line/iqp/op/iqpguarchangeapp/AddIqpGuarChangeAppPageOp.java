package com.yucheng.cmis.biz01line.iqp.op.iqpguarchangeapp;

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

public class AddIqpGuarChangeAppPageOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpGuarChangeApp";
	private final String modelIdCtr = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
            String oldserno = null;
			KeyedCollection kCollGuar = new KeyedCollection(modelId);
			try {
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(cont_no == null || "".equals(cont_no)){
				throw new EMPJDBCException("The value cont_no cannot be empty!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			/**从合同表中取数据*/
			KeyedCollection kCollCtr = dao.queryDetail(modelIdCtr, cont_no, connection);  
			oldserno = (String)kCollCtr.getDataValue("serno"); 
			String prd_id = (String) kCollCtr.getDataValue("prd_id");

			/**计算敞口金额*/
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("cont_amt"));
			
			//获取实时汇率  start
			String cur_type = (String) kCollCtr.getDataValue("cont_cur_type");
			String security_cur_type = (String) kCollCtr.getDataValue("security_cur_type");//保证金币种
			if(security_cur_type==null||security_cur_type.equals("")){
				security_cur_type = "CNY";
			}
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
			//获取实时汇率  end
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("exchange_rate"));//汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/			
			
			BigDecimal security_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_rate")); //保证金比例
			BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("same_security_amt"));//视同保证金
			
			BigDecimal risk_open_amt = new BigDecimal(0);
			risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
			if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
					prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
					prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
				
				//判断是否为信用证业务
				BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
				if("700020".equals(prd_id) || "700021".equals(prd_id)){
					KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", oldserno, connection);
					if(kCollCredit.containsKey("serno")){
						floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						//risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
					}
				}
				//合同金额*保证金比例*（1+溢装比例）*合同汇率/保证金汇率
				//计算结果进百
				//进百后乘保证金汇率
				BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
				java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				String caculateAmt = String.valueOf(securityAmt);
				securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
				String changeAmt = nf.format(securityAmt);
				securityAmt = BigDecimalUtil.replaceNull(changeAmt);
				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security)))).subtract(same_security_amt);
			    if(risk_open_amt.compareTo(new BigDecimal(0))<0){
			    	risk_open_amt = new BigDecimal(0);
			    }
			}
			
			risk_open_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP);
			 
			/**往担保变更申请表中插入数据-------------start----------------------*/
			kCollGuar.addDataField("apply_date",context.getDataValue("OPENDAY")); 
			kCollGuar.addDataField("old_serno", oldserno);
			kCollGuar.addDataField("cont_no", cont_no);
			kCollGuar.addDataField("prd_id", kCollCtr.getDataValue("prd_id"));
			kCollGuar.addDataField("cus_id", kCollCtr.getDataValue("cus_id"));
			kCollGuar.addDataField("assure_main", kCollCtr.getDataValue("assure_main"));
			kCollGuar.addDataField("assure_main_details", kCollCtr.getDataValue("assure_main_details"));
			kCollGuar.addDataField("cont_cur_type", kCollCtr.getDataValue("cont_cur_type"));
			kCollGuar.addDataField("exchange_rate", kCollCtr.getDataValue("exchange_rate")); 
			kCollGuar.addDataField("cont_amt", kCollCtr.getDataValue("cont_amt"));
			kCollGuar.addDataField("security_rate", kCollCtr.getDataValue("security_rate"));
			kCollGuar.addDataField("same_security_amt", TagUtil.replaceNull4Double(kCollCtr.getDataValue("same_security_amt")));
			kCollGuar.addDataField("risk_open_amt",  risk_open_amt);// 敞口金额     
			kCollGuar.addDataField("risk_open_rate", risk_open_amt.divide(cont_amt,2,BigDecimal.ROUND_HALF_EVEN));// 敞口比例
			kCollGuar.addDataField("cont_start_date", kCollCtr.getDataValue("cont_start_date"));
			kCollGuar.addDataField("cont_end_date", kCollCtr.getDataValue("cont_end_date"));
            			
			kCollGuar.addDataField("input_id", context.getDataValue("currentUserId"));
			kCollGuar.addDataField("input_br_id", context.getDataValue("organNo"));
			kCollGuar.addDataField("input_date", context.getDataValue("OPENDAY")); 
			kCollGuar.addDataField("approve_status", "000");
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			kCollGuar.addDataField("manager_br_id", (String)kCollCtr.getDataValue("manager_br_id"));
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			/****--------------------------------end--------------------------------------------****/
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","prdname"};    
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kCollGuar, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kCollGuar, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kCollGuar, new String[]{"input_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			SInfoUtils.addSOrgName(kCollGuar, new String[]{"manager_br_id"});
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			this.putDataElement2Context(kCollGuar, context); 
			
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
