package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpCusAcctRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpCusAcct";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.setDataValue("pk_id", pk_id);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			String cont_no ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String) context.getDataValue("cont_no");
			}
			kColl.put("acct_no", kColl.getDataValue("acct_no").toString().trim());
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  begin**/
			String serno  = (String) kColl.getDataValue("serno");
			KeyedCollection LoanInfo = dao.queryDetail("IqpLoanAppSub", serno, connection);
			String isCloseLoan  = (String) LoanInfo.getDataValue("is_close_loan");
			KeyedCollection kColl4CLCT = dao.queryDetail("CtrLoanContTmp", modify_rel_serno, connection);
			if("1".equals(isCloseLoan)){
				String acct_attr = (String) kColl.getDataValue("acct_attr");
				if("01".equals(acct_attr)){
					context.put("flag", "acctError");
				}else{
					if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
						kColl.setName("IqpCusAcctTmp");
						kColl.put("modify_rel_serno", modify_rel_serno);
						kColl.put("cont_no", (String)kColl4CLCT.getDataValue("cont_no"));
						int m =dao.insert(kColl, connection);
						if(m==1){
							context.addDataField("flag", "success");
						}
					}else{
						int m =dao.insert(kColl, connection);
						if(m==1){
							context.addDataField("flag", "success");
						}	
					}
					
				}
			}else{
				if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
					kColl.setName("IqpCusAcctTmp");
					kColl.put("modify_rel_serno", modify_rel_serno);
					kColl.put("cont_no", (String)kColl4CLCT.getDataValue("cont_no"));
					int m =dao.insert(kColl, connection);
					if(m==1){
						context.addDataField("flag", "success");
					}
				}else{
					int m =dao.insert(kColl, connection);
					if(m==1){
						context.addDataField("flag", "success");
					}	
				}
			}
			/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  end**/
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
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
