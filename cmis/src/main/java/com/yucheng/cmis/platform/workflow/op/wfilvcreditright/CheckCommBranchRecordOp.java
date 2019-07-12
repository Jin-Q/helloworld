package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 
*@author lisj
*@time 2015-4-3
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置 
*							     校验机构是否存在社区支行权限配置信息
*@version v1.0
*
 */
public class CheckCommBranchRecordOp extends CMISOperation {

private final String modelId = "WfiOrgLifeloanRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String org_id  = (String) context.getDataValue("cb_org_id");
            IndexedCollection WOLR  = dao.queryList(modelId, "where org_id='"+org_id+"'", connection);
            if(WOLR!=null && WOLR.size()>0){
            	context.put("flag", "exist");
            }else{
            	context.put("flag", PUBConstant.SUCCESS);
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
