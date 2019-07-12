package com.yucheng.cmis.biz01line.pvp.op.pvprpddscant;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPvpRpddscantDetailOp  extends CMISOperation {
	
	private final String modelId = "PvpLoanApp";
	private final String ContmodelId = "CtrRpddscntCont";
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
			String cont_no = "";
			String flag ="pvp";
			try {
				if(context.containsKey("flag")){  
					flag = (String)context.getDataValue("flag");
				}else{
					context.addDataField("flag", flag); 
				}
				if(context.containsKey(serno_name)){  
					serno_value = (String)context.getDataValue(serno_name);
				}	
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");	
			
			
			String[] args=new String[] { "prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] fieldName=new String[]{"prdname"};
			String[] modelForeign=new String[]{"prdid"};
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			cont_no = kColl.getDataValue("cont_no").toString();
			KeyedCollection contKColl = dao.queryDetail(ContmodelId, cont_no, connection);
			kColl.addDataField("toorg_name",(String)contKColl.getDataValue("toorg_name"));
			
			//详细信息翻译时调用	
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			
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
