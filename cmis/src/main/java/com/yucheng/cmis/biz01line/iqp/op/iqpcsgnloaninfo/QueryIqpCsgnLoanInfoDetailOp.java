package com.yucheng.cmis.biz01line.iqp.op.iqpcsgnloaninfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpCsgnLoanInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpCsgnLoanInfo";
	private final String serno_name = "serno";
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno_value = null;
			String contno_value = null;
			try {
				if(context.containsKey(serno_name)){
					serno_value = (String)context.getDataValue(serno_name);
				}else{
					contno_value = (String)context.getDataValue("cont_no");
					if("".equals(serno_value) || serno_value ==null){//出账
					   //通过合同编号去取合同表中取业务编号。	
						PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance("PvpComponent", context, connection);
						serno_value = cmisComponent.getSernoByContNo(contno_value);
					}
				}
			} catch (Exception e) {
			}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String[] args=new String[] { "csgn_cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    
		    KeyedCollection kColl4iqp = dao.queryDetail("IqpLoanAppSub", serno_value, connection);
			if(kColl4iqp!=null&&kColl4iqp.getDataValue("principal_loan_typ")!=null&&("08".equals(kColl4iqp.getDataValue("principal_loan_typ"))||"14".equals(kColl4iqp.getDataValue("principal_loan_typ")))){
				context.put("flag", "1");
			}
			
			this.putDataElement2Context(kColl, context);
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
