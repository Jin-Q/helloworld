package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpAccAccpIqpAccpDetailDetailOp  extends CMISOperation {

	private final String modelId = "IqpAccpDetail";


	private final String pk1_name = "pk1";
	

	private boolean updateCheck = false;


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");
			
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String cont="";
			if(context.containsKey("cont")){
				cont = (String)context.getDataValue("cont");
			}
			
			String modify_rel_serno="";
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl =new KeyedCollection();
			if(!"".equals(cont) && "modify".equals(cont)){
				kColl = dao.queryFirst("IqpAccpDetailTmp", null, " where pk1 ='"+pk1_value+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				kColl.setName(modelId);
			}else{
				kColl = dao.queryDetail(modelId, pk1_value, connection);
			}
			this.putDataElement2Context(kColl, context);
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
