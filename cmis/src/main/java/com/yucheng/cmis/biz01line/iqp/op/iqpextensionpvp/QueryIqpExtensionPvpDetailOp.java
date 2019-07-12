package com.yucheng.cmis.biz01line.iqp.op.iqpextensionpvp;

import java.sql.Connection;

import org.quartz.utils.Key;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpExtensionPvpDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpExtensionPvp";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
				if(context.containsKey("restrictUsed")&&(context.getDataValue("restrictUsed").toString()).equals("false")){
					
				}else{
					RecordRestrict recordRestrict = this.getRecordRestrict(context);
					recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
				}
			}
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			/** add by lisj 2015-9-8  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			IndexedCollection iColl4PBMR = dao.queryList("PvpBizModifyRel", "where biz_serno='"+serno_value+"' and approve_status='997' order by update_time desc", connection);
			if(iColl4PBMR!=null && iColl4PBMR.size()>0){
				KeyedCollection temp = (KeyedCollection) iColl4PBMR.get(0);
				context.put("modify_rel_serno", (String) temp.getDataValue("modify_rel_serno"));
				context.put("modiflag", "yes");
			}else{
				context.put("modiflag", "no");
			}
			/** add by lisj 2015-9-8  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id",});
			String[] args=new String[] { "cus_id","cont_no","fount_bill_no"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","cont_no","bill_no"};
			String[] fieldName=new String[]{"cus_name","serno","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id"};
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
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
