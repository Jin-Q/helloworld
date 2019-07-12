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
public class DeleteAccTranTrustCompanyRecordOp extends CMISOperation {

	private final String modelId = "AccTranTrustCompany";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			String trade_status = "";
			String bill_no ="";
			String tran_date ="";
			String reclaim_mode ="";
			BigDecimal tran_amt = new BigDecimal(0);
			try {
				serno_value = (String)context.getDataValue(serno_name);
				trade_status = (String)context.getDataValue("trade_status");
				bill_no = (String)context.getDataValue("bill_no");
				tran_date = (String)context.getDataValue("tran_date");
				reclaim_mode = (String)context.getDataValue("reclaim_mode");
				tran_amt = BigDecimalUtil.replaceNull((String)context.getDataValue("tran_amt"));
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			//当为生效、失效操作时，修改期限内费用交易状态 0-失效标志 1-生效状态
			if(trade_status!=null && !"".equals(trade_status) && ("0".equals(trade_status) ||"1".equals(trade_status))){
				if(reclaim_mode!=null && !"".equals(reclaim_mode) && "PP".equals(reclaim_mode)){
					//当款项明细为归还本金时，需判断是否在费用计费时间内，如果存在，则将明细下的费用交易状态修改为未生效
					String condition ="where to_date(last_pay_date, 'yyyy-mm-dd') < to_date('"+tran_date+"', 'yyyy-mm-dd')"	
						+"				and to_date('"+tran_date+"', 'yyyy-mm-dd') < to_date(tran_date, 'yyyy-mm-dd') "
						+"				and trade_status ='1'                                                       "
						+"				and reclaim_mode in ('AM', 'LM')                                           "
						+"				and bill_no = '"+bill_no+"'";
					IndexedCollection iColl = dao.queryList(modelId, null, condition, connection);
					if(iColl!=null && iColl.size() >0){
						for(int i=0;i<iColl.size(); i++){
							KeyedCollection temp = (KeyedCollection) iColl.get(i);
							temp.setDataValue("trade_status", "2");
							dao.update(temp, connection);
						}
					}
					//当删除的明细为归还本金，交易状态为生效时，更新信托台账的贷款余额
					if("1".equals(trade_status)){
						KeyedCollection accLoan = dao.queryAllDetail("AccLoan", bill_no, connection);
						BigDecimal loanBalance = BigDecimalUtil.replaceNull(accLoan.getDataValue("loan_balance").toString());//信托台账贷款余额						
						loanBalance = loanBalance.add(tran_amt);
						accLoan.setDataValue("loan_balance", loanBalance);
						dao.update(accLoan, connection);
					}
				}
			}
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("删除失败！失败记录条数:"+count);
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
