package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpAppendTermsDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAppendTerms";
	

	private final String serno_name = "append_terms_pk";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String append_terms_pk = null;
			String prd_id = null;
			try {
				append_terms_pk = (String)context.getDataValue("append_terms_pk");
				prd_id = (String)context.getDataValue("prd_id");
			} catch (Exception e) {}
			if(append_terms_pk == null || append_terms_pk.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno="";
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
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl =new KeyedCollection();
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				if("BMH".equals(qFlag)){
					kColl = dao.queryFirst("IqpAppendTermsHis", null, " where append_terms_pk ='"+append_terms_pk+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
					kColl.setName("IqpAppendTerms");
				}else{
					kColl = dao.queryFirst("IqpAppendTermsTmp", null, " where append_terms_pk ='"+append_terms_pk+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
					kColl.setName("IqpAppendTerms");	
				}
				
			}else{
				kColl = dao.queryDetail(modelId, append_terms_pk, connection);
			}		
			this.putDataElement2Context(kColl, context);
			//获取可用费用
			KeyedCollection kCollPrd = dao.queryDetail("PrdBasicinfo", prd_id, connection);
            String canFeeCode = (String)kCollPrd.getDataValue("canFeeCode");
	    	context.addDataField("canFeeCode", canFeeCode);
	    	/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
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
