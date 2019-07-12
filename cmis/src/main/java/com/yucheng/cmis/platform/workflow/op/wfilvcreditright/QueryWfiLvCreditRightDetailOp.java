package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*@version v1.0
*
 */
public class QueryWfiLvCreditRightDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiLvCreditRight";
	private final String pk_id_name = "pk_id";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);	
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
	
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			String right_type = (String) kColl.getDataValue("right_type");
			//对社区支行进行特殊处理
			/**if("03".equals(right_type)){
				String org_id = (String)kColl.getDataValue("org_id");
				KeyedCollection temp = dao.queryFirst("WfiOrgCbRel", null, "where comm_branch_id='"+org_id+"'", connection);
				kColl.addDataField("cb_org_name", (String)temp.getDataValue("comm_branch_name"));
			}**/
			this.putDataElement2Context(kColl, context);
			//分/支行配置信息
			if("02".equals(right_type)){
				return "1";
			}
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
