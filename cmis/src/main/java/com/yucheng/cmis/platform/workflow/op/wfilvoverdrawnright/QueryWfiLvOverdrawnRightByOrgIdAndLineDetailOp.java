package com.yucheng.cmis.platform.workflow.op.wfilvoverdrawnright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
/**
 * 
*@author wangj
*@time 2015-5-27
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class QueryWfiLvOverdrawnRightByOrgIdAndLineDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiLvOverdrawnRight";
	
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
			
			String orgId  = (String)kColl.getDataValue("org_id");//获取配置机构码
			String belg_line  = (String)kColl.getDataValue("belg_line");//获取是否控制
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection WLOR = dao.queryList(modelId, " where org_id='"+orgId+"' and belg_line='"+belg_line+"' ", connection);
			if (WLOR != null && WLOR.size() > 0) {
				KeyedCollection kColl2 = (KeyedCollection) WLOR.get(0);
				this.putDataElement2Context(kColl2, context);
				SInfoUtils.addSOrgName(kColl2, new String[]{"org_id"});
			}else{
				this.putDataElement2Context(kColl, context);
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
