package com.yucheng.cmis.biz01line.cont.op.ctrlimitcont;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class SearchCtrLimitContUseDetails  extends CMISOperation {
	private final String iqpModel = "IqpLoanApp";
	private final String ctrModel = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		try{
			connection = this.getConnection(context);
			String cont_no = "";
			try{
				cont_no = (String)context.getDataValue("cont_no");
			}catch(Exception e){
				throw new Exception("合同编号获取为空，请联系用户管理员!");
			}
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			//'000':'待发起', '111':'审批中', '990':'取消', '991':'重办', '992':'打回', '993':'追回', '997':'通过', '998':'否决'
			String conditionIqp = "where limit_cont_no='"+cont_no+"' and approve_status <>'997'";
			//'100':'未生效', '200':'生效', '500':'中止', '600':'注销', '700':'撤销', '800':'作废', '900':'核销'
			String conditionCtr = "where limit_cont_no='"+cont_no+"'";
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "5000");
			
			IndexedCollection iqpIColl = dao.queryList(iqpModel, conditionIqp, connection);
			IndexedCollection contIColl = dao.queryList(ctrModel, conditionCtr, connection);
			for(int i=0;i<iqpIColl.size();i++){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(i);
				/**计算敞口金额*/
				BigDecimal apply_amount = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("apply_amount"));
				String prd_id = (String) iqpKColl.getDataValue("prd_id");
				String serno = (String) iqpKColl.getDataValue("serno");
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
				//获取实时汇率  start
				String cur_type = (String) iqpKColl.getDataValue("apply_cur_type");//申请币种
				String security_cur_type = (String) iqpKColl.getDataValue("security_cur_type");//保证金币种
				//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
				//KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, context, connection);
				//if("failed".equals(kCollRate.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//申请汇率
				//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				//获取实时汇率  end
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(iqpKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					//申请金额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
					//计算结果进百
					//进百后乘保证金汇率
					BigDecimal securityAmt = (apply_amount.multiply(security_rate)).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
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
					//（申请金额-保证金金额）*汇率-视同保证金
					    //申请金额*（1+怡装比例）-申请金额*（1+怡装比例）*保证金比例*汇率
				}
				
				//risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).add(floodact_perc)).multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				iqpKColl.put("risk_open_amt", risk_amt);
			} 
			for(int i=0;i<contIColl.size();i++){
				KeyedCollection contKColl = (KeyedCollection)contIColl.get(i);
				String prd_id = (String) contKColl.getDataValue("prd_id");
				String serno = (String) contKColl.getDataValue("serno");
				/**计算敞口金额*/
				BigDecimal cont_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("cont_amt"));
				
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
				//获取实时汇率  start
				//String cur_type = (String) contKColl.getDataValue("cont_cur_type");
				//String security_cur_type = (String) contKColl.getDataValue("security_cur_type");//保证金币种
				//if(security_cur_type==null||security_cur_type.equals("")){
				//	security_cur_type = "CNY";
				//}
				//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
				//KeyedCollection kCollRateSecurity = service.getHLByCurrType(security_cur_type, context, connection);
				//if("failed".equals(kCollRate.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				//}
				//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
				//获取实时汇率  end
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("exchange_rate"));//汇率
				BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				
				BigDecimal security_rate = BigDecimalUtil.replaceNull(contKColl.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(contKColl.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				risk_open_amt = ((cont_amt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
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
				
				Double risk_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				contKColl.addDataField("risk_open_amt", risk_amt);
			} 
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"}; 
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iqpIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SystemTransUtils.dealName(contIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			
			iqpIColl.setName("LmtUseIqpList");
			contIColl.setName("LmtUseContList");
			this.putDataElement2Context(iqpIColl, context);
			this.putDataElement2Context(contIColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
