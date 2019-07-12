package com.yucheng.cmis.biz01line.psp.op.pspcapusemonitor;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class CheckAccLoanForPspCapOp extends CMISOperation {
//	private final String modelId = "PspCapUseMonitor";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = (String) context.getDataValue("cus_id");
			String bill_no = (String) context.getDataValue("bill_no");
			double disb_amt = Double.parseDouble(context.getDataValue("disb_amt")+"") ;
			if(cus_id==null){
				throw new EMPException("获取不到客户编号！");
			}
			if(bill_no==null){
				throw new EMPException("获取不到借据编号！");
			}
			IndexedCollection iColl ;
			
			String sql = "";
			/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 start */
			if(context.containsKey("pk_id")){
				/**modified by lisj 2014-12-3 修改SQL语句可获取产品编号 begin**/
				sql = "select nvl((select bill_amt from psp_acc_view where bill_no = '"+bill_no+"'),0) - nvl(p_amt ,0) as psp_amt,"
				+"(select prd_id from psp_acc_view where bill_no = '"+bill_no+"') prd_id "
				+ " from (select sum(disb_amt)p_amt from Psp_Cap_Use_Monitor p"
				+ " where cus_id = '"+cus_id+"' and bill_no = '"+bill_no+"' and pk_id != '"+context.getDataValue("pk_id")+"')";
			}else{
				sql = "select nvl((select bill_amt from psp_acc_view where bill_no = '"+bill_no+"'),0) - nvl(p_amt ,0) as psp_amt,"
				+"(select prd_id from psp_acc_view where bill_no = '"+bill_no+"') prd_id "
				+ " from (select sum(disb_amt)p_amt from Psp_Cap_Use_Monitor p"
				+ " where cus_id = '"+cus_id+"' and bill_no = '"+bill_no+"')";
				/**modified by lisj 2014-12-3 修改SQL语句可获取产品编号 end**/
			}
			/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 end */
			
			iColl = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
			/**modified by lisj 2014-12-3 修改金额判断条件，当产品为【商业承兑汇票贴现】或者【银行承兑汇票贴现】不校验用款金额  begin**/
			if(iColl.size()>0){				
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
				double psp_amt = Double.parseDouble(kColl.getDataValue("psp_amt")+"") ;
				String prd_id = (String) kColl.getDataValue("prd_id");
				if(prd_id!=null && !(prd_id.equals("300020") || prd_id.equals("300021"))){
					if(disb_amt > psp_amt){
							context.addDataField("flag", psp_amt);
						}else{
							context.addDataField("flag", "success");
						}	
			 	}else{
			 		context.addDataField("flag", "success");
			 	}				
			}else{
				context.addDataField("flag", "success");
			}	
			/**modified by lisj 2014-12-3 修改金额判断条件，当产品为【商业承兑汇票贴现】或者【银行承兑汇票贴现】不校验用款金额  end**/
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
