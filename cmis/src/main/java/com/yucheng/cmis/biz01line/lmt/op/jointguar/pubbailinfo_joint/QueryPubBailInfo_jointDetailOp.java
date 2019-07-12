package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPubBailInfo_jointDetailOp  extends CMISOperation {
	
	private final String modelId = "PubBailInfo";
	

	private final String serno_name = "serno";
//	private final String cus_id_name = "cus_id";
	private final String bail_acct_no_name = "bail_acct_no";
	
	
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
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

//			String cus_id_value = null;
//			try {
//				cus_id_value = (String)context.getDataValue(cus_id_name);
//			} catch (Exception e) {}
//			if(cus_id_value == null || cus_id_value.length() == 0)
//				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			String bail_acct_no_value = null;
			try {
				bail_acct_no_value = (String)context.getDataValue(bail_acct_no_name);
			} catch (Exception e) {}
			if(bail_acct_no_value == null || bail_acct_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bail_acct_no_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			KeyedCollection kColl = new KeyedCollection();
			String modiflg ="";
			String modify_rel_serno ="";
			String qFlag="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("qFlag")){
				qFlag = (String) context.getDataValue("qFlag");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				pkMap.put("modify_rel_serno",modify_rel_serno);
				pkMap.put("bail_acct_no",bail_acct_no_value);
				if("BMH".equals(qFlag)){
					kColl = dao.queryDetail("PubBailInfoHis", pkMap, connection);
					kColl.setName(modelId);
				}else{
					kColl = dao.queryDetail("PubBailInfoTmp", pkMap, connection);
					kColl.setName(modelId);
				}
			}else{
				pkMap.put("serno",serno_value);
//				pkMap.put("cus_id",cus_id_value);
				pkMap.put("bail_acct_no",bail_acct_no_value);
				kColl = dao.queryDetail(modelId, pkMap, connection);

			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[]{"open_org"});
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
