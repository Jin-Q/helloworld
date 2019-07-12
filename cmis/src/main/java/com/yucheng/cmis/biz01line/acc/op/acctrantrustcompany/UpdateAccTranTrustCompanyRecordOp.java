package com.yucheng.cmis.biz01line.acc.op.acctrantrustcompany;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author 
*@time
*@description 需求编号：【XD141204082】当款项明细为【安排错合费】、【贷款管理费】时，交易金额自动计算
*@version v1.0
*@modified v1.1  lisj
 */
public class UpdateAccTranTrustCompanyRecordOp extends CMISOperation {
	

	private final String modelId = "AccTranTrustCompany";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			String eff_flag ="";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			try{
				 eff_flag = (String)context.getDataValue("eff_flag");//生效标志
			}catch(Exception e){}
			String billNo = (String) kColl.getDataValue("bill_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			//当为生效、失效操作时，修改期限内费用交易状态 0-失效标志 1-生效状态
			if(eff_flag!=null && !"".equals(eff_flag) && ("1".equals(eff_flag) ||"0".equals(eff_flag))){
				String rm_value = (String) kColl.getDataValue("reclaim_mode");
				if(rm_value!=null && !"".equals(rm_value) && "PP".equals(rm_value)){
					//当款项明细为归还本金时，需判断是否在费用计费时间内，如果存在，则将明细下的费用交易状态修改为未生效
					String TranDate =  (String) kColl.getDataValue("tran_date");
					String condition ="where to_date(last_pay_date, 'yyyy-mm-dd') < to_date('"+TranDate+"', 'yyyy-mm-dd')"	
						+"				and to_date('"+TranDate+"', 'yyyy-mm-dd') < to_date(tran_date, 'yyyy-mm-dd') "
						+"				and trade_status ='1'                                                       "
						+"				and reclaim_mode in ('AM', 'LM')                                           "
						+"				and bill_no = '"+billNo+"'";
					IndexedCollection iColl = dao.queryList(modelId, null, condition, connection);
					if(iColl!=null && iColl.size() >0){
						for(int i=0;i<iColl.size(); i++){
							KeyedCollection temp = (KeyedCollection) iColl.get(i);
							temp.setDataValue("trade_status", "2");
							dao.update(temp, connection);
						}
					}
					//更新信托台账的贷款余额
					KeyedCollection accLoan = dao.queryAllDetail("AccLoan", billNo, connection);
					BigDecimal loanBalance = BigDecimalUtil.replaceNull(accLoan.getDataValue("loan_balance").toString());//信托台账贷款余额
					BigDecimal tranAmt = BigDecimalUtil.replaceNull(kColl.getDataValue("tran_amt").toString());//归还本金交易金额
					if("1".equals(eff_flag)){
						loanBalance = loanBalance.subtract(tranAmt);
					}else{
						loanBalance = loanBalance.add(tranAmt);
					}
					accLoan.setDataValue("loan_balance", loanBalance);
					dao.update(accLoan, connection);
				}
				if("1".equals(eff_flag)){
					kColl.setDataValue("trade_status", "1");//生效		
				}else{
					kColl.setDataValue("trade_status", "0");//失效
				}
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("更新失败！失败记录条数: " + count);
			}
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
