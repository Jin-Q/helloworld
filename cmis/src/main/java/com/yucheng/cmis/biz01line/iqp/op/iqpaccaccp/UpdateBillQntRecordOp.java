package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateBillQntRecordOp extends CMISOperation {
	 
	//private final String modelId = "IqpAccAccp";
	private final String modelIdDetail = "IqpAccpDetail";
	private final String modelIdIqpLoanApp = "IqpLoanApp";
	private final String modelIdCont = "CtrLoanCont";
 
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			BigDecimal totalAmt= new BigDecimal(0.00);
			String serno = "";
			String cont_no = "";
			String prd_id = "";
			try {
				serno = (String)context.getDataValue("serno");
				if(context.containsKey("cont_no")){
				  cont_no = (String)context.getDataValue("cont_no");
				}
			} catch (Exception e) {}
			if(serno == null || serno == "")
				throw new EMPJDBCException("The values to update["+serno+"] cannot be empty!");
			
			String condition = "where serno='"+serno+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询银行承兑汇票修改页面的数据
			//KeyedCollection kc = dao.queryDetail(modelId, serno, connection);
			//查询承兑汇票申请明细数据,计算总汇票金额
			IndexedCollection iColl = dao.queryList(modelIdDetail, condition, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				BigDecimal amt = BigDecimalUtil.replaceNull(kColl.getDataValue("drft_amt"));
				totalAmt = totalAmt.add(amt);
			}
			if(cont_no == null || "".equals(cont_no)){
				//保存业务申请页面申请金额数据
				KeyedCollection kCollIqpLoanApp = dao.queryDetail(modelIdIqpLoanApp, serno, connection);
				kCollIqpLoanApp.setDataValue("apply_amount", totalAmt); 
				//获取实时汇率  start
				String cur_type = (String) kCollIqpLoanApp.getDataValue("apply_cur_type");
				KeyedCollection kCollRate = null;
				if(null != cur_type && !"".equals(cur_type)){  //取到币种时才去查询实时汇率信息   2014-03-11
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					kCollRate = service.getHLByCurrType(cur_type, context,connection);
					if("failed".equals(kCollRate.getDataValue("flag"))){
						throw new EMPException("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					}
				}else{
					throw new EMPException("未取到业务币种，请检查业务是否完整！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//获取实时汇率  end
				BigDecimal security_rate = BigDecimalUtil.replaceNull(kCollIqpLoanApp.getDataValue("security_rate")); //保证金比例
				BigDecimal same_security_amt = BigDecimalUtil.replaceNull(kCollIqpLoanApp.getDataValue("same_security_amt"));//视同保证金
				
				BigDecimal risk_open_amt = new BigDecimal(0);
				BigDecimal risk_open_rate = new BigDecimal(0);
				risk_open_amt = ((totalAmt.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
				if(risk_open_amt.compareTo(new BigDecimal(0))<=0){
					risk_open_amt = new BigDecimal(0);
				}else{
					risk_open_rate = risk_open_amt.divide(totalAmt,2,BigDecimal.ROUND_HALF_EVEN);
				}
				kCollIqpLoanApp.setDataValue("risk_open_amt", risk_open_amt);
				kCollIqpLoanApp.setDataValue("risk_open_rate", risk_open_rate);
				dao.update(kCollIqpLoanApp, connection);
				
			}
			/*获取产品编号供"基本信息查看"按钮使用      王青*/
			KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
			prd_id = (String) kCollCont.getDataValue("prd_id");
			context.addDataField("flag", "success");
			context.addDataField("bill_qnt", totalAmt);
			context.addDataField("prd_id", prd_id);
			context.addDataField("msg", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			context.addDataField("bill_qnt", 0);
			context.addDataField("prd_id", "");
			context.addDataField("msg", ee.getMessage());
		} catch(Exception e){
			context.addDataField("flag", "failed");
			context.addDataField("bill_qnt", 0);
			context.addDataField("prd_id", "");
			context.addDataField("msg", e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
