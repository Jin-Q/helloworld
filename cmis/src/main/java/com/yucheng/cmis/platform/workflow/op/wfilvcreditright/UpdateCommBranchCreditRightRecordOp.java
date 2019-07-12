package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*@version v1.0
*
 */
public class UpdateCommBranchCreditRightRecordOp extends CMISOperation {
	

	private final String modelId = "WfiOrgLifeloanRel";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String cb_org_id = (String)kColl.getDataValue("org_id");
			IndexedCollection WLCR = dao.queryList("WfiLvCreditRight", "where org_id='"+cb_org_id+"' and right_type='03'", connection);
			if(WLCR!=null && WLCR.size()>0){
				String pk_id ="";
				if(kColl.containsKey("pk_id")){
					pk_id = (String) kColl.getDataValue("pk_id");//是否存在配置信息
				}
				if(pk_id==null ||"".equals(pk_id)){
					dao.insert(kColl, connection);
				}else{
					int count=dao.update(kColl, connection);
					if(count!=1){
						throw new EMPException("Update Failed! Record Count: " + count);
					}
				}
				context.put("flag", PUBConstant.SUCCESS);
			}else{
				context.put("flag", "inexistence");
			}
			if("success".equals((String)context.getDataValue("flag"))){
				//写入操作记录表
				try {
					KeyedCollection  wfiLCRLog = new KeyedCollection();
					String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
					String op_time = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
					wfiLCRLog.put("operation_id", (String)kColl.getDataValue("pk_id"));
					wfiLCRLog.put("operation_org_id",(String)context.getDataValue("organNo"));
					wfiLCRLog.put("operation_staff", currentUserId);
					wfiLCRLog.put("operation_date", op_time);
					wfiLCRLog.put("right_type", "03");
					wfiLCRLog.put("op_type", "update4CB");//操作类型
					wfiLCRLog.setName("WfiLcrLog");
					dao.insert(wfiLCRLog, connection);
				} catch (Exception e) {
					context.put("flag", "writelogEx");
				}
			}
		}catch (EMPException ee) {
			context.put("flag", PUBConstant.FAIL);
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
